package cn.tonghua.service.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author xusonghui
 */
public class PageInfoUtils {
    /**
     * pageInfo转换
     * @param pageInfo
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> PageInfo<T> pageInfo(PageInfo<?> pageInfo,List<T> ts){
        PageInfo<T> pageInfo1 = new PageInfo<>(ts);
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setHasNextPage(pageInfo.isHasNextPage());
        pageInfo1.setPages(pageInfo.getPages());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        pageInfo1.setEndRow(pageInfo.getEndRow());
        pageInfo1.setFirstPage(pageInfo.getFirstPage());
        pageInfo1.setHasPreviousPage(pageInfo.isHasPreviousPage());
        pageInfo1.setIsFirstPage(pageInfo.isIsFirstPage());
        return pageInfo1;
    }
}
