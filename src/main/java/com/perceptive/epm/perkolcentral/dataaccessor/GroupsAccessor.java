package com.perceptive.epm.perkolcentral.dataaccessor;

import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Groups;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19/8/12
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupsAccessor {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HashMap<Integer, GroupBO> getAllGroups() throws ExceptionWrapper {
        LinkedHashMap<Integer, GroupBO> allGroups = new LinkedHashMap<Integer, GroupBO>();
        try {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Groups.class);
            detachedCriteria.add(Restrictions.eq("isActive",Boolean.TRUE));
            detachedCriteria.addOrder(Order.asc("groupName"));
            for (Object item : hibernateTemplate.findByCriteria(detachedCriteria)) {
                Groups groups = (Groups) item;
                allGroups.put(groups.getGroupId(), new GroupBO(groups));
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return allGroups;
    }
}
