package com.thundersharp.cadmin.core.globalmodels;

public class Projects {

    public String projectKey;
    public String project_org;

    public Projects(String projectKey, String project_org) {
        this.projectKey = projectKey;
        this.project_org = project_org;
    }


    public Projects(){

    }


    public String getProjectKey() {
        return projectKey;
    }

    public String getProject_org() {
        return project_org;
    }
}
