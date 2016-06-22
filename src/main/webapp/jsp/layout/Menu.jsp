<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">
    function logout() {
        $(window).attr('location', "<s:url value="j_spring_security_logout"/>");
    }
    
    function login() {
        $(window).attr('location', "<s:url value="loginView"/>");
    }
</script>

<table width="100%" border="0" cellspacing="8" cellpadding="0">

    <tr>

        <td width="75%" class="navigation_bg" style="padding:0 0 0 30px;">
           <%--  <sec:authorize access="isAuthenticated()"> --%>
                <div id="menu" style="float:left; width:70%;">
                    <ul class="menu">
                        <li><a href="<s:url action="home"/>"><span>Home</span></a></li>
                        <li><a href="<s:url action="employees"/>" class="parent"><span>People</span></a>

                            <div>
                                <ul>
                                    <li><a href="<s:url action="employeesByTeamView"/> "><span>Employee By Teams</span></a>
                                    </li>
                                    <li>
                                        <a href="<s:url action="scrumTeams"/> "><span>Perceptive Kolkata Scrum Teams</span></a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        
                        <sec:authorize access="hasRole('ROLE_PER_KOL_USER')">
                        <li><a href="#" class="parent" style="cursor: default"><span>INow License</span></a>
                               <div>
                               	<ul>
                               		<li><a href="<s:url action="showLicenseSummary"/>"><span>Licenses</span></a></li>
                                   
                                    <li><a href="<s:url action="viewImageNowLicenseRequest"/>"><span>ImageNow License Request</span></a></li>
                                    
                                    <li><a href="<s:url action="imageNowLicenseRequestedByMe"/>"><span>ImageNow License Requested By Me</span></a></li>                                   
                               	</ul>
                               </div>
                        </li>
                        </sec:authorize>
                        
						<sec:authorize access="hasRole('ROLE_ADMIN')">
                        <li><a href="#"
                               class="parent" style="cursor: default"><span>Administration</span></a>

                            <div>
                                <ul>
                                                                    
                                    
                                        <li>
                                            <a href="<s:url action="employeeGroupMapping"/>"><span>Change Employee Group</span></a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="viewEmployeeLicenseMap"/>"><span>License Allocation</span></a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="imageNowLicenseToProvide"/>"><span>Provide IN License</span></a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="rebuildCache"/>"><span>Rebuild Cache</span></a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="viewTrainingSchedule"/>"><span>Training Schedule</span></a>
                                        </li>
                                    

                                </ul>
                            </div>
                        </li>
                        </sec:authorize>
                        <li><a href="#"
                               class="parent" style="cursor: default"><span>Media</span></a>

                            <div>
                                <ul>
                                    <s:url id="videos" action="showPage">
                                        <s:param name="pageId">f626bb14-f595-11e1-8189-5c260a70a767</s:param>
                                    </s:url>
                                    <li><a href="<s:property value="%{videos}"/>"><span>Videos</span></a></li>
                                    <s:url id="picturegallery" action="showPage">
                                        <s:param name="pageId">284f42a4-f5a0-11e1-8189-5c260a70a767</s:param>
                                    </s:url>
                                    <li>
                                        <a href="<s:property value="%{picturegallery}"/>"><span>Picture Gallery</span></a>
                                    </li>
                                </ul>
                            </div>
                        </li>

                        <li><a href="#" class="parent" style="cursor: default"><span>Miscellaneous</span></a>

                            <div>
                                <ul>
                                    <s:url id="faq" action="showPage">
                                        <s:param name="pageId">a3c0b986-ec71-11e1-b28e-d0df9a41cc3f</s:param>
                                    </s:url>
                                    <li><a href="<s:property value="%{faq}"/>"><span>FAQ</span></a></li>
                                    <s:url id="quicklinks" action="showPage">
                                        <s:param name="pageId">9201b439-ed0a-11e1-aca7-5c260a70a767</s:param>
                                    </s:url>
                                    <li><a href="<s:property value="%{quicklinks}"/>"><span>Quick Links</span></a></li>
                                    <li>
                                        <a href="<s:url action="viewReports"/>"><span>Reports</span></a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
            <%-- </sec:authorize> --%>


            <div style="display:none;"><a href="http://apycom.com/">Apycom jQuery Menus</a></div>
            <sec:authorize access="isAuthenticated()">
                <div style="margin:6px 10px 0 0; color:#FFFFFF; width:15%; float:right; text-align:right;"><span
                        style="cursor: pointer" onclick="logout()">Logout</span></div>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <div style="margin:6px 10px 0 0; color:#FFFFFF; width:15%; float:right; text-align:right;"><span
                        style="cursor: pointer" onclick="login()">Login</span></div>
            </sec:authorize>
        </td>
    </tr>
</table>

