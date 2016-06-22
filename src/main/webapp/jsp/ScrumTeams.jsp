<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<table width="98%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Kolkata Scrum Teams
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text" align="center">
                        <!--Content goes here--->
                        <script>
                            jQuery(document).ready(function () {
                                $("#org").jOrgChart({
                                    chartElement:'#chart'
                                });
                            });
                        </script>
                        <ul id="org" style="display:none">
                            <li>
                                Perceptive Kolkata Scrum Teams
                                <ul>
                                    <s:iterator value="employeeKeyedByScrumTeam">
                                        <li><s:property value="key"/>
                                            <ul>
                                                <li>
                                                    <span style="cursor: pointer"
                                                          onclick="$('#id_Div_TeamMembers').html($(this).next().html());$('#id_AllUsers').dialog('open');"><img
                                                            src="<s:url value="/images/messenger.PNG"/>" border="0"
                                                            width="32" height="32"/></span>

                                                    <div style="display: none;">
                                                        <div align="left">
                                                            <s:iterator value="value">
                                                                *&nbsp;<s:property value="employeeName"/>&nbsp;&nbsp;--&nbsp;<I><s:property value="specificRoleInScrumTeam"/></I><br/>
                                                            </s:iterator>
                                                        </div>
                                                    </div>
                                             <span style="cursor: pointer"
                                                   onclick="$('#id_Emails').val($(this).next().text().trim().replace(/ /g,'').slice(0,-1) );
                                                   $('#id_AllEmail').dialog('open');
                                                   $('#id_Emails').focus();
                                                   $('#id_Emails').select();"><img
                                                     src="<s:url value="/images/envelope_B_front.png"/>" border="0"
                                                     width="32" height="32"/></span>

                                                    <div style="display: none;">
                                                        <s:iterator value="value">
                                                            <s:property value="email"/>,
                                                        </s:iterator>
                                                    </div>
                                                </li>
                                            </ul>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </li>
                        </ul>
                        <div id="chart" class="orgChart"></div>
                        <!--Organization chart ends here-->

                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
<sj:dialog id="id_AllEmail" title="Copy to clipboard: Ctrl+C, ESC" autoOpen="false"
           closeOnEscape="true" modal="true" hideEffect="explode" height="250" width="450">
    <div align="center">
        <s:textarea id="id_Emails" theme="simple" cssClass="body_text" cols="58" rows="11"/>
    </div>
</sj:dialog>
<sj:dialog id="id_AllUsers" title="Team Members" autoOpen="false"
           closeOnEscape="true" modal="true" hideEffect="explode" height="250" width="450">
    <div align="center" id="id_Div_TeamMembers">
    </div>
</sj:dialog>