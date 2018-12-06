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
import cn.thinkfree.database.vo.contract.ContractCostVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.service.branchcompany.BranchCompanyService;
import cn.thinkfree.service.businessentity.BusinessEntityService;
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
import java.util.stream.Stream;

@Service
public class ContractInfoServiceImpl extends AbsLogPrinter implements ContractService {
	@Autowired
	MyContractInfoMapper contractInfoMapper;

	@Autowired
	ContractInfoMapper contractJiangInfoMapper;

	@Autowired
	ContractInfoMapper cxcontractInfoMapper;

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

	@Autowired
	CompanySubmitService companySubmitService;

	@Autowired
	ContractTermsChildMapper contractTermsChildMapper;

	@Autowired
	CompanyPaymentMapper companyPaymentMapper;

	@Autowired
	CompanyApplyService companyApplyService;

	@Autowired
	ProvinceMapper provinceMapper;

	@Autowired
	CityMapper cityMapper;

    @Autowired
    EventService eventService;

	//@Autowired
	//NewSchedulingService newSchedulingService;

	@Value( "${custom.cloud.fileUpload}" )
	private String fileUploadUrl;

	@Value( "${custom.cloud.fileUpload.dir}" )
	private String filePathDir;

	@Autowired
	ContractTemplateDictMapper contractTemplateDictMapper;
	
	@Autowired
	DesignerOrderMapper designerOrderMapper;
	
	@Autowired
	DesignDispatchService designDispatchService;

	
	@Autowired
	ConstructionStateService constructionStateService;

	@Autowired
	BranchCompanyService branchCompanyService;

	@Autowired
	FundsCompanyCashMapper fundsCompanyCashMapper;

	@Autowired
	ThirdPartDateService thirdPartDateService;

	@Autowired
	BusinessEntityService businessEntityService;




