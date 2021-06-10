package com.online.system.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * spring security 配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义用户详细
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义登录身份认证组件
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
    }
    
    
    /**
     * 不拦截的请求
     * (可以是Handler URL,也可以是资源URL)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //放行静态资源
        web.ignoring()
                .antMatchers(
                        "/config/changeImage",
                        "/**/*.css",
                        "/**/*.js",
                        "/images/**",
                        "/layui/**",
                        "/favicon.ico"
                        );
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //CSRF禁用，因为不使用session
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                //处理登陆
                .formLogin()
                .loginPage("/login.html") //登录页面 不设限访问
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                //处理登出
                .logout().logoutUrl("/logout")
                //自定义处理登出成功后的方法
                .logoutSuccessHandler(new LogoutHandler())
                .and()
                .exceptionHandling()
                .and()
                //记住我
                .rememberMe().rememberMeParameter("rememberMe")
                //去除set 'X-Frame-Options' to 'deny'.
                .and().headers().frameOptions().disable();
        //禁用缓存
        http.headers().cacheControl();
        // 退出登录处理器
        //http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        // 开启登录认证流程过滤器 就无法使用rememberMe 便于前后端分离
        http.addFilterBefore(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        // 访问控制时登录状态检查过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}