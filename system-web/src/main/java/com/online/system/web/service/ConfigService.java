package com.online.system.web.service;

import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysProperties;
import com.online.system.web.entity.SysTaskSchedule;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConfigService {
    /**
     * 根据季节改变登录页背景图
     * @return
     */
    Result<String> changeImage();

    /**
     * 查询配置列表
     * @param request
     * @return
     */
    Result<List<SysProperties>> querySysPropList(HttpServletRequest request);

    /**
     * 新增或修改配置
     * @param prop
     * @return
     */
    Result<String> addOrUpdateProp(SysProperties prop);

    /**
     * 根据主键查询配置
     * @param request
     * @return
     */
    Result<SysProperties> queryPropDetail(HttpServletRequest request);

    /**
     * 删除配置
     * @param request
     * @return
     */
    Result<String> delProp(HttpServletRequest request);
}
