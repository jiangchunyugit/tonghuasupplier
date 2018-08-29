package cn.thinkfree.service.index;

import cn.thinkfree.core.security.dao.SecurityResourceDao;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.DateUtils;
import cn.thinkfree.database.constants.IndexChartUnit;
import cn.thinkfree.database.constants.MenuType;
import cn.thinkfree.database.mapper.MenuMapper;
import cn.thinkfree.database.model.Menu;
import cn.thinkfree.database.model.MenuExample;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IndexServiceImpl implements IndexService  {


    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    MenuMapper menuMapper;


    /**
     * 首页总览
     * @return
     */
    @Override
    public IndexReportVO summary() {
        UserVO userVO= (UserVO) SessionUserDetailsUtil.getUserDetails();
        IndexUserReportVO indexUserReportVO = userService.countCompanyUser(userVO.getRelationMap());
        IndexProjectReportVO indexProjectReportVO = projectService.countProjectReportVO(userVO.getRelationMap());
        return new IndexReportVO(indexProjectReportVO,indexUserReportVO);
    }

    /**
     * 查询首页树形菜单
     *
     * @return
     */
    @Override
    public List<IndexMenuVO> listIndexMenu() {

        UserVO userVO= (UserVO) SessionUserDetailsUtil.getUserDetails();

        if(userVO.getResources() == null || userVO.getResources().isEmpty()){
            return Collections.emptyList();
        }

        List<String> resouceCode = userVO.getResources().stream().map(SystemResource::getCode).collect(Collectors.toList());
        if(resouceCode.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        MenuExample menuExample = new MenuExample();
        menuExample.setOrderByClause(" sort_num ");
        menuExample.createCriteria().
                andResourceCodeIn(resouceCode);

        List<Menu> menus = menuMapper.selectByExample(menuExample);
        System.out.println(menus);
        return convertMenus(menus);
    }

    /**
     * 项目周期总览
     *
     * @param unit
     * @return
     */
    @Override
    public List<IndexProjectChartItemVO> summaryProjectChart(Integer unit) {
        if(IndexChartUnit.Week.code.equals(unit)){
            List<IndexProjectChartItemVO> dataLine = projectService.summaryProjectChart(DateUtils.firstDayOfWeek(), DateUtils.lastDayOfWeek());
            List<String> weekDays = DateUtils.getWeekDays();
            List<IndexProjectChartItemVO> result = makeUpDateLine(dataLine,weekDays);
            return result;
        }else if(IndexChartUnit.Month.code.equals(unit)){
            List<IndexProjectChartItemVO> dataLine = projectService.summaryProjectChart(DateUtils.firstDayOfMonth(), DateUtils.lastDayOfMonth());
            List<String> monthDays = DateUtils.getMonthDays();
            List<IndexProjectChartItemVO> result = makeUpDateLine(dataLine,monthDays);
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 补全空日期
     * @param dataLine
     * @param days
     * @return
     */
    private List<IndexProjectChartItemVO> makeUpDateLine(List<IndexProjectChartItemVO> dataLine, List<String> days) {
        List<IndexProjectChartItemVO> result = new ArrayList<>();
        for(String day:days){
            Optional<IndexProjectChartItemVO> op = dataLine.stream().filter(d -> d.getDateLine().equals(day)).findFirst();
            if(op.isPresent()){
                result.add(op.get());
            }else{
                result.add(new IndexProjectChartItemVO(day, BigInteger.ZERO.toString()));
            }
        }
        return result;
    }

    /**
     * 转换 菜单为VO
     * @param menus
     * @return
     */
    private List<IndexMenuVO> convertMenus(List<Menu> menus) {
        List<Menu> root = menus.stream().filter(m -> MenuType.ROOT.code.equals(m.getPid())).collect(Collectors.toList());
        return root.stream().map(r->{
            IndexMenuVO indexMenuVO = new IndexMenuVO(r);
            indexMenuVO.setChild(menus.stream()
                    .filter(m -> r.getId().equals(m.getPid()))
                    .map(m->new IndexMenuVO(m))
                    .collect(Collectors.toList())
            );
            return indexMenuVO;
        }).collect(Collectors.toList());
    }
}
