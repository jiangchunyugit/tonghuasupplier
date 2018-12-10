package cn.thinkfree.service.materialsrembrand;

import cn.thinkfree.database.model.MaterialsRemBrand;
import cn.thinkfree.database.model.MaterialsRemBrandSecond;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 品牌信息
 */
public interface MaterialsRemBrandService {

    /**
     * 品牌信息
     * @return
     */
    List<MaterialsRemBrand> getMaterialsRemBrands();

    /**
     * 通过品牌编码查找品类
     * @param sbbm
     * @return
     */
    List<MaterialsRemBrandSecond> getMaterialsRemBrandSecond(String sbbm);
}
