package cn.thinkfree.service.ebsmoke;

import cn.thinkfree.database.vo.ebsmokevo.EbsMokeBranchCompany;
import cn.thinkfree.database.vo.ebsmokevo.EbsCityBranch;
import cn.thinkfree.database.vo.ebsmokevo.StoreBusinessEntity;

import java.util.List;

public interface EbsService {

    /**
     * 埃森哲分公司
     * @return
     */
    List<EbsMokeBranchCompany> ebsBranchCompanyList();

    /**
     * 埃森哲城市分站
     * @param id
     * @return
     */
    List<EbsCityBranch> ebsCityBranchList(Integer id);

    /**
     *
     * @param id
     * @return
     */
    List<StoreBusinessEntity> storeBusinessEntityList(Integer id);
}
