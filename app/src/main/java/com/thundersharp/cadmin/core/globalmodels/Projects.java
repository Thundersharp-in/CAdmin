package com.thundersharp.cadmin.core.globalmodels;

public class Projects {

    public String projectKey;
    public boolean project_head;

    public Projects(String projectKey, boolean project_head) {
        this.projectKey = projectKey;
        this.project_head = project_head;
    }


    public Projects(){

    }


    public String getProjectKey() {
        return projectKey;
    }

    public boolean isProject_head() {
        return project_head;
    }
}
