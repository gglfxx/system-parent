package com.online.system.web.service.impl;

import com.online.system.annotation.OperLog;
import com.online.system.web.dao.MenuDao;
import com.online.system.web.entity.Menu;
import com.online.system.web.entity.Result;
import com.online.system.web.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.online.system.common.utils.SecurityUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单实现类
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;


    @Override
    @Cacheable(cacheNames ="system-menu", unless = "#result == null")
    @OperLog
    public Result<List<Menu>> queryMenu(String menuType,String username) {
        Result<List<Menu>> result = new Result<>();
        int code = 0;
        String msg = "菜单查询成功";
        ArrayList<Menu> menus = (ArrayList<Menu>)menuDao.queryMenu(menuType,username);
        ArrayList<Menu> trees = (ArrayList<Menu>) menus.clone();
        List<Menu> roots = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == 0) {
                menu.setLevel(1);
                roots.add(menu);
                trees.remove(menu);
            }
        }
        buildNodes(roots, trees);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(roots);
        return result;
    }

    private List<Menu> buildNodes(List<Menu> roots, ArrayList<Menu> trees) {
        if (trees.size() == 0)
            return roots;
        List<Menu> leafs = new ArrayList<>();
        ArrayList<Menu> remains = new ArrayList<>();
        remains = (ArrayList<Menu>) trees.clone();
        for (Menu root : roots) {
            for (Menu child : trees) {
                if (root.getId() == child.getParentId()) {
                    child.setLevel(root.getLevel() + 1);
                    if(null==root.getChildren()){
                      List<Menu> newMenus =  new ArrayList<>();
                      newMenus.add(child);
                      root.setChildren(newMenus);
                    }else{
                        root.getChildren().add(child);
                    }
                    leafs.add(child);
                    remains.remove(child);
                }
            }
        }
        buildNodes(leafs, remains);
        return roots;
    }

    @Override
    @OperLog
    public Result<List<Menu>> queryAllMenu() {
        int code = 0;
        String msg = "菜单查询成功";
        Result<List<Menu>> result = new Result<>();
        List<Menu> menus = menuDao.queryAllMenu(null);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(menus.size());
        result.setData(menus);
        return result;
    }

    /***
     * 删除菜单
     * @return
     */
    @Override
    public Result<String> delMenu(HttpServletRequest request) {
        String username = SecurityUtil.getUsername();
        Result<String> result = new Result<>();
        Menu menu = new Menu();
        int code = 1;
        String msg ="删除失败！";
        int count = 0;
        String id = request.getParameter("id");
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setMsg("参数为空无法删除菜单！");
            return result;
        }
        try{
            menu.setId(Integer.valueOf(id));
            menu.setUpdateTime(new Date());
            menu.setUpdateUser(username);
            count = menuDao.deleteById(menu);
        } catch (Exception e) {
            log.warn("删除菜单出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除菜单成功";
        }
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
