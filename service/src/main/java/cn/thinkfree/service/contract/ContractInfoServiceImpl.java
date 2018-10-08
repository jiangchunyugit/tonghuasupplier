package cn.thinkfree.service.contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ContractInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.mapper.PcContractTermsMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.model.PcCompanyFinancial;
import cn.thinkfree.database.model.PcCompanyFinancialExample;
import cn.thinkfree.database.model.PcContractTerms;
import cn.thinkfree.database.model.PcContractTermsExample;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.ContractDetails;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.CompanyAuditStatus;
import cn.thinkfree.service.constants.ContractStatus;
import cn.thinkfree.service.utils.WordUtil;

@Service
public class ContractInfoServiceImpl implements ContractService {

	@Autowired
	ContractInfoMapper contractInfoMapper;
	
	@Autowired
	CompanyInfoMapper companyInfoMapper;
	
	@Autowired
	PcAuditInfoMapper pcAuditInfoMapper;
	
	@Autowired
	PcCompanyFinancialMapper pcCompanyFinancialMapper;
	
	@Autowired
	PcContractTermsMapper pcContractTermsMapper;
	
	

	
	
	@Override
	public PageInfo<ContractVo> pageContractBySEO(ContractSEO contractSEO) {
		PageHelper.startPage(contractSEO.getPage(),contractSEO.getRows());
		List<ContractVo>  list =  contractInfoMapper.selectContractMap(contractSEO);
	   return new PageInfo<>(list);
	}
    
	/**
	 * 
	 * 财务审核的时候要 
	 * 修改公司表的状态
	 * 修改合同表  
	 * 添加审核记录表 
	 * @author lvqidong
	 * 公司入驻状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金 6入驻成功
	 */
	@Override
	@Transactional
	public Map<String,String>  auditContract(String contractNumber, String companyId,
			String auditStatus,String auditCase) {
		
		Map<String,String> map = new HashMap<>();
			
		if(StringUtils.isEmpty(contractNumber)){
			map.put("code", "1");
			map.put("msg", "合同编号为空");
			return  map;
		}if(StringUtils.isEmpty(companyId)){
			map.put("code", "1");
			map.put("msg", "公司编号为空");
			return  map;
		}if(StringUtils.isEmpty(auditStatus)){
			map.put("code", "1");
			map.put("msg", "审核状态为空");
			return  map;
		}if(!StringUtils.isEmpty(auditStatus) && auditCase.equals("1") && StringUtils.isEmpty(auditCase)){
			map.put("code", "1");
			map.put("msg", "清填写审核不通过原因");
			return  map;
		}
		
		//修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝
		ContractVo vo = new ContractVo();
		vo.setCompanyId(companyId);
		vo.setContractNumber(contractNumber);
		if(auditCase.equals(AuditStatus.AuditPass.shortVal()) ){//
			vo.setContractStatus(ContractStatus.AuditPass.shortVal());
		}else{
			vo.setContractStatus(ContractStatus.AuditDecline.shortVal());
		}
		
		//修改公司表 
		int flag = contractInfoMapper.updateContractStatus(vo);
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyId(companyId);
		if(auditCase.equals(AuditStatus.AuditPass.shortVal())){//财务审核通过
			companyInfo.setAuditStatus(CompanyAuditStatus.SUCCESSCHECK.stringVal());
		}else{//财务审核不通过
			companyInfo.setAuditStatus(CompanyAuditStatus.FAILCHECK.stringVal());
		}
		int flagT = companyInfoMapper.updateauditStatus(companyInfo);
		
		UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
		String auditPersion = userVO ==null?"":userVO.getUsername();
		//添加审核记录表
		PcAuditInfo record = new PcAuditInfo("1", "2", auditPersion, auditStatus, new Date(),
				companyId, auditCase, contractNumber);
		
		int flagi = pcAuditInfoMapper.insertSelective(record);
	    
		if(flag > 0 && flagT > 0 &&  flagi  > 0 ){
			
			map.put("code", "0");
			map.put("msg", "审核成功");
			
		}else{
			map.put("code", "1");
			map.put("msg", "审核失败");
		}
		return map;
	}
	
	
	
	

	@Override
	public  Map<String,String> ackEarnestMoney(String contractNumber, String companyId) {
		
		Map<String,String> map = new HashMap<>();
		//修改公司表 
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyId(companyId);
	    companyInfo.setAuditStatus("6");//确认已交保证金
		int flag = companyInfoMapper.updateauditStatus(companyInfo);
         if(flag > 0){
			
			map.put("code", "0");
			map.put("msg", "操作成功");
			
		}else{
			map.put("code", "1");
			map.put("msg", "操作失败");
		}
		return map;
	}

	@Override
	public ContractDetails contractDetails(String contractNumber, String companyId) {
		
		//查询公司
		ContractDetails companyVo = companyInfoMapper.selectCompanyDetails(companyId);
		if(companyVo != null ){//查詢合同信息
			ContractVo contractVo = new ContractVo();
			contractVo.setContractNumber(contractNumber);
			ContractVo con = contractInfoMapper.selectContractBycontractNumber(contractVo);
			companyVo.setSignedTime(con.getSignedTime());
			companyVo.setStartEime(con.getStartTime());
			companyVo.setAuditName(CompanyAuditStatus.getDesc(Integer.valueOf(companyVo.getAuditStatus())));//审核状态
			companyVo.setSignedTime(con.getSignedTime());//签约时间
			//发票信息
			PcCompanyFinancialExample example = new PcCompanyFinancialExample();
			example.createCriteria().andCompanyIdEqualTo(companyId);
			List<PcCompanyFinancial>  list = pcCompanyFinancialMapper.selectByExample(example);
			companyVo.setPcCompanyFinancial(list.get(0));
			//审核信息
			PcAuditInfoExample auexample = new PcAuditInfoExample();
			auexample.createCriteria().andContractNumberEqualTo(contractNumber);
			auexample.setOrderByClause("audit_time");
			List<PcAuditInfo> auditList = pcAuditInfoMapper.selectByExample(auexample);
			companyVo.setAuditInfo(auditList);
			//合同信息
		}
		
		return companyVo;
	}

