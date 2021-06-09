package com.online.system.web.service.impl;

import com.online.system.web.entity.JobOperateEnum;
import com.online.system.web.entity.SysTaskSchedule;
import com.online.system.web.job.QuartzFactory;
import com.online.system.web.service.IQuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务实现类
 */
@Service
public class QuartzServiceImpl implements IQuartzService {

    //调度器
    @Autowired
    private Scheduler scheduler;

    /**
     * 新增定时任务
     * @param job 任务
     */
    @Override
    public void addJob(SysTaskSchedule job) {
        try {
            //创建触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskNo())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getTaskExpress()))
                    .startNow()
                    .build();

            //创建任务
            JobDetail jobDetail = JobBuilder.newJob(QuartzFactory.class)
                    .withIdentity(job.getTaskNo())
                    .build();

            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().put("scheduleJob", job);

            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 操作定时任务
     * @param jobOperateEnum 操作枚举
     * @param job 任务
     * @throws SchedulerException
     */
    @Override
    public void operateJob(JobOperateEnum jobOperateEnum, SysTaskSchedule job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getTaskNo());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            //第一次启动没有实例 导致启动异常
            if(jobOperateEnum.getValue().equals(0)){
                addJob(job);
            }
        }
        switch (jobOperateEnum) {
            case START:
                scheduler.resumeJob(jobKey);
                break;
            case PAUSE:
                scheduler.pauseJob(jobKey);
                break;
            case DELETE:
                scheduler.deleteJob(jobKey);
                break;
        }
    }

    /**
     * 启动所有任务
     * @throws SchedulerException
     */
    @Override
    public void startAllJob() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 暂停所有任务
     * @throws SchedulerException
     */
    @Override
    public void pauseAllJob() throws SchedulerException {
        scheduler.standby();
    }
}
