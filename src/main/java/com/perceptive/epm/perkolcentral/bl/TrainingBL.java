package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import sun.util.calendar.CalendarUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 23/11/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingBL {

    private LinkedHashMap<Date, String> trainingSchedule = new LinkedHashMap<Date, String>();
    private LinkedHashMap<String, Integer> masterSchedule = new LinkedHashMap<String, Integer>();
    private HashMap<Date, String> holidayList = new HashMap<Date, String>();
    private HtmlEmail email;
    private EmployeeBL employeeBL;
    private String configFilePath;
    private String employeeName;

    public HtmlEmail getEmail() {
        return email;
    }

    public void setEmail(HtmlEmail email) {
        this.email = email;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void initTraining() throws ExceptionWrapper {
        try {
            for (String line : FileUtils.readLines(new File(String.format("%s//GeneralTrainingSchedule.config", configFilePath)))) {
                masterSchedule.put(line.split("##")[0], Integer.valueOf(line.split("##")[1]));
            }
            for (String line : FileUtils.readLines(new File(String.format("%s//PerKolHolidayList.config", configFilePath)))) {
                holidayList.put(new SimpleDateFormat("dd-MM-yyyy").parse(line.split(":")[0]), line.split(":")[1]);
            }


        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }

    }

    public String getLexmarkHolidayList() throws ExceptionWrapper {

        try {
            String listOfHolidays = StringUtils.EMPTY;
            for (Map.Entry<Date, String> holidayEntry : holidayList.entrySet()) {
                listOfHolidays = listOfHolidays + new SimpleDateFormat("dd-MM-yyyy").format(holidayEntry.getKey()) + ":" + holidayEntry.getValue() + "#";
            }
            return listOfHolidays.substring(0, listOfHolidays.length() - 1);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public String createTrainingCalendar(String startDate, String[] excludedEvents, Long employeeId, Long buddyId, Boolean sendMail, Boolean printCalenDar) throws ExceptionWrapper {
        String events = StringUtils.EMPTY;
        try {
            Calendar cal = Calendar.getInstance();

            Date startDateForTrng = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
            HashMap<Date, String> excludedDatesForTrng = new HashMap<Date, String>();
            //Populate Excluded dates
            if (excludedEvents != null) {
                for (String excludedDateAndEvent : excludedEvents) {
                    excludedDatesForTrng.put(new SimpleDateFormat("dd-MM-yyyy").parse(excludedDateAndEvent.split("~~")[0]), excludedDateAndEvent.split("~~")[1]);
                }
            }
            Date trainingScheduledUptoThisDate = startDateForTrng;
            for (Map.Entry<String, Integer> mapEntry : masterSchedule.entrySet()) {
                for (int iCounter = 0; iCounter < mapEntry.getValue(); iCounter++) {
                    cal.setTime(trainingScheduledUptoThisDate);
                    while (holidayList.containsKey(trainingScheduledUptoThisDate) ||
                            excludedDatesForTrng.containsKey(trainingScheduledUptoThisDate) ||
                            cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                            cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        if (holidayList.containsKey(trainingScheduledUptoThisDate)) {
                            trainingSchedule.put(trainingScheduledUptoThisDate, "Holiday -- " + holidayList.get(trainingScheduledUptoThisDate));
                        }
                        if (excludedDatesForTrng.containsKey(trainingScheduledUptoThisDate)) {
                            trainingSchedule.put(trainingScheduledUptoThisDate, excludedDatesForTrng.get(trainingScheduledUptoThisDate));
                        }
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                                cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            trainingSchedule.put(trainingScheduledUptoThisDate, "Holiday (Week End)");
                        }
                        trainingScheduledUptoThisDate = DateUtils.addDays(trainingScheduledUptoThisDate, 1);
                        cal.setTime(trainingScheduledUptoThisDate);
                    }
                    trainingSchedule.put(trainingScheduledUptoThisDate, mapEntry.getKey());
                    trainingScheduledUptoThisDate = DateUtils.addDays(trainingScheduledUptoThisDate, 1);
                }
            }

            for (Map.Entry<Date, String> mapEntry : excludedDatesForTrng.entrySet()) {
                if (!trainingSchedule.containsKey(mapEntry.getKey()) && mapEntry.getKey().compareTo(startDateForTrng) > 0) {
                    trainingSchedule.put(mapEntry.getKey(), mapEntry.getValue());
                }
            }
            //Add extra events those which are not already added
            for (Map.Entry<Date, String> eventEntry : trainingSchedule.entrySet()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventEntry.getKey());
                String event = "{title:'" + eventEntry.getValue() + "',start:new Date(" + calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DATE) + "),allDay: true}";
                events = events + event + ",";
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        if (!events.trim().equalsIgnoreCase(StringUtils.EMPTY))
            events = events.substring(0, events.length() - 1);
        if (trainingSchedule.size() > 0 && sendMail)
            sendTrainingScheduleViaMail(employeeId, buddyId);
        if (trainingSchedule.size() > 0 && printCalenDar)
            events = events + "<SEPARATOR>" + printCalendar(employeeId, buddyId);

        return events;
    }

    private String printCalendar(Long employeeId, Long buddyId) throws ExceptionWrapper {
        String trainingCalendar = StringUtils.EMPTY;
        try {
            String fullEmail =
                    "<table id=\"id_PrintScheduleTable\" width=\"80%\" border=\"1\" cellspacing=\".5\" bordercolor=\"#000000\" style=\" font:Georgia;font - size:10 px;\">\n" +
                            "<tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\" colspan=\"7\" align=\"left\">Trainee Name:<em><NEWEMP></em>&nbsp;&nbsp;&nbsp;&nbsp;Buddy Name:<em><BUDDY></em></th>    \n" +
                            "  </tr>" +
                            "  <tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Sunday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Monday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Tuesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Wednesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Thursday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Friday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Saturday</th>\n" +
                            "  </tr>\n" +
                            "<LINE_ITEM>\n" +
                            "</table>";
            String trainingLineItem = " <tr>\n" +
                    "    <td bgcolor=\"#999966\" valign=\"top\">Sun</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Mon</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Tue</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Wed</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Thu</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Fri</td>\n" +
                    "    <td bgcolor=\"#999966\" valign=\"top\">Sat</td>\n" +
                    "  </tr>\n";
            String freshTrainingLineItemToAdd = (String) SerializationUtils.clone(trainingLineItem);
            String trngItemInEmail = StringUtils.EMPTY;
            ArrayList<Date> trainingDates = new ArrayList<Date>(trainingSchedule.keySet());
            Collections.sort(trainingDates);
            for (Date trngDate : trainingDates) {
                freshTrainingLineItemToAdd = freshTrainingLineItemToAdd.replace(new SimpleDateFormat("E").format(trngDate), "<b><i>" + new SimpleDateFormat("dd-MM-yyyy").format(trngDate) + "</i></b><br/>" + trainingSchedule.get(trngDate));
                if (new SimpleDateFormat("E").format(trngDate).equalsIgnoreCase("Sat")) {
                    trngItemInEmail = trngItemInEmail + (String) SerializationUtils.clone(freshTrainingLineItemToAdd);
                    freshTrainingLineItemToAdd = (String) SerializationUtils.clone(trainingLineItem);
                }

            }
            trngItemInEmail = trngItemInEmail + (String) SerializationUtils.clone(freshTrainingLineItemToAdd);
            trngItemInEmail = trngItemInEmail.replaceAll("Sun", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Mon", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Tue", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Wed", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Thu", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Fri", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Sat", "&nbsp;");
            fullEmail = fullEmail.replaceAll("<NEWEMP>", employeeName);
            fullEmail = fullEmail.replaceAll("<BUDDY>", employeeBL.getAllEmployees().get(buddyId).getEmployeeName());
            fullEmail = fullEmail.replaceAll("<LINE_ITEM>", trngItemInEmail);
            trainingCalendar = fullEmail;
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return trainingCalendar;
    }

    private void sendTrainingScheduleViaMail(Long employeeId, Long buddyId) throws ExceptionWrapper {
        try {
            String fullEmail = (employeeId != -1) ?
                    "<html>\n" +
                            "<head>\n" +
                            "</head>\n" +
                            "\n" +
                            "<body style=\"font:Georgia; font-size:12px;\">\n" +
                            "<p>Hello <b><NEWEMP></b>,</p>\n" +
                            "<p>Please find your training schedule below.Your buddy ,<b><BUDDY></b> will help you out in going through the training schedule.Please revert back to your buddy or Development Manager or Scrum Masters if you have questions or queries.</p>\n" +
                            "<p>Thanks,</p>\n" +
                            "<p>Training Schedule Creation Agent</p>\n" +
                            "<p>(Perceptive Kolkata Central)</p>\n" +
                            "<table width=\"80%\" border=\"1\" cellspacing=\".5\" bordercolor=\"#000000\">\n" +
                            "<tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\" colspan=\"7\" align=\"left\">Trainee Name:<em><NEWEMP></em>&nbsp;&nbsp;&nbsp;&nbsp;Buddy Name:<em><BUDDY></em></th>    \n" +
                            "  </tr>" +
                            "  <tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Sunday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Monday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Tuesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Wednesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Thursday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Friday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Saturday</th>\n" +
                            "  </tr>\n" +
                            "<LINE_ITEM>\n" +
                            "</table>" +
                            "<p>&nbsp;</p>\n" +
                            "</body>\n" +
                            "</html>"
                    :
                    "<html>\n" +
                            "<head>\n" +
                            "</head>\n" +
                            "\n" +
                            "<body style=\"font:Georgia; font-size:12px;\">\n" +
                            "<p>Hello <b><BUDDY></b>,</p>\n" +
                            "<p>Please find the training schedule for <b><NEWEMP></b> below.Please help him/her out in going through the training schedule.Please revert back to your Development Manager or Scrum Masters if you have questions or queries.</p>\n" +
                            "<p>Thanks,</p>\n" +
                            "<p>Training Schedule Creation Agent</p>\n" +
                            "<p>(Perceptive Kolkata Central)</p>\n" +
                            "<table width=\"80%\" border=\"1\" cellspacing=\".5\" bordercolor=\"#000000\">\n" +
                            "<tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\" colspan=\"7\" align=\"left\">Trainee Name:<em><NEWEMP></em>&nbsp;&nbsp;&nbsp;&nbsp;Buddy Name:<em><BUDDY></em></th>    \n" +
                            "  </tr>" +
                            "  <tr>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Sunday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Monday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Tuesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Wednesday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Thursday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Friday</th>\n" +
                            "    <th bgcolor=\"#0099CC\" scope=\"col\">Saturday</th>\n" +
                            "  </tr>\n" +
                            "<LINE_ITEM>\n" +
                            "</table>" +
                            "<p>&nbsp;</p>\n" +
                            "</body>\n" +
                            "</html>";

            String trainingLineItem = " <tr>\n" +
                    "    <td bgcolor=\"#999966\" valign=\"top\">Sun</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Mon</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Tue</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Wed</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Thu</td>\n" +
                    "    <td bgcolor=\"#FFFF66\" valign=\"top\">Fri</td>\n" +
                    "    <td bgcolor=\"#999966\" valign=\"top\">Sat</td>\n" +
                    "  </tr>\n";

            String freshTrainingLineItemToAdd = (String) SerializationUtils.clone(trainingLineItem);
            String trngItemInEmail = StringUtils.EMPTY;
            HtmlEmail emailToSend = new HtmlEmail();
            emailToSend.setHostName(email.getHostName());
            ArrayList<Date> trainingDates = new ArrayList<Date>(trainingSchedule.keySet());
            Collections.sort(trainingDates);
            for (Date trngDate : trainingDates) {
                freshTrainingLineItemToAdd = freshTrainingLineItemToAdd.replace(new SimpleDateFormat("E").format(trngDate), "<b><i>" + new SimpleDateFormat("dd-MM-yyyy").format(trngDate) + "</i></b><br/>" + trainingSchedule.get(trngDate));
                if (new SimpleDateFormat("E").format(trngDate).equalsIgnoreCase("Sat")) {
                    trngItemInEmail = trngItemInEmail + (String) SerializationUtils.clone(freshTrainingLineItemToAdd);
                    freshTrainingLineItemToAdd = (String) SerializationUtils.clone(trainingLineItem);
                }

            }
            trngItemInEmail = trngItemInEmail + (String) SerializationUtils.clone(freshTrainingLineItemToAdd);
            trngItemInEmail = trngItemInEmail.replaceAll("Sun", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Mon", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Tue", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Wed", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Thu", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Fri", "&nbsp;");
            trngItemInEmail = trngItemInEmail.replaceAll("Sat", "&nbsp;");
            /*for (Map.Entry<Date, String> eventEntry : trainingSchedule.entrySet()) {
                String trngItemInEmail = String.format(trainingLineItem, new SimpleDateFormat("dd-MM-yyyy").format(eventEntry.getKey()), new SimpleDateFormat("E").format(eventEntry.getKey()), eventEntry.getValue());
                allTrainingItems.add(trngItemInEmail);
            }  */
            fullEmail = fullEmail.replaceAll("<NEWEMP>", employeeName);
            fullEmail = fullEmail.replaceAll("<BUDDY>", employeeBL.getAllEmployees().get(buddyId).getEmployeeName());
            fullEmail = fullEmail.replaceAll("<LINE_ITEM>", trngItemInEmail);
            //fullEmail = String.format(fullEmail, employeeBL.getAllEmployees().get(employeeId).getEmployeeName(), employeeBL.getAllEmployees().get(buddyId).getEmployeeName(), StringUtils.join(allTrainingItems.toArray()));
            //emailToSend.setFrom("PerceptiveKolkataCentral@perceptivesoftware.com", "Perceptive Kolkata Central");
            emailToSend.setFrom("EnterpriseSoftwareKolkata@lexmark.com", "Enterprise Software Kolkata");
            emailToSend.setSubject(String.format("New Employee Training Schedule for %s", employeeName));
            emailToSend.setHtmlMsg(fullEmail);
            if (employeeId != -1)
                emailToSend.addTo(employeeBL.getAllEmployees().get(employeeId).getEmail(), employeeBL.getAllEmployees().get(employeeId).getEmployeeName());
            else
                emailToSend.addTo(employeeBL.getAllEmployees().get(buddyId).getEmail(), employeeBL.getAllEmployees().get(buddyId).getEmployeeName());
            if (employeeId != -1)
                emailToSend.addCc(employeeBL.getAllEmployees().get(buddyId).getEmail(), employeeBL.getAllEmployees().get(buddyId).getEmployeeName());
            //Send mail to scrum masters
            for (EmployeeBO item : employeeBL.getAllEmployeesKeyedByGroupId().get(Integer.valueOf("14"))) {
                emailToSend.addCc(item.getEmail(), item.getEmployeeName());
            }
            //Add his managers email oid so that he/she gets a notification
            if (employeeId != -1)
                emailToSend.addCc(employeeBL.getAllEmployees().get(employeeId).getManagerEmail(),employeeBL.getAllEmployees().get(employeeId).getManager());
            //emailToSend.addTo("saibal.ghosh@perceptivesoftware.com");
            //Adding the attachment for ImageNow Training VM Instructions
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(new File(String.format("%s//INow_Training_Using_VM.pdf",configFilePath)).getAbsolutePath());
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("INow Training VM - Instructions");
            attachment.setName("INow_Training_Using_VM.pdf");
            emailToSend.attach(attachment);
            emailToSend.send();

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }

    }
}
