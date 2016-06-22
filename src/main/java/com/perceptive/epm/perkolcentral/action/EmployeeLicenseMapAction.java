package com.perceptive.epm.perkolcentral.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.LicensesBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 4/10/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLicenseMapAction extends ActionSupport {
    private LicensesBL licensesBL;
    private EmployeeBL employeeBL;
    private String allEmployeeInfoInJson;
    private String licenses;
    private String employeeId;

    public void setLicensesBL(LicensesBL licensesBL) {
        this.licensesBL = licensesBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void setAllEmployeeInfoInJson(String allEmployeeInfoInJson) {
        this.allEmployeeInfoInJson = allEmployeeInfoInJson;
    }

    public String getAllEmployeeInfoInJson() {
        return allEmployeeInfoInJson;
    }

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String executeEmployeeLicenseView() throws ExceptionWrapper {
        try {
            if (!ActionContext.getContext().getSession().containsKey("ALL_LICENSE_TYPES"))
                ActionContext.getContext().getSession().put("ALL_LICENSE_TYPES", licensesBL.getAllLicenseType());

            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();
            employeeLinkedHashMap = employeeBL.getAllEmployees();
            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            allEmployeeInfoInJson = new Gson().toJson(allEmployees);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeUpdateEmployeeLicense() throws ExceptionWrapper {
        try {
            employeeBL.updateEmployeeLicenseMap(employeeId, new ArrayList<String>(Arrays.asList(licenses.split(","))));
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
