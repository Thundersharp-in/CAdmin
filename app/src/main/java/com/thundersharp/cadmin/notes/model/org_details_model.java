package com.thundersharp.cadmin.notes.model;

import java.io.Serializable;

public class org_details_model implements Serializable {

    String company_description;
    String company_logo;
    String organisation_id;
    String organisation_name;
    String organiser_name;
    String organiser_uid;

    public org_details_model() {
    }

    public org_details_model(String company_description, String company_logo, String organisation_id, String organisation_name, String organiser_name, String organiser_uid) {
        this.company_description = company_description;
        this.company_logo = company_logo;
        this.organisation_id = organisation_id;
        this.organisation_name = organisation_name;
        this.organiser_name = organiser_name;
        this.organiser_uid = organiser_uid;
    }

    public String getCompany_description() {
        return company_description;
    }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getOrganisation_id() {
        return organisation_id;
    }

    public void setOrganisation_id(String organisation_id) {
        this.organisation_id = organisation_id;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public void setOrganisation_name(String organisation_name) {
        this.organisation_name = organisation_name;
    }

    public String getOrganiser_name() {
        return organiser_name;
    }

    public void setOrganiser_name(String organiser_name) {
        this.organiser_name = organiser_name;
    }

    public String getOrganiser_uid() {
        return organiser_uid;
    }

    public void setOrganiser_uid(String organiser_uid) {
        this.organiser_uid = organiser_uid;
    }
}
