package com.thundersharp.cadmin.ui.Model;

import android.media.Image;
import android.widget.ImageView;

public class AddProject {
    String projectName;
    String projectDesc;
//    String organisation_id;
//    ImageView logo;

    public AddProject(String projectName, String projectDesc) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
//        this.organisation_id = organisation_id;
//        this.logo = logo;
    }

    public AddProject() {
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

//    public String getOrganisation_id() {
//        return organisation_id;
//    }
//
//    public void setOrganisation_id(String organisation_id) {
//        this.organisation_id = organisation_id;
//    }
//
//    public ImageView getLogo() {
//        return logo;
//    }
//
//    public void setLogo(ImageView logo) {
//        this.logo = logo;
//    }
}
