package cn.thinkfree.service.contract;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import cn.thinkfree.service.constants.*;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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


    @Value( "${custom.cloud.fileUpload.dir}" )
    private String filePathDir;

    @Value( "${custom.cloud.fileUpload}" )
    private String fileUploadUrl;



    @Override
    public PageInfo<MyAgencyContract> pageContractBySEO(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );

        List<MyAgencyContract> list = agencyContractMapper.selectPageList(gencySEO);

        return  (new PageInfo<>( list ) );
    }


    @Override
    public PageInfo<MyAgencyContract> selectoperatingPageList(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );

        List<MyAgencyContract> list = agencyContractMapper.selectoperatingPageList(gencySEO);

        return  (new PageInfo<>( list ) );
    }

    @Override
    public PageInfo<MyAgencyContract> selectFinancialPageList(AgencySEO gencySEO) {

        PageHelper.startPage( gencySEO.getPage(), gencySEO.getRows() );

        List<MyAgencyContract> list = agencyContractMapper.selectFinancialPageList(gencySEO);

        return  (new PageInfo<>( list ) );
    }

    @Override
    @Transactional
    public boolean insertContract(List<ParamAgencySEO> paramAgencyList) {
        boolean  flag = false;
        if(paramAgencyList != null  && paramAgencyList.size() > 0 ){
            try {
                paramAgencyList.forEach(a ->{
                    if(StringUtils.isEmpty(a.getAgencyContract().getContractNumber())){
                        //生成合同编号
                        String contractNumber =  this.getOrderContract();
                        a.getAgencyContract().setContractNumber(contractNumber);
                        a.getAgencyContractTerms().setContractNumber(contractNumber);
                        a.getAgencyContract().setStatus(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString());
                        a.getAgencyContract().setCreateTime(new Date());
                        a.getAgencyContract().setUpdateTime(new Date());
                        agencyContractMapper.insertSelective(a.getAgencyContract());
                        agencyContractTermsMapper.insertSelective(a.getAgencyContractTerms());
                    }else{
                        a.getAgencyContract().setUpdateTime(new Date());
                        AgencyContractExample example = new AgencyContractExample();
                        example.createCriteria().andContractNumberEqualTo(a.getAgencyContract().getContractNumber());
                        agencyContractMapper.updateByExampleSelective(a.getAgencyContract(),example);
                        AgencyContractTermsExample example1 = new AgencyContractTermsExample();
                        example1.createCriteria().andContractNumberEqualTo(a.getAgencyContract().getContractNumber());
                        agencyContractTermsMapper.updateByExampleSelective(a.getAgencyContractTerms(),example1);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                printErrorMes("插入仅销售合同异常"+e.getMessage());
                throw new RuntimeException("插入仅销售合同异常");
            }
            flag = true;
        }
        return flag;
    }


    /**
     * 获取6-10 的随机位数数字
     *  想要生成的长度
     * @return result
     * 品牌编码（8位）+商户号（5位）+年份（2位）+序号（3位）
     */
    public synchronized static String getOrderContract()
    {
        Random random = new Random();
        DecimalFormat df = new DecimalFormat( "00" );
        String no = new SimpleDateFormat( "yyyyMMddHHmmss" ).format( new Date() ) + df.format( random.nextInt( 100 ) );
        return no;
    }



    @Override
    @Transactional
    public boolean auditContract(String companyId ,String contractNumber, String status,String auditStatus, String cause,String brandNo) {
        //合同状态 0待提交1运营审核中2运营审核通过3运营审核不通4财务审核中 5财务审核通过 6财务审核不通过7待签约8生效中9冻结10过期11合同结束

        String pdfUrl = "";
        if(StringUtils.isNoneEmpty(contractNumber)){
            String dbStart = "";
           if(status.equals(AgencyConstants.AgencyType.NOT_SUBMITTED.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
               //运营提交
               dbStart = AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString();
            }
            if(status.equals(AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
                //运营通过
                dbStart = AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString();
            }
            if(status.equals(AgencyConstants.AgencyType.OPERATING_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.NO.val.toString())){
                //运营不通过
                dbStart = AgencyConstants.AgencyType.OPERATING_AUDIT_REFUSED.code.toString();
            }
            if(status.equals(AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
                //财务审核提交
                dbStart = AgencyConstants.AgencyType.SIGN_UP.code.toString();
                CompanyInfo companyInfo = new CompanyInfo();
                companyInfo.setCompanyId(companyId);
                companyInfo.setAuditStatus(CompanyAuditStatus.SUCCESSJOIN.stringVal());
                companyInfoMapper.updateauditStatus(companyInfo);
                //生成pdf
                pdfUrl = createPdf(contractNumber);

                //作废本公司 本品牌的合同
                AgencyContractTermsExample example = new AgencyContractTermsExample();
                example.createCriteria().andCompanyIdEqualTo(companyId);
                List<AgencyContractTerms> list = agencyContractTermsMapper.selectByExample(example);
                for ( AgencyContractTerms s: list) {
                    AgencyContract record1 = new AgencyContract();
                    record1.setStatus("11");//作废
                    AgencyContractExample example1 = new AgencyContractExample();
                    example1.createCriteria().andContractNumberEqualTo(s.getContractNumber());
                    agencyContractMapper.updateByExampleSelective(record1,example1);
                }

            }
            if(status.equals(AgencyConstants.AgencyType.FINANCIAL_AUDIT_ING.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.NO.val.toString())){
                //财务审核不通过
                //dbStart = "6";
                dbStart = AgencyConstants.AgencyType.FINANCIAL_AUDIT_REFUSED.code.toString();
            }
            if(status.equals(AgencyConstants.AgencyType.SIGN_UP.code.toString()) && auditStatus.equals(SysConstants.YesOrNo.YES.val.toString())){
                //生效
                //dbStart = "8";
                dbStart = AgencyConstants.AgencyType.EFFECT_ING.code.toString();
            }
          AgencyContract record = new AgencyContract();
            record.setStatus(dbStart);
            record.setUpdateTime(new Date());
            record.setPdfUrl(pdfUrl);
            AgencyContractExample example = new AgencyContractExample();
            example.createCriteria().andContractNumberEqualTo(contractNumber);
             int  flag = agencyContractMapper.updateByExampleSelective(record,example);
             if(flag > 0  &&   Integer.valueOf(status) > 1  && Integer.valueOf(status) <= 7){
                 //插入审批记录
                 /* 添加审核记录表 */
                 UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
                 String	auditPersion = userVO == null ? "" : userVO.getName();
                 String	auditAccount = userVO == null ? "" : userVO.getUsername();
                 String  auditLevel = "1";
                 if(Integer.valueOf(status) >= 4){
                     auditLevel = "2";
                 }
                 PcAuditInfo auditRecord = new PcAuditInfo("7", auditLevel, auditPersion, auditStatus, new Date(), companyId, cause,
                         contractNumber, new Date(), auditAccount);
                  pcAuditInfoMapper.insertSelective( auditRecord );
             }

            return true;
        }

        return false;
    }

    /***
     * pdf 生成
     * @param
     * @param contractNumber
     * @param
     * @return
     */
    @Override
    public String  createPdf(String contractNumber){
        String pdfUrl = ""; /* 上传服务器返回地址 */
        Map<String, Object> root = new HashMap<>();
        //查询合同数据
        ParamAgency paramAgency = this.getParamAgency(contractNumber);
        Map<String, Object> a = (Map<String, Object>) JSON.toJSON(paramAgency);
        root.putAll(a);
        /* 特殊处理 */
        try {
            String filePath = FreemarkerUtils.savePdf(filePathDir + contractNumber, "4", root);
            /* 上传 */
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
        //冻结 0  解冻 1 作废2
        boolean flag = false;
      /*  AgencyContract record = new AgencyContract();
        if(status.equals("0")){//冻结
           record.setStatus("9");
        }else if(status.equals("1")){//解冻
            record.setStatus("8");
        }else if(status.equals("2")){//作废
            record.setStatus("11");
        }*/
        AgencyContract record = new AgencyContract();
        record.setStatus(status);
        try {
            AgencyContractExample  example = new AgencyContractExample();
            example.createCriteria().andContractNumberEqualTo(contractNumber);
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
        paramAgency.setAgencyContract(list.get(0));
        paramAgency.setAgencyContractTerms(listTwo.get(0));

        //获取 审批信息
        PcAuditInfoExample autit = new PcAuditInfoExample();
        autit.createCriteria().andContractNumberEqualTo(contractNumber).andAuditTypeEqualTo("7");
        List<PcAuditInfo>  auList =  pcAuditInfoMapper.selectByExample(autit);
        //查询审批信息
        paramAgency.setAuditInfo(auList);

        return paramAgency;
    }

    @Override
    public Map<String, String> getBrandNames() {
        Map<String, String> resMap = new HashMap<>(2);
        resMap.put("01","惠普");
        resMap.put("02","中华");
        return resMap;
    }

    @Override
    public Map<String, String> getCategoryNames() {
        Map<String, String> resMap = new HashMap<>(2);
        resMap.put("01","家电");
        resMap.put("02","家具");
        return resMap;
    }


    /**
     * 把公司信息初始化到map
     */
    public Map<String,String>  getDicCompanyName(){
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andRoleIdEqualTo( CompanyType.SJ.stringVal());
        List<CompanyInfo> list = companyInfoMapper.selectByExample(example);
        Map<String, String> maps = list.stream().collect(Collectors.toMap(CompanyInfo::getCompanyId,CompanyInfo::getCompanyName));
       return maps;
    }



}
