package com.perceptive.epm.perkolcentral.servlet;


import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 27/5/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Log4jInitServlet extends javax.servlet.http.HttpServlet {
    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");

        // if the log4j-init-file context parameter is not set, then no point in trying
        if (file != null) {
            String loggingDirectoryPath = getInitParameter("log-directory");
            if (!new File(loggingDirectoryPath).exists()) {
                try {
                    FileUtils.forceMkdir(new File(loggingDirectoryPath));
                    PropertyConfigurator.configure(prefix + file);
                    LoggingHelpUtil.setLog4jinitialized(true);
                    System.out.println("Log4J Logging started: " + prefix + file);
                } catch (IOException ex) {
                    LoggingHelpUtil.setLog4jinitialized(false);
                    ex.printStackTrace();
                }
            } else {
                PropertyConfigurator.configure(prefix + file);
                LoggingHelpUtil.setLog4jinitialized(true);
                System.out.println("Log4J Logging started: " + prefix + file);
            }

        } else {
            System.out.println("Log4J Is not configured for your Application: " + prefix + file);
            LoggingHelpUtil.setLog4jinitialized(false);
        }
    }

}
