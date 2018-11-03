package cn.thinkfree.service.platform.build;

import cn.thinkfree.database.model.BuildPayConfig;
import cn.thinkfree.database.model.BuildSchemeConfig;

import java.util.List;

/**
 * @author xusonghui
 * 施工配置service
 */
public interface BuildConfigService {

    /**
     * 获取所有施工配置方案
     *
     * @return
     */
    List<BuildSchemeConfig> allBuildScheme();

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
     * @param schemeNo 方案编号
     * @return 支付方案列表
     */
    List<BuildPayConfig> payConfigBySchemeNo(String schemeNo);

    /**
     * 保存支付方案
     *
     * @param schemeNo     方案编号
     * @param progressName 进度名称
     * @param stageNo      阶段编号
     * @param time         未支付超时时间
     * @param remark       备注
     */
    void savePayConfig(String schemeNo, String progressName, String stageNo, int time, String remark);

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
}
