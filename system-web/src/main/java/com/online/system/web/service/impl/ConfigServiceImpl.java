package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.SysPropDao;
import com.online.system.web.entity.*;
import com.online.system.web.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 配置管理
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private SysPropDao sysPropDao;

    /**
     * 切换图片
     * @return
     */
    @Override
    public Result<String> changeImage() {
        int code = 0;
        Result<String> result = new Result<>();
        int month = new Date().getMonth();
        String season = null;
        //季节判断
        switch(month+1){
            case 12:
            case 1:
            case 2:
                season = SeasonEnum.WINTER.getSeason();
                break;
            case 3:
            case 4:
            case 5:
                season = SeasonEnum.SPRING.getSeason();
                break;
            case 6:
            case 7:
            case 8:
                season = SeasonEnum.SUMMER.getSeason();
                break;
            case 9:
            case 10:
            case 11:
                season = SeasonEnum.AUTUMN.getSeason();
                break;
        }
        result.setCode(code);
        result.setData(season);
        return result;
    }

    /**
     * 查询配置列表
     * @param request
     * @return
     */
    @Override
    public Result<List<SysProperties>> querySysPropList(HttpServletRequest request) {
        int code = 0;
        String msg = "查询成功";
        Result<List<SysProperties>> result = new Result<>();
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        String sysName = request.getParameter("sysName");
        QueryWrapper<SysProperties> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("sys_name", sysName);
        Page<SysProperties> page = new Page<>(pageNo, pageSize);
        IPage<SysProperties> propPage = sysPropDao.selectPage(page, queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(propPage.getTotal());
        result.setData(propPage.getRecords());
        return result;
    }

    @Override
    public Result<String> addOrUpdateProp(SysProperties prop) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        String msg = "操作失败！";
        if(prop!=null){
            try {
                if (StringUtils.isNotBlank(prop.getId())) {
                    count = sysPropDao.updateById(prop);
                } else {
                    count = sysPropDao.insert(prop);
                }
            } catch (Exception e) {
                log.warn("新增或修改配置出错：" + e.getMessage());
            }
        }
        if(count==1){
            code = 0;
            msg = "操作成功";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 根据主键查询配置
     * @param request
     * @return
     */
    @Override
    public Result<SysProperties> queryPropDetail(HttpServletRequest request) {
        Result<SysProperties> result = new Result<>();
        int code = 1;
        String msg = "查询失败！";
        String id = request.getParameter("id");
        SysProperties sysProp = null;
        if (StringUtils.isNotBlank(id)) {
            sysProp = sysPropDao.selectById(id);
            if (sysProp != null) {
                code = 0;
                msg = "查询成功";
            }
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setData(sysProp);
        return result;
    }

    /**
     * 删除配置
     * @param request
     * @return
     */
    @Override
    public Result<String> delProp(HttpServletRequest request) {
        Result<String> result = new Result<>();
        int code = 1;
        String msg = "删除失败！";
        String id = request.getParameter("id");
        try {
            if (StringUtils.isNotBlank(id)) {
                int success = sysPropDao.deleteById(id);
                if (success == 1) {
                    code = 0;
                    msg = "删除成功";
                }
            }
        } catch (Exception e) {
            log.warn("配置删除失败:" + e.getMessage());
            code = 1;
            msg = "配置删除失败！";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
