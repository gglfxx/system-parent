package com.online.system.web.service;

import com.online.system.web.entity.JobOperateEnum;
import com.online.system.web.entity.SysTaskSchedule;
import org.quartz.SchedulerException;

/**
 * 定时任务实现
 */
public interface IQuartzService {

    /**
     * 新增定时任务
     * @param job 任务
     */
    void addJob(SysTaskSchedule job);

    /**
     * 操作定时任务
     * @param jobOperateEnum 操作枚举
     * @param job 任务
     */
    void operateJob(JobOperateEnum jobOperateEnum, SysTaskSchedule job) throws SchedulerException;

    /**
     * 启动所有任务
     */
    void startAllJob() throws SchedulerException;

    /**
     * 暂停所有任务
     */
    void pauseAllJob() throws SchedulerException;
}
