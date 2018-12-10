package cn.thinkfree.service.materialsrembrand;

import cn.thinkfree.database.mapper.MaterialsRemBrandMapper;
import cn.thinkfree.database.mapper.MaterialsRemBrandSecondMapper;
import cn.thinkfree.database.model.MaterialsRemBrand;
import cn.thinkfree.database.model.MaterialsRemBrandExample;
import cn.thinkfree.database.model.MaterialsRemBrandSecond;
import cn.thinkfree.database.model.MaterialsRemBrandSecondExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 品牌信息
 */
@Service
public class MaterialsRemBrandServiceImpl implements MaterialsRemBrandService {

    @Autowired
    MaterialsRemBrandMapper materialsRemBrandMapper;

    @Autowired
    MaterialsRemBrandSecondMapper materialsRemBrandSecondMapper;
    @Override
    public List<MaterialsRemBrand> getMaterialsRemBrands() {

        MaterialsRemBrandExample materialsRemBrandExample = new MaterialsRemBrandExample();
        return materialsRemBrandMapper.selectByExample(materialsRemBrandExample);
    }

    @Override
    public List<MaterialsRemBrandSecond> getMaterialsRemBrandSecond(String sbbm) {

        MaterialsRemBrandSecondExample materialsRemBrandSecondExample = new MaterialsRemBrandSecondExample();
        materialsRemBrandSecondExample.createCriteria().andSbbmEqualTo(sbbm);
        return materialsRemBrandSecondMapper.selectByExample(materialsRemBrandSecondExample);
    }
}
