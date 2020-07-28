package com.thundersharp.cadmin.core.globalmodels;

import java.io.Serializable;

public class org_details_model implements Serializable {

    public String company_description;
    public String company_logo;
    public String logo_ref;
    public String organisation_id;
    public String organisation_name;
    public String organiser_name;
    public String organiser_uid;

    public org_details_model() {
    }

    public org_details_model(String company_description, String company_logo, String logo_ref, String organisation_id, String organisation_name, String organiser_name, String organiser_uid) {
        this.company_description = company_description;
        this.company_logo = company_logo;
        this.logo_ref = logo_ref;
        this.organisation_id = organisation_id;
        this.organisation_name = organisation_name;
        this.organiser_name = organiser_name;
        this.organiser_uid = organiser_uid;
    }

    public String getCompany_description() {
        return company_description;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public String getLogo_ref() {
        return logo_ref;
    }

    public String getOrganisation_id() {
        return organisation_id;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public String getOrganiser_name() {
        return organiser_name;
    }

    public String getOrganiser_uid() {
        return organiser_uid;
    }

    /**
     *  @Override
     *     public int describeContents() {
     *         return 0;
     *     }
     *
     *     @Override
     *     public void writeToParcel(Parcel parcel, int i) {
     *
     *     }
     * @return
     */

}
