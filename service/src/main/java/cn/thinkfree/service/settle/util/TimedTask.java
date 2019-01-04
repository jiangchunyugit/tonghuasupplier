package cn.thinkfree.service.settle.util;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.mapper.SettlementRatioInfoMapper;
import cn.thinkfree.database.mapper.SettlementRuleInfoMapper;
import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.model.SettlementRatioInfoExample;
import cn.thinkfree.database.model.SettlementRuleInfo;
import cn.thinkfree.database.model.SettlementRuleInfoExample;
import cn.thinkfree.service.constants.SettlementStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 规则比例定时刷新结算规则比例状态
 */
@Component
public class TimedTask extends AbsLogPrinter {

    @Autowired
    SettlementRuleInfoMapper settlementRuleInfoMapper;

    @Autowired
    SettlementRatioInfoMapper settlementRatioInfoMapper;

    @Scheduled(cron = "${schedules}")
    public void ruleTimedTask () {

        printInfoMes("规则定时任务开始");
        // 拼接查询条件
        SettlementRuleInfoExample settlementRuleInfoExample = new SettlementRuleInfoExample();
        // 待生效 刷新为生效
        settlementRuleInfoExample.createCriteria()
                .andStartTimeEqualTo(initDateByDay())
                .andStatusEqualTo(SettlementStatus.EffectiveWait.getCode());

        // 查询
        List<SettlementRuleInfo> settlementRuleInfoList = settlementRuleInfoMapper.selectByExample(settlementRuleInfoExample);
        for (SettlementRuleInfo settlementRuleInfo : settlementRuleInfoList) {
            SettlementRuleInfoExample example = new SettlementRuleInfoExample();
            example.createCriteria().andIdEqualTo(settlementRuleInfo.getId());

            // 生效
            settlementRuleInfo.setStatus(SettlementStatus.Effective.getCode());
            // 更新
            settlementRuleInfoMapper.updateByExampleSelective(settlementRuleInfo,example);
        }

        // 结束日期刷新为失效,作废
        SettlementRuleInfoExample settlementRuleInfoExample1 = new SettlementRuleInfoExample();
        settlementRuleInfoExample1.createCriteria()
                .andEndTimeEqualTo(initDateByDay())
                .andStatusNotEqualTo(SettlementStatus.AuditCAN.getCode());

        // 查询
        List<SettlementRuleInfo> settlementRuleInfoList1 = settlementRuleInfoMapper.selectByExample(settlementRuleInfoExample1);
        for (SettlementRuleInfo settlementRuleInfo : settlementRuleInfoList1) {
            SettlementRuleInfoExample example = new SettlementRuleInfoExample();
            example.createCriteria().andIdEqualTo(settlementRuleInfo.getId());

            if (SettlementStatus.AuditWait.getCode().equals(settlementRuleInfo.getStatus())) {
                // 作废
                settlementRuleInfo.setStatus(SettlementStatus.AuditCAN.getCode());
                // 作废标签
                settlementRuleInfo.setInvalidStatus(OneTrue.YesOrNo.YES.val.toString());
            } else if (SettlementStatus.CANDecline.getCode().equals(settlementRuleInfo.getStatus())
                    ||SettlementStatus.Effective.getCode().equals(settlementRuleInfo.getStatus())) {
                // 失效
                settlementRuleInfo.setStatus(SettlementStatus.Invalid.getCode());
            }

            // 更新
            settlementRuleInfoMapper.updateByExampleSelective(settlementRuleInfo,example);
        }
    }

    /**
     * 获得当天零时零分零秒
     * @return
     */
    public Date initDateByDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Scheduled(cron = "${schedules}")
    public void ratioTimedTask () {

        printInfoMes("比例定时任务开始");
        // 拼接查询条件
        SettlementRatioInfoExample settlementRatioInfoExample = new SettlementRatioInfoExample();
        settlementRatioInfoExample.createCriteria()
                .andEffectStartTimeEqualTo(initDateByDay())
                .andStatusEqualTo(SettlementStatus.EffectiveWait.getCode());

        // 查询
        List<SettlementRatioInfo> settlementRatioInfoList = settlementRatioInfoMapper.selectByExample(settlementRatioInfoExample);
        for (SettlementRatioInfo settlementRatioInfo : settlementRatioInfoList) {
            SettlementRatioInfoExample example = new SettlementRatioInfoExample();
            example.createCriteria().andIdEqualTo(settlementRatioInfo.getId());

            // 生效
            settlementRatioInfo.setStatus(SettlementStatus.Effective.getCode());
            // 更新
            settlementRatioInfoMapper.updateByExampleSelective(settlementRatioInfo,example);
        }

        // 结束日期刷新为失效,作废
        SettlementRatioInfoExample settlementRatioInfoExample1 = new SettlementRatioInfoExample();
        settlementRatioInfoExample1.createCriteria()
                .andEffectEndTimeEqualTo(initDateByDay())
                .andStatusNotEqualTo(SettlementStatus.AuditCAN.getCode());

        // 查询
        List<SettlementRatioInfo> settlementRatioInfoList1 = settlementRatioInfoMapper.selectByExample(settlementRatioInfoExample1);
        for (SettlementRatioInfo settlementRatioInfo : settlementRatioInfoList1) {
            SettlementRatioInfoExample example = new SettlementRatioInfoExample();
            example.createCriteria().andIdEqualTo(settlementRatioInfo.getId());

            if (SettlementStatus.AuditWait.getCode().equals(settlementRatioInfo.getStatus())) {
                // 作废
                settlementRatioInfo.setStatus(SettlementStatus.AuditCAN.getCode());
                // 作废标签
//                settlementRatioInfo.setInvalidStatus(OneTrue.YesOrNo.YES.val.toString());
            } else if (SettlementStatus.CANDecline.getCode().equals(settlementRatioInfo.getStatus())
                    ||SettlementStatus.Effective.getCode().equals(settlementRatioInfo.getStatus())) {
                // 失效
                settlementRatioInfo.setStatus(SettlementStatus.Invalid.getCode());
            }

            // 更新
            settlementRatioInfoMapper.updateByExampleSelective(settlementRatioInfo,example);
        }
    }
}
