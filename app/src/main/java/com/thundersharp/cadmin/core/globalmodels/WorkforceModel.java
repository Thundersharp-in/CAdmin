package com.thundersharp.cadmin.core.globalmodels;

public class WorkforceModel {

    public WorkforceModel(){}

    String compleated,endtime,project_Key,
            starttime,
            usersassigned
            ;

    public WorkforceModel(String compleated, String endtime, String project_Key, String starttime, String usersassigned) {
        this.compleated = compleated;
        this.endtime = endtime;
        this.project_Key = project_Key;
        this.starttime = starttime;
        this.usersassigned = usersassigned;
    }

    public String getCompleated() {
        return compleated;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getProject_Key() {
        return project_Key;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getUsersassigned() {
        return usersassigned;
    }
}
