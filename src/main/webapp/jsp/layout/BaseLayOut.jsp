<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<s:set id="contextPath" value="#request.get('javax.servlet.forward.context_path')"/>
<head><title>Enterprise Software Kolkata</title>
    <sj:head jqueryui="true" jquerytheme="start" compressed="false"/>
    <script type="text/javascript" src="<s:url value="/menu.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/jquery.printElement.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/jOrgChart/jquery.jOrgChart.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/jwplayer/jwplayer.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/jquery.qtip-1.0.0-rc3.min.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/fullcalendar/fullcalendar.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/fullcalendar/fullcalendar.min.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/js/feedget-1.1.0.min.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<s:url value="/js/fullcalendar/fullcalendar.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/js/fullcalendar/fullcalendar.print.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/global.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/menu.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/js/jOrgChart/css/jquery.jOrgChart.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/js/jOrgChart/css/custom.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/css/feedget.css"/>"/>
    <link rel="icon" href="<s:url value="/images/imagenow-icon.png"/>" type="image/x-icon"/>
    <link rel="shortcut icon" href="<s:url value="/images/imagenow-icon.png"/>" type="image/x-icon"/>
    <script type="text/javascript" src="<s:url value="/js/FusionCharts/FusionCharts.js"/>"></script>
</head>
<body id="id_MainBody">
<s:url id="id_Player_Path_URL" value="/js/jwplayer/player.swf"/>
<s:hidden id="id_jwPlayer_Path" value="%{id_Player_Path_URL}"/>
<script type="text/javascript">
    String.prototype.trim = function() {
        return $.trim(this);
    }
</script>
<table width="94%" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
        <td class="body_container">

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="header_container">

                        <img src="<s:url value="/images/top_right_enterprise.jpg"/>" alt="" width="985" height="160"
                             align="right"/>
                        <!--

                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="left" valign="top" width="20%"><img
                                        src="<s:url value="/images/top_bg3_old.jpg"/>" alt="" width="200" height="145"
                                        align="top"/></td>
                                <td align="center" valign="top" width="16%" class="top_text">Welcome!! <span
                                        class="blue"><sec:authorize access="isAuthenticated()"><sec:authentication property="principal.username" /></sec:authorize></span></td>
                                <td align="right" valign="top" width="64%"><img
                                        src="<s:url value="/images/top_bg2_old.jpg"/>" alt=""
                                        width="550" height="145"
                                        align="top"/></td>
                            </tr>
                        </table>
                        -->


                    </td>

                </tr>
                <tr>

                <tr>
                    <td>
                        <!--Menu Inclusion-->
                        <tiles:insertAttribute name="menu"/>
                        <!--Menu inclusion ends here-->
                    </td>
                </tr>
                <td class="body_container">
                    <!--Body Content Goes Here-->
                    <tiles:insertAttribute name="body"/>
                    <!--Body Content Ends Here Here-->
                </td>

                </tr>
                <tr>
                    <td align="center" valign="top" class="footer">© 2012 <span
                            onclick='$("#id_aboutUs").dialog( "open" )' style="cursor: help">EnterpriseSoftware</span>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>
<sj:dialog id="id_aboutUs" title="User Details" autoOpen="false"
           closeOnEscape="true" modal="true" showEffect="slide" hideEffect="explode" height="250" width="450">
    For any suggestion please contact <b>Saibal
    Ghosh</b> (saibal.ghosh@perceptivesoftware.com)/<b>Dipankar Das</b>(diapankar.das@perceptivesoftware.com).You can also use this
    <a href="https://docs.google.com/a/perceptivesoftware.com/spreadsheet/viewform?formkey=dGs3YVhybU5QS1k2MU9vQmZOREh0dnc6MQ" target="_blank">link</a> to log any bug/suggestions.
</sj:dialog>

</body>
</html>
<sec:authorize access="isAuthenticated()">
    <script type="text/javascript">
        idleTime = 0;
        $(document).ready(function () {
            //Increment the idle time counter every minute.
            var idleInterval = setInterval("timerIncrement()", 60000); // 1 minute

            //Zero the idle timer on mouse movement.
            $(this).mousemove(function (e) {
                idleTime = 0;
            });
            $(this).keypress(function (e) {
                idleTime = 0;
            });
            $(this).scroll(function (e) {
                idleTime = 0;
            });
        })
        function timerIncrement() {
            idleTime = idleTime + 1;
            if (idleTime > 480) { // 480 minutes
                pingHeartBit();
            }
        }
        function pingHeartBit() {
            $.ajax({
                url:"<s:url action="ajaxHeartBeat" namespace="ajax"/>",
                type:'POST',
                error:ajaxCallError
            })
        }
        <s:url id="sessionExpiredPageURLBaseLayOut" action="home">
        </s:url>
        function ajaxCallError(jqXHR, textStatus, errorThrown) {
            if (errorThrown == 'Forbidden') {
                $(window).attr('location', "<s:property value="%{sessionExpiredPageURLBaseLayOut}"/>");
            }

        }
    </script>
</sec:authorize>