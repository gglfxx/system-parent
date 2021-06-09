package com.online.system.web.job;

import com.online.system.web.entity.SysTaskSchedule;
import com.online.system.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.lang.reflect.Method;

/**
 * 定时任务执行
 */
@Slf4j
public class QuartzFactory implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取调度数据
        SysTaskSchedule scheduleJob = (SysTaskSchedule) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");

        //获取对应的Bean
        Object object = SpringUtil.getBean(scheduleJob.getTaskClass());
        try {
            //利用反射执行对应方法
            Method method = object.getClass().getMethod(scheduleJob.getTaskMethod());
            method.invoke(object);
        } catch (Exception e) {
            log.warn("定时任务出错："+e.getMessage());
        }
    }
}
