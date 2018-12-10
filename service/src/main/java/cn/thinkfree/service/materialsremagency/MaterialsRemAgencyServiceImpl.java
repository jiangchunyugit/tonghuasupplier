package cn.thinkfree.service.materialsremagency;

import cn.thinkfree.database.mapper.MaterialsRemAgencyMapper;
import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.database.model.MaterialsRemAgencyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商
 */
@Service
public class MaterialsRemAgencyServiceImpl implements MaterialsRemAgencyService{

    @Autowired
    MaterialsRemAgencyMapper materialsRemAgencyMapper;

    @Override
    public List<MaterialsRemAgency> getMaterialsRemAgencys(String code) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("%");
        stringBuffer.append(code);
        stringBuffer.append("%");
        MaterialsRemAgencyExample materialsRemAgencyExample = new MaterialsRemAgencyExample();
        materialsRemAgencyExample.createCriteria().andCodeLike(stringBuffer.toString());
        return materialsRemAgencyMapper.selectByExample(materialsRemAgencyExample);
    }
}
