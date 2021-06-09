package com.online.system.web.service;

import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysTaskSchedule;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 定时任务管理实现
 */
public interface JobService {

    void timingTask();

    /**
     * 定时任务列表
     * @param request
     * @return
     */
    Result<List<SysTaskSchedule>> queryScheduleList(HttpServletRequest request);

    /**
     * 新增或修改定时任务
     * @param task
     * @return
     */
    Result<String> addOrUpdateTask(SysTaskSchedule task);

    /**
     * 查询可执行的定时任务
     * @return
     */
    List<SysTaskSchedule> queryTaskList();

    /**
     * 启用或禁用定时任务
     * @param request
     * @return
     */
    Result<String> enableTask(HttpServletRequest request);

    /**
     * 根据主键查询定时任务
     * @param request
     * @return
     */
    Result<SysTaskSchedule> queryTaskDetail(HttpServletRequest request);

    /**
     * 删除定时任务
     * @param request
     * @return
     */
    Result<String> delTaskSchedule(HttpServletRequest request);
}
