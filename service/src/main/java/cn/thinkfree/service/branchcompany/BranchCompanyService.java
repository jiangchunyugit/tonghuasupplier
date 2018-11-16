package cn.thinkfree.service.branchcompany;

import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.BranchCompanyExample;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分公司（省分站）接口
 */
public interface BranchCompanyService {

    /**
     * 添加分公司
     * @param branchCompany
     * @return
     */
    int addBranchCompany(BranchCompany branchCompany);

    /**
     * 修改分公司信息
     * @param branchCompany
     * @return
     */
    int updateBranchCompany(BranchCompany branchCompany);

    /**
     * 查询分公司信息(分页)
     */
    PageInfo<BranchCompanyVO> branchCompanyList(BranchCompanySEO branchCompanySEO);

    /**
     * 根据分公司id查询公司详情(带城市分站)
     * @param Id
     * @return
     */
    BranchCompanyVO branchCompanyDetails(Integer Id);

    /**
     * 分公司list（不带城市分站）
     * @return
     */
    List<BranchCompany> branchCompanys(Integer flag);

    /**
     * 分公司信息（不带城市分站）
     * @param id
     * @return
     */
    BranchCompany branchCompanyById(Integer id);

    /**
     * 分公司和其所属城市分站信息
     * @return
     */
    List<CompanyRelationVO> companyRelationList();

    /**
     * 权限站点信息
     * @return
     */
    SiteInfo getSiteInfo();

    /**
     * 通过id范围查询分公司信息
     * @param provinceCode
     * @return
     */
    List<BranchCompany> getBranchCompanyByIdList(String provinceCode,String cityCode);

    /**
     * 根据用户查询用户组织架构
     * @return
     */
    List<CompanyInfo> getCompanyOrganizationByUser(String userId);

    /**
     * 根据入驻公司名称获取组织架构
     * @param companyId
     * @return
     */
    EnterCompanyOrganizationVO getCompanyOrganizationByCompanyId(String companyId);
}
