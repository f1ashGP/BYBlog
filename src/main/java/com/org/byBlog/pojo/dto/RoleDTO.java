package com.org.byBlog.pojo.dto;

import com.org.byBlog.validator.group.RoleGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleDTO extends BaseDTO {

    @NotNull(groups = {RoleGroup.OperateRole.class}, message = "id不能为空")
    private Integer id;

    @NotNull(groups = {RoleGroup.AddNewRole.class}, message = "roleName不能为空")
    private String roleName;

    @NotNull(groups = {RoleGroup.AddNewRole.class}, message = "status不能为空")
    private Boolean status;

    @NotNull(groups = {RoleGroup.AddNewRole.class}, message = "path不能为空")
    private String path;
}
