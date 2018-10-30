package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfApprovalOrderMapper;
import cn.thinkfree.database.model.AfApprovalOrder;
import cn.thinkfree.database.model.AfApprovalOrderExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.AfApprovalOrderVO;
import cn.thinkfree.service.approvalflow.AfApprovalOrderService;
import cn.thinkfree.service.approvalflow.AfApprovalRoleService;
import cn.thinkfree.service.approvalflow.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 15:53
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfApprovalOrderServiceImpl implements AfApprovalOrderService {

    @Resource
    private AfApprovalOrderMapper approvalOrderMapper;
    @Resource
    private AfApprovalRoleService approvalRoleService;
    @Resource
    private RoleService roleService;

    @Override
    public List<List<UserRoleSet>> findByConfigPlanNo(String configPlanNo, List<UserRoleSet> allRoles) {
        List<List<UserRoleSet>> roleList = new ArrayList<>();
        AfApprovalOrderExample example = new AfApprovalOrderExample();
        example.createCriteria().andConfigPlanNoEqualTo(configPlanNo);
        List<AfApprovalOrder> approvalOrders = approvalOrderMapper.selectByExample(example);
        if (approvalOrders != null) {
            for (AfApprovalOrder approvalOrder : approvalOrders) {
                List<UserRoleSet> roles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), allRoles);
                roleList.add(roles);
            }
        }
        return roleList;
    }

    @Override
    public void create(String configPlanNo, String configNo, List<List<UserRoleSet>> approvalOrders) {
        if (approvalOrders != null) {
            AfApprovalOrder approvalOrder;
            for (List<UserRoleSet> roles : approvalOrders) {
                if (roles.size() > 0) {
                    approvalOrder = new AfApprovalOrder();
                    approvalOrder.setId(null);
                    approvalOrder.setConfigNo(configNo);
                    approvalOrder.setConfigPlanNo(configPlanNo);
                    approvalOrder.setApprovalOrderNo(UniqueCodeGenerator.AF_CONFIG.getCode());
                    approvalOrder.setFirstRoleId(roles.get(0).getRoleCode());

                    approvalRoleService.create(approvalOrder.getApprovalOrderNo(), roles);
                    insert(approvalOrder);
                }
            }
        }
    }

    private void insert(AfApprovalOrder approvalOrder) {
        approvalOrderMapper.insertSelective(approvalOrder);
    }

    @Override
    public AfApprovalOrderVO findByNo(String approvalOrderNo) {
        AfApprovalOrderVO approvalOrderVO = null;
        AfApprovalOrderExample example = new AfApprovalOrderExample();
        example.createCriteria().andApprovalOrderNoEqualTo(approvalOrderNo);
        List<AfApprovalOrder> approvalOrders = approvalOrderMapper.selectByExample(example);
        AfApprovalOrder approvalOrder = null;
        if (approvalOrders != null && approvalOrders.size() > 0) {
            approvalOrder = approvalOrders.get(0);

            List<UserRoleSet> allRoles = roleService.findAll();
            List<UserRoleSet> roles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), allRoles);

            approvalOrderVO = new AfApprovalOrderVO();
            approvalOrderVO.setApprovalOrder(approvalOrder);
            approvalOrderVO.setRoles(roles);
        }
        return approvalOrderVO;
    }

    @Override
    public AfApprovalOrder findByConfigNoAndCompanyNoAndRoleId(String configNo, String companyNo, String roleId) {
//        List<AfCompanyapprovalOrder> companyapprovalOrders = companyapprovalOrderService.findByCompanyNoAndConfigNo(companyNo, configNo);
//        if (companyapprovalOrders != null && companyapprovalOrders.size() > 0) {
//            List<String> approvalOrderNos = new ArrayList<>(companyapprovalOrders.size());
//            for (AfCompanyapprovalOrder companyapprovalOrder : companyapprovalOrders) {
//                approvalOrderNos.add(companyapprovalOrder.getapprovalOrderNo());
//            }
//
//            AfApprovalOrderExample example = new AfApprovalOrderExample();
//            example.createCriteria().andapprovalOrderNoIn(approvalOrderNos).andFirstRoleIdEqualTo(roleId);
//            List<AfApprovalOrder> approvalOrders = approvalOrderMapper.selectByExample(example);
//            return approvalOrders != null && approvalOrders.size() > 0 ? approvalOrders.get(0) : null;
//        }
        return null;
    }
}
