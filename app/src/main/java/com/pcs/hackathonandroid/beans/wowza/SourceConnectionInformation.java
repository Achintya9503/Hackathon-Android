package com.pcs.hackathonandroid.beans.wowza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SourceConnectionInformation {
    @SerializedName("application")
    @Expose
    public String application;
    @SerializedName("disable_authentication")
    @Expose
    public Boolean disableAuthentication;
    @SerializedName("host_port")
    @Expose
    public Integer hostPort;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("primary_server")
    @Expose
    public String primaryServer;
    @SerializedName("stream_name")
    @Expose
    public String streamName;
    @SerializedName("username")
    @Expose
    public String username;
}
