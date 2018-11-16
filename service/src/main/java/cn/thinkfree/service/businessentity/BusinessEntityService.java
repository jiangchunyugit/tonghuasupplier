package cn.thinkfree.service.businessentity;

import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author jiangchunyu 经营主体service接口
 */
public interface BusinessEntityService {

    /**
     * 添加经营主体
     */
    int addBusinessEntity(BusinessEntityVO businessEntityVO);

    /**
     * 修改经营主体信息
     */
    int updateBusinessEntity(BusinessEntityVO businessEntityVO);

    /**
     * 查询经营主体信息
     */
    PageInfo<BusinessEntityVO> businessEntityList(BusinessEntitySEO businessEntitySEO);

    /**
     * 根据经营主体id查询详情(带有店面信息)
     */
    BusinessEntityVO businessEntityDetails(Integer id);

    /**
     * 经营主体list
     * @return
     */
    List<BusinessEntity> businessEntitys();

    /**
     * 查询经营主体（没有店面信息）
     * @param id
     * @return
     */
    BusinessEntity businessEntityById(Integer id);

    int enableBusinessEntity(BusinessEntityVO businessEntityVO);
}
