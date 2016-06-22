package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employee;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Groups;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Imagenowlicenses;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 21/9/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageNowLicenseDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public LinkedHashMap<String, Imagenowlicenses> getAllImageNowLicenses() throws ExceptionWrapper {
        LinkedHashMap<String, Imagenowlicenses> imagenowlicensesLinkedHashMap = new LinkedHashMap<String, Imagenowlicenses>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Imagenowlicenses.class);
            criteria.createAlias("employeeByRequestedByEmployeeId", "emp");
            criteria.createAlias("groups", "group");
            criteria.addOrder(Order.asc("licenseRequestedOn"));
            criteria.setFetchMode("emp", FetchMode.JOIN);
            criteria.setFetchMode("group", FetchMode.JOIN);
            criteria.addOrder(Order.asc("licenseRequestedOn"));
            for (Object item : hibernateTemplate.findByCriteria(criteria)) {
                Imagenowlicenses imagenowlicenses = (Imagenowlicenses) item;
                imagenowlicensesLinkedHashMap.put(imagenowlicenses.getImageNowLicenseRequestId(), imagenowlicenses);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return imagenowlicensesLinkedHashMap;
    }

    public LinkedHashMap<String, Imagenowlicenses> getAllImageNowLicensesByRequestor(String employeeUID) throws ExceptionWrapper {
        LinkedHashMap<String, Imagenowlicenses> imagenowlicensesLinkedHashMap = new LinkedHashMap<String, Imagenowlicenses>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Imagenowlicenses.class);
            criteria.createAlias("employeeByRequestedByEmployeeId", "emp");
            criteria.createAlias("groups", "group");
            criteria.add(Restrictions.eq("emp.employeeUid", employeeUID));
            criteria.addOrder(Order.asc("licenseRequestedOn"));
            criteria.setFetchMode("emp", FetchMode.JOIN);
            criteria.setFetchMode("group", FetchMode.JOIN);
            for (Object item : hibernateTemplate.findByCriteria(criteria)) {
                Imagenowlicenses imagenowlicenses = (Imagenowlicenses) item;
                imagenowlicensesLinkedHashMap.put(imagenowlicenses.getImageNowLicenseRequestId(), imagenowlicenses);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return imagenowlicensesLinkedHashMap;
    }

    public Imagenowlicenses getAllImageNowLicensesByRequestId(String requestId) throws ExceptionWrapper {

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Imagenowlicenses.class);
            criteria.createAlias("employeeByRequestedByEmployeeId", "emp");
            criteria.createAlias("groups", "group");
            criteria.add(Restrictions.eq("imageNowLicenseRequestId", requestId.trim()));
            criteria.setFetchMode("emp", FetchMode.JOIN);
            criteria.setFetchMode("group", FetchMode.JOIN);

            return (Imagenowlicenses) hibernateTemplate.findByCriteria(criteria).get(0);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void addImageNowLicense(Imagenowlicenses imagenowlicenses, String groupRequestedFor, String employeeUIDWhoRequestedLicense) throws ExceptionWrapper {
        try {
            //Get The Group Info
            DetachedCriteria criteria = DetachedCriteria.forClass(Groups.class);
            criteria.add(Restrictions.eq("groupId", Integer.valueOf(groupRequestedFor)));
            imagenowlicenses.setGroups((Groups) hibernateTemplate.findByCriteria(criteria).get(0));
            criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("employeeUid", employeeUIDWhoRequestedLicense.trim()));
            imagenowlicenses.setEmployeeByRequestedByEmployeeId((Employee) hibernateTemplate.findByCriteria(criteria).get(0));
            hibernateTemplate.save(imagenowlicenses);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void updateImageNowLicense(Imagenowlicenses imagenowlicenses, String providerEmpUID) throws ExceptionWrapper {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
            criteria.add(Restrictions.eq("employeeUid", providerEmpUID.trim()));
            imagenowlicenses.setEmployeeByProvidedByEmployeeId((Employee) hibernateTemplate.findByCriteria(criteria).get(0));
            hibernateTemplate.update(imagenowlicenses);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public void deleteImageNowLicense(Imagenowlicenses imagenowlicenses) throws ExceptionWrapper {
        try {
            hibernateTemplate.delete(imagenowlicenses);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
}
