package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfigScheme;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:01
 */
public interface AfConfigSchemeService {
    
    List<AfConfigScheme> findBySchemeNo(String schemeNo);

    AfConfigScheme findByConfigNoAndSchemeNo(String configNo, String schemeNo);

    void create(String configSchemeNo, String configNo, String schemeNo, String describe, String userId);

}
