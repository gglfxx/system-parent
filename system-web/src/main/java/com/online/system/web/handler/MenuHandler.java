package com.online.system.web.handler;

import com.online.system.annotation.OperLog;
import com.online.system.web.entity.Menu;
import com.online.system.web.entity.Result;
import com.online.system.web.service.MenuService;
import com.online.system.common.utils.SecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单管理
 */
@RestController
@RequestMapping("menu")
public class MenuHandler {

    @Resource
    private MenuService menuService;

    /**
     * 查询左侧菜单
     * @param request
     * @return
     */
    @PostMapping("/queryMenu")
    @OperLog(module = "左侧菜单",type = "查询",desc="查询左侧菜单")
    public Result<List<Menu>> findMenu(HttpServletRequest request){
        String username = SecurityUtil.getUsername();
        String menuType = request.getParameter("menuType");
        Result<List<Menu>> result = menuService.queryMenu(menuType,username);
        return result;
    }

    /**
     * 查询所有菜单资源
     * @param request
     * @return
     */
    @PostMapping("/queryAllMenu")
    @OperLog(module = "菜单管理",type = "查询",desc="展示所有菜单")
    public Result<List<Menu>> findAllMenu(HttpServletRequest request){
        Result<List<Menu>> result = menuService.queryAllMenu();
        return result;
    }

    @PostMapping("/delMenu")
    @OperLog(module = "菜单管理",type = "删除",desc="删除菜单")
    public Result<String> delMenu(HttpServletRequest request){
        Result<String> result = menuService.delMenu(request);
        return result;
    }
}
