<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">
    function handleEmployeeSelectionChange(event, data) {
        //alert();
        var ui = event.originalEvent.ui;
        var employeeId = ui.item.key;
        var employeeInfoJson = <s:property value="allEmployeeInfoInJson" escape="false" escapeHtml="false" escapeXml="false" escapeJavaScript="false"/>;
        //Un-check all the checkboxes first
        $('input[type="checkbox"]').each(function () {
            $(this).attr("checked", false);
        });
        $.each(employeeInfoJson, function () {
            if (this.employeeId == employeeId) {
                $.each(this.licenses, function () {
                    var licenseTypeId = this.licenseTypeId;
                    $('input[type="checkbox"]').each(function () {
                        if ($(this).val() == licenseTypeId)
                            $(this).attr("checked", true);
                    });
                });
            }
        });
    }
    function validateAndSubmit() {
        var errorMessage = '';
        if ($.trim($("#id_employees").val()) == '') {
            errorMessage = '* Please enter the employee name \n';
        }

        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
            return false;
        }
        else {
            $("#id_updateEmployeeLicenseMapping").submit();
            return true;
        }
    }
</script>
<s:form action="updateEmployeeLicenseMapping" id="id_updateEmployeeLicenseMapping" method="POST" theme="simple">
    <table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
        <tr>
            <td width="100%" class="box_border" colspan="3">
                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <th colspan="40%" align="left" valign="top" class="table_top">Change Employee/Group Map
                        </th>
                        <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                    </tr>

                    <tr>
                        <td colspan="2" class="body_text">
                            <!--Content goes here--->
                            <s:url var="remoteurl" action="ajaxAllEmployeeList" namespace="/ajax"/>
                            Employee Name:&nbsp;&nbsp;
                            <sj:autocompleter
                                    id="id_employees"
                                    name="employeeId"
                                    href="%{remoteurl}"
                                    list="gridModel"
                                    listValue="employeeName"
                                    listKey="employeeId"
                                    listLabel="employeeName"
                                    placeholder="Select a Employee"
                                    onSelectTopics="employeeSelectedChanged"
                                    />
                            <span class="btn_bg" align="right"><a href="#"
                                                                  onclick="return validateAndSubmit()">Change</a>
                            </span>
                            <script type="text/javascript">
                                $.subscribe('employeeSelectedChanged', handleEmployeeSelectionChange);
                            </script>
                            <s:checkboxlist list="#session.ALL_LICENSE_TYPES" id="id_Licenses" name="licenses"
                                            theme="simple"
                                            listKey="licenseTypeId" listValue="licenseTypeName"/>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
</s:form>
<script type="text/javascript">
    $('input[type="checkbox"]').before('</br>');//To show the checkboxes vertically
</script>