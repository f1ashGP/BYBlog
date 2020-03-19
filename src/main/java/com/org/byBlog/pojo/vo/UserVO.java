package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.org.byBlog.pojo.po.PublicUserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@ApiModel("用户")
public class UserVO extends BaseVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("nickname")
    private String nickname;

    @ApiModelProperty("状态")
    private Boolean freeze;

    @ApiModelProperty("email")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("上次登陆时间")
    private Date lastLoginTime;

    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("权限")
    private String role;

    public static UserVO fromPO(PublicUserPO publicUserPO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(publicUserPO, userVO);

        return userVO;
    }
}
