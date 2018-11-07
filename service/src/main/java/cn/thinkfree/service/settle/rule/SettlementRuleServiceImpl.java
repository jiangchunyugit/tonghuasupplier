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
import cn.thinkfree.database.vo.settle.SettlementRuleSEO;
import cn.thinkfree.database.vo.settle.SettlementRuleVO;
import cn.thinkfree.service.constants.SettlementStatus;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;
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
    public PageInfo<SettlementRuleInfo> pageSettlementRuleBySEO(SettlementRuleSEO rule) {
        PageHelper.startPage(rule.getPage(), rule.getRows());
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        this.searchRef(example,rule);
        List<SettlementRuleInfo> list = settlementRuleInfoMapper.selectByExample(example);
        printInfoMes("查询 结算比例数量 {}", list.size());
        return new PageInfo<>(list);
    }

    @Override
    public boolean insertOrupdateSettlementRule(SettlementRuleVO settlementRuleVO) {
        int flag = 0;
        if (settlementRuleVO != null) {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            String auditPersion = userVO == null ? "" : userVO.getUsername();
            if (StringUtils.isEmpty(settlementRuleVO.getRuleNumber())) {
                settlementRuleVO.setCreateTime(new Date());
                settlementRuleVO.setUpdateTime(new Date());
                settlementRuleVO.setCreateUser(auditPersion);
                settlementRuleVO.setStatus(SettlementStatus.AuditWait.getCode());// 新增未待审核
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
//            } else {
//                settlementRuleVO.setUpdateTime(new Date());
//                settlementRuleVO.setCreateUser(auditPersion);
//                try {
//                    SettlementRuleInfoExample example = new SettlementRuleInfoExample();
//                    example.createCriteria().andRuleNumberEqualTo(settlementRuleVO.getRuleNumber());
//                    flag = settlementRuleInfoMapper.updateByExampleSelective(settlementRuleVO, example);
//                } catch (Exception e) {
//                    printErrorMes("添加结算规则{}" + e.getMessage());
//                }
//            }

            if (flag > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean copySettlementRule(String ruleNumber) {
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
    public SettlementRuleVO getSettlementRule(String ruleNumber) {

        SettlementRuleVO settlementRuleVO = new SettlementRuleVO();
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        example.createCriteria().andRuleNumberNotEqualTo(ruleNumber);
        List<SettlementRuleInfo>  list = settlementRuleInfoMapper.selectByExample(example);
        SettlementRuleInfo rule = (list!=null && list.size() > 0)?list.get(0):null;

        if(rule != null && StringUtils.isNotBlank(rule.getFeeName())){
            Map<String, String> paream = getCostNames();
            rule.setCollectionType(paream.get(rule.getFeeName()));//翻译

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
    public boolean cancellatSettlementRule(String ruleNumber) {
        SettlementRuleInfoExample example = new SettlementRuleInfoExample();
        example.createCriteria().andRuleNumberEqualTo(ruleNumber);
        SettlementRuleInfo record = new SettlementRuleInfo();
        record.setStatus(SettlementStatus.AuditCAN.getCode());//作废
        int  falg = 	settlementRuleInfoMapper.updateByExampleSelective(record, example);
        if(falg >  0 ){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> getCostNames() {
        Map<String, String> map = new HashMap<>();
        map.put("01", "设计费");
        map.put("02", "施工费");
        map.put("03", "物业费");
        return map;
    }

    public synchronized static String getRuleNumber() {

        Random random = new Random();
        DecimalFormat df = new DecimalFormat("00");
        String no = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + df.format(random.nextInt(100));
        return no;
    }

    @Override
    public void exportList(SettlementRuleSEO rule, HttpServletResponse response) {
        ExcelData data = new ExcelData();
        String title = "合同信息数据";
        data.setName(title);
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("规则编号");
        titles.add("规则名称");
        titles.add("规则有效期");
        titles.add("创建时间");
        titles.add("创建人");
        titles.add("规则状态");
        titles.add("审核状态");
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
        for(int i=0; i<list.size();i++){
            row=new ArrayList<>();
            row.add(list.get(i).getRuleNumber());
            row.add(list.get(i).getRuleName());
            row.add(DateUtils.dateToDateTime(list.get(i).getStartTime())+"———"+DateUtils.dateToDateTime(list.get(i).getStartTime()));
            row.add(DateUtils.dateToDateTime(list.get(i).getCreateTime()));
            row.add(list.get(i).getCreateUser());
            row.add("有问题 待确认");
            row.add(SettlementStatus.getDesc(list.get(i).getStatus()));
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
    @Transactional
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
            recordT.setStatus(auditStatus);
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
        if(!StringUtils.isBlank(rule.getAuditStatus())){
            example.createCriteria().andStatusEqualTo(rule.getAuditStatus());
        }

        if(!StringUtils.isEmpty(rule.getRuleStatus())){
            if(rule.getRuleStatus().equals("0")){//0生效 1失效 2作废 3未生效
                example.createCriteria()
                        .andStartTimeGreaterThanOrEqualTo(new Date())
                        .andEndTimeLessThanOrEqualTo(new Date());
            }else if(rule.getRuleStatus().equals("1")){
                example.createCriteria()
                        .andEndTimeGreaterThan(new Date());
            }else if(rule.getRuleStatus().equals("2")){
                example.createCriteria().andStatusEqualTo("4");
            }else if(rule.getRuleStatus().equals("3")){
                example.createCriteria().andStartTimeLessThan(new Date());
            }
        }
    }
}
