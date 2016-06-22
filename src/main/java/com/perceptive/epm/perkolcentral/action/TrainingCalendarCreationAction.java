package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.TrainingBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 23/11/12
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingCalendarCreationAction extends ActionSupport {
    private String trainingStartDate;
    private String employeeId;
    private String buddyEmployeeId;
    private String nameOfEmployee;
    private String nameOfBuddy;
    private TrainingBL trainingBL;
    private String eventDetail;
    private String[] excludedEvents;
    private String excludedEventsConcatenatedString;
    private Boolean sendMail;

    private String dupsub = StringUtils.EMPTY;
    private String errorMessage = StringUtils.EMPTY;
    private String lexmarkHolidayList = StringUtils.EMPTY;
    private String calendarPrint = StringUtils.EMPTY;

    public String getTrainingStartDate() {
        return trainingStartDate;
    }

    public void setTrainingStartDate(String trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBuddyEmployeeId() {
        return buddyEmployeeId;
    }

    public void setBuddyEmployeeId(String buddyEmployeeId) {
        this.buddyEmployeeId = buddyEmployeeId;
    }


    public void setTrainingBL(TrainingBL trainingBL) {
        this.trainingBL = trainingBL;
    }

    public String getNameOfEmployee() {
        return nameOfEmployee;
    }

    public void setNameOfEmployee(String nameOfEmployee) {
        this.nameOfEmployee = nameOfEmployee;
    }

    public String getNameOfBuddy() {
        return nameOfBuddy;
    }

    public void setNameOfBuddy(String nameOfBuddy) {
        this.nameOfBuddy = nameOfBuddy;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String[] getExcludedEvents() {
        return excludedEvents;
    }

    public void setExcludedEvents(String[] excludedEvents) {
        this.excludedEvents = excludedEvents;
    }

    public String getExcludedEventsConcatenatedString() {
        return StringUtils.join(excludedEvents, "^^");
    }

    public void setExcludedEventsConcatenatedString(String excludedEventsConcatenatedString) {
        this.excludedEventsConcatenatedString = excludedEventsConcatenatedString;
    }

    public Boolean getSendMail() {
        return sendMail;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public String getDupsub() {
        return dupsub;
    }

    public void setDupsub(String dupsub) {
        this.dupsub = dupsub;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLexmarkHolidayList() {
        return lexmarkHolidayList;
    }

    public void setLexmarkHolidayList(String lexmarkHolidayList) {
        this.lexmarkHolidayList = lexmarkHolidayList;
    }



    public String getCalendarPrint() {
        return calendarPrint;
    }

    public void setCalendarPrint(String calendarPrint) {
        this.calendarPrint = calendarPrint;
    }

    public String executeViewTrngCal() throws ExceptionWrapper {
        try {
            lexmarkHolidayList = trainingBL.getLexmarkHolidayList();
            if (StringUtils.isNotEmpty(dupsub)) {
                errorMessage = errorMessage + "* You are trying to re-submit a form that you have already submitted.Please wait for some time." + IOUtils.LINE_SEPARATOR;
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeCreateTrngCal() throws ExceptionWrapper {
        try {
            lexmarkHolidayList = trainingBL.getLexmarkHolidayList();
            trainingBL.setEmployeeName(nameOfEmployee);
            eventDetail = trainingBL.createTrainingCalendar(trainingStartDate, excludedEvents, Long.valueOf(employeeId), Long.valueOf(buddyEmployeeId), sendMail, true);
            calendarPrint = eventDetail.split("<SEPARATOR>")[1];
            eventDetail = eventDetail.split("<SEPARATOR>")[0];

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