	@Override
	public boolean saveCash(String contractNumber, String money,String disposableMoney) {
		if(StringUtils.isBlank(contractNumber)){
			return false;
		}
		ContractInfoExample example = new ContractInfoExample();
		example.createCriteria().andContractNumberEqualTo(contractNumber);
		List<ContractInfo> contractInfo = contractJiangInfoMapper.selectByExample(example);
		if(contractInfo.size() == 1){
			CompanyInfoVo companyInfoVo = companyInfoMapper.selectByCompanyId(contractInfo.get(0).getCompanyId());
			if(companyInfoVo == null){
				return false;
			}
			  //todo 添加保证金记录
			FundsCompanyCash fundsCompanyCash = new FundsCompanyCash();
			// 保证金
			BigDecimal defaultMony = new BigDecimal(0);
			BigDecimal promissMony = StringUtils.isNotBlank(money)?BigDecimal.valueOf(Integer.valueOf(money)):new BigDecimal(0);
			fundsCompanyCash.setRemainingMoney(defaultMony);
			fundsCompanyCash.setArrearageMoney(promissMony);
			fundsCompanyCash.setContractMoney(promissMony);
			fundsCompanyCash.setDirectMoney(defaultMony);
			fundsCompanyCash.setGoodsMoney(defaultMony);
			fundsCompanyCash.setDisposableMoney(StringUtils.isNotBlank(disposableMoney)?BigDecimal.valueOf(Integer.valueOf(disposableMoney)):new BigDecimal(0));
			// 公司id不为空获取门店id，名称，公司id，名称。经营主体编号，名称。
			if (StringUtils.isNotBlank(companyInfoVo.getCompanyId())) {
				EnterCompanyOrganizationVO enterCompanyOrganizationVO = branchCompanyService.getCompanyOrganizationByCompanyId(companyInfoVo.getCompanyId());
				if (enterCompanyOrganizationVO != null) {

					// 门店
					fundsCompanyCash.setShopName(enterCompanyOrganizationVO.getStoreNm());
					fundsCompanyCash.setShopNo(enterCompanyOrganizationVO.getStoreId());
					// 分公司
					fundsCompanyCash.setProvinceCompanyName(enterCompanyOrganizationVO.getBranchCompanyNm());
					fundsCompanyCash.setProvinceCompanyNo(enterCompanyOrganizationVO.getBranchCompanyCode());
					// 经营主体
					fundsCompanyCash.setSubjectName(enterCompanyOrganizationVO.getBusinessEntityNm());
					fundsCompanyCash.setSubjectNo(enterCompanyOrganizationVO.getBusinessEntityCode());
					// 城市分站
					fundsCompanyCash.setSubstationName(enterCompanyOrganizationVO.getCityBranchNm());
					fundsCompanyCash.setSubstationNo(enterCompanyOrganizationVO.getCityBranchCode());

					// 公司名称
					fundsCompanyCash.setCompanyName(companyInfoVo.getCompanyName());
					fundsCompanyCash.setCompanyNo(companyInfoVo.getCompanyId());
					fundsCompanyCash.setType(companyInfoVo.getRoleId());
				}
			}
			int result = fundsCompanyCashMapper.insertSelective(fundsCompanyCash);
			if (result >0 ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PageInfo<ContractVo> pageContractBySEO( ContractSEO contractSEO )
	{
		PageHelper.startPage( contractSEO.getPage(), contractSEO.getRows() );
		List<ContractVo> list = contractInfoMapper.selectContractPage( contractSEO );
		for ( int i = 0; i < list.size(); i++ )
		{
			list.get( i ).setContractStatus( ContractStatus.getDesc( list.get( i ).getContractStatus() ) );
		}
		return  (new PageInfo<>( list ) );
	}


	@Override
	public void exportList( ContractSEO contractSEO, HttpServletResponse response )
	{
		ExcelData data = new ExcelData();
		String title = "合同信息数据";
		data.setName( title );
		/* 添加表头 */
		List<String> titles = new ArrayList<>();
		titles.add( "签约日期" );
		titles.add( "合同编码" );
		titles.add( "公司编码" );
		titles.add( "公司名称" );
		titles.add( "公司类型" );
		titles.add( "所在地" );
		titles.add( "保证金" );
		titles.add( "合同状态" );
		data.setTitles( titles );
		/* 添加列 */
		List<List<Object> >	rows	= new ArrayList<>();
		List<Object>		row	= null;
		PageHelper.startPage( contractSEO.getPage(), contractSEO.getRows() );
		List<ContractVo> list = contractInfoMapper.selectContractPage( contractSEO );
		for ( int i = 0; i < list.size(); i++ )
		{
			list.get(i).setContractStatus(ContractStatus.getDesc(list.get(i).getContractStatus()));
		}
		for ( int i = 0; i < list.size(); i++ )
		{
			row = new ArrayList<>();
			row.add( list.get( i ).getSignedTime() );
			row.add( list.get( i ).getContractNumber() );
			row.add( list.get( i ).getCompanyId() );
			row.add( list.get( i ).getCompanyName() );
			row.add( list.get( i ).getCompanyType() );
			row.add( list.get( i ).getCompanyLocation() );
			row.add( list.get( i ).getDepositMoney() );
			row.add( list.get( i ).getContractStatus() );
			rows.add( row );
		}
		data.setRows( rows );
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		String fileName = title + "_" + fdate.format(new Date()) + ".xls";
		try {
			ExcelUtils.exportExcel( response, fileName, data );
		} catch ( Exception e ) {
			printErrorMes( "合同导出异常", e.getMessage() );
		}
	}


    /**
     *
     * 财务审核的时候要 修改公司表的状态 修改合同表 添加审核记录表
     *
     * @author lvqidong 公司入驻状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金 6入驻成功
     */
    @Override
    @Transactional
    public boolean auditContract( String contractNumber, String companyId, String auditStatus, String auditCase ) {
        Date date = new Date();
        /* 修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝 */
        ContractVo vo = new ContractVo();
        vo.setCompanyId( companyId );
        vo.setContractNumber( contractNumber );
        /* 修改公司表 */
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyId( companyId );
        if ( auditStatus.equals( AuditStatus.AuditPass.shortVal())){  /* 财务审核通过 */
			companyInfo.setAuditStatus( CompanyAuditStatus.SUCCESSCHECK.stringVal() );
            /* 财务审核通过生成合同 */
			String pdfUrl = createContractDoc(contractNumber);
            String signedTime = DateUtil.formartDate(new Date(),"yyyy-MM-dd HH:mm:ss");
            vo.setContractUrl( pdfUrl );
            vo.setSignedTime(signedTime);
			vo.setContractStatus( ContractStatus.AuditPass.shortVal() );
            printInfoMes("财务审核通过发生三方接口数据 contractNumber｛｝", contractNumber);
            try {
            	eventService.publish(new MarginContractEvent(contractNumber,signedTime));
			} catch (Exception e) {
				 printErrorMes("财务审核通过发生三方接口数据 contractNumber｛｝", e.getMessage());
			}
            //生成预付订单
            try {
            	List<SyncOrderVO>  listvo=  thirdPartDateService.getOrderContractToB(contractNumber);
         	    CreateOrder order = new CreateOrder();
                order.setData(listvo);
                eventService.publish(order);
                printInfoMes("财务审核通过 生成合同保证金预付订单 调用完成");
			} catch (Exception e2) {
				printErrorMes("财务审核通过 生成合同保证金预付订单 调用完成｛｝",e2.getMessage());
			}

        } else {
        	/* 财务审核不通过 */
			vo.setContractStatus( ContractStatus.AuditDecline.shortVal() );
            companyInfo.setAuditStatus( CompanyAuditStatus.FAILCHECK.stringVal() );
        }
        ContractInfoExample example = new ContractInfoExample();
        example.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setContractStatus(vo.getContractStatus());
        contractInfo.setContractUrl(vo.getContractUrl());
        //获取合同信息 更新主表的合同开始时间和合同结束时间
        int flag = cxcontractInfoMapper.updateByExampleSelective( contractInfo, example );
        int flagT = companyInfoMapper.updateauditStatus( companyInfo );
        UserVO	userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
		String auditPersion = userVO == null ? "" : userVO.getName();
		String auditAccount = userVO == null ? "" : userVO.getUsername();
        /* 添加审核记录表 */
		PcAuditInfo record = new PcAuditInfo("1", "1", auditPersion, auditStatus, new Date(), companyId, auditCase,
                contractNumber, date, auditAccount);
        int flagon = pcAuditInfoMapper.insertSelective( record );
        // TODO 初始化保证金流水
		if(auditStatus.equals( AuditStatus.AuditPass.shortVal())){
			String fullMoney = "0";
			String firstMoney = "0";
			ContractTermsExample contractTermsExample = new ContractTermsExample();
			ContractTermsExample.Criteria criteria = contractTermsExample.createCriteria()
					.andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber);
			Function<Optional<ContractTerms>,String> eval = c-> c.isPresent() ? c.get().getContractValue() : "0";
			BiFunction<Stream<ContractTerms>,String,Optional<ContractTerms>> filter =(list,code)-> list
					.filter(c -> code.equalsIgnoreCase(c.getContractDictCode())).findFirst();
			/*保证金分期*/
    		ContractTermsChildExample examplec = new ContractTermsChildExample();
    		examplec.createCriteria().
    		andCompanyIdEqualTo((companyInfo.getCompanyId())).
    		andContractNumberEqualTo(contractNumber).andCostTypeEqualTo("13");
    		example.setOrderByClause(" c_type asc");
    		List<ContractTermsChild> childList = contractTermsChildMapper.selectByExample(examplec);
			if("SJHT".equalsIgnoreCase(contractNumber.substring(0,4))){
				String fullMoneyCode = "c15";
				String firstMoneyCode = "c16";
				criteria.andContractDictCodeIn(Lists.newArrayList(fullMoneyCode,firstMoneyCode));
				List<ContractTerms> contractTerms = pcContractTermsMapper.selectByExample(contractTermsExample);
				Optional<ContractTerms> fullMoneyVO = filter.apply(contractTerms.stream(),fullMoneyCode);
				fullMoney = eval.apply(fullMoneyVO);
				//合同结算表数据为空的话第一次支付等于全额
				if(childList == null || childList.size() == 0){
					firstMoney = fullMoney;
				}else{
					for (int i = 0; i < childList.size(); i++) {
						if("1".equals(childList.get(i).getcType())){
							firstMoney = 	childList.get(i).getCostValue();
							break;
						}
					}
				}
//				Optional<ContractTerms> firstMoneyVO = filter.apply(contractTerms.stream(),fullMoneyCode);
//				firstMoney = eval.apply(firstMoneyVO);

			}else if("BDHT".equalsIgnoreCase(contractNumber.substring(0,4))){
				String fullMoneyCode = "c17";
				String firstMoneyCode = "c18";
				criteria.andContractDictCodeIn(Lists.newArrayList(fullMoneyCode,firstMoneyCode));
				List<ContractTerms> contractTerms = pcContractTermsMapper.selectByExample(contractTermsExample);

				Optional<ContractTerms> fullMoneyVO = filter.apply(contractTerms.stream(),fullMoneyCode);
				fullMoney = eval.apply(fullMoneyVO);
				//合同结算表数据为空的话第一次支付等于全额
				if(childList == null || childList.size() == 0){
					firstMoney = fullMoney;
				}else{
					for (int i = 0; i < childList.size(); i++) {
						if("1".equals(childList.get(i).getcType())){
							firstMoney = 	childList.get(i).getCostValue();
							break;
						}
					}
				}
//				Optional<ContractTerms> firstMoneyVO = filter.apply(contractTerms.stream(),fullMoneyCode);
//				firstMoney = eval.apply(firstMoneyVO);
			}
			saveCash(contractNumber,fullMoney,firstMoney);
		}

        if ( flag > 0 && flagT > 0 && flagon > 0 ) {
            return true;
        }
        return false;
    }


	@Override
	public boolean ackEarnestMoney( String contractNumber, String companyId )
	{

		ContractInfoExample example = new ContractInfoExample();
		example.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
		ContractInfo contractInfo = new ContractInfo();
		contractInfo.setContractStatus( "5" );
		cxcontractInfoMapper.updateByExampleSelective( contractInfo, example );
		 //查询当前合同保证金
		ContractTermsExample	exp		= new ContractTermsExample();
	    exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
	    List<ContractTerms> list = pcContractTermsMapper.selectByExample( exp );
	    Map<String,String> resMap = new HashMap<>();
	    if(list != null){
		    for (int i = 0; i < list.size(); i++) {
		    	resMap.put(list.get(i).getContractDictCode(), list.get(i).getContractValue());
			}
	    }
	    //查询公司类型
	    /* 公司详情 */
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo( companyId );
		String deposistMoney = "";
		//装修
	    if( companyInfo.getCompanyInfo().getRoleId().equals( CompanyType.BD.stringVal() )){
	    	if(resMap != null ){
	    	  deposistMoney = String.valueOf(resMap.get("c17"));
	    	}
	    }else{
	    	if(resMap != null){
	    	  deposistMoney = String.valueOf(resMap.get("c15"));
	    	}
	    }
		/* 修改公司表 */
		CompanyInfo record  = new CompanyInfo();
		record.setAuditStatus(CompanyAuditStatus.SUCCESSJOIN.stringVal());
		record.setDepositMoney(Integer.valueOf(deposistMoney));
		CompanyInfoExample companyInfoex = new CompanyInfoExample();
		companyInfoex.createCriteria().andCompanyIdEqualTo( companyId );
		/* 确认已交保证金 */
		int flag = companyInfoMapper.updateByExampleSelective(record ,companyInfoex );
		if ( flag > 0 )
		{
			return true;
		}
		return false;
	}


	@Override
	public ContractDetailsVo contractDetails( String contractNumber, String companyId )
	{
		ContractDetailsVo resVo = new ContractDetailsVo();
		ContractVo vo = new ContractVo();
		vo.setContractNumber( contractNumber );
		ContractVo			newVo		= contractInfoMapper.selectContractBycontractNumber( vo );      /* 合同信息 */
//		CompanySubmitVo			companyInfo	= companySubmitService.findCompanyInfo( newVo.getCompanyId() ); /* 公司信息 */
//		List<Map<String, Object> >	list		= getContractInfo( contractNumber, newVo, companyInfo );
		PcAuditInfoExample autit = new PcAuditInfoExample();
	    autit.createCriteria().andCompanyIdEqualTo(companyId)
	        .andContractNumberEqualTo(contractNumber);
				//.andAuditTypeEqualTo(CompanyConstants.AuditType.CONTRACT.stringVal());
	    autit.setOrderByClause("create_time desc");
	    List<PcAuditInfo>  auList =  pcAuditInfoMapper.selectByExample(autit);
	    resVo.setList(auList);
	    resVo.setVo(newVo);

		return  resVo;
	}


	

	@Override
	public String selectContractBycontractNumber( String contractNumber )
	{
		ContractVo vo = new ContractVo();
		vo.setContractNumber( contractNumber );
		return(contractInfoMapper.selectContractBycontractNumber( vo ) == null ? ""
		       : contractInfoMapper.selectContractBycontractNumber( vo ).getContractUrl() );
	}


	@Override
	public String createContractDoc( String contractNumber)
	{
		String url = ""; /* 上传服务器返回地址 */
		Map<String, Object> root = new HashMap<>(); /* data数据 */

		ContractVo vo = new ContractVo();
		vo.setContractNumber(contractNumber);
		 /* 合同信息 */
		ContractVo newVo = contractInfoMapper.selectContractBycontractNumber(vo);
		/* 公司信息 */
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo((newVo.getCompanyId()));
		if(companyInfo == null){
			throw  new RuntimeException("公司数据为nul");
		}

		/* 合同详情 */
		String companyId = newVo.getCompanyId();
		ContractTermsExample exp = new ContractTermsExample();
		exp.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber);
		List<ContractTerms> list = pcContractTermsMapper.selectByExample(exp);
		for (int i = 0; i < list.size(); i++) {
			root.put(list.get(i).getContractDictCode(), list.get(i).getContractValue());
		}
		/* 查询结算规则 */
		ContractTermsChildExample example = new ContractTermsChildExample();
		example.createCriteria().andCompanyIdEqualTo((newVo.getCompanyId())).andContractNumberEqualTo(contractNumber);
		List<ContractTermsChild> childList = contractTermsChildMapper.selectByExample(example);
		Map<Long, List<ContractTermsChild>> map2 = new LinkedHashMap<Long, List<ContractTermsChild>>();
		/* 根据类型分组 */
		CommonGroupUtils.listGroup2Map(childList, map2, ContractTermsChild.class, "cost_type"); 
		if (childList != null) {
			for (int i = 0; i < childList.size(); i++) {
				ContractTermsChildExample example1 = new ContractTermsChildExample();
				example1.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber)
						.andCostTypeEqualTo(childList.get(i).getCostType());
				List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample(example1);
				root.put("code" + childList.get(i).getCostType(), childListr);
			}
		}
		String signedTime = DateUtil.formartDate(new Date(),"yyyy-MM-dd HH:mm:ss");
		/* 判断公司类型 */
		 /* 装修公司 */
		if (companyInfo.getCompanyInfo().getRoleId().equals(CompanyConstants.RoleType.BD.code))

		{
			if (!StringUtils.isEmpty(signedTime)) {
				root.put("signedTime", DateUtil.formateToDate(signedTime, "yyyy-MM-dd"));// 合同生效时间
			} else {
				root.put("signedTime", "");
			}
			if (root.get("c08") != null && !String.valueOf(root.get("c08")).equals("")) {// 合同开始时间
				root.put("startTime", DateUtil.formateToDate(String.valueOf(root.get("c08")), "yyyy-MM-dd"));// 合同开始时间
			}
			if (root.get("c09") != null && !String.valueOf(root.get("c09")).equals("")) {
				root.put("endTime", DateUtil.formateToDate(String.valueOf(root.get("c09")), "yyyy-MM-dd"));// 合同结束时间
			}
			// 特殊处理
			try {
				String filePath = FreemarkerUtils.savePdf(filePathDir + contractNumber, "1", root);
				/* 上传 */
				url = PdfUplodUtils.upload(filePath, fileUploadUrl);
			} catch (Exception e) {
				e.printStackTrace();
				printErrorMes("生成pdf合同发生错误", e.getMessage());
			}
			/* 设计公司 */
		} else if (companyInfo.getCompanyInfo().getRoleId().equals(CompanyConstants.RoleType.SJ.code))

		{
			if (!StringUtils.isEmpty(signedTime)) {
				root.put("signedTime", DateUtil.formateToDate(signedTime, "yyyy-MM-dd"));// 合同生效时间
			} else {
				root.put("signedTime", "");
			}
			if (root.get("c03") != null && !String.valueOf(root.get("c03")).equals("")) {// 合同开始时间
				root.put("startTime", DateUtil.formateToDate(String.valueOf(root.get("c03")), "yyyy-MM-dd"));// 合同开始时间
			}
			if (root.get("c04") != null && !String.valueOf(root.get("c04")).equals("")) {
				root.put("endTime", DateUtil.formateToDate(String.valueOf(root.get("c03")), "yyyy-MM-dd"));// 合同结束时间
			}

			try {
				String filePath = FreemarkerUtils.savePdf(filePathDir + contractNumber, "0", root);
				/* 上传 */
				url = PdfUplodUtils.upload(filePath, fileUploadUrl);

				if(url!= null){//上传完删除文件
					File bin = new File(filePath);
					bin.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
				printErrorMes("生成pdf合同发生错误", e.getMessage());
			}
		}

		return url;
	}


	/**
	 *
	 *
	 * @param contractNumber
	 * @param CompanyId
	 *            公司编号
	 * @param roleType
	 *            公司类型
	 * @param rmap
	 */
	@Override
	public Map<String, Object> balanceInfo( String contractNumber, String CompanyId, String roleType )
	{
		/* 合同结算比例 */
		Map<String, Object>	rmap	= new HashMap<>();
		ContractTermsExample	exp	= new ContractTermsExample();
		exp.createCriteria().andCompanyIdEqualTo( CompanyId ).andContractNumberEqualTo( contractNumber );
		List<ContractTerms> list = pcContractTermsMapper.selectByExample( exp );

		Map<String, String> dbmap = new HashMap<>();
		for ( int i = 0; i < list.size(); i++ )
		{
			dbmap.put( list.get( i ).getContractDictCode(), list.get( i ).getContractValue() );
		}
		if ( roleType.equals( CompanyType.SJ.stringVal()) ) /* 设计公司 */

		{
			if ( list != null & list.size() > 0 )
			{
				/* 平台服务费 */
				rmap.put( "serviceCharge", dbmap.get( "08" ).toString() );
				/* 产品 */
				rmap.put( "productCharge", dbmap.get( "14" ).toString() );
				/* 施工管理费 */
				rmap.put( "roadWorkCharge", dbmap.get( "17" ).toString() );
				/* 保证金 */
				rmap.put( "cashCharge", dbmap.get( "22" ).toString() );
			}
			/* 查询结算规则 */
			ContractTermsChildExample example = new ContractTermsChildExample();
			example.createCriteria().andCompanyIdEqualTo( CompanyId ).andContractNumberEqualTo( contractNumber );
			List<ContractTermsChild>		childList	= contractTermsChildMapper.selectByExample( example );
			Map<Long, List<ContractTermsChild> >	map_two		= new LinkedHashMap<Long, List<ContractTermsChild> >();
			CommonGroupUtils.listGroup2Map( childList, map_two, ContractTermsChild.class, "cost_type" ); /* 根据类型分组 */
			Map<String, List<ContractTermsChild> > ma = new HashMap<>();
			if ( childList != null )
			{
				for ( int i = 0; i < childList.size(); i++ )
				{
					ContractTermsChildExample example1 = new ContractTermsChildExample();
					example1.createCriteria().andCompanyIdEqualTo( CompanyId ).andContractNumberEqualTo( contractNumber )
					.andCostTypeEqualTo( childList.get( i ).getCostType() );
					List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample( example1 );
					ma.put( childList.get( i ).getCostType(), childListr );
				}
			}
			rmap.put( "designCastList", ma.get( "01" ) );   /* 设计费结算比例 */
			rmap.put( "productCastLis", ma.get( "02" ) );   /* 产品费结算比例 */
			rmap.put( "roadWorkCastLis", ma.get( "03" ) );  /* 施工费结算比例 */
		} else if ( roleType.equals(CompanyType.BD.stringVal() ) ) /* 装饰公司 */

		{
			if ( list != null & list.size() > 0 )
			{
				/* 平台服务费 */
				rmap.put( "serviceCharge", dbmap.get( "16" ).toString() );
				/* 保证金总额 */
				rmap.put( "cashTotal", dbmap.get( "17" ).toString() );
				/* 保证金一次性缴纳 */
				rmap.put( "cashOne", dbmap.get( "18" ).toString() );
				/* 扣除比例 */
				rmap.put( "cashProportion", dbmap.get( "19" ).toString() );
				/* 消费辅料占比 */
				rmap.put( "materialsProportion", dbmap.get( "19" ).toString() );
			}
			/* 查询结算规则 */
			ContractTermsChildExample example = new ContractTermsChildExample();
			example.createCriteria().andCompanyIdEqualTo( CompanyId ).andContractNumberEqualTo( contractNumber );
			List<ContractTermsChild>		childList	= contractTermsChildMapper.selectByExample( example );
			Map<Long, List<ContractTermsChild> >	map2		= new LinkedHashMap<Long, List<ContractTermsChild> >();
			CommonGroupUtils.listGroup2Map( childList, map2, ContractTermsChild.class, "cost_type" ); /* 根据类型分组 */
			Map<String, List<ContractTermsChild> > ma = new HashMap<>();
			if ( childList != null )
			{
				for ( int i = 0; i < childList.size(); i++ )
				{
					ContractTermsChildExample example1 = new ContractTermsChildExample();
					example1.createCriteria().andCompanyIdEqualTo( CompanyId ).andContractNumberEqualTo( contractNumber )
					.andCostTypeEqualTo( childList.get( i ).getCostType() );
					List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample( example1 );
					ma.put( childList.get( i ).getCostType(), childListr );
				}
			}
			rmap.put( "designCastList", ma.get( "04" ) ); /* 返款规则 */
		}
		return rmap;
	}


	@Transactional
	@Override
	public boolean insertContractClause( String contractNumber, String companyId, ContractClauseVO contractClausevo )
	{

	     try {
			if ( contractClausevo.getParamMap() != null )
			{
				Iterator<Map.Entry<String, String> > entries = contractClausevo.getParamMap().entrySet().iterator();
				while ( entries.hasNext() )
				{
					Map.Entry<String, String>	entry	= entries.next();
					String				key	= entry.getKey();
					if ( key.equals( "paymentNumber" ) ) /* 添加支付方案 */
					{
						CompanyPayment record = new CompanyPayment();
						record.setCompanyCode( companyId );
						record.setProgramCode( entry.getValue() );
						companyPaymentMapper.insertSelective( record );
						break;
					}
					String		value	= entry.getValue();
					ContractTerms	terms	= new ContractTerms();
					terms.setCompanyId( companyId );
					terms.setContractNumber( contractNumber );
					terms.setCreateTime( new Date() );
					terms.setUpdateTime( new Date() );
					terms.setContractDictCode( key );
					terms.setContractValue( value );
					/* list.add(terms); */
					ContractTermsExample exp = new ContractTermsExample();
					exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractDictCodeEqualTo( key )
					.andContractNumberEqualTo( contractNumber );
					pcContractTermsMapper.deleteByExample( exp );
					pcContractTermsMapper.insertSelective( terms );
				}
			}
			/* 设置结算规则 */
			if ( contractClausevo.getChildparamMap() != null )
			{
				Iterator < Entry < String, List < ContractTermsChild >>> childentries = contractClausevo.getChildparamMap()
													.entrySet().iterator();
				ContractTermsChildExample	exp	= new ContractTermsChildExample();
				exp.createCriteria().andCompanyIdEqualTo( companyId )
						.andContractNumberEqualTo( contractNumber );
		        /* 删除重复数据 */
				contractTermsChildMapper.deleteByExample( exp );
				while ( childentries.hasNext() )
				{
					Entry<String, List<ContractTermsChild> > entry	= childentries.next();
					List<ContractTermsChild> list = entry.getValue();
					for ( int i = 0; i < list.size(); i++ )
					{
						ContractTermsChild		child	= list.get( i );
					    /* 新增合同规则 */
						contractTermsChildMapper.insertSelective( child );
					}
				}
			}

			// 修改合同状态
			ContractInfo record = new ContractInfo();
			record.setContractStatus(ContractStatus.WaitAudit.shortVal());
			record.setCompanyId(companyId);
			if (StringUtils.isNotBlank(contractClausevo.getParamMap().get("c03"))) {
				Date startTime = DateUtil.formateToDate(contractClausevo.getParamMap().get("c03"), "yyyy-mm-dd");
				record.setStartTime(startTime);
			} else {
				return false;
			}
			if (StringUtils.isNotBlank(contractClausevo.getParamMap().get("c04"))) {
				Date endTime = DateUtil.formateToDate(contractClausevo.getParamMap().get("c04"), "yyyy-mm-dd");
				record.setEndTime(endTime);
			} else {
				return false;
			}
			record.setContractNumber(contractNumber);
			ContractInfoExample example = new ContractInfoExample();
			example.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber);
			cxcontractInfoMapper.updateByExampleSelective(record, example);
			// 修改公司状态
			companyApplyService.updateStatus(companyId, CompanyAuditStatus.CHECKING.stringVal());
		} catch (Exception e) {
			printErrorMes("设置合同条款服务异常 {}" + e.getMessage());
			return false;
		}
		return true;
	}


	


