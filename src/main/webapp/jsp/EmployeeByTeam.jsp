<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<s:url id="sessionExpiredPageURL" action="home">
</s:url>
<script type="text/javascript">
    function showTheTeamMembers() {
        $.publish('showSelectedTeamMembers');
    }

    function getAllEmailIds() {
        if ($("#id_selectGroup").val() == -1) {
            alert('Please select a group.');
            return false;
        }
        else
            $("#gridtable").show();
        $.ajax({
            url:"<s:url action="ajaxEmailList" namespace="ajax"/>",
            type:'POST',
            success:copyEmailIds,
            error:ajaxCallError,
            data:$("#id_EmployeesByGroup").serialize()
        })
    }
    function copyEmailIds(data, status, jxhr) {
        //window.prompt("Copy to clipboard: Ctrl+C, Enter", data);
        $("#id_Emails").val(data);
        $("#id_AllEmail").dialog("open");
        $("#id_Emails").focus();
        $("#id_Emails").select();
    }
    function ajaxCallError(jqXHR, textStatus, errorThrown) {
        if (errorThrown == 'Forbidden')
            $(window).attr('location', "<s:property value="%{sessionExpiredPageURL}"/>");
    }

    function formatEmailLink(cellvalue, options, rowObject) {
        return "<a href='mailto:"+cellvalue+"'>" + cellvalue + "</a>";
    }

</script>
<s:form id="id_EmployeesByGroup">
    <table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
        <tr>
            <td width="100%" class="box_border" colspan="3" align="center">
                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <th colspan="40%" align="left" valign="top" class="table_top">Employees Of Perceptive Kolkata By
                            Team
                        </th>
                        <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                    </tr>
                    <tr>
                        <td colspan="2" class="body_text" align="center">
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="body_text" align="left">
                            <span onclick="getAllEmailIds()" style="cursor:pointer"><b>Copy E-Mail Addresses</b></span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="body_text" align="center">
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="body_text" align="left">
                            <s:select list="#session.ALL_GROUPS" listKey="key" listValue="value.groupName"
                                      id="id_selectGroup"
                                      name="selectedGroupId" headerKey="-1"
                                      headerValue="-Select Team/Group-" onchange="showTheTeamMembers()"
                                      cssClass="body_text" theme="simple">
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="body_text" align="center">
                            &nbsp;
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="body_text" align="center">
                            <s:url var="remoteurl" action="allEmployeesListByTeam" namespace="/ajax"/>
                            <sjg:grid
                                    id="gridtable"
                                    dataType="json"
                                    href="%{remoteurl}"
                                    pager="true"
                                    gridModel="gridModel"
                                    rowList="10,15,20"
                                    rowNum="15"
                                    rownumbers="true"
                                    autowidth="true"
                                    resizable="true"
                                    reloadTopics="showSelectedTeamMembers"
                                    formIds="id_EmployeesByGroup"
                                    onErrorTopics="gridLoadError"
                                    >
                                <sjg:gridColumn name="employeeId" index="employeeId" title="ID" sortable="false"/>
                                <sjg:gridColumn name="employeeName" index="employeeName" title="Name" sortable="true"/>
                                <sjg:gridColumn name="email" index="email" title="E-Mail" sortable="false" formatter="formatEmailLink"/>
                                <sjg:gridColumn name="manager" index="manager" title="Manager" sortable="true"
                                                search="false"/>
                                <sec:authorize access="hasRole('ROLE_ADMIN')">
                                    <sjg:gridColumn name="jobTitle" index="jobTitle" title="Job Title" sortable="true"
                                                    search="false"/>
                                </sec:authorize>
                                <sjg:gridColumn name="extensionNum" index="extensionNum" title="Extension" sortable="false"
                                                search="false"/>
                                <sjg:gridColumn name="workspace" index="workspace" title="Workspace" sortable="false"
                                                search="false"/>
                                <sjg:gridColumn name="mobileNumber" index="mobileNumber" title="Mobile"
                                                sortable="false"/>
                            </sjg:grid>
                        </td>
                        <script type="text/javascript">
                            $.subscribe('gridLoadError', function (event, data) {
                                if (event.originalEvent.status == 'error' && event.originalEvent.error == 'Forbidden') {
                                    $(window).attr('location', "<s:property value="%{sessionExpiredPageURL}"/>");
                                }
                            })

                        </script>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
</s:form>
<sj:dialog id="id_AllEmail" title="Copy to clipboard: Ctrl+C, ESC" autoOpen="false"
           closeOnEscape="true" modal="true" hideEffect="explode" height="250" width="450">
    <div align="center">
        <s:textarea id="id_Emails" theme="simple" cssClass="body_text" cols="58" rows="11"/>
    </div>
</sj:dialog>