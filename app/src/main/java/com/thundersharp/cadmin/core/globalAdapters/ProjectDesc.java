package com.thundersharp.cadmin.core.globalAdapters;

public class ProjectDesc {
    public ProjectDesc(){}
    public String describe,organisation_id,project_key,project_name;
    public boolean status;

    public ProjectDesc(String describe, String organisation_id, String project_key, String project_name, boolean status) {
        this.describe = describe;
        this.organisation_id = organisation_id;
        this.project_key = project_key;
        this.project_name = project_name;
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public String getOrganisation_id() {
        return organisation_id;
    }

    public String getProject_key() {
        return project_key;
    }

    public String getProject_name() {
        return project_name;
    }

    public boolean isStatus() {
        return status;
    }
}
