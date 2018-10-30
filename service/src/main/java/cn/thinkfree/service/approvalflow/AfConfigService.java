package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.vo.AfConfigVO;

import java.util.List;

/**
 * 审批流配置服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
public interface AfConfigService {
    /**
     * 列出所有审批流配置
     * @return 所有审批流配置
     */
    List<AfConfigVO> list();

    /**
     * 审批流配置详情
     * @param configNo 审批流配置编号
     * @return 审批流配置详情
     */
    AfConfigVO detail(String configNo);

    /**
     * 添加审批流
     * @param configVO 审批流配置
     */
    void add(AfConfigVO configVO);

    /**
     * 修改审批流
     * @param configVO 审批流配置
     */
    void edit(AfConfigVO configVO);

    /**
     * 删除审批流
     * @param configNo 审批流配置编号
     */
    void delete(String configNo);

    /**
     * 根据审批流配置查询审批流
     * @param configNo 审批流配置编号
     * @return 审批流配置
     */
    AfConfig findByNo(String configNo);

    /**
     * 根据审批流别名查询审批流配置
     * @param alias 别名
     * @return 审批流配置
     */
    AfConfig findByAlias(String alias);

    /**
     * 根据审批流别名查询审批流配置编号
     * @param alias 别名
     * @return 审批流配置编号
     */
    String findConfigNoByAlias(String alias);
}
