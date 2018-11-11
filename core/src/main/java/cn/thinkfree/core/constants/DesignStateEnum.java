package cn.thinkfree.core.constants;

import java.util.*;

/**
 * @author xusonghui
 * 设计订单枚举类
 */

public enum DesignStateEnum {
    /**
     * 消费者	发布需求
     */
    STATE_1(1, "预约中", "待派单", "无", "无", "创建订单", new Integer[]{10, 999, 330}),
    /**
     * 运营平台	运营平台人员派单至设计公司
     */
    STATE_10(10, "预约中", "平台已派单", "待派单", "无", "运营平台人员派单至设计公司", new Integer[]{20, 1, 330}),
    /**
     * 设计公司	设计公司派单给设计师
     */
    STATE_20(20, "预约中", "设计公司已派单", "设计公司已派单", "待接单", "设计公司派单给设计师", new Integer[]{30, 10, 330}),
    /**
     * 设计师	设计师接单
     */
    STATE_30(30, "服务中", "设计师已接单", "设计师已接单", "设计师已接单", "设计师接单", new Integer[]{40, 330}),
    /**
     * 设计师	发起量房预约
     */
    STATE_40(40, "量房待支付", "设计师发起量房预约", new Integer[]{50, 80, 330}),
    /**
     * 消费者	支付量房费
     */
    STATE_50(50, "量房待交付", "业主支付量房费", new Integer[]{60, 90}),
    /**
     * 设计师	上传量房资料
     */
    STATE_60(60, "量房待确认", "设计师上传量房资料", new Integer[]{70}),
    /**
     * 消费者	确认量房交付物
     */
    STATE_70(70, "量房已确认", "业主确认量房交付物", new Integer[]{130}),
    /**
     * 消费者	拒绝支付量房费（超时未支付）
     */
    STATE_80(80, "订单关闭", "订单关闭（量房支付超时）", "订单关闭（量房支付超时）", "订单关闭（量房支付超时）", "业主拒绝支付量房费（超时未支付）", new Integer[]{}),
    /**
     * 消费者	申请退款
     */
    STATE_90(90, "退款中", "量房退款待设计公司审核", "量房退款待审核", "量房退款待审核", "业主对量房款发起了申请退款", new Integer[]{100, 50}),
    /**
     * 设计公司	同意退款
     */
    STATE_100(100, "退款中", "量房退款待平台审核", "量房退款待平台审核", "量房退款待平台审核", "设计公司同意退款", new Integer[]{110, 50}),
    /**
     * 运营平台	同意退款
     */
    STATE_110(110, "退款中", "平台同意退款", new Integer[]{120, 50}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_120(120, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 发起设计合同及报价
     */
    STATE_130(130, "量房已确认", "合同待审核", "合同待审核", "合同待审核", "设计师发起设计合同及报价", new Integer[]{140, 220, 131}),
    /**
     * 公司审核不通过
     */
    STATE_131(131, "量房已确认", "合同待审核", "审核不通过", "合同审核未通过，待修正", "公司审核不通过", new Integer[]{130}),

    /**
     * 分期款状态<br/>
     * 设计公司	审批合同（通过）
     */
    STATE_140(140, "首款待支付", "合同审核通过", new Integer[]{150, 280}),
    /**
     * 消费者	支付订单首款
     */
    STATE_150(150, "3D待交付", "业主支付订单收款", new Integer[]{160, 151}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_151(151, "退款中", "首款退款待设计公司审核", "首款退款待审核", "首款退款待审核", "业主对合同首款发起了申请退款", new Integer[]{152, 150}),
    /**
     * 设计公司	同意退款
     */
    STATE_152(152, "退款中", "首款退款待平台审核", "首款退款待平台审核", "首款退款待平台审核", "设计公司同意退款", new Integer[]{153, 150}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_153(153, "退款中", "平台同意退款", new Integer[]{154, 150}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_154(154, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 设计师	上传3D效果图
     */
    STATE_160(160, "3D待确认", "设计师上传了3D效果图", new Integer[]{170, 161}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_161(161, "退款中", "首款退款待设计公司审核", "首款退款待审核", "首款退款待审核", "业主对合同首款发起了申请退款", new Integer[]{162, 160}),
    /**
     * 设计公司	同意退款
     */
    STATE_162(162, "退款中", "首款退款待平台审核", "首款退款待平台审核", "首款退款待平台审核", "设计公司同意退款", new Integer[]{163, 160}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_163(163, "退款中", "平台同意退款", new Integer[]{164, 160}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_164(164, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 消费者	确认3D效果图
     */
    STATE_170(170, "中期款待支付", "业主确认了3D效果图", new Integer[]{180, 280}),
    /**
     * 消费者	支付订单中期款
     */
    STATE_180(180, "施工图待交付", "业主支付了中期款", new Integer[]{190, 181}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_181(181, "退款中", "中期款退款待设计公司审核", "中期款退款待审核", "中期款退款待审核", "业主对合同中期款发起了申请退款", new Integer[]{182, 180}),
    /**
     * 设计公司	同意退款
     */
    STATE_182(182, "退款中", "中期款退款待平台审核", "中期款退款待平台审核", "中期款退款待平台审核", "设计公司同意退款", new Integer[]{183, 180}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_183(183, "退款中", "平台同意退款", new Integer[]{184, 180}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_184(184, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 设计师	上传施工图
     */
    STATE_190(190, "施工图待确认", "设计师上传了施工图", new Integer[]{200, 191}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_191(191, "退款中", "中期款退款待设计公司审核", "中期款退款待审核", "中期款退款待审核", "业主对合同中期款发起了申请退款", new Integer[]{192, 190}),
    /**
     * 设计公司	同意退款
     */
    STATE_192(192, "退款中", "中期款退款待平台审核", "中期款退款待平台审核", "中期款退款待平台审核", "设计公司同意退款", new Integer[]{193, 190}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_193(193, "退款中", "平台同意退款", new Integer[]{194, 190}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_194(194, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 消费者	确认交付物
     */
    STATE_200(200, "尾款待支付", "业主确认了交付物", new Integer[]{210}),
    /**
     * 消费者	支付订单尾款
     */
    STATE_210(210, "交易完成", "业主支付了订单尾款", new Integer[]{}),
    /**
     * 全款状态<br/>
     * 设计公司	审批合同通过
     */
    STATE_220(220, "全款待支付", "合同审核通过", new Integer[]{230, 280}),
    /**
     * 消费者	支付订单款项（全款）
     */
    STATE_230(230, "3D待交付", "业主支付了订单（全款）", new Integer[]{240, 231}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_231(231, "退款中", "全款退款待设计公司审核", "全款退款待审核", "全款退款待审核", "业主对合同款发起了申请退款", new Integer[]{232, 230}),
    /**
     * 设计公司	同意退款
     */
    STATE_232(232, "退款中", "全款退款待平台审核", "全款退款待平台审核", "全款退款待平台审核", "设计公司同意退款", new Integer[]{233, 230}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_233(233, "退款中", "平台同意退款", new Integer[]{234, 230}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_234(234, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 设计师	上传3D效果图
     */
    STATE_240(240, "3D待确认", "设计师上传了3D效果图", new Integer[]{250, 241}),
    /**
     * 消费者	申请退款（收款或全款）
     */
    STATE_241(241, "退款中", "全款退款待设计公司审核", "全款退款待审核", "全款退款待审核", "业主对合同款发起了申请退款", new Integer[]{242, 240}),
    /**
     * 设计公司	同意退款
     */
    STATE_242(242, "退款中", "全款退款待平台审核", "全款退款待平台审核", "全款退款待平台审核", "设计公司同意退款", new Integer[]{243, 240}),
    /**
     * 运营平台（财务）	同意退款
     */
    STATE_243(243, "退款中", "平台同意退款", new Integer[]{244, 240}),
    /**
     * 运营平台（财务）	放款
     */
    STATE_244(244, "订单关闭", "财务放款", new Integer[]{}),
    /**
     * 消费者	确认3D效果图
     */
    STATE_250(250, "施工图待交付", "业主确认了3D效果图", new Integer[]{260}),
    /**
     * 设计师	上传施工图
     */
    STATE_260(260, "施工图待确认", "设计师上传了施工图", new Integer[]{270}),
    /**
     * 消费者	确认交付物	交易完成
     */
    STATE_270(270, "交易完成", "业主确认了交付物", new Integer[]{}),
    /**
     * 订单阶段逆行<br/>
     * 消费者	拒绝支付订单款项（超时未支付）
     */
    STATE_280(280, "订单关闭", "订单关闭（支付超时）", "订单关闭（支付超时）", "订单关闭（支付超时）", "业主拒绝支付订单，订单被关闭", new Integer[]{}),
    /**
     * 消费者	订单终止
     */
    STATE_330(330, "订单关闭", "订单关闭（消费者终止）", "订单关闭（消费者终止）", "订单关闭（消费者终止）", "业主终止了订单", new Integer[]{}),
    /**
     * 运营平台手动关闭订单
     */
    STATE_999(999, "订单关闭", "平台关闭了订单", new Integer[]{});
    private int state;
    private String stateCompany;
    private String statePlatform;
    private String stateDesigner;
    private String stateOwner;
    private String logText;
    private Integer[] nextStates = new Integer[]{};

    DesignStateEnum(int state, String stateOwner, String statePlatform, String stateCompany, String stateDesigner, String logText, Integer[] nextStates) {
        this.state = state;
        this.stateCompany = stateCompany;
        this.statePlatform = statePlatform;
        this.stateDesigner = stateDesigner;
        this.stateOwner = stateOwner;
        this.nextStates = nextStates;
        this.logText = logText;
    }

    DesignStateEnum(int state, String stateName, String logText, Integer[] nextStates) {
        this.state = state;
        this.stateCompany = stateName;
        this.statePlatform = stateName;
        this.stateDesigner = stateName;
        this.stateOwner = stateName;
        this.nextStates = nextStates;
        this.logText = logText;
    }

    public List<DesignStateEnum> getNextStates() {
        List<DesignStateEnum> designStateEnums = new ArrayList<>();
        DesignStateEnum[] stateEnums = DesignStateEnum.values();
        List<Integer> states = Arrays.asList(nextStates);
        for(DesignStateEnum stateEnum : stateEnums){
            if(states.contains(stateEnum.state)){
                designStateEnums.add(stateEnum);
            }
        }
        return designStateEnums;
    }

    /**
     *
     * @param type 1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态
     * @return
     */
    public String getStateName(int type){
        switch (type) {
            case 1:
                return statePlatform;
            case 2:
                return stateCompany;
            case 3:
                return stateDesigner;
            case 4:
                return stateOwner;
            default:
                throw new RuntimeException("无效的类型");
        }
    }

    /**
     * 根据类型获取订单状态类型列表
     *
     * @param type ，1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态
     * @return
     */
    public static List<Map<String, Object>> allStates(int type) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        DesignStateEnum[] stateEnums = DesignStateEnum.values();
        List<String> stateNames = new ArrayList<>();
        for (DesignStateEnum designStateEnum : stateEnums) {
            String stateName = null;
            switch (type) {
                case 1:
                    stateName = designStateEnum.statePlatform;
                    break;
                case 2:
                    stateName = designStateEnum.stateCompany;
                    break;
                case 3:
                    stateName = designStateEnum.stateDesigner;
                    break;
                case 4:
                    stateName = designStateEnum.stateOwner;
                    break;
            }
            if(stateName == null || stateNames.contains(stateName)){
                continue;
            }
            stateNames.add(stateName);
            Map<String, Object> map = new HashMap<>();
            map.put("key", designStateEnum.state);
            map.put("val", designStateEnum.statePlatform);
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * 根据类型获取所有类型值
     *
     * @param state 订单状态值
     * @param type ，1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态
     * @return
     */
    public static List<Integer> queryStatesByState(int state,int type){
        DesignStateEnum designStateEnum = queryByState(state);
        List<Integer> integers = new ArrayList<>();
        DesignStateEnum[] stateEnums = DesignStateEnum.values();
        for (DesignStateEnum stateEnum : stateEnums) {
            if(type == 1 && designStateEnum.statePlatform.equals(stateEnum.statePlatform)){
                integers.add(stateEnum.state);
            }
            if(type == 2 && designStateEnum.stateCompany.equals(stateEnum.stateCompany)){
                integers.add(stateEnum.state);
            }
            if(type == 3 && designStateEnum.stateDesigner.equals(stateEnum.stateDesigner)){
                integers.add(stateEnum.state);
            }
            if(type == 4 && designStateEnum.stateOwner.equals(stateEnum.stateOwner)){
                integers.add(stateEnum.state);
            }
        }
        return integers;
    }

    /**
     * 根据枚举的状态值查询枚举
     *
     * @param state 状态值
     * @return
     */
    public static DesignStateEnum queryByState(int state) {
        DesignStateEnum[] stateEnums = DesignStateEnum.values();
        for (DesignStateEnum designStateEnum : stateEnums) {
            if (designStateEnum.state == state) {
                return designStateEnum;
            }
        }
        throw new RuntimeException("无效的状态值");
    }

    public int getState() {
        return state;
    }

    public String getLogText() {
        return logText;
    }
}
