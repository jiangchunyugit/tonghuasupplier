package cn.tonghua.service.projector;

import cn.tonghua.database.mapper.EquipmentMapper;
import cn.tonghua.database.model.Equipment;
import cn.tonghua.database.model.EquipmentExample;
import cn.tonghua.database.utils.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@Service
public class ProjectorServiceImpl implements ProjectorService {

    @Autowired
    EquipmentMapper equipmentMapper;

    private byte[] onCmd = new byte[]{0x25, 0x31, 0x50, 0x4f, 0x57, 0x52, 0x20, 0x31, 0x0d};
    private byte[] offCmd = new byte[]{0x25, 0x31, 0x50, 0x4f, 0x57, 0x52, 0x20, 0x30, 0x0d};
    private byte[] rgbCmd = new byte[]{0x25, 0x31, 0x49, 0x4e, 0x50, 0x54, 0x20, 0x31, 0x32, 0x0d};
    private byte[] videoCmd = new byte[]{0x25, 0x31, 0x49, 0x4e, 0x50, 0x54, 0x20, 0x32, 0x31, 0x0d};
    private byte[] dviCmd = new byte[]{0x25, 0x31, 0x49, 0x4e, 0x50, 0x54, 0x20, 0x33, 0x31, 0x0d};

    @Override
    public Boolean projectorOperation(String serviceNm, int type) {

        EquipmentExample equipmentExample = new EquipmentExample();
        // 判断是否存在
        if (isExit(serviceNm, equipmentExample)) {

            List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
            if (equipments != null && equipments.size() > 0) {
                Equipment equipment = equipments.get(0);
                byte[] cmd = new byte[0];
                switch (type) {
                    case 0:
                        cmd = onCmd;
                        break;
                    case 1:
                        cmd = offCmd;
                        break;
                    case 3:
                        cmd = rgbCmd;
                        break;
                    case 4:
                        cmd = videoCmd;
                        break;
                    default: return false;
                }
                Socket socket = null;
                InputStream is = null;
                OutputStream os = null;
                byte[] response = new byte[128];
                try {
                    socket = new Socket(equipment.getIp(), Integer.valueOf(equipment.getPort()));
                    os = socket.getOutputStream();
                    is = socket.getInputStream();

                    // 1.establish connection
                    is.read(response);
                    if ((char) response[7] == '0') {
                        // 2.send projector cmd
                        os.write(cmd);
                        int n = is.read(response);
                        System.out.println("response=" + new String(response, 0, n));
                    }
                    equipment.setIsEnable(type);
                    equipmentMapper.updateByExampleSelective(equipment,equipmentExample);
                    return true;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return false;
    }

    private boolean isExit(String serviceNm, EquipmentExample equipmentExample) {
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andServiceNameEqualTo(serviceNm);
        criteria.andTypeEqualTo(ServiceType.Projector.code);
        return equipmentMapper.countByExample(equipmentExample)>0?true:false;
    }
}
