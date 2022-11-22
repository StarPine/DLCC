package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/10/22 16:54
 * Description: This is MessageRuleBean
 */
public class MessageRuleBean {
    private Integer type;
    @SerializedName("rule_type")
    private Integer ruleType;
    @SerializedName("rule_value")
    private Integer ruleValue;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(Integer ruleValue) {
        this.ruleValue = ruleValue;
    }

    @Override
    public String toString() {
        return "MessageRuleBean{" +
                "type=" + type +
                ", ruleType=" + ruleType +
                ", ruleValue=" + ruleValue +
                '}';
    }
}