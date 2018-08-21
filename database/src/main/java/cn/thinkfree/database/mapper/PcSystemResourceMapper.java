package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.PcSystemResource;
import cn.thinkfree.database.vo.PcSystemResourceVo;

import java.util.List;

public interface PcSystemResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PcSystemResource record);

    int insertSelective(PcSystemResource record);

    PcSystemResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PcSystemResource record);

    int updateByPrimaryKey(PcSystemResource record);

    /**
     * 查询所有
     * @return
     */
    List<PcSystemResourceVo> selectAll();
}