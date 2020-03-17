package com.org.byBlog.enums;

public enum OperateMode {
    UPDATE(1, "update"), DELETE(2, "delete"),OPERATE_STATUS(3,"operateStatus");

    private Integer id;
    private String mode;

    public static OperateMode get(String desc) {
        for (OperateMode mode : values()) {
            if (mode.mode.equals(desc)) {
                return mode;
            }
        }
        return null;
    }

    OperateMode(Integer id, String mode) {
        this.id = id;
        this.mode = mode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
