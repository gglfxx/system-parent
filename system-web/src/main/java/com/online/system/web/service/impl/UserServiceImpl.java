package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.annotation.OperLog;
import com.online.system.web.dao.UserDao;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.SysUser;
import com.online.system.web.service.UserService;
import com.online.system.common.utils.EncryptUtil;
import com.online.system.common.utils.IDCardVerify;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao userDao;

    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<String> findPermissionsByUserId(String id) {
        return null;
    }

    @Override
    public List<String> findRoleNameByUserId(String id) {
        return null;
    }

    /**
     * 查询用户
     * @param username 用户
     * @param sex 性别
     * @param name 姓名
     * @return 返回
     */
    @Override
    @OperLog
    public Result<List<SysUser>> findUser(Page<SysUser> page, String username, String sex, String name) {
        int code = 0;
        String msg = "查询成功";
        Result<List<SysUser>>  result = new Result<>();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        queryWrapper.eq(StringUtils.isNotBlank(sex),"sex", sex);
        queryWrapper.like("name",name);
        IPage<SysUser> users = userDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(users.getTotal());
        result.setData(users.getRecords());
        return result;
    }

    @Override
    @OperLog
    public Result<String> enableUser(String id, String status) {
    	Result<String> result = new Result<>();
        int code = 1;
        String msg = "更新用户状态失败！";
        boolean success = false;
        SysUser user = new SysUser();
        user.setId(id);
        user.setUseFlag(Integer.valueOf(status));
        int count = 0;
        try {
            count = userDao.updateById(user);
        } catch (Exception e) {
            log.warn("更新用户状态出错！"+e.getMessage());
        }
        if(count==1){
            code =0 ;
            success = true;
            msg = "更新用户状态成功";

        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }

    /**
     * 新增或更新用户
     * @param user 用户
     * @return 返回状态
     */
    @Override
    @OperLog
    public Result<String> addOrUpdateUser(SysUser user) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        if(user!=null){
            try {
                //验证身份证规则
                //判空
                if(StringUtils.isNotBlank(user.getIdNum())){
                    if(IDCardVerify.validateCard(user.getIdNum())){
                        if(StringUtils.isNotBlank(user.getId())){
                            count = userDao.updateById(user);
                            msg.append("修改");
                        }else{
                            user.setPassword(EncryptUtil.shiroMd5Encrypt("123456",2));
                            count=  userDao.insert(user);
                            msg.append("新增");
                        }
                        msg.append("用户");
                        if(count==1){
                            code = 0;
                            msg.append("成功");
                            success = true;
                        }else{
                            msg.append("失败！");
                        }
                    }else{
                        msg.append("身份证验证不通过！");
                    }
                }else{
                    msg.append("身份证不能为空！");
                }
            } catch (Exception e) {
                log.warn(msg.toString()+"出错！"+e.getMessage());
            }
        }
        result.setCode(code);
        result.setMsg(msg.toString());
        result.setSuccess(success);
        return result;
    }
    
    /**
     * 删除用户
     */
	@Override
	@OperLog
	public Result<String> delUser(String id) {
		Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        boolean success = false;
        String msg = "删除用户失败！";
		if(StringUtils.isBlank(id)){
			result.setCode(code);
			result.setSuccess(success);
			result.setMsg("缺少主键无法删除用户！");
			return result;
		}
        try {
    		count = userDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除用户出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除用户成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
	}

	@OperLog
    @Override
    public Result<SysUser> findUserById(String id) {
        Result<SysUser> result = new Result<>();
        int code = 1;
        SysUser user = null;
        boolean success = false;
        String msg = "用户查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("缺少主键无法查询用户！");
            return result;
        }
        try {
            user = userDao.selectById(id);
        } catch (Exception e) {
            log.warn("用户查询出错！"+e.getMessage());
        }
        if(user!=null){
            code = 0;
            msg = "用户查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(user);
        return result;
    }

    /**
     * 根据用户名修改密码
     * @param user
     * @return
     */
    @Override
    public Result<String> changePwd(SysUser user) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        String msg = "密码修改失败！";
        boolean success = false;
        if(user!=null){
            String password = user.getPassword();
            String secretPwd = null;
            //密码不为空并且符合规则才能修改密码
            if(StringUtils.isNotBlank(password)){
                secretPwd = EncryptUtil.shiroMd5Encrypt(password,2);
                UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("username",user.getUsername()).set("password",secretPwd);
                count = userDao.update(null,updateWrapper);
            }
        }
        if(count==1){
            success = true;
            code = 0;
            msg = "密码修改成功";
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
}
