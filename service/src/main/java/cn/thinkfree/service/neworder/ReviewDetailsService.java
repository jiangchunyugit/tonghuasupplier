package cn.thinkfree.service.neworder;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.pcvo.ProjectQuotationCheckVo;
import cn.thinkfree.database.pcvo.QuotationVo;
import cn.thinkfree.database.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author : jiang
 * @Date: 2018/11/13 11:46
 * @Description:
 */
public interface ReviewDetailsService {

    /**
     * 新增软保价
     * @param softQuoteVO
     * @return
     */
    MyRespBundle<String> saveSoftQuote(SoftQuoteVO softQuoteVO);

    /**
     * 新增硬保价
     * @param hardQuoteVO
     * @return
     */
    MyRespBundle<String> saveHardQuote(HardQuoteVO hardQuoteVO);

    /**
     * 新增施工基础保价
     * @param basisConstructionVO
     * @return
     */
    MyRespBundle<String> saveBasisConstructionVO(BasisConstructionVO basisConstructionVO);

    /**
     * 获取精准报价
     * @param projectNo
     * @return
     */
    MyRespBundle<List<QuotationVo>> getPriceDetail(String projectNo);

    /**
     * 删除软装
     * @param id
     * @return
     */
    MyRespBundle<String> delSoftQuote(String id);

    /**
     * 删除硬装
     * @param id
     * @return
     */
    MyRespBundle<String> delHardQuote(String id);

    /**
     * 删除基础施工项
     * @param id
     * @return
     */
    MyRespBundle<String> delBasisConstruction(String id);

    /**
     * 获取精准报价审核信息
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectQuotationCheckVo> getCheckDetail(String projectNo);

    /**
     * 提交精准报价审核信息
     * @param projectNo
     * @return
     */
    MyRespBundle<String> addCheckDetail(String projectNo);

    /**
     * 审核精准报价
     * @param projectNo
     * @param result
     * @param refuseReason
     * @return
     */
    MyRespBundle<String> reviewOffer(String projectNo, int result, String refuseReason);

    /**
     * 获取上海报价信息(转施工)
     * @param projectNo
     * @param predatingTime
     * @param remark
     * @return
     */
    MyRespBundle getShangHaiPriceDetail(String projectNo);

    /**
     * 设计师发起预交底详情页---->app使用
     * @param projectNo
     * @return
     */
    MyRespBundle<PredatingVo> queryPredatingDetails(String projectNo);

    /**
     * remark
     * @param projectNo
     * @param predatingTime
     * @param remark
     * @return
     */
    MyRespBundle projectPredating(String projectNo, Date predatingTime, String remark);

    /**
     * 设计信息
     * @param projectNo
     * @return
     */
    MyRespBundle<DesignVo> getDesignDetail(String projectNo);
}
