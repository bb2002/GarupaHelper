package kr.saintdev.bandhelp.core.libs;

import android.app.ActivityManager;
import android.content.Context;
import kr.saintdev.bandhelp.core.services.DetectService;
import kr.saintdev.bandhelp.core.services.ProtectorService;


public class ServiceUtility {
    /**
     * @Date 05.24 2019
     * @return Bangdream Detect service running stat
     * Check Bangdream detect service status.
     */
    public static boolean isBangdreamDetectServiceRunning(Context context) {
        return isTargetServiceRunning(DetectService.class, context);
    }

    /**
     * @Date 05.24 2019
     * @return Bangdream Protect service running stat
     * Check Bangdream Protect service status.
     */
    public static boolean isBangdreamProtectorServiceRunning(Context context) {
        return isTargetServiceRunning(ProtectorService.class, context);
    }

    /**
     * @Date 05.24 2019
     * @param clazz     => Check class
     * @param context   => Context
     * @return          => Service running status.
     */
    private static boolean isTargetServiceRunning(Class clazz, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(clazz.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
