package com.qlst.model;

public enum UserRole {
    ADMIN,
    CUSTOMER;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isCustomer() {
        return this == CUSTOMER;
    }
}
