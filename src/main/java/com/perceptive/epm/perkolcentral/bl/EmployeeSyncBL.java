package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: saibal
 * Date: 2/21/13
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeSyncBL {
    private LdapTemplate ldapTemplate;
    private EmployeeBL employeeBL;
    private HashMap<String, String> managerEmailKeyedByUid = new HashMap<String, String>();


    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }


    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }


    public void executeEmployeeSync() throws ExceptionWrapper {
        try {
            LoggingHelpUtil.printDebug("-----------------------------------------------NightlyEmployeeSyncJob started-----------------------------------------------");
            List<EmployeeBO> employeeListFromLDAP = ldapTemplate.search("", "(&(|(&(lexorgpersonbusinessunit=PCSFT)(lexorgpersonofficecountry=IND))(&(lexorgpersonbusinessunit=BW)(lexorgpersonofficecountry=IND)))(lexorgpersonemployeestatus=A))", new AttributesMapper() {
                @Override
                public EmployeeBO mapFromAttributes(Attributes attributes) throws NamingException {

                    EmployeeBO employeeBO = new EmployeeBO();

                    employeeBO.setEmail(attributes.get("mail").get() == null ? "" : attributes.get("mail").get().toString());
                    employeeBO.setEmployeeId(attributes.get("employeenumber").get().toString());
                    employeeBO.setEmployeeName(attributes.get("displayname").get().toString());
                    employeeBO.setEmployeeUid(attributes.get("uid").get().toString());
                    employeeBO.setMobileNumber(attributes.get("mobile") == null ? "" : StringUtils.deleteWhitespace(attributes.get("mobile").get().toString()));
                    employeeBO.setJobTitle(attributes.get("title") == null ? "" : attributes.get("title").get().toString());
                    employeeBO.setManager(attributes.get("manager")==null?"":attributes.get("manager").get().toString());
                    employeeBO.setManagerEmail(attributes.get("lexorgpersonmanagersname").get().toString());
                    employeeBO.setActive(true);
                    return employeeBO;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            //Get the manager email-ids
            for (EmployeeBO employeeBO : employeeListFromLDAP) {
                if (managerEmailKeyedByUid.containsKey(employeeBO.getManagerEmail().trim().toLowerCase())) {
                    employeeBO.setManagerEmail(managerEmailKeyedByUid.get(employeeBO.getManagerEmail().trim().toLowerCase()));
                } else {
                    String key = employeeBO.getManagerEmail().trim().toLowerCase();
                    getManagerEmailId(employeeBO);
                    managerEmailKeyedByUid.put(key, employeeBO.getManagerEmail());
                }
            }
            //Indialab parsing is stopped for high overhead
            //Get the information from Indialab portal
            //for (EmployeeBO employeeBO : employeeListFromLDAP) {
            //    getIndiaLabInfo(employeeBO);
            //}
            employeeBL.updateAllEmployee(employeeListFromLDAP);
            LoggingHelpUtil.printDebug("-----------------------------------------------NightlyEmployeeSyncJob  :  Rebuilding the Cache-----------------------------------------------");
            employeeBL.getAllEmployees();
            employeeBL.getAllEmployeesKeyedByGroupId();
            LoggingHelpUtil.printDebug("-----------------------------------------------NightlyEmployeeSyncJob ended-----------------------------------------------");

        } catch (Exception e) {
            LoggingHelpUtil.printError("--------------------------------------------------------Error occurred while executing the job NightlyEmployeeSyncJob--------------------------------------------------------");
            LoggingHelpUtil.printError(e);
            throw new ExceptionWrapper(e);
        }
    }

    private void getManagerEmailId(EmployeeBO employeeBO) throws ExceptionWrapper {
        try {
            String email = ((DirContextAdapter) ldapTemplate.lookup(employeeBO.getManagerEmail().replaceFirst(",o=lexmark", ""))).getStringAttribute("mail");
            employeeBO.setManagerEmail(email);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
}
