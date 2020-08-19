package com.thundersharp.cadmin.core.globalmodels;

import java.io.Serializable;

public class Organisations  {     // Parcelable  implements Serializable

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
