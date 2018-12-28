package cn.thinkfree.service.contract;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import cn.thinkfree.service.constants.*;
import cn.thinkfree.service.utils.AccountHelper;
import cn.thinkfree.service.utils.FreemarkerUtils;
import cn.thinkfree.service.utils.PdfUplodUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商合同
 */
@Service
public class AgencyServiceImpl extends AbsLogPrinter implements AgencyService {

    @Autowired
    AgencyContractMapper agencyContractMapper;

    @Autowired
    AgencyContractTermsMapper agencyContractTermsMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Autowired
    AgencyContractChangeMapper agencyContractChangeMapper;

    @Autowired
    DealerBrandInfoMapper dealerBrandInfoMapper;

    @Value( "${custom.cloud.fileUpload.dir}" )
    private String filePathDir;

    @Value( "${custom.cloud.fileUpload}" )
    private String fileUploadUrl;

    @Override
    public PageInfo<AgencyContract> pageContractBySEO(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );
        List<AgencyContract> list = agencyContractMapper.selectPageList(gencySEO);
        return  (new PageInfo<>( list ) );
    }

    @Override
    public PageInfo<AgencyContract> selectoperatingPageList(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );
        List<AgencyContract> list = agencyContractMapper.selectoperatingPageList(gencySEO);
        return  (new PageInfo<>( list ) );
    }

    @Override
    public PageInfo<AgencyContract> selectFinancialPageList(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );
        List<AgencyContract> list = agencyContractMapper.selectFinancialPageList(gencySEO);
        return  (new PageInfo<>( list ) );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertContract(ParamAgencySEO paramAgencySEO) {
        Long debugFlag = System.currentTimeMillis();
        boolean  flag = false;
            try {

                if(StringUtils.isEmpty(paramAgencySEO.getContractNumber())){
                    printErrorMes("新增合同:{},处理合同编号",debugFlag);
                    //生成合同编号
                    String contractNumber =  this.getOrderContract(paramAgencySEO.getDealerCompanyId(), paramAgencySEO.getBrandNo());
                    printErrorMes("新增合同:{},处理合同编号:{}",debugFlag,contractNumber);
                    paramAgencySEO.setContractNumber(contractNumber);
                    paramAgencySEO.setStatus(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString());
                    paramAgencySEO.setCreateTime(new Date());
                    agencyContractMapper.insertSelective(paramAgencySEO);
                }else{
                    printErrorMes("更新合同:{},拼装数据",debugFlag);
                    paramAgencySEO.setUpdateTime(new Date());
                    paramAgencySEO.setStatus(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString());
                    AgencyContractExample example = new AgencyContractExample();
                    example.createCriteria().andContractNumberEqualTo(paramAgencySEO.getContractNumber());
                    agencyContractMapper.updateByExampleSelective(paramAgencySEO,example);
                    printErrorMes("更新合同:{},更新完成",debugFlag);
                    AgencyContractTermsExample example1 = new AgencyContractTermsExample();
                    example1.createCriteria().andContractNumberEqualTo(paramAgencySEO.getContractNumber());
                    agencyContractTermsMapper.deleteByExample(example1);
                    printErrorMes("更新合同:{},更新完成,清理子表",debugFlag);
                }
                printErrorMes("新增合同:{},处理子表",debugFlag);
                this.insertAgencyContractTerm(paramAgencySEO);
                printErrorMes("新增合同:{},子表完成",debugFlag);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
                printErrorMes("插入仅销售合同异常"+e.getMessage());
                throw new RuntimeException("插入仅销售合同异常");
            }
        return flag;
    }

    @Override
    public boolean changeContract(ParamAgencySEO paramAgencySEO) {

        if (paramAgencySEO != null) {

            // 合同信息变更插入
            String oldContractNumber = paramAgencySEO.getContractNumber();
            String contractNumber =  this.getOrderContract(paramAgencySEO.getDealerCompanyId(), paramAgencySEO.getBrandNo());
            paramAgencySEO.setContractNumber(contractNumber);
            paramAgencySEO.setStatus(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString());
            paramAgencySEO.setCreateTime(new Date());
            agencyContractMapper.insertSelective(paramAgencySEO);

            // 变更合同关联表插入
            AgencyContractChange agencyContractChange = new AgencyContractChange();
            agencyContractChange.setChangeAfterCode(paramAgencySEO.getContractNumber());
            agencyContractChange.setChangeBeforeCode(oldContractNumber);
            agencyContractChange.setChangeTime(new Date());
            agencyContractChange.setIsDone(SysConstants.YesOrNo.NO.shortVal());
            agencyContractChangeMapper.insertSelective(agencyContractChange);
            return true;
        }
        return false;
    }

    /**
     * 插入门店摊位
     * @param paramAgencySEO
     */
    private void insertAgencyContractTerm(ParamAgencySEO paramAgencySEO) {

        // 插入门店摊位
        if (paramAgencySEO.getAgencyContractTermsList() != null && paramAgencySEO.getAgencyContractTermsList().size() >0) {

            paramAgencySEO.getAgencyContractTermsList().forEach(e ->{
                e.setContractNumber(paramAgencySEO.getContractNumber());
                agencyContractTermsMapper.insertSelective(e);
            });
        }
    }

    /**
     * 获取6-10 的随机位数数字
     *  想要生成的长度
     * @return result
     * 品牌编码（8位）+商户号（5位）+年份（2位）+序号（3位）
     */
    public  String getOrderContract(String brandNo, String agency) {
        String no = new SimpleDateFormat( "yy" ).format( new Date() ) + AccountHelper.randomAgencyContract(3);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(brandNo);
        stringBuffer.append(agency);
        stringBuffer.append(no);
        return stringBuffer.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditContract(String contractNumber, String status,String auditStatus, String cause) {
        //合同状态 0待提交1运营审核中2运营审核通过3运营审核不通4财务审核中 5财务审核通过 6财务审核不通过7待签约8生效中9冻结10过期11合同结束
        String pdfUrl = "";
        if(StringUtils.isNoneEmpty(contractNumber)){
            String dbStart = "";
           if(status.equals(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
               //运营提交
               dbStart = AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString();
               return this.auditContractUpdate(dbStart,pdfUrl,contractNumber);
            }
            if(status.equals(AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
                //运营通过
                dbStart = AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString();
                if (this.auditContractUpdate(dbStart,pdfUrl,contractNumber)) {
                    return this.auditRecord(new AuditRecord("1",auditStatus,cause,contractNumber));
                }
                return false;
            }
            if(status.equals(AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.NO.val.toString())){
                //运营不通过
                dbStart = AgencyConstants.AgencyType.OPERATING_AUDIT_REFUSED.code.toString();
                if (this.auditContractUpdate(dbStart,pdfUrl,contractNumber)) {
                    return this.auditRecord(new AuditRecord("1",auditStatus,cause,contractNumber));
                }
                return false;
            }
            if(status.equals(AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
                //财务审核通过
                dbStart = AgencyConstants.AgencyType.SIGN_UP.code.toString();
                //生成pdf
                pdfUrl = createPdf(contractNumber);
                if (this.auditContractUpdate(dbStart,pdfUrl,contractNumber)) {
                    return this.auditRecord(new AuditRecord("2",auditStatus,cause,contractNumber));
                }
                return false;
            }
            if(status.equals(AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.NO.val.toString())){
                //财务审核不通过
                dbStart = AgencyConstants.AgencyType.FINANCIAL_AUDIT_REFUSED.code.toString();
                if (this.auditContractUpdate(dbStart,pdfUrl,contractNumber)) {
                    return this.auditRecord(new AuditRecord("2",auditStatus,cause,contractNumber));
                }
                return false;
            }
            if(status.equals(AgencyConstants.AgencyType.SIGN_UP.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){

                //签约生效 dbStart = "8";
                dbStart = AgencyConstants.AgencyType.EFFECT_ING.code.toString();
//                this.updateBrandStatus(contractNumber,dbStart);
                // 判断是否是变更合同。（变更合同需要把之前的合同作废）
                AgencyContractChangeExample agencyContractChangeExample = new AgencyContractChangeExample();
                agencyContractChangeExample.createCriteria().andChangeAfterCodeEqualTo(contractNumber)
                .andIsDoneEqualTo(SysConstants.YesOrNo.NO.shortVal());

                List<AgencyContractChange> agencyContractChanges = agencyContractChangeMapper.selectByExample(agencyContractChangeExample);
                if (agencyContractChanges.size() > 0) {
                    AgencyContractChange agencyContractChange = agencyContractChanges.get(0);
                    if (StringUtils.isNotBlank(agencyContractChange.getChangeBeforeCode())) {
                        AgencyContractExample agencyContractExample = new AgencyContractExample();
                        agencyContractExample.createCriteria().andContractNumberEqualTo(agencyContractChange.getChangeBeforeCode());
                        AgencyContract agencyContract = new AgencyContract();
                        agencyContract.setStatus(AgencyConstants.AgencyType.INVALID_ING.code.toString());
                        agencyContractMapper.updateByExampleSelective(agencyContract,agencyContractExample);
                    }
                }
                return this.auditContractUpdate(dbStart,pdfUrl,contractNumber);
            }
        }
        return false;
    }
    private boolean auditContractUpdate(String dbStart, String pdfUrl, String contractNumber){
        AgencyContract record = new AgencyContract();
        record.setStatus(dbStart);
        record.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(pdfUrl)) {
            record.setPdfUrl(pdfUrl);
        }
        AgencyContractExample example = new AgencyContractExample();
        example.createCriteria().andContractNumberEqualTo(contractNumber);

        int  flag = agencyContractMapper.updateByExampleSelective(record,example);
        if (flag >0) {
            return true;
        }
        return false;
    }

    /**
     * 2运营审核通过,3运营审核不通,5财务审核通过,6财务审核不通过
     * @param auditRecord
     * @return
     */
    private boolean auditRecord(AuditRecord auditRecord) {

        //插入审批记录添加审核记录表
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        String	auditPersion = userVO == null ? "" : userVO.getName();
        String	auditAccount = userVO == null ? "" : userVO.getUsername();

        PcAuditInfo pcAuditInfo = new PcAuditInfo("7", auditRecord.getAuditLevel(), auditPersion, auditRecord.getAuditStatus()
                , new Date(), "", auditRecord.getCause(),
                auditRecord.getContractNumber(), new Date(), auditAccount);

        if (pcAuditInfoMapper.insertSelective(pcAuditInfo) >0) {
            return true;
        }
        return false;
    }

    @Override
    public String createPdf(String contractNumber){

        // 上传服务器返回地址
        String pdfUrl = "";
        Map<String, Object> root = new HashMap<>();
        //查询合同数据
        ParamAgency paramAgency = this.getParamAgency(contractNumber);
        Map<String, Object> a = (Map<String, Object>) JSON.toJSON(paramAgency);
        root.putAll(a);
        // 特殊处理
        try {
            String filePath = FreemarkerUtils.savePdf(filePathDir + contractNumber, "4", root);
            // 上传
            pdfUrl = PdfUplodUtils.upload(filePath, fileUploadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            printErrorMes("生成pdf合同发生错误", e.getMessage());
        }
        return  pdfUrl;
    }

    @Override
    public boolean updateContractStatus(String companyId, String contractNumber, String status) {
        //9冻结10过期11作废
        boolean flag = false;
        AgencyContract record = new AgencyContract();
        record.setStatus(status);
        try {
            AgencyContractExample  example = new AgencyContractExample();
            AgencyContractExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(companyId)) {
                criteria.andCompanyIdEqualTo(companyId);
            }
            if (StringUtils.isNotBlank(contractNumber)) {
                criteria.andContractNumberEqualTo(contractNumber);
//                this.updateBrandStatus(contractNumber,status);
            }
             agencyContractMapper.updateByExampleSelective(record,example);
            flag =  true;
        } catch (Exception e) {
            e.printStackTrace();
            printErrorMes("改变销售合同状态异常"+e.getMessage());
            throw new RuntimeException("修改销售合同状态异常");
        }
        return flag;
    }

    @Override
    public ParamAgency getParamAgency(String contractNumber) {

        ParamAgency paramAgency = new ParamAgency();
        AgencyContractExample example = new AgencyContractExample();
        example.createCriteria().andContractNumberEqualTo(contractNumber);
        List<AgencyContract>  list = agencyContractMapper.selectByExample(example);
        AgencyContractTermsExample exampleTwo = new AgencyContractTermsExample();
        exampleTwo.createCriteria().andContractNumberEqualTo(contractNumber);
        List<AgencyContractTerms>  listTwo = agencyContractTermsMapper.selectByExample(exampleTwo);
        paramAgency.setAgencyContract(list.size() > 0?list.get(0):new AgencyContract());
        paramAgency.setAgencyContractTermsList(listTwo);

        //获取 审批信息
        PcAuditInfoExample autit = new PcAuditInfoExample();
        autit.createCriteria().andContractNumberEqualTo(contractNumber).andAuditTypeEqualTo("7");
        autit.setOrderByClause("audit_time DESC");
        List<PcAuditInfo>  auList =  pcAuditInfoMapper.selectByExample(autit);
        //查询审批信息
        paramAgency.setAuditInfo(auList);

        return paramAgency;
    }

    @Override
    public String checkRepeat(ParamAgencySEO paramAgencyList) {

        List<String> status = new ArrayList<>(Arrays.asList("10","11"));
        AgencyContractExample agencyContractExample = new AgencyContractExample();
        AgencyContractExample.Criteria criteria = agencyContractExample.createCriteria();
        criteria.andBrandNoEqualTo(paramAgencyList.getBrandNo());
        criteria.andCategoryNoEqualTo(paramAgencyList.getCategoryNo());
        criteria.andCompanyIdEqualTo(paramAgencyList.getCompanyId());
        criteria.andStatusNotIn(status);

        // 门店查询
        AgencyContractExample agencyContractExampleShop = new AgencyContractExample();
        AgencyContractExample.Criteria criteriashop = agencyContractExampleShop.createCriteria();
        criteriashop.andCompanyIdEqualTo(paramAgencyList.getCompanyId());
        criteriashop.andStatusNotIn(status);
        if (StringUtils.isNotBlank(paramAgencyList.getContractNumber())) {

            criteria.andContractNumberNotEqualTo(paramAgencyList.getContractNumber());
            criteriashop.andContractNumberNotEqualTo(paramAgencyList.getContractNumber());
        }
        if (agencyContractMapper.countByExample(agencyContractExample) >0) {
            return "品牌，品类已存在，请重新选择";
        } else {

            List<String> contractNumbers = agencyContractMapper.selectByExample(agencyContractExampleShop).stream().map(a->a.getContractNumber()).collect(Collectors.toList());
            if (paramAgencyList.getAgencyContractTermsList() != null) {
                Set resultcheck = new HashSet();
                for (AgencyContractTerms e:paramAgencyList.getAgencyContractTermsList()) {

                    resultcheck.add(e);
                    AgencyContractTermsExample agencyContractTermsExample = new AgencyContractTermsExample();
                    AgencyContractTermsExample.Criteria criteria1 = agencyContractTermsExample.createCriteria();

                    if (StringUtils.isBlank(e.getShopName()) || StringUtils.isBlank(e.getShopCode())) {
                        return "门店名称不可为空";
                    }
                    if (StringUtils.isBlank(e.getBoothName()) || StringUtils.isBlank(e.getBoothCode())) {
                        return "摊位名称不可为空";
                    }
                    if (contractNumbers.size() > 0) {
                        criteria1.andContractNumberIn(contractNumbers);
                        criteria1.andShopCodeEqualTo(e.getShopCode());
                        criteria1.andBoothCodeEqualTo(e.getBoothCode());
                        if (agencyContractTermsMapper.countByExample(agencyContractTermsExample) >0) {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("门店：");
                            stringBuffer.append(e.getShopName());
                            stringBuffer.append(",");
                            stringBuffer.append(e.getBoothName());
                            stringBuffer.append(".");
                            stringBuffer.append("已存在，请重新选择");
                            return stringBuffer.toString();
                        }
                    }
                }
                if (resultcheck.size() < paramAgencyList.getAgencyContractTermsList().size()) {
                    return "请不要选择相同的门店和摊位";
                }
            }
            }
        return null;
    }

    /**
     * 刷新品牌状态
     * @param contractNumber
     * @param status
     * @return
     */
    private boolean updateBrandStatus(String contractNumber,String status) {

        AgencyContractExample agencyContractExample = new AgencyContractExample();
        agencyContractExample.createCriteria().andContractNumberEqualTo(contractNumber);

        List<AgencyContract> agencyContracts = agencyContractMapper.selectByExample(agencyContractExample);

        if (agencyContracts.size() >0) {
            AgencyContract agencyContract = agencyContracts.get(0);
            if (StringUtils.isNotBlank(agencyContract.getBrandNo()) && StringUtils.isNotBlank(agencyContract.getCompanyId())
                    && StringUtils.isNotBlank(agencyContract.getCategoryNo())) {
                DealerBrandInfoExample dealerBrandInfoExample = new DealerBrandInfoExample();
                dealerBrandInfoExample.createCriteria().andBrandNoEqualTo(agencyContract.getBrandNo()).
                        andCategoryNoEqualTo(agencyContract.getCategoryNo()).andCompanyIdEqualTo(agencyContract.getCompanyId());
                List<DealerBrandInfo> dealerBrandInfos = dealerBrandInfoMapper.selectByExample(dealerBrandInfoExample);
                if (dealerBrandInfos.size() > 0) {

                    DealerBrandInfo dealerBrandInfo = new DealerBrandInfo();
                    dealerBrandInfo = dealerBrandInfos.get(0);

                    String dealerStatus = null;
                    if (dealerBrandInfo.getAuditStatus().equals(AgencyConstants.AgencyType.OPERATING_AUDIT_THROUGH.code)
                            && AgencyConstants.AgencyType.EFFECT_ING.code.toString().equals(status)) {

                        dealerStatus = AgencyConstants.AgencyType.OPERATING_AUDIT_REFUSED.code.toString();
                    } else if (dealerBrandInfo.getAuditStatus().equals(AgencyConstants.AgencyType.OPERATING_AUDIT_REFUSED.code)
                            && AgencyConstants.AgencyType.INVALID_ING.code.toString().equals(status)) {

                        dealerStatus = AgencyConstants.AgencyType.OPERATING_AUDIT_THROUGH.code.toString();
                    }

                    if (StringUtils.isNotBlank(dealerStatus)) {
                        dealerBrandInfo.setAuditStatus(Integer.valueOf(dealerStatus));
                        return dealerBrandInfoMapper.updateByExampleSelective(dealerBrandInfo,dealerBrandInfoExample)>0?true:false;
                    }
                }
            }
        }
        return false;
    }

    class AuditRecord {

        public AuditRecord(String auditLevel, String auditStatus, String cause, String contractNumber) {
            this.auditLevel = auditLevel;
            this.auditStatus = auditStatus;
            this.cause = cause;
            this.contractNumber = contractNumber;
        }

        /**
         * 运营1 ，财务2
         */
        private String auditLevel;

        /**
         * 审批状态
         */
        private String auditStatus;

        /**
         * 审批原因
         */
        private String cause;

        /**
         * 合同编号
         */
        private String contractNumber;

        public String getAuditLevel() {
            return auditLevel;
        }

        public void setAuditLevel(String auditLevel) {
            this.auditLevel = auditLevel;
        }

        public String getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public void setContractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
        }
    }
}
