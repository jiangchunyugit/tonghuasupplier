package cn.thinkfree.service.materialsrembrand;

import cn.thinkfree.database.mapper.DealerBrandInfoMapper;
import cn.thinkfree.database.mapper.DealerCategoryMapper;
import cn.thinkfree.database.mapper.MaterialsRemBrandMapper;
import cn.thinkfree.database.mapper.MaterialsRemBrandSecondMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.constants.BrandConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    DealerCategoryMapper dealerCategoryMapper;

    /**
     * 截取最小值
     */
    private final int MinValue = 3;
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

//        MaterialsRemBrandSecondExample materialsRemBrandSecondExample = new MaterialsRemBrandSecondExample();
//        materialsRemBrandSecondExample.createCriteria().andSbbmEqualTo(sbbm);
        return this.interceptCategoryCode(materialsRemBrandSecondMapper.selectBySbbm(sbbm));
    }

    @Override
    public List<DealerBrandInfo> getDealerBrandList(String companyId) {

        DealerBrandInfoExample dealerBrandInfoExample = new DealerBrandInfoExample();

        dealerBrandInfoExample.createCriteria().andCompanyIdEqualTo(companyId)
                .andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code);
        dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);

        return dealerBrandInfoMapper.selectByContract(companyId);
    }

    @Override
    public List<DealerCategory> getDealerBrandSecondList(Integer id) {


        DealerBrandInfoExample dealerBrandInfoExample = new DealerBrandInfoExample();

        dealerBrandInfoExample.createCriteria().andIdEqualTo(id);
        List<DealerBrandInfo> list = dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);
        if (list.size() > 0) {
            DealerBrandInfo dealerBrandInfo = list.get(0);
            dealerBrandInfoExample.clear();
            dealerBrandInfoExample.createCriteria().andCompanyIdEqualTo(dealerBrandInfo.getCompanyId())
                    .andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code);
            List<DealerBrandInfo> list1 = dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);
            DealerCategoryExample dealerCategoryExample = new DealerCategoryExample();
            dealerCategoryExample.createCriteria().andIdIn(list1.stream().map(DealerBrandInfo::getId).collect(Collectors.toList()));
            return dealerCategoryMapper.selectByExample(dealerCategoryExample);
        }
        return Collections.emptyList();
//        DealerBrandInfoExample dealerBrandInfoExample = new DealerBrandInfoExample();
//        DealerBrandInfoExample.Criteria criteria = dealerBrandInfoExample.createCriteria();
//
//        criteria.andCompanyIdEqualTo(companyId);
//        criteria.andAuditStatusEqualTo(BrandConstants.AuditStatus.AUDITSUCCESS.code);
//        criteria.andBrandNoEqualTo(brandNo);

//        return dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);
        }

    /**
     * 截取品类编码从第四位开始
     * @param materialsRemBrandSeconds
     * @return
     */
    private List<MaterialsRemBrandSecond> interceptCategoryCode(List<MaterialsRemBrandSecond> materialsRemBrandSeconds) {

        if (materialsRemBrandSeconds != null && materialsRemBrandSeconds.size() > 0) {

            materialsRemBrandSeconds.forEach(e->{
                if (e.getSpfldm2().length() >MinValue) {
                    String intercept = e.getSpfldm2().substring(MinValue);
                    e.setSpfldm2(intercept);
                }
            });
        }

    return materialsRemBrandSeconds;
    }
}
