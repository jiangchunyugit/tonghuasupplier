package cn.thinkfree.service.materialsrembrand;

import cn.thinkfree.database.mapper.DealerBrandInfoMapper;
import cn.thinkfree.database.mapper.MaterialsRemBrandMapper;
import cn.thinkfree.database.mapper.MaterialsRemBrandSecondMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.constants.BrandConstants;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    DealerBrandInfoMapper dealerBrandInfoMapper;
    @Override
    public List<MaterialsRemBrand> getMaterialsRemBrands(String sbmc) {

        MaterialsRemBrandExample materialsRemBrandExample = new MaterialsRemBrandExample();
        MaterialsRemBrandExample.Criteria criteria = materialsRemBrandExample.createCriteria();
        if (StringUtils.isNotBlank(sbmc)) {
            StringBuffer stringBufferSbmc = new StringBuffer();
            stringBufferSbmc.append("%");
            stringBufferSbmc.append(sbmc);
            stringBufferSbmc.append("%");
            criteria.andSbmcLike(stringBufferSbmc.toString());
        }
        return materialsRemBrandMapper.selectByExample(materialsRemBrandExample);
    }

    @Override
    public List<MaterialsRemBrandSecond> getMaterialsRemBrandSecond(String sbbm) {

        MaterialsRemBrandSecondExample materialsRemBrandSecondExample = new MaterialsRemBrandSecondExample();
        materialsRemBrandSecondExample.createCriteria().andSbbmEqualTo(sbbm);
        return materialsRemBrandSecondMapper.selectByExample(materialsRemBrandSecondExample);
    }

    @Override
    public List<DealerBrandInfo> getDealerBrandList(String companyId) {

//        return dealerBrandInfoMapper.selectByContract(companyId);
        return null;
    }

    @Override
    public List<DealerBrandInfo> getDealerBrandSecondList(String companyId, String brandNo) {

        DealerBrandInfoExample dealerBrandInfoExample = new DealerBrandInfoExample();
        DealerBrandInfoExample.Criteria criteria = dealerBrandInfoExample.createCriteria();

        criteria.andCompanyIdEqualTo(companyId);
        criteria.andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code);
        criteria.andBrandNoEqualTo(brandNo);

        return dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);
        }
}
