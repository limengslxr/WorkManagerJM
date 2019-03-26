package com.sh3h.workmanagerjm.ui.handler.inputMaterial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.sh3h.dataprovider.condition.MaterialCondition;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.MaterialSpinnerAdapter;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputMaterialHandleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputMaterialHandleFragment extends ParentFragment implements View.OnTouchListener,
        AdapterView.OnItemSelectedListener, InputMaterialMvpView {

    @BindView(R.id.sp_material_type)
    Spinner spType;
    @BindView(R.id.sp_material_name)
    Spinner spName;
    @BindView(R.id.sp_material_standard)
    Spinner spStandard;
    @BindView(R.id.sp_material_unit)
    Spinner spUnit;
    @BindView(R.id.et_material_number)
    EditText etNumber;
    @Inject
    InputMaterialPresenter presenter;
    private MaterialSpinnerAdapter typeAdapter, nameAdapter, standardAdapter, unitAdapter;
    private List<String> typeList, nameList, standardList, unitList;
    private DUMaterial material;
    private Unbinder unbinder;
    private boolean filterSpinner;

    public static InputMaterialHandleFragment newInstance(DUMaterial material) {
        InputMaterialHandleFragment fragment = new InputMaterialHandleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ConstantUtil.Parcel.DUMATERIAL, material);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        material = savedInstanceState == null ? getArguments().getParcelable(ConstantUtil.Parcel.DUMATERIAL)
                : savedInstanceState.getParcelable(ConstantUtil.Parcel.DUMATERIAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_material_handle, container, false);
        ManagerActivity activity = (ManagerActivity) getActivity();
        activity.getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        initView(material);
        enableView();
        setOnListener();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantUtil.Parcel.DUMATERIAL, material);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        unbinder.unbind();
    }

    public boolean checkDataInvalid() {
        String count = etNumber.getText().toString();
        if (TextUtil.isNullOrEmpty(count)) {
            ApplicationsUtil.showMessage(getContext(), R.string.toast_material_count_is_null);
            return false;
        }

        material.setCount(Double.parseDouble(count));
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (filterSpinner) {
            return;
        }
        filterSpinner = true;

        switch (parent.getId()) {
            case R.id.sp_material_type:
                material.setType(typeList.get(spType.getSelectedItemPosition()));
                presenter.getMaterial(material, MaterialCondition.GET_NAME);
                break;
            case R.id.sp_material_name:
                material.setName(nameList.get(spName.getSelectedItemPosition()));
                presenter.getMaterial(material, MaterialCondition.GET_STANDARD);
                break;
            case R.id.sp_material_standard:
                material.setSpec(standardList.get(spStandard.getSelectedItemPosition()));
                presenter.getMaterial(material, MaterialCondition.GET_UNIT);
                break;
            case R.id.sp_material_unit:
                material.setUnit(unitList.get(spUnit.getSelectedItemPosition()));
                setCountType();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.sp_material_type:
            case R.id.sp_material_name:
            case R.id.sp_material_standard:
            case R.id.sp_material_unit:
                filterSpinner = false;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void getTypeResult(List<DUMaterial> materials) {
        typeList.clear();
        boolean find = false;
        int position = 0;
        for (DUMaterial material : materials) {
            if (!find) {
                if (material.getType().equals(this.material.getType())) {
                    find = true;
                } else {
                    position++;
                }
            }
            typeList.add(material.getType());
        }
        typeAdapter.notifyDataSetChanged();
        position = find ? position : 0;
        int originPosition = spType.getSelectedItemPosition();
        filterSpinner = false;
        spType.setSelection(position);
        if (originPosition == position) {
            material.setType(typeList.get(position));
            presenter.getMaterial(material, MaterialCondition.GET_NAME);
        }
    }

    @Override
    public void getNameResult(List<DUMaterial> materials) {
        String type = this.material.getType();
        if (ConstantUtil.Default.MATERIAL_NAME.equals(type)) {
            Collections.sort(materials, (o1, o2) -> {
                if (ConstantUtil.Default.MATERIAL_NAME.equals(o1.getName())) {
                    return -1;
                }

                if (ConstantUtil.Default.MATERIAL_NAME.equals(o2.getName())) {
                    return 1;
                }
                return 0;
            });
        }

        nameList.clear();
        boolean find = false;
        int position = 0;
        for (DUMaterial material : materials) {
            if (!find) {
                if (material.getName().equals(this.material.getName())) {
                    find = true;
                } else {
                    position++;
                }
            }
            nameList.add(material.getName());
        }
        nameAdapter.notifyDataSetChanged();
        position = find ? position : 0;
        int originPosition = spName.getSelectedItemPosition();
        filterSpinner = false;
        spName.setSelection(position);
        if (position == originPosition) {
            material.setName(nameList.get(position));
            presenter.getMaterial(material, MaterialCondition.GET_STANDARD);
        }
    }

    @Override
    public void getStandardResult(List<DUMaterial> materials) {
        Collections.sort(materials, (o1, o2) -> {
            String one = o1.getSpec();
            String two = o2.getSpec();
            if (TextUtil.isNullOrEmpty(one) || TextUtil.isNullOrEmpty(two)) {
                return 0;
            }

            return NumberUtil.transformNumber(one) - NumberUtil.transformNumber(two);
        });

        standardList.clear();
        boolean find = false;
        int position = 0;
        for (DUMaterial material : materials) {
            if (!find) {
                if (material.getSpec().equals(this.material.getSpec())) {
                    find = true;
                } else {
                    position++;
                }
            }
            standardList.add(material.getSpec());
        }
        standardAdapter.notifyDataSetChanged();
        position = find ? position : 0;
        int originPosition = spStandard.getSelectedItemPosition();
        filterSpinner = false;
        spStandard.setSelection(position);
        if (originPosition == position) {
            material.setSpec(standardList.get(position));
            presenter.getMaterial(material, MaterialCondition.GET_UNIT);
        }
    }

    @Override
    public void getUnitResult(List<DUMaterial> materials) {
        unitList.clear();
        boolean find = false;
        int position = 0;
        for (DUMaterial material : materials) {
            if (!find) {
                if (material.getUnit().equals(this.material.getUnit())) {
                    this.material.setMaterialNo(material.getMaterialNo());
                    find = true;
                } else {
                    position++;
                    this.material.setMaterialNo(null);
                }
            }
            unitList.add(material.getUnit());
        }
        unitAdapter.notifyDataSetChanged();
        position = find ? position : 0;
        int originPosition = spUnit.getSelectedItemPosition();
        filterSpinner = false;
        spUnit.setSelection(position);
        setCountType();
        if (originPosition == position) {
            material.setUnit(unitList.get(position));
        }
    }

    public void initView(DUMaterial material) {
        this.material = material == null ? new DUMaterial() : material;

        typeAdapter = typeAdapter == null ? new MaterialSpinnerAdapter() : typeAdapter;
        typeList = typeList == null ? new ArrayList<>() : typeList;
        initOneSpinner(typeAdapter, typeList, spType);

        nameAdapter = nameAdapter == null ? new MaterialSpinnerAdapter() : nameAdapter;
        nameList = nameList == null ? new ArrayList<>() : nameList;
        initOneSpinner(nameAdapter, nameList, spName);

        standardAdapter = standardAdapter == null ? new MaterialSpinnerAdapter() : standardAdapter;
        standardList = standardList == null ? new ArrayList<>() : standardList;
        initOneSpinner(standardAdapter, standardList, spStandard);

        unitAdapter = unitAdapter == null ? new MaterialSpinnerAdapter() : unitAdapter;
        unitList = unitList == null ? new ArrayList<>() : unitList;
        initOneSpinner(unitAdapter, unitList, spUnit);

        double count = this.material.getCount();
        etNumber.setText(Math.abs(count) < ConstantUtil.SMALL_NUMBER
                ? TextUtil.EMPTY : String.valueOf(this.material.getCount()));

        presenter.getMaterial(this.material, MaterialCondition.GET_TYPE);
        filterSpinner = false;
    }

    public DUMaterial provideMaterial() {
        return material;
    }

    private void enableView() {
        ParentActivity activity = (ParentActivity) getActivity();
        DUData data = activity.getDUData();
        boolean permitWrite = data.isPermitWrite();
        spType.setEnabled(permitWrite);
        spName.setEnabled(permitWrite);
        spStandard.setEnabled(permitWrite);
        spUnit.setEnabled(permitWrite);
        etNumber.setEnabled(permitWrite);
    }

    private void setOnListener() {
        spType.setOnItemSelectedListener(this);
        spName.setOnItemSelectedListener(this);
        spStandard.setOnItemSelectedListener(this);
        spUnit.setOnItemSelectedListener(this);
        spType.setOnTouchListener(this);
        spName.setOnTouchListener(this);
        spStandard.setOnTouchListener(this);
        spUnit.setOnTouchListener(this);
    }

    private void initOneSpinner(MaterialSpinnerAdapter adapter, List<String> list, Spinner spinner) {
        adapter.setList(list);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setCountType() {
        String unit = unitList.get(spUnit.getSelectedItemPosition());
        etNumber.setInputType("米".equals(unit) ?
                InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER
                : InputType.TYPE_CLASS_NUMBER);
    }

}
