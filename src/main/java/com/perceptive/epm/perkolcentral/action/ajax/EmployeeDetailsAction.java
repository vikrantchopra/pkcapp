package com.perceptive.epm.perkolcentral.action.ajax;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.businessobject.LicenseBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.apache.commons.codec.StringEncoderComparator;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 17/8/12
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeDetailsAction extends ActionSupport {


    private ArrayList<EmployeeBO> gridModel;
    private String selectedGroupId;
    private String selectedLicenseTypeName;
    private EmployeeBL employeeBL;


    // get how many rows we want to have into the grid - rowNum attribute in the
    // grid
    private Integer rows = 0;

    // Get the requested page. By default grid sets this to 1.
    private Integer page = 0;

    // sorting order - asc or desc
    private String sord = "asc";

    // get index row - i.e. user click to sort.
    private String sidx;

    // Search Field
    private String searchField;

    // The Search String
    private String searchString;

    // he Search Operation
    // ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
    private String searchOper;

    // Your Total Pages
    private Integer total = 0;

    // All Records
    private Integer records = 0;

    //For Employee Autocomplete
    private String term;

    @JSON(serialize = false)
    public EmployeeBL getEmployeeBL() {
        return employeeBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public ArrayList<EmployeeBO> getGridModel() {
        return gridModel;
    }

    public void setGridModel(ArrayList<EmployeeBO> gridModel) {
        this.gridModel = gridModel;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchOper() {
        return searchOper;
    }

    public void setSearchOper(String searchOper) {
        this.searchOper = searchOper;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public String getSelectedGroupId() {
        return selectedGroupId;
    }

    public void setSelectedGroupId(String selectedGroupId) {
        this.selectedGroupId = selectedGroupId;
    }

    public String getSelectedLicenseTypeName() {
        return selectedLicenseTypeName;
    }

    public void setSelectedLicenseTypeName(String selectedLicenseTypeName) {
        this.selectedLicenseTypeName = selectedLicenseTypeName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String executeGetAllEmployees() throws ExceptionWrapper {
        try {
            Soundex sndx = new Soundex();
            DoubleMetaphone doubleMetaphone = new DoubleMetaphone();
            final StringEncoderComparator comparator1 = new StringEncoderComparator(doubleMetaphone);

            LoggingHelpUtil.printDebug("Page " + getPage() + " Rows " + getRows() + " Sorting Order " + getSord() + " Index Row :" + getSidx());
            LoggingHelpUtil.printDebug("Search :" + searchField + " " + searchOper + " " + searchString);

            // Calcalate until rows ware selected
            int to = (rows * page);

            // Calculate the first row to read
            int from = to - rows;
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

            employeeLinkedHashMap = employeeBL.getAllEmployees();
            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            //Handle search
            if (searchOper != null && !searchOper.trim().equalsIgnoreCase("") && searchString != null && !searchString.trim().equalsIgnoreCase("")) {
                if (searchOper.trim().equalsIgnoreCase("eq")) {
                    CollectionUtils.filter(allEmployees, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            return ((EmployeeBO) o).getEmployeeName().equalsIgnoreCase(searchString.trim());  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    });
                } else if (searchOper.trim().equalsIgnoreCase("slk")) {
                    CollectionUtils.filter(allEmployees, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            return (new StringEncoderComparator(new Soundex()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0
                                    || new StringEncoderComparator(new DoubleMetaphone()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0
                                    || new StringEncoderComparator(new Metaphone()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0
                                    || new StringEncoderComparator(new RefinedSoundex()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0);  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    });
                } else {
                    //First check whether there is an exact match
                    if (CollectionUtils.exists(allEmployees, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            return (((EmployeeBO) o).getEmployeeName().toLowerCase().contains(searchString.trim().toLowerCase()));  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    })) {
                        CollectionUtils.filter(allEmployees, new Predicate() {
                            @Override
                            public boolean evaluate(Object o) {
                                return (((EmployeeBO) o).getEmployeeName().toLowerCase().contains(searchString.trim().toLowerCase()));
                            }
                        });
                    } else {
                        ArrayList<String> matchedEmployeeIds = employeeBL.getLuceneUtil().getBestMatchEmployeeName(searchString.trim().toLowerCase());
                        allEmployees = new ArrayList<EmployeeBO>();
                        for (String id : matchedEmployeeIds) {
                            allEmployees.add(employeeBL.getAllEmployees().get(Long.valueOf(id)));
                        }
                    }
                }

                /*{
                    CollectionUtils.filter(allEmployees, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            if (((EmployeeBO) o).getEmployeeName().toLowerCase().contains(searchString.trim().toLowerCase()))
                                return true;
                            else if(new StringEncoderComparator(new Soundex()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0
                                    || new StringEncoderComparator(new DoubleMetaphone()).compare(((EmployeeBO) o).getEmployeeName().toLowerCase(), searchString.trim().toLowerCase()) == 0)
                            {
                                return true;
                            }
                            else {
                                for (String empNameParts : ((EmployeeBO) o).getEmployeeName().trim().split(" ")) {
                                    if (new StringEncoderComparator(new Soundex()).compare(empNameParts.toLowerCase(), searchString.trim().toLowerCase()) == 0
                                            || new StringEncoderComparator(new DoubleMetaphone()).compare(empNameParts.toLowerCase(), searchString.trim().toLowerCase()) == 0
                                        //    || new StringEncoderComparator(new Metaphone()).compare(empNameParts.toLowerCase(), searchString.trim().toLowerCase()) == 0
                                        //    || new StringEncoderComparator(new RefinedSoundex()).compare(empNameParts.toLowerCase(), searchString.trim().toLowerCase()) == 0
                                            ) {
                                        return true;
                                    }
                                }
                                return false;
                            }


                        }
                    });
                } */
            }
            //// Handle Order By
            if (sidx != null && !sidx.equals("")) {


                Collections.sort(allEmployees, new Comparator<EmployeeBO>() {
                    public int compare(EmployeeBO e1, EmployeeBO e2) {
                        if (sidx.equalsIgnoreCase("employeeName"))
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                        else if (sidx.equalsIgnoreCase("jobTitle"))
                            return sord.equalsIgnoreCase("asc") ? e1.getJobTitle().compareTo(e2.getJobTitle()) : e2.getJobTitle().compareTo(e1.getJobTitle());
                        else if (sidx.equalsIgnoreCase("manager"))
                            return sord.equalsIgnoreCase("asc") ? e1.getManager().compareTo(e2.getManager()) : e2.getManager().compareTo(e1.getManager());
                        else
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                    }
                });

            }
            //

            records = allEmployees.size();
            total = (int) Math.ceil((double) records / (double) rows);

            gridModel = new ArrayList<EmployeeBO>();
            to = to > records ? records : to;
            for (int iCounter = from; iCounter < to; iCounter++) {
                EmployeeBO employeeBO = allEmployees.get(iCounter);
                //new EmployeeBO((Employee) employeeLinkedHashMap.values().toArray()[iCounter]);
                gridModel.add(employeeBO);
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);

        }
        return SUCCESS;
    }

    public String executeEmployeesByTeamAjax() throws ExceptionWrapper {
        try {
            LoggingHelpUtil.printDebug("Page " + getPage() + " Rows " + getRows() + " Sorting Order " + getSord() + " Index Row :" + getSidx());
            LoggingHelpUtil.printDebug("Search :" + searchField + " " + searchOper + " " + searchString);

            // Calcalate until rows ware selected
            int to = (rows * page);

            // Calculate the first row to read
            int from = to - rows;
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

            employeeLinkedHashMap = employeeBL.getAllEmployees();

            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            CollectionUtils.filter(allEmployees, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    EmployeeBO emp = (EmployeeBO) o;
                    if (CollectionUtils.exists(emp.getGroups(), new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            return ((GroupBO) o).getGroupId().equalsIgnoreCase(selectedGroupId); //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }))
                        return true;
                    else
                        return false;
                }
            });

            //// Handle Order By
            if (sidx != null && !sidx.equals("")) {


                Collections.sort(allEmployees, new Comparator<EmployeeBO>() {
                    public int compare(EmployeeBO e1, EmployeeBO e2) {
                        if (sidx.equalsIgnoreCase("employeeName"))
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                        else if (sidx.equalsIgnoreCase("jobTitle"))
                            return sord.equalsIgnoreCase("asc") ? e1.getJobTitle().compareTo(e2.getJobTitle()) : e2.getJobTitle().compareTo(e1.getJobTitle());
                        else if (sidx.equalsIgnoreCase("manager"))
                            return sord.equalsIgnoreCase("asc") ? e1.getManager().compareTo(e2.getManager()) : e2.getManager().compareTo(e1.getManager());
                        else
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                    }
                });

            }
            //

            records = allEmployees.size();
            total = (int) Math.ceil((double) records / (double) rows);

            gridModel = new ArrayList<EmployeeBO>();
            to = to > records ? records : to;
            for (int iCounter = from; iCounter < to; iCounter++) {
                EmployeeBO employeeBO = allEmployees.get(iCounter);
                //new EmployeeBO((Employee) employeeLinkedHashMap.values().toArray()[iCounter]);
                gridModel.add(employeeBO);
            }


        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeEmployeesByLicenseAjax() throws ExceptionWrapper {
        try {
            LoggingHelpUtil.printDebug("Page " + getPage() + " Rows " + getRows() + " Sorting Order " + getSord() + " Index Row :" + getSidx());
            LoggingHelpUtil.printDebug("Search :" + searchField + " " + searchOper + " " + searchString);

            // Calcalate until rows ware selected
            int to = (rows * page);

            // Calculate the first row to read
            int from = to - rows;
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();
            employeeLinkedHashMap = employeeBL.getAllEmployees();

            ArrayList<EmployeeBO> allEmployees = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
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

            //// Handle Order By
            if (sidx != null && !sidx.equals("")) {


                Collections.sort(allEmployees, new Comparator<EmployeeBO>() {
                    public int compare(EmployeeBO e1, EmployeeBO e2) {
                        if (sidx.equalsIgnoreCase("employeeName"))
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                        else if (sidx.equalsIgnoreCase("jobTitle"))
                            return sord.equalsIgnoreCase("asc") ? e1.getJobTitle().compareTo(e2.getJobTitle()) : e2.getJobTitle().compareTo(e1.getJobTitle());
                        else
                            return sord.equalsIgnoreCase("asc") ? e1.getEmployeeName().compareTo(e2.getEmployeeName()) : e2.getEmployeeName().compareTo(e1.getEmployeeName());
                    }
                });

            }
            //

            records = allEmployees.size();
            total = (int) Math.ceil((double) records / (double) rows);

            gridModel = new ArrayList<EmployeeBO>();
            to = to > records ? records : to;
            for (int iCounter = from; iCounter < to; iCounter++) {
                EmployeeBO employeeBO = allEmployees.get(iCounter);
                //new EmployeeBO((Employee) employeeLinkedHashMap.values().toArray()[iCounter]);
                gridModel.add(employeeBO);
            }


        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

    public String executeGetAllEmployeesAtOnce() throws ExceptionWrapper {
        try {
            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

            employeeLinkedHashMap = employeeBL.getAllEmployees();
            gridModel = new ArrayList<EmployeeBO>(employeeLinkedHashMap.values());
            CollectionUtils.filter(gridModel, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return StringUtils.contains(((EmployeeBO) o).getEmployeeName().toLowerCase(), term.toLowerCase());
                }
            });
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }

}


