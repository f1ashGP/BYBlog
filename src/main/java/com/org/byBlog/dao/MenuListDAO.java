package com.org.byBlog.dao;

import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.po.MenuListPO;

import java.util.List;

public interface MenuListDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuListPO record);

    int insertSelective(MenuListPO record);

    MenuListPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MenuListPO record);

    int updateByPrimaryKey(MenuListPO record);

    List<MenuListPO> getMenuList(MenuDTO menuDTO);

    List<MenuListPO> getMenuByUserList(MenuDTO menuDTO);

    Integer getMenuTotalCount(MenuDTO menuDTO);
}