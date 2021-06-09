package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.annotation.OperLog;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Role;
import com.online.system.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表
     * @param request
     * @return
     */
    @GetMapping("/findRole")
    public Result<List<Role>> queryRole(HttpServletRequest request){
        String roleCode = request.getParameter("roleCode");
        String roleName = request.getParameter("roleName");
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<Role> page = new Page<>(pageNo,pageSize);
        Result<List<Role>> result = roleService.queryRole(page,roleCode,roleName);
        return result;
    }

    @PostMapping("/queryRoleDetatil")
    @OperLog(module = "角色管理",type="查询",desc="查询角色详情")
    public Result<Role> queryRoleDetatil(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<Role> result = roleService.findRoleById(id);
        return result;
    }

    /**
     * 新增角色
     * @return
     */
    @PostMapping("/addOrUpdateRole")
    @OperLog(module = "角色管理",type="新增",desc="新增角色")
    public Result<String> addUser(@RequestBody Role role){
        Result<String> result = roleService.addOrUpdateRole(role);
        return result;
    }

    /**
     * 删除角色
     * @param request
     * @return
     */
    @PostMapping("/delRole")
    @OperLog(module = "角色管理",type="删除",desc="删除角色")
    public Result<String> delRole(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<String> result = roleService.delRole(id);
        return result;
    }

    /**
     * 禁用启用角色
     * @param request
     * @return
     */
    @PostMapping("/enableRole")
    @OperLog(module = "角色管理",type="更新",desc="禁用启用角色")
    public Result<String> enableRole(HttpServletRequest request){
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        Result<String> result = roleService.enableRole(id,status);
        return result;
    }
}
