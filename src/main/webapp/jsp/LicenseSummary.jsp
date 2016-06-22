<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>
<s:url id="sessionExpiredPageURL" action="home">
</s:url>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">License Summary:
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text" align="center">
                        <!--License graph generate Starts--->
                        <s:div id="chartdiv"/>
                        <script type="text/javascript">
                            function populateTheDetails(selectedLicCatName) {
                                $("#id_selectedLicenseTypeName").val(selectedLicCatName);
                                $.publish('showThePeopleWithSpecifiedLicenseType');
                                $("#id_licensingDetails").dialog("open");
                            }
                            var chart = new FusionCharts("<s:url value="/js/FusionCharts/FCF_MSColumn3D.swf" />", "ChartId", "900", "600");
                            chart.setDataXML("<s:property value="licenseSummaryChartXML" escapeXml="false" escape="false" escapeHtml="false"/>");
                            chart.render("chartdiv");
                        </script>
                        <!--License graph generate ends-->
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>

<!--Code to show the licensing details-->
<form id="id_License">
    <s:hidden id="id_selectedLicenseTypeName" name="selectedLicenseTypeName"/>
</form>
<sj:dialog id="id_licensingDetails" title="License Details" autoOpen="false"
           closeOnEscape="true" modal="true" hideEffect="explode" height="500" width="1300">
    <div align="center">
        <s:url var="remoteurl" action="allEmployeesListByLicense" namespace="/ajax"/>
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
                reloadTopics="showThePeopleWithSpecifiedLicenseType"
                formIds="id_License"
                onErrorTopics="gridLoadError"
                >
            <sjg:gridColumn name="employeeId" index="employeeId" title="ID" sortable="false"/>
            <sjg:gridColumn name="employeeName" index="employeeName" title="Name" sortable="true"/>
            <sjg:gridColumn name="email" index="email" title="E-Mail" sortable="false"/>
        </sjg:grid>
        <script type="text/javascript">
            $.subscribe('gridLoadError', function (event, data) {
                if (event.originalEvent.status=='error' && event.originalEvent.error=='Forbidden') {
                    $(window).attr('location', "<s:property value="%{sessionExpiredPageURL}"/>");
                }
            })

        </script>
    </div>
</sj:dialog>
<!--Code to show the licensing details ends here-->
