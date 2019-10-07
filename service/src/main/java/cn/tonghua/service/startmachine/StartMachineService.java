package cn.tonghua.service.startmachine;

import cn.tonghua.core.bundle.MyRespBundle;

public interface StartMachineService {

    /**
     * 启动程序
     * @param servieceNm
     * @return
     */
    MyRespBundle startMachine(String servieceNm);

    /**
     * 一键全开
     * @return
     */
    MyRespBundle startMachine();
}
