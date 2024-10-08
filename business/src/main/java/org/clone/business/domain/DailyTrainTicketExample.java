package org.clone.business.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DailyTrainTicketExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DailyTrainTicketExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("`date` is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("`date` is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Date value) {
            addCriterionForJDBCDate("`date` =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Date value) {
            addCriterionForJDBCDate("`date` >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Date value) {
            addCriterionForJDBCDate("`date` <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("`date` <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Date> values) {
            addCriterionForJDBCDate("`date` in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("`date` not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("`date` not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andTrainCodeIsNull() {
            addCriterion("train_code is null");
            return (Criteria) this;
        }

        public Criteria andTrainCodeIsNotNull() {
            addCriterion("train_code is not null");
            return (Criteria) this;
        }

        public Criteria andTrainCodeEqualTo(String value) {
            addCriterion("train_code =", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeNotEqualTo(String value) {
            addCriterion("train_code <>", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeGreaterThan(String value) {
            addCriterion("train_code >", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeGreaterThanOrEqualTo(String value) {
            addCriterion("train_code >=", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeLessThan(String value) {
            addCriterion("train_code <", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeLessThanOrEqualTo(String value) {
            addCriterion("train_code <=", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeLike(String value) {
            addCriterion("train_code like", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeNotLike(String value) {
            addCriterion("train_code not like", value, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeIn(List<String> values) {
            addCriterion("train_code in", values, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeNotIn(List<String> values) {
            addCriterion("train_code not in", values, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeBetween(String value1, String value2) {
            addCriterion("train_code between", value1, value2, "trainCode");
            return (Criteria) this;
        }

        public Criteria andTrainCodeNotBetween(String value1, String value2) {
            addCriterion("train_code not between", value1, value2, "trainCode");
            return (Criteria) this;
        }

        public Criteria andStartIsNull() {
            addCriterion("`start` is null");
            return (Criteria) this;
        }

        public Criteria andStartIsNotNull() {
            addCriterion("`start` is not null");
            return (Criteria) this;
        }

        public Criteria andStartEqualTo(String value) {
            addCriterion("`start` =", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotEqualTo(String value) {
            addCriterion("`start` <>", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartGreaterThan(String value) {
            addCriterion("`start` >", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartGreaterThanOrEqualTo(String value) {
            addCriterion("`start` >=", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLessThan(String value) {
            addCriterion("`start` <", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLessThanOrEqualTo(String value) {
            addCriterion("`start` <=", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartLike(String value) {
            addCriterion("`start` like", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotLike(String value) {
            addCriterion("`start` not like", value, "start");
            return (Criteria) this;
        }

        public Criteria andStartIn(List<String> values) {
            addCriterion("`start` in", values, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotIn(List<String> values) {
            addCriterion("`start` not in", values, "start");
            return (Criteria) this;
        }

        public Criteria andStartBetween(String value1, String value2) {
            addCriterion("`start` between", value1, value2, "start");
            return (Criteria) this;
        }

        public Criteria andStartNotBetween(String value1, String value2) {
            addCriterion("`start` not between", value1, value2, "start");
            return (Criteria) this;
        }

        public Criteria andStartPinyinIsNull() {
            addCriterion("start_pinyin is null");
            return (Criteria) this;
        }

        public Criteria andStartPinyinIsNotNull() {
            addCriterion("start_pinyin is not null");
            return (Criteria) this;
        }

        public Criteria andStartPinyinEqualTo(String value) {
            addCriterion("start_pinyin =", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinNotEqualTo(String value) {
            addCriterion("start_pinyin <>", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinGreaterThan(String value) {
            addCriterion("start_pinyin >", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinGreaterThanOrEqualTo(String value) {
            addCriterion("start_pinyin >=", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinLessThan(String value) {
            addCriterion("start_pinyin <", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinLessThanOrEqualTo(String value) {
            addCriterion("start_pinyin <=", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinLike(String value) {
            addCriterion("start_pinyin like", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinNotLike(String value) {
            addCriterion("start_pinyin not like", value, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinIn(List<String> values) {
            addCriterion("start_pinyin in", values, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinNotIn(List<String> values) {
            addCriterion("start_pinyin not in", values, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinBetween(String value1, String value2) {
            addCriterion("start_pinyin between", value1, value2, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartPinyinNotBetween(String value1, String value2) {
            addCriterion("start_pinyin not between", value1, value2, "startPinyin");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterionForJDBCTime("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterionForJDBCTime("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterionForJDBCTime("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartIndexIsNull() {
            addCriterion("start_index is null");
            return (Criteria) this;
        }

        public Criteria andStartIndexIsNotNull() {
            addCriterion("start_index is not null");
            return (Criteria) this;
        }

        public Criteria andStartIndexEqualTo(Integer value) {
            addCriterion("start_index =", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexNotEqualTo(Integer value) {
            addCriterion("start_index <>", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexGreaterThan(Integer value) {
            addCriterion("start_index >", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("start_index >=", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexLessThan(Integer value) {
            addCriterion("start_index <", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexLessThanOrEqualTo(Integer value) {
            addCriterion("start_index <=", value, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexIn(List<Integer> values) {
            addCriterion("start_index in", values, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexNotIn(List<Integer> values) {
            addCriterion("start_index not in", values, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexBetween(Integer value1, Integer value2) {
            addCriterion("start_index between", value1, value2, "startIndex");
            return (Criteria) this;
        }

        public Criteria andStartIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("start_index not between", value1, value2, "startIndex");
            return (Criteria) this;
        }

        public Criteria andEndIsNull() {
            addCriterion("`end` is null");
            return (Criteria) this;
        }

        public Criteria andEndIsNotNull() {
            addCriterion("`end` is not null");
            return (Criteria) this;
        }

        public Criteria andEndEqualTo(String value) {
            addCriterion("`end` =", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndNotEqualTo(String value) {
            addCriterion("`end` <>", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndGreaterThan(String value) {
            addCriterion("`end` >", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndGreaterThanOrEqualTo(String value) {
            addCriterion("`end` >=", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndLessThan(String value) {
            addCriterion("`end` <", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndLessThanOrEqualTo(String value) {
            addCriterion("`end` <=", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndLike(String value) {
            addCriterion("`end` like", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndNotLike(String value) {
            addCriterion("`end` not like", value, "end");
            return (Criteria) this;
        }

        public Criteria andEndIn(List<String> values) {
            addCriterion("`end` in", values, "end");
            return (Criteria) this;
        }

        public Criteria andEndNotIn(List<String> values) {
            addCriterion("`end` not in", values, "end");
            return (Criteria) this;
        }

        public Criteria andEndBetween(String value1, String value2) {
            addCriterion("`end` between", value1, value2, "end");
            return (Criteria) this;
        }

        public Criteria andEndNotBetween(String value1, String value2) {
            addCriterion("`end` not between", value1, value2, "end");
            return (Criteria) this;
        }

        public Criteria andEndPinyinIsNull() {
            addCriterion("end_pinyin is null");
            return (Criteria) this;
        }

        public Criteria andEndPinyinIsNotNull() {
            addCriterion("end_pinyin is not null");
            return (Criteria) this;
        }

        public Criteria andEndPinyinEqualTo(String value) {
            addCriterion("end_pinyin =", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinNotEqualTo(String value) {
            addCriterion("end_pinyin <>", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinGreaterThan(String value) {
            addCriterion("end_pinyin >", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinGreaterThanOrEqualTo(String value) {
            addCriterion("end_pinyin >=", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinLessThan(String value) {
            addCriterion("end_pinyin <", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinLessThanOrEqualTo(String value) {
            addCriterion("end_pinyin <=", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinLike(String value) {
            addCriterion("end_pinyin like", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinNotLike(String value) {
            addCriterion("end_pinyin not like", value, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinIn(List<String> values) {
            addCriterion("end_pinyin in", values, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinNotIn(List<String> values) {
            addCriterion("end_pinyin not in", values, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinBetween(String value1, String value2) {
            addCriterion("end_pinyin between", value1, value2, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndPinyinNotBetween(String value1, String value2) {
            addCriterion("end_pinyin not between", value1, value2, "endPinyin");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterionForJDBCTime("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterionForJDBCTime("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterionForJDBCTime("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndIndexIsNull() {
            addCriterion("end_index is null");
            return (Criteria) this;
        }

        public Criteria andEndIndexIsNotNull() {
            addCriterion("end_index is not null");
            return (Criteria) this;
        }

        public Criteria andEndIndexEqualTo(Integer value) {
            addCriterion("end_index =", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexNotEqualTo(Integer value) {
            addCriterion("end_index <>", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexGreaterThan(Integer value) {
            addCriterion("end_index >", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("end_index >=", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexLessThan(Integer value) {
            addCriterion("end_index <", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexLessThanOrEqualTo(Integer value) {
            addCriterion("end_index <=", value, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexIn(List<Integer> values) {
            addCriterion("end_index in", values, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexNotIn(List<Integer> values) {
            addCriterion("end_index not in", values, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexBetween(Integer value1, Integer value2) {
            addCriterion("end_index between", value1, value2, "endIndex");
            return (Criteria) this;
        }

        public Criteria andEndIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("end_index not between", value1, value2, "endIndex");
            return (Criteria) this;
        }

        public Criteria andFcIsNull() {
            addCriterion("fc is null");
            return (Criteria) this;
        }

        public Criteria andFcIsNotNull() {
            addCriterion("fc is not null");
            return (Criteria) this;
        }

        public Criteria andFcEqualTo(Integer value) {
            addCriterion("fc =", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcNotEqualTo(Integer value) {
            addCriterion("fc <>", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcGreaterThan(Integer value) {
            addCriterion("fc >", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcGreaterThanOrEqualTo(Integer value) {
            addCriterion("fc >=", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcLessThan(Integer value) {
            addCriterion("fc <", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcLessThanOrEqualTo(Integer value) {
            addCriterion("fc <=", value, "fc");
            return (Criteria) this;
        }

        public Criteria andFcIn(List<Integer> values) {
            addCriterion("fc in", values, "fc");
            return (Criteria) this;
        }

        public Criteria andFcNotIn(List<Integer> values) {
            addCriterion("fc not in", values, "fc");
            return (Criteria) this;
        }

        public Criteria andFcBetween(Integer value1, Integer value2) {
            addCriterion("fc between", value1, value2, "fc");
            return (Criteria) this;
        }

        public Criteria andFcNotBetween(Integer value1, Integer value2) {
            addCriterion("fc not between", value1, value2, "fc");
            return (Criteria) this;
        }

        public Criteria andFcPriceIsNull() {
            addCriterion("fc_price is null");
            return (Criteria) this;
        }

        public Criteria andFcPriceIsNotNull() {
            addCriterion("fc_price is not null");
            return (Criteria) this;
        }

        public Criteria andFcPriceEqualTo(BigDecimal value) {
            addCriterion("fc_price =", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceNotEqualTo(BigDecimal value) {
            addCriterion("fc_price <>", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceGreaterThan(BigDecimal value) {
            addCriterion("fc_price >", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fc_price >=", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceLessThan(BigDecimal value) {
            addCriterion("fc_price <", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fc_price <=", value, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceIn(List<BigDecimal> values) {
            addCriterion("fc_price in", values, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceNotIn(List<BigDecimal> values) {
            addCriterion("fc_price not in", values, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fc_price between", value1, value2, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andFcPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fc_price not between", value1, value2, "fcPrice");
            return (Criteria) this;
        }

        public Criteria andScIsNull() {
            addCriterion("sc is null");
            return (Criteria) this;
        }

        public Criteria andScIsNotNull() {
            addCriterion("sc is not null");
            return (Criteria) this;
        }

        public Criteria andScEqualTo(Integer value) {
            addCriterion("sc =", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScNotEqualTo(Integer value) {
            addCriterion("sc <>", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScGreaterThan(Integer value) {
            addCriterion("sc >", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScGreaterThanOrEqualTo(Integer value) {
            addCriterion("sc >=", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScLessThan(Integer value) {
            addCriterion("sc <", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScLessThanOrEqualTo(Integer value) {
            addCriterion("sc <=", value, "sc");
            return (Criteria) this;
        }

        public Criteria andScIn(List<Integer> values) {
            addCriterion("sc in", values, "sc");
            return (Criteria) this;
        }

        public Criteria andScNotIn(List<Integer> values) {
            addCriterion("sc not in", values, "sc");
            return (Criteria) this;
        }

        public Criteria andScBetween(Integer value1, Integer value2) {
            addCriterion("sc between", value1, value2, "sc");
            return (Criteria) this;
        }

        public Criteria andScNotBetween(Integer value1, Integer value2) {
            addCriterion("sc not between", value1, value2, "sc");
            return (Criteria) this;
        }

        public Criteria andScPriceIsNull() {
            addCriterion("sc_price is null");
            return (Criteria) this;
        }

        public Criteria andScPriceIsNotNull() {
            addCriterion("sc_price is not null");
            return (Criteria) this;
        }

        public Criteria andScPriceEqualTo(BigDecimal value) {
            addCriterion("sc_price =", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceNotEqualTo(BigDecimal value) {
            addCriterion("sc_price <>", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceGreaterThan(BigDecimal value) {
            addCriterion("sc_price >", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sc_price >=", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceLessThan(BigDecimal value) {
            addCriterion("sc_price <", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sc_price <=", value, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceIn(List<BigDecimal> values) {
            addCriterion("sc_price in", values, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceNotIn(List<BigDecimal> values) {
            addCriterion("sc_price not in", values, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sc_price between", value1, value2, "scPrice");
            return (Criteria) this;
        }

        public Criteria andScPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sc_price not between", value1, value2, "scPrice");
            return (Criteria) this;
        }

        public Criteria andSsIsNull() {
            addCriterion("ss is null");
            return (Criteria) this;
        }

        public Criteria andSsIsNotNull() {
            addCriterion("ss is not null");
            return (Criteria) this;
        }

        public Criteria andSsEqualTo(Integer value) {
            addCriterion("ss =", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsNotEqualTo(Integer value) {
            addCriterion("ss <>", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsGreaterThan(Integer value) {
            addCriterion("ss >", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsGreaterThanOrEqualTo(Integer value) {
            addCriterion("ss >=", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsLessThan(Integer value) {
            addCriterion("ss <", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsLessThanOrEqualTo(Integer value) {
            addCriterion("ss <=", value, "ss");
            return (Criteria) this;
        }

        public Criteria andSsIn(List<Integer> values) {
            addCriterion("ss in", values, "ss");
            return (Criteria) this;
        }

        public Criteria andSsNotIn(List<Integer> values) {
            addCriterion("ss not in", values, "ss");
            return (Criteria) this;
        }

        public Criteria andSsBetween(Integer value1, Integer value2) {
            addCriterion("ss between", value1, value2, "ss");
            return (Criteria) this;
        }

        public Criteria andSsNotBetween(Integer value1, Integer value2) {
            addCriterion("ss not between", value1, value2, "ss");
            return (Criteria) this;
        }

        public Criteria andSsPriceIsNull() {
            addCriterion("ss_price is null");
            return (Criteria) this;
        }

        public Criteria andSsPriceIsNotNull() {
            addCriterion("ss_price is not null");
            return (Criteria) this;
        }

        public Criteria andSsPriceEqualTo(BigDecimal value) {
            addCriterion("ss_price =", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceNotEqualTo(BigDecimal value) {
            addCriterion("ss_price <>", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceGreaterThan(BigDecimal value) {
            addCriterion("ss_price >", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ss_price >=", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceLessThan(BigDecimal value) {
            addCriterion("ss_price <", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ss_price <=", value, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceIn(List<BigDecimal> values) {
            addCriterion("ss_price in", values, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceNotIn(List<BigDecimal> values) {
            addCriterion("ss_price not in", values, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ss_price between", value1, value2, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andSsPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ss_price not between", value1, value2, "ssPrice");
            return (Criteria) this;
        }

        public Criteria andHsIsNull() {
            addCriterion("hs is null");
            return (Criteria) this;
        }

        public Criteria andHsIsNotNull() {
            addCriterion("hs is not null");
            return (Criteria) this;
        }

        public Criteria andHsEqualTo(Integer value) {
            addCriterion("hs =", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsNotEqualTo(Integer value) {
            addCriterion("hs <>", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsGreaterThan(Integer value) {
            addCriterion("hs >", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsGreaterThanOrEqualTo(Integer value) {
            addCriterion("hs >=", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsLessThan(Integer value) {
            addCriterion("hs <", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsLessThanOrEqualTo(Integer value) {
            addCriterion("hs <=", value, "hs");
            return (Criteria) this;
        }

        public Criteria andHsIn(List<Integer> values) {
            addCriterion("hs in", values, "hs");
            return (Criteria) this;
        }

        public Criteria andHsNotIn(List<Integer> values) {
            addCriterion("hs not in", values, "hs");
            return (Criteria) this;
        }

        public Criteria andHsBetween(Integer value1, Integer value2) {
            addCriterion("hs between", value1, value2, "hs");
            return (Criteria) this;
        }

        public Criteria andHsNotBetween(Integer value1, Integer value2) {
            addCriterion("hs not between", value1, value2, "hs");
            return (Criteria) this;
        }

        public Criteria andHsPriceIsNull() {
            addCriterion("hs_price is null");
            return (Criteria) this;
        }

        public Criteria andHsPriceIsNotNull() {
            addCriterion("hs_price is not null");
            return (Criteria) this;
        }

        public Criteria andHsPriceEqualTo(BigDecimal value) {
            addCriterion("hs_price =", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceNotEqualTo(BigDecimal value) {
            addCriterion("hs_price <>", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceGreaterThan(BigDecimal value) {
            addCriterion("hs_price >", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hs_price >=", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceLessThan(BigDecimal value) {
            addCriterion("hs_price <", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hs_price <=", value, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceIn(List<BigDecimal> values) {
            addCriterion("hs_price in", values, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceNotIn(List<BigDecimal> values) {
            addCriterion("hs_price not in", values, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hs_price between", value1, value2, "hsPrice");
            return (Criteria) this;
        }

        public Criteria andHsPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hs_price not between", value1, value2, "hsPrice");
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

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

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