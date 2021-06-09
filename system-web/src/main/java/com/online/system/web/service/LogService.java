package com.online.system.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.OperationLog;
import com.online.system.web.entity.Result;

import java.util.List;

/**
 * 日志实现
 */
public interface LogService {

    /**
     * 新增日志
     * @param operLog
     */
    void add(OperationLog operLog);

    /**
     * 查询日志
     * @param page
     * @param username
     * @return
     */
    Result<List<OperationLog>> findLog(Page<OperationLog> page, String username);
}
