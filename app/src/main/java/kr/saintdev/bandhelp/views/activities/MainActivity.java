package kr.saintdev.bandhelp.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.saintdev.bandhelp.R;
import kr.saintdev.bandhelp.core.database.GarupaDBController;
import kr.saintdev.bandhelp.core.libs.ApplicationDetector;
import kr.saintdev.bandhelp.core.libs.PermissionUtility;
import kr.saintdev.bandhelp.core.services.DetectService;
import kr.saintdev.bandhelp.types.GarupaPlayTime;
import kr.saintdev.bandhelp.views.lib.GithubGrass;

public class MainActivity extends AppCompatActivity {
    private TextView appStatusTextView = null;
    private ImageView appStatusImageView = null;
    private CardView appStatusCont = null;
    private TextView garupaKoreanStatus = null;
    private TextView garupaJapaneseStatus = null;
    private TextView todayPlayTimeText = null;
    private View[] optionsView = null;
    private LinearLayout monthPlayTimeCont = null;
    private GithubGrass grassView = null;
    private View grassViewContainer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find View Objects.
        this.appStatusTextView = findViewById(R.id.app_status_text);
        this.appStatusImageView = findViewById(R.id.app_status_icon);
        this.appStatusCont = findViewById(R.id.application_status_cont);
        this.garupaJapaneseStatus = findViewById(R.id.garupa_japanese_status);
        this.garupaKoreanStatus = findViewById(R.id.garupa_korean_status);
        this.todayPlayTimeText = findViewById(R.id.today_playtime_text);
        this.optionsView = new View[] {
                        findViewById(R.id.display_settings_cont),
                        findViewById(R.id.application_info_cont)
        };
        this.monthPlayTimeCont = findViewById(R.id.record_gatsu);
        this.grassViewContainer = findViewById(R.id.month_grass_cont);

        // Draw grassView
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        this.grassView = new GithubGrass(this, month, month, 0);
        this.grassView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        monthPlayTimeCont.addView(this.grassView);

        this.optionsView[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GeneralSettingsActivity.class));
            }
        });
        this.optionsView[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DevInfoActivity.class));
            }
        });
        this.appStatusCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GeneralSettingsActivity.class));
            }
        });

        this.grassViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PlayRecoredActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Detect application problem
        setApplicationProblems();

        // Search Garupa game.
        searchGarupaGames();

        // Set today play time
        setTodayPlayTimeData();

        drawGrassBlocks();
    }

    /**
     * @Date 06.01 2019
     * SetApplicationProblems View
     */
    private void setApplicationProblems() {
        StringBuilder message = new StringBuilder();
        boolean isHadProblem = false;

        // Check Accessibility service
        if(!PermissionUtility.isAccessibilityServiceEnable(this, DetectService.class)) {
            message.append(getString(R.string.application_accessibility_error));
            isHadProblem = true;
        }

        // Check Overlay Permission
        if(!PermissionUtility.hadOverlayPermission(this)) {
            if(isHadProblem) {
                message.append(", ");
                message.append(getString(R.string.application_permission_error));
            } else {
                message.append(getString(R.string.application_permission_error));
            }

            isHadProblem = true;
        }

        if(isHadProblem) {
            this.appStatusCont.setCardBackgroundColor(getResources().getColor(R.color.colorWarning));
            this.appStatusTextView.setText(message.toString());
            this.appStatusImageView.setImageResource(R.drawable.ic_status_error);
        } else {
            this.appStatusCont.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            this.appStatusTextView.setText(R.string.application_ok);
            this.appStatusImageView.setImageResource(R.drawable.ic_status_ok);
        }
    }

    /**
     * @Date 06.01 2019
     * Search Bangdream! Girls band party!.
     */
    private void searchGarupaGames() {
        ApplicationDetector detector = new ApplicationDetector(MainActivity.this, new ApplicationDetector.OnApplicationSearchListener() {
            @Override
            public void onResult(Boolean[] result) {
                if(result[0]) {
                    garupaKoreanStatus.setText(R.string.garupa_installed);
                    garupaKoreanStatus.setTextColor(getResources().getColor(R.color.colorSuccess));
                }
                if(result[1]) {
                    garupaJapaneseStatus.setText(R.string.garupa_installed);
                    garupaKoreanStatus.setTextColor(getResources().getColor(R.color.colorSuccess));
                }
            }
        });
        String[] bangdreams = getResources().getStringArray(R.array.garupa_packages);
        detector.execute(bangdreams[0], bangdreams[1]);
    }

    /**
     * @Date 06.02 2019
     * Set Today play time
     */
    private void setTodayPlayTimeData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = new Date(System.currentTimeMillis());
        String dateString = format.format(date);

        ArrayList<GarupaPlayTime> playTimes =  GarupaDBController.getInstance(this).getCurrentDayPlayTime(dateString);
        if(playTimes == null) {
            this.todayPlayTimeText.setText("00h 00m");
        } else {
            int times = 0;
            for(GarupaPlayTime time : playTimes) {
                times += time.getPlayTimeMinute();
            }

            int hour = times / 60;
            int minute = times % 60;
            this.todayPlayTimeText.setText(hour + "h " + minute + "m");
        }
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
                    } else {
                        i--;
                        break;
                    }
                }

                int color;
                if (playtimes == 0) {
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
