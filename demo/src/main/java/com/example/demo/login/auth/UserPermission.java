package com.example.demo.login.auth;

public enum UserPermission {
    PRODUCT("product");
    private final String permission;

    UserPermission(String permissions) {
        this.permission = permissions;
    }

    public String getPermission() {
        return permission;
    }
}
