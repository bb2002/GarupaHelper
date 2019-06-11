package kr.saintdev.bandhelp.views.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.saintdev.bandhelp.R;

public class GithubGrass extends LinearLayout {
    private HashMap<Integer, View> GrassMonthView = new HashMap<>();        // 1   -> View
    private HashMap<String, View> GrassDayViews = new HashMap<>();          // 1/1 -> View

    public GithubGrass(Context context) {
        super(context);
        InitView(context, null);
    }

    public GithubGrass(Context context, int startMonth, int endMonth, int orient) {
        super(context);
        InitView(context, startMonth, endMonth, orient);
    }

    public GithubGrass(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context, attrs);
    }

    public GithubGrass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context, attrs);
    }

    public GithubGrass(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitView(context, attrs);
    }

    private void InitView(Context context, int grassStartMonth, int grassEndMonth, int orient) {
        // 올해 달력을 그린다.
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = grassStartMonth; i <= grassEndMonth; i ++) {
            cal.set(Calendar.MONTH, i-1);
            int monthOfLength = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            // Create new view
            View newView = inflater.inflate(R.layout.github_grass_month, null);
            addView(newView);
            GrassMonthView.put(i, newView);

            // Set MonthView
            ((TextView)newView.findViewById(R.id.grass_title)).setText(getMonthResource(i));

            // Set Grass View
            GridLayout grassLayout = newView.findViewById(R.id.grass_container);
            grassLayout.setOrientation(orient);
            if(orient == 0) {
                // hor
                grassLayout.setColumnCount(7);
            } else {
                // ver
                grassLayout.setRowCount(7);
            }

            // Create month in grass
            for(int j = 0; j < monthOfLength; j ++) {
                View grassView = new View(context);
                LayoutParams params = new LayoutParams(dpToPx(15), dpToPx(15));
                params.setMargins(dpToPx(2),dpToPx(2),dpToPx(2),dpToPx(2));
                grassView.setLayoutParams(params);
                grassView.setBackgroundResource(R.color.githubGrass_Gray);
                grassLayout.addView(grassView);

                this.GrassDayViews.put(i + "/" + (j + 1), grassView);
            }
        }
    }

    private void InitView(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);

        int grassStartMonth = 1;
        int grassEndMonth = 1;
        int orient = 0;

        if(attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GithubGrass);
            grassStartMonth = array.getInteger(R.styleable.GithubGrass_start_month, 1);
            grassEndMonth = array.getInteger(R.styleable.GithubGrass_end_month, 1);
            orient = array.getInteger(R.styleable.GithubGrass_orient, 0);
            array.recycle();
        }

        InitView(context, grassStartMonth, grassEndMonth, orient);
    }

    private int dpToPx(int dp) {
        DisplayMetrics met = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(met);

        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, met));
    }

    private int getMonthResource(int month) {
        switch(month) {
            case 1: return R.string.jun;
            case 2: return R.string.feb;
            case 3: return R.string.march;
            case 4: return R.string.april;
            case 5: return R.string.may;
            case 6: return R.string.june;
            case 7: return R.string.july;
            case 8: return R.string.aguest;
            case 9: return R.string.setempber;
            case 10: return R.string.oct;
            case 11: return R.string.nov;
            case 12: return R.string.dec;
            default: return R.string.jun;
        }
    }

    public void setGrassColor(int day, int month, int colorId) {
        View grassMonth = GrassDayViews.get(month + "/" + day);
        if(grassMonth != null) {
            grassMonth.setBackgroundResource(colorId);
        }
    }
}
