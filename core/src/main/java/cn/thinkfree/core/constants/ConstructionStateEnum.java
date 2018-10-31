package cn.thinkfree.core.constants;


import java.util.*;

/**
 * 施工订单枚举类
 */

public enum ConstructionStateEnum {

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
    STATE_500(500, new String[]{"designer"}, "订单转入施工", "待签约", "等待平台派单", "---", "---", 1, new Integer[]{510}),

    /**
     * 运营平台-派单给装饰公司 - 签约
     */
    STATE_510(510, new String[]{"platform"}, "派单给装饰公司", "待签约", "等待公司派单", "等待公司派单", "---", 1, new Integer[]{520}),

    /**
     * 装饰公司-派单给服务人员 - 签约
     */
    STATE_520(520, new String[]{"constructionCompany"}, "派单给服务人员", "待签约", "等待公司派单", "等待公司派单", "---", 1, new Integer[]{530}),

    /**
     * 装饰公司-施工报价完成- 签约
     */
    STATE_530(530, new String[]{"constructionCompany"}, "施工报价完成", "待签约", "等待报价审核", "等待报价审核", "---", 1, new Integer[]{540}),

    /**
     * 装饰公司-审核完成- 签约
     */
    STATE_540(540, new String[]{"constructionCompany"}, "审核完成", "待签约", "等待合同录入", "等待合同录入", "---", 1, new Integer[]{550}),

    /**
     * 装饰公司-合同录入- 签约
     */
    STATE_550(550, new String[]{"constructionCompany"}, "合同录入", "待签约", "等待签约", "等待签约", "---", 1, new Integer[]{560}),

    /**
     * 装饰公司-确认线下签约完成（自动创建工地项目）- 签约
     */
    STATE_560(560, new String[]{"constructionCompany"}, "确认线下签约完成(自动创建工地项目)", "待支付", "等待首付款支付", "等待首付款支付", "等待首付款支付", 1, new Integer[]{560}),

    //TODO  支付方式状态


    ;


    // 状态值
    private int state;
    // 操作者
    private String[] operater = new String[]{};
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
    // 是否自动可取消
    private int isCancel;
    // 下一步状态
    private Integer[] nextStates = new Integer[]{};

    ConstructionStateEnum(int state, String[] operater, String operateInfo, String stateConsumer, String statePlatform, String stateConstructionCompany, String stateConstructor, int isCancel, Integer[] nextStates) {
        this.state = state;
        this.operater = operater;
        this.operateInfo = operateInfo;
        this.stateConsumer = stateConsumer;
        this.statePlatform = statePlatform;
        this.stateConstructionCompany = stateConstructionCompany;
        this.stateConstructor = stateConstructor;
        this.isCancel = isCancel;
        this.nextStates = nextStates;
    }

    /**
     *  下一步状态
     * @return
     */
    public static List<Integer> getNextStates(int state) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state){
                List<Integer> statesList = Arrays.asList(constructionStateEnum.nextStates);
                return statesList;
            }
        }
        return null;
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
        List<String> stateNames = new ArrayList<>();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            String stateName = null;
            switch (type) {
                case 1:
                    stateName = constructionStateEnum.statePlatform;
                    break;
                case 2:
                    stateName = constructionStateEnum.stateConstructionCompany;
                    break;
                case 3:
                    stateName = constructionStateEnum.stateConstructor;
                    break;
                case 4:
                    stateName = constructionStateEnum.stateConsumer;
                    break;
                default:
                    break;
            }
            if (stateName == null || stateNames.contains(stateName)) {
                continue;
            }
            stateNames.add(stateName);
            Map<String, Object> map = new HashMap<>();
            map.put("key", constructionStateEnum.state);
            map.put("val", constructionStateEnum.statePlatform);
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
        ConstructionStateEnum ConstructionStateEnum = queryByState(state);
        List<Integer> integers = new ArrayList<>();
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum stateEnum : stateEnums) {
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
     * @param role
     * @return
     */
    public static String queryStateByRole(int state, String role) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            if (constructionStateEnum.state == state) {
                switch (role) {
                    case "platform":
                        return constructionStateEnum.statePlatform;
                    case "consumer":
                        return constructionStateEnum.stateConsumer;
                    case "constructionCompany":
                        return constructionStateEnum.stateConstructionCompany;
                    case "constructor":
                        return constructionStateEnum.stateConstructor;
                    default:
                        return null;
                }
            }
        }
        return null;
    }

    /**
     * 查询角色权限
     *
     * @param role
     * @return
     */
    public static boolean queryIsState(String role) {
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        for (ConstructionStateEnum constructionStateEnum : stateEnums) {
            List<String> operater = Arrays.asList(constructionStateEnum.operater);
            if (operater.contains(role)){
                return true;
            }
        }
        return false;
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
}
