package cn.tonghua.service.volume;

import cn.tonghua.service.utils.ControlSystemVolume;
import org.springframework.stereotype.Service;

@Service
public class VolumeServiceImpl implements VolumeService {

    @Override
    public boolean volume(int type) {
        return ControlSystemVolume.controlSystemVolume(type);
    }
}
