package kr.saintdev.bandhelp.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.saintdev.bandhelp.R;
import kr.saintdev.bandhelp.core.database.GarupaDBController;
import kr.saintdev.bandhelp.types.GarupaPlayTime;
import kr.saintdev.bandhelp.views.lib.GithubGrass;

public class PlayRecoredActivity extends AppCompatActivity {
    private GithubGrass grassView = null;
    private int totalPlayTime = 0;
    private int playDate = 0;
    private int veryLong = 0;
    private Date verylongdate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recored);
        this.grassView = findViewById(R.id.grass);

        drawGrassBlocks();

        ((TextView) findViewById(R.id.recored_1year_data)).setText(totalPlayTime + "M");
        ((TextView) findViewById(R.id.recored_avarge_data)).setText(totalPlayTime / playDate + "M");
        ((TextView) findViewById(R.id.recored_1year_data)).setText(totalPlayTime / playDate + "M");

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String to = transFormat.format(verylongdate);

        ((TextView) findViewById(R.id.recored_long_date_data)).setText(to);

    }

    /**
     * @Date 06.11 2019
     * Draw Grass Views
     */
    private void drawGrassBlocks() {
        Calendar cal = Calendar.getInstance();
        GarupaDBController dbHelper = GarupaDBController.getInstance(this);

        String month = (cal.get(Calendar.MONTH) + 1) <= 9 ? ("0" + (cal.get(Calendar.MONTH) + 1)) : (cal.get(Calendar.MONTH) + 1) + "";

        ArrayList<GarupaPlayTime> times = dbHelper.getCurrentMonthPlayTime(cal.get(Calendar.YEAR) + "-" + month);

        if(times != null) {
            for (int i = 0; i < times.size(); i++) {
                Calendar dateTime = Calendar.getInstance();
                dateTime.setTime(times.get(i).getStartDateTime());      // 시작 날짜를 구한다.
                int playtimes = 0;

                for (; i < times.size(); i++) {
                    GarupaPlayTime nextTime = times.get(i);
                    Calendar nextCal = Calendar.getInstance();
                    nextCal.setTime(times.get(i).getStartDateTime());      // 시작 날짜를 구한다.

                    if (nextCal.get(Calendar.DAY_OF_MONTH) == dateTime.get(Calendar.DAY_OF_MONTH) && nextCal.get(Calendar.MONTH) == dateTime.get(Calendar.MONTH) && nextCal.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR)) {
                        // 년 월 일이 모두 같은 플레이 시간
                        playtimes += nextTime.getPlayTimeMinute();
                        this.totalPlayTime += nextTime.getPlayTimeMinute();

                        if(veryLong < nextTime.getPlayTimeMinute()) {
                            veryLong = nextTime.getPlayTimeMinute();
                            verylongdate = nextTime.getStartDateTime();
                        }
                    } else {
                        i--;
                        break;
                    }
                }

                int color;
                playDate ++;

                if (playtimes == 0) {
                    playDate --;
                    color = R.color.githubGrass_Gray;
                } else if (playtimes > 0 && playtimes <= 30) {
                    color = R.color.githubGrass_Green1;
                } else if (playtimes >= 31 && playtimes <= 60) {
                    color = R.color.githubGrass_Green2;
                } else if (playtimes > 61 && playtimes <= 120) {
                    color = R.color.githubGrass_Green3;
                } else {
                    color = R.color.githubGrass_Red1;
                }

                this.grassView.setGrassColor(dateTime.get(Calendar.DAY_OF_MONTH), dateTime.get(Calendar.MONTH) + 1, color);
            }
        }
    }
}
