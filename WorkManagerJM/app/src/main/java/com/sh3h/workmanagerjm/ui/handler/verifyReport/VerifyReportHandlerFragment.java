package com.sh3h.workmanagerjm.ui.handler.verifyReport;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyReportHandlerFragment extends HandlerFragment {

    private Unbinder unbinder;

    public static VerifyReportHandlerFragment newInstance() {
        return new VerifyReportHandlerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_report_handler, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDUDate();
        initView();
        enableView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemLongClick(int fileType, int position) {

    }

    @Override
    public void temporaryStorage() {

    }

    @Override
    public boolean checkDataInvalid() {
        return false;
    }

    @Override
    public boolean isNeedPicture() {
        return false;
    }

    @Override
    public void saveMaterial(DUMaterial material) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int type, int i) {

    }

    private void initView() {

    }

    private void enableView() {
    }

}
