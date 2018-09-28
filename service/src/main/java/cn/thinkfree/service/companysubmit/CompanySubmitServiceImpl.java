package cn.thinkfree.service.companysubmit;

import java.util.*;

import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.CompanyInfoExpandMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ContractInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.PcCompanyFinancialMapper;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.CompanyInfoExample;
import cn.thinkfree.database.model.CompanyInfoExpand;
import cn.thinkfree.database.model.CompanyInfoExpandExample;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcCompanyFinancial;
import cn.thinkfree.service.constants.CompanyAuditStatus;

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
	ContractInfoMapper contractInfoMapper;
	
	@Autowired
	PcAuditInfoMapper pcAuditInfoMapper;


	/**
	 * 公司列表
	 * @param companyListSEO
	 * @return
	 */
	@Override
	public PageInfo<CompanyListVo> list(CompanyListSEO companyListSEO) {
		UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
		List<String> relationMap = userVO.getRelationMap();
		companyListSEO.setRelationMap(relationMap);

		PageHelper.startPage(companyListSEO.getPage(),companyListSEO.getRows());
		List<CompanyListVo> companyListVoList = companyInfoMapper.list(companyListSEO);
		return new PageInfo<>(companyListVoList);
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
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();

        CompanyInfoExpand companyInfoExpand = companySubmitVo.getCompanyInfoExpand();
        companyInfoExpand.setUpdateTime(date);
        //企业税务登记证
        companyInfoExpand.setTaxCodePhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getTaxCodePhotoUrl()));
        CompanyInfoExpandExample companyInfoExpandExample = new CompanyInfoExpandExample();
        companyInfoExpandExample.createCriteria()
				.andCompanyIdEqualTo(companySubmitVo.getCompanyInfo().getCompanyId());
        return companyInfoExpandMapper.updateByExampleSelective(companyInfoExpand,companyInfoExpandExample);
    }

    private int addFinancial(CompanySubmitVo companySubmitVo, Date date) {
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();

        PcCompanyFinancial pcCompanyFinancial = companySubmitVo.getPcCompanyFinancial();
        pcCompanyFinancial.setCompanyId(companySubmitVo.getCompanyInfo().getCompanyId());
        pcCompanyFinancial.setCreateTime(date);
        pcCompanyFinancial.setUpdateTime(date);
        //开户行许可证
        pcCompanyFinancial.setLicenseUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLicenseUrl()));
        return pcCompanyFinancialMapper.insertSelective(pcCompanyFinancial);
    }

    private int updateCompanyInfo(CompanySubmitVo companySubmitVo, Date date) {
        //公司资质上传文件
        CompanySubmitFileVo companySubmitFileVo = companySubmitVo.getCompanySubmitFileVo();
        CompanyInfo companyInfo = companySubmitVo.getCompanyInfo();

        companyInfo.setPhone(companyInfo.getLegalPhone());
        companyInfo.setUpdateTime(date);
        //资质上传成功后审批状态改为已激活
        companyInfo.setAuditStatus(CompanyAuditStatus.ACTIVATION.stringVal());
        //营业执照
        companyInfo.setBusinessPhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getBusinessPhotoUrl()));
        //装修施工资质证书
        companyInfo.setWorkPhotoUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getWorkPhotoUrl()));
        //法人身份证正面
        companyInfo.setLefalCardUpUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLefalCardUpUrl()));
        //法人身份证反面
        companyInfo.setLefalCardDownUrl(WebFileUtil.fileCopy("static/", companySubmitFileVo.getLefalCardDownUrl()));
        CompanyInfoExample companyInfoExample = new CompanyInfoExample();
        companyInfoExample.createCriteria().andCompanyIdEqualTo(companyInfo.getCompanyId());
        return companyInfoMapper.updateByExampleSelective(companyInfo,companyInfoExample);
    }

	@Override
	public Map<String, String> auditContract( String companyId, String auditStatus,
			String auditCase) {
		Map<String,String> map = new HashMap<>();
		
		if(StringUtils.isEmpty(companyId)){
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
		if(auditCase.equals("0")){
	        //运营审核通过生成合同编号
			String contractNumber = String.valueOf(UUID.randomUUID());
			
			//修改合同表 0草稿 1待审批 2 审批通过 3 审批拒绝
			ContractVo vo = new ContractVo();
			vo.setCompanyId(companyId);
			vo.setContractNumber(contractNumber);
			vo.setContractStatus("0");
			int flag = contractInfoMapper.updateContractStatus(vo);
			//修改公司表 
		
			CompanyInfo companyInfo = new CompanyInfo();
			companyInfo.setCompanyId(companyId);
			if(auditCase.equals("0")){//运营审核通过
				companyInfo.setAuditStatus(CompanyAuditStatus.APTITUDETG.stringVal());
			}else{//财务审核不通过
				companyInfo.setAuditStatus(CompanyAuditStatus.SUCCESSJOSB.stringVal());
			}
			int flagT = companyInfoMapper.updateauditStatus(companyInfo);
			
			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//添加审核记录表
			PcAuditInfo record = new PcAuditInfo("1", "1", auditPersion, auditStatus, new Date(),
					companyId, auditCase, contractNumber);
			
			int flagi = pcAuditInfoMapper.insertSelective(record);
		    
			if(flag > 0 && flagT > 0 &&  flagi  > 0 ){
				
				map.put("code", "0");
				map.put("msg", "审核成功");
				
			}else{
				map.put("code", "1");
				map.put("msg", "审核失败");
			}
		}else{//审核失败

			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO ==null?"":userVO.getUsername();
			//添加审核记录表
			PcAuditInfo record = new PcAuditInfo("1", "1", auditPersion, auditStatus, new Date(),
					companyId, auditCase, "");
		    pcAuditInfoMapper.insertSelective(record);
			
		}
		return map;
	}
}
