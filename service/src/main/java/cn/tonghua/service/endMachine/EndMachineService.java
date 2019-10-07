package cn.tonghua.service.endMachine;

import cn.tonghua.core.bundle.MyRespBundle;

public interface EndMachineService {

    /**
     * 关闭程序
     * @param servieceNm
     * @return
     */
    MyRespBundle endMachine(String servieceNm);

    /**
     * 一键全开
     * @return
     */
    MyRespBundle endMachine();
}
