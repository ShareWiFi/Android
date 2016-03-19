package com.tbaehr.sharewifi.model.viewmodel;

import android.net.wifi.ScanResult;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.ShareWiFiApplication;
import com.tbaehr.sharewifi.model.SecurityMode;

import static com.tbaehr.sharewifi.features.WiFiHelper.getQuality;
import static com.tbaehr.sharewifi.features.WiFiHelper.getScanResultSecurity;
import static com.tbaehr.sharewifi.features.WiFiHelper.getSignalStrength;
import static com.tbaehr.sharewifi.features.WiFiHelper.isConnectedWith;

/**
 * Created by tbaehr on 21.02.16.
 */
public class WiFiNetwork implements Comparable<WiFiNetwork> {

    public enum SignalStrength {
        NONE,
        LOW,
        MEDIUM,
        GOOD,
        PERFECT;
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
        SHARED_WITH_ME_WITH_EVERYONE;

        public Integer toInteger() {
            switch (this) {
                case SHARED_BY_ME_WITH_EVERYONE: return 0;
                case SHARED_BY_ME_WITHIN_GROUPS: return 1;
                case SHARED_BY_ME_WITH_MY_DEVICES: return 2;
                case SHARED_WITH_ME_WITH_EVERYONE: return 3;
                case SHARED_WITH_ME_WITHIN_GROUPS: return 4;
                case NOT_SHARED: return 5;
                case UNKNOWN: return 6;
                default: return -1;
            }
        }

        public static ShareStatus fromInteger(Integer integer) {
            switch (integer) {
                case 0: return SHARED_BY_ME_WITH_EVERYONE;
                case 1: return SHARED_BY_ME_WITHIN_GROUPS;
                case 2: return SHARED_BY_ME_WITH_MY_DEVICES;
                case 3: return SHARED_WITH_ME_WITH_EVERYONE;
                case 4: return SHARED_WITH_ME_WITHIN_GROUPS;
                case 5: return NOT_SHARED;
                case 6: return UNKNOWN;
                default: throw new IllegalArgumentException("Integer "+integer+" is not a valid share status ID.");
            }
        }

        public boolean isShared() {
            return !(this.equals(UNKNOWN) || this.equals(NOT_SHARED));
        }

        public boolean isSharedByMe() {
            return this.equals(ShareStatus.SHARED_BY_ME_WITH_EVERYONE) ||
                    this.equals(ShareStatus.SHARED_BY_ME_WITHIN_GROUPS) ||
                    this.equals(ShareStatus.SHARED_BY_ME_WITH_MY_DEVICES);
        }

        public boolean isSharedWithEveryone() {
            return this.equals(SHARED_BY_ME_WITH_EVERYONE) || this.equals(SHARED_WITH_ME_WITH_EVERYONE);
        }

        public boolean isSharedWithinGroups() {
            return this.equals(SHARED_BY_ME_WITHIN_GROUPS) || this.equals(SHARED_WITH_ME_WITHIN_GROUPS);
        }

        public boolean isSharedWithMyDevices() {
            return this.equals(SHARED_BY_ME_WITH_MY_DEVICES);
        }

        public int getDrawable() {
            switch (this) {
                case SHARED_BY_ME_WITH_EVERYONE: return R.drawable.ic_menu_shared_with_all;
                case SHARED_BY_ME_WITHIN_GROUPS: return R.drawable.ic_menu_shared_with_groups;
                case SHARED_BY_ME_WITH_MY_DEVICES: return R.drawable.ic_menu_shared_with_devices;
                case SHARED_WITH_ME_WITH_EVERYONE: return R.drawable.ic_menu_downloaded_blue;
                case SHARED_WITH_ME_WITHIN_GROUPS: return R.drawable.ic_menu_downloaded_orange;
                case NOT_SHARED: return R.drawable.ic_menu_share_off;
                case UNKNOWN: return -1;
                default: return -1;
            }
        }
    }

    private boolean connected;

    private boolean encrypted;

    private String ssid;

    private ShareStatus shareStatus;

    private SignalStrength signalStrength;

    private int signalLevel;

    private Quality quality;

    public WiFiNetwork(ScanResult scanResult, ShareStatus shareStatus) {
        ssid = scanResult.SSID != null ? scanResult.SSID : scanResult.BSSID;
        if (ssid.equals("")) {
            ssid = ShareWiFiApplication.getAppContext().getString(R.string.networkstatus_hidden_wifi);
        }
        encrypted = getScanResultSecurity(scanResult).equals(SecurityMode.OPEN) ? false : true;
        signalStrength = getSignalStrength(scanResult);
        signalLevel = scanResult.level;
        connected = isConnectedWith(scanResult);
        quality = getQuality(scanResult);

        this.shareStatus = shareStatus;
    }

    public WiFiNetwork(String ssid, boolean encrypted, SignalStrength signalStrength, boolean connected, Quality quality, ShareStatus shareStatus) {
        this.ssid = ssid;
        this.connected = connected;
        this.shareStatus = shareStatus;
        this.signalStrength = signalStrength;
        this.encrypted = encrypted;
        this.quality = quality;
    }

    public WiFiNetwork(String ssid, boolean encrypted, SignalStrength signalStrength, boolean connected, ShareStatus shareStatus) {
        this.ssid = ssid;
        this.connected = connected;
        this.shareStatus = shareStatus;
        this.signalStrength = signalStrength;
        this.encrypted = encrypted;
    }

    @Override
    public int compareTo(WiFiNetwork another) {
        if (connected) {
            return -1;
        }

        if (another.connected) {
            return 1;
        }

        return (new Integer(another.signalLevel)).compareTo((new Integer(signalLevel)));
    }

    public String getSsid() {
        return ssid;
    }

    public ShareStatus getShareStatus() {
        return shareStatus;
    }

    public String getDescription() {
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

    public int getSignalStrengthIcon() {
        if (signalStrength == SignalStrength.NONE) {
            return R.drawable.ic_menu_signal_off;
        } else if (signalStrength == SignalStrength.LOW) {
            return R.drawable.ic_menu_signal_low;
        } else if (signalStrength == SignalStrength.MEDIUM) {
            return R.drawable.ic_menu_signal_medium;
        } else if (signalStrength == SignalStrength.GOOD) {
            return R.drawable.ic_menu_signal_good;
        } else if (signalStrength == SignalStrength.PERFECT) {
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
