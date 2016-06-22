package com.perceptive.epm.perkolcentral.bl;

import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 8/10/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheBL {
    private EmployeeBL employeeBL;
    private GroupsBL groupsBL;


    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void setGroupsBL(GroupsBL groupsBL) {
        this.groupsBL = groupsBL;
    }

    @TriggersRemove(cacheName = {"EmployeeCache", "GroupCache", "EmployeeKeyedByGroupCache"}, when = When.BEFORE_METHOD_INVOCATION, removeAll = true, keyGenerator = @KeyGenerator(
            name = "HashCodeCacheKeyGenerator",
            properties = @Property(name = "includeMethod", value = "false")))
    @Async
    public void rebuildCache() throws ExceptionWrapper {
        try {

            employeeBL.getAllEmployees();
            groupsBL.getAllGroups();
            employeeBL.getAllEmployeesKeyedByGroupId();

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
}
