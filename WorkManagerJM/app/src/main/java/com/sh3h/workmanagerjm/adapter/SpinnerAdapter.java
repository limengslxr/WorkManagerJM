package com.sh3h.workmanagerjm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.R;

import java.util.List;

/**
 * spinner适配器
 */
public class SpinnerAdapter extends BaseAdapter {
    private List<DUWord> list;

    public void setList(List<DUWord> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_item_spinner);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.textView.setText(list.get(i).getName());
        return view;
    }

    private class Holder {
        private TextView textView;
    }
}
