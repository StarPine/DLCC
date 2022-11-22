package com.fine.friendlycc.bean;

/**
 * Author: 彭石林
 * Time: 2022/1/14 19:51
 * Description: This is RoleInfoBean
 */
public class RoleInfoBean {
    private String serverId;
    private String roleId;
    private String roleName;
    private String avatarUrl;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}