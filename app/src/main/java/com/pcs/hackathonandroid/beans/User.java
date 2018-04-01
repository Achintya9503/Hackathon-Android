package com.pcs.hackathonandroid.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("fullName")
    @Expose
    public String fullName;
    @SerializedName("streaming")
    @Expose
    public Boolean isStreaming;
    @SerializedName("uid")
    @Expose
    public String uid;

    public User(String email, String fullName, String uid) {
        this.email = email;
        this.fullName = fullName;
        this.uid = uid;
    }
}
