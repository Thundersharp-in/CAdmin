package com.thundersharp.cadmin.core.globalmodels;

import java.io.Serializable;

public class Organisations implements Serializable {     // Parcelable
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


    /**
     *  @Override
     *     public int describeContents() {
     *         return 0;
     *     }
     *
     *     @Override
     *     public void writeToParcel(Parcel dest, int flags) {
     *
     *     }
     * @return
     */

}
