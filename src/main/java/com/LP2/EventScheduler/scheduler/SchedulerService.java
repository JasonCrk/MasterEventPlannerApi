package com.LP2.EventScheduler.scheduler;

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SchedulerService {

    public JobDetail buildJobDetail(
            QuartzJobBean job,
            JobDataMap jobData,
            String description,
            String identifyGroup
    ) {
        return JobBuilder.newJob(job.getClass())
                .withDescription(description)
                .withIdentity(UUID.randomUUID().toString(), identifyGroup)
                .usingJobData(jobData)
                .storeDurably()
                .build();
    }

    public Trigger buildTriggerWithCronSchedule(
            JobDetail jobDetail,
            String identifyGroup,
            String description,
            CronScheduleBuilder schedule
    ) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), identifyGroup)
                .withDescription(description)
                .withSchedule(schedule)
                .build();
    }
}
