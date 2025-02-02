package com.easyjava.enums;

public enum PageSizeEnum {
    SIZE20(20);
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    PageSizeEnum(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
