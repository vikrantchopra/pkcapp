package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Pages;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 22/8/12
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PageDataAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HashMap<String, Pages> getAllPages() throws ExceptionWrapper {
        HashMap<String, Pages> pagesHashMap = new HashMap<String, Pages>();
        try {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Pages.class);
            for (Object item : hibernateTemplate.findByCriteria(detachedCriteria)) {
                Pages pages = (Pages) item;
                pagesHashMap.put(pages.getPageId(), pages);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return pagesHashMap;
    }
}
