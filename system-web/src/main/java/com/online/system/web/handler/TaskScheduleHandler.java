package com.online.system.web.handler;

import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysTaskSchedule;
import com.online.system.web.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 定时任务管理
 */
@RestController
@RequestMapping("/schedule")
public class TaskScheduleHandler {

    @Autowired
    private JobService jobService;

    /**
     * 查询定时任务列表
     * @param request
     * @return
     */
    @RequestMapping("/querySchedule")
    public Result<List<SysTaskSchedule>> queryScheduleList(HttpServletRequest request){
        Result<List<SysTaskSchedule>> result = jobService.queryScheduleList(request);
        return result;
    }

    /**
     * 新增或更新定时任务
     * @param task
     * @return
     */
    @RequestMapping("/addOrUpdateTask")
    public Result<String> addOrUpdateTask(@RequestBody SysTaskSchedule task){
        Result<String> result = jobService.addOrUpdateTask(task);
        return result;
    }

    /**
     * 启用或停用定时任务
     * @param request
     * @return
     */
    @RequestMapping("/enableTask")
    public Result<String> enableTask(HttpServletRequest request){
        Result<String> result = jobService.enableTask(request);
        return result;
    }

    /**
     * 根据主键查询定时任务
     */
    @PostMapping("/queryTaskDetail")
    public Result<SysTaskSchedule> queryTaskDetail(HttpServletRequest request){
        Result<SysTaskSchedule> result = jobService.queryTaskDetail(request);
        return result;
    }

    /**
     * 删除定时任务
     * @param request
     * @return
     */
    @PostMapping("/delTaskSchedule")
    public Result<String> delTaskSchedule(HttpServletRequest request){
        Result<String> result = jobService.delTaskSchedule(request);
        return result;
    }
}
