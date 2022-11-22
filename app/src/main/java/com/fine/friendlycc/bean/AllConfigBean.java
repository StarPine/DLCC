package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wulei
 */
public class AllConfigBean {

    @SerializedName("program_time")
    private List<ConfigItemBean> programTime;
    @SerializedName("hope_object")
    private List<ConfigItemBean> hopeObject;
    @SerializedName("report_reason")
    private List<ConfigItemBean> reportReason;
    private EvaluateConfigBean evaluate;
    private List<OccupationConfigItemBean> occupation;
    private List<ConfigItemBean> city;
    private List<ConfigItemBean> theme;
    private List<ConfigItemBean> height;
    private List<ConfigItemBean> weight;
    private List<GameConfigBean> game;
    private SystemConfigBean config;
    private CrystalDetailsConfigBean crystalDetailsConfig;
    @SerializedName("default_home_page")
    private String defaultHomePage;

    @SerializedName("is_tips")
    private Integer isTips;
    //动态配置http请求域名
    @SerializedName("api_url")
    private String apiUrl;
    private int userInvite;

    public int getUserInvite() {
        return userInvite;
    }

    public void setUserInvite(int userInvite) {
        this.userInvite = userInvite;
    }

    public CrystalDetailsConfigBean getCrystalDetailsConfig() {
        return crystalDetailsConfig;
    }

    public void setCrystalDetailsConfig(CrystalDetailsConfigBean crystalDetailsConfig) {
        this.crystalDetailsConfig = crystalDetailsConfig;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    //任务中心相关配置
    private SystemConfigTaskBean task;

    public SystemConfigTaskBean getTask() {
        return task;
    }

    public List<GameConfigBean> getGame() {
        return game;
    }

    public void setGame(List<GameConfigBean> game) {
        this.game = game;
    }

    public void setTask(SystemConfigTaskBean task) {
        this.task = task;
    }

    public List<ConfigItemBean> getProgramTime() {
        return programTime;
    }

    public void setProgramTime(List<ConfigItemBean> programTime) {
        this.programTime = programTime;
    }

    public List<ConfigItemBean> getHopeObject() {
        return hopeObject;
    }

    public void setHopeObject(List<ConfigItemBean> hopeObject) {
        this.hopeObject = hopeObject;
    }

    public List<ConfigItemBean> getReportReason() {
        return reportReason;
    }

    public void setReportReason(List<ConfigItemBean> reportReason) {
        this.reportReason = reportReason;
    }

    public EvaluateConfigBean getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(EvaluateConfigBean evaluate) {
        this.evaluate = evaluate;
    }

    public List<OccupationConfigItemBean> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<OccupationConfigItemBean> occupation) {
        this.occupation = occupation;
    }

    public List<ConfigItemBean> getCity() {
        return city;
    }

    public void setCity(List<ConfigItemBean> city) {
        this.city = city;
    }

    public List<ConfigItemBean> getTheme() {
        return theme;
    }

    public void setTheme(List<ConfigItemBean> theme) {
        this.theme = theme;
    }

    public List<ConfigItemBean> getHeight() {
        return height;
    }

    public void setHeight(List<ConfigItemBean> height) {
        this.height = height;
    }

    public List<ConfigItemBean> getWeight() {
        return weight;
    }

    public void setWeight(List<ConfigItemBean> weight) {
        this.weight = weight;
    }

    public SystemConfigBean getConfig() {
        return config;
    }

    public void setConfig(SystemConfigBean config) {
        this.config = config;
    }

    public String getDefaultHomePage() {
        return defaultHomePage;
    }

    public void setDefaultHomePage(String defaultHomePage) {
        this.defaultHomePage = defaultHomePage;
    }

    public Integer getIsTips() {
        return isTips;
    }

    public void setIsTips(Integer isTips) {
        this.isTips = isTips;
    }

    @Override
    public String toString() {
        return "AllConfigBean{" +
                "programTime=" + programTime +
                ", hopeObject=" + hopeObject +
                ", reportReason=" + reportReason +
                ", evaluate=" + evaluate +
                ", occupation=" + occupation +
                ", city=" + city +
                ", theme=" + theme +
                ", height=" + height +
                ", weight=" + weight +
                ", game=" + game +
                ", config=" + config +
                ", defaultHomePage='" + defaultHomePage + '\'' +
                ", task=" + task +
                '}';
    }
}