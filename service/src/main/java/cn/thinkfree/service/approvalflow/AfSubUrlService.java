package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfSubUrl;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
public interface AfSubUrlService {

    List<AfSubUrl> findByConfigLogNo(String configLogNo);

    void create(String configLogNo, List<AfSubUrl> subUrls);
}
