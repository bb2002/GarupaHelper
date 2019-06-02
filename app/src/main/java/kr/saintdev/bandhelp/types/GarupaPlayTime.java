package kr.saintdev.bandhelp.types;

/**
 * Copyright (c) 2015-2019 Saint software All rights.
 * Team 09:19
 */

import java.util.Date;

public class GarupaPlayTime {
    public enum GarupaNation {
        GARUPA_KOREA,
        GARUPA_JAPANESE
    }

    private int playTimeMinute;         // 플레이 시간
    private Date startDateTime;      // 게임 시작 시간
    private GarupaNation playNation;

    public GarupaPlayTime(int playTimeMinute, Date startDateTime, GarupaNation playNation) {
        this.playTimeMinute = playTimeMinute;
        this.startDateTime = startDateTime;
        this.playNation = playNation;
    }

    public int getPlayTimeMinute() {
        return playTimeMinute;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public GarupaNation getPlayNation() {
        return playNation;
    }

    public static GarupaNation getNationFromString(String nation) {
        if(nation.equals("Korean")) {
            return GarupaNation.GARUPA_KOREA;
        } else if(nation.equals("Japanese")) {
            return GarupaNation.GARUPA_JAPANESE;
        } else {
            return GarupaNation.GARUPA_KOREA;
        }
    }

    public static String getStringFromNation(GarupaNation nation) {
        switch (nation) {
            case GARUPA_JAPANESE: return "Japanese";
            case GARUPA_KOREA:    return "Korean";
            default:    return "Korean";
        }
    }
}
