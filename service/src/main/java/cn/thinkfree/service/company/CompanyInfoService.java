package cn.thinkfree.service.company;

import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.vo.CompanyInfoSEO;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.StaffsVO;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyInfoService {
    List<CompanyInfo> selectByCompany(UserVO userVO);

    /**
     * 添加公司
     */
    int addCompanyInfo(CompanyInfo companyInfo);

    /**
     * 修改公司信息
     */
    int updateCompanyInfo(CompanyInfo companyInfo);
    /**
     * 查询公司信息
     */
    PageInfo<CompanyInfo> list(CompanyInfoSEO companyInfoSEO);

    /**
     * 员工信息
     */
    PageInfo<StaffsVO> staffMessage(String companyId, Integer page, Integer rows);


    /**
     * 根据公司id查询公司详情
     * @param companyId
     * @return
     */
    CompanyInfoVo companyDetails(String companyId);
}
