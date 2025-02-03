package com.easyjava.entity.query;

import com.easyjava.enums.PageSizeEnum;

public class SimplePage {
    private int pageNo;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private int start;
    private int end;

    public SimplePage() {}

    public SimplePage(Integer pageNo, int totalCount, int pageSize) {
        if (null == pageNo) {
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        action();
    }

    public SimplePage(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void action() { // TODO: 检验这个方法逻辑是否正确
        if (this.pageNo <= 0) {
            this.pageNo = PageSizeEnum.SIZE20.getPageSize();
        }
        if (this.totalCount > 0) {
            this.totalPage = this.totalCount % this.pageSize == 0 ? this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1;
        } else {
            this.totalPage = 1;
        }
        if (this.pageNo <= 1) {
            this.pageNo = 1;
        }
        if (this.pageNo > this.totalPage) {
            this.pageNo = this.totalPage;
        }
        this.start = (this.pageNo - 1) * this.pageSize;
        this.end = this.pageSize;
    }
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.action();
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
