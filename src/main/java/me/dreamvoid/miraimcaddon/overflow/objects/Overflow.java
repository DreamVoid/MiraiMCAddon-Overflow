package me.dreamvoid.miraimcaddon.overflow.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Overflow {
    @SerializedName("ws_host")
    private String wsHost = "ws://127.0.0.1:3001";

    @SerializedName("reversed_ws_port")
    private int reversedWsPort = -1;

    @SerializedName("token")
    private String token = "";

    @SerializedName("no_platform")
    private boolean noPlatform = false;

    @SerializedName("use_cq_code")
    private boolean useCqCode = false;

    @SerializedName("retry-times")
    private int retryTimes = 5;

    @SerializedName("retry_wait_mills")
    private long retryWaitMills = 5000;

    @SerializedName("retry_rest_mills")
    private long retryRestMills = 60000;

    @SerializedName("heartbeat_check_seconds")
    private int heartbeatCheckSeconds = 60;

    @SerializedName("drop_events_before_connected")
    private boolean dropEventsBeforeConnected = true;
}