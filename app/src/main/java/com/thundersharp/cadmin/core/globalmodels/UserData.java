package com.thundersharp.cadmin.core.globalmodels;

public class UserData {
    public UserData(){}

    public String bio,dob,email,image_uri,name,phone_no,uid;

    public UserData(String bio, String dob, String email, String image_uri, String name, String phone_no, String uid) {
        this.bio = bio;
        this.dob = dob;
        this.email = email;
        this.image_uri = image_uri;
        this.name = name;
        this.phone_no = phone_no;
        this.uid = uid;
    }

    public String getBio() {
        return bio;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getName() {
        return name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getUid() {
        return uid;
    }
}
