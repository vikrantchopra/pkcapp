package com.perceptive.epm.perkolcentral.junit4TestCases;

import com.perceptive.epm.perkolcentral.bl.EmployeeSyncBL;
import com.perceptive.epm.perkolcentral.common.jobs.NightlyEmployeeSyncJob;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: saibal
 * Date: 2/21/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:resources/PerceptiveKolkataCentralSpringConfig.xml"})
public class NightSyncJobTest implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private EmployeeSyncBL employeeSyncBL;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Before
    public void init(){
        //Setting the datasource for unit test case
        LocalSessionFactoryBean localSessionFactoryBean=(LocalSessionFactoryBean)applicationContext.getBean("&HibernateSessionFactory");
        BasicDataSource unitTestBasicDataSource=(BasicDataSource)applicationContext.getBean("UnitTestDataSource");
        localSessionFactoryBean.setDataSource(unitTestBasicDataSource);

        employeeSyncBL=(EmployeeSyncBL)applicationContext.getBean("springManagedEmployeeSyncBL");
    }

    @Test
    public void testJob() throws Exception {
     employeeSyncBL.executeEmployeeSync();

    }
}
