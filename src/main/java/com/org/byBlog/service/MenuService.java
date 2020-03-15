package com.org.byBlog.service;

import com.org.byBlog.dao.MenuListDAO;
import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.po.MenuListPO;
import com.org.byBlog.pojo.po.PublicUserPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.MenuVO;
import com.org.byBlog.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Resource
    private PublicUserDAO publicUserDAO;

    @Resource
    private MenuListDAO menuListDAO;

    private static final Integer PARENT_ID = 0;

    public Result<List<MenuVO>> getMenuList(String account) {
        PublicUserPO user = publicUserDAO.getUserByAccount(account);
        String role = user.getRole();

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRole(role);
        menuDTO.setParentId(PARENT_ID);

        // 返回数据
        List<MenuVO> menuVOList = new ArrayList<>();

        List<MenuListPO> menuList = menuListDAO.getMenuByUserList(menuDTO);
        for (MenuListPO menu : menuList) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            menuDTO.setParentId(menu.getId());
            List<MenuListPO> childMenuList = menuListDAO.getMenuByUserList(menuDTO);
            List<MenuVO> childList = childMenuList.stream().map(
                    menuListPO -> MenuVO.fromPO(menuListPO)
            ).collect(Collectors.toList());
            menuVO.setChildMenuList(childList);
            menuVOList.add(menuVO);
        }

        Result<List<MenuVO>> result = new Result(menuVOList.size() > 0 ? 0 : 1,menuVOList.size() > 0 ? "获取成功" : "获取失败");
        result.setData(menuVOList);
        return result;
    }

    public Result<ListVO<MenuVO>> getAllMenuList(MenuDTO menuDTO) {
        Integer totalCount = menuListDAO.getMenuTotalCount(menuDTO);
        if (totalCount == 0) {
            return new Result<>(1,"暂无数据");
        }

        List<MenuListPO> menuList = menuListDAO.getMenuList(menuDTO);
        List<MenuVO> menuVOList = menuList.stream().map(
                menuListPO -> MenuVO.fromListVO(menuListPO)
        ).collect(Collectors.toList());

        ListVO<MenuVO> listVO = new ListVO<>();
        listVO.setList(menuVOList);
        listVO.setTotal(totalCount);

        Result<ListVO<MenuVO>> result = new Result(menuVOList.size() > 0 ? 0 : 1,menuVOList.size() > 0 ? "获取成功" : "获取失败");
        result.setData(listVO);
        return result;
    }
}