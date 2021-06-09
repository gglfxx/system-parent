package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.annotation.OperLog;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysUser;
import com.online.system.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserHandler {

    private UserService userService;

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findUser")
    @OperLog(module = "用户管理",type="查询",desc="用户查询")
    public Result<List<SysUser>> queryUser(HttpServletRequest request){
        String username = request.getParameter("username");
        String sex = request.getParameter("sex");
        String name = request.getParameter("name");
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<SysUser> page = new Page<>(pageNo,pageSize);
        Result<List<SysUser>> result = userService.findUser(page,username,sex,name);
        return result;
    }

    @PostMapping("/enableUser")
    @OperLog(module = "用户管理",type="更新",desc="禁用启用用户")
    public Result<String> enableUser(HttpServletRequest request){
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        Result<String> result = userService.enableUser(id,status);
        return result;
    }

    /**
     * 新增用户
     * @return
     */
    @PostMapping("/addOrUpdateUser")
    @OperLog(module = "用户管理",type="新增",desc="新增用户")
    public Result<String> addUser(@RequestBody SysUser user){
        Result<String> result = userService.addOrUpdateUser(user);
        return result;
    }
    
    /**
     * 删除用户
     * @param request
     * @return
     */
    @PostMapping("/delUser")
    @OperLog(module = "用户管理",type="删除",desc="删除用户")
    public Result<String> delUser(HttpServletRequest request){
    	String id = request.getParameter("id");
    	Result<String> result = userService.delUser(id);
        return result;
    }

    @PostMapping("/queryUserDetail")
    @OperLog(module = "用户管理",type="查询",desc="查询用户详情")
    public Result<SysUser> queryUserDetail(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<SysUser> result = userService.findUserById(id);
        return result;
    }

    @PostMapping("/changePwd")
    @OperLog(module = "用户管理",type="修改",desc="修改密码")
    public Result<String> changePwd(@RequestBody SysUser user){
        Result<String> result = userService.changePwd(user);
        return result;
    }
}
