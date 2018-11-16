package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.constants.Role;
import cn.thinkfree.database.mapper.AfInstancePdfUrlMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfExportPdfVO;
import cn.thinkfree.database.vo.AfUserDTO;
import cn.thinkfree.database.vo.AfUserVO;
import cn.thinkfree.service.approvalflow.AfApprovalLogService;
import cn.thinkfree.service.approvalflow.AfConfigService;
import cn.thinkfree.service.approvalflow.AfInstancePdfUrlService;
import cn.thinkfree.service.approvalflow.RoleService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.config.PdfConfig;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 审批流记录PDF
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 15:56
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfInstancePdfUrlServiceImpl implements AfInstancePdfUrlService {

    @Autowired
    private AfInstancePdfUrlMapper instancePdfUrlMapper;
    @Autowired
    private PdfConfig pdfConfig;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private HttpLinks httpLinks;
    @Autowired
    private AfConfigService configService;
    @Autowired
    private AfApprovalLogService approvalLogService;
    @Autowired
    private RoleService roleService;

    @Override
    public void create(AfInstance instance) {

        AfExportPdfVO exportPdfVO = new AfExportPdfVO();

        exportPdfVO.setRemark(instance.getRemark());
        exportPdfVO.setProjectNo(instance.getProjectNo());
        exportPdfVO.setInstanceNo(instance.getInstanceNo());
        exportPdfVO.setCreateTime(DateUtil.formartDate(instance.getCreateTime(), DateUtil.FORMAT));
        exportPdfVO.setConfigNo(instance.getConfigNo());

        Project project = projectService.findByProjectNo(instance.getProjectNo());
        exportPdfVO.setAddress(project.getAddressDetail());
        String customerId = project.getOwnerId();
        AfUserDTO customer = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), customerId, Role.CC.id);
        exportPdfVO.setCustomerName(customer.getUsername());
        exportPdfVO.setPhoneNo(customer.getPhone());

        List<AfApprovalLog> approvalLogs = approvalLogService.findByInstanceNo(instance.getInstanceNo());
        List<AfUserVO> userVOs = new ArrayList<>();
        if (approvalLogs != null) {
            for (AfApprovalLog approvalLog : approvalLogs) {
                AfUserDTO user = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), approvalLog.getUserId(), approvalLog.getRoleId());
                UserRoleSet role = roleService.findById(approvalLog.getRoleId());
                if (approvalLog.getSort() == 0) {
                    exportPdfVO.setCreateUsername(user.getUsername());
                    exportPdfVO.setCreateRoleName(role.getRoleName());
                } else {
                    AfUserVO userVO = new AfUserVO();
                    userVO.setUsername(user.getUsername());
                    userVO.setRoleName(role.getRoleName());
                    userVOs.add(userVO);
                }
            }
        }
        exportPdfVO.setApprovalUsers(userVOs);

        AfConfig config = configService.findByNo(instance.getConfigNo());
        exportPdfVO.setConfigName(config.getName());

        String exportFileName = AfUtils.createPdf(pdfConfig, exportPdfVO, instance.getData());
        String pdfUrl = AfUtils.uploadFile(pdfConfig.getExportDir(), exportFileName, pdfConfig.getFileUploadUrl());

        AfInstancePdfUrl instancePdfUrl = new AfInstancePdfUrl();
        instancePdfUrl.setConfigNo(instance.getConfigNo());
        instancePdfUrl.setCreateTime(new Date());
        instancePdfUrl.setInstanceNo(instance.getInstanceNo());
        instancePdfUrl.setPdfUrl(pdfUrl);
        instancePdfUrl.setProjectNo(instance.getProjectNo());
        instancePdfUrl.setScheduleSort(instance.getScheduleSort());

        insert(instancePdfUrl);
    }

    private void insert(AfInstancePdfUrl instancePdfUrl) {
        instancePdfUrlMapper.insertSelective(instancePdfUrl);
    }

    @Override
    public List<AfInstancePdfUrl> findByProjectNo(String projectNo) {
        AfInstancePdfUrlExample example = new AfInstancePdfUrlExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        example.setOrderByClause("create_time desc");
        return instancePdfUrlMapper.selectByExample(example);
    }
}
