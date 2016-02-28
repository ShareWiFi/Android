package com.tbaehr.sharewifi.model.viewmodel;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.ShareWiFiApplication;

/**
 * Created by tbaehr on 21.02.16.
 */
public class WiFiNetwork {

    public enum SignalStrenght {
        NONE,
        LOW,
        MEDIUM,
        GOOD,
        PERFECT
    }

    public enum Quality {
        GOOD,
        BAD
    }

    public enum ShareStatus {
        UNKNOWN,
        NOT_SHARED,
        SHARED_BY_ME_WITH_MY_DEVICES,
        SHARED_BY_ME_WITHIN_GROUPS,
        SHARED_WITH_ME_WITHIN_GROUPS,
        SHARED_BY_ME_WITH_EVERYONE,
        SHARED_WITH_ME_WITH_EVERYONE
    }

    private boolean connected;

    private boolean encrypted;

    private String ssid;

    private ShareStatus shareStatus;

    private SignalStrenght signalStrenght;

    private Quality quality;

    public WiFiNetwork(String ssid, boolean encrypted, SignalStrenght signalStrenght, boolean connected, Quality quality, ShareStatus shareStatus) {
        this.ssid = ssid;
        this.connected = connected;
        this.shareStatus = shareStatus;
        this.signalStrenght = signalStrenght;
        this.encrypted = encrypted;
        this.quality = quality;
    }

    public WiFiNetwork(String ssid, boolean encrypted, SignalStrenght signalStrenght, boolean connected, ShareStatus shareStatus) {
        this.ssid = ssid;
        this.connected = connected;
        this.shareStatus = shareStatus;
        this.signalStrenght = signalStrenght;
        this.encrypted = encrypted;
    }

    public String getSsid() {
        return ssid;
    }

    public String getShareStatusDescription() {
        String connectionStatusDescription = connected ? ShareWiFiApplication.getAppContext().getString(
                R.string.networkstatus_connected)+". " : "";
        if (shareStatus == ShareStatus.UNKNOWN) {
            return ShareWiFiApplication.getAppContext().getString(R.string.networkstatus_unknownnetwork);
        } else if (shareStatus == ShareStatus.NOT_SHARED) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_notshared);
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITH_MY_DEVICES) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_sharedByMe_withmydevices);
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITHIN_GROUPS) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_sharedByMe_withingroups);
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITH_EVERYONE) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_sharedByMe_witheveryone);
        } else if (shareStatus == ShareStatus.SHARED_WITH_ME_WITHIN_GROUPS) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_sharedWithMe_groups);
        } else if (shareStatus == ShareStatus.SHARED_WITH_ME_WITH_EVERYONE) {
            return connectionStatusDescription + ShareWiFiApplication.getAppContext().getString(R.string.sharestatus_sharedWithMe_everyone);
        } else {
            throw new RuntimeException("Non-handled share status "+shareStatus.name());
        }
    }

    public int getShareStatusIcon() {
        if (shareStatus == ShareStatus.UNKNOWN) {
            throw new RuntimeException("Cannot return a share icon for a unknown network");
        } else if (shareStatus == ShareStatus.NOT_SHARED) {
            return R.drawable.ic_menu_share_off;
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITH_MY_DEVICES) {
            return R.drawable.ic_menu_shared_with_devices;
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITHIN_GROUPS) {
            return R.drawable.ic_menu_shared_with_groups;
        } else if (shareStatus == ShareStatus.SHARED_BY_ME_WITH_EVERYONE) {
            return R.drawable.ic_menu_shared_with_all;
        } else if (shareStatus == ShareStatus.SHARED_WITH_ME_WITHIN_GROUPS) {
            return R.drawable.ic_menu_downloaded_blue;
        } else if (shareStatus == ShareStatus.SHARED_WITH_ME_WITH_EVERYONE) {
            return R.drawable.ic_menu_downloaded_orange;
        } else {
            throw new RuntimeException("Non-handled share status "+shareStatus.name());
        }
    }

    public int getSignalStrenghtIcon() {
        if (signalStrenght == SignalStrenght.NONE) {
            return R.drawable.ic_menu_signal_off;
        } else if (signalStrenght == SignalStrenght.LOW) {
            return R.drawable.ic_menu_signal_low;
        } else if (signalStrenght == SignalStrenght.MEDIUM) {
            return R.drawable.ic_menu_signal_medium;
        } else if (signalStrenght == SignalStrenght.GOOD) {
            return R.drawable.ic_menu_signal_good;
        } else if (signalStrenght == SignalStrenght.PERFECT) {
            return R.drawable.ic_menu_signal_perfect;
        } else {
            throw new RuntimeException("Non-handled share status "+shareStatus.name());
        }
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean isUnknownNetwork() {
        return ShareStatus.UNKNOWN == shareStatus;
    }

    public boolean isQualityBad() {
        return quality != null && quality == Quality.BAD;
    }
}
