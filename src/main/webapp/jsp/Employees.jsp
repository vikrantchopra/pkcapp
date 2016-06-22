<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<s:url id="sessionExpiredPageURL" action="home">
</s:url>
<script type="text/javascript">
    function getAllEmailIds() {
        $.ajax({
            url:"<s:url action="ajaxEmailList" namespace="ajax"/>",
            type:'POST',
            success:copyEmailIds,
            error:ajaxCallError
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
        if (errorThrown == 'Forbidden') {
            $(window).attr('location', "<s:property value="%{sessionExpiredPageURL}"/>");
        }

    }
    function formatEmailLink(cellvalue, options, rowObject) {
        return "<a href='mailto:" + cellvalue + "'>" + cellvalue + "</a>";
    }

    function formatImageLink(cellvalue, options, rowObject) {
        return "<span name='employeeImageShowLink' style='cursor: pointer'  onclick='showImage(" + cellvalue + ")'>" + cellvalue + "</span>";
    }
    function showImage(employeeId) {
        var imageURL = 'http://10.195.17.14/PeopleImages/' + employeeId + '.jpg';
        $("#id_EmpImage").attr('src', imageURL);
        $("#id_Image").dialog("open");
    }

    function bindQTip() {
        // By suppling no content attribute, the library uses each elements title attribute by default
        //$('span[name="employeeImageShowLink"]').qtip({
        // Simply use an HTML img tag within the HTML string
        //    content:'<img src="http://10.195.17.14/PeopleImages/' + $(this).text() + '.jpg" alt="Owl" />'
        //});

        $('span[name="employeeImageShowLink"]').each(function () {

            //Preload the image
            var _sWidth = 125;
            try {
                var _oImg = new Image;
                _oImg.src = 'http://10.195.17.14/PeopleImages/' + $(this).text() + '.jpg';
                _sWidth = _oImg.width;

                $(_oImg)
                        .error(function () {
                            _sWidth = 130;
                        })
            }
            catch (err) {
                alert(err);
                _sWidth = 130;
            }

            $(this).qtip({
                style:{
                    border:{
                        width:2,
                        radius:8,
                        color:'#6699CC'
                    },
                    width:(_sWidth == 0) ? 150 : +_sWidth + "px"
                    //  height:_sHeight+"px"
                },
                content:'<img  src="http://10.195.17.14/PeopleImages/' + $(this).text() + '.jpg" alt="Image Not Available" onError="loadAlternateImage(this)"/>'
            })
            //Put the image icon
            $(this).html("<img  src='<s:url value="/images/cameraicon.jpg"/>' alt='' width='24' height='24'/>");
        });
    }
    function loadAlternateImage(imageLocation) {
        imageLocation.src = '<s:url value="/images/smileys_001_01.png"/>';
        imageLocation.width = 125;
        imageLocation.height = 125;
    }

</script>

<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3" align="center">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Employees Of Perceptive Kolkata
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>
                <tr>
                    <td colspan="2" class="body_text" align="left">
                        <span onclick="getAllEmailIds()" style="cursor:pointer"><b>Copy E-Mail Addresses</b></span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="body_text" align="left">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="body_text" align="center">

                        <s:url var="remoteurl" action="allEmployeesList" namespace="/ajax"/>
                        <sjg:grid
                                id="gridtable"
                                dataType="json"
                                href="%{remoteurl}"
                                pager="true"
                                toppager="true"
                                gridModel="gridModel"
                                rowList="10,15,20"
                                rowNum="15"
                                rownumbers="true"
                                autowidth="true"
                                resizable="true"
                                onErrorTopics="gridLoadError"
                                onCompleteTopics="gridLoadComplete"
                                onGridCompleteTopics="clean_td_title"
                                navigator="true"
                                navigatorCloneToTop="true"
                                navigatorSearch="true"
                                navigatorAdd="false"
                                navigatorDelete="false"
                                navigatorEdit="false"
                                navigatorView="false"
                                >

                            <sjg:gridColumn name="employeeId" index="employeeId" title="Employee Number"
                                            sortable="false"
                                            search="false" javascriptTooltip="true" width="80"/>
                            <sjg:gridColumn name="employeeName" index="employeeName" title="Name" sortable="true"
                                            search="true" searchoptions="{sopt:['cn','eq']}" href="www.google.co.in"/>
                            <sjg:gridColumn name="email" index="email" title="E-Mail" sortable="false" search="false"
                                            formatter="formatEmailLink"/>
                            <sjg:gridColumn name="manager" index="manager" title="Manager" sortable="true"
                                            search="false"/>
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <sjg:gridColumn name="jobTitle" index="jobTitle" title="Job Title" sortable="true"
                                                search="false"/>
                            </sec:authorize>

                            <sjg:gridColumn name="mobileNumber" index="mobileNumber" title="Mobile" sortable="false"
                                            search="false"/>
                            <sjg:gridColumn name="extensionNum" index="extensionNum" title="Extension" sortable="false"
                                            search="false"/>
                            <sjg:gridColumn name="workspace" index="workspace" title="Workspace" sortable="false"
                                            search="false"/>
                            <sjg:gridColumn name="employeeId" index="picId" title="Picture" sortable="false"
                                            search="false" formatter="formatImageLink" javascriptTooltip="false"
                                            width="100" align="center"/>
                        </sjg:grid>
                    </td>

                    <script type="text/javascript">

                        $.subscribe('gridLoadError', function (event, data) {
                            //alert(data);
                            if (event.originalEvent.status == 'error' && event.originalEvent.error == 'Forbidden') {
                                $(window).attr('location', "<s:property value="%{sessionExpiredPageURL}"/>");
                            }
                        })
                        $.subscribe('gridLoadComplete', function (event, data) {
                            //alert(data);
                            // bindQTip();
                        })
                        $.subscribe('clean_td_title', function () {
                            // remove td title in jquery grid
                            bindQTip();
                            $("td").each(function () {
                                var td = $(this);
                                if (td.attr("role") == "gridcell" && td.attr("aria-describedby") == "gridtable_employeeId" && $($(this).html()).attr('name') == 'employeeImageShowLink') {
                                    td.removeAttr("title");
                                }
                            });
                        });
                    </script>
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
<sj:dialog id="id_Image" title="Image Viewer" autoOpen="false"
           closeOnEscape="true" modal="true" hideEffect="explode" height="200" width="200">
    <div align="center">
        <img src="" alt="Image Not Available" title="Image Not Available" id="id_EmpImage" align="middle"
             onError="loadAlternateImage(this)"/>
    </div>
</sj:dialog>

<script type="text/javascript">
    $(document).keypress(function (event) {
        if (event.which == 13 && $("#fbox_gridtable_search").length > 0) {
            $("#fbox_gridtable_search").focus();
        }
    });
</script>