	@Override
	public Map<String, Object> getContractDetailInfo( String contractNumber, String companyId )
	{
		Map<String, Object> reMap = new HashMap<>();
		/* 公司详情 */
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo( companyId );

		/* 合同信息 */
		ContractVo vo = new ContractVo();
		vo.setContractNumber( contractNumber );
		ContractVo	newVo = contractInfoMapper.selectContractBycontractNumber( vo );
		reMap.put("ContractVo",newVo == null ? "" : newVo );

		/* 合同详情 */
		ContractTermsExample exp = new ContractTermsExample();
		/* 判断公司类型 */
		exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
		exp.setOrderByClause(" contract_dict_code ");
		List<ContractTerms> list = pcContractTermsMapper.selectByExample( exp );

		if ( list.size() == 0 && companyInfo != null )
		{
         //ContractTerms term_0 = new ContractTerms( "01", "居然设计家" );
          //list.add( term_0 );
			String type = companyInfo.getCompanyInfo().getRoleId().equals("SJ")?"0":"1";
			ContractTemplateDictExample example = new ContractTemplateDictExample();
			example.createCriteria().andTypeEqualTo(type);
			List<ContractTemplateDict> list1 = contractTemplateDictMapper.selectByExample(example);
			for (int i = 0; i < list1.size(); i++) {
				ContractTerms ter = new ContractTerms();

		        String str1 = list1.get(i).getCode().replace("c", "");
				ter.setContractDictCode(str1);
				list.add(ter);
			}
			
		}
		/* 查询结算规则 */
			ContractTermsChildExample example = new ContractTermsChildExample();
			example.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
			List<ContractTermsChild>		childList	= contractTermsChildMapper.selectByExample( example );
			Map<Long, List<ContractTermsChild> >	map2		= new LinkedHashMap<Long, List<ContractTermsChild> >();
			CommonGroupUtils.listGroup2Map( childList, map2, ContractTermsChild.class, "cost_type" ); /* 根据类型分组 */
			if ( childList != null )
			{
				Map<String, List<ContractTermsChild> > ma = new HashMap<>();
				for ( int i = 0; i < childList.size(); i++ )
				{
					ContractTermsChildExample example1 = new ContractTermsChildExample();
					example1.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber )
					.andCostTypeEqualTo( childList.get( i ).getCostType() );
					List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample( example1 );
					ma.put( childList.get( i ).getCostType(), childListr );
				}
				reMap.put( "ContractChild", ma );
			}
		
