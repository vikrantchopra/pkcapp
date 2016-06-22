package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Employeelicensemapping;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 26/8/12
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLicenseDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public ArrayList<String> getEmployeeIdsByLicenseId(java.lang.String licenseTypeId) throws ExceptionWrapper {
        ArrayList<String> employeeIds = new ArrayList<String>();
        Session session = hibernateTemplate.getSessionFactory().openSession();
        boolean isInTransaction = session.getTransaction() != null;
        Transaction tx = null;
        if (!isInTransaction) {
            tx = session.beginTransaction();
        }
        try {
            Criteria criteria = session.createCriteria(Employeelicensemapping.class);

            criteria.createAlias("employee", "emp");
            criteria.add(Restrictions.eq("licensemaster.licenseTypeId", Long.valueOf(licenseTypeId)));
            criteria.add(Restrictions.like("emp.isActive", true));
            for (Object obj : criteria.list()) {
                Employeelicensemapping employeelicensemapping = (Employeelicensemapping) obj;
                employeeIds.add(Long.toString(employeelicensemapping.getEmployee().getEmployeeId()));
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        } finally {
            {
                if (!isInTransaction && tx.isActive()) {
                    tx.commit();
                }
                session.close();
            }
        }
        return employeeIds;
    }
}
