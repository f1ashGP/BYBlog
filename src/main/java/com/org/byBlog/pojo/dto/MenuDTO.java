package com.org.byBlog.pojo.dto;

import lombok.Data;

@Data
public class MenuDTO extends BaseDTO{

    private String role;

    private Integer parentId;

}
