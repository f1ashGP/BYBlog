package com.org.byBlog.pojo.dto;

import com.org.byBlog.validator.group.UserGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
    private Long id;

    @NotNull(groups = {UserGroup.Register.class, UserGroup.Login.class}, message = "账号不能为空")
    private String account;

    @NotNull(groups = {UserGroup.Register.class, UserGroup.Login.class}, message = "密码不能为空")
    private String password;

    @NotNull(groups = UserGroup.Register.class, message = "昵称不能为空")
    private String nickname;

    private String email;
}