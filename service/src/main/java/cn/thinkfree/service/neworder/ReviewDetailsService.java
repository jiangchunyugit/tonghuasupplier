package cn.thinkfree.service.neworder;

import cn.thinkfree.database.vo.BasisConstructionVO;
import cn.thinkfree.database.vo.HardQuoteVO;
import cn.thinkfree.database.vo.SoftQuoteVO;

import java.util.List;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 11:46
 * @Description:
 */
public interface ReviewDetailsService {
    /**
     * @Author jiang
     * @Description 硬装保价详情
     * @Date
     * @Param
     * @return
     **/
    List<HardQuoteVO> getHardQuote(String projectNo, String roomType);
    /**
     * @Author jiang
     * @Description 软装保价详情
     * @Date
     * @Param
     * @return
     **/
    List<SoftQuoteVO> getSoftQuote(String projectNo, String roomType);
    /**
     * @Author jiang
     * @Description 基础保价详情
     * @Date
     * @Param
     * @return
     **/
    List<BasisConstructionVO> getBasisConstruction(String projectNo, String roomType);
    /**
     * @Author jiang
     * @Description 新增软保价
     * @Date
     * @Param
     * @return
     **/
    List<SoftQuoteVO> saveSoftQuote(SoftQuoteVO softQuoteVO);
    /**
     * @Author jiang
     * @Description 新增硬保价
     * @Date
     * @Param
     * @return
     **/
    List<HardQuoteVO> saveHardQuote(HardQuoteVO hardQuoteVO);
    /**
     * @Author jiang
     * @Description 新增施工基础保价
     * @Date
     * @Param
     * @return
     **/
    List<BasisConstructionVO> saveBasisConstructionVO(BasisConstructionVO basisConstructionVO);
}
