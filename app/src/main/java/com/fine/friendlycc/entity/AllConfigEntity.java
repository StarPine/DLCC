package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wulei
 */
public class AllConfigEntity {

    @SerializedName("program_time")
    private List<ConfigItemEntity> programTime;
    @SerializedName("hope_object")
    private List<ConfigItemEntity> hopeObject;
    @SerializedName("report_reason")
    private List<ConfigItemEntity> reportReason;
    private EvaluateConfigEntity evaluate;
    private List<OccupationConfigItemEntity> occupation;
    private List<ConfigItemEntity> city;
    private List<ConfigItemEntity> theme;
    private List<ConfigItemEntity> height;
    private List<ConfigItemEntity> weight;
    private List<GameConfigEntity> game;
    private SystemConfigEntity config;
    private CrystalDetailsConfigEntity crystalDetailsConfig;
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

    public CrystalDetailsConfigEntity getCrystalDetailsConfig() {
        return crystalDetailsConfig;
    }

    public void setCrystalDetailsConfig(CrystalDetailsConfigEntity crystalDetailsConfig) {
        this.crystalDetailsConfig = crystalDetailsConfig;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    //任务中心相关配置
    private SystemConfigTaskEntity task;

    public SystemConfigTaskEntity getTask() {
        return task;
    }

    public List<GameConfigEntity> getGame() {
        return game;
    }

    public void setGame(List<GameConfigEntity> game) {
        this.game = game;
    }

    public void setTask(SystemConfigTaskEntity task) {
        this.task = task;
    }

    public List<ConfigItemEntity> getProgramTime() {
        return programTime;
    }

    public void setProgramTime(List<ConfigItemEntity> programTime) {
        this.programTime = programTime;
    }

    public List<ConfigItemEntity> getHopeObject() {
        return hopeObject;
    }

    public void setHopeObject(List<ConfigItemEntity> hopeObject) {
        this.hopeObject = hopeObject;
    }

    public List<ConfigItemEntity> getReportReason() {
        return reportReason;
    }

    public void setReportReason(List<ConfigItemEntity> reportReason) {
        this.reportReason = reportReason;
    }

    public EvaluateConfigEntity getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(EvaluateConfigEntity evaluate) {
        this.evaluate = evaluate;
    }

    public List<OccupationConfigItemEntity> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<OccupationConfigItemEntity> occupation) {
        this.occupation = occupation;
    }

    public List<ConfigItemEntity> getCity() {
        return city;
    }

    public void setCity(List<ConfigItemEntity> city) {
        this.city = city;
    }

    public List<ConfigItemEntity> getTheme() {
        return theme;
    }

    public void setTheme(List<ConfigItemEntity> theme) {
        this.theme = theme;
    }

    public List<ConfigItemEntity> getHeight() {
        return height;
    }

    public void setHeight(List<ConfigItemEntity> height) {
        this.height = height;
    }

    public List<ConfigItemEntity> getWeight() {
        return weight;
    }

    public void setWeight(List<ConfigItemEntity> weight) {
        this.weight = weight;
    }

    public SystemConfigEntity getConfig() {
        return config;
    }

    public void setConfig(SystemConfigEntity config) {
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
        return "AllConfigEntity{" +
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
