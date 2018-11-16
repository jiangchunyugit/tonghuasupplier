package cn.thinkfree.core.constants;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 施工订单枚举类
 */

public enum ConstructionStateEnumB {

    /**
     *  字段说明
     *
     *  designer = "设计师";
     *  platform = "运营平台";
     *  consumer = "消费者";
     *  constructionCompany = "装饰公司";
     *  constructor = "施工人员";
     *  1 可以取消
     *  0 不可取消
     */

    /**
     * 设计师-将订单转入施工 - 创建
     */
    //   STATE_500(500, "订单转入施工", "待签约", "等待平台派单", "", "", new Integer[]{510}),

    /**
     * 运营平台-派单给装饰公司 - 签约
     */
    STATE_500(500, "派单给装饰公司", "待签约", "等待公司派单", "等待公司派单", "",new Integer[]{510}),

    /**
     * 装饰公司-派单给服务人员 - 签约
     */
    STATE_510(510, "派单给服务人员", "待签约", "等待报价", "等待公司派单", "", new Integer[]{520}),

    /**
     * 装饰公司-施工报价完成- 签约
     */
    STATE_520(520, "施工报价完成", "待签约", "等待报价审核", "等待报价审核", "", new Integer[]{530}),

    /**
     * 装饰公司-审核完成- 签约
     */
    STATE_530(530, "审核完成", "待签约", "等待合同录入", "等待合同录入", "", new Integer[]{540,520}),

    /**
     * 装饰公司-合同录入- 签约
     */
    STATE_540(540, "合同录入", "待签约", "等待签约", "等待签约", "", new Integer[]{550}),

    /**
     * 装饰公司-确认线下签约完成（自动创建工地项目）- 签约
     */
    STATE_550(550, "确认线下签约完成(自动创建工地项目)", "待支付", "等待首付款支付", "等待首付款支付", "等待首付款支付", new Integer[]{600}),

    /**
     * 消费者- 首期款支付
     */
    STATE_600(600, "支付首期款", "待开工", "等待开工", "等待开工", "等待开工", new Integer[]{605}),

    /**
     * 施工人员- 施工中
     */
    STATE_605(605, "开工报告", "施工中", "施工中", "施工中", "施工中", new Integer[]{610}),

    /* 阶段款支付 - 施工人员 验收 */
    STATE_610(610, "阶段验收通过", "待支付", "等待阶段款支付", "等待阶段款支付", "等待阶段款支付", new Integer[]{615}),

    /* 施工中 - 消费者 支付  */
    STATE_615(615, "支付阶段款", "施工中", "施工中", "施工中", "施工中", new Integer[]{690}),

    /**
     * 施工人员- 尾款支付
     */
    STATE_690(690, "竣工验收通过", "待支付", "等待尾款支付", "等待尾款支付", "等待尾款支付",  new Integer[]{700}),

    //TODO
    /**
     * 消费者- 订单完成
     */
    STATE_700(700, "支付尾款", "已完成", "已完成", "已完成", "已完成", new Integer[]{}),

    //TODO
    /**
     *  消费者 签约阶段逆向
     */
    STATE_710(710,"取消订单", "等待处理——已关闭", "已关闭", "已关闭", "已关闭", new Integer[]{720}),

    /**
     *  消费者 支付未开工逆向
     */
    STATE_720(720, "取消订单", "退款中", "退款待审核", "退款待审核", "退款待审核", new Integer[]{730}),


    /**
     *  消费者 支付未开工逆向
     */
    STATE_730(730, "审核通过", "已完成", "已完成", "已完成", "已完成", new Integer[]{}),


    /**
     * 订单关闭 --
     */
    STATE_888(888, "订单关闭", "订单关闭", "订单关闭", "订单关闭", "订单关闭", new Integer[]{}),

    ;


    // 状态值
    private int state;
    // 操作-说明
    private String operateInfo;
    // 消费者当前状态
    private String stateConsumer;
    // 运营平台当前状态
    private String statePlatform;
    // 装饰公司当前状态
    private String stateConstructionCompany;
    // 施工人员状态
    private String stateConstructor;
    //下一个状态
    private Integer[] nextStates = new Integer[]{};

    ConstructionStateEnumB(int state, String operateInfo, String stateConsumer, String statePlatform, String stateConstructionCompany, String stateConstructor,Integer[] nextStates) {
        this.state = state;
        this.operateInfo = operateInfo;
        this.stateConsumer = stateConsumer;
        this.statePlatform = statePlatform;
        this.stateConstructionCompany = stateConstructionCompany;
        this.stateConstructor = stateConstructor;
        this.nextStates = nextStates;
    }

    /**
     * 查询状态码 对应的状态
     *
     * @return
     */
    public static String getNowStateInfo(Integer state,int type) {
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnumB.values();
        for (ConstructionStateEnumB constructionStateEnum : stateEnums) {
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
    public List<ConstructionStateEnumB> getNextStates() {
        List<ConstructionStateEnumB> constructionStateEnumBs = new ArrayList<>();
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnumB.values();
        List<Integer> states = Arrays.asList(nextStates);
        for(ConstructionStateEnumB stateEnum : stateEnums){
            if(states.contains(stateEnum.state)){
                constructionStateEnumBs.add(stateEnum);
            }
        }
        return constructionStateEnumBs;
    }


    /**
     * 根据类型获取订单状态类型列表
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    public static List<Map<String, Object>> allStates(int type) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnumB.values();

        for (ConstructionStateEnumB constructionStateEnum : stateEnums) {
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
        ConstructionStateEnumB ConstructionStateEnum = queryByState(state);
        List<Integer> integers = new ArrayList<>();
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnumB stateEnum : stateEnums) {
            if (type == 1 && ConstructionStateEnum.statePlatform.equals(stateEnum.statePlatform)) {
                integers.add(stateEnum.state);
            }
            if (type == 2 && ConstructionStateEnum.stateConstructionCompany.equals(stateEnum.stateConstructionCompany)) {
                integers.add(stateEnum.state);
            }
            if (type == 3 && ConstructionStateEnum.stateConstructor.equals(stateEnum.stateConstructor)) {
                integers.add(stateEnum.state);
            }
            if (type == 4 && ConstructionStateEnum.stateConsumer.equals(stateEnum.stateConsumer)) {
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
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnumB.values();
        for (ConstructionStateEnumB constructionStateEnum : stateEnums) {
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
    public static ConstructionStateEnumB queryByState(int state) {
        ConstructionStateEnumB[] stateEnums = ConstructionStateEnumB.values();
        for (ConstructionStateEnumB constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state) {
                return constructionStateEnum;
            }
        }
        throw new RuntimeException("无效的状态值");
    }

    public int getState() {
        return state;
    }
}
