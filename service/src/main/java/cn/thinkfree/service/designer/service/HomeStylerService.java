package cn.thinkfree.service.designer.service;

import cn.thinkfree.service.designer.vo.HomeStyler;

public interface HomeStylerService {

    /**
     * 获取CaseID
     * @param projectNo
     * @return
     */
    String findCaseID(String projectNo);

    /**
     * 保存家居数据
     * @param caseID
     * @return
     */
    String saveHomeStyler(String caseID);

    /**
     * 根据项目主键查询家居数据
     * @param projectNo
     * @return
     */
    HomeStyler findDataByProjectNo(String projectNo);



}

