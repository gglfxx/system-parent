package com.online.system.web.security;

import com.online.system.web.entity.JwtUserDetails;
import com.online.system.web.entity.Role;
import com.online.system.web.entity.SysUser;
import com.online.system.web.entity.SysUserRole;
import com.online.system.web.service.RoleService;
import com.online.system.web.service.UserRoleService;
import com.online.system.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义用户和权限
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        SysUser user = userService.findByUsername(username);
        // 判断用户是否存在
        if(user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 添加权限
        List<SysUserRole> userRoles = userRoleService.listByUserId(user.getId());
        for (SysUserRole userRole : userRoles) {
            Role role = roleService.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        // 返回UserDetails实现类
        return new JwtUserDetails(user.getUsername(), user.getPassword(), authorities);
    }
}
