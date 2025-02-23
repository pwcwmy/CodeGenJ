package com.easyjava.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class PaginationResultVO<T> {
    private long totalCount;
    private long pageSize;
    private long pageNo;
    private long totalPage;
    private List<T> list = new ArrayList<>();

    public PaginationResultVO() {}

    public PaginationResultVO(long totalCount, long pageSize, long pageNo, long totalPage, List<T> list) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.totalPage = totalPage;
        this.list = list;
    }

    public PaginationResultVO(long totalCount, long pageSize, long totalPage, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.list = list;
    }

    public PaginationResultVO(List<T> list) {
        this.list = list;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