	@Override
	public String selectContractBycontractNumber(String contractNumber) {
		ContractVo  vo = new ContractVo();
		vo.setContractNumber(contractNumber);
		return contractInfoMapper.selectContractBycontractNumber(vo).getContractUrl();
	}

	@Override
	public Map<String, String> createContractDoc(String contractNumber) {
		
		Map<String,String> map = new HashMap<>();
		ContractVo  vo  = new ContractVo();
		vo.setContractNumber(contractNumber);
		ContractVo newVo = contractInfoMapper.selectContractBycontractNumber(vo);
		if(newVo != null  ){
			//获取公司信息 
			CompanyInfoVo  companyInfo  = companyInfoMapper.selectByCompanyId(newVo.getCompanyId());
			String ownerCompanyName = "_____居然之家_____";//甲方公司名称
			String secondCompanyName = "_____"+companyInfo.getCompanyName()+"_____";//乙方公司名称
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formatstartYear[] = null;
			String formatendYear[] = null;
			try {
				formatstartYear = sdf.format(format1.parse(newVo.getStartTime())).split("-");
				formatendYear = sdf.format(format1.parse(newVo.getEndTime())).split("-");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String startYear =  formatstartYear[0];//合同开始年
			String startmonth = formatstartYear[1];//合同开始年
			String startsun =   formatstartYear[2] ;//合同开始年
			String endYear =    formatendYear[0];//合同结束年
			String endtmonth  = formatendYear[1] ;//合同结束月
			String endsun =  formatendYear[2]  ;//合同结束日
			//开户户名
			PcCompanyFinancial accountinfo = pcCompanyFinancialMapper.findPcCompanyFinancialByCompanyId(newVo.getCompanyId());
			String cardName = accountinfo.getCardName();//开户行名称
			String accounBranchName = accountinfo.getAccountBranchName();//开户银行名称
			String accountNumber = accountinfo.getAccountNumber()+"";//银行卡卡号
			
			Map<String,List<Map<String,String>>> root=new HashMap<String,List<Map<String,String>>> ();//data数据
	        List<Map<String,String>> reportresult =new ArrayList<Map<String,String>>();
	        Map<String,String> rep=new HashMap<String,String> ();
	        rep.put("ownerCompanyName", ownerCompanyName);
	        rep.put("secondCompanyName", secondCompanyName);
	        rep.put("startYear", startYear);
	        rep.put("startmonth", startmonth);
	        rep.put("startsun", startsun);
	        rep.put("endYear", endYear);
	        rep.put("endtmonth", endtmonth);
	        rep.put("endsun", endsun);
	        rep.put("cardName", cardName);
	        rep.put("accounBranchName", accounBranchName);
	        rep.put("accountNumber", accountNumber);//銀行卡號
	        
			//判断公司类型
			if(companyInfo.getRoleId().equals("BD")){//装修公司
				//自定义魔板中的信息
				
				//自定义魔板中的信息
		        reportresult.add(rep);
		        root.put("reportresult", reportresult);
				WordUtil.createWord(root, "sj_ftl.xml", "", secondCompanyName+"_"+sdf.format(new Date())+"入住合作合同.doc");
				
			}else if(companyInfo.getRoleId().equals("SJ")){//设计公司
				//自定义魔板中的信息
		        reportresult.add(rep);
		        root.put("reportresult", reportresult);
				WordUtil.createWord(root, "sj_ftl.xml", "http://localhost:7181/static/", secondCompanyName+"_"+sdf.format(new Date())+"入住合作合同.doc");
			}
			
		}else{
			map.put("code", "1");
			map.put("msg", "操作失败");
		}
		
		return map;
	}
    
	@Transactional
	@Override
	public Map<String, String> insertContractClause(String contractNumber,String companyId,Map<String,String> map) {
		
		Map<String,String> resMap = new HashMap<>();
		//List<PcContractTerms> list = new ArrayList<>();
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator(); 
		while (entries.hasNext()) { 
		  Map.Entry<String, String> entry = entries.next(); 
		  String key = entry.getKey();
		  String value = entry.getValue();
		  PcContractTerms terms = new PcContractTerms();
		  terms.setCompanyId(companyId);
		  terms.setContractNumber(contractNumber);
		  terms.setCreateTime(new Date());
		  terms.setUpdateTime(new Date());
		  terms.setContractDictCode(key);
		  terms.setContractValue(value);
		  //list.add(terms);
		  PcContractTermsExample exp = new PcContractTermsExample();
		  exp.createCriteria().andCompanyIdEqualTo(companyId).andContractDictCodeEqualTo(key).andContractNumberEqualTo(contractNumber);
		  pcContractTermsMapper.deleteByExample(exp);
		  pcContractTermsMapper.insertSelective(terms);
		}
		resMap.put("code", "0");
		resMap.put("msg", "设置成功");
		return resMap;
	}

	@Override
	public Map<String, String> getContractBycontractNumber(String contractNumber) {
		// TODO Auto-generated method stub
		return null;
	}

   
}
