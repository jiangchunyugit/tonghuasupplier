package cn.thinkfree.service.project;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.event.MyEventBus;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.RandomNumUtils;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.event.ProjectStopEvent;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.remote.RemoteResult;
import cn.thinkfree.service.utils.ProjectQuotationItemSortUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends AbsLogPrinter implements ProjectService {


    @Autowired
    PreProjectCompanySetMapper preProjectCompanySetMapper;

    @Autowired
    PreProjectUserRoleMapper preProjectUserRoleMapper;

    @Autowired
    PreProjectGuideMapper preProjectGuideMapper;

    @Autowired
    PreProjectInfoMapper preProjectInfoMapper;

    @Autowired
    PreProjectMaterialMapper preProjectMaterialMapper;

    @Autowired
    PreProjectConstructionMapper preProjectConstructionMapper;

    @Autowired
    PreProjectConstructionInfoMapper preProjectConstructionInfoMapper;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    CloudService cloudService;

    @Autowired
    ConsumerSetMapper consumerSetMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    ProjectDocumentMapper projectDocumentMapper;



    /**
     * 所谓五分钟
     */
    @Value("${custom.sendSMS.threshold}")
    private static Long threshold ; // 300000L

    /**
     * 汇总公司项目情况
     *
     * @param companyRelationMap
     * @return
     */
    @Override
    public IndexProjectReportVO countProjectReportVO(List<String> companyRelationMap) {
        IndexProjectReportVO indexProjectReportVO = preProjectCompanySetMapper.countCompanyProject(companyRelationMap);
        if(null == indexProjectReportVO){
            return new IndexProjectReportVO().init();
        }
        return indexProjectReportVO;
    }

    /**
     * 分页查询项目信息
     *
     * 因为 角色存储的时候存在了多份,且关联关系较复杂
     * 所以 当有角色条件时,先查询出角色所负责的项目,在依次进行筛选和分页
     *
     * @param projectSEO
     * @return
     */
    @Override
    public PageInfo<ProjectVO> pageProjectBySEO(ProjectSEO projectSEO) {

        Boolean needFilter = false;
        List<String> projectNos = Lists.newArrayList();
        // 过滤管家所负责的项目
        if(projectSEO.getSteward() != null  ){
            printDebugMes("项目分页查询,存在管家条件:{}",projectSEO.getSteward());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.Steward.roleCode)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getSteward());
           projectNos.addAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
           needFilter = true;
        }
        // 过滤项目经理所负责的项目
        if(projectSEO.getProjectManager() != null){
            printDebugMes("项目分页查询,存在项目经理条件:{}",projectSEO.getProjectManager());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.ProjectManager.roleCode)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getProjectManager());
            // 取交集
            projectNos.retainAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
            needFilter = true;
        }

        if(needFilter && projectNos.isEmpty()){
            printDebugMes("存在管家或项目经理,但过滤后无项目编号");
            // 为空直接返回
            return new PageInfo();
        }

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        projectSEO.setCompanyRelationMap(userVO.getRelationMap());
        PageHelper.startPage(projectSEO.getPage(),projectSEO.getRows());
        List<ProjectVO> projectVOS = preProjectGuideMapper.selectProjectVOBySEO(projectSEO);


        return new PageInfo<>(projectVOS);
    }

    /**
     * 删除项目根据项目主键
     *
     * @param projectNo
     * @return
     */
    @Transactional
    @Override
    public String deleteProjectByProjectNo(String projectNo) {

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);
        printInfoMes("删除项目编号{},查询到项目引导信息:{}",projectNo,preProjectGuides);
        if(preProjectGuides.isEmpty() || preProjectGuides.size() > 1){
            printErrorMes("删除项目编号{},查询到项目引导信息不唯一",projectNo);
            return "数据异常,无法删除";
        }
        PreProjectGuide preProjectGuide = preProjectGuides.get(0);
        if(!ProjectStatus.NotOnLine.shortVal().equals(preProjectGuide.getStatus())){
            return "数据状态不可删除";
        }

        PreProjectGuide deleteObj = new PreProjectGuide();
        deleteObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        preProjectGuideMapper.updateByExampleSelective(deleteObj,preProjectGuideExample);

        delProjectInfo(projectNo);
        delProjectConstructionAndInfo(projectNo);
        delProjectMaterial(projectNo);
        return "操作成功";
    }

    /**
     * 停工
     *
     * @param projectNo
     * @return
     */
    @Override
    public String updateProjectStateForStop(String projectNo) {
        printInfoMes("停工项目:{}",projectNo);
        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(ProjectStatus.Working.shortVal());
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);

        if(preProjectGuides.isEmpty()){
            return "数据状态不可停工!";
        }

        PreProjectGuide preProjectGuide = new PreProjectGuide();
        preProjectGuide.setStatus(ProjectStatus.StopTheWork.shortVal());
        preProjectGuideMapper.updateByExampleSelective(preProjectGuide,preProjectGuideExample);

        MyEventBus.getInstance().publicEvent(new ProjectStopEvent(projectNo));
        return "操作成功";
    }

    /**
     * 查询项目详情 根据项目编号
     *
     * @param projectNo
     * @return
     */
    @Override
    public ProjectDetailsVO selectProjectDetailsVOByProjectNo(String projectNo) {

        ProjectDetailsVO projectDetailsVO = preProjectGuideMapper.selectProjectDetailsByProjectNo(projectNo);
        if(projectDetailsVO == null){
            return new ProjectDetailsVO();
        }
        PreProjectUserRoleExample roleExample = new PreProjectUserRoleExample();
        roleExample.createCriteria().andProjectNoEqualTo(projectNo).andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<ProjectUserRoleVO> preProjectUserRoles = preProjectUserRoleMapper.selectProjectUserRoleVOByExample(roleExample);
        projectDetailsVO.setStaffs(preProjectUserRoles);

        return projectDetailsVO;
    }

    /**
     * 根据职位查询员工
     *
     * @param job
     * @param filter
     * @return
     */
    @Override
    public List<StaffsVO> selectStaffsByJob(String job, String filter) {

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        CompanyUserSetExample companyUserSetExample = new CompanyUserSetExample();
        CompanyUserSetExample.Criteria firstCriteria = companyUserSetExample.createCriteria();
        firstCriteria.andCompanyIdEqualTo(userVO.getCompanyID())
                .andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                .andIsBindEqualTo(SysConstants.YesOrNo.YES.shortVal())
                .andRoleIdEqualTo(job.toUpperCase());
        if(StringUtils.isNotBlank(filter)){
            firstCriteria.andNameLike("%"+filter+"%");
            companyUserSetExample.or().andCompanyIdEqualTo(userVO.getCompanyID())
                    .andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andIsBindEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(job.toUpperCase())
                    .andPhoneLike("%"+filter+"%");
        }

        return companyUserSetMapper.selectStaffsVOByExample(companyUserSetExample);
    }

    /**
     * 查询项目报价单
     *
     * @param projectNo
     * @return
     */
    @Override
    public ProjectQuotationVO selectProjectQuotationVoByProjectNo(String projectNo) {
        ProjectQuotationVO projectQuotationVO = new ProjectQuotationVO();
        List<ProjectQuotationItemVO> items = new ArrayList<>();

        PreProjectInfoExample preProjectInfoExample= new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectInfo> info = preProjectInfoMapper.selectByExample(preProjectInfoExample);
        if(info.isEmpty() || info.size() > 1){
            return new ProjectQuotationVO();
        }
        SpringBeanUtil.copy(info.get(0),projectQuotationVO);
        PreProjectConstructionExample preProjectConstructionExample = new PreProjectConstructionExample();
        preProjectConstructionExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectConstruction> preProjectConstructions = preProjectConstructionMapper.selectByExample(preProjectConstructionExample);

        PreProjectConstructionInfoExample preProjectConstructionInfoExample = new PreProjectConstructionInfoExample();
        preProjectConstructionInfoExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal())
                .andProjectNoEqualTo(projectNo);
        List<PreProjectConstructionInfo> pcis = preProjectConstructionInfoMapper.selectByExample(preProjectConstructionInfoExample);


        for(PreProjectConstruction p:preProjectConstructions){
            ProjectQuotationItemVO tmp = new ProjectQuotationItemVO();
            SpringBeanUtil.copy(p,tmp);
            tmp.setPreProjectConstructionInfo(pcis.stream()
                    .filter(i->i.getConstrucionProjectNo().equals(p.getConstructionProjectNo()))
                    .findFirst().get());
            items.add(tmp);
        }
        // 排序也是很有趣的工作
        Set<String> dis = items.stream().map(ProjectQuotationItemVO::getConstructionProjectType).collect(Collectors.toSet());
        List<ProjectQuotationItemVO> bigType = new ArrayList<>();
        dis.forEach(d->{
            ProjectQuotationItemVO v = new ProjectQuotationItemVO();
            v.setConstructionProjectType(d);
            v.setDetailItem(items.stream().filter(i->d.equals(i.getConstructionProjectType())).collect(Collectors.toList()));
            bigType.add(v);
        });
        Collections.sort(bigType, Comparator.comparing(o -> ProjectQuotationItemSortUtils.sortNo(o.getConstructionProjectType())));
        projectQuotationVO.setItems(bigType);
        projectQuotationVO.setProjectNo(projectNo);

        return projectQuotationVO;
    }

    /**
     * 编辑报价单
     *
     * @param projectQuotationVO
     * @return
     */
    @Transactional
    @Override
    public String editQuotation(ProjectQuotationVO projectQuotationVO) {
        String projectNo = projectQuotationVO.getProjectNo();

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);
        if(preProjectGuides.isEmpty() || preProjectGuides.size() > 1){
            throw  new RuntimeException("数据完整性破损,操作失败!");
        }
        PreProjectGuide preProjectGuide = preProjectGuides.get(0);
        if(!ProjectStatus.NotOnLine.shortVal().equals(preProjectGuide.getStatus())){
            return "数据状态不可编辑!";
        }


        delProjectConstructionAndInfo(projectNo);

        saveProjectConstructionAndInfo(projectQuotationVO);


        return "操作成功!";
    }

    /**
     * 项目上线
     * @param projectDetailsVO
     * @return
     */
    @Transactional
    @Override
    public String updateProjectForUpOnline(ProjectDetailsVO projectDetailsVO) {

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectDetailsVO.getProjectNo());

        if(projectDetailsVO.getThumbnail() != null){
            String filePath = WebFileUtil.fileCopy("/project",projectDetailsVO.getThumbnail());
            projectDetailsVO.setProjectPic(filePath);
        }
        preProjectGuideMapper.updateByExampleSelective(projectDetailsVO,preProjectGuideExample);

        // 处理业主信息
