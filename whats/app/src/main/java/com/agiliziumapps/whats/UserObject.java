package com.agiliziumapps.whats;

public class UserObject {
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    private String name, phone;

    public UserObject(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
