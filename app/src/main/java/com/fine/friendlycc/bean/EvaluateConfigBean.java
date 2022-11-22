package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvaluateConfigBean {
    @SerializedName("evaluate_female")
    private List<EvaluateObjBean> evaluateFemale;
    @SerializedName("evaluate_male")
    private List<EvaluateObjBean> evaluateMale;

    public List<EvaluateObjBean> getEvaluateFemale() {
        return evaluateFemale;
    }

    public void setEvaluateFemale(List<EvaluateObjBean> evaluateFemale) {
        this.evaluateFemale = evaluateFemale;
    }

    public List<EvaluateObjBean> getEvaluateMale() {
        return evaluateMale;
    }

    public void setEvaluateMale(List<EvaluateObjBean> evaluateMale) {
        this.evaluateMale = evaluateMale;
    }
}