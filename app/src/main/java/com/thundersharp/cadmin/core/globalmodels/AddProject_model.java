package com.thundersharp.cadmin.core.globalmodels;

public class AddProject_model {
    public String projectName;
    public String projectDesc;
    public String project_id;
    public String organisation_id;
    public boolean status;

    public AddProject_model() {
    }

    public AddProject_model(String projectName, String projectDesc, String project_id, String organisation_id, boolean status) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.project_id = project_id;
        this.organisation_id = organisation_id;
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getOrganisation_id() {
        return organisation_id;
    }

    public boolean isStatus() {
        return status;
    }
}
