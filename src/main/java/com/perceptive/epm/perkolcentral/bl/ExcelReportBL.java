package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.businessobject.LicenseBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 25/9/12
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelReportBL {
    private EmployeeBL employeeBL;
    private GroupsBL groupsBL;
    private LicensesBL licensesBL;

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public void setLicensesBL(LicensesBL licensesBL) {
        this.licensesBL = licensesBL;
    }

    private void setCellBorder(Workbook wb, Cell cell) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("Georgia");
        style.setFont(font);

        style.setWrapText(true);
        style.setAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        cell.setCellStyle(style);
        cell.setCellType(Cell.CELL_TYPE_STRING);
    }

    public FileInputStream generateAllEmployeeReport(File file) throws ExceptionWrapper {
        try {
            FileInputStream inp = new FileInputStream(file);
            //InputStream inp = new FileInputStream("workbook.xlsx");

            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);

            int iRowCounter = 1;
            for (EmployeeBO employeeBO : employeeBL.getAllEmployees().values()) {
                Row row = sheet.getRow(iRowCounter);
                if (row == null)
                    row = sheet.createRow(iRowCounter);
                Cell cell = row.getCell(0);
                if (cell == null)
                    cell = row.createCell(0);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getEmployeeId());

                cell = row.getCell(1);
                if (cell == null)
                    cell = row.createCell(1);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getEmployeeUid());

                cell = row.getCell(2);
                if (cell == null)
                    cell = row.createCell(2);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getEmployeeName());

                cell = row.getCell(3);
                if (cell == null)
                    cell = row.createCell(3);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getEmail());

                cell = row.getCell(4);
                if (cell == null)
                    cell = row.createCell(4);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getManager());

                cell = row.getCell(5);
                if (cell == null)
                    cell = row.createCell(5);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getMobileNumber());

                cell = row.getCell(6);
                if (cell == null)
                    cell = row.createCell(6);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getExtensionNum());

                cell = row.getCell(7);
                if (cell == null)
                    cell = row.createCell(7);
                setCellBorder(wb, cell);
                cell.setCellValue(employeeBO.getWorkspace());

                iRowCounter = iRowCounter + 1;
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
            return new FileInputStream(file);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }

    }

    public FileInputStream generateAllEmployeeTeamWiseReport(File file, Boolean isScrumTeam) throws ExceptionWrapper {
        try {
            FileInputStream inp = new FileInputStream(file);
            //InputStream inp = new FileInputStream("workbook.xlsx");

            Workbook wb = WorkbookFactory.create(inp);
            int iSheetCounter = 1;
            for (Integer groupID : employeeBL.getAllEmployeesKeyedByGroupId().keySet()) {
                GroupBO groupBO = groupsBL.getAllGroups().get(groupID);
                if (isScrumTeam && !groupBO.getRallyGroup())
                    continue;
                if (!isScrumTeam && groupBO.getRallyGroup())
                    continue;

                Sheet sheet = wb.cloneSheet(0);
                wb.setSheetName(wb.getSheetIndex(sheet), groupBO.getGroupName());
                //wb.setSheetName(iSheetCounter,groupBO.getGroupName());

                int iRowCounter = 1;
                for (EmployeeBO employeeBO : employeeBL.getAllEmployeesKeyedByGroupId().get(groupID)) {
                    Row row = sheet.getRow(iRowCounter);
                    if (row == null)
                        row = sheet.createRow(iRowCounter);
                    Cell cell = row.getCell(0);
                    if (cell == null)
                        cell = row.createCell(0);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeId());

                    cell = row.getCell(1);
                    if (cell == null)
                        cell = row.createCell(1);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeUid());

                    cell = row.getCell(2);
                    if (cell == null)
                        cell = row.createCell(2);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeName());

                    cell = row.getCell(3);
                    if (cell == null)
                        cell = row.createCell(3);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmail());

                    cell = row.getCell(4);
                    if (cell == null)
                        cell = row.createCell(4);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getManager());

                    cell = row.getCell(5);
                    if (cell == null)
                        cell = row.createCell(5);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getMobileNumber());

                    cell = row.getCell(6);
                    if (cell == null)
                        cell = row.createCell(6);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getExtensionNum());

                    cell = row.getCell(7);
                    if (cell == null)
                        cell = row.createCell(7);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getWorkspace());

                    if (isScrumTeam) {
                        cell = row.getCell(8);
                        if (cell == null)
                            cell = row.createCell(8);
                        setCellBorder(wb, cell);
                        cell.setCellValue(employeeBO.getSpecificRoleInScrumTeam());
                    }

                    iRowCounter = iRowCounter + 1;
                }
            }
            iSheetCounter = iSheetCounter + 1;
            wb.removeSheetAt(0);
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
            return new FileInputStream(file);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }

    }

    public FileInputStream generateLicenseReport(File file) throws ExceptionWrapper {
        try {
            FileInputStream inp = new FileInputStream(file);
            //InputStream inp = new FileInputStream("workbook.xlsx");

            Workbook wb = WorkbookFactory.create(inp);
            HashMap<String, ArrayList<String>> licenseInfoKeyedByLicenseName = licensesBL.getLicenseRelatedInfo();
            //Create The Summary Sheet
            Sheet sheetSummary = wb.getSheetAt(1);
            int iSummaryRowCounter = 1;
            for (String licenseType : licenseInfoKeyedByLicenseName.keySet()) {

                Row row = sheetSummary.getRow(iSummaryRowCounter);
                if (row == null)
                    row = sheetSummary.createRow(iSummaryRowCounter);
                Cell cell = row.getCell(0);
                if (cell == null)
                    cell = row.createCell(0);
                setCellBorder(wb, cell);
                cell.setCellValue(licenseType);

                row = sheetSummary.getRow(iSummaryRowCounter);
                if (row == null)
                    row = sheetSummary.createRow(iSummaryRowCounter);
                cell = row.getCell(1);
                if (cell == null)
                    cell = row.createCell(1);
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(0)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                row = sheetSummary.getRow(iSummaryRowCounter);
                if (row == null)
                    row = sheetSummary.createRow(iSummaryRowCounter);
                cell = row.getCell(2);
                if (cell == null)
                    cell = row.createCell(2);
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(1)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                row = sheetSummary.getRow(iSummaryRowCounter);
                if (row == null)
                    row = sheetSummary.createRow(iSummaryRowCounter);
                cell = row.getCell(3);
                if (cell == null)
                    cell = row.createCell(3);
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(0)) - Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(1)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                iSummaryRowCounter=iSummaryRowCounter+1;
            }
            int iSheetCounter = 1;

            for (String licenseType : licenseInfoKeyedByLicenseName.keySet()) {
                Sheet sheet = wb.cloneSheet(0);
                wb.setSheetName(wb.getSheetIndex(sheet), licenseType);
                CellReference cellReference = new CellReference("B1");
                Row row = sheet.getRow(cellReference.getRow());
                Cell cell = row.getCell(cellReference.getCol());
                if (cell == null)
                    cell = row.createCell(cellReference.getCol());
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(0)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                cellReference = new CellReference("B2");
                row = sheet.getRow(cellReference.getRow());
                cell = row.getCell(cellReference.getCol());
                if (cell == null)
                    cell = row.createCell(cellReference.getCol());
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(1)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                cellReference = new CellReference("B3");
                row = sheet.getRow(cellReference.getRow());
                cell = row.getCell(cellReference.getCol());
                if (cell == null)
                    cell = row.createCell(cellReference.getCol());
                setCellBorder(wb, cell);
                cell.setCellValue(Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(0)) - Double.parseDouble(licenseInfoKeyedByLicenseName.get(licenseType).get(1)));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);


                ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeBL.getAllEmployees().values());
                final String selectedLicenseTypeName = licenseType;
                CollectionUtils.filter(allEmployees, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        EmployeeBO emp = (EmployeeBO) o;
                        if (CollectionUtils.exists(emp.getLicenses(), new Predicate() {
                            @Override
                            public boolean evaluate(Object o) {
                                return ((LicenseBO) o).getLicenseTypeName().equalsIgnoreCase(selectedLicenseTypeName); //To change body of implemented methods use File | Settings | File Templates.
                            }
                        }))
                            return true;
                        else
                            return false;
                    }
                });

                int iRowCounter = 5;
                for (EmployeeBO employeeBO : allEmployees) {
                    row = sheet.getRow(iRowCounter);
                    if (row == null)
                        row = sheet.createRow(iRowCounter);
                    cell = row.getCell(0);
                    if (cell == null)
                        cell = row.createCell(0);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeId());

                    cell = row.getCell(1);
                    if (cell == null)
                        cell = row.createCell(1);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeUid());

                    cell = row.getCell(2);
                    if (cell == null)
                        cell = row.createCell(2);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmployeeName());

                    cell = row.getCell(3);
                    if (cell == null)
                        cell = row.createCell(3);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getEmail());

                    cell = row.getCell(4);
                    if (cell == null)
                        cell = row.createCell(4);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getManager());

                    cell = row.getCell(5);
                    if (cell == null)
                        cell = row.createCell(5);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getMobileNumber());

                    cell = row.getCell(6);
                    if (cell == null)
                        cell = row.createCell(6);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getExtensionNum());

                    cell = row.getCell(7);
                    if (cell == null)
                        cell = row.createCell(7);
                    setCellBorder(wb, cell);
                    cell.setCellValue(employeeBO.getWorkspace());

                    iRowCounter = iRowCounter + 1;
                }
            }
            iSheetCounter = iSheetCounter + 1;
            wb.removeSheetAt(0);
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
            return new FileInputStream(file);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }

    }
}
