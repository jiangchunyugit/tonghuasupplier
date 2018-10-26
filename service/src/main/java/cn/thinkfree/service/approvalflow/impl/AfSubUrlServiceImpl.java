package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfSubUrlMapper;
import cn.thinkfree.database.model.AfSubUrl;
import cn.thinkfree.database.model.AfSubUrlExample;
import cn.thinkfree.service.approvalflow.AfSubUrlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfSubUrlServiceImpl implements AfSubUrlService {

    @Resource
    private AfSubUrlMapper subUrlMapper;

    @Override
    public List<AfSubUrl> findByConfigLogNo(String configLogNo) {
        AfSubUrlExample example = new AfSubUrlExample();
        example.createCriteria().andConfigLogNoEqualTo(configLogNo);
        return subUrlMapper.selectByExample(example);
    }

    @Override
    public void create(String configLogNo, List<AfSubUrl> subUrls) {
        if (subUrls != null) {
            for (AfSubUrl subUrl : subUrls) {
                subUrl.setId(null);
                subUrl.setConfigLogNo(configLogNo);
                insert(subUrl);
            }
        }
    }
    private void insert(AfSubUrl subUrl) {
        subUrlMapper.insertSelective(subUrl);
    }
}
