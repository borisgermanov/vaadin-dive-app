package io.github.borisgermanov.data;

import jakarta.persistence.Entity;

@Entity
public class UserPermission extends AbstractEntity {

    private String loginName;
    private String fullName;
    private String permission;

    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }

}
