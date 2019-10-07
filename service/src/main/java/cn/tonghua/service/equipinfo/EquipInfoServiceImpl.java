package cn.tonghua.service.equipinfo;

import cn.tonghua.database.mapper.EquipmentMapper;
import cn.tonghua.database.model.Equipment;
import cn.tonghua.database.model.EquipmentExample;
import cn.tonghua.database.utils.FileResourceEnabled;
import cn.tonghua.database.vo.FileResourceSEO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EquipInfoServiceImpl implements EquipInfoService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public List<Equipment> equipments(FileResourceSEO fileResourceSEO) {

        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andIsDelNotEqualTo(FileResourceEnabled.ONE_true.code);
        PageHelper.startPage(fileResourceSEO.getPage(),fileResourceSEO.getRows());
        return equipmentMapper.selectByExample(equipmentExample);
    }

    @Override
    public Equipment equipmentById(int id) {
        return equipmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean addEquipment(Equipment equipment) {
        equipment.setCreateTime(new Date());
        equipment.setIsDel(FileResourceEnabled.ZEROR_false.code);
        equipment.setIsEnable(FileResourceEnabled.ZEROR_false.code);
        return equipmentMapper.insertSelective(equipment)>0?true:false;
    }

    @Override
    public boolean updateEquipment(Equipment equipment) {
        return equipmentMapper.updateByPrimaryKeySelective(equipment)>0?true:false;
    }
}
