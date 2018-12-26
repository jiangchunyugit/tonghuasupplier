package cn.thinkfree.service.materialsremagency;

import cn.thinkfree.database.mapper.MaterialsRemAgencyMapper;
import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.database.model.MaterialsRemAgencyExample;
import cn.thinkfree.database.vo.AgencyContractCompanyInfoVo;
import org.apache.commons.lang3.StringUtils;
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
    public List<MaterialsRemAgency> getMaterialsRemAgencys(String code, String name) {

        MaterialsRemAgencyExample materialsRemAgencyExample = new MaterialsRemAgencyExample();
        MaterialsRemAgencyExample.Criteria criteria = materialsRemAgencyExample.createCriteria();
        if (StringUtils.isNotBlank(code)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("%");
            stringBuffer.append(code);
            stringBuffer.append("%");
            criteria.andCodeLike(stringBuffer.toString());
        }
        if (StringUtils.isNotBlank(name)) {
            StringBuffer stringBufferName = new StringBuffer();
            stringBufferName.append("%");
            stringBufferName.append(name);
            stringBufferName.append("%");
            criteria.andNameLike(stringBufferName.toString());
        }
        return materialsRemAgencyMapper.selectByExample(materialsRemAgencyExample);
    }

    @Override
    public List<AgencyContractCompanyInfoVo> getAgencyCompanyInfos(String companyId, String dealerCompanyId) {
        if (StringUtils.isNotBlank(dealerCompanyId)) {
            StringBuffer stringBufferName = new StringBuffer();
            stringBufferName.append("%");
            stringBufferName.append(dealerCompanyId);
            stringBufferName.append("%");
            dealerCompanyId = stringBufferName.toString();
        }
        return materialsRemAgencyMapper.getAgencyCompanyInfos(companyId,dealerCompanyId);
    }
}
