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
        settlementRuleInfoExample.createCriteria()
                .andStartTimeLessThanOrEqualTo(new Date())
                .andStatusNotEqualTo(SettlementStatus.AuditCAN.getCode());

        // 查询
        List<SettlementRuleInfo> settlementRuleInfoList = settlementRuleInfoMapper.selectByExample(settlementRuleInfoExample);
        for (SettlementRuleInfo settlementRuleInfo : settlementRuleInfoList) {
            SettlementRuleInfoExample example = new SettlementRuleInfoExample();
            example.createCriteria().andIdEqualTo(settlementRuleInfo.getId());
            // 失效
            if (this.datecompare(settlementRuleInfo.getEndTime())>0) {

                if (SettlementStatus.AuditWait.getCode().equals(settlementRuleInfo.getStatus())) {
                    // 作废
                    settlementRuleInfo.setStatus(SettlementStatus.AuditCAN.getCode());
                    // 作废标签
                    settlementRuleInfo.setInvalidStatus(OneTrue.YesOrNo.YES.toString());
                } else if (SettlementStatus.CANDecline.getCode().equals(settlementRuleInfo.getStatus())
                        ||SettlementStatus.AuditPass.getCode().equals(settlementRuleInfo.getStatus())) {
                    // 失效
                    settlementRuleInfo.setStatus(SettlementStatus.Invalid.getCode());
                }
            } else if (SettlementStatus.AuditPass.getCode().equals(settlementRuleInfo.getStatus())){
                // 生效
                settlementRuleInfo.setStatus(SettlementStatus.Effective.getCode());
            }

            // 更新
            settlementRuleInfoMapper.updateByExampleSelective(settlementRuleInfo,example);
        }
    }

    /**
     * 跟当前时间比较
     * @param date
     * @return
     */
    private Integer datecompare(Date date) {

        Date date1 = new Date();
        int compareTo = date1.compareTo(date);
        return compareTo;
    }

    @Scheduled(cron = "${schedules}")
    public void ratioTimedTask () {

        printInfoMes("比例定时任务开始");
        // 拼接查询条件
        SettlementRatioInfoExample settlementRatioInfoExample = new SettlementRatioInfoExample();
        settlementRatioInfoExample.createCriteria()
                .andEffectStartTimeLessThanOrEqualTo(new Date())
                .andStatusNotEqualTo(SettlementStatus.AuditCAN.getCode());

        // 查询
        List<SettlementRatioInfo> settlementRatioInfoList = settlementRatioInfoMapper.selectByExample(settlementRatioInfoExample);
        for (SettlementRatioInfo settlementRatioInfo : settlementRatioInfoList) {
            SettlementRatioInfoExample example = new SettlementRatioInfoExample();
            example.createCriteria().andIdEqualTo(settlementRatioInfo.getId());
            // 失效
            if (this.datecompare(settlementRatioInfo.getEffectEndTime())>0) {

                if (SettlementStatus.AuditWait.getCode().equals(settlementRatioInfo.getStatus())) {
                    // 作废
                    settlementRatioInfo.setStatus(SettlementStatus.AuditCAN.getCode());
                } else if (SettlementStatus.CANDecline.getCode().equals(settlementRatioInfo.getStatus())
                        ||SettlementStatus.AuditPass.getCode().equals(settlementRatioInfo.getStatus())) {
                    // 失效
                    settlementRatioInfo.setStatus(SettlementStatus.Invalid.getCode());
                }
            } else if (SettlementStatus.AuditPass.getCode().equals(settlementRatioInfo.getStatus())){
                // 生效
                settlementRatioInfo.setStatus(SettlementStatus.Effective.getCode());
            }

            // 更新
            settlementRatioInfoMapper.updateByExampleSelective(settlementRatioInfo,example);
        }
    }
}
