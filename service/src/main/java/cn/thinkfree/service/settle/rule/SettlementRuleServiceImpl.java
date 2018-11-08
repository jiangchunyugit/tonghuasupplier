package cn.thinkfree.service.settle.rule;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.DateUtils;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.settle.SettlementRuleContractVO;
import cn.thinkfree.database.vo.settle.SettlementRuleSEO;
import cn.thinkfree.database.vo.settle.SettlementRuleVO;
import cn.thinkfree.service.constants.SettlementRuleStatus;
import cn.thinkfree.service.constants.SettlementStatus;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.thinkfree.core.logger.AbsLogPrinter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
public class SettlementRuleServiceImpl extends AbsLogPrinter implements SettlementRuleService {

    @Autowired
    SettlementRuleInfoMapper settlementRuleInfoMapper;

    @Autowired
    SettlementMethodInfoMapper settlementMethodInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Override
    public PageInfo<SettlementRuleInfo> pageSettlementRuleBySEO (SettlementRuleSEO rule) {

        PageHelper.startPage(rule.getPage(), rule.getRows());
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        this.searchRef(example,rule);
        List<SettlementRuleInfo> list = settlementRuleInfoMapper.selectByExample(example);

       this.resetStatus(list);
        printInfoMes("查询 结算比例数量 {}", list.size());
        return new PageInfo<>(list);
    }

    private Integer datecompare(Date date) {

        Date date1 = new Date();
        int compareTo = date1.compareTo(date);
        return compareTo;
    }

