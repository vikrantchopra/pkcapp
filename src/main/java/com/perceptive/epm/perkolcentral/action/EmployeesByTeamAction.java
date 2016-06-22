package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.GroupsBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19/8/12
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeesByTeamAction extends ActionSupport {
    private GroupsBL groupsBL;
    private EmployeeBL employeeBL;
    private String selectedGroupId;

    public GroupsBL getGroupsBL() {
        return groupsBL;
    }

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public EmployeeBL getEmployeeBL() {
        return employeeBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public String getSelectedGroupId() {
        return selectedGroupId;
    }

    public void setSelectedGroupId(String selectedGroupId) {
        this.selectedGroupId = selectedGroupId;
    }

    public String executeEmployeesByTeamView() throws ExceptionWrapper {
        try {
            if (!ActionContext.getContext().getSession().containsKey("ALL_GROUPS")) {
                ActionContext.getContext().getSession().put("ALL_GROUPS", groupsBL.getAllGroups());
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }


}
