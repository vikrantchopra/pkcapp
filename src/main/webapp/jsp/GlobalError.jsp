<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<p align="center">
    <img src="<s:url value="/images/alert2.png"/>" border="0" width="64" height="64"/><span style="font-weight: bold;color: #D80000;"> The application encountered an
    error.<span onclick='$("#id_errorDetails").dialog( "open" )' style="cursor: help"><u>Click here</u></span> for details.
    </span>
</p>
<sj:dialog id="id_errorDetails" title="Error Details" autoOpen="false"
           closeOnEscape="true" modal="true" showEffect="slide" hideEffect="explode" height="900" width="900">
    <s:property value="exceptionWrapper.strErrorStack"/>
</sj:dialog>
