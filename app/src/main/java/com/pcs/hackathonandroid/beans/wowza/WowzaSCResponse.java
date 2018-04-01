package com.pcs.hackathonandroid.beans.wowza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WowzaSCResponse {
    @SerializedName("live_stream")
    @Expose
    public LiveStream liveStream;
}
