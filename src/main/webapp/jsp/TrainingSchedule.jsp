<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">
    var listOfLexmarkHolidays = '<s:property value="lexmarkHolidayList"/>';
    var excludeDateMap = new Object();
    function removeCustomEvent(eventRemovalDate) {
        excludeDateMap[eventRemovalDate] = null;
        $("#" + eventRemovalDate).next().remove();
        $("#" + eventRemovalDate).next().remove();
        $("#" + eventRemovalDate).remove();
    }

    function addExtraSchedule() {
        var errorMessage = '';
        if ($.trim($("#id_trng_excludeDate").val()) == '') {
            errorMessage = '* Please enter the date for the new event \n';
        }
        var dayOfWeek = new Date($("#id_trng_excludeDate").val().trim().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3")).getDay();
        if (dayOfWeek == 0 || dayOfWeek == 6) {
            errorMessage = errorMessage + '* The day you entered is on weekend.Please enter a new date.\n';
        }
        $.each(listOfLexmarkHolidays.split('#'), function (index) {
            if ($.trim(listOfLexmarkHolidays.split('#')[index].split(':')[0]) == $.trim($("#id_trng_excludeDate").val())) {
                errorMessage = errorMessage + '* The day you entered is a holiday in Lexmark (' + $.trim(listOfLexmarkHolidays.split('#')[index].split(':')[1]) + ').\n';
                return false;
            }
        })
        if ($.trim($("#id_excludeReason").val()) == '') {
            errorMessage = errorMessage + '* Please enter the new event \n';
        }
        if ($.trim(errorMessage) == '' && excludeDateMap[$.trim($("#id_trng_excludeDate").val())] != null) {
            errorMessage = errorMessage + '* There is already an event on this date \n';
        }
        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
        }
        else {
            excludeDateMap[$.trim($("#id_trng_excludeDate").val())] = $.trim($("#id_excludeReason").val());
            $("#id_xtra_events").append("<span id='" + $.trim($("#id_trng_excludeDate").val()) + "'>" + $.trim($("#id_trng_excludeDate").val()) +
                    "-->&nbsp;&nbsp;&nbsp;" + $.trim($("#id_excludeReason").val()) + "-->&nbsp;&nbsp;&nbsp;<a href='#' onclick=\"removeCustomEvent('" + $.trim($("#id_trng_excludeDate").val()) + "')\">Remove</a>" +
                    "</span><input type='hidden' name='excludedEvents' value='" + $.trim($("#id_trng_excludeDate").val()) + "~~" + $.trim($("#id_excludeReason").val()) + "'/><br/>");
        }
    }
    function validateAndSubmit() {
        var errorMessage = '';
        //This is the time when the user is not added to perceptive system yet
        if ($.trim($("#id_employees").val()) == '' && $.trim($("#id_employees_widget").val()) != '') {
            $.trim($("#id_employees").val(-1));
        }
        if ($.trim($("#id_employees").val()) == '') {
            errorMessage = '* Please enter the trainee name \n';
        }
        if ($.trim($("#id_buddy_employees").val()) == '') {
            errorMessage = errorMessage + '* Please enter the buddy name \n';
        }

        if ($.trim($("#id_employees").val()) != '' && $.trim($("#id_buddy_employees").val()) != '' && $.trim($("#id_employees").val()) == $.trim($("#id_buddy_employees").val())) {
            errorMessage = errorMessage + '* Employee name and Buddy name cannot be same. \n';
        }
        if ($.trim($("#id_trng_startDate").val()) == '') {
            errorMessage = errorMessage + '* Please enter training start date \n';
        }
        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
            return false;
        }
        else {
            if ($('#id_IsSendEmail') == 'true') {

            }
            $("#id_nameOfEmployee").val($("#id_employees_widget").val());
            $("#id_nameOfBuddy").val($("#id_buddy_employees_widget").val());
            $("#id_TrainingScheduleForm").submit();
            return true;
        }
    }
    $(document).ready(function () {

        //Set the name of buddy
        $("#id_employees_widget").val($("#id_nameOfEmployee").val());
        $("#id_buddy_employees_widget").val($("#id_nameOfBuddy").val());
        //Add the excluded events:if any

        var excludedEventsConcatenatedString = '<s:property value="excludedEventsConcatenatedString"/>';
        if ($.trim(excludedEventsConcatenatedString) != '') {
            var excludedEventStringArray = excludedEventsConcatenatedString.split('^^');
            $.each(excludedEventStringArray, function (index) {
                excludeDateMap[$.trim(excludedEventStringArray[index].split('~~')[0])] = $.trim(excludedEventStringArray[index].split('~~')[1]);
                $("#id_xtra_events").append("<span id='" + $.trim(excludedEventStringArray[index].split('~~')[0]) + "'>" + $.trim(excludedEventStringArray[index].split('~~')[0]) +
                        "-->&nbsp;&nbsp;&nbsp;" + $.trim(excludedEventStringArray[index].split('~~')[1]) + "-->&nbsp;&nbsp;&nbsp;<a href='#' onclick=\"removeCustomEvent('" + $.trim(excludedEventStringArray[index].split('~~')[0]) + "')\">Remove</a>" +
                        "</span><input type='hidden' name='excludedEvents' value='" + $.trim(excludedEventStringArray[index].split('~~')[0]) + "~~" + $.trim(excludedEventStringArray[index].split('~~')[1]) + "'/><br/>");
            });
        }
        //----------------------------------------------------------------------------------------------------
        // page is now ready, initialize the calendar...
        var calendarStartDate = new Date();
        if ($("#id_trng_startDate").val().trim() != '')
            calendarStartDate = new Date($("#id_trng_startDate").val().trim().replace(/(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"));

        $('#id_Training_Calendar').fullCalendar({
            // put your options and callbacks here
            year:calendarStartDate.getFullYear(),
            month:calendarStartDate.getMonth(),
            date:calendarStartDate.getDate(),
            theme:true,
            events:[
                <s:property value="eventDetail"/>
            ],
            eventColor:'#378006',
            header:{
                left:'prev,next today',
                center:'title',
                right:'month,basicWeek,basicDay'
            }
        })

    });
</script>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Training Schedule
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text">
                        <!--Content goes here--->
                        <s:form theme="simple" id="id_TrainingScheduleForm" action="createTrainingSchedule">
                            <s:token/>
                            <s:if test="%{errorMessage!=null && errorMessage!=''}">
                                <span style="color: red;"> <s:property
                                        value="errorMessage"/></span><br/>
                            </s:if>
                            <s:url var="remoteurl" action="ajaxAllEmployeeList" namespace="/ajax"/>
                            <table border="0" align="left" width="50%">
                                <tr>
                                    <td align="left">
                                        <b>Employee Name:</b>
                                    </td>
                                    <td align="left">
                                        <sj:autocompleter
                                                id="id_employees"
                                                name="employeeId"
                                                href="%{remoteurl}"
                                                list="gridModel"
                                                listValue="employeeName"
                                                listKey="employeeId"
                                                listLabel="employeeName"
                                                placeholder="Select a Employee"
                                                />
                                        <s:hidden name="nameOfEmployee" id="id_nameOfEmployee"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <b>Buddy Name:</b>
                                    </td>
                                    <td align="left">
                                        <sj:autocompleter
                                                id="id_buddy_employees"
                                                name="buddyEmployeeId"
                                                href="%{remoteurl}"
                                                list="gridModel"
                                                listValue="employeeName"
                                                listKey="employeeId"
                                                listLabel="employeeName"
                                                placeholder="Select Buddy"
                                                />
                                        <s:hidden name="nameOfBuddy" id="id_nameOfBuddy"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <b>Training Start Date:</b>
                                    </td>
                                    <td align="left">
                                        <sj:datepicker id="id_trng_startDate" name="trainingStartDate"
                                                       displayFormat="dd-mm-yy"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <b>Create Custom Event:</b>
                                    </td>
                                    <td align="left">
                                        <sj:datepicker id="id_trng_excludeDate" name="excludeDate"
                                                       displayFormat="dd-mm-yy"/>&nbsp;&nbsp;&nbsp;<s:textfield
                                            id="id_excludeReason" name="excludeReason" theme="simple" maxLength="15"/>
                                        &nbsp;&nbsp;&nbsp;
                                        <a href="#" onclick='addExtraSchedule()'>Add Event</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <b>Custom Events:</b>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div id="id_xtra_events"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <s:checkbox id="id_IsSendEmail" name="sendMail" theme="simple"
                                                    label="E-Mail to employee/buddy" labelposition="left"/><b>Send
                                        E-Mail to
                                        Employee and Buddy</b>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="2">
                                        <span align="right" class="btn_bg"><a href="#"
                                                                              onclick="return validateAndSubmit()">Create</a>
                                    </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                       <span align="right" class="btn_bg"><a href="#"
                                                                             onclick='$("#id_PrintSchedule").dialog( "open" )'>Print</a> </span>
                                    </td>
                                </tr>
                            </table>
                            <br/>

                            <div id="id_Training_Calendar"/>
                        </s:form>
                        <br/>

                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
<sj:dialog id="id_PrintSchedule" title="Training Schedule:Print Preview" autoOpen="false"
           closeOnEscape="true" modal="true" showEffect="slide" hideEffect="explode" height="800" width="800">
    <div align="center">
        <img src="<s:url value="/images/printer1.png"/>"
             width="32" height="32" alt="Print"
             title="Print" onclick="$('#id_PrintScheduleTable').printElement({pageTitle:'New Employee Training Schedule'});"/><br/>
        <s:property value="calendarPrint" escapeHtml="false"/>
    </div>
</sj:dialog>


