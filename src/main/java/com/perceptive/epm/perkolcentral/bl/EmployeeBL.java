package com.perceptive.epm.perkolcentral.bl;

import com.googlecode.ehcache.annotations.*;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import com.perceptive.epm.perkolcentral.common.utils.LuceneUtil;
import com.perceptive.epm.perkolcentral.dataaccessor.EmployeeDataAccessor;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employee;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employeegroupmap;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employeelicensemapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 16/8/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeBL {
    private EmployeeDataAccessor employeeDataAccessor;
    private HtmlEmail email;
    private GroupsBL groupsBL;
    private LuceneUtil luceneUtil;


    public void setEmployeeDataAccessor(EmployeeDataAccessor employeeDataAccessor) {
        this.employeeDataAccessor = employeeDataAccessor;
    }

    public void setEmail(HtmlEmail email) {
        this.email = email;
    }

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public void setLuceneUtil(LuceneUtil luceneUtil) {
        this.luceneUtil = luceneUtil;
    }

    public LuceneUtil getLuceneUtil() {
        return luceneUtil;
    }

    @Cacheable(cacheName = "EmployeeKeyedByGroupCache", keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public LinkedHashMap<Integer, ArrayList<EmployeeBO>> getAllEmployeesKeyedByGroupId() throws ExceptionWrapper {
        LinkedHashMap<Integer, ArrayList<EmployeeBO>> employeeBOLinkedHashMap = new LinkedHashMap<Integer, ArrayList<EmployeeBO>>();
        Collection<EmployeeBO> employeeBOCollection = this.getAllEmployees().values();
        try {
            for (Object item : groupsBL.getAllGroups().values()) {
                final GroupBO groupBO = (GroupBO) item;
                ArrayList<EmployeeBO> employeeBOArrayList = new ArrayList<EmployeeBO>(employeeBOCollection);
                CollectionUtils.filter(employeeBOArrayList, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return CollectionUtils.exists(((EmployeeBO) o).getGroups(), new Predicate() {
                            @Override
                            public boolean evaluate(Object o) {
                                return ((GroupBO) o).getGroupId().equalsIgnoreCase(groupBO.getGroupId());
                            }
                        });
                    }
                });
                employeeBOLinkedHashMap.put(Integer.valueOf(groupBO.getGroupId()), employeeBOArrayList);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return employeeBOLinkedHashMap;
    }

    public EmployeeBO getEmployeeByEmployeeUID(String employeeUID) throws ExceptionWrapper {
        try {
            ArrayList<EmployeeBO> employeeBOArrayList = new ArrayList<EmployeeBO>(getAllEmployees().values());
            final String UID = employeeUID;
            return (EmployeeBO) CollectionUtils.find(employeeBOArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((EmployeeBO) o).getEmployeeUid().trim().equalsIgnoreCase(UID.trim());  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Cacheable(cacheName = "EmployeeCache", keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public LinkedHashMap<Long, EmployeeBO> getAllEmployees() throws ExceptionWrapper {
        try {
            LinkedHashMap<Long, EmployeeBO> employeeBOLinkedHashMap = employeeDataAccessor.getAllEmployees();
            luceneUtil.indexUserInfo(employeeBOLinkedHashMap.values());
            return employeeBOLinkedHashMap;
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }


    @TriggersRemove(cacheName = {"EmployeeCache", "GroupCache", "EmployeeKeyedByGroupCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true, keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public void updateEmployeeGroupMap(String employeeId, ArrayList<String> groupIds) throws ExceptionWrapper {
        try {
            ArrayList<Employeegroupmap> employeegroupmapArrayList = employeeDataAccessor.getEmployeegroupmapByEmployeeID(employeeId);
            for (Object item : employeegroupmapArrayList) {
                final Employeegroupmap employeegroupmap = (Employeegroupmap) item;
                if (!CollectionUtils.exists(groupIds, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((String) o).trim().equalsIgnoreCase(employeegroupmap.getGroups().getGroupId().toString());
                    }
                })) {
                    employeeDataAccessor.deleteEmployeeGroupMap(employeegroupmap);
                }
            }
            for (Object item : groupIds) {
                final String groupId = (String) item;
                if (groupId.trim().equalsIgnoreCase(""))
                    continue;
                if (!CollectionUtils.exists(employeegroupmapArrayList, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((Employeegroupmap) o).getGroups().getGroupId().toString().equalsIgnoreCase(groupId.trim());
                    }
                })) {
                    employeeDataAccessor.addEmployeeGroupMap(employeeId, groupId);
                }
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @TriggersRemove(cacheName = {"EmployeeCache", "GroupCache", "EmployeeKeyedByGroupCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true, keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public void updateEmployeeLicenseMap(String employeeId, ArrayList<String> licenseIds) throws ExceptionWrapper {
        try {
            ArrayList<Employeelicensemapping> employeelicensemappingArrayList = employeeDataAccessor.getEmployeeLicenseMapByEmployeeId(employeeId);
            for (Object item : employeelicensemappingArrayList) {
                final Employeelicensemapping employeelicensemapping = (Employeelicensemapping) item;
                if (!CollectionUtils.exists(licenseIds, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((String) o).trim().equalsIgnoreCase(employeelicensemapping.getLicensemaster().getLicenseTypeId().toString());
                    }
                })) {
                    employeeDataAccessor.deleteEmployeeLicenseMap(employeelicensemapping);
                }
            }
            for (Object item : licenseIds) {
                final String licenseTypeId = (String) item;
                if (licenseTypeId.trim().equalsIgnoreCase(""))
                    continue;
                if (!CollectionUtils.exists(employeelicensemappingArrayList, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((Employeelicensemapping) o).getLicensemaster().getLicenseTypeId().toString().equalsIgnoreCase(licenseTypeId.trim());
                    }
                })) {
                    employeeDataAccessor.addEmployeeLicenseMap(employeeId, licenseTypeId);
                }
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @TriggersRemove(cacheName = {"EmployeeCache", "GroupCache", "EmployeeKeyedByGroupCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true, keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public void updateAllEmployee(List<EmployeeBO> employeeListFromLDAP) throws ExceptionWrapper {
        try {
            String messageTemplateAdded = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"font:Georgia; font-size:12px;\">\n" +
                    "<p>Dear All,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>A new user is added to the system.</p>\n" +
                    "</blockquote>\n" +
                    "<ul>\n" +
                    "  <li><strong><em>User Name</em></strong>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>User Short Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>Employee Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><strong><em>User Email-Id</em></strong>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Mobile Number</strong></em>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Job Title</strong></em> : <strong>%s</strong></li>\n" +
                    "</ul>\n" +
                    "<p>Please take necessary actions.</p>\n" +
                    "<p>Thanks,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>Perceptive Kolkata Central</p>\n" +
                    "</blockquote>\n" +
                    "</body>\n" +
                    "</html>";

            String messageTemplateDeleted = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"font:Georgia; font-size:12px;\">\n" +
                    "<p>Dear All,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>An  user is removed from the system.</p>\n" +
                    "</blockquote>\n" +
                    "<ul>\n" +
                    "  <li><strong><em>User Name</em></strong>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>User Short Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>Employee Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><strong><em>User Email-Id</em></strong>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Mobile Number</strong></em>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Job Title</strong></em> : <strong>%s</strong></li>\n" +
                    "</ul>\n" +
                    "<p>Please take necessary actions.</p>\n" +
                    "<p>Thanks,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>Perceptive Kolkata Central</p>\n" +
                    "</blockquote>\n" +
                    "</body>\n" +
                    "</html>";

            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = employeeDataAccessor.getAllEmployees();
            for (EmployeeBO employeeBO : employeeListFromLDAP) {
                if (!employeeLinkedHashMap.containsKey(Long.valueOf(employeeBO.getEmployeeId()))) {
                    //Add a new employee
                    Employee employee = new Employee();
                    employee.setEmployeeId(Long.parseLong(employeeBO.getEmployeeId()));
                    employee.setEmail(employeeBO.getEmail());
                    employee.setEmployeeName(employeeBO.getEmployeeName());
                    employee.setEmployeeUid(employeeBO.getEmployeeUid());
                    employee.setJobTitle(employeeBO.getJobTitle());
                    employee.setMobileNumber(employeeBO.getMobileNumber());
                    employee.setManager(employeeBO.getManager());
                    employee.setManagerEmail(employeeBO.getManagerEmail());
                    employee.setExtensionNum(employeeBO.getExtensionNum());
                    employee.setWorkspace(employeeBO.getWorkspace());
                    employee.setIsActive(true);
                    employeeDataAccessor.addEmployee(employee);
                    LoggingHelpUtil.printDebug(String.format("Adding user ----> %s with details %s %s", employeeBO.getEmployeeName(), System.getProperty("line.separator"), ReflectionToStringBuilder.toString(employeeBO)));
                    //Send the mail
                    HtmlEmail emailToSend = new HtmlEmail();
                    emailToSend.setHostName(email.getHostName());
                    String messageToSend = String.format(messageTemplateAdded, employeeBO.getEmployeeName(),
                            employeeBO.getEmployeeUid(),
                            employeeBO.getEmployeeId().toString(),
                            employeeBO.getEmail(),
                            employeeBO.getMobileNumber(),
                            employeeBO.getJobTitle());

                    emailToSend.setHtmlMsg(messageToSend);
                    //emailToSend.setTextMsg(StringEscapeUtils.escapeHtml(messageToSend));
                    emailToSend.getToAddresses().clear();
                    //Send mail to scrum masters and Development Managers Group Id 15
                    Collection<EmployeeBO> allEmployeesNeedToGetMail = CollectionUtils.union(getAllEmployeesKeyedByGroupId().get(Integer.valueOf("14")), getAllEmployeesKeyedByGroupId().get(Integer.valueOf("15")));
                    for (EmployeeBO item : allEmployeesNeedToGetMail) {
                        emailToSend.addTo(item.getEmail(), item.getEmployeeName());
                    }
                    emailToSend.addTo(employeeBO.getManagerEmail(),employeeBO.getManager());//Send the mail to manager
                    //emailToSend.setFrom("PerceptiveKolkataCentral@perceptivesoftware.com", "Perceptive Kolkata Central");
                    emailToSend.setFrom("EnterpriseSoftwareKolkata@lexmark.com", "Enterprise Software Kolkata");
                    emailToSend.setSubject(String.format("New employee added : %s", employeeBO.getEmployeeName()));
                    emailToSend.send();
                    //==========================Mail send ends here===========================================================================================================
                    sendMailToPerceptiveOpsTeam(employeeBO);//Send mail to operations team in Shawnee
                } else {
                    //Update a new employee
                    employeeDataAccessor.updateEmployee(employeeBO);
                    LoggingHelpUtil.printDebug(String.format("Updating user ----> %s with details %s %s", employeeBO.getEmployeeName(), System.getProperty("line.separator"), ReflectionToStringBuilder.toString(employeeBO)));
                }

                //========================================================================================================================================
            }
            //Delete employees if any
            for (Object obj : employeeLinkedHashMap.values()) {
                final EmployeeBO emp = (EmployeeBO) obj;
                if (!CollectionUtils.exists(employeeListFromLDAP, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return emp.getEmployeeId().trim().equalsIgnoreCase(((EmployeeBO) o).getEmployeeId().trim());  //To change body of implemented methods use File | Settings | File Templates.
                    }
                })) {
                    emp.setActive(false); //Soft delete the Employee
                    employeeDataAccessor.updateEmployee(emp);//Rest deletion will be taken care by the Trigger AFTER_EMPLOYEE_UPDATE
                    LoggingHelpUtil.printDebug(String.format("Deleting user ----> %s with details %s %s", emp.getEmployeeName(), System.getProperty("line.separator"), ReflectionToStringBuilder.toString(emp)));
                    //Send the mail
                    HtmlEmail emailToSend = new HtmlEmail();
                    emailToSend.setHostName(email.getHostName());

                    String messageToSend = String.format(messageTemplateDeleted, emp.getEmployeeName(),
                            emp.getEmployeeUid(),
                            emp.getEmployeeId().toString(),
                            emp.getEmail(),
                            emp.getMobileNumber(),
                            emp.getJobTitle());
                    emailToSend.setHtmlMsg(messageToSend);
                    //emailToSend.setTextMsg(StringEscapeUtils.escapeHtml(messageToSend));
                    emailToSend.getToAddresses().clear();
                    //Send mail to scrum masters ==Group ID 14 and Development Managers Group Id 15
                    Collection<EmployeeBO> allEmployeesNeedToGetMail = CollectionUtils.union(getAllEmployeesKeyedByGroupId().get(Integer.valueOf("14")), getAllEmployeesKeyedByGroupId().get(Integer.valueOf("15")));
                    for (EmployeeBO item : allEmployeesNeedToGetMail) {
                        emailToSend.addTo(item.getEmail(), item.getEmployeeName());
                    }
                    //emailToSend.setFrom("PerceptiveKolkataCentral@perceptivesoftware.com", "Perceptive Kolkata Central");
                    emailToSend.setFrom("EnterpriseSoftwareKolkata@lexmark.com", "Enterprise Software Kolkata");
                    emailToSend.setSubject(String.format("Employee removed : %s", emp.getEmployeeName()));
                    emailToSend.send();
                }
            }

            luceneUtil.indexUserInfo(getAllEmployees().values());

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    private void sendMailToPerceptiveOpsTeam(EmployeeBO employeeBO) throws Exception{
        try
        {
            //Send the mail
            HtmlEmail emailToSend = new HtmlEmail();
            emailToSend.setHostName(email.getHostName());
            String messageTemplateAdded = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"font:Georgia; font-size:12px;\">\n" +
                    "<p>Dear All,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>A new employee has joined Perceptive Kolkata.</p>\n" +
                    "</blockquote>\n" +
                    "<ul>\n" +
                    "  <li><strong><em>User Name</em></strong>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>User Short Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><em><strong>Employee Id</strong></em>: <strong>%s</strong></li>\n" +
                    "  <li><strong><em>User Email-Id</em></strong>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Mobile Number</strong></em>:<strong> %s</strong></li>\n" +
                    "  <li><em><strong>Job Title</strong></em> : <strong>%s</strong></li>\n" +
                    "</ul>\n" +
                    "<p>Please take  following  actions.</p>\n" +
                    "<ul>\n" +
                    "  <li>Access to Rally.</li>\n" +
                    "  <li>Access to Salesforce.</li>\n" +
                    "  <li>Access to Confluence.</li>\n" +
                    "  <li>Access to Perceptive AD.</li>\n" +
                    "  <li>Access to TFS.</li>\n" +
                    "</ul>\n" +
                    "<p>Thanks,</p>\n" +
                    "<blockquote>\n" +
                    "  <p>Perceptive Kolkata Central( http://10.195.17.14/PerceptiveKolkataCentral )</p>\n" +
                    "</blockquote>\n" +
                    "</body>\n" +
                    "</html>";
            String messageToSend = String.format(messageTemplateAdded, employeeBO.getEmployeeName(),
                    employeeBO.getEmployeeUid(),
                    employeeBO.getEmployeeId().toString(),
                    employeeBO.getEmail(),
                    employeeBO.getMobileNumber(),
                    employeeBO.getJobTitle());

            emailToSend.setHtmlMsg(messageToSend);
            //emailToSend.setTextMsg(StringEscapeUtils.escapeHtml(messageToSend));
            emailToSend.getToAddresses().clear();
            emailToSend.addTo("per.special.rad.operations@perceptivesoftware.com","RAD - Operations");
            emailToSend.addTo("radops@perceptivesoftware.com","RAD - Operations Team");
            Collection<EmployeeBO> allEmployeesNeedToGetMail = getAllEmployeesKeyedByGroupId().get(Integer.valueOf("14"));
            for (EmployeeBO item : allEmployeesNeedToGetMail) {
                emailToSend.addCc(item.getEmail(), item.getEmployeeName());
            }
            //emailToSend.setFrom("PerceptiveKolkataCentral@perceptivesoftware.com", "Perceptive Kolkata Central");
            emailToSend.setFrom("EnterpriseSoftwareKolkata@lexmark.com", "Enterprise Software Kolkata");
            emailToSend.setSubject(String.format("New employee joined @ Lexmark Kolkata : %s", employeeBO.getEmployeeName()));
            emailToSend.send();
        }
        catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }


}
