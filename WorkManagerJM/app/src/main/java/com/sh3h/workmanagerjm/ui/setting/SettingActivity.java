package com.sh3h.workmanagerjm.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.util.SystemUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sh3h.workmanagerjm.R.id.fs_cv_network;

public class SettingActivity extends ParentActivity implements SettingMvpView, View.OnClickListener {

    @Inject
    SettingPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.setting_tv_version_info)
    TextView mVersion;

    @BindView(fs_cv_network)
    CardView cardView;

    /*@BindView(R.id.switch_picture)
    Switch aSwitch;*/

    private Unbinder unbinder;
    private EditText baseUriTextView;
    private EditText baseUriReverseTextView;
    private CheckBox reservedAddressCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        presenter.attachView(this);
        getIntentData();
        initMyToolBar();
        initData();
        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initData() {
        //版本信息
        String version = SystemUtil.getVersionName(MainApplication.get(this));
        mVersion.setText(version);
        cardView.setOnClickListener(this);

        //presenter.getPicQuality();
    }

    private void initMyToolBar() {
        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle(R.string.title_setting);
        setSupportActionBar(mToolbar);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        saveUserInfo(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fs_cv_network:
                setNetWork();
                break;
            default:
        }
    }

    private void setNetWork() {
        String baseURI = TextUtil.getString(presenter.getBaseUri());
        String reverseBrokeURL = TextUtil.getString(presenter.getReverseBaseUri());
        boolean isUsingReservedAddress = presenter.isUsingReservedAddress();

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.text_setting_network)
                .customView(R.layout.dialog_setting_network, true)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog1, which) -> {
                    presenter.saveNetWorkSetting(baseUriTextView.getText().toString(),
                            baseUriReverseTextView.getText().toString(),
                            reservedAddressCheckBox.isChecked());
                    hideKeyBoard();
                })
                .build();

        if (dialog.getCustomView() != null) {
            baseUriTextView = (EditText) dialog.getCustomView().findViewById(R.id.et_data_address);
            baseUriReverseTextView = (EditText) dialog.getCustomView().findViewById(R.id.et_reserved_data_address);
            reservedAddressCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.cb_is_reserved_address);

            baseUriTextView.setText(baseURI);
            baseUriTextView.setSelection(baseURI.length());

            baseUriReverseTextView.setText(reverseBrokeURL);
            baseUriReverseTextView.setSelection(reverseBrokeURL.length());

            reservedAddressCheckBox.setChecked(isUsingReservedAddress);
        }
        dialog.show();
    }

    @Override
    public void showMessage(String message) {
        if (!TextUtil.isNullOrEmpty(message)) {
            ApplicationsUtil.showMessage(this, message);
        }
    }

    @Override
    public void showMessage(int message) {
        ApplicationsUtil.showMessage(this, message);
    }

    /*@Override
    public void updatePicQuality(Boolean aBoolean) {
        aSwitch.setChecked(aBoolean);
    }*/
}
