package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.vo.AfConfigVO;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
public interface AfConfigService {
    List<AfConfigVO> list();

    AfConfigVO detail(String configNo);

    void update(AfConfigVO configVO);

    AfConfig findByConfigNo(String configNo);

    AfConfig findByAlias(String alias);
}
