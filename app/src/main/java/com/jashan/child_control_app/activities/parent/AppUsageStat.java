package com.jashan.child_control_app.activities.parent;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.jashan.child_control_app.model.UsageStatsWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

public class AppUsageStat  {
    UsageStatsManager mUsageStatsManager;
    private final Context context;
    private PackageManager packageManager;

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;


    public AppUsageStat(Context context){
        this.context = context;
        mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        packageManager = context.getPackageManager();
    }

    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTimeInMillis();
    }

    public List<UsageStatsWrapper> retrieveUsageStats() {
        if (!checkForPermission(context)) {
            //view.onUserHasNoPermission()
        }

        List<String> installedApps = getInstalledAppList();
//        Map<String, UsageStats> usageStats = mUsageStatsManager.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());
//        List<UsageStats> stats = new ArrayList<>();
//        stats.addAll(usageStats.values());
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,getStartTime(), System.currentTimeMillis());
        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps, stats);
        //view.onUsageStatsRetrieved(finalList);
        return finalList;
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }

    private List<String> getInstalledAppList(){
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos){
            installedApps.add(info.packageName);
        }
        return installedApps;
    }

    private List<UsageStatsWrapper> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
        List<UsageStatsWrapper> list = new ArrayList<>();
        //int count=0;
        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                    added = true;
                    list.add(fromUsageStat(stat));
                }
            }
            if (!added) {
                list.add(fromUsageStat(name));
            }

        }
        Collections.sort(list);
        int count=0;
        List<UsageStatsWrapper> topFiveApp = new ArrayList<>();
        for(UsageStatsWrapper i : list){
            topFiveApp.add(i);
            count++;
            if(count==5)
                break;
        }

        return topFiveApp;
    }

    private UsageStatsWrapper fromUsageStat(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return new UsageStatsWrapper(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private UsageStatsWrapper fromUsageStat(UsageStats usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
            return new UsageStatsWrapper(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }


}


