package cn.thinkfree.service.ebsmoke;

import cn.thinkfree.database.mapper.BusinessEntityMapper;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.EbsMokeBranchCompany;
import cn.thinkfree.database.vo.ebsmokevo.EbsCityBranch;
import cn.thinkfree.database.vo.ebsmokevo.StoreBusinessEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EbsServiceImpl implements EbsService {

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    CityMapper cityMapper;

    @Autowired
    BusinessEntityMapper businessEntityMapper;

    @Override
    public List<EbsMokeBranchCompany> ebsBranchCompanyList() {

        Map<Integer,String> ebsBranchCompanyMap = new HashMap<>();
        ebsBranchCompanyMap.put(11,"北京分公司");
        ebsBranchCompanyMap.put(12,"天津分公司");
        ebsBranchCompanyMap.put(13,"河北分公司");
        ebsBranchCompanyMap.put(14,"山西分公司");
        ebsBranchCompanyMap.put(21,"辽宁分公司");
        ebsBranchCompanyMap.put(22,"吉林分公司");
        ebsBranchCompanyMap.put(23,"黑龙江分公司");
        ebsBranchCompanyMap.put(31,"上海分公司");
        ebsBranchCompanyMap.put(32,"江苏分公司");
        ebsBranchCompanyMap.put(33,"浙江分公司");
        ebsBranchCompanyMap.put(34,"安徽分公司");
        ebsBranchCompanyMap.put(35,"福建分公司");
        ebsBranchCompanyMap.put(36,"江西分公司");
        ebsBranchCompanyMap.put(37,"山东分公司");
        ebsBranchCompanyMap.put(41,"河南分公司");
        ebsBranchCompanyMap.put(42,"湖北分公司");
        ebsBranchCompanyMap.put(43,"湖南分公司");
        ebsBranchCompanyMap.put(44,"广东分公司");
        ebsBranchCompanyMap.put(46,"海南分公司");
        ebsBranchCompanyMap.put(50,"重庆分公司");
        ebsBranchCompanyMap.put(51,"四川分公司");
        ebsBranchCompanyMap.put(52,"贵州分公司");
        ebsBranchCompanyMap.put(53,"云南分公司");
        ebsBranchCompanyMap.put(61,"陕西分公司");
        ebsBranchCompanyMap.put(62,"甘肃分公司");
        ebsBranchCompanyMap.put(63,"青海分公司");

        Iterator<Map.Entry<Integer,String>> it = ebsBranchCompanyMap.entrySet().iterator();

        List<EbsMokeBranchCompany> ebsMokeBranchCompanies= new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            EbsMokeBranchCompany ebsMokeBranchCompany = new EbsMokeBranchCompany();

            ebsMokeBranchCompany.setId(entry.getKey());
            ebsMokeBranchCompany.setNm(entry.getValue());
            ebsMokeBranchCompanies.add(ebsMokeBranchCompany);
        }

        return ebsMokeBranchCompanies;
    }

    @Override
    public List<EbsCityBranch> ebsCityBranchList(Integer id) {

        List<EbsCityBranch> ebsCityBranches = new ArrayList<>();
        CityExample cityExample = new CityExample();
        cityExample.createCriteria().andProvinceCodeEqualTo(String.valueOf(id));
        cityMapper.selectByExample(cityExample).forEach(e->{

            EbsCityBranch ebsCityBranch = new EbsCityBranch();
            ebsCityBranch.setId(Integer.valueOf(e.getCityCode()));
            ebsCityBranch.setName(e.getCityName().replace("市","站"));
            ebsCityBranches.add(ebsCityBranch);
        });

        return ebsCityBranches;
    }

    @Override
    public List<StoreBusinessEntity> storeBusinessEntityList(Integer id) {

        CityExample cityExample = new CityExample();
        cityExample.createCriteria().andCityCodeEqualTo(String.valueOf(id));

        BusinessEntityExample businessEntityExample = new BusinessEntityExample();
        businessEntityExample.createCriteria().andCityBranchEbsidEqualTo(id);

        List<BusinessEntity> businessEntities = businessEntityMapper.selectByExample(businessEntityExample);

        StoreBusinessEntity storeBusinessEntity = new StoreBusinessEntity();
        storeBusinessEntity.setStoreNm(cityMapper.selectByExample(cityExample).get(0).getCityName().replace("市","一分店"));
        storeBusinessEntity.setBusinessEntityList(businessEntities);

        List<StoreBusinessEntity> storeBusinessEntities = new ArrayList<>();
        storeBusinessEntities.add(storeBusinessEntity);
        return storeBusinessEntities;
    }
}
