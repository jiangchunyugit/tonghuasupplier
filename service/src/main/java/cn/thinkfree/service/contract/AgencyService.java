package cn.thinkfree.service.contract;

import cn.thinkfree.database.model.AgencyContract;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import com.github.pagehelper.PageInfo;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商合同
 */
public interface AgencyService {

    /**
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<AgencyContract> pageContractBySEO(AgencySEO gencySEO);

    /**
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<AgencyContract> selectoperatingPageList(AgencySEO gencySEO);

    /**
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<AgencyContract> selectFinancialPageList(AgencySEO gencySEO);

    /**
     * 分页查询 经销商合同（小b）
     * @param gencySEO
     * @return
     */
    PageInfo<AgencyContract> selectBPageList(AgencySEO gencySEO);

    /**
     * 新增经销商 合同信息
     * @param paramAgencyList
     * @return
     */
    boolean insertContract(ParamAgencySEO paramAgencyList);

    /**
     * 校验品牌品类，门店，摊位重复。
     * @param paramAgencyList
     * @return
     */
    String checkRepeat(ParamAgencySEO paramAgencyList);

    /**
     * 变更，续签 合同信息
     * @param paramAgencySEO
     * @return
     */
    boolean changeContract(ParamAgencySEO paramAgencySEO);

    /**
     * 审核 合同 顺序调启审核
     * @param contractNumber
     * @param status
     * @param auditStatus
     * @param cause
     * @return
     */
    boolean auditContract(String contractNumber, String status,String auditStatus, String cause);

    /**
     * 冻结 9  解冻 8 作废11
     * @param companyId
     * @param contractNumber
     * @param status
     * @return
     */
    boolean updateContractStatus(String companyId ,String contractNumber,String status);

    /**
     * 根据合同编号查询合同
     * @param contractNumber
     * @return
     */
    ParamAgency getParamAgency(String contractNumber);

    /**
     * 创建ftp
     * @param contractNumber
     * @return
     */
     String createPdf(String contractNumber);

    /**
     * 返回审批原因
     * @param contruct
     * @param status
     * @return
     */
     PcAuditInfo checkCase(String contruct, String status);
}
