package com.easylive.common.entity.enums;

public enum PageSizeEnum {
    SIZE20(20);
    private long pageSize;

    public long getPageSize() {
        return pageSize;
    }

    PageSizeEnum(long pageSize) {
        this.pageSize = pageSize;
    }
}
