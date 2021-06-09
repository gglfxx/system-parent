package com.online.system.web.service;

import com.online.system.web.entity.Menu;
import com.online.system.web.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单接口
 */
public interface MenuService {
    Result<List<Menu>> queryMenu(String menuType,String username);

    Result<List<Menu>> queryAllMenu();

    /**
     * 删除菜单
     * @return
     */
    Result<String> delMenu(HttpServletRequest request);
}
