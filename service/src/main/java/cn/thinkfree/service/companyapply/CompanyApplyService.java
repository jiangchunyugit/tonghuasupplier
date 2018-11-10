package cn.thinkfree.service.companyapply;

import cn.thinkfree.database.model.PcApplyInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.CompanyApplySEO;
import cn.thinkfree.database.vo.PcApplyInfoSEO;
import cn.thinkfree.database.vo.PcApplyInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;


/**
 * @author ying007
 * 公司申请事项接口：入驻申请，资质变更。。。
 */
public interface CompanyApplyService {

    /**
     * 更新公司入驻状态
     * @param companyId
     * @param status
     * @return
     */
    boolean updateStatus(String companyId, String status);

    /**
     * 公司申请事项（运营人员添加入驻信息）
     * @param pcApplyInfoSEO
     * @return
     */
    boolean addApplyInfo(PcApplyInfoSEO pcApplyInfoSEO);

    /**
     * 删除申请记录（is_delete)
     * @param id
     * @return
     */
    boolean updateApply(Integer id);

    /**
     * 查询申请记录
     * @param id
     * @return
     */
    PcApplyInfoVo findById(Integer id);

    /**
     * 根据参数查询申请信息
     * @param companyApplySEO
     * @return
     */
    PageInfo<PcApplyInfoVo> findByParam(CompanyApplySEO companyApplySEO);

    /**
     * a:添加账号---》返回公司id
     * @param roleId
     * @return
     */
    String generateCompanyId(String roleId);

    /**
     * b:添加账号--》创建用户
     * 注：添加账号及发送短信后申请表状态改为已办理  不显示办理按钮。。
     * @param pcApplyInfoSEO
     * @return
     */
    Map<String, Object> addCompanyAdmin(PcApplyInfoSEO pcApplyInfoSEO);

    /**
     * 激活账户
     * @param userRegister
     * @return
     */
    boolean updateRegister(UserRegister userRegister);

    /**
     * 根据角色查询申请数量  未办理
     * @param role
     * @return
     */
    Long countApply(String role);

    /**
     * 发送邮件验证码
     * @param email
     */
    void sendMessage(String email);

    /**
     * 校验公司名称是否重复
     * @param name
     * @return
     */
    boolean checkCompanyName(String name);

    /**
     * 校验公司邮箱是否重复
     * @param email
     * @return
     */
    boolean checkEmail(String email);
}
