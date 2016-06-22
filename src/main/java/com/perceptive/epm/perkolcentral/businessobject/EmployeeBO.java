package com.perceptive.epm.perkolcentral.businessobject;

import com.perceptive.epm.perkolcentral.common.utils.GroupEnum;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employee;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 18/8/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeBO implements Serializable {
    private String employeeId;
    private String employeeUid;
    private String employeeName;
    private String email;
    private String jobTitle;
    private String mobileNumber;
    private String manager;
    private String managerEmail;
    private String extensionNum;
    private String workspace;
    private boolean isActive = true;
    private ArrayList<GroupBO> groups = new ArrayList<GroupBO>();
    private ArrayList<LicenseBO> licenses = new ArrayList<LicenseBO>();
    private ArrayList<RoleBO> roles = new ArrayList<RoleBO>();
    private String specificRoleInScrumTeam;

    public EmployeeBO() {
        super();
    }

    public EmployeeBO(Employee employee) {
        employeeId = Long.toString(employee.getEmployeeId());
        employeeUid = employee.getEmployeeUid();
        employeeName = employee.getEmployeeName();
        email = employee.getEmail();
        jobTitle = employee.getJobTitle();
        mobileNumber = employee.getMobileNumber();
        manager = employee.getManager();
        managerEmail=employee.getManagerEmail();
        extensionNum = employee.getExtensionNum();
        workspace = employee.getWorkspace();
        isActive = employee.isIsActive();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeUid() {
        return employeeUid;
    }

    public void setEmployeeUid(String employeeUid) {
        this.employeeUid = employeeUid;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getExtensionNum() {
        return extensionNum;
    }

    public void setExtensionNum(String extensionNum) {
        this.extensionNum = extensionNum;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<GroupBO> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupBO> groups) {
        this.groups = groups;
    }

    public ArrayList<LicenseBO> getLicenses() {
        return licenses;
    }

    public void setLicenses(ArrayList<LicenseBO> licenses) {
        this.licenses = licenses;
    }

    public ArrayList<RoleBO> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<RoleBO> roles) {
        this.roles = roles;
    }

    public String getSpecificRoleInScrumTeam() {
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.SCRUM_MASTERS.getCode()));
            }
        })) {
            return "SCRUM MASTER";
        }
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.DEV_MANAGER.getCode()));
            }
        })) {
            return "DEVELOPMENT MANAGER";
        }
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.VERTICAL_QA.getCode()));
            }
        })) {
            return "QA";
        }
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.VERTICAL_TW.getCode()));
            }
        })) {
            return "TECH. WRITER";
        }
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.VERTICAL_UX.getCode()));
            }
        })) {
            return "UX";
        }
        if (CollectionUtils.exists(groups, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((GroupBO) o).getGroupId().equalsIgnoreCase(String.valueOf(GroupEnum.VERTICAL_DEV.getCode()));
            }
        })) {
            return "DEVELOPMENT";
        }
        return specificRoleInScrumTeam;
    }
}
