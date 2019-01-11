package cn.thinkfree.service.platform.designer;

import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.service.platform.vo.DesignerMsgListVo;
import cn.thinkfree.service.platform.vo.DesignerMsgVo;
import cn.thinkfree.service.platform.vo.DesignerStyleConfigVo;
import cn.thinkfree.service.platform.vo.PageVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xusonghui
 * 设计师服务service
 */
public interface DesignerService {
    /**
     * 根据条件查询设计师列表
     *
     * @param designerName          设计师姓名
     * @param designerRealName      设计师用户名
     * @param phone                 手机号
     * @param authState             实名认证状态
     * @param province              所在省份
     * @param city                  所在城市
     * @param area                  所在区域
     * @param level                 设计师等级
     * @param identity              设计师身份
     * @param cardNo                身份证号
     * @param source                来源
     * @param tag                   标签
     * @param registrationTimeStart 注册时间
     * @param registrationTimeEnd   注册时间
     * @param sort                  排序区域
     * @param branchCompanyCode
     * @param cityBranchCode
     * @param storeCode
     * @param pageSize              每页多少条
     * @param pageIndex             第几页
     * @return
     */
    PageVo<List<DesignerMsgListVo>> queryDesigners(
            String designerName, String designerRealName, String phone, String authState,
            String province, String city, String area, String level, String identity, String cardNo, String source,
            String tag, String registrationTimeStart, String registrationTimeEnd, String sort, String branchCompanyCode, String cityBranchCode, String storeCode, int pageSize, int pageIndex);

    /**
     * 根据用户ID查询设计师信息
     *
     * @param userId 用户ID
     * @return
     */
    DesignerMsgVo queryDesignerByUserId(String userId);

    /**
     * 根据用户ID查询员工信息
     *
     * @param userId 用户ID
     * @return
     */
    EmployeeMsg queryEmployeeMsgByUserId(String userId);

    /**
     * 根据公司ID查询该公司得设计师信息
     *
     * @param companyId 公司ID
     * @return
     */
    List<EmployeeMsg> queryDesignerByCompanyId(String companyId);

    /**
     * 通过用户id查询用户设计风格
     *
     * @param userId
     * @return
     */
    List<DesignerStyleConfigVo> queryDesignerStyleByUserId(String userId);

    /**
     * 设计师排序
     *
     * @param userId 用户ID
     * @param sort   排序值
     */
    void setDesignerSort(String userId, int sort);

    /**
     * 查询设计风格
     *
     * @return
     */
    List<DesignerStyleConfigVo> queryDesignerStyle();

    /**
     * 编辑设计师信息
     *
     * @param userId            用户ID
     * @param tag               设计师标签
     * @param identity          设计师身份
     * @param workingTime       工作年限
     * @param volumeRoomMoney   量房费
     * @param designerMoneyLow  设计费低
     * @param designerMoneyHigh 设计费高
     * @param masterStyle       擅长风格
     */
    void editDesignerMsg(
            String userId, String tag, String identity, String workingTime, String volumeRoomMoney,
            String designerMoneyLow, String designerMoneyHigh, String masterStyle);

    /**
     * 创建设计师
     *
     * @param phone             手机号
     * @param email             邮箱
     * @param province          所属省份
     * @param city              所属市
     * @param area              所属区
     * @param workingTime       工作年限
     * @param masterStyle       擅长风格
     * @param volumeRoomMoney   量房费
     * @param designerMoneyLow  设计费低
     * @param designerMoneyHigh 设计费高
     */
    void createDesigner(
            String userName, String phone, String email, int sex, String province, String city, String area, String workingTime,
            String masterStyle, String volumeRoomMoney, String designerMoneyLow, String designerMoneyHigh);

    /**
     * 导入设计师
     *
     * @param designerFile excel文件
     * @param optionId     操作人ID
     * @param companyId    操作人所属公司
     */
    void importDesign(MultipartFile designerFile, String optionId, String companyId);
}
