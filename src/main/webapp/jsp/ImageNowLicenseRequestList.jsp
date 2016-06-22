<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Licenses requested
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text">
                        <!--Content goes here--->
                        <table width="100%" border="1" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="left" valign="middle" class="table_top">License Requested On</td>
                                <td align="left" valign="middle" class="table_top">License Requested For</td>
                                <td align="left" valign="middle" class="table_top">Is Provided</td>
                                <td align="left" valign="middle" class="table_top">SYSFP File</td>
                                <sec:authorize access="hasRole('ROLE_ADMIN')">
                                    <td align="left" valign="middle" class="table_top">Provide/Cancel Request</td>
                                </sec:authorize>
                            </tr>
                            <s:iterator value="imagenowlicensesArrayList">
                                <s:url var="sysFpFileDownload" action="imageNowLicenseRequestSysFpFileDownload">
                                    <s:param name="imagenowlicenses.imageNowLicenseRequestId"><s:property
                                            value="imageNowLicenseRequestId"/></s:param>
                                    <s:param name="imagenowlicenses.fileName"><s:property
                                            value="fileName"/></s:param>
                                </s:url>
                                <s:url var="cancelINLicReq" action="cancelImageNowLicense">
                                    <s:param name="imagenowlicenses.imageNowLicenseRequestId"><s:property
                                            value="imageNowLicenseRequestId"/></s:param>

                                </s:url>
                                <s:url var="provideINLicReq" action="provideImageNowLicense">
                                    <s:param name="imagenowlicenses.imageNowLicenseRequestId"><s:property
                                            value="imageNowLicenseRequestId"/></s:param>

                                </s:url>
                                <tr>
                                    <td align="left" valign="middle" class="body_text"><s:date name="licenseRequestedOn"
                                                                                               format="dd-MMM-yyyy hh:mm"/></td>
                                    <td align="left" valign="middle" class="body_text"><s:property
                                            value="groups.groupName"/></td>
                                    <td align="left" valign="middle" class="body_text"><s:property
                                            value="isProvided"/></td>
                                    <td align="left" valign="middle" class="body_text"><s:a
                                            href="%{sysFpFileDownload}"><s:property
                                            value="fileName"/></s:a></td>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        <td align="left" valign="middle" class="body_text"><s:a
                                                href="%{provideINLicReq}"><img
                                                src="<s:url value="/images/checkmark.png"/>" width="16" height="16"
                                                alt="Provide" title="Provide"/></s:a>&nbsp;&nbsp;&nbsp;&nbsp;<s:a
                                                href="%{cancelINLicReq}"><img src="<s:url value="/images/delete.png"/>"
                                                                              width="16" height="16" alt="Cancel"
                                                                              title="Cancel"/></s:a></td>
                                    </sec:authorize>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>