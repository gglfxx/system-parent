package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.LogDao;
import com.online.system.web.entity.OperationLog;
import com.online.system.web.entity.Result;
import com.online.system.web.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志管理
 */
@Service
public class LogServiceImpl implements LogService{

    @Resource
    private LogDao logDao;

    @Override
    public void add(OperationLog operLog) {
        logDao.insert(operLog);
    }

    @Override
    public Result<List<OperationLog>> findLog(Page<OperationLog> page, String username) {
        int code = 0;
        String msg = "查询成功";
        Result<List<OperationLog>>  result = new Result<>();
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        IPage<OperationLog> logs = logDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setCount(logs.getTotal());
        result.setMsg(msg);
        result.setData(logs.getRecords());
        return result;
    }
}
