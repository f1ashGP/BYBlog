package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserVO extends BaseVO {

    private String nickname;
}