package com.perceptive.epm.perkolcentral.interceptors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.utils.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 20/8/12
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionCheckInterceptor implements Interceptor {
    private EmployeeBL employeeBL;

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void destroy() {
        //LogManager.info(this," SessionCheckInterceptor destroy() is called...");
    }


    public void init() {
        //LogManager.info(this," SessionCheckInterceptor init() is called...");
    }


    public String intercept(ActionInvocation actionInvocation) throws Exception {
        String actionResult = actionInvocation.invoke();

        ActionContext context = actionInvocation.getInvocationContext();
        Map<String, Object> sessionMap = context.getSession();
        if (sessionMap != null && !sessionMap.containsKey(Constants.logged_in_user)) {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String employeeUID = auth.getName();
                EmployeeBO loggedInUser = employeeBL.getEmployeeByEmployeeUID(employeeUID);
                sessionMap.put(Constants.logged_in_user, loggedInUser);
            }
        }

        return actionResult;
    }

}