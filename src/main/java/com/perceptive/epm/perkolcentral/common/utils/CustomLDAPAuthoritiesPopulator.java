package com.perceptive.epm.perkolcentral.common.utils;

import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.RoleBO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 3/9/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomLDAPAuthoritiesPopulator implements LdapAuthoritiesPopulator {
    private EmployeeBL employeeBL;

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    @Override
    public Collection<GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        Set userPerms = new HashSet();
        final String uid = username;
        ArrayList<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<GrantedAuthority>();
        try {
            //get users permissions from service
            //Set permissions = userService.getPermissions(username);
            ArrayList<EmployeeBO> employeeBOArrayList = new ArrayList<EmployeeBO>(employeeBL.getAllEmployees().values());
            CollectionUtils.filter(employeeBOArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((EmployeeBO) o).getEmployeeUid().trim().equalsIgnoreCase(uid.trim());  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            if (employeeBOArrayList.size() > 0) {
                for (RoleBO roleBO : employeeBOArrayList.get(0).getRoles()) {
                    grantedAuthorityArrayList.add(new GrantedAuthorityImpl(roleBO.getRoleName().trim()));
                }
                grantedAuthorityArrayList.add(new GrantedAuthorityImpl("ROLE_PER_KOL_USER"));//This is the default ROLE to all Perceptive Kolkata Employee
            }
        } catch (Exception ex) {
            LoggingHelpUtil.printError(ex);
        }
        return grantedAuthorityArrayList;

    }
}
