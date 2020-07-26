package com.thundersharp.cadmin.core.globalmodels;

public class Organisations {
    public Organisations(){

    }

    public String organisationKey;
    public boolean manager;

    public Organisations(String organisationKey, boolean manager) {
        this.organisationKey = organisationKey;
        this.manager = manager;
    }

    public String getOrganisationKey() {
        return organisationKey;
    }

    public boolean isManager() {
        return manager;
    }
}
