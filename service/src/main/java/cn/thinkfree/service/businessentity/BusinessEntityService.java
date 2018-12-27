package cn.thinkfree.service.businessentity;

import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.vo.BranchCompanyVO;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityStoreVO;
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
     * 校验重复信息
     * @param businessEntity
     * @return
     */
    boolean checkRepeat(BusinessEntity businessEntity);
    /**
     * 添加经营主体
     * @param businessEntity
     * @return
     */
    boolean addBusinessEntity(BusinessEntity businessEntity);

    /**
     * 修改经营主体信息
     * @param businessEntity
     * @return
     */
    boolean updateBusinessEntity(BusinessEntity businessEntity);

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
     * 门店回写
     * @param id
     * @return
     */
    BusinessEntityStoreVO businessEntityStoreDetails(Integer id);

    /**
     * 选择门店保存
     * @param businessEntityStoreVO
     * @return
     */
    boolean insertBusinessEntityStore(BusinessEntityStoreVO businessEntityStoreVO);

    /**
     * 经营主体list
     * @return
     */
    List<BusinessEntity> businessEntices();

    /**
     * 编辑回写
     * @param id
     * @return
     */
    BusinessEntity businessEntityById(Integer id);

    /**
     * 删除主体
     * @param businessEntity
     * @return
     */
    boolean deleteBusinessEntity(BusinessEntity businessEntity);

    /**
     * 通过公司编号获取经营主体ebsId
     * @param companyId
     * @return
     */
    String getBusinessEbsIdByCompanyId(String companyId);


    /**
     * 根据门店id 查询 经营主体名称
     * @param  storeId
     */
    String getBusinessStoreName(String storeId);
}
