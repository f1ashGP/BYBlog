package com.org.byBlog.controller;

import com.org.byBlog.pojo.dto.UserDTO;
import com.org.byBlog.pojo.po.PublicUserPO;
import com.org.byBlog.service.UserService;
import com.org.byBlog.utils.Result;
import com.org.byBlog.validator.group.UserGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"用户接口"})
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户注册")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "nickname", value = "昵称", required = true, dataType = "String", paramType = "query", defaultValue = "F1ash")
            }
    )
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@ApiIgnore @Validated({UserGroup.Register.class}) UserDTO userDTO,
                           @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            return new Result(errors);
        }

        Result result = userService.register(userDTO);
        return result;
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "String", paramType = "query", defaultValue = "admin"),
                    @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query", defaultValue = "admin")
            }
    )
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@ApiIgnore HttpServletRequest request, @ApiIgnore @Validated({UserGroup.Login.class}) UserDTO userDTO,
                        @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            return new Result(errors);
        }

        Result result = userService.login(userDTO);
        PublicUserPO data = (PublicUserPO) result.getData();
        if (result.getCode() == 0) {
            request.getSession().setAttribute("uid", data.getId());
            result.setData(data.getId());
        }
        return result;
    }

    @ApiOperation(value = "登录超时", hidden = true)
    @RequestMapping(value = "/loginTimeout", method = {RequestMethod.POST, RequestMethod.GET})
    public Result loginTimeout() {
        return new Result(3, "登录超时");
    }

    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public Result logout(@ApiIgnore HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        return new Result(0, "退出成功");
    }

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/getLoginInfo", method = {RequestMethod.GET})
    public Result getLoginInfo(@ApiIgnore HttpServletRequest request) {
        Long uid = (Long) request.getSession().getAttribute("uid");
        Result result = userService.getLoginInfo(uid);
        return result;
    }

}
