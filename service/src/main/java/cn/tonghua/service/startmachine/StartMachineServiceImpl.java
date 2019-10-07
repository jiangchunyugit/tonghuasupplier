package cn.tonghua.service.startmachine;

import cn.tonghua.core.base.AbsBaseService;
import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.database.mapper.EquipmentMapper;
import cn.tonghua.database.model.Equipment;
import cn.tonghua.database.model.EquipmentExample;
import cn.tonghua.database.utils.FileResourceEnabled;
import cn.tonghua.database.utils.ServiceType;
import cn.tonghua.service.utils.StartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class StartMachineServiceImpl extends AbsBaseService implements StartMachineService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public MyRespBundle startMachine(String servieceNm) {

        EquipmentExample equipmentExample = new EquipmentExample();
        // 判断是否存在
        if (isExit(servieceNm, equipmentExample)) {

            List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
            if (equipments != null && equipments.size() > 0) {
                Equipment equipment = equipments.get(0);
                String ip =equipment.getIp();
                String mask = equipment.getMask();

                try {
                StartUtil.startMachine(ip,mask);
                equipment.setIsEnable(FileResourceEnabled.ONE_true.code);
                equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                return sendSuccessMessage("操作成功");
                } catch (UnknownHostException e) {
                    //Ip地址错误时候抛出的异常
                    return sendFailMessage("Ip地址错误");
                } catch (IOException e) {
                    //获取socket失败时候抛出的异常
                    return sendFailMessage("操作失败");
                }
            }
        }
        return sendFailMessage("操作失败");
    }

    @Override
    public MyRespBundle startMachine() {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andTypeEqualTo(ServiceType.Machie.code);

        List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);

        if (equipments != null && equipments.size() > 0) {
            for (Equipment equipment:equipments) {

                String ip =equipment.getIp();
                String mask = equipment.getMask();
                try {
                    StartUtil.startMachine(ip,mask);
                    equipment.setIsEnable(FileResourceEnabled.ONE_true.code);
                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                } catch (UnknownHostException e) {
                    //Ip地址错误时候抛出的异常
                    return sendFailMessage(equipment.getServiceName()+"Ip地址错误");
                } catch (IOException e) {
                    //获取socket失败时候抛出的异常
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
        criteria.andTypeEqualTo(ServiceType.Machie.code);
        return equipmentMapper.countByExample(equipmentExample)>0?true:false;
    }
}
