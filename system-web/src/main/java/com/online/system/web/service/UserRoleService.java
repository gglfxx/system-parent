package com.online.system.web.service;

import com.online.system.web.entity.SysUserRole;

import java.util.List;

/**
 * 用户角色实现类
 */
public interface UserRoleService {
    List<SysUserRole> listByUserId(String id);
}
