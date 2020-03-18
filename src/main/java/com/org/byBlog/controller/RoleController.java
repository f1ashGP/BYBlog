package com.org.byBlog.controller;

import com.org.byBlog.pojo.dto.RoleDTO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.RoleVO;
import com.org.byBlog.service.RoleService;
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

@Api(tags = {"权限列表"})
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "获取所有菜单列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "page", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "count", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1")
            }
    )
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    public Result<ListVO<RoleVO>> getRoleList(@ApiIgnore RoleDTO roleDTO) {
        Result result = roleService.getRoleList(roleDTO);
        return result;
    }
}
