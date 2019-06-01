package kr.saintdev.bandhelp.core.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import kr.saintdev.bandhelp.R;


public class DetectService extends AccessibilityService {
    private static String[] IGNORE_APPLICATIONS = new String[]{
            "com.google.android.gms",
            "com.android.systemui",
            "com.android.vending",
            "kr.saintdev.bandhelp"
    };

    private boolean isBangdreamRunning = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String eventPackageName = event.getPackageName().toString();
            if(isIgnoreableApp(eventPackageName)) {
                // 무시해도 상관 없는 이벤트
                return;
            }

            boolean isBangdreamEvent = isEventByBangdream(eventPackageName);

            if(isBangdreamRunning) {
                // 현재 뱅드림 실행 중.
                if(!isBangdreamEvent) {
                    // 뱅드림이 중지됨.
                    onBangdreamStop();
                }
            } else {
                // 현재 뱅드림 실행중이 아님.
                if(isBangdreamEvent) {
                    // 뱅드림이 실행됨
                    onBangdreamRun();
                }
            }
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
    }

    /**
     * @Date 05.24 2019
     * Call when bangdream started.
     */
    private void onBangdreamRun() {
        isBangdreamRunning = true;
        startService(new Intent(this, ProtectorService.class));
    }

    /**
     * @Date 05.24 2019
     * @param eventPackageName  => EventProvider Package name.
     * @return  => if event provider is bangdream, return true.
     */
    private boolean isEventByBangdream(String eventPackageName) {
        String[] apps = getResources().getStringArray(R.array.garupa_packages);
        for(String app : apps) {
            if(app.equals(eventPackageName)) {
                return true;
            }
        }

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
