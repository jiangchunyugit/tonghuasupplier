package cn.thinkfree.database.constants;

public class SyncOrderEnum {

    /**
     * 合同付款方式
     */
    public enum ContractType {
        /**
         * 全额付款
         */
        Full(1, "全款"),
        /**
         * 分期付款
         */
        multiple(2, "分期");

        public Integer code;
        ContractType(Integer code, String mes) {
            this.code = code;
        }
    }

    /**
     * 订单类型
     */
    public enum Type{
        /**
         * 设计订单
         */
        Design(1),
        /**
         * 装饰订单
         */
        Construction(2),
        /**
         * 合同订单
         */
        Contract(3);

        public Integer code;
        Type(Integer code) {
            this.code = code;
        }
    }

    /**
     * 费用类型
     */
    public enum SubType{

        /**
         * 量房费
         */
        VolumeRoom(1),
        /**
         * 担保金
         */
        deposit(5),
        /**
         * 第一次合同款
         */
        FirstContract(31),
        /**
         * 第二次合同款
         */
        SecondContract(32),
        /**
         * 第三次合同款
         */
        ThirdContract(33),
        /**
         * 第一次施工款
         */
        FirstConstruction(21),
        /**
         * 第二次施工款
         */
        SecondConstruction(22),
        /**
         * 第三次施工款
         */
        ThirdConstruction(23);
        public Integer code;
        SubType(Integer code) {
            this.code = code;
        }
    }


}
