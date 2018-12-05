package cn.thinkfree.service.contract;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.constants.SyncOrderEnum;
import cn.thinkfree.database.event.MarginContractEvent;
import cn.thinkfree.database.event.sync.CreateOrder;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.paramAgency;
import cn.thinkfree.database.vo.contract.ContractCostVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.service.branchcompany.BranchCompanyService;
import cn.thinkfree.service.companyapply.CompanyApplyService;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import cn.thinkfree.service.constants.*;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.pcthirdpartdate.ThirdPartDateService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AgencyServiceImpl extends AbsLogPrinter implements AgencyService {

    @Autowired
    AgencyContractMapper agencyContractMapper;

    @Autowired
    AgencyContractTermsMapper agencyContractTermsMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;



    @Override
    public PageInfo<MyAgencyContract> pageContractBySEO(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );

        List<MyAgencyContract> list = agencyContractMapper.selectPageList(gencySEO);

        return  (new PageInfo<>( list ) );
    }

    @Override
    @Transactional
    public boolean insertContract(List<paramAgency> paramAgencyList) {
        boolean  flag = false;
        if(paramAgencyList != null  && paramAgencyList.size() > 0 ){
            try {
                paramAgencyList.forEach(a ->{
                    agencyContractMapper.insertSelective(a.getAgencyContract());
                    agencyContractTermsMapper.insertSelective(a.getAgencyContractTerms());
                });
            } catch (Exception e) {
                e.printStackTrace();
                printErrorMes("插入仅销售合同异常"+e.getMessage());
                throw new RuntimeException("插入仅销售合同异常");
            }
            flag = true;
        }
        return flag;
    }








    /**
     * 把公司信息初始化到map
     */
    public Map<String,String>  getDicCompanyName(){
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andRoleIdEqualTo( CompanyType.SJ.stringVal());
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        Map<String, String> maps = list.stream().collect(Collectors.toMap(CompanyInfo::getCompanyId,CompanyInfo::getCompanyName));
       return maps;
    }



}
