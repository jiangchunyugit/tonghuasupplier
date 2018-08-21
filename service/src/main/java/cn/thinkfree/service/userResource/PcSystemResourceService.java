package cn.thinkfree.service.userResource;


import cn.thinkfree.database.vo.MySystemResource;


import java.util.List;

public interface PcSystemResourceService {

    List<MySystemResource> getUserResource(String userId);

    boolean saveByUserId(String userId, List<Integer> resourceId);
}
