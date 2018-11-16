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
import cn.thinkfree.database.model.ContractTermsExample;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.DesignerOrderExample;
import cn.thinkfree.database.model.OrderContract;
import cn.thinkfree.database.model.OrderContractExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.vo.MarginContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
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
    
    
    

    @Override
    public MarginContractVO getMarginContract(String contractNumber) {


        MarginContractVO marginContractVO = new MarginContractVO();
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        contractInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
        List<ContractInfo> contractInfos = contractInfoMapper.selectByExample(contractInfoExample);

        if (contractInfos.size() > 0) {

            // 合同信息
            ContractInfo contractInfo = contractInfos.get(0);
            marginContractVO.setOptionType(type);
            marginContractVO.setContractNumber(contractInfo.getContractNumber());
            marginContractVO.setTransactionDate(contractInfo.getSignedTime().toString());
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
	public  List<SyncOrderVO> getOrderContract(String orderNumber) {
		
           
           List<SyncOrderVO> listVo = new ArrayList<>();
           
           OrderContractExample contractInfoExample = new OrderContractExample();
           contractInfoExample.createCriteria().andOrderNumberEqualTo(orderNumber);
           List<OrderContract> contractInfos = orderContractMapper.selectByExample(contractInfoExample);
           if(contractInfos != null && contractInfos.size()==1){
        	   
        	   OrderContract contract = contractInfos.get(0);
        	   CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contract.getCompanyId());
        	   if(contract != null){
        		  //初始化合同详情 
        		 ContractTermsExample terms = new ContractTermsExample();
     			 terms.createCriteria().andCompanyIdEqualTo(contract.getCompanyId())
     			 .andContractNumberEqualTo(contract.getContractNumber());
     			 List<ContractTerms> listTerms = contractTermsMapper.selectByExample(terms);
     			 Map<String,String> resMap = new HashMap<>();
     			 for (int i = 0; i < listTerms.size(); i++) {
     				resMap.put(listTerms.get(i).getContractDictCode(), listTerms.get(i).getContractValue());
				 }
        		 //判断当前合同类型 02设计合同_to_c 03施工合同_to_c
        		  if(contract.getContractType().equals("02")){
        			  //查询设计订单项目信息
        			  DesignerOrderExample example = new DesignerOrderExample();
        			  example.createCriteria().andOrderNoEqualTo(orderNumber);
        			  List<DesignerOrder> conorder =  designerOrderMapper.selectByExample(example);
        			  
        			  //设计判断是分期 换是全款
        			  
        			  String ctype = String.valueOf(resMap.get("c18"));
        			  if(ctype.equals("0")){//全款
        				  SyncOrderVO vo = new SyncOrderVO();
        				  //合同金额 全款
        				  vo.setActualAmount(String.valueOf(resMap.get("c19")));
        				  vo.setCompanyId(contract.getCompanyId());
        				  //公司名称
        				  vo.setCompanyName(companyInfo==null?"系统数据错误":companyInfo.getCompanyName());
        				  //支付名称
        				  vo.setTypeSub("设计费");
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
        				  vo.setIsEnd("");
        				  //合同类型 订单类型：设计1、施工2、合同3
        				  vo.setType("1");
        				  //项目地址
        				  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
        				  //项目编号
        				  vo.setProjectNo(conorder.get(0)!=null?"":conorder.get(0).getProjectNo());
        				  //签约时间
        				  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
        				  //是否个性化
        				  vo.setStyleType(conorder.get(0)!=null?"":conorder.get(0).getStyleType());
        				  
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
	            				  vo.setTypeSub(jsonMap.get("name"));
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
	            				  if(i == 0){
	            				  vo.setIsEnd("1");
	            				  }else{
	            					  if(i == jsonArray.size()-1 ){
	            						  vo.setIsEnd("2");
	            					  }else{
	            						  vo.setIsEnd("0");
	            					  }
	            				  }
	            				  //合同类型 订单类型：设计1、施工2、合同3
	            				  vo.setType("1");
	            				  
	            				  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
	            				  //项目编号
	            				  vo.setProjectNo(conorder!=null?"":conorder.get(0).getProjectNo());
	            				  //签约时间
	            				  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
	            				  //是否个性化
//	            				  vo.setStyleType(conorder!=null?"":conorder.get(0).getType());
	            				  
	            				  vo.setSort(""+(i+1));

	                              listVo.add(vo);
							  }
        				  }else{
        					  printInfoMes("调用订单合同发生错误 ","contractInfos is null or contractInfos.size() > 0 {}",jsonSr.toString());
        					  return null;
        				  }
        			  }
        			  
        		  }else{
        			  
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
	            				  //支付名称
	            				  vo.setTypeSub(jsonMap.get("progressName"));
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
	            				  if(i == 0){
	            				  vo.setIsEnd("1");
	            				  }else{
	            					  if(i == jsonArray.size()-1 ){
	            						  vo.setIsEnd("2");
	            					  }else{
	            						  vo.setIsEnd("0");
	            					  }
	            				  }
	            				  //合同类型 订单类型：设计1、施工2、合同3
	            				  vo.setType("1");
	            				  
	            				  vo.setProjectAddr(resMap.get("c08")+resMap.get("c09")+resMap.get("c10")+resMap.get("c11"));
	            				  //项目编号
	            				  vo.setProjectNo(conorder!=null?"":conorder.get(0).getProjectNo());
	            				  //签约时间
	            				  vo.setSignedTime(DateUtil.formartDate(contract.getSignTime(), "yyyy-MM-dd"));
	            				  //是否个性化
	            				  vo.setStyleType(conorder!=null?"":conorder.get(0).getType());
	            				  
	            				  vo.setSort(""+(i+1));

	                              listVo.add(vo);
							  }
     				  }else{
     					  printInfoMes("调用订单合同发生错误 ","contractInfos is null or contractInfos.size() > 0 {}",jsonSr.toString());
     					  return null;
     				  }
     			  
        			  
        		  }
        	   }
        	   
           }else{
        	   
        	   printInfoMes("调用订单合同发生错误 ","contractInfos is null or contractInfos.size() > 0 {}",contractInfos.toString());
        	   return null;
        	   
           }
           
		
		return listVo;
	}
	

    
    
    
}
