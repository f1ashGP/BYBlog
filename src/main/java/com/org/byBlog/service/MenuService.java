package com.org.byBlog.service;

import com.org.byBlog.dao.MenuListDAO;
import com.org.byBlog.dao.PublicUserDAO;
import com.org.byBlog.enums.OperateMode;
import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.po.MenuListPO;
import com.org.byBlog.pojo.po.PublicUserPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.MenuVO;
import com.org.byBlog.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Resource
    private PublicUserDAO publicUserDAO;

    @Resource
    private MenuListDAO menuListDAO;

    public static final Integer PARENT = 0;
    public static final Boolean NOT_CLOSED = true;

    public Result<List<MenuVO>> getMenuList(String account) {
        PublicUserPO user = publicUserDAO.getUserByAccount(account);
        String role = user.getRole();

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRole(role);
        menuDTO.setParentId(PARENT);
        menuDTO.setStatus(NOT_CLOSED);

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

        Result<List<MenuVO>> result = new Result(0, "获取成功");
        result.setData(menuVOList);
        return result;
    }

    public Result<List<MenuListPO>> getParentMenuList() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setParentId(PARENT);
        List<MenuListPO> parentMenuList = menuListDAO.getParentMenuList(menuDTO);

        Result result = new Result(0, "获取成功");
        result.setData(parentMenuList);
        return result;
    }

    public Result<ListVO<MenuVO>> getAllMenuList(MenuDTO menuDTO) {
        menuDTO.setParentId(PARENT);
        Integer totalCount = menuListDAO.getMenuTotalCount(menuDTO);
        if (totalCount == 0) {
            return new Result<>(1, "暂无数据");
        }

        List<MenuListPO> menuList = menuListDAO.getMenuList(menuDTO);
        List<MenuVO> menuVOList = menuList.stream().map(
                menuListPO -> MenuVO.fromListVO(menuListPO)
        ).collect(Collectors.toList());

        ListVO<MenuVO> listVO = new ListVO<>();
        listVO.setList(menuVOList);
        listVO.setTotal(totalCount);

        Result<ListVO<MenuVO>> result = new Result(menuVOList.size() > 0 ? 0 : 1, menuVOList.size() > 0 ? "获取成功" : "获取失败");
        result.setData(listVO);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result operateMenu(MenuDTO menuDTO) {
        String mode = menuDTO.getMode();
        if (OperateMode.DELETE.getMode().equals(mode)) {
            menuDTO.setParentId(menuDTO.getId());
            List<MenuListPO> parentMenuList = menuListDAO.getParentMenuList(menuDTO);
            if (parentMenuList.size() > 0) {
                return new Result(1, "父级侧边栏下还存在子级侧边栏无法删除");
            }
            int delete = menuListDAO.deleteByPrimaryKey(menuDTO.getId());
            return new Result<>(delete > 0 ? 0 : 1, delete > 0 ? "删除成功" : "删除失败");
        }
        MenuListPO menuPO = new MenuListPO();
        if (OperateMode.OPERATE_STATUS.getMode().equals(mode)) {
            MenuListPO menu = menuListDAO.selectByPrimaryKey(menuDTO.getId());
            MenuListPO parentMenu = menuListDAO.selectByPrimaryKey(menu.getParent());
            if (!PARENT.equals(menu.getParent()) && !parentMenu.getStatus()) {
                return new Result(1, "主菜单已关闭，不能进行操作");
            }
            if (PARENT.equals(menu.getParent()) && !menuDTO.getStatus()) {
                menuDTO.setParentId(menuDTO.getId());
                menuListDAO.updateParentMenu(menuDTO);
            }
            BeanUtils.copyProperties(menuDTO, menuPO);
            int selective = menuListDAO.updateByPrimaryKeySelective(menuPO);
            return new Result<>(selective > 0 ? 0 : 1, selective > 0 ? "修改成功" : "修改失败");
        }
        if (OperateMode.UPDATE.getMode().equals(mode)) {
            Integer childMenuCount = menuListDAO.checkIsParentMenu(menuDTO.getId());
            if (childMenuCount > 0 && menuDTO.getStatus()) {
                return new Result(1,"无法成为子级侧边栏，该侧边栏下存在子级侧边栏");
            }
            if (menuDTO.getParentId().equals(menuDTO.getId())) {
                return new Result(1,"父级侧边栏不允许为自己");
            }
            BeanUtils.copyProperties(menuDTO, menuPO);
            menuPO.setParent(menuDTO.getParentId());
            int selective = menuListDAO.updateByPrimaryKeySelective(menuPO);
            return new Result<>(selective > 0 ? 0 : 1, selective > 0 ? "修改成功" : "修改失败");
        }
        return new Result<>(1, "暂无操作类型");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addNewMenu(MenuDTO menuDTO) {
        menuDTO.setMode("name");
        Integer checkMenuNameIsExists = menuListDAO.checkMenuIsExists(menuDTO);
        if (checkMenuNameIsExists > 0) {
            return new Result(1, "菜单名称已存在");
        }
        menuDTO.setMode("path");
        Integer checkMenuPathIsExists = menuListDAO.checkMenuIsExists(menuDTO);
        if (StringUtils.isNotEmpty(menuDTO.getPath()) && checkMenuPathIsExists > 0) {
            return new Result(1, "路径名称已存在");
        }

        String msg = "添加成功";
        MenuListPO menuListPO = new MenuListPO();
        BeanUtils.copyProperties(menuDTO, menuListPO);
        MenuListPO parentMenu = menuListDAO.selectByPrimaryKey(menuDTO.getParentId());
        if (menuDTO.getParentId() != PARENT && !parentMenu.getStatus()) {
            menuListPO.setStatus(false);
            msg += "，子级菜单跟随主菜单关闭";
        }
        menuListPO.setParent(menuDTO.getParentId());
        menuListPO.setCreateTime(new Date());
        int insertSelective = menuListDAO.insertSelective(menuListPO);
        return new Result(insertSelective > 0 ? 0 : 1, insertSelective > 0 ? msg : "添加失败");
    }

    public Result checkMenuIsOpen(String name) {
        MenuListPO menu = menuListDAO.getMenuByName(name);
        if (menu.getStatus()) {
            return new Result(0, "未关闭");
        }
        return new Result(1, "已经关闭");
    }

    public Result getMenuInfo(Integer id) {
        MenuListPO menuListPO = menuListDAO.selectByPrimaryKey(id);
        if (Objects.isNull(menuListPO)) {
            return new Result(1, "该侧边栏不存在");
        }
        MenuVO menuVO = MenuVO.fromPO(menuListPO);
        return new Result(0, "获取成功", menuVO);
    }
}