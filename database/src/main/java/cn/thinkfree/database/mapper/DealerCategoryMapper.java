package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.DealerCategory;
import cn.thinkfree.database.model.DealerCategoryExample;
import java.util.List;

import cn.thinkfree.database.vo.AuditBrandInfoVO;
import org.apache.ibatis.annotations.Param;

public interface DealerCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    long countByExample(DealerCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int deleteByExample(DealerCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int insert(DealerCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int insertSelective(DealerCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    List<DealerCategory> selectByExample(DealerCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    DealerCategory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DealerCategory record, @Param("example") DealerCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DealerCategory record, @Param("example") DealerCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DealerCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_dealer_category
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DealerCategory record);

    /**
     * 经销商品牌审核回显
     * @param brandId
     * @return
     */
//    List<AuditBrandInfoVO> showBrandDetail(String brandId);
}