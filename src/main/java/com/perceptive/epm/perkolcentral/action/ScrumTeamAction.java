package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.GroupsBL;
import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 27/8/12
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScrumTeamAction extends ActionSupport {
    private GroupsBL groupsBL;
    private EmployeeBL employeeBL;
    private LinkedHashMap<String, ArrayList<EmployeeBO>> employeeKeyedByScrumTeam;

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public HashMap<String, ArrayList<EmployeeBO>> getEmployeeKeyedByScrumTeam() {
        return employeeKeyedByScrumTeam;
    }

    public String executeShowScrumTeams() throws ExceptionWrapper {
        try {
            employeeKeyedByScrumTeam = new LinkedHashMap<String, ArrayList<EmployeeBO>>();
            ArrayList<GroupBO> groupBOArrayList = new ArrayList<GroupBO>(groupsBL.getAllGroups().values());
            CollectionUtils.filter(groupBOArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((GroupBO) o).getRallyGroup();
                }
            });

            Collections.sort(groupBOArrayList, new Comparator<GroupBO>() {
                @Override
                public int compare(GroupBO o1, GroupBO o2) {
                    return o1.getGroupName().compareToIgnoreCase(o2.getGroupName());
                }
            });

            LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();
            employeeLinkedHashMap = employeeBL.getAllEmployees();

            for (Object item : groupBOArrayList) {
                final String selectedGroupId = ((GroupBO) item).getGroupId();
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
                Collections.sort(allEmployees, new Comparator<EmployeeBO>() {
                    @Override
                    public int compare(EmployeeBO o1, EmployeeBO o2) {
                        return o1.getEmployeeName().compareToIgnoreCase(o2.getEmployeeName());
                    }
                });
                employeeKeyedByScrumTeam.put(((GroupBO) item).getGroupName(), allEmployees);
            }


        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
