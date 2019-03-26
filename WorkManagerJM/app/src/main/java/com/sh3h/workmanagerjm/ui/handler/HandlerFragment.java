package com.sh3h.workmanagerjm.ui.handler;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.adapter.OnItemClickListener;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.myinterface.OnItemLongClickListener;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class HandlerFragment extends ParentFragment implements View.OnClickListener,
        OnItemClickListener, OnItemLongClickListener, AdapterView.OnItemSelectedListener {
    protected DUData data;
    protected DUTask task;
    protected DUHandle handle;
    //这个字段用来判断处理界面是否允许用户修改界面内容
    protected boolean permitWrite;

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void initDUDate() {
        ParentActivity activity = (ParentActivity) getActivity();
        data = activity.getDUData();
        task = data.getDuTask();
        permitWrite = data.isPermitWrite();
        handle = data.getActualHandle();
    }

    public abstract void temporaryStorage();

    //检查数据有效性
    public abstract boolean checkDataInvalid();

    public abstract boolean isNeedPicture();

    public DUHandle getHandle() {
        return handle;
    }

    public abstract void saveMaterial(DUMaterial material);

    public void scanResult(String result) {
        Log.i("abcd", result);
    }

    protected void initSpinner(Spinner spinner, int value, List<DUWord> list) {
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(list);
        spinner.setAdapter(adapter);
        if (list == null || list.size() == 0) {
            return;
        }

        int position = 0;
        int index = 0;
        for (DUWord word : list) {
            if (String.valueOf(value).equals(word.getValue())) {
                position = index;
                break;
            } else {
                index++;
            }
        }
        spinner.setSelection(position);
        adapter.notifyDataSetChanged();
    }

    protected void initSpinner(Spinner spinner, String valve, List<DUWord> list) {
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(list);
        spinner.setAdapter(adapter);
        if (list == null || list.size() == 0) {
            return;
        }

        if (TextUtil.isNullOrEmpty(valve)) {
            spinner.setSelection(0);
            adapter.notifyDataSetChanged();
            return;
        }

        int position = 0;
        int index = 0;
        for (DUWord word : list) {
            if (valve.equals(word.getName())) {
                position = index;
                break;
            } else {
                index++;
            }
        }

        spinner.setSelection(position);
        adapter.notifyDataSetChanged();
    }

    protected void scan() {
        ManagerActivity activity = (ManagerActivity) getActivity();
        activity.scan();
    }

    protected boolean isNeedCheckDataInvalid(int value) {
        return value != ConstantUtil.NoCheckResolveResult.DUPLICATION_REPAIR
                && value != ConstantUtil.NoCheckResolveResult.NO_NEED_RESOLVE
                && value != ConstantUtil.NoCheckResolveResult.TRANSFORM_RESOLVE;
    }

}
