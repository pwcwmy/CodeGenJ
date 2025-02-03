package com.easyjava.entity.enums;

public enum PageSizeEnum {
    SIZE10(0), SIZE15(15), SIZE20(20), SIZE50(50), SIZE100(100);
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    PageSizeEnum(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
