package com.perceptive.epm.perkolcentral.action.ajax;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 20/8/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MiscAjaxAction extends ActionSupport {
    private InputStream inputStream;
    private EmployeeBL employeeBL;
    private String selectedGroupId;

    public String getSelectedGroupId() {
        return selectedGroupId;
    }

    public void setSelectedGroupId(String selectedGroupId) {
        this.selectedGroupId = selectedGroupId;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public EmployeeBL getEmployeeBL() {
        return employeeBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public String executeGetAllEmails() throws ExceptionWrapper {
        try {
            String emails = "";
            ArrayList<String> emailsArray = new ArrayList<String>();
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

            employeeLinkedHashMap = employeeBL.getAllEmployees();
            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            if (selectedGroupId != null && !selectedGroupId.trim().equalsIgnoreCase("")) {
                CollectionUtils.filter(allEmployees, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        EmployeeBO emp = (EmployeeBO) o;
                        if (CollectionUtils.exists(emp.getGroups(), new Predicate() {
                            @Override
                            public boolean evaluate(Object o) {
                                return ((GroupBO) o).getGroupId().equalsIgnoreCase(selectedGroupId); //To change body of implemented methods use File | Settings | File Templates.
                            }
                        }))
                            return true;
                        else
                            return false;
                    }
                });
            }
            for (Object item : allEmployees) {
                emailsArray.add(((EmployeeBO) item).getEmail());
            }
            emails = StringUtils.join(emailsArray, ',');
            inputStream = IOUtils.toInputStream(emails);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeGetEmployeeInformation() throws ExceptionWrapper {
        try {
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

            employeeLinkedHashMap = employeeBL.getAllEmployees();
            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            Gson jsonEmployeeArray = new Gson();
            inputStream = IOUtils.toInputStream(jsonEmployeeArray.toJson(allEmployees));
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeHeartBeat() {
        inputStream = IOUtils.toInputStream("My Heart Is Beating");
        return SUCCESS;
    }
}
