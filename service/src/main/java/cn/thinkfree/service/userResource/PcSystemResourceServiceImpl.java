package cn.thinkfree.service.userResource;

import cn.thinkfree.database.mapper.PcSystemResourceMapper;
import cn.thinkfree.database.mapper.PcUserResourceMapper;
import cn.thinkfree.database.model.PcSystemResource;
import cn.thinkfree.database.model.PcUserResource;
import cn.thinkfree.database.vo.MySystemResource;
import cn.thinkfree.database.vo.PcSystemResourceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PcSystemResourceServiceImpl implements PcSystemResourceService{

    @Autowired
    PcSystemResourceMapper pcSystemResourceMapper;

    @Autowired
    PcUserResourceMapper pcUserResourceMapper;

    /**
     * 获取已有资源权限
     */
    @Override
    public List<MySystemResource> getUserResource(String userId){
        List<PcSystemResourceVo> pcSystemResourceVos = pcSystemResourceMapper.selectAll();

        List<Integer> resourceId = pcUserResourceMapper.selectByUserId(userId);
        if(resourceId == null || resourceId.size() == 0){
            return getMySystemResources(pcSystemResourceVos);
        }
        for(Integer re: resourceId){
            for(PcSystemResourceVo pvo: pcSystemResourceVos){
                if(re.equals(pvo.getId())){
                    pvo.setAuthResource(true);
                }
            }
        }
        return getMySystemResources(pcSystemResourceVos);
    }


    /**
     * 添加资源权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveByUserId(String userId, String resourceId){
        int delLine = pcUserResourceMapper.deleteByUserId(userId);

//        String[] resource = resourceId.split(",");
        int saveLine = 0;
        List<PcUserResource> pcUserResources = new ArrayList<>();
        if(null != resourceId && !"".equals(resourceId)){
            String[] resourceSp = resourceId.split(",");
            for(int i = 0; i < resourceSp.length; i++){
                PcUserResource pu = new PcUserResource();
                pu.setUserId(userId);
                pu.setResourceId(Integer.valueOf(resourceSp[i]));
                pcUserResources.add(pu);
            }
            saveLine = pcUserResourceMapper.insertBatch(pcUserResources);
        }
        /*resourceSp.forEach(s->{
            PcUserResource pu = new PcUserResource();
            pu.setUserId(userId);
            pu.setResourceId(s);
            pcUserResources.add(pu);
        });*/

        if(saveLine == pcUserResources.size() && delLine >= 0){
            return true;
        }
        return false;

    }

    private List<MySystemResource> getMySystemResources(List<PcSystemResourceVo> pcSystemResourceVos) {
        List<MySystemResource> mySystemResources = new ArrayList<>();
        for (PcSystemResource pcr : pcSystemResourceVos) {
            if (pcr.getPid() == 0) {
                MySystemResource msr = new MySystemResource();
                msr.setPcSystemResource(pcr);
                mySystemResources.add(msr);
            }
        }
        for (MySystemResource ms : mySystemResources) {
            List<PcSystemResourceVo> psrList = new ArrayList<>();
            for (PcSystemResourceVo pcr : pcSystemResourceVos) {
                if (0 != (pcr.getPid()) && pcr.getPid() == ms.getPcSystemResource().getId()) {
                    psrList.add(pcr);
                    ms.setPcSystemResourceVoList(psrList);
                }
            }
        }
        return mySystemResources;

    }
}
