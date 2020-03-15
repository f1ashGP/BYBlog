package com.org.byBlog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by Lowi on 2018/8/7 0007.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListVO<T> {

    private List<T> list;
    private Integer total;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
