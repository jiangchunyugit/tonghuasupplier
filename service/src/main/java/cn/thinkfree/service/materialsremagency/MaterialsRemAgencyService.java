package cn.thinkfree.service.materialsremagency;

import cn.thinkfree.database.model.MaterialsRemAgency;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商信息
 */
public interface MaterialsRemAgencyService {

    /**
     * 获取经销商信息
     * @return
     */
    List<MaterialsRemAgency> getMaterialsRemAgencys(String code,String name);
}
