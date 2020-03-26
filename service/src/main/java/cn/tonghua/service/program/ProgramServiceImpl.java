package cn.tonghua.service.program;

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
public class ProgramServiceImpl extends AbsBaseService implements ProgramService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    CloudService cloudService;

    @Override
    public MyRespBundle programOperation(String servieceNm,int type) {

        EquipmentExample equipmentExample = new EquipmentExample();
        // 判断是否存在
        if (isExit(servieceNm, equipmentExample)) {

            List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
            if (equipments != null && equipments.size() > 0) {
                Equipment equipment = equipments.get(0);
                RemoteResult remoteResult = new RemoteResult();
                if(FileResourceEnabled.ONE_true.code==type ) {
                    remoteResult = cloudService.sendCommand(equipment.getPosition(),equipment);
                    equipment.setIsEnable(type);
                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                    EquipmentExample equipmentExample1 = new EquipmentExample();
                    EquipmentExample.Criteria criteria = equipmentExample1.createCriteria();
                    criteria.andProgramServiceIdEqualTo(equipment.getProgramServiceId()).andIsEnableEqualTo(FileResourceEnabled.ONE_true.code).andIdNotEqualTo(equipment.getId());
                    List<Equipment> equipments1 = equipmentMapper.selectByExample(equipmentExample1);

                    for(Equipment u:equipments1) {
                        killProgram(u);
                        u.setIsEnable(FileResourceEnabled.ZEROR_false.code);
                        equipmentMapper.updateByPrimaryKeySelective(u);
                    }
                } else {
                    remoteResult = killProgram(equipment);
                }

//                if (remoteResult != null && remoteResult.isComplete()) {
//                    equipment.setIsEnable(type);
//                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
//                    return sendSuccessMessage("操作成功");
//                }
            }
        }
        return sendFailMessage("操作失败");
    }
    private RemoteResult killProgram(Equipment equipment) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("taskkill /f /im");
        stringBuilder.append(" ");
        stringBuilder.append(equipment.getExeName());
        return cloudService.sendCommand(stringBuilder.toString(),equipment);
    }
    private boolean isExit(String serviceNm, EquipmentExample equipmentExample) {
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andServiceNameEqualTo(serviceNm);
        criteria.andTypeEqualTo(ServiceType.Program.code);
        return equipmentMapper.countByExample(equipmentExample)>0?true:false;
    }
}
