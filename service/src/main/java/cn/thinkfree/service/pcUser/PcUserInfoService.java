package cn.thinkfree.service.pcUser;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.MyPageHelper;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.AccountVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PcUserInfoService {
    List<PcUserInfo> selectByParam(UserVO userVO);
    PageInfo<PcUserInfoVo> findByParam(MyPageHelper myPageHelper);
    boolean delPcUserInfo(String userId);
    boolean saveUserInfo(PcUserInfoVo pcUserInfoVo);
    boolean updateUserInfo(PcUserInfoVo pcUserInfoVo);
    PcUserInfoVo findByUserId(String userId);
    /**
     * 修改密码
     */
    String updatePassWord(String oldPassWord, String newPassWord);

    /**
     * 启用账户
     */
    String canEnabled(String id, Integer enabled);

    /**
     * 新增用户账号
     * @param accountVO
     * @return
     */
    AccountVO saveUserAccount(AccountVO accountVO);

    /**
     * 查询账号详情
     * @param id
     * @return
     */
    AccountVO findAccountVOByID(Integer id);
}
