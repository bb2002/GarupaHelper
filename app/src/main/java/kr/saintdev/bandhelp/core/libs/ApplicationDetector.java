package kr.saintdev.bandhelp.core.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;

import java.util.List;

public class ApplicationDetector extends AsyncTask<String, Void, Boolean[]> {
    private Context context = null;
    private OnApplicationSearchListener listener = null;

    public ApplicationDetector(Context context, OnApplicationSearchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean[] doInBackground(String... targetApps) {
        Boolean[] searchResult = new Boolean[targetApps.length];

        List<PackageInfo> installedApps = context.getPackageManager().getInstalledPackages(0);
        for(int i = 0; i < installedApps.size(); i ++) {
            for(int j = 0; j < targetApps.length; j ++) {
                if(targetApps[j].equals(installedApps.get(i).packageName)) {
                    searchResult[j] = true;
                    break;
                }
                if(searchResult[j] == null) {
                    searchResult[j] = false;
                }
            }
        }

        return searchResult;
    }

    @Override
    protected void onPostExecute(Boolean[] booleans) {
        super.onPostExecute(booleans);
        if(listener != null) {
            listener.onResult(booleans);
        }
    }

    public interface OnApplicationSearchListener {
        void onResult(Boolean[] result);
    }
}
