package com.thundersharp.cadmin.core.globalmodels;

public class ManagerModels {
    public String useruid;
    public String info;

    public ManagerModels() {
    }

    public ManagerModels(String useruid, String info) {
        this.useruid = useruid;
        this.info = info;
    }

    public String getUseruid() {
        return useruid;
    }

    public String getInfo() {
        return info;
    }
}
