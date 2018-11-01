package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstructionOrderOperateImpl extends AbsBaseController implements ConstructionOrderOperate {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;


    /**
     * 施工订单管理-列表
     * 运营后台
     *
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ConstructionOrder>> getConstructionOrderList(int pageNum,int pageSize) {


        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrder> pageInfo = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        pageInfo.setList(list);

        return RespData.success(pageInfo);
    }
}
