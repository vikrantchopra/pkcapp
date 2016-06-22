package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14/8/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionUnhandledExceptionHandler extends ActionSupport {
    private ExceptionWrapper exceptionWrapper;

    public ExceptionWrapper getExceptionWrapper() {
        return exceptionWrapper;
    }

    public void setExceptionWrapper(ExceptionWrapper exceptionWrapper) {
        this.exceptionWrapper = exceptionWrapper;
    }

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String execute() {
        if (exception != null) {
            exceptionWrapper = (ExceptionWrapper) exception;
        }
        return "ERROR";
    }

}
