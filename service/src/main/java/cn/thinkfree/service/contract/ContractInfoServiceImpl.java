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
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.constants.SyncOrderEnum;
import cn.thinkfree.database.mapper.CityMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.CompanyPaymentMapper;
import cn.thinkfree.database.mapper.ContractInfoMapper;
import cn.thinkfree.database.mapper.ContractTermsChildMapper;
import cn.thinkfree.database.mapper.ContractTermsMapper;
import cn.thinkfree.database.mapper.MyContractInfoMapper;
import cn.thinkfree.database.mapper.OrderContractMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.mapper.ProvinceMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import cn.thinkfree.database.model.CompanyPayment;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.ContractInfoExample;
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
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.database.vo.ContractClauseVO;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.contract.ContractCostVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.service.companyapply.CompanyApplyService;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.CompanyConstants;
import cn.thinkfree.service.constants.CompanyFinancialType;
import cn.thinkfree.service.constants.CompanyType;
import cn.thinkfree.service.constants.ContractStatus;
import cn.thinkfree.service.utils.CommonGroupUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
import cn.thinkfree.service.utils.FreemarkerUtils;
import cn.thinkfree.service.utils.PdfUplodUtils;

@Service
public class ContractInfoServiceImpl extends AbsLogPrinter implements ContractService {
	@Autowired
	MyContractInfoMapper contractInfoMapper;

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


	@Value( "${custom.cloud.fileUpload}" )
	private String fileUploadUrl;

