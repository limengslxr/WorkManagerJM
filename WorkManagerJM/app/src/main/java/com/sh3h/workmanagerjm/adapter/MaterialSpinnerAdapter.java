package com.sh3h.workmanagerjm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sh3h.workmanagerjm.R;

import java.util.List;

/**
 * Created by LiMeng on 2017/6/8.
 */

public class MaterialSpinnerAdapter extends BaseAdapter{
    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MaterialSpinnerAdapter.Holder holder;
        if (view == null) {
            holder = new MaterialSpinnerAdapter.Holder();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner, parent, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_item_spinner);
            view.setTag(holder);
        } else {
            holder = (MaterialSpinnerAdapter.Holder) view.getTag();
        }
        holder.textView.setText(list.get(position));
        return view;
    }

    private class Holder {
        private TextView textView;
    }

}
