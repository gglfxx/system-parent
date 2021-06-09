package com.online.system.web.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转类
 */
@Controller
public class IndexHandler {

    //登录页
    @RequestMapping("/")
    public String login(){
        return "login";
    }

    //重定向到主页
    @RequestMapping("/index")
    public String index(){
        return "redirect:/index.html";
    }

    //菜单管理
    @RequestMapping("sysMenu/menuManage")
    public String menuManage(){
        return "page/menu/menu.html";
    }

    //用户管理
    @RequestMapping("sysMenu/userManage")
    public String userManage(){
        return "page/user/user.html";
    }

    //字典管理
    @RequestMapping("sysMenu/dictManage")
    public String dictManage(){
        return "page/dictionary/dictionary.html";
    }

    //图片管理
    @RequestMapping("sysMenu/imageManage")
    public String imageManage(){
        return "page/images/images.html";
    }

    /**
     * 日志管理
     * @return
     */
    @RequestMapping("sysMenu/logManage")
    public String logManage(){
        return "page/log/log.html";
    }

    /**
     * 角色管理
     * @return
     */
    @RequestMapping("sysMenu/roleManage")
    public String roleManage(){
        return "page/role/role.html";
    }

    /**
     * 版本管理
     * @return
     */
    @RequestMapping("sysMenu/versionManage")
    public String versionManage(){
        return "page/version/version.html";
    }

    /**
     * 通知公告
     * @return
     */
    @RequestMapping("sysMenu/noticeManage")
    public String noticeManage(){
        return "page/notice/notice.html";
    }

    /**
     * 视频管理
     * @return
     */
    @RequestMapping("sysMenu/videoManage")
    public String videoManage(){
        return "page/video/video.html";
    }

    /**
     * 定时任务管理
     * @return
     */
    @RequestMapping("sysMenu/quartzManage")
    public String quartzManage(){
        return "page/taskSchedule/schedule.html";
    }

    /**
     * 配置管理
     * @return
     */
    @RequestMapping("sysMenu/propManage")
    public String propManage(){
        return "page/sysProperties/properties.html";
    }
}