	@Value( "${custom.cloud.fileUpload.dir}" )
	private String filePathDir;





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
		ExcelData	data	= new ExcelData();
		String		title	= "合同信息数据";
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
			list.get( i ).setContractStatus( ContractStatus.getDesc( list.get( i ).getContractStatus() ) );
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
		SimpleDateFormat	fdate		= new SimpleDateFormat( "yyyy-MM-dd-HHmmss" );
		String			fileName	= title + "_" + fdate.format( new Date() ) + ".xls";
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
	public boolean auditContract( String contractNumber, String companyId, String auditStatus, String auditCase )
	{
		/* 修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝 */
		ContractVo vo = new ContractVo();
		vo.setCompanyId( companyId );
		vo.setContractNumber( contractNumber );
		if ( auditStatus.equals( AuditStatus.AuditPass.shortVal() ) ) /*  */
		{
			vo.setContractStatus( ContractStatus.AuditPass.shortVal() );
		} else {
			vo.setContractStatus( ContractStatus.AuditDecline.shortVal() );
		}
		/* 修改公司表 */
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyId( companyId );

		if ( auditStatus.equals( AuditStatus.AuditPass.shortVal() ) )   /* 财务审核通过 */
		{
			companyInfo.setAuditStatus( CompanyAuditStatus.SUCCESSCHECK.stringVal() );
			/* 财务审核通过生成合同 */
			String pdfUrl = this.createContractDoc( contractNumber );
			vo.setContractUrl( pdfUrl );
			vo.setSignedTime(DateUtil.formartDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		} else {                                                        /* 财务审核不通过 */
			companyInfo.setAuditStatus( CompanyAuditStatus.FAILCHECK.stringVal() );
		}
		ContractInfoExample example = new ContractInfoExample();
		example.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
		ContractInfo contractInfo = new ContractInfo();
		contractInfo.setContractStatus( vo.getContractStatus() );
		contractInfo.setContractUrl( vo.getContractUrl() );
		int flag = cxcontractInfoMapper.updateByExampleSelective( contractInfo, example );
		int flagT = companyInfoMapper.updateauditStatus( companyInfo );
		UserVO	userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
		String	auditPersion = userVO == null ? "" : userVO.getUsername();
		/* 添加审核记录表 */
		PcAuditInfo record = new PcAuditInfo( "1", "2", auditPersion, auditStatus, new Date(), companyId, auditCase,
						      contractNumber );
		int flagon = pcAuditInfoMapper.insertSelective( record );

		if ( flag > 0 && flagT > 0 && flagon > 0 )
		{
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
		/* 修改公司表 */
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyId( companyId );
		companyInfo.setAuditStatus(  CompanyAuditStatus.SUCCESSJOIN.stringVal()  ); /* 确认已交保证金 */
		int flag = companyInfoMapper.updateauditStatus( companyInfo );
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


	private List<Map<String, Object> > getContractInfo( String contractNumber, ContractVo newVo,
							    CompanySubmitVo companyInfo )
	{
		List<Map<String, Object> > resList = new ArrayList<>();

		String			ownerCompanyName	= "居然之家";                                               /* 甲方公司名称 */
		String			secondCompanyName	= companyInfo.getCompanyInfo().getCompanyName();        /* 乙方公司名称 */
		SimpleDateFormat	sdf			= new SimpleDateFormat( "yyyy-MM-dd" );
		DateFormat		format1			= new SimpleDateFormat( "yyyy-MM-dd" );
		String			formatstartYear[]	= null;
		String			formatendYear[]		= null;
		try {
			formatstartYear = sdf.format( format1.parse( newVo.getStartTime() ) ).split( "-" );
			formatendYear	= sdf.format( format1.parse( newVo.getEndTime() ) ).split( "-" );
		} catch ( ParseException e ) {
			e.printStackTrace();
		}
		String	startYear	= formatstartYear[0];                           /* 合同开始年 */
		String	startmonth	= formatstartYear[1];                           /* 合同开始年 */
		String	startsun	= formatstartYear[2];                           /* 合同开始年 */
		String	endYear		= formatendYear[0];                             /* 合同结束年 */
		String	endtmonth	= formatendYear[1];                             /* 合同结束月 */
		String	endsun		= formatendYear[2];                             /* 合同结束日 */
		/* 开户户名 */
		PcCompanyFinancial accountinfo = pcCompanyFinancialMapper
						 .findPcCompanyFinancialByCompanyId( newVo.getCompanyId() );
		String	cardName		= accountinfo.getCardName();            /* 开户行名称 */
		String	accounBranchName	= accountinfo.getAccountBranchName();   /* 开户银行名称 */
		String	accountNumber		= accountinfo.getAccountNumber() + "";  /* 银行卡卡号 */

		Map<String, Object> rmap = balanceInfo( contractNumber, newVo.getCompanyId(),
							companyInfo.getCompanyInfo().getRoleId() );

		Map<String, Object> rep = new HashMap<>();
		rep.put( "ownerCompanyName", ownerCompanyName );
		rep.put( "secondCompanyName", secondCompanyName );
		rep.put( "startYear", startYear );
		rep.put( "startmonth", startmonth );
		rep.put( "startsun", startsun );
		rep.put( "endYear", endYear );
		rep.put( "endtmonth", endtmonth );
		rep.put( "endsun", endsun );
		rep.put( "cardName", cardName );
		rep.put( "accounBranchName", accounBranchName );
		rep.put( "accountNumber", accountNumber );

		resList.add( rmap ); /* 结算比例 */

		return(resList);
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
		String	url	= "";                                                                   /* 上传服务器返回地址 */
		SimpleDateFormat sdf	= new SimpleDateFormat( "yyyy-MM-dd" );
		Map<String, Object>	root = new HashMap<>();                                                      /* data数据 */

		ContractVo vo = new ContractVo();
		vo.setContractNumber( contractNumber );
		ContractVo	newVo		= contractInfoMapper.selectContractBycontractNumber( vo );              /* 合同信息 */
		CompanySubmitVo companyInfo	= companySubmitService.findCompanyInfo( (newVo.getCompanyId() ) );      /* 公司信息 */
		/* 合同详情 */
		String			companyId	= newVo.getCompanyId();
		ContractTermsExample	exp		= new ContractTermsExample();
		exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );
		List<ContractTerms> list = pcContractTermsMapper.selectByExample( exp );
		for ( int i = 0; i < list.size(); i++ )
		{
			root.put( list.get( i ).getContractDictCode(), list.get( i ).getContractValue() );
		}
		/* 查询结算规则 */
		ContractTermsChildExample example = new ContractTermsChildExample();
		example.createCriteria().andCompanyIdEqualTo( (newVo.getCompanyId() ) ).andContractNumberEqualTo( contractNumber );
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
				root.put( childList.get( i ).getCostType(), childListr );
			}
		}
		/* 判断公司类型 */
		if ( companyInfo.getCompanyInfo().getRoleId().equals( "BD" ) ) /* 装修公司 */

		{
			try {
				String filePath = FreemarkerUtils.savePdf(filePathDir+contractNumber, "1", root );
				/* 上传 */
				url = PdfUplodUtils.upload( filePath, fileUploadUrl );
			} catch (Exception e) {
				e.printStackTrace();
				printErrorMes("生成pdf合同发生错误", e.getMessage());
			}

		} else if ( companyInfo.getCompanyInfo().getRoleId().equals( "SJ" ) )/* 设计公司 */

		{
			try {
				String filePath = FreemarkerUtils.savePdf(filePathDir+contractNumber, "0", root );
				/* 上传 */
				url = PdfUplodUtils.upload( filePath, fileUploadUrl );
			} catch (Exception e) {
				e.printStackTrace();
				printErrorMes("生成pdf合同发生错误", e.getMessage());
			}
		}

		return  url;
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
		if ( roleType.equals( "SJ" ) ) /* 设计公司 */

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
		} else if ( roleType.equals( "BD" ) ) /* 装饰公司 */

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
		return(rmap);
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
			while ( childentries.hasNext() )
			{
				Entry<String, List<ContractTermsChild> >	entry	= childentries.next();
				String						key	= entry.getKey();
				List<ContractTermsChild>			list	= entry.getValue();
				for ( int i = 0; i < list.size(); i++ )
				{
					ContractTermsChild		child	= list.get( i );
					ContractTermsChildExample	exp	= new ContractTermsChildExample();
					exp.createCriteria().andCompanyIdEqualTo( companyId ).andCostTypeEqualTo( key )
					.andContractNumberEqualTo( contractNumber );
					contractTermsChildMapper.deleteByExample( exp );        /* 删除重复数据 */
					contractTermsChildMapper.insertSelective( child );      /* 新增合同规则 */
				}
			}
		}

		//修改合同状态
		ContractInfo record = new ContractInfo();
		record.setContractStatus(ContractStatus.WaitAudit.shortVal());
		record.setCompanyId(companyId);
		if(StringUtils.isNotBlank(contractClausevo.getParamMap().get("c03"))){
		      Date startTime = DateUtil.formateToDate(contractClausevo.getParamMap().get("c03"), "yyyy-mm-dd");
		      record.setStartTime(startTime);
		    }else{
		      return false;
		    }
		    if(StringUtils.isNotBlank(contractClausevo.getParamMap().get("c04"))){
		      Date endTime = DateUtil.formateToDate(contractClausevo.getParamMap().get("c04"), "yyyy-mm-dd");
		      record.setEndTime(endTime);
		    }else{
		      return false;
		    }
		record.setContractNumber(contractNumber);
		ContractInfoExample example = new ContractInfoExample();
		example.createCriteria().andCompanyIdEqualTo(companyId).
		andContractNumberEqualTo(contractNumber);
		cxcontractInfoMapper.updateByExampleSelective(record, example);
		//修改公司状态
		companyApplyService.updateStatus(companyId, CompanyAuditStatus.CHECKING.stringVal());
	} catch (Exception e) {
		printErrorMes("设置合同条款服务异常 {}"+ e.getMessage());
		return false;
	}
		return  true;
	}


	@Override
	public Map<String, String> getContractBycontractNumber( String contractNumber )
	{
		return(null);
	}


	@Override
	public Map<String, Object> getContractDetailInfo( String contractNumber, String companyId )
	{
		Map<String, Object> reMap = new HashMap<>();
		/* 公司详情 */
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo( companyId );

		/* 合同详情 */
		ContractTermsExample exp = new ContractTermsExample();
		/* 判断公司类型 */
		exp.createCriteria().andCompanyIdEqualTo( companyId ).andContractNumberEqualTo( contractNumber );

		List<ContractTerms> list = pcContractTermsMapper.selectByExample( exp );

		if ( list.size() == 0 && companyInfo != null )
		{
			ContractTerms term_0 = new ContractTerms( "01", "居然设计家" );
			list.add( term_0 );
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
		/* record.setConractUrlPdf(url); */
		orderContractMapper.insertSelective( record );
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

	@Transactional
	@Override
	public boolean createOrderContract(String orderNumber) {
		try {
			// 订单合同
			OrderContractExample record = new OrderContractExample();
			record.createCriteria().andOrderNumberEqualTo(orderNumber);
			List<OrderContract> list = orderContractMapper.selectByExample(record);
			OrderContract contrat = list.get(0);

			Map<String, Object> root = new HashMap<>();
			// 查询合同信息
			ContractTermsExample exp = new ContractTermsExample();
			exp.createCriteria().andContractNumberEqualTo(orderNumber);
			List<ContractTerms> listTerm = pcContractTermsMapper.selectByExample(exp);
			for (int i = 0; i < list.size(); i++) {
				root.put(listTerm.get(i).getContractDictCode(), listTerm.get(i).getContractValue());
			}

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


	@Transactional
	@Override
	public boolean insertDesignOrderContract( String orderNumber, String companyId, Map<String, String> paramMap )
	{
		try {
			/* 插入合同主表 */
			String contractNumber = this.createOrderContract( companyId, orderNumber, "02" );
			/* 插入合同iterm 详情 */
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
				}
			}
			return true;
		} catch ( Exception e ) {
			
			return false;
		}
	}


	@Transactional
	@Override
	public boolean insertRoadWorkOrderContract( String orderNumber, String companyId, Map<String, String> paramMap )
	{
		try {
			/* 插入合同主表 */
			String contractNumber = this.createOrderContract( companyId, orderNumber, "03" );
			/* 插入合同iterm 详情 */
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
				}
			}
			return(true);
		} catch ( Exception e ) {
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
	public List<ContractCostVo> queryListContractCostVoBycontractNumber(String contractNumber) {

		// 更加合同编号查询入住公司合同类型
		ContractVo vo = new ContractVo();
		vo.setContractNumber(contractNumber);
		ContractVo newVo = contractInfoMapper.selectContractBycontractNumber(vo); /* 合同信息 */
		CompanySubmitVo companyInfo = companySubmitService.findCompanyInfo(newVo.getCompanyId()); /* 公司信息 */
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
		CommonGroupUtils.listGroup2Map(childList, map2, ContractTermsChild.class, "cost_type"); /* 根据类型分组 */
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
		Map<String, String> costTypeMap = getCostNames(companyInfo.getCompanyInfo().getRoleId());

		List<ContractCostVo> resList = new ArrayList<>();

		if (companyInfo.getCompanyInfo().getRoleId().equals("BD")) {
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

		} else if (companyInfo.getCompanyInfo().getRoleId().equals("DB")) {

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

		if(type.equals(CompanyType.BD.name())){
			resMap.put("01", "平台服务费");
			resMap.put("02", "产品服务费");
			resMap.put("03", "施工管理费");
			resMap.put("04", "设计费");
			resMap.put("05", "保证金");
//			resMap.put("06", "一次性保证金");
//			resMap.put("07", "尾款比例");
		}else if(type.equals(CompanyType.BD.name())){
			resMap.put("01", "平台服务费");
			resMap.put("02", "结算周期");
			resMap.put("03", "结算比例");;
			resMap.put("04", "保证金");
//			resMap.put("06", "一次性保证金");
//			resMap.put("07", "尾款比例");
		}

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
			// TODO 需要埃森哲数据 经营主体编码
			result.setGsdm(105);
		}
		// 10 有效 20无效
		result.setStatus("10");

		return Optional.of(result);
	}
}
