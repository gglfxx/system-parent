package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.annotation.OperLog;
import com.online.system.web.entity.OperationLog;
import com.online.system.web.entity.Result;
import com.online.system.web.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 日志管理
 */
@RestController
@RequestMapping("/log")
public class LogHandler {

    //日志处理类
    @Autowired
    private LogService logService;

    @GetMapping("/queryLog")
    @OperLog(module = "日志管理",type="查询",desc="日志查询")
    public Result<List<OperationLog>> queryUser(HttpServletRequest request){
        String username = request.getParameter("username");
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<OperationLog> page = new Page<>(pageNo,pageSize);
        Result<List<OperationLog>> result = logService.findLog(page,username);
        return result;
    }
}