    @Override
    public boolean insertOrupdateSettlementRule (SettlementRuleVO settlementRuleVO) {

        int flag = 0;
        if (settlementRuleVO != null) {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            String auditPersion = userVO == null ? "" : userVO.getUsername();
            if (StringUtils.isEmpty(settlementRuleVO.getRuleNumber())) {
                settlementRuleVO.setCreateTime(new Date());
                settlementRuleVO.setUpdateTime(new Date());
                settlementRuleVO.setCreateUser(auditPersion);
                // 新增未待审核
                settlementRuleVO.setStatus(SettlementStatus.AuditWait.getCode());
                settlementRuleVO.setRuleNumber(getRuleNumber());
//                SettlementRuleInfo settlementRuleInfo = new SettlementRuleInfo();
//                SpringBeanUtil.copy(settlementRuleVO, settlementRuleInfo);
                try {
                    flag = settlementRuleInfoMapper.insertSelective(settlementRuleVO);
                    settlementRuleVO.getSettlementMethodInfos().forEach(e -> {

                        e.setRuleCode(settlementRuleVO.getId().toString());
                        settlementMethodInfoMapper.insertSelective(e);
                    });
                } catch (Exception e) {
                    printErrorMes("添加结算规则{}" + e.getMessage());
                }
            }

            if (flag > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean copySettlementRule (String ruleNumber) {

        if(!StringUtils.isEmpty(ruleNumber)){
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            String auditPersion = userVO == null ? "" : userVO.getUsername();
            SettlementRuleInfoExample example = new SettlementRuleInfoExample();
            example.createCriteria().andRuleNumberEqualTo(ruleNumber);
            List<SettlementRuleInfo>  list = settlementRuleInfoMapper.selectByExample(example);
            SettlementRuleInfo ralio = list.get(0);
            ralio.setId(null);
            ralio.setRuleNumber(getRuleNumber());
            ralio.setCreateTime(new Date());
            ralio.setUpdateTime(new Date());
            ralio.setCreateUser(auditPersion);
            int  falg = settlementRuleInfoMapper.insertSelective(ralio);
            if(falg > 0 ){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


    @Override
    public SettlementRuleVO getSettlementRule (String ruleNumber) {

        SettlementRuleVO settlementRuleVO = new SettlementRuleVO();
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        example.createCriteria().andRuleNumberNotEqualTo(ruleNumber);
        List<SettlementRuleInfo>  list = settlementRuleInfoMapper.selectByExample(example);
        SettlementRuleInfo rule = (list!=null && list.size() > 0)?list.get(0):null;

        if(rule != null && StringUtils.isNotBlank(rule.getFeeName())){

            Map<String, String> paream = getCostNames();
            //翻译
            rule.setFeeName(paream.get(rule.getFeeName()));
            SettlementMethodInfoExample settlementMethodInfoExample = new SettlementMethodInfoExample();
            settlementMethodInfoExample.createCriteria().andRuleCodeEqualTo(rule.getId().toString());
            List<SettlementMethodInfo> settlementMethodInfos = settlementMethodInfoMapper.selectByExample(settlementMethodInfoExample);
            SpringBeanUtil.copy(rule,settlementRuleVO);
            settlementRuleVO.setSettlementMethodInfos(settlementMethodInfos);
        }

        //获取 审批信息
        PcAuditInfoExample autit = new PcAuditInfoExample();
        autit.createCriteria().andContractNumberEqualTo(ruleNumber).andAuditTypeEqualTo("4");
        List<PcAuditInfo>  auList =  pcAuditInfoMapper.selectByExample(autit);
        settlementRuleVO.setAuditInfo(auList);
        return settlementRuleVO;
    }




    @Override
    public boolean cancellatSettlementRule (String ruleNumber) {
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        example.createCriteria().andRuleNumberEqualTo(ruleNumber);
        SettlementRuleInfo record = new SettlementRuleInfo();
        //作废
        record.setStatus(SettlementStatus.AuditCAN.getCode());
        int  falg = 	settlementRuleInfoMapper.updateByExampleSelective(record, example);
        if(falg >  0 ){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> getCostNames () {
        Map<String, String> map = new HashMap<>();
        map.put("01", "设计费");
        map.put("02", "施工费");
        map.put("03", "物业费");
        return map;
    }

    public synchronized static String getRuleNumber () {

        Random random = new Random();
        DecimalFormat df = new DecimalFormat("00");
        String no = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + df.format(random.nextInt(100));
        return no;
    }

    @Override
    public void exportList (SettlementRuleSEO rule, HttpServletResponse response) {
        ExcelData data = new ExcelData();
        String title = "合同信息数据";
        data.setName(title);
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("序号");
        titles.add("testid");
        titles.add("结算规则编号");
        titles.add("结算规则名称");
        titles.add("结算规则有效期");
        titles.add("test状态");
        titles.add("创建时间");
        titles.add("创建人");
        titles.add("结算规则状态");
        titles.add("系统生成对账单时间");
        titles.add("结算周期");
        titles.add("结算办法");
        titles.add("备注");
//        titles.add("审核状态");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList<>();
        List<Object> row = null;
        PageHelper.startPage(rule.getPage(),rule.getRows());
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();

        this.searchRef(example,rule);
        List<SettlementRuleInfo> list = settlementRuleInfoMapper.selectByExample(example);
//		for (int i = 0; i < list.size(); i++) {
//			list.get(i).setStatus(status);(ContractStatus.getDesc(list.get(i).getContractStatus()));
//		}
        this.resetStatus(list);
        for(int i=0; i<list.size();i++){

            BillCycleResult billCycleResult = this.billCycleConvert(list.get(i));
            row=new ArrayList<>();
            row.add(i+1);
            row.add(list.get(i).getId());
            row.add(list.get(i).getRuleNumber());
            row.add(list.get(i).getRuleName());
            row.add(DateUtils.dateToDateTime(list.get(i).getStartTime())+"———"+DateUtils.dateToDateTime(list.get(i).getEndTime()));
            row.add(list.get(i).getStatus());
            row.add(DateUtils.dateToDateTime(list.get(i).getCreateTime()));
            row.add(list.get(i).getCreateUser());
//            row.add("有问题 待确认");
            row.add(SettlementStatus.getDesc(list.get(i).getStatus()));
            row.add(billCycleResult.getCheckTime());
            row.add(billCycleResult.getCycleTime());
            row.add(billCycleResult.getSettlementMethod());
            row.add(list.get(i).getRemark());
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String fileName=title+"_"+fdate.format(new Date())+".xls";
        try {
            ExcelUtils.exportExcel(response, fileName, data);
        } catch (Exception e) {
            printErrorMes("结算规则导出异常",e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchcCheckSettlementRule(List<String> ruleNumbers,String auditStatus,String auditCase) {


        for (int i = 0; i < ruleNumbers.size(); i++) {
            //添加审核记录表
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            String auditPersion = userVO == null ? "" : userVO.getUsername();
            String companyId = userVO==null?"":userVO.getCompanyID();
            String ruleNumber = String.valueOf(ruleNumbers.get(i));
            PcAuditInfo record = new PcAuditInfo("4", "2",
                    auditPersion, auditStatus, new Date(),
                    companyId, auditCase, String.valueOf(ruleNumbers.get(i)));
            //插入审核记录
            pcAuditInfoMapper.insertSelective(record);
            //修改状态
            SettlementRuleInfoExample example = new SettlementRuleInfoExample();
            example.createCriteria().andRuleNumberEqualTo(ruleNumber);
            SettlementRuleInfo recordT = new SettlementRuleInfo();

            List<SettlementRuleInfo> settlementRuleInfos = settlementRuleInfoMapper.selectByExample(example);
            SettlementRuleInfo settlementRuleInfo = (settlementRuleInfos!=null && settlementRuleInfos.size() > 0)?settlementRuleInfos.get(0):null;
            if (settlementRuleInfo == null) {
                return false;
            }
            if (settlementRuleInfo.getStatus().equals(SettlementStatus.CANDecline.getCode())) {

                if (auditStatus.equals(SettlementStatus.AuditPass.getCode())) {

                 recordT.setStatus(SettlementStatus.AuditCAN.getCode());
                } else {
                    recordT.setStatus(auditStatus);
                }
            } else {
                recordT.setStatus(auditStatus);
            }
            settlementRuleInfoMapper.updateByExampleSelective(recordT, example);
            return true;
        }
        return false;
    }

    private void searchRef (SettlementRuleInfoExample example,SettlementRuleSEO rule) {

        if(!StringUtils.isBlank(rule.getRuleNumber())){
            example.createCriteria().andRuleNumberLike("%"+rule.getRuleNumber()+"%");
        }
        if(!StringUtils.isBlank(rule.getRuleName())){
            example.createCriteria().andRuleNameLike("%"+rule.getRuleName()+"%");
        }
        if(!StringUtils.isBlank(rule.getCreateUser())){
            example.createCriteria().andCreateUserLike("%"+rule.getCreateUser()+"%");
        }
        if(rule.getStartTime() !=null && !StringUtils.isBlank(rule.getStartTime()+"")){
            example.createCriteria().andStartTimeGreaterThanOrEqualTo(rule.getStartTime());
        }
        if(rule.getEndTime() != null && !StringUtils.isBlank(rule.getEndTime()+"")){
            example.createCriteria().andEndTimeLessThanOrEqualTo(rule.getEndTime());
        }

        if(!StringUtils.isEmpty(rule.getRuleStatus())){
            //1 待审核 2审核通过 3审核不通过 4作废 5申请作废 7生效 8失效 9未生效

            // 生效
            if(SettlementStatus.Effective.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andStatusEqualTo(SettlementStatus.AuditPass.getCode())
                        .andStartTimeLessThanOrEqualTo(new Date())
                        .andEndTimeGreaterThanOrEqualTo(new Date());
            // 失效
            }else if(SettlementStatus.Invalid.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andEndTimeLessThan(new Date());
            // 待生效
            }else if(SettlementStatus.EffectiveWait.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andStartTimeGreaterThan(new Date())
                        .andStatusEqualTo(SettlementStatus.AuditPass.getCode());
            // 已作废
            }else if(SettlementStatus.AuditCAN.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andStatusEqualTo(SettlementStatus.AuditCAN.getCode());
            // 申请作废
            }else if (SettlementStatus.CANDecline.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andStatusEqualTo(SettlementStatus.CANDecline.getCode());

            // 待审核
            }else if (SettlementStatus.AuditWait.getCode().equals(rule.getRuleStatus())){
                example.createCriteria()
                        .andStatusEqualTo(SettlementStatus.AuditWait.getCode());
            }
        }
    }

    @Override
    public List<SettlementRuleContractVO> getSettlementRuleContract(SettlementRuleInfo settlementRuleInfo) {

        return settlementRuleInfoMapper.selectBycontract(settlementRuleInfo);
    }

    @Override
    public boolean applicationInvalid(String ruleNumber) {
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        example.createCriteria().andRuleNumberEqualTo(ruleNumber);
        SettlementRuleInfo record = new SettlementRuleInfo();
        //申请作废
        record.setStatus(SettlementStatus.CANDecline.getCode());
        int  falg = 	settlementRuleInfoMapper.updateByExampleSelective(record, example);
        if(falg >  0 ){
            return true;
        }
        return false;
    }

    private void resetStatus(List<SettlementRuleInfo> list) {

        for (SettlementRuleInfo e : list) {
            if (!SettlementStatus.AuditCAN.getCode().equals(e.getStatus()) &&
                    !SettlementStatus.CANDecline.getCode().equals(e.getStatus())) {

                if (SettlementStatus.AuditPass.getCode().equals(e.getStatus())) {

                    if (this.datecompare(e.getStartTime())>=0 && this.datecompare(e.getEndTime())<= 0){

                        e.setStatus(SettlementStatus.Effective.getCode());
                    } else if (this.datecompare(e.getStartTime())<=0) {

                        e.setStatus(SettlementStatus.EffectiveWait.getCode());
                    }else if (this.datecompare(e.getEndTime())>=0) {

                        e.setStatus(SettlementStatus.Invalid.getCode());
                    }
                }else if (this.datecompare(e.getEndTime())>=0) {

                    e.setStatus(SettlementStatus.Invalid.getCode());
                }
            }
        }
    }

    /**
     * 合同周期状态转换
     * @param settlementRuleInfo
     * @return
     */
    private BillCycleResult billCycleConvert(SettlementRuleInfo settlementRuleInfo) {

        BillCycleResult billCycleResult = new BillCycleResult();

        StringBuilder method = new StringBuilder();
        billCycleResult.setCheckTime(DateUtils.dateToDateTime(settlementRuleInfo.getCheckingTime()));

        if(SettlementRuleStatus.LastDaySettlement.getCode().equals(settlementRuleInfo.getCycleType())) {
            method.append(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));
//            billCycleResult.setSettlementMethod(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));

        }else if (SettlementRuleStatus.NaturalMonthSettlement.getCode().equals(settlementRuleInfo.getCycleType())) {

            billCycleResult.setCycleTime(String.valueOf(settlementRuleInfo.getCycleStime().getDay()));
            method.append(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));
//            billCycleResult.setSettlementMethod(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));
        }
        else if (SettlementRuleStatus.NextMonthSettlement.getCode().equals(settlementRuleInfo.getCycleType())) {

            billCycleResult.setCycleTime(String.valueOf(settlementRuleInfo.getCycleStime().getDay()));
            method.append(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));
//            billCycleResult.setSettlementMethod(SettlementRuleStatus.getDesc(settlementRuleInfo.getCycleType()));
        } else if (SettlementRuleStatus.WeekSettlement.getCode().equals(settlementRuleInfo.getCycleType())){

            method.append("每周");
            method.append(this.weekConvert(Integer.valueOf(settlementRuleInfo.getCycleValue())));
            method.append("结算");
//            billCycleResult.setSettlementMethod("每周"+this.weekConvert(Integer.valueOf(settlementRuleInfo.getCycleValue()))+"结算");
        } else if (SettlementRuleStatus.DaySettlement.getCode().equals(settlementRuleInfo.getCycleType())){

            method.append("每");
            method.append(this.weekConvert(Integer.valueOf(settlementRuleInfo.getCycleValue())));
            method.append("天结算一次");
//            billCycleResult.setSettlementMethod("每"+settlementRuleInfo.getCycleValue()+"结算一次");
        }

        billCycleResult.setSettlementMethod(method.toString());
        return billCycleResult;
    }

    /**
     * 星期转化
     * @param number
     * @return
     */
    private String weekConvert(Integer number) {

        String day = "";
        switch(number){
            case 1:day="一";break;
            case 2:day="二";break;
            case 3:day="三";break;
            case 4:day="四";break;
            case 5:day="五";break;
            case 6:day="六";break;
            case 7:day="日";break;
        }

        return day;
    }

    class BillCycleResult{

        /**
         * 对账时间
         */
        private String checkTime;

        /**
         * 结算周期
         */
        private String cycleTime;

        /**
         * 结算方案
         */
        private String settlementMethod;

//        public BillCycleResult (String checkTime,String cycleTime ,String settlementMethod){
//
//            this.checkTime = checkTime;
//            this.cycleTime = cycleTime;
//            this.settlementMethod = settlementMethod;
//        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
        }

        public String getCycleTime() {
            return cycleTime;
        }

        public void setCycleTime(String cycleTime) {
            this.cycleTime = cycleTime;
        }

        public String getSettlementMethod() {
            return settlementMethod;
        }

        public void setSettlementMethod(String settlementMethod) {
            this.settlementMethod = settlementMethod;
        }
    }
}
