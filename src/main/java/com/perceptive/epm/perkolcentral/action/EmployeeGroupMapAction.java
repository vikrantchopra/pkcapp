package com.perceptive.epm.perkolcentral.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.GroupsBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 29/8/12
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeGroupMapAction extends ActionSupport {
    private GroupsBL groupsBL;
    private EmployeeBL employeeBL;
    private String allEmployeeInfoInJson;
    private String employeeId;
    private String groups;

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public String getAllEmployeeInfoInJson() {
        return allEmployeeInfoInJson;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String executeEmployeeGroupMapView() throws ExceptionWrapper {
        try {
            if (!ActionContext.getContext().getSession().containsKey("ALL_GROUPS")) {
                ActionContext.getContext().getSession().put("ALL_GROUPS", groupsBL.getAllGroups());
            }
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();
            employeeLinkedHashMap = employeeBL.getAllEmployees();
            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            allEmployeeInfoInJson = new Gson().toJson(allEmployees);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeUpdateGroupMap() throws ExceptionWrapper {
        try {
            employeeBL.updateEmployeeGroupMap(employeeId, new ArrayList<String>(Arrays.asList(groups.split(","))));

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
