package cn.tonghua.service.equipinfo;

import cn.tonghua.database.model.Equipment;
import cn.tonghua.database.vo.FileResourceSEO;

import java.util.List;

public interface EquipInfoService {

    List<Equipment> equipments(FileResourceSEO fileResourceSEO);

    Equipment equipmentById(int id);

    Boolean addEquipment(Equipment equipment);

    boolean updateEquipment(Equipment equipment);
}
