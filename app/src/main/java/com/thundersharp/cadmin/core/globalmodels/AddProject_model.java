package com.thundersharp.cadmin.core.globalmodels;

public class AddProject_model {
    String projectName;
    String projectDesc;
    String project_id;
    //ImageView logo;

    public AddProject_model() {
    }

    public AddProject_model(String projectName, String projectDesc, String project_id) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.project_id = project_id;
//        this.logo = logo;
    }



    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    //
//    public ImageView getLogo() {
//        return logo;
//    }
//
//    public void setLogo(ImageView logo) {
//        this.logo = logo;
//    }
}
