<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">

    function validateAndSubmit() {
        var errorMessage = '';
        if ($.trim($("#id_INGroup").val()) == '-1') {
            errorMessage = '* Please enter the team name \n';
        }
        if ($.trim($("#id_INVersion").val()) == '') {
            errorMessage = errorMessage + '* Please enter the ImageNow version \n';
        }
        if ($("#id_INNumberOfLicense").val().trim() == '') {
            errorMessage = errorMessage + '* Please enter number of licenses required \n';
        }
        else if (isNaN($("#id_INNumberOfLicense").val())) {
            errorMessage = errorMessage + '* The number of licenses required should be a number \n';
        }
        else if (parseInt($("#id_INNumberOfLicense").val()) <= 0) {
            errorMessage = errorMessage + '* The number of licenses required should be a positive number \n';
        }
        else {
            $("#id_INNumberOfLicense").val(parseInt($("#id_INNumberOfLicense").val()));
        }
        if ($("#id_INFile").val().trim() == '') {
            errorMessage = errorMessage + '* Please specify the file path \n';
        }
        else if ($("#id_INFile").val().trim().split('.').pop().toLowerCase() != 'sysfp') {
            errorMessage = errorMessage + '* Only sysfp file can be uploaded \n';
        }
        else {
            errorMessage = errorMessage;
        }

        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
            return false;
        }
        else {
            $("#id_INRequest").submit();
            return true;
        }
    }
</script>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">ImageNow License Request
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text">
                        <!--Content goes here--->
                        <s:form id="id_INRequest" action="imageNowLicenseRequest" theme="simple"
                                enctype="multipart/form-data">
                            <s:token/>
                            <table>
                                <s:if test="%{errorMessage!=null && errorMessage!=''}">
                                    <tr>
                                        <td colspan="2"><span style="color: red;"> <s:property
                                                value="errorMessage"/></span></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td>License Requested For</td>
                                    <td><s:select id="id_INGroup" name="groupRequestedFor" headerKey="-1"
                                                  headerValue="Select Team"
                                                  list="rallyGroups"
                                                  listKey="groupId" listValue="groupName" theme="simple"
                                                  cssClass="body_text"/></td>
                                </tr>
                                <tr>
                                    <td>ImageNow Version</td>
                                    <td><s:textfield name="imagenowlicenses.imageNowVersion" id="id_INVersion"
                                                     theme="simple" cssClass="body_text" maxLength="98"/></td>
                                </tr>
                                <tr>
                                    <td>Number Of Licenses</td>
                                    <td><s:textfield name="imagenowlicenses.numberOfLicenses"
                                                     id="id_INNumberOfLicense" theme="simple"
                                                     cssClass="body_text"/></td>
                                </tr>
                                
                                <tr>
                                    <td>Validate Hostname</td>
                                    <td><s:textfield name="imagenowlicenses.hostName"
                                                     id="id_Hostname" theme="simple"
                                                     cssClass="body_text"/></td>
                                </tr>
                                
                                <tr>
                                    <td>Attach Sysfp File:</td>
                                    <td><s:file id="id_INFile" theme="simple" name="sysfpFile"/></td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <div align="right" class="btn_bg"><a href="#"
                                                                             onclick="return validateAndSubmit()">Request</a>
                                        </div>
                                    </td>
                                    <td align="left">
                                        <div align="right" class="btn_bg"><a href="#"
                                                                             onclick="$('#id_INRequest').get(0).reset()">Reset</a>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </s:form>
                        <!--Content goes here--->
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
<script type="text/javascript">
    $("input").keypress(function (event) {
        if (event.which == 13) {
            event.preventDefault();
            validateAndSubmit();
        }
    });
</script>