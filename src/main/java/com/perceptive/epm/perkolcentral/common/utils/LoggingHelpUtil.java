package com.perceptive.epm.perkolcentral.common.utils;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 21/5/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoggingHelpUtil {

    private static Logger log = Logger.getLogger("PerceptiveKolkata");

    private static boolean isLog4jinitialized = false;

    public static void setLog4jinitialized(boolean log4jinitialized) {
        isLog4jinitialized = log4jinitialized;
    }
    
    public static void printTrace(String trace) {
        if (isLog4jinitialized)
            log.trace(trace);
        else
            System.out.println(trace);
    }

    public static void printDebug(String debug) {
        if (isLog4jinitialized)
            log.debug(debug);
        else
            System.out.println(debug);
    }

    public static void printError(String error) {
        if (isLog4jinitialized)
            log.error(error);
        else
            System.out.println(error);
    }

    public static void printError(Exception ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        printError(result.toString());
    }
}
