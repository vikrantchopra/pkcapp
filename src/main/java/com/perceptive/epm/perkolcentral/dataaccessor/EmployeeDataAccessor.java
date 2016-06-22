package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.businessobject.LicenseBO;
import com.perceptive.epm.perkolcentral.businessobject.RoleBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.*;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 16/8/12
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }


    public LinkedHashMap<Long, EmployeeBO> getAllEmployees() throws ExceptionWrapper {
        LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();
        Session session = hibernateTemplate.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {

            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.eq("isActive", true));
            criteria.addOrder(Order.asc("employeeName"));

            for (Object item : criteria.list()) {
                Employee employee = (Employee) item;
                EmployeeBO employeeBO = new EmployeeBO(employee);

                for (Object obj : employee.getEmployeegroupmaps()) {
                    Employeegroupmap employeegroupmap = (Employeegroupmap) obj;

                    GroupBO groupBO = new GroupBO(employeegroupmap.getGroups());
                    employeeBO.getGroups().add(groupBO);
                }
                //==================================================================================================================
                //Get the license info for the employee
                for (Object obj : employee.getEmployeelicensemappings()) {
                    Employeelicensemapping employeelicensemapping = (Employeelicensemapping) obj;
                    LicenseBO licenseBO = new LicenseBO(employeelicensemapping.getLicensemaster());
                    employeeBO.getLicenses().add(licenseBO);
                }
                //==================================================================================================================
                //==================================================================================================================
                //Get the roles for the employee
                for (Employeerolemapping employeerolemapping : employee.getEmployeerolemappings()) {
                    RoleBO roleBO = new RoleBO(employeerolemapping.getRolemaster());
                    employeeBO.getRoles().add(roleBO);
                }
                //==================================================================================================================
                employeeLinkedHashMap.put(Long.valueOf(employee.getEmployeeId()), employeeBO);
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        } finally {
            if (tx.isActive())
                tx.commit();
            if (session.isOpen())
                session.close();
        }
        return employeeLinkedHashMap;
    }

    public void addEmployeeGroupMap(String employeeId, String groupId) throws ExceptionWrapper {
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("employeeId", Long.parseLong(employeeId.trim())));
            Employee employee = (Employee) hibernateTemplate.findByCriteria(criteria).get(0);

            criteria = DetachedCriteria.forClass(Groups.class);
            criteria.add(Restrictions.eq("groupId", Integer.valueOf(groupId.trim())));
            Groups groups = (Groups) hibernateTemplate.findByCriteria(criteria).get(0);

            Employeegroupmap employeegroupmap = new Employeegroupmap(employee, groups);
            hibernateTemplate.save(employeegroupmap);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void deleteEmployeeGroupMap(Employeegroupmap employeegroupmap) throws ExceptionWrapper {
        try {
            hibernateTemplate.delete(employeegroupmap);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public ArrayList<Employeegroupmap> getEmployeegroupmapByEmployeeID(String employeeId) throws ExceptionWrapper {
        ArrayList<Employeegroupmap> employeegroupmapArrayList = new ArrayList<Employeegroupmap>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employeegroupmap.class);
            criteria.createAlias("employee", "emp");
            criteria.add(Restrictions.eq("emp.employeeId", Long.valueOf(employeeId)));
            criteria.setFetchMode("emp", FetchMode.JOIN);
            criteria.setFetchMode("groups", FetchMode.JOIN);
            for (Object item : hibernateTemplate.findByCriteria(criteria)) {
                employeegroupmapArrayList.add((Employeegroupmap) item);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return employeegroupmapArrayList;
    }

    public void addEmployeeLicenseMap(String employeeId, String licenseId) throws ExceptionWrapper {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("employeeId", Long.parseLong(employeeId.trim())));
            Employee employee = (Employee) hibernateTemplate.findByCriteria(criteria).get(0);

            criteria = DetachedCriteria.forClass(Licensemaster.class);
            criteria.add(Restrictions.eq("licenseTypeId", Long.valueOf(licenseId.trim())));
            Licensemaster licensemaster = (Licensemaster) hibernateTemplate.findByCriteria(criteria).get(0);
            Employeelicensemapping employeelicensemapping = new Employeelicensemapping(employee, licensemaster);
            hibernateTemplate.save(employeelicensemapping);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void deleteEmployeeLicenseMap(Employeelicensemapping employeelicensemapping) throws ExceptionWrapper {
        try {
            hibernateTemplate.delete(employeelicensemapping);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public ArrayList<Employeelicensemapping> getEmployeeLicenseMapByEmployeeId(String employeeId) throws ExceptionWrapper {
        ArrayList<Employeelicensemapping> employeelicensemappingArrayList = new ArrayList<Employeelicensemapping>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employeelicensemapping.class);
            criteria.createAlias("employee", "emp");
            criteria.add(Restrictions.eq("emp.employeeId", Long.valueOf(employeeId)));
            criteria.setFetchMode("emp", FetchMode.JOIN);
            criteria.setFetchMode("licensemaster", FetchMode.JOIN);
            for (Object item : hibernateTemplate.findByCriteria(criteria)) {
                employeelicensemappingArrayList.add((Employeelicensemapping) item);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return employeelicensemappingArrayList;
    }

    public void addEmployee(Employee employee) throws ExceptionWrapper {
        try {
            hibernateTemplate.save(employee);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void updateEmployee(EmployeeBO employeeBO) throws ExceptionWrapper {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("employeeId", Long.parseLong(employeeBO.getEmployeeId())));
            criteria.setFetchMode("employeelicensemappings", FetchMode.JOIN);
            criteria.setFetchMode("employeegroupmaps", FetchMode.JOIN);
            criteria.setFetchMode("employeerolemappings", FetchMode.JOIN);

            Employee employee = (Employee) hibernateTemplate.findByCriteria(criteria).get(0);

            employee.setEmail(employeeBO.getEmail());
            employee.setEmployeeName(employeeBO.getEmployeeName());
            employee.setEmployeeUid(employeeBO.getEmployeeUid());
            employee.setJobTitle(employeeBO.getJobTitle());
            employee.setMobileNumber(employeeBO.getMobileNumber());
            employee.setManager(employeeBO.getManager());
            employee.setManagerEmail(employeeBO.getManagerEmail());
            //employee.setExtensionNum(employeeBO.getExtensionNum());
            //employee.setWorkspace(employeeBO.getWorkspace());
            employee.setIsActive(employeeBO.isActive());
            hibernateTemplate.update(employee);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public LinkedHashMap<Long, EmployeeBO> getAllEmployeesByTemplate() throws ExceptionWrapper {
        LinkedHashMap<Long, EmployeeBO> employeeLinkedHashMap = new LinkedHashMap<Long, EmployeeBO>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("isActive", true));
            criteria.addOrder(Order.asc("employeeName"));


            for (Object item : hibernateTemplate.findByCriteria(criteria)) {
                Employee employee = (Employee) item;
                EmployeeBO employeeBO = new EmployeeBO(employee);

                //Get the group information for the employee
                for (Object obj : employee.getEmployeegroupmaps()) {
                    Employeegroupmap employeegroupmap = (Employeegroupmap) obj;
                    employeegroupmap = hibernateTemplate.get(Employeegroupmap.class, employeegroupmap.getEmployeeGroupMappingId());
                    GroupBO groupBO = new GroupBO(employeegroupmap.getGroups());
                    employeeBO.getGroups().add(groupBO);
                }
                //==================================================================================================================
                //Get the license info for the employee
                for (Object obj : employee.getEmployeelicensemappings()) {
                    Employeelicensemapping employeelicensemapping = (Employeelicensemapping) obj;
                    LicenseBO licenseBO = new LicenseBO(employeelicensemapping.getLicensemaster());
                    employeeBO.getLicenses().add(licenseBO);
                }
                //==================================================================================================================
                employeeLinkedHashMap.put(Long.valueOf(employee.getEmployeeId()), employeeBO);
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return employeeLinkedHashMap;
    }


}
