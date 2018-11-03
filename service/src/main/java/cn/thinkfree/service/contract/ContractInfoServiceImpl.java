package cn.thinkfree.service.contract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ContractTermsChildMapper;
import cn.thinkfree.database.mapper.ContractTermsMapper;
import cn.thinkfree.database.mapper.MyContractInfoMapper;
import cn.thinkfree.database.mapper.OrderContractMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.ContractTerms;
import cn.thinkfree.database.model.ContractTermsChild;
import cn.thinkfree.database.model.ContractTermsChildExample;
import cn.thinkfree.database.model.ContractTermsExample;
import cn.thinkfree.database.model.OrderContract;
import cn.thinkfree.database.model.OrderContractExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.model.PcCompanyFinancial;
import cn.thinkfree.database.model.PcCompanyFinancialExample;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.database.vo.ContractClauseVO;
import cn.thinkfree.database.vo.ContractDetails;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.CompanyAuditStatus;
import cn.thinkfree.service.constants.ContractStatus;
import cn.thinkfree.service.utils.CommonGroupUtils;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
import cn.thinkfree.service.utils.FreemarkerUtils;
import cn.thinkfree.service.utils.WordUtil;

@Service
public class ContractInfoServiceImpl extends AbsLogPrinter implements ContractService {

	@Autowired
	MyContractInfoMapper contractInfoMapper;

	@Autowired
	CompanyInfoMapper companyInfoMapper;

	@Autowired
	PcAuditInfoMapper pcAuditInfoMapper;

	@Autowired
	PcCompanyFinancialMapper pcCompanyFinancialMapper;

	@Autowired
	ContractTermsMapper pcContractTermsMapper;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private OrderContractMapper orderContractMapper;

	//发送邮件的模板引擎
	@Autowired
	private FreeMarkerConfigurer configurer;

	@Autowired
	CompanySubmitService companySubmitService;
	
	@Autowired
	ContractTermsChildMapper  contractTermsChildMapper;


	@Override
	public PageInfo<ContractVo> pageContractBySEO(ContractSEO contractSEO) {
		PageHelper.startPage(contractSEO.getPage(),contractSEO.getRows());
		List<ContractVo>  list =  contractInfoMapper.selectContractPage(contractSEO);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setContractStatus(ContractStatus.getDesc(list.get(i).getContractStatus()));
		}
		return new PageInfo<>(list);
	}




	@Override
	public void exportList(ContractSEO contractSEO,HttpServletResponse response) {
		ExcelData data = new ExcelData();
		String title = "合同信息数据";
		data.setName(title);
		//添加表头
		List<String> titles = new ArrayList<>();
		titles.add("签约日期");
		titles.add("合同编码");
		titles.add("公司编码");
		titles.add("公司名称");
		titles.add("公司类型");
		titles.add("所在地");
		titles.add("保证金");
		titles.add("合同状态");
		data.setTitles(titles);
		//添加列
		List<List<Object>> rows = new ArrayList<>();
		List<Object> row = null;
		PageHelper.startPage(contractSEO.getPage(),contractSEO.getRows());
		List<ContractVo>  list =  contractInfoMapper.selectContractPage(contractSEO);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setContractStatus(ContractStatus.getDesc(list.get(i).getContractStatus()));
		}
		for(int i=0; i<list.size();i++){
			row=new ArrayList<>();
			row.add(list.get(i).getSignedTime());
			row.add(list.get(i).getContractNumber());
			row.add(list.get(i).getCompanyId());
			row.add(list.get(i).getCompanyName());
			row.add(list.get(i).getCompanyType());
			row.add(list.get(i).getCompanyLocation());
			row.add(list.get(i).getDepositMoney());
			row.add(list.get(i).getContractStatus());
			rows.add(row);
		}
		data.setRows(rows);
		SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		String fileName=title+"_"+fdate.format(new Date())+".xls";
		try {
			ExcelUtils.exportExcel(response, fileName, data);
		} catch (Exception e) {
			printErrorMes("合同导出异常",e.getMessage());
		}
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
		}if((!StringUtils.isEmpty(auditCase) && auditStatus.equals("1"))){
			map.put("code", "1");
			map.put("msg", "清填写审核不通过原因");
			return  map;
		}

		//修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝
		ContractVo vo = new ContractVo();
		vo.setCompanyId(companyId);
		vo.setContractNumber(contractNumber);
		if(auditStatus.equals(AuditStatus.AuditPass.shortVal()) ){//
			vo.setContractStatus(ContractStatus.AuditPass.shortVal());
		}else{
			vo.setContractStatus(ContractStatus.AuditDecline.shortVal());
		}