		/* 查询合同设置项目 */
		reMap.put( "companyMap", companyInfo == null ? "" : companyInfo );
		reMap.put( "ContractList", list );

		return(reMap);
	}


	@Override
	public String createOrderContract( String companyId, String orderNumber, String type )
	{
		String		contractNumber	= "HT" + getOrderContract();
		OrderContract	record		= new OrderContract();
		record.setContractNumber( contractNumber );
		record.setCompanyId( companyId );
		record.setSignTime( new Date() );
		record.setContractType( type );
		record.setCteateTime( new Date() );
		record.setUpdateTime( new Date() );
		record.setOrderNumber( orderNumber );
		record.setAuditType("2");
		/* record.setConractUrlPdf(url); */
		//每个订单只有一个合同
		OrderContractExample example = new OrderContractExample();
		example.createCriteria().andOrderNumberEqualTo(orderNumber);
		orderContractMapper.deleteByExample(example);
		
		orderContractMapper.insertSelective( record );
		//newSchedulingService.createScheduling(orderNumber);
		return(contractNumber);
	}


	@Override
	public boolean auditStatusOrderContract( String orderNumber, String auditStatus )
	{
		/* 审核通过 生成合同 */
		OrderContract record = new OrderContract();
		if ( auditStatus.equals( AuditStatus.AuditPass.shortVal() ) )
		{
			record.setAuditType( auditStatus );
		} else if ( auditStatus.equals( AuditStatus.AuditPass.shortVal() ) )
		{
			record.setAuditType( auditStatus );
		}
		OrderContractExample example = new OrderContractExample();
		example.createCriteria().andOrderNumberEqualTo( orderNumber );
		int flag = orderContractMapper.updateByExample( record, example );

		if ( flag > 0 )
		{
			return(true);
		}
		return(false);
	}

	
	@Override
	public boolean createOrderContractpdf(String orderNumber, String CompanyId,Map<String, Object> root) {
		try {
			// 订单合同
			OrderContractExample record = new OrderContractExample();
			record.createCriteria().andOrderNumberEqualTo(orderNumber).andCompanyIdEqualTo(CompanyId);
			List<OrderContract> list = orderContractMapper.selectByExample(record);
			OrderContract contrat = list.get(0);
			String pdfUrl = "";
			if (contrat.getContractType().equals("02")) {

				try {
					String filePath = FreemarkerUtils.savePdf(filePathDir + orderNumber, "2", root);
					/* 上传 */
					pdfUrl = PdfUplodUtils.upload(filePath, fileUploadUrl);
				} catch (Exception e) {
					e.printStackTrace();
					printErrorMes("生成pdf合同发生错误", e.getMessage());
				}

			} else if (contrat.getContractType().equals("03")) {
				if(root != null && root.size() > 0 ){
					try {
						JSONArray arr = JSONArray.parseArray(String.valueOf(root.get("c08")));
						List<Map<String,String>> listR = new ArrayList<>();
						Map<String,String> mapR = new HashMap<>();
						for (int i = 0; i < arr.size(); i++) {
							 JSONObject job = arr.getJSONObject(i); 
							mapR.put("code01", job.getString("progressName"));
							mapR.put("code02", job.getString("stageCode"));
							mapR.put("code03", job.getString("payPercentum"));
							mapR.put("code04", job.getString("payMoney"));
							listR.add(mapR);
						}
						root.put("c08",listR);
					} catch (Exception e) {
						e.printStackTrace();
						printErrorMes("生成合同pdf 解析json 错误{}", e.getMessage());
						throw new RuntimeException("系统数据异常");
					}
				}else{
					throw new RuntimeException("设计合同生成pdf 数据异常  数据为 null or ''");
				}
				try {
					String filePath = FreemarkerUtils.savePdf(filePathDir + orderNumber, "3", root);
					/* 上传 */
					pdfUrl = PdfUplodUtils.upload(filePath, fileUploadUrl);
				} catch (Exception e) {
					e.printStackTrace();
					printErrorMes("生成pdf合同发生错误", e.getMessage());
				}
			}
			OrderContract recordu = new OrderContract();
			recordu.setConractUrlPdf(pdfUrl);
			OrderContractExample example = new OrderContractExample();
			example.createCriteria().andOrderNumberEqualTo(orderNumber);
			orderContractMapper.updateByExampleSelective(recordu, example);
		} catch (Exception e) {
			printErrorMes("生成订单合同{}",orderNumber,"系统异常 {}"+e.getMessage());
			return false;
		}
		return true;
	}




	@Override
	public String getPdfUrlByOrderNumber( String orderNumber )
	{
		OrderContractExample example = new OrderContractExample();

		example.createCriteria().andOrderNumberEqualTo( orderNumber );

		List<OrderContract> list = orderContractMapper.selectByExample( example );

		return( (list != null && list.size() > 0) ? list.get( 0 ).getConractUrlPdf() : "");
	}


	/**
	 * 获取6-10 的随机位数数字
	 *
	 * @param length
	 *            想要生成的长度
	 * @return result
	 */
	public synchronized static String getOrderContract()
	{
		Random random = new Random();
		DecimalFormat df = new DecimalFormat( "00" );
		String no = new SimpleDateFormat( "yyyyMMddHHmmss" ).format( new Date() ) + df.format( random.nextInt( 100 ) );
		return no;
	}


	
	@Override
	public  Map<String,Object> insertDesignOrderContract( String orderNumber, Map<String, String> paramMap )
	{
		Map<String, Object> resMap = new HashMap<>();
		//设计订单
		DesignerOrderExample examploder = new DesignerOrderExample();
		examploder.createCriteria().andOrderNoEqualTo(orderNumber);
		List<DesignerOrder> oderlist = designerOrderMapper.selectByExample(examploder);
		if(oderlist == null || oderlist.size()==0){
			 resMap.put("code", "false");
			 resMap.put("msg", "该订单不存在");
			 return resMap;
		}
		
		OrderContractExample expo = new OrderContractExample();
		expo.createCriteria().andOrderNumberEqualTo(orderNumber);
		//插入订单合同是否录入
		 List<OrderContract> list = orderContractMapper.selectByExample(expo);
		 if(list.size() > 0){
			 resMap.put("code", "false");
			 resMap.put("msg", "该合同已被其他设计师录入，无法重复录入");
			 return resMap;
		 }
		 String companyId = oderlist.get(0).getCompanyId();
		try {
			
			/* 插入合同主表 */
			String contractNumber = createOrderContract(companyId, orderNumber, "02");
			/* 插入合同iterm 详情 */
			Map<String,Object> root = new HashMap<>();
			if ( paramMap != null )
			{
				Iterator<Map.Entry<String, String> > entries = paramMap.entrySet().iterator();
				while ( entries.hasNext() )
				{
					Map.Entry<String, String>	entry	= entries.next();
					String				key	= entry.getKey();
					String				value	= entry.getValue();
					ContractTerms			terms	= new ContractTerms();
					terms.setCompanyId( companyId );
					terms.setContractNumber( contractNumber );
					terms.setCreateTime( new Date() );
					terms.setUpdateTime( new Date() );
					terms.setContractDictCode( key );
					terms.setContractValue( value );
					/* list.add(terms); */
					ContractTermsExample exp = new ContractTermsExample();
					exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractDictCodeEqualTo( key )
					.andContractNumberEqualTo( contractNumber );
					pcContractTermsMapper.deleteByExample( exp );
					pcContractTermsMapper.insertSelective( terms );
					root.put(key,value);
				}
			}
			//处理json数据
			if(root.size() > 0 && StringUtils.isNotEmpty(String.valueOf(root.get("c20")))){
				List<Map<String,String>> rootMap = new ArrayList<>();
				  String jsonSr = String.valueOf(root.get("c20"));
				  if(!StringUtils.isEmpty(jsonSr)){
  				  JSONArray jsonArray=JSONArray.parseArray(jsonSr);
  				  for (int i = 0; i < jsonArray.size(); i++) {
  					Map<String,String> jsonMap = (Map<String, String>) jsonArray.get(i);
  					rootMap.add(jsonMap);
  				  }
				 }
				  root.put("c100",rootMap);
			}
			//生成pdf
			createOrderContractpdf(orderNumber, companyId, root);
			 resMap.put("code", "true");
			 resMap.put("msg", "合同录入成功");
			 return resMap;
		} catch ( Exception e ) {
			e.printStackTrace();
			 resMap.put("code", "false");
			 resMap.put("msg", "服务异常,请联系管理员");
		}
		
		return resMap;
	}


	
	@Override
	public boolean insertRoadWorkOrderContract( String orderNumber, String companyId, Map<String, String> paramMap )
	{
		try {
			/* 插入合同主表 */
			String contractNumber = createOrderContract(companyId, orderNumber, "03");
			/* 插入合同iterm 详情 */
			Map<String,Object> root = new HashMap<>();
			if ( paramMap != null )
			{
				Iterator<Map.Entry<String, String> > entries = paramMap.entrySet().iterator();
				while ( entries.hasNext() )
				{
					Map.Entry<String, String>	entry	= entries.next();
					String	key	= entry.getKey();
					String	value= entry.getValue();
					ContractTerms terms	= new ContractTerms();
					terms.setCompanyId( companyId );
					terms.setContractNumber( contractNumber );
					terms.setCreateTime( new Date() );
					terms.setUpdateTime( new Date() );
					terms.setContractDictCode( key );
					terms.setContractValue( value );
					ContractTermsExample exp = new ContractTermsExample();
					exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractDictCodeEqualTo( key )
					.andContractNumberEqualTo( contractNumber );
					pcContractTermsMapper.deleteByExample( exp );
					pcContractTermsMapper.insertSelective( terms );
					root.put(key,value);
				}
			}
			//生成pdf
			createOrderContractpdf(orderNumber, companyId, root);
			return(true);
		} catch ( Exception e ) {
			e.printStackTrace();
			return(false);
		}
	}


	@Override
	public List<ContractInfo> getEnterContractBycompanyId( String companyId )
	{
		List<ContractInfo> list = new ArrayList<>();
		if ( !StringUtils.isEmpty( companyId ) )
		{
			ContractInfoExample example = new ContractInfoExample();
			list.addAll( cxcontractInfoMapper.selectByExample( example ) );
		}
		return(list);
	}


	@Override
	public List<ContractCostVo> queryListContractCostVoBycontractNumber(String contractNumber, String roleId) {

		// 更加合同编号查询入住公司合同类型
		ContractVo vo = new ContractVo();
		vo.setContractNumber(contractNumber);
		ContractVo newVo = contractInfoMapper.selectContractBycontractNumber(vo); /* 合同信息 */
//		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo(newVo.getCompanyId()); /* 公司信息 */
		/* 合同详情 */
		String companyId = newVo.getCompanyId();
		ContractTermsExample exp = new ContractTermsExample();
		exp.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber);
		List<ContractTerms> list = pcContractTermsMapper.selectByExample(exp);

		Map<String, String> dbmap = new HashMap<>();

		for (int i = 0; i < list.size(); i++) {
			dbmap.put(list.get(i).getContractDictCode(), list.get(i).getContractValue());
		}
		/* 查询结算规则 */
		Map<String, List<ContractTermsChild>> ma = new HashMap<>();
		ContractTermsChildExample example = new ContractTermsChildExample();
		example.createCriteria().andCompanyIdEqualTo((newVo.getCompanyId())).andContractNumberEqualTo(contractNumber);
		List<ContractTermsChild> childList = contractTermsChildMapper.selectByExample(example);
		Map<Long, List<ContractTermsChild>> map2 = new LinkedHashMap<Long, List<ContractTermsChild>>();
		 /* 根据类型分组 */
		CommonGroupUtils.listGroup2Map(childList, map2, ContractTermsChild.class, "cost_type");
		if (childList != null) {
			for (int i = 0; i < childList.size(); i++) {
				ContractTermsChildExample example1 = new ContractTermsChildExample();
				example1.createCriteria().andCompanyIdEqualTo(companyId).andContractNumberEqualTo(contractNumber)
						.andCostTypeEqualTo(childList.get(i).getCostType());
				List<ContractTermsChild> childListr = contractTermsChildMapper.selectByExample(example1);
				ma.put(childList.get(i).getCostType(), childListr);
			}
		}
		// 判断公司类型
		Map<String, String> costTypeMap = getCostNames(roleId);

		List<ContractCostVo> resList = new ArrayList<>();

		if ("SJ".equals(roleId)) {
			for (String key : costTypeMap.keySet()) {
				if (key.toString().equals("01")) {// 平台服务
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c08")));
					resList.add(costVo0);
				} else if (key.toString().equals("02")) {
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c11")));
					costVo0.setList(ma.get(key));
					resList.add(costVo0);
				} else if (key.toString().equals("03")) {
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c12")));
					resList.add(costVo0);
					costVo0.setList(ma.get(key));
				} else if (key.toString().equals("04")) {
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c15")));
					costVo0.setList(ma.get(key));
				}
			}

		} else if ("DB".equals(roleId)) {

			for (String key : costTypeMap.keySet()) {
				if (key.toString().equals("01")) {// 平台服务
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setList(ma.get(key));
					resList.add(costVo0);
				} else if (key.toString().equals("02")) {
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c11")));
					resList.add(costVo0);
				} else if (key.toString().equals("03")) {// 结算比例
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c12")));
					resList.add(costVo0);
					costVo0.setList(ma.get(key));
				} else if (key.toString().equals("04")) {
					ContractCostVo costVo0 = new ContractCostVo();
					costVo0.setCostCode(key.toString());
					costVo0.setCostName(costTypeMap.get(key));
					costVo0.setCostValue(String.valueOf(dbmap.get("c15")));
					costVo0.setList(ma.get(key));
				}
			}
		}

		return resList;
	}

