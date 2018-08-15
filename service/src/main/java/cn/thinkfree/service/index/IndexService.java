package cn.thinkfree.service.index;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.vo.IndexMenuVO;
import cn.thinkfree.database.vo.IndexReportVO;

import java.util.List;

public interface IndexService  {
    /**
     * 首页 总览
     * @return
     */
    IndexReportVO summary();

    /**
     * 查询首页树形菜单
     * @return
     */
    List<IndexMenuVO> listIndexMenu();

}
