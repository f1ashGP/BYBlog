package com.org.byBlog.pojo.dto;

import com.org.byBlog.validator.group.MenuGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MenuDTO extends BaseDTO {

    @NotNull(groups = MenuGroup.OperateMenu.class, message = "id不能为空")
    private Integer id;

    @NotNull(groups = {MenuGroup.AddNewMenu.class}, message = "状态不能为空")
    private Boolean status;

    private String role;

    private Integer parentId;

    @NotNull(groups = {MenuGroup.AddNewMenu.class}, message = "名称不能为空")
    private String name;

    private String path;

    private String icon;
}
