package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.SystemMessage;

import java.util.List;
import java.util.Map;

public interface SystemMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemMessage record);

    int insertSelective(SystemMessage record);

    SystemMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemMessage record);

    int updateByPrimaryKey(SystemMessage record);

    List<SystemMessage> selectByParam(Map<String, Object> param);
}