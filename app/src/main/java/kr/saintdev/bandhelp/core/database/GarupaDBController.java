package kr.saintdev.bandhelp.core.database;
/**
 * Copyright (c) 2015-2019 Saint software All rights.
 * Team. 09:19
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.saintdev.bandhelp.types.GarupaPlayTime;

public class GarupaDBController {
    private static GarupaDBController instance = null;

    public static GarupaDBController getInstance(Context context) {
        if(GarupaDBController.instance == null) {
            instance = new GarupaDBController(context);
        }

        return instance;
    }

    private GarupaDBHelper dbHelper = null;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private GarupaDBController(Context context) {
        this.dbHelper = new GarupaDBHelper(context, "garupa", null, 1);
    }

    public void addNewPlayTime(GarupaPlayTime newTime) {
        String dateString = format.format(newTime.getStartDateTime());

        SQLiteDatabase writable = dbHelper.getWritable();
        writable.execSQL("INSERT INTO `garupa_play_time` (play_time, play_date, play_nation) VALUES(?,?,?);",
                new Object[]{ newTime.getPlayTimeMinute(), dateString, GarupaPlayTime.getStringFromNation(newTime.getPlayNation()) });
        writable.close();
    }

    /**
     * _id 에 맞는 칼럼 정보를 가공하여 가져온다.
     * @param _id   DB Primary key.
     * @return  get current play time from primary key.
     */
    public GarupaPlayTime getPlayTime(int _id) {
        SQLiteDatabase readable = dbHelper.getReadOnly();
        Cursor csr = readable.rawQuery("SELECT * FROM garupa_play_time WHERE _id = ?", new String[]{ _id+"" });

        if(csr.moveToNext()) {
            return parseCursorToPlayTime(csr);
        } else {
            return null;
        }
    }

    /**
     *  날짜에 맞는 플레이 시간들을 가져온다.
     * @param date  current date
     * @return  Playtime array.
     */
    public ArrayList<GarupaPlayTime> getCurrentDayPlayTime(String date) {
        SQLiteDatabase readable = dbHelper.getReadOnly();
        Cursor csr = readable.rawQuery("SELECT * FROM garupa_play_time WHERE play_date LIKE '%"+date+"%'", null);

        ArrayList<GarupaPlayTime> playTimes = new ArrayList<>();
        while(csr.moveToNext()) {
            GarupaPlayTime playTime = parseCursorToPlayTime(csr);
            if(playTime != null) {
                playTimes.add(playTime);
            }
        }

        return playTimes.size() == 0 ? null : playTimes;
    }

    /**
     * 모든 플레이 시간 객체를 리턴한다.
     * @return Playtime array.
     */
    public ArrayList<GarupaPlayTime> getAllPlayTimes() {
        SQLiteDatabase readable = dbHelper.getReadOnly();
        Cursor csr = readable.rawQuery("SELECT * FROM garupa_play_time ORDER BY _id DESC", null);

        ArrayList<GarupaPlayTime> playTimes = new ArrayList<>();
        while(csr.moveToNext()) {
            GarupaPlayTime playTime = parseCursorToPlayTime(csr);
            if(playTime != null) {
                playTimes.add(playTime);
            }
        }

        return playTimes.size() == 0 ? null : playTimes;
    }

    /**
     * Parse Cursor data.
     * @param csr   Cursor (Should move to next ready)
     * @return      GarupaPlayTime
     */
    private GarupaPlayTime parseCursorToPlayTime(Cursor csr) {
        try {
            int playTime = csr.getInt(1);
            Date playDate = format.parse(csr.getString(2));
            GarupaPlayTime.GarupaNation nation = GarupaPlayTime.getNationFromString(csr.getString(3));
            return new GarupaPlayTime(playTime, playDate, nation);
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
