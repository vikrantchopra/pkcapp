package com.perceptive.epm.perkolcentral.common.exception;


import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.apache.commons.lang.RandomStringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 27/5/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionWrapper extends Exception {
    private String strErrNum = null;
    private String strErrorStack = null;

    public String getStrErrNum() {
        return strErrNum;
    }

    public void setStrErrNum(String strErrNum) {
        this.strErrNum = strErrNum;
    }

    public String getStrErrorStack() {
        return strErrorStack;
    }

    public void setStrErrorStack(String strErrorStack) {
        this.strErrorStack = strErrorStack;
    }

    public ExceptionWrapper(Exception ex) {

        if (!ex.getClass().toString().equalsIgnoreCase("class com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper")) {
            strErrNum = RandomStringUtils.randomAlphabetic(10);
            strErrorStack = getStackMessage(ex);
            LoggingHelpUtil.printError(strErrorStack);
        } else {
            strErrNum = ((ExceptionWrapper) ex).strErrNum;
            strErrorStack = ((ExceptionWrapper) ex).strErrorStack;
        }
    }

    private String getStackMessage(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("The error encountered due to : " + strErrNum);
        pw.println(
                " =================================================================================================\n");
        pw.print(" [ ");
        pw.print(exception.getClass().getName().toString());
        pw.print(" ] ");

        pw.println(exception.getMessage());
        exception.printStackTrace(pw);
        pw.println(
                " =================================================================================================\n");

        return sw.toString();

    }

}
