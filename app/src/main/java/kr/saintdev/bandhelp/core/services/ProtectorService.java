package kr.saintdev.bandhelp.core.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import kr.saintdev.bandhelp.R;

public class ProtectorService extends Service {
    private View overlayView = null;
    private WindowManager wmManager = null;

    public ProtectorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.overlayView = inflater.inflate(R.layout.game_overlay, null);
            this.overlayView.setOnTouchListener(OnOverlayViewClickListener);
            this.wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            // Add view in overlay.
            addOverlayView(false);

            // Run data force
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if(pref.getBoolean("lte_mode", false)) {
                // Force LTE RUN.
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        removeOverlayView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private View.OnTouchListener OnOverlayViewClickListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                // Close overlay view.
                removeOverlayView();
                addOverlayView(true);
            }

            return true;
        }
    };

    /**
     * @Date 05.24 2019
     * Remove view overlay.
     */
    private void removeOverlayView() {
        try {
            wmManager.removeView(overlayView);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @Date 05.25 2019
     * Add view overlay
     */
    private void addOverlayView(boolean bHidden) {
        try {
            this.wmManager.addView(this.overlayView, getParams(bHidden));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @Date 05.24 2019
     * @param dp    => to px
     * @return  => calc px
     */
    private int dpToPx(int dp) {
        DisplayMetrics met = new DisplayMetrics();
        wmManager.getDefaultDisplay().getMetrics(met);

        return dp * (met.densityDpi / 160);
    }

    /**
     * @Date 05.25 2019
     * @param bHidden   => is hidden mode?
     * @return  => Params
     */
    private WindowManager.LayoutParams getParams(boolean bHidden) {
        int flag;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            flag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                dpToPx(24),
                flag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.LEFT | Gravity.TOP;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        params.screenOrientation = Integer.parseInt(pref.getString("display_orient", "0"));

        if(bHidden) {
            params.width = 0;
            params.height = 0;
            params.alpha = 0f;
        }

        return params;
    }
}
