package kr.saintdev.bandhelp.core.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import java.util.Date;

import kr.saintdev.bandhelp.R;
import kr.saintdev.bandhelp.core.database.GarupaDBController;
import kr.saintdev.bandhelp.types.GarupaPlayTime;


public class DetectService extends AccessibilityService {
    private static String[] IGNORE_APPLICATIONS = new String[]{
            "com.google.android.gms",
            "com.android.systemui",
            "com.android.vending",
            "kr.saintdev.bandhelp"
    };

    private boolean isBangdreamRunning = false;
    private GarupaPlayTime.GarupaNation bangdreamNation = GarupaPlayTime.GarupaNation.GARUPA_KOREA;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                String eventPackageName = event.getPackageName().toString();
                if (isIgnoreableApp(eventPackageName)) {
                    // 무시해도 상관 없는 이벤트
                    return;
                }

                boolean isBangdreamEvent = isEventByBangdream(eventPackageName);

                if (isBangdreamRunning) {
                    // 현재 뱅드림 실행 중.
                    if (!isBangdreamEvent) {
                        // 뱅드림이 중지됨.
                        onBangdreamStop();
                    }
                } else {
                    // 현재 뱅드림 실행중이 아님.
                    if (isBangdreamEvent) {
                        // 뱅드림이 실행됨
                        onBangdreamRun();
                    }
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * @Date 05.24 2019
     * Call when bangdream stopped.
     */
    private void onBangdreamStop() {
        isBangdreamRunning = false;
        stopService(new Intent(this, ProtectorService.class));

        Date startDate = new Date(currnetPlayStartTime);
        int playTime = (int) ((System.currentTimeMillis() - currnetPlayStartTime) / 60000);

        if(playTime != 0) {
            // Play 1 minute over.
            GarupaPlayTime newTime = new GarupaPlayTime(playTime, startDate, bangdreamNation);
            GarupaDBController.getInstance(this).addNewPlayTime(newTime);
        }
    }

    /**
     * @Date 05.24 2019
     * Call when bangdream started.
     */
    long currnetPlayStartTime = 0;
    private void onBangdreamRun() {
        isBangdreamRunning = true;
        startService(new Intent(this, ProtectorService.class));

        // Save Play Start Time.
        this.currnetPlayStartTime = System.currentTimeMillis();
    }

    /**
     * @Date 05.24 2019
     * @param eventPackageName  => EventProvider Package name.
     * @return  => if event provider is bangdream, return true.
     */
    private boolean isEventByBangdream(String eventPackageName) {
        String[] apps = getResources().getStringArray(R.array.garupa_packages);
        if(apps[0].equals(eventPackageName)) {
            // Play Korean.
            bangdreamNation = GarupaPlayTime.GarupaNation.GARUPA_KOREA;
            return true;
        } else if(apps[1].equals(eventPackageName)) {
            // Play japanese.
            bangdreamNation = GarupaPlayTime.GarupaNation.GARUPA_JAPANESE;
            return true;
        }

        // Not bangdream.
        return false;
    }

    /**
     * @Date 05.24 2019
     * @param eventPackageName  => EventProvider Package name.
     * @return  => If event provider is ignoreable app, return true.
     */
    private boolean isIgnoreableApp(String eventPackageName) {
        for(String app : IGNORE_APPLICATIONS) {
            if(app.equals(eventPackageName)) {
                return true;
            }
        }

        return false;
    }
}
