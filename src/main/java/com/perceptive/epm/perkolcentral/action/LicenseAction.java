package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.LicensesBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 26/8/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseAction extends ActionSupport {
    private LicensesBL licensesBL;
    private String licenseSummaryChartXML;


    public LicensesBL getLicensesBL() {
        return licensesBL;
    }

    public void setLicensesBL(LicensesBL licensesBL) {
        this.licensesBL = licensesBL;
    }

    public String getLicenseSummaryChartXML() {
        return licenseSummaryChartXML;
    }

    public void setLicenseSummaryChartXML(String licenseSummaryChartXML) {
        this.licenseSummaryChartXML = licenseSummaryChartXML;
    }

    public String executeLicenseSummaryView() throws ExceptionWrapper {
        try {
            licenseSummaryChartXML = licensesBL.getLicenseSummaryChartData();
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
