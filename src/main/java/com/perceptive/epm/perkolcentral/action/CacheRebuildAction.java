package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.CacheBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;


import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 8/10/12
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheRebuildAction extends ActionSupport {
    private CacheBL cacheBL;

    public void setCacheBL(CacheBL cacheBL) {
        this.cacheBL = cacheBL;
    }

    public String executeCacheRebuild() throws ExceptionWrapper {
        try {
            LoggingHelpUtil.printDebug("-----------------------------------------------CreateCacheJob started-----------------------------------------------");
            cacheBL.rebuildCache();
            LoggingHelpUtil.printDebug("-----------------------------------------------CreateCacheJob finished-----------------------------------------------");

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
