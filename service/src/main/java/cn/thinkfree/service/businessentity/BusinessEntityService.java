package cn.thinkfree.service.businessentity;

import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经营主体  接口
 */
public interface BusinessEntityService {

    /**
     * 添加经营主体
     * @param businessEntityVO
     * @return
     */
    int addBusinessEntity(BusinessEntityVO businessEntityVO);

    /**
     * 修改经营主体信息
     * @param businessEntityVO
     * @return
     */
    int updateBusinessEntity(BusinessEntityVO businessEntityVO);

    /**
     * 查询经营主体信息
     * @param businessEntitySEO
     * @return
     */
    PageInfo<BusinessEntityVO> businessEntityList(BusinessEntitySEO businessEntitySEO);

    /**
     * 根据经营主体id查询详情(带有店面信息)
     * @param id
     * @return
     */
    BusinessEntityVO businessEntityDetails(Integer id);

    /**
     * 经营主体list
     * @return
     */
    List<BusinessEntity> businessEntices();

    /**
     * 查询经营主体（没有店面信息）
     * @param id
     * @return
     */
    BusinessEntity businessEntityById(Integer id);

    /**
     * 启用禁用
     * @param businessEntityVO
     * @return
     */
    int enableBusinessEntity(BusinessEntityVO businessEntityVO);
}
