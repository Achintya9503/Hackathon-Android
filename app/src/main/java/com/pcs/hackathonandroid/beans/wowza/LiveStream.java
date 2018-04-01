package com.pcs.hackathonandroid.beans.wowza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveStream {
    @SerializedName("aspect_ratio_height")
    @Expose
    public Integer aspectRatioHeight;
    @SerializedName("aspect_ratio_width")
    @Expose
    public Integer aspectRatioWidth;
    @SerializedName("billing_mode")
    @Expose
    public String billingMode;
    @SerializedName("broadcast_location")
    @Expose
    public String broadcastLocation;
    @SerializedName("closed_caption_type")
    @Expose
    public String closedCaptionType;
    @SerializedName("connection_code")
    @Expose
    public String connectionCode;
    @SerializedName("connection_code_expires_at")
    @Expose
    public String connectionCodeExpiresAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("delivery_method")
    @Expose
    public String deliveryMethod;
    @SerializedName("delivery_protocols")
    @Expose
    public List<String> deliveryProtocols = null;
    @SerializedName("direct_playback_urls")
    @Expose
    public List<DirectPlaybackUrl> directPlaybackUrls = null;
    @SerializedName("encoder")
    @Expose
    public String encoder;
    @SerializedName("hosted_page")
    @Expose
    public Boolean hostedPage;
    @SerializedName("hosted_page_sharing_icons")
    @Expose
    public Boolean hostedPageSharingIcons;
    @SerializedName("hosted_page_title")
    @Expose
    public String hostedPageTitle;
    @SerializedName("hosted_page_url")
    @Expose
    public String hostedPageUrl;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("low_latency")
    @Expose
    public Boolean lowLatency;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("player_countdown")
    @Expose
    public Boolean playerCountdown;
    @SerializedName("player_embed_code")
    @Expose
    public String playerEmbedCode;
    @SerializedName("player_hls_playback_url")
    @Expose
    public String playerHlsPlaybackUrl;
    @SerializedName("player_id")
    @Expose
    public String playerId;
    @SerializedName("player_responsive")
    @Expose
    public Boolean playerResponsive;
    @SerializedName("player_type")
    @Expose
    public String playerType;
    @SerializedName("recording")
    @Expose
    public Boolean recording;
    @SerializedName("source_connection_information")
    @Expose
    public SourceConnectionInformation sourceConnectionInformation;
    @SerializedName("stream_targets")
    @Expose
    public List<StreamTarget> streamTargets = null;
    @SerializedName("target_delivery_protocol")
    @Expose
    public String targetDeliveryProtocol;
    @SerializedName("transcoder_type")
    @Expose
    public String transcoderType;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("use_stream_source")
    @Expose
    public Boolean useStreamSource;
}
