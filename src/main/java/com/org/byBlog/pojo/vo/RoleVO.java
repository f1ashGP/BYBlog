package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.org.byBlog.pojo.po.RoleAccessPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@ApiModel("权限")
public class RoleVO extends BaseVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("权限")
    private String roleName;

    @ApiModelProperty("状态")
    private Boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    public static RoleVO fromPO(RoleAccessPO roleAccessPO) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(roleAccessPO, roleVO);
        roleVO.setRoleName(roleAccessPO.getRole());

        return roleVO;
    }
}
