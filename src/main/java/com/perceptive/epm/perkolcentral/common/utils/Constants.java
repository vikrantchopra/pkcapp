package com.perceptive.epm.perkolcentral.common.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10/9/12
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final String employeeName = "employeeName";
    public static final String employeeUID = "UID";
    public static final String employeeID = "ID";
    public static final String logged_in_user = "LOGGED_IN_USER";
    public static final String email_license_request="<html>\n" +
            "<head>\n" +
            "<title>License Request</title>\n" +
            "</head>\n" +
            "<body style=\"font:Georgia; font-size:12px;\">\n" +
            "<p>Please provide ImageNow License with following detail.</p>\n" +
            "<ul>\n" +
            "  <li><strong>License Requested By</strong>:%s </li>\n" +
            "  <li><strong>Team Name</strong>:%s</li>\n" +
            "  <li><strong>License Requested On</strong>:%s</li>\n" +
            "  <li><strong>ImageNow Version</strong>:%s</li>\n" +
            "  <li><strong>Total Number of Licenses</strong>:%s</li>\n" +
            "</ul>\n" +
            "<p>Thanks,</p>\n" +
            "<p>ImageNow License Request Agent (EnterpriseSoftwareKolkata)</p>\n" +
            "</body>\n" +
            "</html>";
    public final static String REPORT_ALL_USER="REPORT_ALL_USER";
    public final static String REPORT_ALL_USER_SCRUM_TEAM_WISE="REPORT_ALL_USER_SCRUM_TEAM_WISE";
    public final static String REPORT_ALL_USER_NONSCRUM_TEAM_WISE="REPORT_ALL_USER_NONSCRUM_TEAM_WISE";
    public final static String REPORT_LICENSE="REPORT_LICENSE";
}
