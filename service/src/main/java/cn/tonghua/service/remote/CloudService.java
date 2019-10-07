package cn.tonghua.service.remote;

import cn.tonghua.database.model.Equipment;

public interface CloudService {

    RemoteResult sendCommand(String command, Equipment equipment);
}