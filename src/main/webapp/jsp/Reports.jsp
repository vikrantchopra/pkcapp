<%@ page import="com.perceptive.epm.perkolcentral.common.utils.Constants" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Excel Reports
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text">
                        <!--Content goes here--->
                        <ul>
                            <li>
                                <s:url var="rep_all_users" action="excelReport">
                                    <s:param name="reportKey"><%=Constants.REPORT_ALL_USER%></s:param>
                                </s:url>
                                <s:a href="%{rep_all_users}">Generate All User Report</s:a>
                            </li>
                            <li>
                                <s:url var="rep_all_users" action="excelReport">
                                    <s:param name="reportKey"><%=Constants.REPORT_ALL_USER_SCRUM_TEAM_WISE%></s:param>
                                </s:url>
                                <s:a href="%{rep_all_users}">Generate All User Report (Scrum Team Wise)</s:a>
                            </li>
                            <li>
                                <s:url var="rep_all_users" action="excelReport">
                                    <s:param name="reportKey"><%=Constants.REPORT_ALL_USER_NONSCRUM_TEAM_WISE%></s:param>
                                </s:url>
                                <s:a href="%{rep_all_users}">Generate All User Report (Team Wise)</s:a>
                            </li>
                            <li>
                                <s:url var="rep_all_users" action="excelReport">
                                    <s:param name="reportKey"><%=Constants.REPORT_LICENSE%></s:param>
                                </s:url>
                                <s:a href="%{rep_all_users}">License Information</s:a>
                            </li>
                        </ul>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>