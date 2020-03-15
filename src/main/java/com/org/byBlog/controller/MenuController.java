package com.org.byBlog.controller;

import com.org.byBlog.pojo.dto.MenuDTO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.MenuVO;
import com.org.byBlog.service.MenuService;
import com.org.byBlog.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation(value = "获取所有菜单列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "page", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "count", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1")
            }
    )
    @RequestMapping(value = "/getAllMenuList", method = RequestMethod.GET)
    public Result<ListVO<MenuVO>> getAllMenuList(@ApiIgnore MenuDTO menuDTO) {
        Result result = menuService.getAllMenuList(menuDTO);
        return result;
    }
}
