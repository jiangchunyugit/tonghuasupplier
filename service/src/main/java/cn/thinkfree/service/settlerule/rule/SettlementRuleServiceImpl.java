package cn.thinkfree.service.settlerule.rule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.MyContractInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.mapper.ContractTermsMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.ContractTerms;
import cn.thinkfree.database.model.ContractTermsExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.model.PcCompanyFinancial;
import cn.thinkfree.database.model.PcCompanyFinancialExample;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.ContractDetails;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.CompanyAuditStatus;
import cn.thinkfree.service.constants.ContractStatus;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
import cn.thinkfree.service.utils.WordUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Service
public class SettlementRuleServiceImpl extends AbsLogPrinter implements SettlementRuleService {

	
}
