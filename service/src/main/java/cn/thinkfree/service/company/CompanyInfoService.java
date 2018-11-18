package cn.thinkfree.service.company;

import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Optional;

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

    /**
     * 获取公司信息根据公司名
     * @param name
     * @return
     */
    List<SelectItem> listCompanyByLikeName(String name);

    /**
     * 根据公司编号获取同步数据
     * @param companyID
     * @return
     */
    Optional<SyncTransactionVO> selectSyncDateByCompanyID(String companyID);

    /**
     * 根据公司类型查询公司列表
     * @param roleId
     * @return
     */
    List<CompanyInfo> companyInfoByRole(String roleId);
}
