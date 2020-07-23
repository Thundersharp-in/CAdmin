package com.thundersharp.cadmin.core.globalmodels;

public class org_details_model {

    public org_details_model(){ }

    public String company_description,company_logo,org_manager_uid,organisation_id,organisation_name,organiser_name;

    public org_details_model(String company_description, String company_logo, String org_manager_uid, String organisation_id, String organisation_name, String organiser_name) {
        this.company_description = company_description;
        this.company_logo = company_logo;
        this.org_manager_uid = org_manager_uid;
        this.organisation_id = organisation_id;
        this.organisation_name = organisation_name;
        this.organiser_name = organiser_name;
    }

    public String getCompany_description() {
        return company_description;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public String getOrg_manager_uid() {
        return org_manager_uid;
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
}
