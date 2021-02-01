package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.online.system.web.dao.UserRoleDao;
import com.online.system.web.entity.SysUserRole;
import com.online.system.web.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色实现类
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    /**
     * 根据用户id查询所有角色
     * @param id
     * @return
     */
    @Override
    public List<SysUserRole> listByUserId(String id) {
        List<SysUserRole> userRoles = null;
        if(StringUtils.isNotBlank(id)){
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",id);
            userRoles = userRoleDao.selectList(queryWrapper);
        }
        return userRoles;
    }
}
