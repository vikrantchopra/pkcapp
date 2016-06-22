package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.businessobject.LicenseBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Licensemaster;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 26/8/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseMasterDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HashMap<String, LicenseBO> getAllLicenseType() throws ExceptionWrapper {
        HashMap<String, LicenseBO> licenseBOHashMap = new HashMap<String, LicenseBO>();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Licensemaster.class);
            for (Object obj : hibernateTemplate.findByCriteria(criteria)) {
                LicenseBO licenseBO = new LicenseBO((Licensemaster) obj);
                licenseBOHashMap.put(licenseBO.getLicenseTypeId(), licenseBO);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return licenseBOHashMap;
    }
}
