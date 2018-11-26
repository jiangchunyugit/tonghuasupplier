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
import cn.thinkfree.service.platform.employee.EmployeeService;
import cn.thinkfree.service.platform.vo.EmployeeMsgVo;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AfInstancePdfUrlServiceImpl.class);

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
    @Autowired
    private EmployeeService employeeService;

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
        AfUserDTO customer = getUserInfo(customerId, Role.CC.id);
        exportPdfVO.setCustomerName(customer.getUsername());
        exportPdfVO.setPhoneNo(customer.getPhone());

        List<AfApprovalLog> approvalLogs = approvalLogService.findByInstanceNo(instance.getInstanceNo());
        List<AfUserVO> userVOs = new ArrayList<>();
        if (approvalLogs != null) {
            for (AfApprovalLog approvalLog : approvalLogs) {
                AfUserDTO user = getUserInfo(approvalLog.getUserId(), approvalLog.getRoleId());
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
        deleteLocalExportFile(pdfConfig.getExportDir(), exportFileName);

        AfInstancePdfUrl instancePdfUrl = new AfInstancePdfUrl();
        instancePdfUrl.setConfigNo(instance.getConfigNo());
        instancePdfUrl.setCreateTime(new Date());
        instancePdfUrl.setInstanceNo(instance.getInstanceNo());
        instancePdfUrl.setPdfUrl(pdfUrl);
        instancePdfUrl.setProjectNo(instance.getProjectNo());
        instancePdfUrl.setScheduleSort(instance.getScheduleSort());

        insert(instancePdfUrl);
    }

    private AfUserDTO getUserInfo(String userId, String roleId) {
        AfUserDTO userDTO;
        if (Role.CC.id.equals(roleId)) {
            userDTO = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), userId, roleId);
        } else {
            userDTO = new AfUserDTO();
            EmployeeMsgVo employeeMsg = employeeService.employeeMsgById(userId);
            if (employeeMsg == null) {
                LOGGER.error("未查询到用户信息：userId:{}", userId);
                throw new RuntimeException();
            }
            userDTO.setHeadPortrait(employeeMsg.getIconUrl());
            userDTO.setUsername(employeeMsg.getRealName());
            userDTO.setUserId(userId);
            userDTO.setPhone(employeeMsg.getPhone());
        }
        return userDTO;
    }

    private void deleteLocalExportFile(String exportDir, String exportFileName) {
        File exportFile = new File(exportDir, exportFileName);
        if(exportFile.exists() && exportFile.isFile()) {
            boolean delete = exportFile.delete();
            if (!delete) {
                LOGGER.error("删除临时文件失败！exportDir:{},exportFileName:{}", exportDir, exportFileName);
            }
        }
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
