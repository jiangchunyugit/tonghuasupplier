package cn.thinkfree.service.pcthirdpartdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.ContractInfoMapper;
import cn.thinkfree.database.mapper.ContractTermsChildMapper;
import cn.thinkfree.database.mapper.ContractTermsMapper;
import cn.thinkfree.database.mapper.DesignerOrderMapper;
import cn.thinkfree.database.mapper.OrderContractMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.ContractInfoExample;
import cn.thinkfree.database.model.ContractTerms;
import cn.thinkfree.database.model.ContractTermsChild;
import cn.thinkfree.database.model.ContractTermsChildExample;
import cn.thinkfree.database.model.ContractTermsExample;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.DesignerOrderExample;
import cn.thinkfree.database.model.OrderContract;
import cn.thinkfree.database.model.OrderContractExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.vo.MarginContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.service.constants.CompanyConstants;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.utils.DateUtil;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 第三方数据接口
 */
@Service
public class ThirdPartDateServiceImpl extends AbsLogPrinter implements ThirdPartDateService {

    @Autowired
    ContractInfoMapper contractInfoMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Value("${optionType}")
    private String type;

    @Autowired
    OrderContractMapper orderContractMapper;

    @Autowired
    ContractTermsMapper contractTermsMapper;

    @Autowired
    ConstructionOrderMapper  construtionOrderMapper;

    @Autowired
    DesignerOrderMapper designerOrderMapper;

    @Autowired
    NewSchedulingService newSchedulingService;

    @Autowired
	ContractTermsChildMapper contractTermsChildMapper;




