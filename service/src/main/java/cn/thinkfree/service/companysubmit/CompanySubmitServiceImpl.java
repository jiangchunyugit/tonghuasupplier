package cn.thinkfree.service.companysubmit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.companyapply.CompanyApplyService;
import cn.thinkfree.service.constants.AuditStatus;
import cn.thinkfree.service.constants.CompanyApply;
import cn.thinkfree.service.constants.ContractStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.CompanyInfoExpandMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.MyContractInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcAuditTemporaryInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.service.constants.CompanyConstants;
import cn.thinkfree.service.utils.ContractNum;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;

/**
 * @author ying007
 * 公司入驻业务
 */
@Service
public class CompanySubmitServiceImpl implements CompanySubmitService {

    @Autowired
    CompanyInfoExpandMapper companyInfoExpandMapper;

    @Autowired
    PcCompanyFinancialMapper pcCompanyFinancialMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;
    
    @Autowired
	MyContractInfoMapper contractInfoMapper;
	
	@Autowired
	PcAuditInfoMapper pcAuditInfoMapper;

	@Autowired
	PcAuditTemporaryInfoMapper pcAuditTemporaryInfoMapper;

	@Autowired
	CompanyApplyService companyApplyService;

	@Autowired
	CompanySubmitService companySubmitService;

	final static String TARGET = "static/";



	@Override
	public CompanyDetailsVO companyDetails(String contractNumber, String companyId) {
		CompanyDetailsVO companyDetailsVO = new CompanyDetailsVO();
		CompanySubmitVo companySubmitVo = companySubmitService.findCompanyInfo(companyId);
		companyDetailsVO.setCompanySubmitVO(companySubmitVo);
        //todo 合同结算条款待完成

		return null;
	}

    @Override
	public PcAuditInfo findAuditCase(String contractNumber) {
		PcAuditInfoExample example = new PcAuditInfoExample();
		example.createCriteria().andContractNumberEqualTo(contractNumber);

		List<PcAuditInfo> pcAuditInfos = pcAuditInfoMapper.selectByExample(example);

		if(pcAuditInfos.size() > 0){
			return pcAuditInfos.get(0);
		}
		return null;
	}

	@Override
	public AuditInfoVO findAuditStatus(String companyId) {
		AuditInfoVO auditInfoVO = pcAuditInfoMapper.findAuditStatus(companyId);
		//TOdo
		//如果公司入驻状态是7：确认保证金  说明运营，财务审核完成审核，合同签约
		if(CompanyAuditStatus.NOTPAYBAIL.code.toString().equals(auditInfoVO.getCompanyAuditType())){
			auditInfoVO.setAuditCase("");
			auditInfoVO.setCompanyAuditName("签约完成");
		}else if(CompanyAuditStatus.NOTPAYBAIL.code > Integer.parseInt(auditInfoVO.getCompanyAuditType())){
			auditInfoVO.setCompanyAuditName("资质审核中");
		}
		return null;
	}

