package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.RebateNode;

import java.util.List;

/**
 * 返款节点服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/18 18:01
 */
public interface RebateNodeService {

    List<RebateNode> findByType(Integer nodeType);
}
