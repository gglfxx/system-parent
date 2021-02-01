package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.VersionDao;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Version;
import com.online.system.web.service.VersionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 *版本实现类
 */
@Service
@Slf4j
public class VersionServiceImpl implements VersionService {

    @Resource
    private VersionDao versionDao;

    /**
     *
     * @param page 分页
     * @param versionType 版本类型
     * @return 返回结果集
     */
    @Override
    public Result<List<Version>> queryVersion(Page<Version> page, String versionType) {
        int code = 0;
        String msg = "查询成功";
        Result<List<Version>>  result = new Result<>();
        IPage<Version> versionPage = versionDao.queryVersion(page,versionType);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(versionPage.getTotal());
        result.setData(versionPage.getRecords());
        return result;
    }

    @Override
    public Result<String> addOrUpdateVersion(Version version) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        if(version!=null){
            try {
                if(StringUtils.isNotBlank(version.getId())){
                    count = versionDao.updateById(version);
                    msg.append("修改");
                }else{
                    count=  versionDao.insert(version);
                    msg.append("新增");
                }
                msg.append("版本信息");
            } catch (Exception e) {
                log.warn(msg.toString()+"出错！"+e.getMessage());
            }
        }
        if(count==1){
            code = 0;
            msg.append("成功");
            success = true;
        }else{
            msg.append("失败！");
        }
        result.setCode(code);
        result.setMsg(msg.toString());
        result.setSuccess(success);
        return result;
    }

    /**
     * 根据主键查询版本信息
     * @param id
     * @return
     */
    @Override
    public Result<Version> findVersionById(String id) {
        Result<Version> result = new Result<>();
        int code = 1;
        Version version = null;
        boolean success = false;
        String msg = "版本信息查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("无法查询版本信息！");
            return result;
        }
        try {
            version = versionDao.selectById(id);
        } catch (Exception e) {
            log.warn("版本信息查询出错！"+e.getMessage());
        }
        if(version!=null){
            code = 0;
            msg = "版本信息查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(version);
        return result;
    }

    /**
     * 根据主键删除版本信息
     * @param id
     * @return
     */
    @Override
    public Result<String> delVersion(String id) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        boolean success = false;
        String msg = "删除版本信息失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(success);
            result.setMsg("无法删除版本信息！");
            return result;
        }
        try {
            count = versionDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除版本信息出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除版本信息成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
}
