package com.easyjava.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class PaginationResultVO<T> {
    private Integer totalCount;
    private Integer pageSize;
    private Integer pageNo;
    private Integer totalPage;
    private List<T> list = new ArrayList<>();

    public PaginationResultVO() {}

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer pageNo, Integer totalPage, List<T> list) {
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.totalPage = totalPage;
        this.list = list;
    }

    public PaginationResultVO(Integer totalCount, Integer pageSize, Integer totalPage, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.list = list;
    }

    public PaginationResultVO(List<T> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
