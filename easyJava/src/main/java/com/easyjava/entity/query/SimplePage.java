package com.easyjava.entity.query;

import com.easyjava.entity.enums.PageSizeEnum;

public class SimplePage {
    private long pageNo;
    private long pageSize;
    private long totalCount;
    private long totalPage;
    private long start;
    private long end;

    public SimplePage(long pageNo, long pageSize, long totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        action();
    }

    private void action() {
        // 默认值处理
        if (this.pageSize <= 0) {
            this.pageSize = PageSizeEnum.SIZE20.getPageSize();
        }
        if (this.totalCount < 0) {
            this.totalCount = 0;
        }

        // 计算总页数
        this.totalPage = (this.totalCount + this.pageSize - 1) / this.pageSize;

        // 限制 pageNo 范围
        this.pageNo = Math.max(1, Math.min(this.pageNo, this.totalPage));

        // 计算起始位置
        this.start = (this.pageNo - 1) * this.pageSize;
        this.end = this.pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
