package cn.tonghua.service.program;

import cn.tonghua.core.bundle.MyRespBundle;

public interface ProgramService {

    /**
     * 程序
     * @param servieceNm
     * @return
     */
    MyRespBundle programOperation(String servieceNm,int type);
}
