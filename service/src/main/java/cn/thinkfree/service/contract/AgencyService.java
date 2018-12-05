package cn.thinkfree.service.contract;

import cn.thinkfree.database.model.AgencyContract;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.vo.ContractClauseVO;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.paramAgency;
import cn.thinkfree.database.vo.contract.ContractCostVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AgencyService {


    /***
     * 分页查询 经销商合同（运营）
     * @param gencySEO
     * @return
     */
    PageInfo<MyAgencyContract> pageContractBySEO(AgencySEO gencySEO);

    /**
     * 新增经销商 合同信息
     * @param paramAgencyList
     * @return
     */
    boolean insertContract(List<paramAgency> paramAgencyList);


}
