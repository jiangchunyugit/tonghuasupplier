package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 返回带分页的数据
 */
public class PageVo<T> {
    @ApiModelProperty("总条数")
    private long total;
    @ApiModelProperty("每页多少条")
    private int pageSize;
    @ApiModelProperty("第几页")
    private int pageIndex;
    @ApiModelProperty("业务数据")
    private T data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
