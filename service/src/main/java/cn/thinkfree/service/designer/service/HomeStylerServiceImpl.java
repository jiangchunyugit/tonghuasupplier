package cn.thinkfree.service.designer.service;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.mapper.PreProjectInfoMapper;
import cn.thinkfree.database.model.PreProjectInfo;
import cn.thinkfree.database.model.PreProjectInfoExample;
import cn.thinkfree.service.designer.vo.HomeStyler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HomeStylerServiceImpl extends AbsLogPrinter implements HomeStylerService {


    @Value("${custom.design.caseIDUrl}")
    String getCaseIDUrl;
    @Value("${custom.design.homeStylerUrl}")
    String getHomeStylerUrl;
    @Value("${custom.design.templateUrl}")
    String templateUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PreProjectInfoMapper preProjectInfoMapper;

    /**
     * 获取CaseID
     *
     * @param projectNo
     * @return
     */
    @Override
    public String findCaseID(String projectNo) {
        String url = getCaseIDUrl+projectNo;
        String obj = restTemplate.getForObject(url, String.class);
        return obj;
    }


    /**
     * 保存家居数据
     *
     * @param caseID
     * @return
     */
    @Override
    public String saveHomeStyler(String caseID) {
        HomeStyler homeStyler = restTemplate.getForObject(getHomeStylerUrl + caseID, HomeStyler.class);
        System.out.println(homeStyler);

        return null;
    }

    /**
     * 根据项目主键查询家居数据
     *
     * @param projectNo
     * @return
     */
    @Override
    public HomeStyler findDataByProjectNo(String projectNo) {

        if(StringUtils.isBlank(projectNo)){
            return new HomeStyler();
        }
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectInfo> preProjectInfos = preProjectInfoMapper.selectByExample(preProjectInfoExample);

        if(preProjectInfos.isEmpty() || preProjectInfos.size() > 1){
            return new HomeStyler();
        }
        PreProjectInfo preProjectInfo = preProjectInfos.get(0);
        String filePackage = preProjectInfo.getFilePackage();
        if(StringUtils.isBlank(filePackage)){
            return new HomeStyler();
        }
        try {
            HomeStyler homeStyler = restTemplate.getForObject(getHomeStylerUrl + convertUrl(filePackage), HomeStyler.class);
            return homeStyler;
        }catch (Exception e){
            printErrorMes(e.getMessage());
            return new HomeStyler();
        }
    }

    private String convertUrl(String url){
//        url ="https://3d.homestyler.com/cn/?assetId=3a477ee0-5210-4c26-9707-0df911104a87";
        return url.replace(templateUrl,"");
    }
}
