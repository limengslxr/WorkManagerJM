package com.sh3h.workmanagerjm.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by LiMeng on 2017/4/25.
 */

public class MapUtil {
    public static Intent getMapIntent(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setClassName("com.sh3h.citymap", "com.sh3h.citymap.ui.map.ArcGisMapActivity");
        PackageManager pm = context.getPackageManager();

        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(intent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        ResolveInfo info = resolveInfo.get(0);
        String packageName = info.activityInfo.packageName;
        String className = info.activityInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(intent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

}
