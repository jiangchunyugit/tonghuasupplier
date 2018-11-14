package cn.thinkfree.service.neworder;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.pcvo.ProjectQuotationCheckVo;
import cn.thinkfree.database.pcvo.QuotationVo;
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
     * @Description 新增软保价
     * @Date
     * @Param
     * @return
     **/
    MyRespBundle<String> saveSoftQuote(SoftQuoteVO softQuoteVO);
    /**
     * @Author jiang
     * @Description 新增硬保价
     * @Date
     * @Param
     * @return
     **/
    MyRespBundle<String> saveHardQuote(HardQuoteVO hardQuoteVO);
    /**
     * @Author jiang
     * @Description 新增施工基础保价
     * @Date
     * @Param
     * @return
     **/
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
     * @param checkVo
     * @return
     */
    MyRespBundle<String> addCheckDetail(ProjectQuotationCheckVo checkVo);
}
