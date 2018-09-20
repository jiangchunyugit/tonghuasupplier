package cn.thinkfree.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PcContractTemplateExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public PcContractTemplateExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeIsNull() {
            addCriterion("contract_tp_type is null");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeIsNotNull() {
            addCriterion("contract_tp_type is not null");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeEqualTo(String value) {
            addCriterion("contract_tp_type =", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeNotEqualTo(String value) {
            addCriterion("contract_tp_type <>", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeGreaterThan(String value) {
            addCriterion("contract_tp_type >", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeGreaterThanOrEqualTo(String value) {
            addCriterion("contract_tp_type >=", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeLessThan(String value) {
            addCriterion("contract_tp_type <", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeLessThanOrEqualTo(String value) {
            addCriterion("contract_tp_type <=", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeLike(String value) {
            addCriterion("contract_tp_type like", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeNotLike(String value) {
            addCriterion("contract_tp_type not like", value, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeIn(List<String> values) {
            addCriterion("contract_tp_type in", values, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeNotIn(List<String> values) {
            addCriterion("contract_tp_type not in", values, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeBetween(String value1, String value2) {
            addCriterion("contract_tp_type between", value1, value2, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpTypeNotBetween(String value1, String value2) {
            addCriterion("contract_tp_type not between", value1, value2, "contractTpType");
            return (Criteria) this;
        }

        public Criteria andContractTpNameIsNull() {
            addCriterion("contract_tp_name is null");
            return (Criteria) this;
        }

        public Criteria andContractTpNameIsNotNull() {
            addCriterion("contract_tp_name is not null");
            return (Criteria) this;
        }

        public Criteria andContractTpNameEqualTo(String value) {
            addCriterion("contract_tp_name =", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameNotEqualTo(String value) {
            addCriterion("contract_tp_name <>", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameGreaterThan(String value) {
            addCriterion("contract_tp_name >", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameGreaterThanOrEqualTo(String value) {
            addCriterion("contract_tp_name >=", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameLessThan(String value) {
            addCriterion("contract_tp_name <", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameLessThanOrEqualTo(String value) {
            addCriterion("contract_tp_name <=", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameLike(String value) {
            addCriterion("contract_tp_name like", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameNotLike(String value) {
            addCriterion("contract_tp_name not like", value, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameIn(List<String> values) {
            addCriterion("contract_tp_name in", values, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameNotIn(List<String> values) {
            addCriterion("contract_tp_name not in", values, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameBetween(String value1, String value2) {
            addCriterion("contract_tp_name between", value1, value2, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpNameNotBetween(String value1, String value2) {
            addCriterion("contract_tp_name not between", value1, value2, "contractTpName");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkIsNull() {
            addCriterion("contract_tp_remark is null");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkIsNotNull() {
            addCriterion("contract_tp_remark is not null");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkEqualTo(String value) {
            addCriterion("contract_tp_remark =", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkNotEqualTo(String value) {
            addCriterion("contract_tp_remark <>", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkGreaterThan(String value) {
            addCriterion("contract_tp_remark >", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("contract_tp_remark >=", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkLessThan(String value) {
            addCriterion("contract_tp_remark <", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkLessThanOrEqualTo(String value) {
            addCriterion("contract_tp_remark <=", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkLike(String value) {
            addCriterion("contract_tp_remark like", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkNotLike(String value) {
            addCriterion("contract_tp_remark not like", value, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkIn(List<String> values) {
            addCriterion("contract_tp_remark in", values, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkNotIn(List<String> values) {
            addCriterion("contract_tp_remark not in", values, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkBetween(String value1, String value2) {
            addCriterion("contract_tp_remark between", value1, value2, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andContractTpRemarkNotBetween(String value1, String value2) {
            addCriterion("contract_tp_remark not between", value1, value2, "contractTpRemark");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIsNull() {
            addCriterion("\"upload_time \" is null");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIsNotNull() {
            addCriterion("\"upload_time \" is not null");
            return (Criteria) this;
        }

        public Criteria andUploadTimeEqualTo(Date value) {
            addCriterion("\"upload_time \" =", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotEqualTo(Date value) {
            addCriterion("\"upload_time \" <>", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeGreaterThan(Date value) {
            addCriterion("\"upload_time \" >", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("\"upload_time \" >=", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeLessThan(Date value) {
            addCriterion("\"upload_time \" <", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeLessThanOrEqualTo(Date value) {
            addCriterion("\"upload_time \" <=", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIn(List<Date> values) {
            addCriterion("\"upload_time \" in", values, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotIn(List<Date> values) {
            addCriterion("\"upload_time \" not in", values, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeBetween(Date value1, Date value2) {
            addCriterion("\"upload_time \" between", value1, value2, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotBetween(Date value1, Date value2) {
            addCriterion("\"upload_time \" not between", value1, value2, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadUrlIsNull() {
            addCriterion("upload_url is null");
            return (Criteria) this;
        }

        public Criteria andUploadUrlIsNotNull() {
            addCriterion("upload_url is not null");
            return (Criteria) this;
        }

        public Criteria andUploadUrlEqualTo(String value) {
            addCriterion("upload_url =", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlNotEqualTo(String value) {
            addCriterion("upload_url <>", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlGreaterThan(String value) {
            addCriterion("upload_url >", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlGreaterThanOrEqualTo(String value) {
            addCriterion("upload_url >=", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlLessThan(String value) {
            addCriterion("upload_url <", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlLessThanOrEqualTo(String value) {
            addCriterion("upload_url <=", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlLike(String value) {
            addCriterion("upload_url like", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlNotLike(String value) {
            addCriterion("upload_url not like", value, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlIn(List<String> values) {
            addCriterion("upload_url in", values, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlNotIn(List<String> values) {
            addCriterion("upload_url not in", values, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlBetween(String value1, String value2) {
            addCriterion("upload_url between", value1, value2, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andUploadUrlNotBetween(String value1, String value2) {
            addCriterion("upload_url not between", value1, value2, "uploadUrl");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table pc_contract_template
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table pc_contract_template
     *
     * @mbg.generated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}