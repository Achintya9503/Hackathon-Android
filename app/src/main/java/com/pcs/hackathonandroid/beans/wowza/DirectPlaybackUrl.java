package com.pcs.hackathonandroid.beans.wowza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectPlaybackUrl {
    @SerializedName("rtmp")
    @Expose
    public List<String> rtmp = null;
    @SerializedName("rtsp")
    @Expose
    public List<String> rtsp = null;
    @SerializedName("wowz")
    @Expose
    public List<String> wowz = null;
}
