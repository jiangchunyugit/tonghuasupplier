package cn.thinkfree.service.contract;

import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface AgencyService {


    /***
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<MyAgencyContract> pageContractBySEO(AgencySEO gencySEO);

    /***
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<MyAgencyContract> selectoperatingPageList(AgencySEO gencySEO);



    /***
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<MyAgencyContract> selectFinancialPageList(AgencySEO gencySEO);

    /**
     * 新增经销商 合同信息
     * @param paramAgencyList
     * @return
     */
    boolean insertContract(List<ParamAgencySEO> paramAgencyList);


    /**
     * 审核 合同
     *  顺序调启审核
     */
   boolean  auditContract(String companyId ,String contractNumber, String status,String auditStatus, String cause,String brandNo);


    /**
     * 冻结 0  解冻 1 作废2
     */
    boolean  updateContractStatus(String companyId ,String contractNumber,String status);

    /**
     * 根据合同编号查询合同
     *
     * 2018年12月5日 15:23:56
     */
    ParamAgency getParamAgency(String contractNumber);


    /**
     * 创建ftp
     */
     String  createPdf(String contractNumber);


    /***
     * 获取品牌列表 toDO （需要查徐阳的表）
     *
     */
    Map<String,String> getBrandNames();


    /***
     * 获取品牌列表 toDO （需要查徐阳的表）
     *
     */
    Map<String,String> getCategoryNames();


}
