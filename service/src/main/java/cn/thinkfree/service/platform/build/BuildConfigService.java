package cn.thinkfree.service.platform.build;

import cn.thinkfree.database.model.BuildPayConfig;
import cn.thinkfree.database.model.BuildSchemeConfig;
import cn.thinkfree.service.platform.vo.CompanySchemeVo;
import cn.thinkfree.service.platform.vo.PageVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xusonghui
 * 施工配置service
 */
public interface BuildConfigService {

    /**
     * 获取所有施工配置方案
     *
     * @param schemeNo
     * @param schemeName
     * @param companyId
     * @param cityStation
     * @param storeNo
     * @param isEnable
     * @param pageSize
     * @param pageIndex
     * @return
     */
    PageVo<List<BuildSchemeConfig>> allBuildScheme(String schemeNo, String schemeName, String companyId, String cityStation,
                                                   String storeNo, int isEnable, int pageSize, int pageIndex);

    /**
     * 创建施工配置方案
     *
     * @param schemeName  方案名称
     * @param companyId   公司ID
     * @param cityStation 城市站ID
     * @param storeNo     门店编号
     * @param remark      备注
     * @return 方案编号
     */
    String createScheme(String schemeName, String companyId, String cityStation, String storeNo, String remark);

    /**
     * 启用施工方案
     *
     * @param schemeNo
     */
    void enableScheme(String schemeNo);

    /**
     * 删除施工方案
     *
     * @param schemeNo
     */
    void delScheme(String schemeNo);

    /**
     * 根据方案编号查询方案基础信息
     *
     * @param schemeNo 方案编号
     * @return 方案信息
     */
    BuildSchemeConfig bySchemeNo(String schemeNo);

    /**
     * 根据方案编号查询支付方案信息
     *
     * @param schemeNo  方案编号
     * @param pageSize  每页几条
     * @param pageIndex 第几页
     * @return 支付方案列表
     */
    PageVo<List<BuildPayConfig>> payConfigBySchemeNo(String schemeNo, int pageSize, int pageIndex);

    /**
     * 保存支付方案
     *@param paySchemeNo   支付方案编号
     * @param schemeNo     方案编号
     * @param progressName 进度名称
     * @param stageNo      阶段编号
     * @param payPercentum 支付方案百分比
     * @param time         未支付超时时间
     * @param remark       备注
     */
    void savePayConfig(String paySchemeNo, String schemeNo, String progressName, String stageNo, BigDecimal payPercentum, int time, String remark);

    /**
     * 删除支付方案
     *
     * @param paySchemeNo 支付方案编号
     */
    void delPayConfig(String paySchemeNo);

    /**
     * 公司选择方案
     *
     * @param companyId      公司ID
     * @param schemeNo       方案编号
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    void chooseScheme(String companyId, String schemeNo, String optionUserId, String optionUserName);

    /**
     * 停用施工方案
     *
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     */
    void stopScheme(String companyId, String optionUserId, String optionUserName);

    /**
     * 根据条件查询施工方案
     *
     * @param searchKey
     * @param companyId
     * @param cityStation
     * @param storeNo
     * @return
     */
    List<BuildSchemeConfig> queryScheme(String searchKey, String companyId, String cityStation, String storeNo);

    /**
     * 根据方案编号查询支付方案列表
     *
     * @param projectNo 方案编号
     * @return
     */
    List<BuildPayConfig> queryPayScheme(String projectNo);

    /**
     * 装饰公司启用施工方案
     *
     * @param companyId      公司ID
     * @param schemeNo       方案编号
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    void companyEnableScheme(String companyId, String schemeNo, String optionUserId, String optionUserName);

    /**
     * 根据公司ID查询公司选择的施工方案
     *
     * @param companyId
     * @param pageSize
     * @param pageIndex
     */
    PageVo<List<CompanySchemeVo>> queryByCompanyId(String companyId, int pageSize, int pageIndex);

    /**
     * 装饰公司删除施工方案
     *
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     * @param schemeNo       方案编号
     */
    void companyDelScheme(String companyId, String optionUserId, String optionUserName, String schemeNo);

    /**
     * 根据公司ID查询方案编号
     * @param companyId
     * @return
     */
    String getSchemeNoByCompanyId(String companyId);
}