//		ContractInfo ss = new ContractInfo();
//		ss.setCompanyId("测试");
		//applicationContext.publishEvent(new AuditEvent(ss));
		//修改公司表
		int flag = contractInfoMapper.updateContractStatus(vo);
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyId(companyId);
		if(auditStatus.equals(AuditStatus.AuditPass.shortVal())){//财务审核通过
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

		int flagon = pcAuditInfoMapper.insertSelective(record);

		if(flag > 0 && flagT > 0 &&  flagon > 0 ){
			map.put("code", "0");
			map.put("msg", "审核成功");
			return map;
		}else{
			map.put("code", "1");
			map.put("msg", "审核失败");
			return map;
		}
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
		return contractInfoMapper.selectContractBycontractNumber(vo)==null?"":
				contractInfoMapper.selectContractBycontractNumber(vo).getContractUrl();
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
				WordUtil.createWord(configurer,root, "/sj_ftl.xml", "", secondCompanyName+"_"+sdf.format(new Date())+"入住合作合同.doc");

			}else if(companyInfo.getRoleId().equals("SJ")){//设计公司
				//自定义魔板中的信息
				reportresult.add(rep);
				root.put("reportresult", reportresult);
				WordUtil.createWord(configurer,root, "/sj_ftl.xml", "http://localhost:7181/static/", secondCompanyName+"_"+sdf.format(new Date())+"入住合作合同.doc");
			}

		}else{
			map.put("code", "1");
			map.put("msg", "操作失败");
		}

		return map;
	}

	@Transactional
	@Override
	public boolean insertContractClause(String contractNumber,String companyId,ContractClauseVO contractClausevo) {

		Map<String,String> resMap = new HashMap<>();
		//List<PcContractTerms> list = new ArrayList<>();
		if(contractClausevo.getParamMap() != null){
			Iterator<Map.Entry<String, String>> entries = contractClausevo.getParamMap().entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				String key = entry.getKey();
				String value = entry.getValue();
				ContractTerms terms = new ContractTerms();
				terms.setCompanyId(companyId);
				terms.setContractNumber(contractNumber);
				terms.setCreateTime(new Date());
				terms.setUpdateTime(new Date());
				terms.setContractDictCode(key);
				terms.setContractValue(value);
				//list.add(terms);
				ContractTermsExample exp = new ContractTermsExample();
				exp.createCriteria().andCompanyIdEqualTo(companyId).andContractDictCodeEqualTo(key).andContractNumberEqualTo(contractNumber);
				pcContractTermsMapper.deleteByExample(exp);
				pcContractTermsMapper.insertSelective(terms);
			}
		}
		//设置结算规则
		if(contractClausevo.getChildparamMap() != null){
			Iterator<Entry<String, List<ContractTermsChild>>> childentries = contractClausevo.getChildparamMap().entrySet().iterator();
			while (childentries.hasNext()) {
				Entry<String, List<ContractTermsChild>> entry = childentries.next();
				String key = entry.getKey();
			    List<ContractTermsChild> list = entry.getValue();
			    for (int i = 0; i < list.size(); i++) {
			    	ContractTermsChild child = list.get(i);
			    	ContractTermsChildExample exp = new ContractTermsChildExample();
			    	exp.createCriteria().andCompanyIdEqualTo(companyId).andCostTypeEqualTo(key).andContractNumberEqualTo(contractNumber);
			    	contractTermsChildMapper.deleteByExample(exp);//删除重复数据
			    	contractTermsChildMapper.insertSelective(child);//新增合同规则
				}
			}
		}
		return true;
	}

	@Override
	public Map<String, String> getContractBycontractNumber(String contractNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getContractDetailInfo(String contractNumber, String companyId) {

		Map<String, Object> reMap = new HashMap<>();
		// 公司详情
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo(companyId);

		// 合同详情
		ContractTermsExample exp = new ContractTermsExample();
		//判断公司类型
		exp.createCriteria().andCompanyIdEqualTo(companyId).
				andContractNumberEqualTo(contractNumber);

		List<ContractTerms> list = pcContractTermsMapper.selectByExample(exp);

		if(list.size() == 0 && companyInfo != null){
			ContractTerms term_0 = new ContractTerms("01","居然设计家");
			list.add(term_0);
		}
		
		
		
		//定义结算规则
		Map<String,Map<String,List<ContractTermsChild>>> map =new HashMap<>();
		//查询结算规则
		ContractTermsChildExample example = new ContractTermsChildExample();
		example.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber);
		List<ContractTermsChild> childList = contractTermsChildMapper.selectByExample(example);
		Map<Long, List<ContractTermsChild>> map2 = new LinkedHashMap<Long, List<ContractTermsChild>>();
		CommonGroupUtils.listGroup2Map(childList, map2, ContractTermsChild.class, "cost_type");//根据类型分组
		
		if(childList != null){
			Map<String,List<ContractTermsChild>> ma = new HashMap<>();
			for (int i = 0; i < childList.size(); i++) {
				ContractTermsChildExample example1 = new ContractTermsChildExample();
				example1.createCriteria().andCompanyIdEqualTo(companyId)
						.andContractNumberEqualTo(contractNumber)
						.andCostTypeEqualTo(childList.get(i).getCostType());
				List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample(example1);
				ma.put(childList.get(i).getCostType(), childListr);
			}
			reMap.put("ContractChild", ma);
		}
		
		//查询合同设置项目
		reMap.put("companyMap", companyInfo==null?"":companyInfo);
		reMap.put("ContractList", list);
		
		return reMap;
	}




	@Override
	public boolean createOrderContract(String companyId, String orderNumber, String type) {
		String url = "";
		String contractNumber = "HT"+getOrderContract();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("test", "测试");
			FreemarkerUtils.savePdf(contractNumber, type, map);

		} catch (Exception e) {
			printErrorMes("生成订单合同 系统错误{}",e.getMessage());
			return false;
		}
		OrderContract record = new OrderContract();
		record.setContractNumber(contractNumber);
		record.setCompanyId(companyId);
		record.setSignTime(new Date());
		record.setContractType(type);
		record.setCteateTime(new Date());
		record.setUpdateTime(new Date());
		record.setOrderNumber(orderNumber);
		record.setConractUrlPdf(url);
		int flag = orderContractMapper.insertSelective(record);

		if(flag > 0 ){
			return true;
		}
		return false;
	}




	@Override
	public String getPdfUrlByOrderNumber(String orderNumber) {
		OrderContractExample example = new OrderContractExample();
		example.createCriteria().andOrderNumberEqualTo(orderNumber);
		List<OrderContract>  list = orderContractMapper.selectByExample(example);
		return (list !=null && list.size() > 0)?list.get(0).getConractUrlPdf():"";
	}


	/**
	 * 获取6-10 的随机位数数字
	 * @param length	想要生成的长度
	 * @return result
	 */
	public synchronized static String getOrderContract() {

		Random random = new Random();
		DecimalFormat df = new DecimalFormat("00");
		String no = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + df.format(random.nextInt(100));
		return no;
	}



}