//String sr [] = {"平台服务费-01","产品服务费-02","施工管理费-03","设计费-04","保证金-04"};
	@Override
	public Map<String,String> getCostNames(String type) {
		Map<String,String> resMap = new HashMap<>();
		resMap.put("01", "设计费");
		resMap.put("02", "施工费");
		resMap.put("03", "施工平台管理服务费");
		resMap.put("04", "设计平台管理服务费");
		resMap.put("05", "产品服务费");
		resMap.put("06", "租金");
		resMap.put("07", "物业费");
		resMap.put("08", "其他收费");
		resMap.put("09", "材料推荐服务费");
		resMap.put("10", "施工服务费");
		resMap.put("11", "先行赔付款");
		resMap.put("12", "客户赔偿款");
		resMap.put("13", "合同保证金");
		resMap.put("14", "入驻费");
		return resMap;
	}

	/**
	 * 查询订单同步数据
	 *
	 * @param contractID
	 * @return
	 */
	@Override
	public Optional<SyncOrderVO> selectSyncDateByOrder(String contractID) {
		ContractInfo contractInfo = contractInfoMapper.selectOneByExample(contractID);
		SyncOrderVO result = new SyncOrderVO();

		if(contractInfo != null){
			result.setSignedTime(contractInfo.getSignedTime() != null ? contractInfo.getSignedTime().toInstant().toString() : "");
			result.setStartTime(contractInfo.getStartTime() != null ? contractInfo.getStartTime().toInstant().toString() : "");
			result.setEndTime(contractInfo.getEndTime() != null ? contractInfo.getEndTime().toInstant().toString() : "");

		}

		CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contractInfo.getCompanyId());
		if(companyInfo != null){
			result.setCompanyId(companyInfo.getCompanyId());
			result.setCompanyName(companyInfo.getCompanyName());

		}

		result.setType(SyncOrderEnum.Type.Contract.code.toString());
		result.setTypeSub(SyncOrderEnum.SubType.deposit.code.toString());
		result.setContractType(SyncOrderEnum.ContractType.Full.code.toString());


		return Optional.ofNullable(result);
	}


	/**
	 * 获取远程推送信息
	 *
	 * @param contractNumber
	 * @return
	 */
	@Override
	public Optional<SyncContractVO> selectSyncDateByContractNumber(String contractNumber) {

		ContractInfo contractInfo = contractInfoMapper.selectOneByExample(contractNumber);
		SyncContractVO result = new SyncContractVO();
		result.setHth(contractNumber);
		result.setHtyxq_START(contractInfo.getStartTime()!= null? contractInfo.getStartTime().toInstant().toString():"");
		result.setHtyxq_END(contractInfo.getEndTime()!= null? contractInfo.getEndTime().toInstant().toString():"");

		PcCompanyFinancialExample pcCompanyFinancialExample = new PcCompanyFinancialExample();
		pcCompanyFinancialExample.createCriteria().andCompanyIdEqualTo(contractInfo.getCompanyId());
		List<PcCompanyFinancial> companyFinancialList = pcCompanyFinancialMapper.selectByExample(pcCompanyFinancialExample);

		// 银行信息
		if(companyFinancialList.stream().filter(f-> CompanyFinancialType.BANK.shortVal().equals(f.getAccountType())).findFirst().isPresent()){
			PcCompanyFinancial pcCompanyFinancial = companyFinancialList.stream()
					.filter(f-> CompanyFinancialType.BANK.shortVal().equals(f.getAccountType())).findFirst().get();
			result.setFkyhzh(pcCompanyFinancial.getAccountNumber());
			result.setFkkhh(pcCompanyFinancial.getAccountName());
			result.setFkyhhm(pcCompanyFinancial.getCardName());
		}
		// 居然金融
		if(companyFinancialList.stream().filter(f-> CompanyFinancialType.JURAN.shortVal().equals(f.getAccountType())).findFirst().isPresent()){
			PcCompanyFinancial pcCompanyFinancial = companyFinancialList.stream()
					.filter(f-> CompanyFinancialType.JURAN.shortVal().equals(f.getAccountType())).findFirst().get();
			result.setJrdzzh(pcCompanyFinancial.getAccountNumber());
		}
		CompanyInfoExample companyInfoExample = new CompanyInfoExample();
		companyInfoExample.createCriteria().andCompanyIdEqualTo(contractInfo.getCompanyId());
		List<CompanyInfo> companyInfoList = companyInfoMapper.selectByExample(companyInfoExample);
		if(!companyInfoList.isEmpty() || companyInfoList.size() == 1){
			CompanyInfo companyInfo = companyInfoList.get(0);
			// 编码
			result.setGhdwdm(companyInfo.getCompanyId());
			// 名称
			result.setGhdwmc(companyInfo.getCompanyName());
			result.setProvince(provinceMapper.convertCodeToName(companyInfo.getProvinceCode()));
			result.setCity(cityMapper.convertCodeToName(companyInfo.getCityCode()));
			result.setFddm(companyInfo.getSiteCompanyId());
			String code = businessEntityService.getBusinessEbsIdByCompanyId(companyInfo.getCompanyId());
			if(StringUtils.isNotBlank(code)){
				try{
					result.setGsdm( Integer.valueOf(code));
				}catch (Exception e){
					printErrorMes(e.getMessage());
				}
			}
//			result.setGsdm(105);
		}
		// 10 有效 20无效
		result.setStatus("10");

		return Optional.of(result);
	}

    @Transactional
	@Override
	public boolean examineOrderContract(String orderNumber, String status, String cause) {
		
		try {
			OrderContract record = new OrderContract();
			record.setAuditType(status);
			OrderContractExample example = new OrderContractExample();
			example.createCriteria().andOrderNumberEqualTo(orderNumber);
			// 查询合同
			List<OrderContract> list = orderContractMapper.selectByExample(example);
			if (list == null || list.size() == 0) {
				throw new RuntimeException("无法找到此订单合同信息");
			}
			if (status.equals("1")) {// 通过
				OrderContract contract = list.get(0);
				if (contract.getContractType().equals("02")) {// 设计合同
					// 查询合同是全款换是分期
					ContractTermsExample example1 = new ContractTermsExample();
					example1.createCriteria().andCompanyIdEqualTo(contract.getCompanyId())
							.andContractNumberEqualTo(contract.getContractNumber());
					List<ContractTerms> childListr = pcContractTermsMapper.selectByExample(example1);
					Map<String, String> chMap = new HashMap<>();

					for (int i = 0; i < childListr.size(); i++) {
						chMap.put(childListr.get(i).getContractDictCode(), childListr.get(i).getContractValue());
					}
					// 1全款合同，2分期合同
					int type = 1;
					if (String.valueOf(chMap.get("c18")).equals("1")) {// 分期
						type = 2;
					}
					printInfoMes("合同审批调用 订单接口 orderNo{},type{}", orderNumber, type);
					designDispatchService.reviewPass(orderNumber, type);

				} else {// 施工合同
					printInfoMes("合同审批调用 订单接口 orderNo{}}", orderNumber);
					constructionStateService.contractCompleteState(orderNumber);
				}
				record.setSignTime(new Date());// 插入时间
				printInfoMes("合同审批调用 生成订单orderNumber{}}", orderNumber);
				List<SyncOrderVO> listvo = thirdPartDateService.getOrderContract(orderNumber);
				//for (int i = 0; i < listvo.size(); i++) {
				CreateOrder order = new CreateOrder();
				order.setData(listvo);
				eventService.publish(order);
				//}
			} else {// 拒绝 插入拒绝原因
				// 查询合同编号
				UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
				String auditPersion = userVO == null ? "" : userVO.getName();
				String auditAccount = userVO == null ? "" : userVO.getUsername();
				/* 添加审核记录表 */
				PcAuditInfo te = new PcAuditInfo("2", "1", auditPersion, status, new Date(), list.get(0).getCompanyId(),
						cause, list.get(0).getContractNumber(), new Date(), auditAccount);
				pcAuditInfoMapper.insertSelective(te);

			}
			orderContractMapper.updateByExampleSelective(record, example);
		} catch (Exception e) {
			e.getStackTrace();
            printErrorMes("订单编号  orderNumber 合同审核程序异常{}",e.getMessage());
		}
		return false;
	}


	@Override
	public List<PcAuditInfo> getAuditInfoList(String orderNumber) {
		OrderContractExample example = new OrderContractExample();
		example.createCriteria().andOrderNumberEqualTo(orderNumber);
		// 查询合同
		List<OrderContract> list = orderContractMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			throw new RuntimeException("无法找到此订单合同信息");
		}
		//查询不通过的
		PcAuditInfoExample auexample =new PcAuditInfoExample();
		auexample.createCriteria().andCompanyIdEqualTo(list.get(0).getCompanyId())
		.andContractNumberEqualTo(list.get(0).getContractNumber()).andAuditStatusEqualTo("0");
		List<PcAuditInfo> aulist = pcAuditInfoMapper.selectByExample(auexample);
		return aulist;
	}
}
