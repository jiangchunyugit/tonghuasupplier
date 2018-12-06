package cn.thinkfree.service.platform.basics;

import cn.thinkfree.database.model.BasicsData;
import cn.thinkfree.service.platform.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 基础数据接口
 */
public interface BasicsService {
    /**
     * 查询所有数据编码
     *
     * @return
     */
    List<Map<String, String>> allParentCode();

    /**
     * 查询证件类型
     *
     * @return
     */
    List<BasicsData> cardTypes();

    /**
     * 查询证件类型
     *
     * @return
     */
    List<BasicsData> countryType();

    /**
     * 查询证件类型
     *
     * @return
     */
    List<BasicsData> cancelDesign();

    /**
     * 查询证件类型
     *
     * @return
     */
    List<BasicsData> refund();

    /**
     * 查询证件类型
     *
     * @return
     */
    List<BasicsData> cancelCons();

    /**
     * 根据分组类型查询基础配置信息
     *
     * @param groupCode 分支类型
     * @return
     */
    List<BasicsData> queryData(String groupCode);

    /**
     * 查询具体基础数据
     * @param groupCode
     * @param code
     * @return
     */
    BasicsData queryDataOne(String groupCode,String code);

    /**
     * 根据分组类型查询基础配置信息
     *
     * @param groupCode 分支类型
     * @param pageSize  每页多少条
     * @param pageIndex 第几页
     * @return
     */
    PageVo<List<BasicsData>> queryData(String groupCode, int pageSize, int pageIndex);

    /**
     * 根据分组类型查询基础配置信息
     *
     * @param groupCode 分支类型
     * @return
     */
    List<BasicsData> queryData(String groupCode, List<String> keyCodes);

    /**
     * 创建基础数据
     *
     * @param dataId     数据唯一ID
     * @param groupCode  分组编码
     * @param basicsName 基础数据名称
     * @param remark     备注
     */
    void createBasics(String dataId, String groupCode, String basicsName, String remark);

    /**
     * 根据主键ID标记删除改基础数据
     *
     * @param dataId
     */
    void delBasics(String dataId);

    /**
     * 获取省市区信息
     *
     * @param type       类型，1省份，2市，3区
     * @param parentCode 父级编码
     * @return
     */
    List<Map<String, String>> pua(int type, String parentCode);

    Map<String,String> getProvince();
    Map<String,String> getCity();
    Map<String,String> getArea();
}
