package cn.thinkfree.service.designer.service;

import cn.thinkfree.service.designer.vo.HomeStyler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HomeStylerServiceImpl implements HomeStylerService {


    @Value("${custom.design.caseIDUrl}")
    String getCaseIDUrl;
    @Value("${custom.design.homeStylerUrl}")
    String getHomeStylerUrl;
    @Value("${custom.design.templateUrl}")
    String templateUrl;

    @Autowired
    RestTemplate restTemplate;

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
//        String url = getCaseIDUrl+projectNo;
//        String caseID = restTemplate.getForObject(url, String.class);
        HomeStyler homeStyler = restTemplate.getForObject(getHomeStylerUrl + projectNo, HomeStyler.class);
        return homeStyler;
    }

    private String convertUrl(String url){
        url ="https://3d.homestyler.com/cn/?assetId=3a477ee0-5210-4c26-9707-0df911104a87";
        return url.replace(templateUrl,"");
    }
}
