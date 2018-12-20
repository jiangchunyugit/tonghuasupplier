package cn.thinkfree.service.settle.rule;

import cn.thinkfree.database.model.RebateNode;
import cn.thinkfree.database.model.SettlementRuleInfo;
import cn.thinkfree.database.vo.settle.SettlementRuleContractVO;
import cn.thinkfree.database.vo.settle.SettlementRuleSEO;
import cn.thinkfree.database.vo.settle.SettlementRuleVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 规则接口
 */
public interface SettlementRuleService {

    /**
     * 根据条件分页查询 合同数据
     * @param settlementRuleSEO
     * @return
     */
    PageInfo<SettlementRuleInfo> pageSettlementRuleBySEO(SettlementRuleSEO settlementRuleSEO);


    /**
     * 创建 结算规则
     * @param settlementRuleVO
     * @return
     */
    boolean insertOpiateSettlementRule(SettlementRuleVO settlementRuleVO);

    /**
     * 拷贝结算规则
     * @param ruleNumber
     * @return
     */
    boolean copySettlementRule(String ruleNumber);

    /**
     * 查看结算规则
     * @param ruleNumber
     * @return
     */
    SettlementRuleVO getSettlementRule(String ruleNumber);

    /**
     * 作废结算规则
     * @param ruleNumber
     * @return
     */
    boolean cancelledSettlementRule(String ruleNumber);

    /**
     * 申请作废结算规则
     * @param ruleNumber
     * @return
     */
    boolean applicationInvalid(String ruleNumber);


    /**
     * 获取费用名称  代收款费用名称
     * @return
     */
    Map<String,String> getCostNames();

    /**
     * 获取费用名称  平台费用名称
     * @return
     */
    Map<String,String> getPlateFormNames();

    /**
     * 导出数据 （根据数据导出）
     * @param settlementRuleSEO
     * @param response
     */
    void exportList(SettlementRuleSEO settlementRuleSEO, HttpServletResponse response);

    /**
     * 批量审核
     * @param ruleNumbers
     * @param auditStatus
     * @param auditCase
     * @return
     */
    boolean batchCheckSettlementRule(List<String> ruleNumbers, String auditStatus, String auditCase);

    /**
     * 合同获取规则
     * @param settlementRuleInfo
     * @return
     */
    List<SettlementRuleContractVO> getSettlementRuleContract(SettlementRuleInfo settlementRuleInfo);

    /**
     * 编辑作废 结算规则
     * @param settlementRuleVO
     * @return
     */
    boolean updateSettlementRule(SettlementRuleVO settlementRuleVO);


    /**
     * 查询结算规则结算节点
     *
     */
    List<RebateNode> getCostNamesForRebateNode(String costType);
}
