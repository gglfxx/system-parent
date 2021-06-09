package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.RoleDao;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Role;
import com.online.system.web.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 角色实现类
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public Result<List<Role>> queryRole(Page<Role> page, String roleCode, String roleName) {
        int code = 0;
        String msg = "查询成功";
        Result<List<Role>>  result = new Result<>();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("role_code", roleCode);
        queryWrapper.like("role_name",roleName);
        IPage<Role> dictionaryIPage = roleDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(dictionaryIPage.getTotal());
        result.setData(dictionaryIPage.getRecords());
        return result;
    }

    @Override
    public Result<Role> findRoleById(String id) {
        Result<Role> result = new Result<>();
        int code = 1;
        Role role = null;
        boolean success = false;
        String msg = "角色查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("缺少主键无法查询角色！");
            return result;
        }
        try {
            role = roleDao.selectById(id);
        } catch (Exception e) {
            log.warn("角色查询出错！"+e.getMessage());
        }
        if(role!=null){
            code = 0;
            msg = "角色查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(role);
        return result;
    }

    @Override
    public Result<String> addOrUpdateRole(Role role) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        String type = null;
        if(role!=null){
            try {
                if(StringUtils.isNotBlank(role.getId())){
                    count = roleDao.updateById(role);
                    type = "修改";
                }else{
                    count=  roleDao.insert(role);
                    type = "新增";
                }
                msg.append(type).append("角色");
            } catch (Exception e) {
                log.warn(msg.toString()+"出错！"+e.getMessage());
            }
        }
        if(count==1){
            code = 0;
            msg.append("成功");
        }else{
            msg.append("失败！");
        }
        result.setCode(code);
        result.setMsg(msg.toString());
        return result;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public Result<String> delRole(String id) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        String msg = "删除角色失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setMsg("缺少主键无法删除角色！");
            return result;
        }
        try {
            count = roleDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除角色出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除角色成功";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 禁用或启用角色
     * @param id
     * @param status
     * @return
     */
    @Override
    public Result<String> enableRole(String id, String status) {
        Result<String> result = new Result<>();
        int code = 1;
        String msg = "更新角色状态失败！";
        Role role = new Role();
        role.setId(id);
        role.setUseFlag(Integer.valueOf(status));
        int count = 0;
        try {
            count = roleDao.updateById(role);
        } catch (Exception e) {
            log.warn("更新角色状态出错！"+e.getMessage());
        }
        if(count==1){
            code =0 ;
            msg = "更新角色状态成功";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 根据主键查询角色
     * @param id
     * @return
     */
    @Override
    public Role selectById(String id) {
        Role role = null;
        if(StringUtils.isNotBlank(id)){
            role = roleDao.selectById(id);
        }
        return role;
    }
}