	@Override
	public CompanySubmitVo findCompanyInfo(String companyId) {
		CompanySubmitVo companySubmitVo = new CompanySubmitVo();

		//查询companyInfo表：平台状态：platform_type=0;
		// 删除状态：is_delete=2;    审核状态：is_check=1;    审批状态：audit_status=7
		CompanyInfoExample companyInfoExample = new CompanyInfoExample();
		companyInfoExample.createCriteria().andCompanyIdEqualTo(companyId)
				.andIsDeleteEqualTo(SysConstants.YesOrNoSp.NO.shortVal())
				.andIsCheckEqualTo(SysConstants.YesOrNoSp.YES.shortVal())
				.andAuditStatusEqualTo(CompanyAuditStatus.SUCCESSJOIN.stringVal())
				.andPlatformTypeEqualTo(SysConstants.YesOrNo.NO.shortVal());

		List<CompanyInfo> companyInfo = companyInfoMapper.selectByExample(companyInfoExample);
		if(companyInfo.size() <= 0  && companyInfo.get(0) == null){
			return null;
		}
		companySubmitVo.setCompanyInfo(companyInfo.get(0));

		//查询companyInfoExpand表：
		CompanyInfoExpandExample companyInfoExpandExample = new CompanyInfoExpandExample();
		companyInfoExpandExample.createCriteria().andCompanyIdEqualTo(companyId);
		List<CompanyInfoExpand> companyInfoExpand = companyInfoExpandMapper.selectByExample(companyInfoExpandExample);

		if(companyInfoExpand.size() > 0  && companyInfoExpand.get(0) != null){
			if(companyInfoExpand.get(0).getCompanyType() != null && StringUtils.isNotBlank(companyInfoExpand.get(0).getCompanyType().toString())) {
				companySubmitVo.setCompanyTypeName(CompanyConstants.CompanySharesType.getDesc(companyInfoExpand.get(0).getCompanyType().intValue()));
			}
			companySubmitVo.setCompanyInfoExpand(companyInfoExpand.get(0));
		}



//		对公账信息PcCompanyFinancial
		PcCompanyFinancialExample pcCompanyFinancialExample = new PcCompanyFinancialExample();
		pcCompanyFinancialExample.createCriteria().andCompanyIdEqualTo(companyId);
		List<PcCompanyFinancial> companyFinancials = pcCompanyFinancialMapper.selectByExample(pcCompanyFinancialExample);
		if(companyFinancials.size() > 0  && companyFinancials.get(0) != null){
			companySubmitVo.setPcCompanyFinancial(companyFinancials.get(0));
		}

		return companySubmitVo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean changeCompanyInfo(CompanyTemporaryVo companyTemporaryVo) {
		PcAuditTemporaryInfo pcAuditTemporaryInfo = new PcAuditTemporaryInfo();
		SpringBeanUtil.copy(companyTemporaryVo, pcAuditTemporaryInfo);
		int line = pcAuditTemporaryInfoMapper.insertSelective(pcAuditTemporaryInfo);
		if(line > 0){
			return true;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String auditChangeCompany(String companyId, String auditStatus, String auditCase) {
		Date date = new Date();
		//1：查询公司资质临时表
		PcAuditTemporaryInfoExample example = new PcAuditTemporaryInfoExample();
		example.createCriteria().andCompanyIdEqualTo(companyId);
		List<PcAuditTemporaryInfo>
				pcAuditTemporaryInfo = pcAuditTemporaryInfoMapper.selectByExample(example);

		//audit_type:审核状态 0通过  1不通过
		if(SysConstants.YesOrNo.NO.toString().equals(auditStatus)){

			//2：修改公司临时表状态：change_status:资质变更状态：0：审批成功 1：审批失败
			pcAuditTemporaryInfo.get(0).setChangeStatus(SysConstants.YesOrNo.NO.shortVal());
			int addLine = pcAuditTemporaryInfoMapper.updateByExampleSelective(pcAuditTemporaryInfo.get(0), example);
			if(addLine <= 0){
				return "审批失败";
			}
			//3：审批通过更新公司表
			CompanyInfo companyInfo = new CompanyInfo();

			SpringBeanUtil.copy(pcAuditTemporaryInfo, companyInfo);

			companyInfo.setId(null);
			companyInfo.setUpdateTime(date);
			CompanyInfoExample companyInfoExample = new CompanyInfoExample();
			companyInfoExample.createCriteria().andCompanyIdEqualTo(companyId);
			int companyLine = companyInfoMapper.updateByExampleSelective(companyInfo, companyInfoExample);
			if(companyLine <= 0){
				return "审批失败";
			}

			//4:审批通过更新公司拓展表
			CompanyInfoExpand companyInfoExpand = new CompanyInfoExpand();
			CompanyInfoExpandExample companyInfoExpandExample = new CompanyInfoExpandExample();
			companyInfoExpandExample.createCriteria().andCompanyIdEqualTo(companyId);
			SpringBeanUtil.copy(pcAuditTemporaryInfo, companyInfoExpand);
			companyInfoExpand.setId(null);
			companyInfoExpand.setUpdateTime(date);
			int companyExpandLine = companyInfoExpandMapper.updateByExampleSelective(companyInfoExpand, companyInfoExpandExample);
			if(companyExpandLine <= 0){
				return "审批失败";
			}

			//5:审批通过更新公司银行账户表
			PcCompanyFinancial pcCompanyFinancial = new PcCompanyFinancial();
			PcCompanyFinancialExample pcCompanyFinancialExample = new PcCompanyFinancialExample();
			pcCompanyFinancialExample.createCriteria().andCompanyIdEqualTo(companyId);
			SpringBeanUtil.copy(pcAuditTemporaryInfo, pcCompanyFinancial);
			pcCompanyFinancial.setId(null);
			pcCompanyFinancial.setUpdateTime(date);
			int financialLine = pcCompanyFinancialMapper.updateByExampleSelective(pcCompanyFinancial, pcCompanyFinancialExample);
			if(financialLine <= 0){
				return "审批失败";
			}

			//运营审核通过添加一条审批记录
			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//添加审核记录表
			PcAuditInfo record = new PcAuditInfo(CompanyConstants.AuditType.CHANGE.toString(), SysConstants.YesOrNo.YES.toString(), auditPersion, auditStatus, date,
					companyId, auditCase, "");

			int flagi = pcAuditInfoMapper.insertSelective(record);
			if(flagi <= 0){
				return "审批失败";
			}

			return "审批成功";

		}else{//审核失败

			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//添加审核记录表
			PcAuditInfo record = new PcAuditInfo(CompanyConstants.AuditType.CHANGE.toString(), SysConstants.YesOrNo.YES.toString(), auditPersion, auditStatus, date,
					companyId, auditCase, "");
			int line = pcAuditInfoMapper.insertSelective(record);
			//2：修改公司临时表状态：change_status:资质变更状态：0：审批成功 1：审批失败
			pcAuditTemporaryInfo.get(0).setChangeStatus(SysConstants.YesOrNo.YES.shortVal());
			int addLine = pcAuditTemporaryInfoMapper.updateByExampleSelective(pcAuditTemporaryInfo.get(0), example);
			if(addLine <= 0 && line <= 0){
				return "审批失败";
			}
			return "审批成功";

		}
	}

	@Override
	public PcAuditTemporaryInfo findCompanyTemporaryInfo(String companyId) {
		PcAuditTemporaryInfoExample example = new PcAuditTemporaryInfoExample();
		example.createCriteria().andCompanyIdEqualTo(companyId);
		PcAuditTemporaryInfo pcAuditTemporaryInfo = pcAuditTemporaryInfoMapper.findCompanyTemporaryInfo(companyId);
		return pcAuditTemporaryInfo;
	}

	/**
	 * 公司列表
	 * @param companyListSEO
	 * map.put("company_id","desc");排序形式
	 * @return
	 */
	@Override
	public PageInfo<CompanyListVo> list(CompanyListSEO companyListSEO) {
		UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
		//todo 获取分站id？？？？星级
//		List<String> relationMap = userVO.getRelationMap();
		List<String> relationMap = new ArrayList<>();
        relationMap.add("44");
		relationMap.add("1402");
		companyListSEO.setRelationMap(relationMap);
		PageHelper.startPage(companyListSEO.getPage(),companyListSEO.getRows());
		List<CompanyListVo> companyListVoList = companyInfoMapper.list(companyListSEO);
		return new PageInfo<>(companyListVoList);
	}

	@Override
	public void downLoad(HttpServletResponse response, CompanyListSEO companyListSEO) {
		ExcelData data = new ExcelData();
		data.setName("用户信息数据");
		//添加表头
		String[] titleArrays = {"公司编号","公司类型","公司性质","所属站点","公司名称",
				"入驻日期","截至时间","签约时间","法人","联系人","联系电话","保证金","状态"};
		List<String> titles = new ArrayList<>();
		for(String title: titleArrays){
			titles.add(title);
		}
		data.setTitles(titles);
		//添加列
		List<List<Object>> rows = new ArrayList<>();
		List<Object> row = null;
		List<CompanyListVo> companyListVoList = companyInfoMapper.downLoad(companyListSEO);

		for(CompanyListVo vo: companyListVoList){
			row=new ArrayList<>();
			row.add(vo.getCompanyId());
			row.add(vo.getRoleName());
			row.add(vo.getComapnyNature());
			row.add(vo.getSiteProvinceName()+vo.getSiteCityName()+vo.getSiteName());
			row.add(vo.getCompanyName());
			row.add(vo.getStartTime());
			row.add(vo.getEndTime());
			row.add(vo.getSignedTime());
			row.add(vo.getLegalName());
			row.add(vo.getContactName());
			row.add(vo.getContactPhone());
			row.add(vo.getDepositMoney());
			row.add(CompanyAuditStatus.getDesc(Integer.parseInt(vo.getAuditStatus())));
			rows.add(row);
		}
		data.setRows(rows);

		SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String fileName=fdate.format(new Date())+".xls";
		try{
			ExcelUtils.exportExcel(response, fileName, data);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upCompanyInfo(CompanySubmitVo companySubmitVo) {
        Date date = new Date();

        //1.更新表company_info
        int ci = updateCompanyInfo(companySubmitVo, date);

        //2.插入表pc_company_financial
        int pcf = addFinancial(companySubmitVo, date);

        //3.更新表company_info_expand
        int cie = updateCompanyExpand(companySubmitVo, date);

        if(ci > 0 && pcf > 0 && cie > 0){
            return true;
        }
        return false;
    }

    private int updateCompanyExpand(CompanySubmitVo companySubmitVo, Date date) {

        CompanyInfoExpand companyInfoExpand = companySubmitVo.getCompanyInfoExpand();
        companyInfoExpand.setUpdateTime(date);

        CompanyInfoExpandExample companyInfoExpandExample = new CompanyInfoExpandExample();
        companyInfoExpandExample.createCriteria()
				.andCompanyIdEqualTo(companySubmitVo.getCompanyInfo().getCompanyId());
        return companyInfoExpandMapper.updateByExampleSelective(companyInfoExpand,companyInfoExpandExample);
    }

    private int addFinancial(CompanySubmitVo companySubmitVo, Date date) {

        PcCompanyFinancial pcCompanyFinancial = companySubmitVo.getPcCompanyFinancial();
        pcCompanyFinancial.setCompanyId(companySubmitVo.getCompanyInfo().getCompanyId());
        pcCompanyFinancial.setCreateTime(date);
        pcCompanyFinancial.setUpdateTime(date);
        return pcCompanyFinancialMapper.insertSelective(pcCompanyFinancial);
    }

    private int updateCompanyInfo(CompanySubmitVo companySubmitVo, Date date) {
        CompanyInfo companyInfo = companySubmitVo.getCompanyInfo();

        companyInfo.setPhone(companyInfo.getLegalPhone());
        companyInfo.setUpdateTime(date);
        //资质上传成功后审批状态改为资质待审核
        companyInfo.setAuditStatus(CompanyAuditStatus.AUDITING.stringVal());

        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyInfo.getCompanyId());
        return companyInfoMapper.updateByExampleSelective(companyInfo,companyInfoExample);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String auditContract(PcAuditInfo pcAuditInfo) {
		Date date = new Date();
		Map<String,String> map = new HashMap<>();

		String companyId = pcAuditInfo.getCompanyId();

		//审核通过  运营审核通过生成合同编号
		if(AuditStatus.AuditPass.shortVal().equals(pcAuditInfo.getAuditStatus())){
			//1.修改公司表
			boolean applyFlag = companyApplyService.updateStatus(companyId, CompanyAuditStatus.SUCCESSAUDIT.code.toString());

			//todo 从登陆信息中获取公司类型
			String contractNumber =ContractNum.getInstance().GenerateOrder("DB");
		//	String contractNumber = String.valueOf(UUID.randomUUID());
			
			//2.修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝ContractStatus
			ContractVo vo = new ContractVo();
			vo.setCompanyId(companyId);
			vo.setContractNumber(contractNumber);
			vo.setContractStatus(ContractStatus.DraftStatus.shortVal());
			int flag = contractInfoMapper.updateContractStatus(vo);
			
			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//3.添加审核记录表
			PcAuditInfo record = new PcAuditInfo(pcAuditInfo.getAuditType(), pcAuditInfo.getAuditLevel(), auditPersion, pcAuditInfo.getAuditStatus(), date,
					companyId, pcAuditInfo.getAuditCase(), contractNumber);
			
			int flagon = pcAuditInfoMapper.insertSelective(record);
		    
			if(flag > 0 && applyFlag &&  flagon  > 0 ){
				return "审核成功";
				
			}else{
				return "审核失败";
			}
		}else{//审核失败
			boolean applyFlag = companyApplyService.updateStatus(pcAuditInfo.getCompanyId(), CompanyAuditStatus.FAILAUDIT.code.toString());

			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//添加审核记录表
			PcAuditInfo record = new PcAuditInfo(pcAuditInfo.getAuditType(), pcAuditInfo.getAuditLevel(), auditPersion, pcAuditInfo.getAuditStatus(), date,
					companyId, pcAuditInfo.getAuditCase(), "");
		    int line = pcAuditInfoMapper.insertSelective(record);
		    if(applyFlag && line > 0){
				return "审核成功";
			}else{
				return "审核失败";
			}
		}
	}

    @Override
    public String joinSuccess(String companyId) {
	    boolean aLine = companyApplyService.updateStatus(companyId, CompanyAuditStatus.NOTPAYBAIL.stringVal());

	    //todo 合同状态修改待完成
	    return null;
    }
}
