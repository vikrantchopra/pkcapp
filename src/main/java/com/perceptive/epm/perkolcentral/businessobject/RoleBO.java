package com.perceptive.epm.perkolcentral.businessobject;

import com.perceptive.epm.perkolcentral.hibernate.pojo.Rolemaster;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 4/9/12
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoleBO implements Serializable {
    private Long roleId;
    private String roleName;

    public RoleBO(Rolemaster rolemaster) {
        roleId = rolemaster.getRoleId();
        roleName = rolemaster.getRoleName();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
