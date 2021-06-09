package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.TaskScheduleDao;
import com.online.system.web.entity.JobOperateEnum;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysTaskSchedule;
import com.online.system.web.service.IQuartzService;
import com.online.system.web.service.JobService;
import com.online.system.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务管理实现类
 */
@Slf4j
@Service
@Transactional
public class JobServiceImpl implements JobService {

    private static final AtomicInteger atomicNum = new AtomicInteger();

    @Resource
    private TaskScheduleDao taskDao;

    @Resource
    private IQuartzService iQuartzService;


    @Override
    public void timingTask() {
        List<SysTaskSchedule> scheduleJobs = queryTaskList();
        if (scheduleJobs != null) {
            scheduleJobs.forEach(iQuartzService::addJob);
        }
    }

    /**
     * 查询定时任务列表
     *
     * @param request
     * @return
     */
    @Override
    public Result<List<SysTaskSchedule>> queryScheduleList(HttpServletRequest request) {
        int code = 0;
        String msg = "查询成功";
        Result<List<SysTaskSchedule>> result = new Result<>();
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        String taskName = request.getParameter("taskName");
        String taskGroup = request.getParameter("taskGroup");
        String taskStatus = request.getParameter("taskStatus");
        QueryWrapper<SysTaskSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("task_name", taskName);
        queryWrapper.eq(StringUtils.isNotBlank(taskGroup), "task_group", taskGroup);
        queryWrapper.eq(StringUtils.isNotBlank(taskStatus), "task_status", taskStatus);
        Page<SysTaskSchedule> page = new Page<>(pageNo, pageSize);
        IPage<SysTaskSchedule> taskPage = taskDao.selectPage(page, queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(taskPage.getTotal());
        result.setData(taskPage.getRecords());
        return result;
    }

    @Override
    public Result<String> addOrUpdateTask(SysTaskSchedule task) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        String msg = "定时任务操作失败！";
        //新增或修改标记 false为新增
        boolean isAddOrEdit = false;
        try {
            if (task != null) {
                try {
                    if (StringUtils.isNotBlank(task.getId())) {
                        count = taskDao.updateById(task);
                    } else {
                        isAddOrEdit = true;
                        task.setTaskNo(generateTaskNo());
                        count = taskDao.insert(task);
                    }
                } catch (Exception e) {
                    log.warn(msg.toString() + "出错！" + e.getMessage());
                }
            }
            if (count == 1) {
                int taskStatus = task.getTaskStatus();
                if (!isAddOrEdit) {
                    //启用状态为0 则启动定时任务
                    if (JobOperateEnum.START.getValue().equals(taskStatus)) {
                        iQuartzService.operateJob(JobOperateEnum.START, task);
                    }
                } else {
                    //如果是编辑 要判断之前状态为什么 然后对定时任务进行操作
                    SysTaskSchedule oldTask = taskDao.selectById(task.getId());
                    if (oldTask != null && oldTask.getTaskStatus() != task.getTaskStatus()) {
                        if (JobOperateEnum.START.getValue().equals(taskStatus)) {
                            iQuartzService.operateJob(JobOperateEnum.START, task);
                        } else if (JobOperateEnum.PAUSE.getValue().equals(taskStatus)) {
                            iQuartzService.operateJob(JobOperateEnum.PAUSE, task);
                        }
                    }
                }
                msg = "定时任务操作成功";
                code = 0;
            }
        } catch (SchedulerException e) {
            log.warn("定时任务新增或修改失败:" + e.getMessage());
            code = 1;
            msg = "定时任务新增或修改失败！";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 查询所有可执行的定时任务
     *
     * @return
     */
    @Override
    public List<SysTaskSchedule> queryTaskList() {
        QueryWrapper<SysTaskSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_status", 0).eq("delete_flag", 0);
        return taskDao.selectList(queryWrapper);
    }

    /**
     * 启用或禁用定时任务
     *
     * @param request
     * @return
     */
    @Override
    public Result<String> enableTask(HttpServletRequest request) {
        Result<String> result = new Result<>();
        int code = 1;
        String msg = "操作失败！";
        String id = request.getParameter("id");
        String enable = request.getParameter("status");
        /**
         *  1、先判断id是否为空
         *  2、判断状态是否为空
         *  3、根据id查询这条记录是否存在 对比传参状态和库里的状态是否一致 一致则不可操作
         */
        try {
            if (StringUtils.isNotBlank(id)) {
                SysTaskSchedule oldTaskSch = taskDao.selectById(id);
                if (StringUtils.isNotBlank(enable)) {
                    if (oldTaskSch != null) {
                        //旧的定时任务状态
                        String oldStatus = String.valueOf(oldTaskSch.getTaskStatus());
                        if (StringUtils.isNotBlank(oldStatus) && !enable.equals(oldStatus)) {
                            int newStatus = Integer.valueOf(enable);
                            oldTaskSch.setTaskStatus(newStatus);
                            int success = taskDao.updateById(oldTaskSch);
                            if (success == 1) {
                                //0启动 1暂停
                                if (JobOperateEnum.START.getValue().equals(newStatus)) {
                                    iQuartzService.operateJob(JobOperateEnum.START, oldTaskSch);
                                } else if (JobOperateEnum.PAUSE.getValue().equals(newStatus)) {
                                    iQuartzService.operateJob(JobOperateEnum.PAUSE, oldTaskSch);
                                }
                                code = 0;
                                msg = "操作成功";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("定时任务操作失败:" + e.getMessage());
            code = 1;
            msg = "定时任务操作失败！";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 根据主键查询定时任务
     *
     * @param request
     * @return
     */
    @Override
    public Result<SysTaskSchedule> queryTaskDetail(HttpServletRequest request) {
        Result<SysTaskSchedule> result = new Result<>();
        int code = 1;
        String msg = "查询失败！";
        String id = request.getParameter("id");
        SysTaskSchedule taskSchedule = null;
        if (StringUtils.isNotBlank(id)) {
            taskSchedule = taskDao.selectById(id);
            if (taskSchedule != null) {
                code = 0;
                msg = "查询成功";
            }
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setData(taskSchedule);
        return result;
    }

    /**
     * 根据主键删除定时任务
     *
     * @param request
     * @return
     */
    @Override
    public Result<String> delTaskSchedule(HttpServletRequest request) {
        Result<String> result = new Result<>();
        int code = 1;
        String msg = "删除失败！";
        String id = request.getParameter("id");
        try {
            if (StringUtils.isNotBlank(id)) {
                SysTaskSchedule oldTaskSch = taskDao.selectById(id);
                int success = taskDao.deleteById(id);
                if (success == 1) {
                    iQuartzService.operateJob(JobOperateEnum.DELETE, oldTaskSch);
                    code = 0;
                    msg = "删除成功";
                }
            }
        } catch (Exception e) {
            log.warn("定时任务删除失败:" + e.getMessage());
            code = 1;
            msg = "定时任务操作失败！";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 获取6位数字
     */
    private String getNewAutoNum() {
        // 线程安全的原子操作，所以此方法无需同步
        int newNum = atomicNum.incrementAndGet();
        // 数字长度为6位，长度不够数字前面补0
        String newStrNum = String.format("%06d", newNum);
        return newStrNum;
    }

    /**
     * 生成任务号
     *
     * @return
     */
    private String generateTaskNo() {
        String prefix = "TK";
        StringBuffer sb = new StringBuffer();
        String time = DateUtil.dateFormatToString("YYMMdd", new Date());
        sb.append(prefix).append(time).append(getNewAutoNum());
        QueryWrapper<SysTaskSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_no", sb.toString());
        int count = taskDao.selectCount(queryWrapper);
        if (count > 0) {
            return generateTaskNo();
        }
        return sb.toString();
    }
}
