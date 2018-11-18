package cn.thinkfree.service.settle.rule;

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
     * @return pageList
     */
    PageInfo<SettlementRuleInfo> pageSettlementRuleBySEO(SettlementRuleSEO settlementRuleSEO);


    /**
     *
     * 创建/修改 结算规则
     * @return boolean
     */
    boolean insertOrupdateSettlementRule(SettlementRuleVO settlementRuleVO);


    /**
     *
     * 拷贝结算规则
     * @return boolean
     */
    boolean copySettlementRule(String ruleNumber);



    /**
     *
     * 查看结算规则
     * @return SettlementRuleInfo
     */
    SettlementRuleVO getSettlementRule(String ruleNumber);


    /**
     *
     * 作废结算规则
     * @return SettlementRuleInfo
     */
    boolean cancellatSettlementRule(String ruleNumber);

    /**
     *
     * 申请作废结算规则
     * @return SettlementRuleInfo
     */
    boolean applicationInvalid(String ruleNumber);


    /**
     * 获取费用名称从埃森哲获取
     * @return
     */
    Map<String,String> getCostNames();

    Map<String,String> getPlateFormNames();

    /**
     * 导出数据 （根据数据导出）
     * @return null
     */
    void exportList(SettlementRuleSEO settlementRuleSEO, HttpServletResponse response);

    /**
     * 批量审核
     * @return
     */
    boolean  batchcCheckSettlementRule(List<String> ruleNumbers, String auditStatus, String auditCase);

    /**
     * 合同获取规则
     * @param settlementRuleInfo
     * @return
     */
    List<SettlementRuleContractVO> getSettlementRuleContract(SettlementRuleInfo settlementRuleInfo);

    /**
     *
     * 编辑作废 结算规则
     * @return boolean
     */
    boolean updateSettlementRule(SettlementRuleVO settlementRuleVO);
}
