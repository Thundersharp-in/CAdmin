package com.thundersharp.cadmin.core.globalmodels;

public class AddProjectModel {
    String project_name;
    String describe;
    String project_key;
    String organisation_id;
    String status;


    public AddProjectModel() {
    }

    public AddProjectModel(String project_name, String describe, String project_key, String organisation_id, String status) {
        this.project_name = project_name;
        this.describe = describe;
        this.project_key = project_key;
        this.organisation_id = organisation_id;
        this.status = status;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getProject_key() {
        return project_key;
    }

    public void setProject_key(String project_key) {
        this.project_key = project_key;
    }

    public String getOrganisation_id() {
        return organisation_id;
    }

    public void setOrganisation_id(String organisation_id) {
        this.organisation_id = organisation_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
