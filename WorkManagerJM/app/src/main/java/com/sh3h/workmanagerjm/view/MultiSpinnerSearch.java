package com.sh3h.workmanagerjm.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUSearchPerson;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;

import java.util.ArrayList;
import java.util.List;

public class MultiSpinnerSearch extends AppCompatSpinner implements OnCancelListener{
    private static final String TAG = MultiSpinnerSearch.class.getSimpleName();
    private List<DUSearchPerson> items;
    private String defaultText = TextUtil.EMPTY;
    private SpinnerListener listener;
    private MyAdapter adapter;
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;
    private boolean isMultiple;

    public MultiSpinnerSearch(Context context) {
        super(context);
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }

        String spinnerText = spinnerBuffer.toString();
        spinnerText = spinnerText.length() > 2
                ? spinnerText.substring(0, spinnerText.length() - 2) : defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(),
                R.layout.item_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null){
            adapter.notifyDataSetChanged();
        }

        listener.onItemsSelected(items);
    }

    @Override
    public boolean performClick() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null){
            return super.performClick();
        }

        builder = new AlertDialog.Builder(getContext());

        builder.setTitle(isMultiple ? R.string.text_please_select_assist_person :
                R.string.text_please_select_accept_person);
        View view = inflater.inflate(R.layout.dialog_spinner_search, null);
        builder.setView(view);

        ListView listView = (ListView) view.findViewById(R.id.alertSearchListView);
        listView.setChoiceMode(isMultiple
                ? ListView.CHOICE_MODE_MULTIPLE : ListView.CHOICE_MODE_SINGLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items);
        listView.setAdapter(adapter);

        TextView emptyText = (TextView) view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        EditText editText = (EditText) view.findViewById(R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (isMultiple){
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel());
        }
        builder.setOnCancelListener(this);
        ad = builder.show();
        return true;
    }

    public void setItems(List<DUSearchPerson> items, int position, boolean isMultiple,
                         SpinnerListener listener) {
        this.items = items;
        this.isMultiple = isMultiple;
        this.listener = listener;

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }
        if (spinnerBuffer.length() > 2)
            defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(),
                R.layout.item_spinner, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            items.get(position).setSelected(true);
            //listener.onItemsSelected(items);
            onCancel(null);
        }
    }

    private void resetData(){
        if (items == null || items.size() == 0){
            return;
        }

        for (DUSearchPerson person : items){
            person.setSelected(false);
        }
    }

    //Adapter Class
    private class MyAdapter extends BaseAdapter implements OnClickListener, Filterable{

        private List<DUSearchPerson> arrayList;
        private List<DUSearchPerson> mOriginalValues; // Original Values
        private LayoutInflater inflater;

        MyAdapter(Context context, List<DUSearchPerson> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.item_listview_multiple,  parent, false);
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cb_multi_spinner);
            DUSearchPerson data = arrayList.get(position);
            checkBox.setChecked(data.isSelected());
            checkBox.setText(data.getName());
            checkBox.setOnClickListener(this);
            checkBox.setTag(Integer.MAX_VALUE, position);
            return convertView;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() != R.id.cb_multi_spinner){
                Log.i(TAG, "error id");
                return;
            }

            CheckBox checkBox = (CheckBox) view;
            int position = (int) checkBox.getTag(Integer.MAX_VALUE);
            DUSearchPerson data = arrayList.get(position);

            if (isMultiple){
                data.setSelected(checkBox.isChecked());
                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                notifyDataSetChanged();
                return;
            }

            Log.i(TAG, "simple On Click Selected Item : " + data.getName() + " : " + data.isSelected());
            MultiSpinnerSearch.this.resetData();
            int len = arrayList.size();
            for (int i = 0; i < len; i++) {
                arrayList.get(i).setSelected(i == position);
            }
            ad.dismiss();
            MultiSpinnerSearch.this.onCancel(ad);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (List<DUSearchPerson>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<DUSearchPerson> filter = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    /*
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                filter.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = filter.size();
                        results.values = filter;
                    }
                    return results;
                }
            };
        }
    }
}