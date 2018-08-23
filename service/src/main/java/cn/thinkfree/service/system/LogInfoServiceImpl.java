package cn.thinkfree.service.system;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.mapper.LogInfoMapper;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.model.LogInfoExample;
import cn.thinkfree.database.vo.LogInfoSEO;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogInfoServiceImpl implements LogInfoService {

    MyLogger myLogger = LogUtil.getLogger(LogInfoService.class);

    @Autowired
    LogInfoMapper logInfoMapper;

    @Transactional
    @Override
    public void save(LogInfo logInfo) {
        logInfoMapper.insertSelective(logInfo);
    }

    @Transactional
    @Override
    public PageInfo<LogInfo> pageLogInfo(LogInfoSEO logInfoSEO) {


        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();


        LogInfoExample logInfoExample = new LogInfoExample();
        logInfoExample.setOrderByClause(" create_time desc");
        LogInfoExample.Criteria criteria = logInfoExample.createCriteria();

        criteria.andCompanyIdIn(userVO.getRelationMap());

        if(StringUtils.isNotBlank(logInfoSEO.getPerson())){
            criteria.andOperatorEqualTo(logInfoSEO.getPerson());
        }
        if(null != logInfoSEO.getDate()){
            criteria.andCreateTimeEqualTo(logInfoSEO.getDate());
        }
        PageHelper.startPage(logInfoSEO.getPage(),logInfoSEO.getRows());
        List<LogInfo> list = logInfoMapper.selectByExample(logInfoExample);
        return new PageInfo<>(list);
    }
}
