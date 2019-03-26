package com.sh3h.workmanagerjm.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.dispatch.DispatchActivity;
import com.sh3h.workmanagerjm.ui.list.ListActivity;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.ui.search.SearchActivity;
import com.sh3h.workmanagerjm.ui.setting.SettingActivity;
import com.sh3h.workmanagerjm.ui.statistics.StatisticsActivity;
import com.sh3h.workmanagerjm.ui.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends ParentActivity {

    private static final int GRIDCOLUMN = 3;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        initMyToolBar();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                toSettingActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 跳转至设置界面
     */
    private void toSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    private void initMyToolBar() {
        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle("jack");
        setSupportActionBar(mToolbar);
    }

    private void initData() {
        List<ItemData> datas = new ArrayList<>();
        datas.add(new ItemData("表务工单", R.mipmap.ic_home_meter_task, "1"));
        datas.add(new ItemData("报装工单", R.mipmap.ic_home_meter_install, "1"));
        datas.add(new ItemData("催缴工单", R.mipmap.ic_home_call_pay, "1"));
        datas.add(new ItemData("热线工单", R.mipmap.ic_home_hot, "1"));
        datas.add(new ItemData("内部工单", R.mipmap.ic_home_inside, "1"));
        datas.add(new ItemData("上报工单", R.mipmap.ic_home_report, "1"));
        datas.add(new ItemData("历史工单", R.mipmap.ic_home_history, "1"));
        datas.add(new ItemData("工单查询", R.mipmap.ic_home_select, "1"));
        datas.add(new ItemData("工单派遣", R.mipmap.ic_home_dispatch, "1"));
        datas.add(new ItemData("统计", R.mipmap.ic_home_statistics, "1"));
        datas.add(new ItemData("工单审核", R.mipmap.ic_home_verify, "1"));
        datas.add(new ItemData("抢单", R.mipmap.ic_home_grab_work, "1"));

        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(R.layout.gridview_item, datas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, GRIDCOLUMN));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
            implements View.OnClickListener {

        private List<ItemData> itemDatas;
        private int layoutId;

        MyRecyclerViewAdapter(int layoutId, List<ItemData> itemDatas) {
            this.layoutId = layoutId;
            this.itemDatas = itemDatas;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemData itemData = itemDatas.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(itemData.data);
            viewHolder.imageView.setImageResource(itemData.icon);
            viewHolder.linearLayout.setTag(Integer.MAX_VALUE, position);
            viewHolder.linearLayout.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return itemDatas.size();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.apk_item:
                    int position = (int) view.getTag(Integer.MAX_VALUE);
                    itemClicked(position);
                    break;
                default:
                    break;
            }
        }
    }

    private void itemClicked(int position) {
        Intent intent = null;
        String entrance = null;
        switch (position) {
            case 0:     //表务工单
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.ORDERTASKBW;
                break;
            case 1:     //报装工单
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance =  ConstantUtil.TaskEntrance.METER_INSTALL;
                break;
            case 2:     //催缴
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.CALL_PAY;
                break;
            case 3:     //热线
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.HOT;
                break;
            case 4:     //内部
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.INSIDE;
                break;
            case 5:     //上报工单
                intent = new Intent(this, ManagerActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.REPORT;
                break;
            case 6:     //历史工单
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.HISTORY;
                break;
            case 7:     //查询
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.SEARCH;
                break;
            case 8:     //派单
                intent = new Intent(this, DispatchActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.DISPATCH;
                break;
            case 9:     //统计
                intent = new Intent(this, StatisticsActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "admin");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.DISPATCH;
                break;
            case 10:     //审核
                intent = new Intent(this, ListActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "admin");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                entrance = ConstantUtil.TaskEntrance.VERIFY;
                break;
            case 11:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(ConstantUtil.TaskEntrance.ACCOUNT, "189");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_ID, "6001");
                intent.putExtra(ConstantUtil.TaskEntrance.USER_NAME, "区俏婷");
                intent.putExtra(ConstantUtil.TaskEntrance.PARAMES, "relUrl:app.m.html#/mobile/grabWork;");
                startActivity(intent);
                return;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(ConstantUtil.TaskEntrance.PARAMES, entrance);
            startActivity(intent);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.apk_name);
            imageView = (ImageView) itemView.findViewById(R.id.apk_icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.apk_item);
        }
    }

    private class ItemData {
        String data;
        int icon;
        String count;

        ItemData(String data, int icon, String count) {
            this.data = data;
            this.icon = icon;
            this.count = count;
        }
    }
}
