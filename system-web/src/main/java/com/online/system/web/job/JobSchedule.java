package com.online.system.web.job;

import com.online.system.web.service.IQuartzService;
import com.online.system.web.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 自启动定时任务
 */
@Component
public class JobSchedule implements CommandLineRunner {

    @Autowired
    private JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("任务调度开始==============任务调度开始");
        jobService.timingTask();
        System.out.println("任务调度结束==============任务调度结束");
    }
}
