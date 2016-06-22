package com.perceptive.epm.perkolcentral.bl;

import com.googlecode.ehcache.annotations.*;
import com.perceptive.epm.perkolcentral.businessobject.GroupBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import com.perceptive.epm.perkolcentral.dataaccessor.GroupsAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19/8/12
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupsBL {
    private GroupsAccessor groupsAccessor;

    public GroupsAccessor getGroupsAccessor() {
        return groupsAccessor;
    }

    public void setGroupsAccessor(GroupsAccessor groupsAccessor) {
        this.groupsAccessor = groupsAccessor;
    }

    @Cacheable(cacheName = "GroupCache", keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public HashMap<Integer, GroupBO> getAllGroups() throws ExceptionWrapper {
        try {
            return groupsAccessor.getAllGroups();
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    public ArrayList<GroupBO> getAllRallyGroups() throws ExceptionWrapper {
        try {
            ArrayList<GroupBO> groupBOArrayList = new ArrayList<GroupBO>(getAllGroups().values());
            CollectionUtils.filter(groupBOArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((GroupBO) o).getRallyGroup().booleanValue();
                }
            });
            return groupBOArrayList;
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

}
