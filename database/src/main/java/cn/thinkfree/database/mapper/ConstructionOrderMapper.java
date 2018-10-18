package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import java.util.List;

import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.vo.ProjectOrderVO;
import cn.thinkfree.database.vo.StageDetailsVO;
import org.apache.ibatis.annotations.Param;

public interface ConstructionOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    long countByExample(ConstructionOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int deleteByExample(ConstructionOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int insert(ConstructionOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int insertSelective(ConstructionOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    List<ConstructionOrder> selectByExample(ConstructionOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    ConstructionOrder selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ConstructionOrder record, @Param("example") ConstructionOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ConstructionOrder record, @Param("example") ConstructionOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ConstructionOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table construction_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ConstructionOrder record);
    int updateByPrimaryKey(DesignOrder record);
    /**
     * @return
     * @Author jiang
     * @Description 分页查询项目派单
     * @Date
     * @Param
     **/
    List<ProjectOrderVO> selectProjectOrderByPage(@Param("projectOrderVO") ProjectOrderVO projectOrderVO, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
    /**
     * @return
     * @Author jiang
     * @Description 查询项目派单总条数
     * @Date
     * @Param
     **/
    Integer selectProjectOrderCount(@Param("projectOrderVO") ProjectOrderVO projectOrderVO);
    /**
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     * @return
     **/
    List<StageDetailsVO> selectStageDetailsList(@Param("projectNo") String projectNo,@Param("type") Integer type);
}