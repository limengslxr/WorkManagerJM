package com.sh3h.workmanagerjm.ui.detail;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.util.MapUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class DetailFragment extends ParentFragment implements View.OnClickListener {
    protected DUData duData;
    protected DUTask task;

    protected void initDUTask() {
        ParentActivity activity = (ParentActivity) getActivity();
        duData = activity.getDUData();
        task = duData.getDuTask();
    }

    protected void navigationMap(DUTask task) {
        if (task == null) {
            return;
        }

        double longitude = task.getLongitude();
        double latitude = task.getLatitude();
        Intent intent;
        String address = task.getAddress();
        final double invalid = 200.0d;
        if (Math.abs(longitude) < invalid && Math.abs(latitude) < invalid) {
            if (TextUtil.isNullOrEmpty(address)) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_invalid_address_error);
                return;
            }

            try {
                intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/navi?query=" + address));
                startActivity(intent);
            } catch (Exception e) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_please_check_baidu_map);
            }
            return;
        }

        intent = MapUtil.getMapIntent(getContext());
        if (intent != null) {
            intent.putExtra(ConstantUtil.Map.MAP_STATUS, ConstantUtil.Map.NAVIGATION_MAP);
            intent.putExtra(ConstantUtil.Map.LONGITUDE, longitude);
            intent.putExtra(ConstantUtil.Map.LATITUDE, latitude);
            intent.putExtra(ConstantUtil.Map.ADDRESS, task.getAddress());
            getActivity().startActivity(intent);
        }
    }

}
