package com.online.system.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysProperties;
import com.online.system.web.entity.SysTaskSchedule;
import com.online.system.web.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 配置管理
 */
@RestController
@RequestMapping("/config")
public class ConfigHandler {

    private ConfigService configService;

    @Autowired
    private void setConfigService(ConfigService configService){
        this.configService = configService;
    }

    /**
     * 设置季节
     * @param request
     * @return
     */
    @GetMapping("/changeImage")
    public Result<String> changeImage(HttpServletRequest request) throws JsonProcessingException {
        Result<String> result = configService.changeImage();
        return result;
    }

    /**
     * 查询配置列表
     * @param request
     */
    @RequestMapping("/querySysProp")
    public Result<List<SysProperties>> querySysPropList(HttpServletRequest request){
        return configService.querySysPropList(request);
    }

    /**
     * 新增或更新配置
     * @param prop
     * @return
     */
    @RequestMapping("/addOrUpdateProp")
    public Result<String> addOrUpdateProp(@RequestBody SysProperties prop){
        Result<String> result = configService.addOrUpdateProp(prop);
        return result;
    }


    /**
     * 根据主键查询配置
     */
    @PostMapping("/queryPropDetail")
    public Result<SysProperties> queryPropDetail(HttpServletRequest request){
        return configService.queryPropDetail(request);
    }

    /**
     * 删除配置
     * @param request
     * @return
     */
    @PostMapping("/delProp")
    public Result<String> delProp(HttpServletRequest request){
        return configService.delProp(request);
    }
}
