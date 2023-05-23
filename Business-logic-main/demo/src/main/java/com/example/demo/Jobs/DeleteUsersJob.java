package com.example.demo.Jobs;

import com.example.demo.Service.QuartzService;
import com.example.demo.Service.UserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class DeleteUsersJob extends QuartzJobBean {

    @Autowired
    private QuartzService quartzService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Running cron task on deleting accounts");
        quartzService.deleteUnusedAccounts();
    }

}
