package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvaluateConfigEntity {
    @SerializedName("evaluate_female")
    private List<EvaluateObjEntity> evaluateFemale;
    @SerializedName("evaluate_male")
    private List<EvaluateObjEntity> evaluateMale;

    public List<EvaluateObjEntity> getEvaluateFemale() {
        return evaluateFemale;
    }

    public void setEvaluateFemale(List<EvaluateObjEntity> evaluateFemale) {
        this.evaluateFemale = evaluateFemale;
    }

    public List<EvaluateObjEntity> getEvaluateMale() {
        return evaluateMale;
    }

    public void setEvaluateMale(List<EvaluateObjEntity> evaluateMale) {
        this.evaluateMale = evaluateMale;
    }
}
