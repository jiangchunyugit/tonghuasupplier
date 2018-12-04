package cn.thinkfree.service.construction;

import cn.thinkfree.service.construction.vo.ConstructionCityVo;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/12/3 14:42
 */
public interface CommonService {

    /**
     * 查询 当前状态值 By projectNo
     */
    Integer queryStateCode(String projectNo);

    /**
     * 查询 当前状态值 By orderNo
     */
    Integer queryStateCodeByOrderNo(String orderNo);
    /**
     * 查询 当前详细阶段（支付/施工） By orderNo
     */
    String queryStateCodeDetailByOrderNo(String orderNo);

    /**
     * 更新状态值 By projectNo
     */
    boolean updateStateCode(String projectNo, int stateCode);

    /**
     * 更新状态值 By orderNo
     */
    boolean updateStateCodeByOrderNo(String orderNo, int stateCode);

    /**
     * 更新状态值-同步到project表中
     */
    int updateToProject(String projectNo, int stateCode);

    /**
     * 施工订单 城市列表
     *
     * @return
     */
    List<ConstructionCityVo> getCityList();

    /**
     * 施工订单 城市编码转城市名称
     *
     * @return
     */
    String getCityNameByCode(String cityCode);


    /**
     *  查询施工阶段名称
     * @param orderNo
     * @param sort
     * @return
     */
    String getSchedulingName (String orderNo,Integer sort);
}
