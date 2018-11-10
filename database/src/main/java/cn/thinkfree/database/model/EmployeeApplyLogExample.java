package cn.thinkfree.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeApplyLogExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public EmployeeApplyLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
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
     * This method corresponds to the database table employee_apply_log
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
     * This method corresponds to the database table employee_apply_log
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table employee_apply_log
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
     * This class corresponds to the database table employee_apply_log
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNull() {
            addCriterion("apply_time is null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNotNull() {
            addCriterion("apply_time is not null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeEqualTo(Date value) {
            addCriterion("apply_time =", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotEqualTo(Date value) {
            addCriterion("apply_time <>", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThan(Date value) {
            addCriterion("apply_time >", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("apply_time >=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThan(Date value) {
            addCriterion("apply_time <", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThanOrEqualTo(Date value) {
            addCriterion("apply_time <=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIn(List<Date> values) {
            addCriterion("apply_time in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotIn(List<Date> values) {
            addCriterion("apply_time not in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeBetween(Date value1, Date value2) {
            addCriterion("apply_time between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotBetween(Date value1, Date value2) {
            addCriterion("apply_time not between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andDealStateIsNull() {
            addCriterion("deal_state is null");
            return (Criteria) this;
        }

        public Criteria andDealStateIsNotNull() {
            addCriterion("deal_state is not null");
            return (Criteria) this;
        }

        public Criteria andDealStateEqualTo(Integer value) {
            addCriterion("deal_state =", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateNotEqualTo(Integer value) {
            addCriterion("deal_state <>", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateGreaterThan(Integer value) {
            addCriterion("deal_state >", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("deal_state >=", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateLessThan(Integer value) {
            addCriterion("deal_state <", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateLessThanOrEqualTo(Integer value) {
            addCriterion("deal_state <=", value, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateIn(List<Integer> values) {
            addCriterion("deal_state in", values, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateNotIn(List<Integer> values) {
            addCriterion("deal_state not in", values, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateBetween(Integer value1, Integer value2) {
            addCriterion("deal_state between", value1, value2, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealStateNotBetween(Integer value1, Integer value2) {
            addCriterion("deal_state not between", value1, value2, "dealState");
            return (Criteria) this;
        }

        public Criteria andDealTimeIsNull() {
            addCriterion("deal_time is null");
            return (Criteria) this;
        }

        public Criteria andDealTimeIsNotNull() {
            addCriterion("deal_time is not null");
            return (Criteria) this;
        }

        public Criteria andDealTimeEqualTo(Date value) {
            addCriterion("deal_time =", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotEqualTo(Date value) {
            addCriterion("deal_time <>", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeGreaterThan(Date value) {
            addCriterion("deal_time >", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("deal_time >=", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeLessThan(Date value) {
            addCriterion("deal_time <", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeLessThanOrEqualTo(Date value) {
            addCriterion("deal_time <=", value, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeIn(List<Date> values) {
            addCriterion("deal_time in", values, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotIn(List<Date> values) {
            addCriterion("deal_time not in", values, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeBetween(Date value1, Date value2) {
            addCriterion("deal_time between", value1, value2, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealTimeNotBetween(Date value1, Date value2) {
            addCriterion("deal_time not between", value1, value2, "dealTime");
            return (Criteria) this;
        }

        public Criteria andDealExplainIsNull() {
            addCriterion("deal_explain is null");
            return (Criteria) this;
        }

        public Criteria andDealExplainIsNotNull() {
            addCriterion("deal_explain is not null");
            return (Criteria) this;
        }

        public Criteria andDealExplainEqualTo(String value) {
            addCriterion("deal_explain =", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainNotEqualTo(String value) {
            addCriterion("deal_explain <>", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainGreaterThan(String value) {
            addCriterion("deal_explain >", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainGreaterThanOrEqualTo(String value) {
            addCriterion("deal_explain >=", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainLessThan(String value) {
            addCriterion("deal_explain <", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainLessThanOrEqualTo(String value) {
            addCriterion("deal_explain <=", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainLike(String value) {
            addCriterion("deal_explain like", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainNotLike(String value) {
            addCriterion("deal_explain not like", value, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainIn(List<String> values) {
            addCriterion("deal_explain in", values, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainNotIn(List<String> values) {
            addCriterion("deal_explain not in", values, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainBetween(String value1, String value2) {
            addCriterion("deal_explain between", value1, value2, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealExplainNotBetween(String value1, String value2) {
            addCriterion("deal_explain not between", value1, value2, "dealExplain");
            return (Criteria) this;
        }

        public Criteria andDealUserIdIsNull() {
            addCriterion("deal_user_id is null");
            return (Criteria) this;
        }

        public Criteria andDealUserIdIsNotNull() {
            addCriterion("deal_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andDealUserIdEqualTo(String value) {
            addCriterion("deal_user_id =", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdNotEqualTo(String value) {
            addCriterion("deal_user_id <>", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdGreaterThan(String value) {
            addCriterion("deal_user_id >", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("deal_user_id >=", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdLessThan(String value) {
            addCriterion("deal_user_id <", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdLessThanOrEqualTo(String value) {
            addCriterion("deal_user_id <=", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdLike(String value) {
            addCriterion("deal_user_id like", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdNotLike(String value) {
            addCriterion("deal_user_id not like", value, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdIn(List<String> values) {
            addCriterion("deal_user_id in", values, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdNotIn(List<String> values) {
            addCriterion("deal_user_id not in", values, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdBetween(String value1, String value2) {
            addCriterion("deal_user_id between", value1, value2, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andDealUserIdNotBetween(String value1, String value2) {
            addCriterion("deal_user_id not between", value1, value2, "dealUserId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNull() {
            addCriterion("company_id is null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIsNotNull() {
            addCriterion("company_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompanyIdEqualTo(String value) {
            addCriterion("company_id =", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotEqualTo(String value) {
            addCriterion("company_id <>", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThan(String value) {
            addCriterion("company_id >", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdGreaterThanOrEqualTo(String value) {
            addCriterion("company_id >=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThan(String value) {
            addCriterion("company_id <", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLessThanOrEqualTo(String value) {
            addCriterion("company_id <=", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdLike(String value) {
            addCriterion("company_id like", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotLike(String value) {
            addCriterion("company_id not like", value, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdIn(List<String> values) {
            addCriterion("company_id in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotIn(List<String> values) {
            addCriterion("company_id not in", values, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdBetween(String value1, String value2) {
            addCriterion("company_id between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andCompanyIdNotBetween(String value1, String value2) {
            addCriterion("company_id not between", value1, value2, "companyId");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeIsNull() {
            addCriterion("invalid_time is null");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeIsNotNull() {
            addCriterion("invalid_time is not null");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeEqualTo(Date value) {
            addCriterion("invalid_time =", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeNotEqualTo(Date value) {
            addCriterion("invalid_time <>", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeGreaterThan(Date value) {
            addCriterion("invalid_time >", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("invalid_time >=", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeLessThan(Date value) {
            addCriterion("invalid_time <", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeLessThanOrEqualTo(Date value) {
            addCriterion("invalid_time <=", value, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeIn(List<Date> values) {
            addCriterion("invalid_time in", values, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeNotIn(List<Date> values) {
            addCriterion("invalid_time not in", values, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeBetween(Date value1, Date value2) {
            addCriterion("invalid_time between", value1, value2, "invalidTime");
            return (Criteria) this;
        }

        public Criteria andInvalidTimeNotBetween(Date value1, Date value2) {
            addCriterion("invalid_time not between", value1, value2, "invalidTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table employee_apply_log
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
     * This class corresponds to the database table employee_apply_log
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