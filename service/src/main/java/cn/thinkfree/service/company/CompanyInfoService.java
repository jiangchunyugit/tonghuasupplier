package cn.thinkfree.service.company;

import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.vo.UserVO;

import java.util.List;

public interface CompanyInfoService {
    List<CompanyInfo> selectByCompany(UserVO userVO);
}
