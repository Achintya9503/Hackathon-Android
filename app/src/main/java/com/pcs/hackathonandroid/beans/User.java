package com.pcs.hackathonandroid.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("is_streaming")
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
