package com.perceptive.epm.perkolcentral.action;


import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.ExcelReportBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 25/9/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelReportAction extends ActionSupport {
    private String reportKey;
    private InputStream reportInputStream;
    private ExcelReportBL excelReportBL;
    private String pathToExcelReportTemplateFolder;
    private String reportName;

    public String getReportKey() {
        return reportKey;
    }

    public void setReportKey(String reportKey) {
        this.reportKey = reportKey;
    }

    public InputStream getReportInputStream() {
        return reportInputStream;
    }

    public void setReportInputStream(InputStream reportInputStream) {
        this.reportInputStream = reportInputStream;
    }

    public void setExcelReportBL(ExcelReportBL excelReportBL) {
        this.excelReportBL = excelReportBL;
    }

    public void setPathToExcelReportTemplateFolder(String pathToExcelReportTemplateFolder) {
        this.pathToExcelReportTemplateFolder = pathToExcelReportTemplateFolder;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String execute() throws ExceptionWrapper {
        try {

            if (reportKey.equalsIgnoreCase(Constants.REPORT_ALL_USER)) {
                reportName = Constants.REPORT_ALL_USER + ".xlsx";
                File tempFile = new File(FilenameUtils.concat(FileUtils.getTempDirectoryPath(), UUID.randomUUID() + ".xlsx"));
                FileUtils.copyInputStreamToFile(ServletActionContext.getServletContext().getResourceAsStream(String.format("%s/%s", pathToExcelReportTemplateFolder, "AllEmployeeReportTemplate.xlsx")), tempFile);
                reportInputStream = excelReportBL.generateAllEmployeeReport(tempFile);
                FileUtils.deleteQuietly(tempFile);
            }
            if (reportKey.equalsIgnoreCase(Constants.REPORT_ALL_USER_SCRUM_TEAM_WISE)) {
                reportName = Constants.REPORT_ALL_USER_SCRUM_TEAM_WISE + ".xlsx";
                File tempFile = new File(FilenameUtils.concat(FileUtils.getTempDirectoryPath(), UUID.randomUUID() + ".xlsx"));
                FileUtils.copyInputStreamToFile(ServletActionContext.getServletContext().getResourceAsStream(String.format("%s/%s", pathToExcelReportTemplateFolder, "AllEmployeeScrumTeamWiseReportTemplate.xlsx")), tempFile);
                reportInputStream = excelReportBL.generateAllEmployeeTeamWiseReport(tempFile,true);
                FileUtils.deleteQuietly(tempFile);
            }
            if (reportKey.equalsIgnoreCase(Constants.REPORT_ALL_USER_NONSCRUM_TEAM_WISE)) {
                reportName = Constants.REPORT_ALL_USER_NONSCRUM_TEAM_WISE + ".xlsx";
                File tempFile = new File(FilenameUtils.concat(FileUtils.getTempDirectoryPath(), UUID.randomUUID() + ".xlsx"));
                FileUtils.copyInputStreamToFile(ServletActionContext.getServletContext().getResourceAsStream(String.format("%s/%s", pathToExcelReportTemplateFolder, "AllEmployeeReportTemplate.xlsx")), tempFile);
                reportInputStream = excelReportBL.generateAllEmployeeTeamWiseReport(tempFile,false);
                FileUtils.deleteQuietly(tempFile);
            }
            if (reportKey.equalsIgnoreCase(Constants.REPORT_LICENSE)) {
                reportName = Constants.REPORT_LICENSE + ".xlsx";
                File tempFile = new File(FilenameUtils.concat(FileUtils.getTempDirectoryPath(), UUID.randomUUID() + ".xlsx"));
                FileUtils.copyInputStreamToFile(ServletActionContext.getServletContext().getResourceAsStream(String.format("%s/%s", pathToExcelReportTemplateFolder, "LicenseReportTemplate.xlsx")), tempFile);
                reportInputStream = excelReportBL.generateLicenseReport(tempFile);
                FileUtils.deleteQuietly(tempFile);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
