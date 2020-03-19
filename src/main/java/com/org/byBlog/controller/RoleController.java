package com.org.byBlog.controller;

import com.org.byBlog.pojo.dto.RoleDTO;
import com.org.byBlog.pojo.vo.ListVO;
import com.org.byBlog.pojo.vo.RoleVO;
import com.org.byBlog.service.RoleService;
import com.org.byBlog.utils.Result;
import com.org.byBlog.validator.group.RoleGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
                    @ApiImplicitParam(name = "count", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "name", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "侧边栏列表"),
                    @ApiImplicitParam(name = "role", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "status", value = "", required = false, dataType = "String", paramType = "query", defaultValue = "false")
            }
    )
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    public Result<ListVO<RoleVO>> getRoleList(@ApiIgnore RoleDTO roleDTO) {
        Result result = roleService.getRoleList(roleDTO);
        return result;
    }

    @ApiOperation(value = "获取单个权限信息")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "query", defaultValue = "1")
            }
    )
    @RequestMapping(value = "/getRoleInfo", method = RequestMethod.GET)
    public Result getRoleInfo(@ApiIgnore @NotNull(message = "id不能为空") Integer id) {
        Result result = roleService.getRoleInfo(id);
        return result;
    }

    @ApiOperation(value = "增加新的侧边栏")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "roleName", value = "名称", required = true, dataType = "String", paramType = "query", defaultValue = "人员管理"),
                    @ApiImplicitParam(name = "path", value = "路径", required = false, dataType = "String", paramType = "query", defaultValue = "/path"),
                    @ApiImplicitParam(name = "role", value = "权限", required = false, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "status", value = "状态 true = 关闭，false = 未关闭", required = true, dataType = "String", paramType = "query", defaultValue = "false")
            }
    )
    @RequestMapping(value = "/addNewRole", method = RequestMethod.POST)
    public Result addNewRole(@ApiIgnore @Validated({RoleGroup.AddNewRole.class}) RoleDTO roleDTO) {
        Boolean roleName = roleService.getRoleName(roleDTO.getRoleName());
        if (roleName) {
            return new Result(1, "权限不符合规则");
        }
        Result result = roleService.addNewRole(roleDTO);
        return result;
    }

    @ApiOperation(value = "根据id修改或删除侧边栏")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Int", paramType = "query", defaultValue = "1"),
                    @ApiImplicitParam(name = "roleName", value = "名称", required = true, dataType = "String", paramType = "query", defaultValue = "人员管理"),
                    @ApiImplicitParam(name = "path", value = "路径", required = false, dataType = "String", paramType = "query", defaultValue = "/path"),
                    @ApiImplicitParam(name = "role", value = "权限", required = false, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "status", value = "状态 true = 关闭，false = 未关闭", required = true, dataType = "String", paramType = "query", defaultValue = "false"),
                    @ApiImplicitParam(name = "mode", value = "update = 修改 delete = 删除 operateStatus = 修改状态", required = true, dataType = "String", paramType = "query", defaultValue = "update")
            }
    )
    @RequestMapping(value = "/operateRole", method = RequestMethod.POST)
    public Result operateRole(@ApiIgnore @Validated({RoleGroup.OperateRole.class}) RoleDTO roleDTO) {
        Result result = roleService.operateRole(roleDTO);
        return result;
    }

}
