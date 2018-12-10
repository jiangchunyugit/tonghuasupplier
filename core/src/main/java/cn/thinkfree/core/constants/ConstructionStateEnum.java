package cn.thinkfree.core.constants;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 施工订单枚举类
 */
public enum ConstructionStateEnum {

    /**
     * 运营平台-派单给装饰公司 - 签约
     */
    STATE_500(500, "派单给装饰公司", "待签约", "等待公司派单", "等待公司派单", "",new Integer[]{510}, true),
    /**
     * 装饰公司-派单给服务人员 - 签约
     */
    STATE_510(510, "派单给服务人员", "待签约", "等待报价", "等待公司派单", "", new Integer[]{520}, true),
    /**
     * 装饰公司-施工报价完成- 签约
     */
    STATE_520(520, "施工报价完成", "待签约", "等待报价审核", "等待报价审核", "", new Integer[]{530}, true),
    /**
     * 装饰公司-审核完成- 签约
     */
    STATE_530(530, "审核完成", "待签约", "等待合同录入", "等待合同录入", "", new Integer[]{540,520}, true),
    /**
     * 装饰公司-合同录入- 签约
     */
    STATE_540(540, "合同录入", "待签约", "等待签约", "等待签约", "", new Integer[]{550}, true),
    /**
     * 装饰公司-确认线下签约完成（自动创建工地项目）- 签约
     */
    /* 签约完成-是当前操作 */
    STATE_550(550, "确认线下签约完成(自动创建工地项目)", "待支付", "等待首付款支付", "等待首付款支付", "等待首付款支付", new Integer[]{600}, true),
    /**
     * 消费者- 首期款支付
     */
    STATE_600(600, "支付首期款（当前操作）", "等待开工报告", "等待开工报告", "等待开工报告", "等待开工报告", true),
    /**
     * 施工人员- 开工报告
     */
    STATE_610(610, "开工报告（当前操作）", "等待开工", "等待开工", "等待开工", "等待开工", true),
    /**
     *  阶段款支付 - 施工人员 验收
     */
    STATE_620(620, "阶段验收通过（当前操作）", "待支付", "等待阶段款支付", "等待阶段款支付", "等待阶段款支付", true),
    /**
     * 施工中 - 消费者 支付
     */
    STATE_630(630, "支付阶段款 （当前操作）", "施工中", "阶段施工中", "阶段施工中", "阶段施工中", true),
    /**
     * 施工人员- 待尾款支付
     */
    STATE_690(690, "竣工验收通过（当前操作）", "待支付", "等待尾款支付", "等待尾款支付", "等待尾款支付", true),
    /**
     * 消费者- 订单完成
     */
    STATE_700(700, "支付尾款（当前操作）", "已完成", "已完成", "已完成", "已完成", true),
    /**
     *  消费者 签约阶段逆向
     */
    STATE_710(710,"取消订单（当前操作）", "已关闭", "已关闭", "已关闭", "已关闭", false),
    /**
     *  消费者 支付未开工逆向
     */
    STATE_720(720, "取消订单（当前操作）", "退款中", "退款待审核", "退款待审核", "退款待审核", new Integer[]{730}, false),
    /**
     *  消费者 支付未开工逆向-审核通过
     */
    STATE_730(730, "审核通过（当前操作）", "已完成", "已完成", "已完成", "已完成", false),
    /**
     * 订单关闭 --
     */
    STATE_888(888, "订单关闭", "订单关闭", "订单关闭", "订单关闭", "订单关闭", false),
    ;

    /**
     * 状态值
     */
    private int state;
    /**
     * 操作-说明
     */
    private String operateInfo;
    /**
     * 消费者端显示状态名称
     */
    private String stateConsumer;
    /**
     * 运营平台显示状态名称
     */
    private String statePlatform;
    /**
     * 装饰公司显示状态名称
     */
    private String stateConstructionCompany;
    /**
     * 施工人员显示状态名称
     */
    private String stateConstructor;
    /**
     * 下一个状态集合
     */
    private Integer[] nextStates;
    /**
     * true:正向状态;false:逆向状态
     */
    private boolean stateType;

    ConstructionStateEnum(int state, String operateInfo, String stateConsumer, String statePlatform, String stateConstructionCompany, String stateConstructor, Integer[] nextStates, boolean stateType) {
        this.state = state;
        this.operateInfo = operateInfo;
        this.stateConsumer = stateConsumer;
        this.statePlatform = statePlatform;
        this.stateConstructionCompany = stateConstructionCompany;
        this.stateConstructor = stateConstructor;
        this.nextStates = nextStates;
        this.stateType = stateType;
    }

    ConstructionStateEnum(int state, String operateInfo, String stateConsumer, String statePlatform, String stateConstructionCompany, String stateConstructor, boolean stateType) {
        this.state = state;
        this.operateInfo = operateInfo;
        this.stateConsumer = stateConsumer;
        this.statePlatform = statePlatform;
        this.stateConstructionCompany = stateConstructionCompany;
        this.stateConstructor = stateConstructor;
        this.nextStates = new Integer[]{};
        this.stateType = stateType;
    }

