package com.org.byBlog.pojo.dto;

public class BaseDTO {
    private Integer uid;

    private String name;

    private Integer roleId;

    private String role;

    private Long directGroupId;

    private Integer page = 1;

    private Integer count = 1;

    private Integer offset = (page - 1) * count;

    private String order;

    private String sort;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDirectGroupId() {
        return directGroupId;
    }

    public void setDirectGroupId(Long directGroupId) {
        this.directGroupId = directGroupId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;

        this.setOffset((this.page - 1) * this.count);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;

        this.setOffset((this.page - 1) * this.count);
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
