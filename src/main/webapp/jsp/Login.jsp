<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">

    function validateAndSubmit() {
        var errorMessage = '';
        if ($.trim($("#id_j_username").val()) == '') {
            errorMessage = '* Please enter the login name \n';
        }
        if ($.trim($("#id_j_password").val()) == '') {
            errorMessage = errorMessage + '* Please enter the password \n';
        }
        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
            return false;
        }
        else {
            $("#id_j_spring_security_check").submit();
            return true;
        }

    }
</script>
<table width="60%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="50%" class="box_border">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Login:</th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>
                <tr>
                    <td colspan="2" align="center">&nbsp;
                    <s:if test="%{#parameters.loginFail!=null}">
                        <b> <font color="red">Login failed!Please try again.</font></b>
                    </s:if>
                        <s:if test="%{#parameters.invalidSession!=null}">
                            <b> <font color="red">Session expired or Invalid Session!Please log-in to proceed.</font></b>
                        </s:if>
                        <s:if test="%{#parameters.logout!=null}">
                            <b> <font color="red">You have successfully logged-out of the system!Please log-in again.</font></b>
                        </s:if>
                    </td>
                </tr>

                <form action="<s:url value="j_spring_security_check"/>" method="post" id="id_j_spring_security_check">
                    <tr>
                        <td width="40%" align="right" valign="middle"><span class="body_text"><b>Login Name:</b></span>
                        </td>
                        <td width="60%" align="left" valign="middle"><span style="width: 50%"><s:textfield
                                name="j_username" id="id_j_username" theme="simple" cssClass="search_bg"
                                cssStyle="width: 40%"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="40%" align="right" valign="middle"><span class="body_text"><b>Password:</b></span>
                        </td>
                        <td width="60%" align="left" valign="middle"><span style="width: 50%"><s:password
                                name="j_password" id="id_j_password" theme="simple" cssClass="search_bg"
                                cssStyle="width: 40%"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="40%" align="right" valign="middle">
                            <div align="right" class="btn_bg"><a href="#"
                                                                 onclick="return validateAndSubmit()">Login</a>
                            </div>
                        </td>
                        <td width="60%" align="left" valign="middle">
                            <div align="right" class="btn_bg"><a href="#"
                                                                 onclick="$('#id_j_username').val('');$('#id_j_password').val('')">Reset</a>
                            </div>
                        </td>
                    </tr>
                </form>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script type="text/javascript">
    $("input").keypress(function(event) {
        if (event.which == 13) {
            event.preventDefault();
            validateAndSubmit();
        }
    });
</script>