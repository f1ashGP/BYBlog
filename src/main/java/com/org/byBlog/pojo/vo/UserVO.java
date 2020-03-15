package com.org.byBlog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户返回列表")
public class UserVO extends BaseVO {

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("权限")
    private String role;
}