    /**
     * 查询状态码 对应的状态
     *
     * @return
     */
    public static String getNowStateInfo(Integer state,int type) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state) {
                switch (type) {
                    case 1:
                        return constructionStateEnum.statePlatform;
                    case 2:
                        return constructionStateEnum.stateConstructionCompany;
                    case 3:
                        return constructionStateEnum.stateConstructor;
                    case 4:
                        return constructionStateEnum.stateConsumer;
                    default:
                        break;
                }
            }
        }
        return null;
    }

    /**
     *  下一步状态
     * @return
     */
    public List<ConstructionStateEnum> getNextStates() {
        List<ConstructionStateEnum> constructionStates = new ArrayList<>();
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        List<Integer> states = Arrays.asList(nextStates);
        for(ConstructionStateEnum stateEnum : stateEnums){
            if(states.contains(stateEnum.state)){
                constructionStates.add(stateEnum);
            }
        }
        return constructionStates;
    }


    /**
     * 根据类型获取订单状态类型列表
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    public static List<Map<String, Object>> allStates(int type) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();

        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            int stateCode = 0;
            String stateInfo = "";
            switch (type) {
                case 1:
                    stateCode = constructionStateEnum.state;
                    stateInfo = constructionStateEnum.statePlatform;
                    break;
                case 2:
                    stateCode = constructionStateEnum.state;
                    stateInfo = constructionStateEnum.stateConstructionCompany;
                    break;
                case 3:
                    stateCode = constructionStateEnum.state;
                    stateInfo = constructionStateEnum.stateConstructor;
                    break;
                case 4:
                    stateCode = constructionStateEnum.state;
                    stateInfo = constructionStateEnum.stateConsumer;
                    break;
                default:
                    break;
            }
            if (StringUtils.isBlank(stateInfo)){
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("key", stateCode);
            map.put("val", stateInfo);
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * 根据类型获取所有类型值
     *
     * @param state 订单状态值
     * @param type  ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    public static List<Integer> queryStatesByState(int state, int type) {
        ConstructionStateEnum constructionState = queryByState(state);
        List<Integer> integers = new ArrayList<>();
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum stateEnum : stateEnums) {
            if (type == 1 && constructionState.statePlatform.equals(stateEnum.statePlatform)) {
                integers.add(stateEnum.state);
            }
            if (type == 2 && constructionState.stateConstructionCompany.equals(stateEnum.stateConstructionCompany)) {
                integers.add(stateEnum.state);
            }
            if (type == 3 && constructionState.stateConstructor.equals(stateEnum.stateConstructor)) {
                integers.add(stateEnum.state);
            }
            if (type == 4 && constructionState.stateConsumer.equals(stateEnum.stateConsumer)) {
                integers.add(stateEnum.state);
            }
        }
        return integers;
    }

    /**
     * 根据状态 & 角色 - 查询当前状态说明
     *
     * @param state
     * @param type
     * @return
     */
    public static String queryStateByRole(Integer state, int type) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state) {
                switch (type) {
                    case 1:
                        return constructionStateEnum.statePlatform;
                    case 2:
                        return constructionStateEnum.stateConstructionCompany;
                    case 3:
                        return constructionStateEnum.stateConstructor;
                    case 4:
                        return constructionStateEnum.stateConsumer;
                    default:
                        return null;
                }
            }
        }
        return null;
    }


    /**
     * 根据类型获取所有类型值
     *
     * @param type  ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    public String getStateName(int type){
        switch (type) {
            case 1:
                return statePlatform;
            case 2:
                return stateConstructionCompany;
            case 3:
                return stateConstructor;
            case 4:
                return stateConsumer;
            default:
                throw new RuntimeException("无效的类型");
        }
    }

    /**
     * 根据枚举的状态值查询枚举
     *
     * @param state 状态值
     * @return
     */
    public static ConstructionStateEnum queryByState(int state) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state) {
                return constructionStateEnum;
            }
        }
        throw new RuntimeException("无效的状态值");
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(String operateInfo) {
        this.operateInfo = operateInfo;
    }

    public String getStateConsumer() {
        return stateConsumer;
    }

    public void setStateConsumer(String stateConsumer) {
        this.stateConsumer = stateConsumer;
    }

    public String getStatePlatform() {
        return statePlatform;
    }

    public void setStatePlatform(String statePlatform) {
        this.statePlatform = statePlatform;
    }

    public String getStateConstructionCompany() {
        return stateConstructionCompany;
    }

    public void setStateConstructionCompany(String stateConstructionCompany) {
        this.stateConstructionCompany = stateConstructionCompany;
    }

    public String getStateConstructor() {
        return stateConstructor;
    }

    public void setStateConstructor(String stateConstructor) {
        this.stateConstructor = stateConstructor;
    }

    public void setNextStates(Integer[] nextStates) {
        this.nextStates = nextStates;
    }

    public boolean isStateType() {
        return stateType;
    }

    public void setStateType(boolean stateType) {
        this.stateType = stateType;
    }
}
