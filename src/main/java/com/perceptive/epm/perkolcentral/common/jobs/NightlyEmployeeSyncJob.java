package com.perceptive.epm.perkolcentral.common.jobs;

import com.perceptive.epm.perkolcentral.bl.EmployeeSyncBL;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 4/9/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class NightlyEmployeeSyncJob extends QuartzJobBean {

    private EmployeeSyncBL employeeSyncBL;

    public EmployeeSyncBL getEmployeeSyncBL() {
        return employeeSyncBL;
    }

    public void setEmployeeSyncBL(EmployeeSyncBL employeeSyncBL) {
        this.employeeSyncBL = employeeSyncBL;
    }

    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        try {
            employeeSyncBL.executeEmployeeSync();

        } catch (Exception ex) {
            throw new JobExecutionException(ex);
        }
    }


}
