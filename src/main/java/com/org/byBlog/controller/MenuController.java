package com.org.byBlog.controller;

import com.org.byBlog.enums.Role;
import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.po.MenuListPO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.MenuVO;
import com.org.byBlog.service.MenuService;
import com.org.byBlog.utils.Result;
import com.org.byBlog.validator.group.MenuGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

@Api(tags = {"菜单列表"})
@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @ApiOperation(value = "获取当前登录用户权限的菜单列表")
    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    public Result<List<MenuVO>> getMenuList(@ApiIgnore HttpServletRequest request) {
        String account = (String) request.getSession().getAttribute("account");
        Result result = menuService.getMenuList(account);
        return result;
    }

    @ApiOperation(value = "获取菜单下所有权限")
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    public Result<List<Map<String, String>>> getRoleList() {
        List<String> roleList = Arrays.asList(Role.NORMAL.getDesc(), Role.ADMIN.getDesc());

        List<Map<String, String>> roleVoList = new ArrayList<>();
        for (String role : roleList) {
            Map<String, String> map = new HashMap<>();
            map.put("role", role);
            roleVoList.add(map);
        }
        Result result = new Result(0, "获取成功", roleVoList);
        return result;
    }

    @ApiOperation(value = "获取所有父级菜单列表")
    @RequestMapping(value = "/getParentMenuList", method = RequestMethod.GET)
    public Result<List<MenuListPO>> getParentMenuList() {
        Result result = menuService.getParentMenuList();
        return result;
    }

    @ApiOperation(value = "获取所有菜单列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "page", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "count", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "name", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "侧边栏管理"),
                    @ApiImplicitParam(name = "role", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "status", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "false")
            }
    )
    @RequestMapping(value = "/getAllMenuList", method = RequestMethod.GET)
    public Result<ListVO<MenuVO>> getAllMenuList(@ApiIgnore MenuDTO menuDTO) {
        Result result = menuService.getAllMenuList(menuDTO);
        return result;
    }

    @ApiOperation(value = "根据id修改或删除侧边栏")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Int", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query", defaultValue = "人员管理"),
                    @ApiImplicitParam(name = "path", value = "路径", required = false, dataType = "String", paramType = "query", defaultValue = "/path"),
                    @ApiImplicitParam(name = "icon", value = "图标", required = false, dataType = "String", paramType = "query", defaultValue = "el-icon-setting"),
                    @ApiImplicitParam(name = "parentId", value = "父级列表", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "status", value = "状态 true = 关闭，false = 未关闭", required = true, dataType = "String", paramType = "query", defaultValue = "false"),
                    @ApiImplicitParam(name = "mode", value = "update = 修改 delete = 删除 operateStatus = 修改状态", required = true, dataType = "String", paramType = "query", defaultValue = "false")
            }
    )
    @RequestMapping(value = "/operateMenu", method = RequestMethod.POST)
    public Result operateMenu(@ApiIgnore @Validated({MenuGroup.OperateMenu.class}) MenuDTO menuDTO) {
        Result result = menuService.operateMenu(menuDTO);
        return result;
    }

    @ApiOperation(value = "增加新的侧边栏")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "query", defaultValue = "人员管理"),
                    @ApiImplicitParam(name = "path", value = "路径", required = false, dataType = "String", paramType = "query", defaultValue = "/path"),
                    @ApiImplicitParam(name = "icon", value = "图标", required = false, dataType = "String", paramType = "query", defaultValue = "el-icon-setting"),
                    @ApiImplicitParam(name = "parentId", value = "父级列表", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "status", value = "状态 true = 关闭，false = 未关闭", required = true, dataType = "String", paramType = "query", defaultValue = "false")
            }
    )
    @RequestMapping(value = "/addNewMenu", method = RequestMethod.POST)
    public Result addNewMenu(@ApiIgnore @Validated({MenuGroup.AddNewMenu.class}) MenuDTO menuDTO) {
        Result result = menuService.addNewMenu(menuDTO);
        return result;
    }

    @ApiOperation(value = "判断是否开启该侧边栏")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "menuName", value = "侧边栏名称", required = true, dataType = "String", paramType = "query", defaultValue = "人员管理")
            }
    )
    @RequestMapping(value = "/checkMenuIsOpen", method = RequestMethod.GET)
    public Result checkMenuIsOpen(@ApiIgnore String menuName) {
        if (Objects.isNull(menuName) || StringUtils.isEmpty(menuName)) {
            return new Result(1,"请填写menuName");
        }
        Result result = menuService.checkMenuIsOpen(menuName);
        return result;
    }

    @ApiOperation(value = "获取单个侧边栏信息")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "query", defaultValue = "1")
            }
    )
    @RequestMapping(value = "/getMenuInfo", method = RequestMethod.GET)
    public Result getMenuInfo(@ApiIgnore @NotNull(message = "id不能为空") Integer id) {
        Result result = menuService.getMenuInfo(id);
        return result;
    }
}