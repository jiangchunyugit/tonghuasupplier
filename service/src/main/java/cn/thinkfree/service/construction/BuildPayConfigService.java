package cn.thinkfree.service.construction;

import cn.thinkfree.database.model.BuildPayConfig;

import java.util.List;

/**
 * 施工支付方案配置
 *
 * @author song
 * @version 1.0
 * @date 2018/12/22 19:00
 */
public interface BuildPayConfigService {

    /**
     * 根据方案编号查询支付配置
     * @param schemeNo 方案编号
     * @return 支付配置
     */
    List<BuildPayConfig> findBySchemeNo(String schemeNo);
}
