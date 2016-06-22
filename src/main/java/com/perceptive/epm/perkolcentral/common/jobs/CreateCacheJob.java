package com.perceptive.epm.perkolcentral.common.jobs;

import com.perceptive.epm.perkolcentral.bl.CacheBL;
import com.perceptive.epm.perkolcentral.bl.EmployeeBL;
import com.perceptive.epm.perkolcentral.bl.GroupsBL;
import com.perceptive.epm.perkolcentral.common.utils.LoggingHelpUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 8/10/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateCacheJob extends QuartzJobBean {
    private CacheBL cacheBL;


    public void setCacheBL(CacheBL cacheBL) {
        this.cacheBL = cacheBL;
    }


    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        try {
            LoggingHelpUtil.printDebug("-----------------------------------------------CreateCacheJob started-----------------------------------------------");
            cacheBL.rebuildCache();
            LoggingHelpUtil.printDebug("-----------------------------------------------CreateCacheJob finished-----------------------------------------------");


        } catch (Exception e) {
            LoggingHelpUtil.printError("--------------------------------------------------------Error occurred while executing the job CreateCacheJob--------------------------------------------------------");
            LoggingHelpUtil.printError(e);
            throw new JobExecutionException(e);
        }

    }
}
