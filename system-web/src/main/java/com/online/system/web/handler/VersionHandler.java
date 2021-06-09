package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Version;
import com.online.system.web.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 版本管理
 */

@RestController
@RequestMapping("/version")
public class VersionHandler {

    private VersionService versionService;

    @Autowired
    public void setVersionService (VersionService versionService) {
        this.versionService = versionService;
    }
    /**
     * 查询版本信息
     * @param request 参数
     * @return 返回值
     */
    @RequestMapping("/findVersion")
    public Result<List<Version>> findVersion(HttpServletRequest request){
        Result<List<Version>> result;
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<Version> page = new Page<>(pageNo,pageSize);
        String versionType = request.getParameter("versionType");
        result = versionService.queryVersion(page,versionType);
        return result;
    }

    /**
     * 新增或更新版本
     * @param version 版本
     * @return 返回结果
     */
    @PostMapping("/addOrUpdateVersion")
    public Result<String> addOrUpdateVersion(@RequestBody Version version){
        Result<String> result = versionService.addOrUpdateVersion(version);
        return result;
    }

    /**
     * 根据主键查询版本信息
     * @param request 参数
     * @return 返回值
     */
    @PostMapping("/queryVersionDetatil")
    public Result<Version> queryVersionDetatil(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<Version> result = versionService.findVersionById(id);
        return result;
    }

    /**
     * 根据主键删除版本信息
     * @param request 参数
     * @return 返回值
     */
    @PostMapping("/delVersion")
    public Result<String> delVersion(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<String> result = versionService.delVersion(id);
        return result;
    }
}