    @Override
    public MarginContractVO getMarginContract(String contractNumber,String signedTime) {


        MarginContractVO marginContractVO = new MarginContractVO();
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        contractInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
        List<ContractInfo> contractInfos = contractInfoMapper.selectByExample(contractInfoExample);

        if (contractInfos.size() > 0) {

            // 合同信息
            ContractInfo contractInfo = contractInfos.get(0);
            marginContractVO.setOptionType(type);
            marginContractVO.setContractNumber(contractInfo.getContractNumber());
            marginContractVO.setTransactionDate(String.valueOf(contractInfo.getSignedTime()));
            marginContractVO.setVendorId(contractInfo.getCompanyId());

            // 公司信息
            if (StringUtils.isNotBlank(contractInfo.getCompanyId())) {

//                CompanyInfoExample companyInfoExample = new CompanyInfoExample();
//                companyInfoExample.createCriteria().andCompanyIdEqualTo(contractInfo.getCompanyId());
                CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contractInfo.getCompanyId());

                if (companyInfo != null) {
                    marginContractVO.setOperatingUnit(companyInfo.getSiteCompanyId());
                    marginContractVO.setVendorName(companyInfo.getCompanyName());
                }
            }

            // 审批人信息
            PcAuditInfoExample pcAuditInfoExample = new PcAuditInfoExample();
            pcAuditInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
            List<PcAuditInfo> pcAuditInfos = pcAuditInfoMapper.selectByExample(pcAuditInfoExample);
            if (pcAuditInfos.size() > 0) {

                PcAuditInfo pcAuditInfo = pcAuditInfos.get(0);
                marginContractVO.setOperationPerson(pcAuditInfo.getAuditPersion());
            }

            return marginContractVO;
        }
        return null;
    }
    
    
    

	@Override
	public List<SyncOrderVO> getOrderContract(String orderNumber) {

		List<SyncOrderVO> listVo = new ArrayList<>();
		OrderContractExample contractInfoExample = new OrderContractExample();
		contractInfoExample.createCriteria().andOrderNumberEqualTo(orderNumber);
		List<OrderContract> contractInfos = orderContractMapper.selectByExample(contractInfoExample);
		if (contractInfos != null && contractInfos.size() == 1) {

			OrderContract contract = contractInfos.get(0);

			CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contract.getCompanyId());

			if (contract != null) {
				// 初始化合同详情
				ContractTermsExample terms = new ContractTermsExample();
				terms.createCriteria().andCompanyIdEqualTo(contract.getCompanyId())
						.andContractNumberEqualTo(contract.getContractNumber());
				List<ContractTerms> listTerms = contractTermsMapper.selectByExample(terms);
				Map<String, String> resMap = new HashMap<>();
				for (int i = 0; i < listTerms.size(); i++) {
					resMap.put(listTerms.get(i).getContractDictCode(), listTerms.get(i).getContractValue());
				}
				// 判断当前合同类型 02设计合同_to_c 03施工合同_to_c
				if (contract.getContractType().equals("02")) {
					// 设计合同数据拼接
					this.designData(orderNumber, listVo, contract, companyInfo, resMap);

				} else {
					// 施工订单
					roadWorkData(orderNumber, listVo, contract, companyInfo, resMap);
				}
			}

		} else {
			printInfoMes("调用订单合同发生错误 ", "contractInfos is null or contractInfos.size() > 0 {}",
					contractInfos.toString());
			return null;

		}

		return listVo;
	}

   /**
    * 
    * 施工合同数据
    * @param orderNumber
    * @param listVo
    * @param contract
    * @param companyInfo
    * @param resMap
    */
	private void roadWorkData(String orderNumber, List<SyncOrderVO> listVo, OrderContract contract,
			CompanyInfo companyInfo, Map<String, String> resMap) {
		ConstructionOrderExample example = new ConstructionOrderExample();
		  example.createCriteria().andOrderNoEqualTo(orderNumber);
		  List<ConstructionOrder> conorder =  construtionOrderMapper.selectByExample(example);
		//分期 根据分期json循环数据  [{'sortNumber':'0','name':'设计3d方案','ratio': '30','costValue': '200000'},{'sortNumber': '1','name':'设计3d方案2','ratio': '40','costValue': '2222222222'}]
		 // String jsonSr = "[{'sortNumber':'0','name':'设计3d方案','ratio': '30','costValue': '200000'},{'sortNumber': '1','name':'设计3d方案2','ratio': '40','costValue': '2222222222'}]";
		  String jsonSr = resMap.get("c08");
		  if(!StringUtils.isEmpty(jsonSr)){
				  JSONArray jsonArray=JSONArray.parseArray(jsonSr);
				  for (int i = 0; i < jsonArray.size(); i++) {
					  SyncOrderVO vo = new SyncOrderVO();
					  //合同金额 全款
					  @SuppressWarnings("unchecked")
					  Map<String,String> jsonMap = (Map<String, String>) jsonArray.get(i);
					  vo.setActualAmount(jsonMap.get("payMoney"));//支付金额
					  vo.setCompanyId(contract.getCompanyId());
					  //公司名称
					  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
					  //支付名称jsonMap.get("progressName")
					  vo.setTypeSub("200"+(i+1)+"");
					  //是否全额支付
					  vo.setContractType("2");
					  //业主名称
					  vo.setConsumerName(resMap.get("c03"));
					  //合同开始时间
					  vo.setStartTime("");
					  //合同结束时间
					  vo.setEndTime("");
					  //订单编号
					  vo.setFromOrderid(orderNumber);
					  //是否全额支付
					 // if(i == 0){
//					  vo.setIsEnd("1");
//					  }else{
						  if(i == jsonArray.size()-1 ){
							  vo.setIsEnd("1");
						  }else{
							  vo.setIsEnd("2");
						  }
//					  }
					  //合同类型 订单类型：设计1、施工2、合同3
					  vo.setType("1");

					  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
					  //项目编号
					  vo.setProjectNo(conorder ==null?"":conorder.get(0).getProjectNo());
					  //签约时间
					  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
					  //是否个性化
					  vo.setStyleType(conorder ==null?"":conorder.get(0).getType());

					  vo.setSort(""+(i+1));

		              listVo.add(vo);
				  }
		 
	      } 
		else
		{
			  printInfoMes("调用订单合同发生错误 ","contractInfos is null or contractInfos.size() > 0 {}",jsonSr.toString());
		 }
		  
	}

	
	/**
	 * 
	 * 设计订单拼接数据
	 * @param orderNumber
	 * @param listVo
	 * @param contract
	 * @param companyInfo
	 * @param resMap
	 */
	private void designData(String orderNumber, List<SyncOrderVO> listVo, OrderContract contract,
			CompanyInfo companyInfo, Map<String, String> resMap) {
		//查询设计订单项目信息
		  DesignerOrderExample example = new DesignerOrderExample();
		  example.createCriteria().andOrderNoEqualTo(orderNumber);
		  List<DesignerOrder> conorder =  designerOrderMapper.selectByExample(example);

		  //设计判断是分期 换是全款

		  String ctype = String.valueOf(resMap.get("c18"));
		  if(ctype.equals("1")){//全款
			  SyncOrderVO vo = new SyncOrderVO();
			  //合同金额 全款
			  vo.setActualAmount(String.valueOf(resMap.get("c19")));
			  vo.setCompanyId(contract.getCompanyId());
			  //公司名称
			  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
			  //支付名称
			  vo.setTypeSub("1001");
			  //是否全额支付
			  vo.setContractType("1");
			  //业主名称
			  vo.setConsumerName(resMap.get("c03"));
			  //合同开始时间
			  vo.setStartTime("");
			  //合同结束时间
			  vo.setEndTime("");
			  //订单编号
			  vo.setFromOrderid(orderNumber);
			  //是否全额支付
			  vo.setIsEnd("2");
			  //合同类型 订单类型：设计1、施工2、合同3
			  vo.setType("1");
			  //项目地址
			  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
			  //项目编号
			  vo.setProjectNo(conorder.get(0)==null?"":conorder.get(0).getProjectNo());
			  //签约时间
			  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
			  //是否个性化
			  vo.setStyleType(conorder.get(0)==null?"":conorder.get(0).getStyleType());

			  vo.setSort("");

		      listVo.add(vo);
		  }else{//分期 根据分期json循环数据  [{'sortNumber':'0','name':'设计3d方案','ratio': '30','costValue': '200000'},{'sortNumber': '1','name':'设计3d方案2','ratio': '40','costValue': '2222222222'}]
			// String jsonSr = "[{'sortNumber':'0','name':'设计3d方案','ratio': '30','costValue': '200000'},{'sortNumber': '1','name':'设计3d方案2','ratio': '40','costValue': '2222222222'}]";
			 String jsonSr = resMap.get("c20");
			  if(!StringUtils.isEmpty(jsonSr)){
				  JSONArray jsonArray=JSONArray.parseArray(jsonSr);
				  for (int i = 0; i < jsonArray.size(); i++) {
					  SyncOrderVO vo = new SyncOrderVO();
					  //合同金额 全款
					  @SuppressWarnings("unchecked")
					  Map<String,String> jsonMap = (Map<String, String>) jsonArray.get(i);
					  vo.setActualAmount(jsonMap.get("costValue"));
					  vo.setCompanyId(contract.getCompanyId());
					  //公司名称
					  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
					  //支付名称
					  vo.setTypeSub("100"+(i+1)+"");
					  //是否全额支付
					  vo.setContractType("2");
					  //业主名称
					  vo.setConsumerName(resMap.get("c03"));
					  //合同开始时间
					  vo.setStartTime("");
					  //合同结束时间
					  vo.setEndTime("");
					  //订单编号
					  vo.setFromOrderid(orderNumber);
					  //是否全额支付
//					  if(i == 0){
//					  vo.setIsEnd("1");
//						  }else{
//							  if(i == jsonArray.size()-1 ){
//								  vo.setIsEnd("2");
//							  }else{
//								  vo.setIsEnd("0");
//							  }
//					  }
					  if(i == jsonArray.size() - 1 ){
						  vo.setIsEnd("2");
					  }else{
						  vo.setIsEnd("1");
					  }
					  //合同类型 订单类型：设计1、施工2、合同3
					  vo.setType("1");

					  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
					  //项目编号
					  vo.setProjectNo(conorder==null?"":conorder.get(0).getProjectNo());
					  //签约时间
					  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
					  //是否个性化
					  vo.setStyleType(conorder==null?"":conorder.get(0).getStyleType());

					  vo.setSort(String.valueOf(jsonMap.get("sortNumber")));

		              listVo.add(vo);
				  }
			  }else{
				  printInfoMes("调用订单合同发生错误 ","contractInfos is null or contractInfos.size() > 0 {}",jsonSr.toString());
			  }
		  }
	}
	
	

	@Override
	public List<SyncOrderVO> getOrderContractToB(String contractNumber) {
		List<SyncOrderVO> listVo = new ArrayList<>();
		//根据合同编号 查询 入住合同
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        contractInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
        List<ContractInfo> contractInfos = contractInfoMapper.selectByExample(contractInfoExample);
        if (contractInfos.size() > 0){
        	ContractInfo contract = contractInfos.get(0);
        	CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contract.getCompanyId());
        	//合同信息
        	ContractTermsExample contractTermsExample = new ContractTermsExample();
			contractTermsExample.createCriteria()
			.andCompanyIdEqualTo(companyInfo.getCompanyId()).andContractNumberEqualTo(contractNumber);
			 List<ContractTerms> list = contractTermsMapper.selectByExample( contractTermsExample );
		    Map<String,String> resMap = new HashMap<>();
		    if(list != null){
			    for (int i = 0; i < list.size(); i++) {
			    	resMap.put(list.get(i).getContractDictCode(), list.get(i).getContractValue());
			    }
			}
        	/*保证金分期*/
    		ContractTermsChildExample example = new ContractTermsChildExample();
    		example.createCriteria().
    		andCompanyIdEqualTo((companyInfo.getCompanyId())).
    		andContractNumberEqualTo(contractNumber).andCostTypeEqualTo("13");
    		example.setOrderByClause(" c_type asc");
    		List<ContractTermsChild> childList = contractTermsChildMapper.selectByExample(example);
    		if(childList == null || childList.size() > 2){
    			throw new RuntimeException("入住合同"+contractNumber+"{}设置保证金金额数据错误");
    		}
        	if(companyInfo.getRoleId().equals(CompanyConstants.RoleType.SJ.code)){
        		//设计
        		 this.designDataToB(contractNumber, listVo, contract, companyInfo, resMap, childList);
        		  
        	}else{
        		//施工
        		this.roadWorkDataToB(contractNumber, listVo, contract, companyInfo, resMap, childList);
        	}
        	  
        }
		return listVo;
	}



	/***
	 * 施工合同数据
	 * @param contractNumber
	 * @param listVo
	 * @param contract
	 * @param companyInfo
	 * @param resMap
	 * @param childList
	 */
	private void roadWorkDataToB(String contractNumber, List<SyncOrderVO> listVo, ContractInfo contract,
			CompanyInfo companyInfo, Map<String, String> resMap, List<ContractTermsChild> childList) {
		//施工
			
			 SyncOrderVO vo = new SyncOrderVO();
			  //合同金额 全款
			  if(childList.size() == 0){
				      vo.setActualAmount(String.valueOf(resMap.get("c17")));
			  }else{
				  vo.setActualAmount(String.valueOf(childList.get(1).getCostValue()));
			  }
			  //
			  vo.setCompanyId(contract.getCompanyId());
			  //公司名称
			  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
			  //支付名称
			  vo.setTypeSub("8001");
			  //是否全额支付 ：1全款，2分期
			  if(childList.size() > 1){
			     vo.setContractType("2");
			     //是否全额支付
			  }else{
				 vo.setContractType("1");
				 //是否全额支付
				
			  }
			  vo.setIsEnd("1");
			  //业主名称
			  vo.setConsumerName(companyInfo.getLegalName());//法人名称
			  //合同开始时间 
			  vo.setStartTime(String.valueOf(resMap.get("c08")));
			  //合同结束时间
			  vo.setEndTime(String.valueOf(resMap.get("c09")));
			  //订单编号
			  vo.setFromOrderid(contractNumber);
			 
			  //合同类型 订单类型：设计1、施工2、合同3
			  vo.setType("8");
			  //项目地址
			  vo.setProjectAddr("");
			  //项目编号
			  vo.setProjectNo("");
			  //签约时间
			  vo.setSignedTime(contract.getSignedTime()==null?"":DateUtil.formartDate(contract.getSignedTime(), "yyyy-MM-dd"));
			  //是否个性化
			  vo.setStyleType("");
			  
			  vo.setSort("");

		     listVo.add(vo);
	}



	/**
	 * 
	 * TOB设计合同数据
	 * @param contractNumber
	 * @param listVo
	 * @param contract
	 * @param companyInfo
	 * @param resMap
	 * @param childList
	 */

	private void designDataToB(String contractNumber, List<SyncOrderVO> listVo, ContractInfo contract,
			CompanyInfo companyInfo, Map<String, String> resMap, List<ContractTermsChild> childList) {
		SyncOrderVO vo = new SyncOrderVO();
		  //合同金额 全款
		  if(childList.size() == 0){
		      vo.setActualAmount(String.valueOf(resMap.get("c15")));
		  }else{
			  vo.setActualAmount(String.valueOf(childList.get(1).getCostValue()));
		  }
		  //
		  vo.setCompanyId(contract.getCompanyId());
		  //公司名称
		  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
		  //支付名称
		  vo.setTypeSub("7001");
		  //是否全额支付 ：1全款，2分期
		  if(childList.size() > 1){
		     vo.setContractType("2");
		  }else{
			 vo.setContractType("1");
		  }
		  //业主名称
		  vo.setConsumerName(companyInfo.getLegalName());//法人名称
		  //合同开始时间 
		  vo.setStartTime(String.valueOf(resMap.get("c03")));
		  //合同结束时间
		  vo.setEndTime(String.valueOf(resMap.get("c04")));
		  //订单编号
		  vo.setFromOrderid(contractNumber);
		  //是否全额支付
		  vo.setIsEnd("1");
		  //合同类型 订单类型：设计1、施工2、合同3
		  vo.setType("7");
		  //项目地址
		  vo.setProjectAddr("");
		  //项目编号
		  vo.setProjectNo("");
		  //签约时间
		  vo.setSignedTime(contract.getSignedTime()==null?"":DateUtil.formartDate(contract.getSignedTime(), "yyyy-MM-dd"));
		  //是否个性化
		  vo.setStyleType("");
		  
		  vo.setSort("");

		  listVo.add(vo);
	}
	

    
    
    
}
