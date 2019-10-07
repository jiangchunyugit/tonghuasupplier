package cn.tonghua.service.endMachine;

import cn.tonghua.core.base.AbsBaseService;
import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.database.mapper.EquipmentMapper;
import cn.tonghua.database.model.Equipment;
import cn.tonghua.database.model.EquipmentExample;
import cn.tonghua.database.utils.FileResourceEnabled;
import cn.tonghua.database.utils.ServiceType;
import cn.tonghua.service.remote.CloudService;
import cn.tonghua.service.remote.RemoteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndMachineServiceImpl extends AbsBaseService implements EndMachineService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    CloudService cloudService;

    @Override
    public MyRespBundle endMachine(String servieceNm) {

        EquipmentExample equipmentExample = new EquipmentExample();
        // 判断是否存在
        if (isExit(servieceNm, equipmentExample)) {

            List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
            if (equipments != null && equipments.size() > 0) {
                Equipment equipment = equipments.get(0);
                RemoteResult remoteResult = cloudService.sendCommand("shutdown -s -t 5",equipment);

                if (remoteResult != null && remoteResult.isComplete()) {
                    equipment.setIsEnable(FileResourceEnabled.ONE_true.code);
                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                    return sendSuccessMessage("操作成功");
                }
            }
        }
        return sendFailMessage("操作失败");
    }

    @Override
    public MyRespBundle endMachine() {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andTypeEqualTo(ServiceType.Machie.code);

        List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);

        if (equipments != null && equipments.size() > 0) {
            for (Equipment equipment:equipments) {

                RemoteResult remoteResult = cloudService.sendCommand("shutdown -s -t 5",equipment);

                if (remoteResult != null && remoteResult.isComplete()) {
                    equipment.setIsEnable(FileResourceEnabled.ONE_true.code);
                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                }else {
                    return sendFailMessage("操作失败");
                }
            }
            return sendSuccessMessage("操作成功");
        }
        return sendFailMessage("操作失败");
    }

    private boolean isExit(String serviceNm, EquipmentExample equipmentExample) {
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andServiceNameEqualTo(serviceNm);
        return equipmentMapper.countByExample(equipmentExample)>0?true:false;
    }
}
