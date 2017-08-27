package com.whl.client.gateway.model;

public class PageInfo {

    private int pageNumber;
    private int pageSize;
    private int totalPage;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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

    @Override
    public String toString() {
        return "PageInfo [pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", totalPage=" + totalPage + "]";
    }

}
