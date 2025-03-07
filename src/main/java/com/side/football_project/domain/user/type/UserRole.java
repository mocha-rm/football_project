package com.side.football_project.domain.user.type;

public enum UserRole {
    ADMIN,
    VENDOR,
    NORMAL;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
