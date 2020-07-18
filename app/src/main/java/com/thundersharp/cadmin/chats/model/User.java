package com.thundersharp.cadmin.chats.model;


import java.io.Serializable;

public class User implements Serializable {
    public String charactor_id;
    public String data;
    public String ingame_name;
    public String real_name;
    public String useremail;
    public String phonenumber;
    public String joined;
    public String uid;
    public String firebaseToken;

    public User(){

    }

    public User(String charactor_id, String data, String ingame_name, String real_name, String useremail, String phonenumber, String joined, String uid, String firebaseToken) {
        this.charactor_id = charactor_id;
        this.data = data;
        this.ingame_name = ingame_name;
        this.real_name = real_name;
        this.useremail = useremail;
        this.phonenumber = phonenumber;
        this.joined = joined;
        this.uid = uid;
        this.firebaseToken = firebaseToken;
    }

}
