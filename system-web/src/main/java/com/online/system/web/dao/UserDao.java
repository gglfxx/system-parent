package com.online.system.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.online.system.web.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户接口
 */
public interface UserDao extends BaseMapper<SysUser> {

    SysUser findByUsername(@Param("username") String username);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    SysUser selectByUsername(@Param("username") String username, @Param("password") String password);

    /**
     * 查询当前用户角色权限
     * @param id
     * @return
     */
    List<String> findPermissionsByUserId(@Param("id") String id);

    /**
     * 更新用户状态
     * @param id
     * @param status
     * @return
     */
    int updateUserStatus(@Param("id") String id, @Param("status") String status);
}
