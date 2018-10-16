package cn.thinkfree.service.platform.designer.vo;

/**
 * @author xusonghui
 * 返回带分页的数据
 */
public class PageVo<T> {

    private long total;
    private int pageSize;
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
}
