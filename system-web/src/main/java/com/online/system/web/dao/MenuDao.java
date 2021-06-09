package com.online.system.web.dao;

import com.online.system.web.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单dao
 */
public interface MenuDao  {

    List<Menu> queryMenu(@Param("menuType") String menuType, @Param("username") String username);

    List<Menu> queryAllMenu(@Param("menuType") String menuType);

    int deleteById(Menu menu);
}
