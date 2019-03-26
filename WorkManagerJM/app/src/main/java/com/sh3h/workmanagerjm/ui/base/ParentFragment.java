package com.sh3h.workmanagerjm.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.util.PermissionUtil;

public abstract class ParentFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtil.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, permissionGrant);
    }

    protected void checkCallPhone() {

    }

    protected void callPhone(String telephone) {
        if (TextUtil.isNullOrEmpty(telephone)) {
            ApplicationsUtil.showMessage(getContext(), R.string.toast_telephone_null);
            return;
        }
        telephone = telephone.replaceAll("[^(0-9)]", TextUtil.EMPTY);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + telephone);
        intent.setData(data);
        startActivity(intent);
    }

    protected void checkCamera() {

    }

    protected PermissionUtil.PermissionGrant permissionGrant = (requestCode) -> {
        switch (requestCode) {
            case PermissionUtil.CODE_CALL_PHONE:
                checkCallPhone();
                break;
            case PermissionUtil.CODE_CAMERA:
                checkCamera();
                break;
            default:
                break;
        }
    };

}
