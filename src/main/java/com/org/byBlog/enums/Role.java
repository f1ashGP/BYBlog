package com.org.byBlog.enums;

public enum Role {
    ADMIN(1, "admin"), NORMAL(2, "normal");

    private Integer id;
    private String desc;

    public static Role get(String desc) {
        for (Role role : values()) {
            if (role.desc.equals(desc)) {
                return role;
            }
        }
        return null;
    }

    Role(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
