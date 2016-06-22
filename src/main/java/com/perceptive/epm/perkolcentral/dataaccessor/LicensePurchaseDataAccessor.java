package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Licensepurchase;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 25/8/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicensePurchaseDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public ArrayList<Licensepurchase> getAllLicensePurchaseInformation() throws ExceptionWrapper {
        Session session = hibernateTemplate.getSessionFactory().openSession();
        boolean isInTransaction = session.getTransaction() != null;
        Transaction tx = null;
        if (!isInTransaction) {
            tx = session.beginTransaction();
        }
        ArrayList<Licensepurchase> licensepurchases = new ArrayList<Licensepurchase>();
        try {
            Criteria criteria = session.createCriteria(Licensepurchase.class);
            criteria.setFetchMode("licensemaster", FetchMode.JOIN);
            for (Object obj : criteria.list()) {
                Licensepurchase licensepurchase = (Licensepurchase) obj;
                licensepurchases.add(licensepurchase);
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
        return licensepurchases;
    }
}
