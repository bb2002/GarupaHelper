package kr.saintdev.bandhelp.core.libs;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class PermissionUtility {
    /**
     * @Date 05.24 2019
     * @param context   => context
     * @param clazz     => Accessibility Serivce Class.
     * @return  if accessibility service is running, return true.
     */
    public static boolean isAccessibilityServiceEnable(Context context, Class<? extends AccessibilityService> clazz) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enableServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for(AccessibilityServiceInfo service : enableServices) {
            ServiceInfo info = service.getResolveInfo().serviceInfo;
            if(info.packageName.equals(context.getPackageName()) && info.name.equals(clazz.getName())) {
                // In App, In Service is runnable.
                return true;
            }
        }

        return false;
    }

    /**
     * @Date 05.25 2019
     * Check overlay permission.
     */
    public static boolean hadOverlayPermission(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }

        return true;
    }

    public static int openOverlayPermissionSetting(Activity activity) {
        if(!hadOverlayPermission(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, 0x9);
        }

        return 0x9;
    }

    public static void openAccessibilityServiceSettings(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