//        updateConsumer(projectDetailsVO);

        printInfoMes("处理项目员工信息");

        replacement(projectDetailsVO);

        PreProjectInfo preProjectInfo = projectDetailsVO.getInfo();
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andIdEqualTo(preProjectInfo.getId());
        preProjectInfoMapper.updateByExampleSelective(preProjectInfo,preProjectInfoExample);

//        MyEventBus.getInstance().publicEvent(new ProjectUpOnline(projectDetailsVO.getProjectNo()));

        return "操作成功!";
    }

    /**
     * 更新业主信息
     * @param projectDetailsVO
     */
    private void updateConsumer(ProjectDetailsVO projectDetailsVO) {
        PreProjectGuide tmp = preProjectGuideMapper.selectByPrimaryKey(projectDetailsVO.getId());
        ConsumerSet updateConsumer = new ConsumerSet();
        updateConsumer.setName(projectDetailsVO.getCustomerName());
        updateConsumer.setPhone(projectDetailsVO.getCustomerPhone());
        ConsumerSetExample consumerSetExample = new ConsumerSetExample();
        consumerSetExample.createCriteria().andConsumerIdEqualTo(tmp.getCustomerNo());
        consumerSetMapper.updateByExampleSelective(updateConsumer,consumerSetExample);

        PreProjectUserRole updatePreProjectUserRole = new PreProjectUserRole();
        updatePreProjectUserRole.setUserName(projectDetailsVO.getCustomerName());
        PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
        preProjectUserRoleExample.createCriteria().andProjectNoEqualTo(projectDetailsVO.getProjectNo())
                .andRoleIdEqualTo(UserJobs.Owner.roleCode).andUserIdEqualTo(tmp.getCustomerNo());
        preProjectUserRoleMapper.updateByExampleSelective(updatePreProjectUserRole,preProjectUserRoleExample);
    }


    /**
     * 移交项目
     * 替换缩略图
     * 替换人员
     * @param projectTransferVO
     * @return
     */
    @Transactional
    @Override
    public String updateProjectByTransfer(ProjectTransferVO projectTransferVO) {

        if(projectTransferVO.getThumbnail() != null){
            String filePath = WebFileUtil.fileCopy("/project",projectTransferVO.getThumbnail());

            PreProjectGuide updateObj = new PreProjectGuide();
            updateObj.setProjectPic(filePath);
            PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
            preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectTransferVO.getProjectNo());
            preProjectGuideMapper.updateByExampleSelective(updateObj,preProjectGuideExample);
        }
        List<ProjectUserRoleVO> projectUserRoleVOS = projectTransferVO.getStaffs();
        projectUserRoleVOS.forEach(p->{

            String userName = "";
            UserInfoExample userInfoExample = new UserInfoExample();
            userInfoExample.createCriteria().andUserIdEqualTo(p.getUserId());
            List<UserInfo> users = userInfoMapper.selectByExample(userInfoExample);

            if(!users.isEmpty() || !(users.size() > 1)){
                userName = users.get(0).getName();
            }
            PreProjectUserRole updateObj = new PreProjectUserRole();
            updateObj.setIsTransfer(SysConstants.YesOrNo.YES.shortVal());
            updateObj.setTransferUserId(p.getUserId());
            updateObj.setTransferTime(new Date());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIdEqualTo(p.getId());
            preProjectUserRoleMapper.updateByExampleSelective(updateObj,preProjectUserRoleExample);

            PreProjectUserRole insert = new PreProjectUserRole();
            insert.setIsTransfer(SysConstants.YesOrNo.NO.shortVal());
            insert.setIsJob(SysConstants.YesOrNo.YES.shortVal());
            insert.setCreateTime(new Date());
            insert.setUserId(p.getUserId());
            insert.setRoleId(p.getRoleId());
            insert.setUserName(userName);
            insert.setRoleName(UserJobs.findByCodeStr(p.getRoleId()).mes);
            insert.setProjectNo(projectTransferVO.getProjectNo());
            preProjectUserRoleMapper.insertSelective(insert);
        });

        return "操作成功!";
    }

    /**
     * 项目图标 总览
     *
     * @param firstDayOfWeek
     * @param lastDayOfWeek
     * @return
     */
    @Override
    public List<IndexProjectChartItemVO> summaryProjectChart(LocalDate firstDayOfWeek, LocalDate lastDayOfWeek) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        return preProjectGuideMapper.selectProjectLineChat(firstDayOfWeek.toString(),lastDayOfWeek.toString(),userVO.getRelationMap());
    }

    /**
     * 查询一个员工参与的项目
     *
     * @param userID
     * @param status
     * @param rows
     * @param page
     * @return
     */
    @Override
    public PageInfo<ProjectVO> selectProjectVOForPerson(String userID, Integer status, Integer rows, Integer page) {
        PageHelper.startPage(page,rows);
        List<ProjectVO> list = preProjectGuideMapper.selectProjectVOForPerson(userID,status);
        return new PageInfo<>(list);
    }

    /**
     * 汇总一个员工的所有项目状态
     *
     * @param userID
     * @return
     */
    @Override
    public IndexProjectReportVO countProjectForPerson(String userID) {
        IndexProjectReportVO indexProjectReportVO = preProjectCompanySetMapper.countProjectForPerson(userID);
        if(null == indexProjectReportVO){
            return new IndexProjectReportVO().init();
        }
        return indexProjectReportVO;
    }

    /**
     * 根据项目编号查询主材信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public List<PreProjectMaterial> selectProjectMaterilsByProjectNo(String projectNo) {

        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();

        preProjectMaterialExample.setOrderByClause(" create_time ");

        preProjectMaterialExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal())
                .andProjectNoEqualTo(projectNo);

        return preProjectMaterialMapper.selectByExample(preProjectMaterialExample);
    }

    /**
     * 编辑主材信息
     *
     *
     * @param projectNo
     * @param preProjectMaterials
     * @return
     */
    @Transactional
    @Override
    public String editMaterials(String projectNo, List<PreProjectMaterial> preProjectMaterials) {

        printInfoMes("删除主材信息:{}",projectNo);
        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();
        preProjectMaterialExample.createCriteria().andProjectNoEqualTo(projectNo);
        PreProjectMaterial delObj = new PreProjectMaterial();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        preProjectMaterialMapper.updateByExampleSelective(delObj,preProjectMaterialExample);
        if(preProjectMaterials.isEmpty()){
            return "操作成功!";
        }

        preProjectMaterials.forEach(m->{
            m.setProjectNo(projectNo);
            m.setCreateTime(new Date());
            m.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            preProjectMaterialMapper.insertSelective(m);
        });

        return "操作成功!";
    }

    /**
     * 查询一个公司及子公司的项目
     *
     * @param companyID
     * @param status
     * @param rows
     * @param page
     * @return
     */
    @Override
    public PageInfo<ProjectVO> selectProjectVOForCompany(String companyID, Integer status, Integer rows, Integer page) {


        PageHelper.startPage(page,rows);
        List<ProjectVO> list = preProjectGuideMapper.selectProjectVOForCompany(companyID,status);
        return new PageInfo<>(list);
    }

    /**
     * 再次通知业主
     *
     * @param phone
     * @return
     */
    @Transactional
    @Override
    public String notifyOwner(String phone) {
        String activeCode = RandomNumUtils.random(6);

        ConsumerSetExample consumerSetExample = new ConsumerSetExample();
        consumerSetExample.createCriteria().andPhoneEqualTo(phone);


        List<ConsumerSet> consumerSets = consumerSetMapper.selectByExample(consumerSetExample);
        if(consumerSets.isEmpty() || consumerSets.size() > 1){
            return "数据异常";
        }
        ConsumerSet consumerSet = consumerSets.get(0);

        if(SysConstants.YesOrNo.YES.shortVal().equals(consumerSet.getIsBind())){
            return "已激活用户无需再次激活";
        }


        Date lastBindTime = consumerSet.getBindTime();
        if(lastBindTime != null){
            Long ms=lastBindTime.getTime();
            Long now = new Date().getTime();
            if((now-ms) > threshold){
                ConsumerSet update = new ConsumerSet();
                update.setBindTime(new Date());
                update.setActivationCode(activeCode);
                consumerSetMapper.updateByExampleSelective(update,consumerSetExample);
            }else{
                return "邀请频率过高,请稍后重试";
            }
        }else{
            ConsumerSet update = new ConsumerSet();
            update.setBindTime(new Date());
            update.setActivationCode(activeCode);
            consumerSetMapper.updateByExampleSelective(update,consumerSetExample);
        }
        RemoteResult<String> rs = cloudService.sendSms(phone,activeCode);
        if(!rs.isComplete()) throw  new RuntimeException("总有你想不到的意外");
        return rs.isComplete() ? "操作成功":"操作失败";
    }

    /**
     * 判断业主是否已激活
     *
     * @param projectNo
     * @return
     */
    @Override
    public Boolean selectOwnerIsActivatByProjectNo(String projectNo) {
        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<PreProjectGuide> ps = preProjectGuideMapper.selectByExample(preProjectGuideExample);

        if(ps.isEmpty() || ps.size() >1){
            return Boolean.TRUE;
        }
        PreProjectGuide p = ps.get(0);
        ConsumerSetExample consumerSetExample = new ConsumerSetExample();
        consumerSetExample.createCriteria().andConsumerIdEqualTo(p.getCustomerNo()).andIsBindEqualTo(SysConstants.YesOrNo.YES.shortVal());
        List<ConsumerSet> cs = consumerSetMapper.selectByExample(consumerSetExample);
        return !cs.isEmpty();
    }

    /**
     * 上传项目资料包
     *
     * @param projectDocumentContainer
     * @return
     */
    @Transactional
    @Override
    public String uploadProjectDocuments(ProjectDocumentContainer projectDocumentContainer) {

        List<ProjectDocumentVO> documents = projectDocumentContainer.getProjectDocuments();
        if(documents.isEmpty()){
            return "上传文件为空";
        }
        String projectNo = documents.get(0).getProjectNo();
        printInfoMes("清除旧资料包:{}",projectNo);
        ProjectDocument del = new ProjectDocument();
        del.setIsDel(SysConstants.YesOrNo.YES.shortVal());
        ProjectDocumentExample projectDocumentExample = new ProjectDocumentExample();
        projectDocumentExample.createCriteria().andProjectNoEqualTo(projectNo);
        projectDocumentMapper.updateByExampleSelective(del,projectDocumentExample);
        printInfoMes("写入新的资料包:{}",projectNo);
        documents.forEach(d->{
            ProjectDocument insert = new ProjectDocument();
            SpringBeanUtil.copy(d,insert);
            String filePath = d.getFileUrl();
            if(d.getFile() != null){
                filePath = WebFileUtil.fileCopy("/project/document",d.getFile());
                insert.setFileName(d.getFile().getOriginalFilename());
            }
            insert.setFileUrl(filePath);
            insert.setIsDel(SysConstants.YesOrNo.NO.shortVal());
            insert.setProjectNo(projectNo);
            if(d.getId() != null){
                projectDocumentExample.clear();
                projectDocumentMapper.updateByPrimaryKeySelective(insert);
            }else {
                projectDocumentMapper.insertSelective(insert);
            }
//            insert.setFileDesc(d.getFileD);
//            insert.setSortNo(d.getSortNo());
        });

        return "操作成功";
    }

    /**
     * 查询项目资料包
     *
     * @param projectNo
     * @return
     */
    @Override
    public List<ProjectDocument> listProjectDocuments(String projectNo) {
        if(StringUtils.isBlank(projectNo)){
            return Collections.EMPTY_LIST;
        }

        ProjectDocumentExample projectDocumentExample = new ProjectDocumentExample();
        projectDocumentExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal());

        return projectDocumentMapper.selectByExample(projectDocumentExample);
    }


    /**
     * 新增报价单条目信息
     * @param projectQuotationVO
     */
    private void saveProjectConstructionAndInfo(ProjectQuotationVO projectQuotationVO) {
        List<ProjectQuotationItemVO> allWaitSave =   projectQuotationVO.getItems();
        PreProjectConstructionInfo update = null;
        for(ProjectQuotationItemVO vo : allWaitSave){
            vo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            vo.setCreatTime(new Date());
            vo.setProjectNo(projectQuotationVO.getProjectNo());
            preProjectConstructionMapper.insertSelective(vo);

            update = vo.getPreProjectConstructionInfo();
            update.setCreateTime(new Date());
            update.setProjectNo(vo.getProjectNo());
            update.setConstrucionProjectNo(vo.getConstructionProjectNo());
            update.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            update.setProjectNo(projectQuotationVO.getProjectNo());
            preProjectConstructionInfoMapper.insertSelective(update);
        }
    }

    /**
     * 替换人员
     * @param projectDetailsVO
     */
    private void replacement(ProjectDetailsVO projectDetailsVO) {

        List<ProjectUserRoleVO> preProjectUserRoles = projectDetailsVO.getStaffs();
        PreProjectUserRoleExample del =new PreProjectUserRoleExample();
        del.createCriteria().andProjectNoEqualTo(projectDetailsVO.getProjectNo()).andRoleIdNotEqualTo(UserJobs.Owner.roleCode);
        preProjectUserRoleMapper.deleteByExample(del);
        for(PreProjectUserRole vo : preProjectUserRoles){
            String userName = "";
            UserInfoExample userInfoExample = new UserInfoExample();
            userInfoExample.createCriteria().andUserIdEqualTo(vo.getUserId());
            List<UserInfo> users = userInfoMapper.selectByExample(userInfoExample);

            if(!users.isEmpty() || !(users.size() > 1)){
                userName = users.get(0).getName();
            }
            PreProjectUserRole preProjectUserRole = new PreProjectUserRole();
            preProjectUserRole.setProjectNo(projectDetailsVO.getProjectNo());
            preProjectUserRole.setRoleId(vo.getRoleId());
            preProjectUserRole.setUserId(vo.getUserId());
            preProjectUserRole.setUserName(userName);
            preProjectUserRole.setRoleName(UserJobs.findByCodeStr(vo.getRoleId()).mes);
            preProjectUserRole.setIsJob(SysConstants.YesOrNo.YES.shortVal());
            preProjectUserRole.setIsTransfer(SysConstants.YesOrNo.NO.shortVal());
            preProjectUserRole.setCreateTime(new Date());
            preProjectUserRoleMapper.insertSelective(preProjectUserRole);

        }
    }


    /**
     * 删除主材信息
     * @param projectNo
     */
    private void delProjectMaterial(String projectNo) {
        PreProjectMaterial delObj = new PreProjectMaterial();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();
        preProjectMaterialExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        preProjectMaterialMapper.updateByExampleSelective(delObj,preProjectMaterialExample);

    }

    /**
     * 删除报价单及详情
     * @param projectNo
     */
    private void delProjectConstructionAndInfo(String projectNo) {

        PreProjectConstruction delObj = new PreProjectConstruction();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectConstructionExample preProjectConstructionExample = new PreProjectConstructionExample();
        preProjectConstructionExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal()).andProjectNoEqualTo(projectNo);
        preProjectConstructionMapper.updateByExampleSelective(delObj,preProjectConstructionExample);

        PreProjectConstructionInfo delInfo = new PreProjectConstructionInfo();
        delInfo.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectConstructionInfoExample preProjectConstructionInfoExample = new PreProjectConstructionInfoExample();
        preProjectConstructionInfoExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        preProjectConstructionInfoMapper.updateByExampleSelective(delInfo,preProjectConstructionInfoExample);


    }

    /**
     * 删除项目信息
     * @param projectNo
     */
    private void delProjectInfo(String projectNo) {
        // 标记项目基本信息
        PreProjectInfo delObj = new PreProjectInfo();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal()).andProjectNoEqualTo(projectNo);
        preProjectInfoMapper.updateByExampleSelective(delObj,preProjectInfoExample);
    }

}
