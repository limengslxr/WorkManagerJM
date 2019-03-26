package com.sh3h.workmanagerjm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.myinterface.OnItemLongClickListener;

import java.util.List;

/**
 * Created by LiMeng on 2017/6/8.
 */
public class InputMaterialAdapter extends RecyclerView.Adapter<InputMaterialAdapter.MaterialHolder>
        implements View.OnClickListener, View.OnLongClickListener{
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private List<DUMaterial> materials;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setMaterials(List<DUMaterial> materials) {
        this.materials = materials;
    }

    @Override
    public MaterialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_input_material, parent, false);
        return new MaterialHolder(view);
    }

    @Override
    public void onBindViewHolder(MaterialHolder holder, int position) {
        DUMaterial material = materials.get(position);
        StringBuilder builder = new StringBuilder();
        builder.append(material.getType());
        builder.append("  ");
        builder.append(material.getName());
        builder.append("  ");
        builder.append(material.getSpec());
        builder.append("  ");
        builder.append(material.getUnit());
        builder.append("  ");
        builder.append(material.getCount());
        holder.tvMaterial.setText(builder.toString());
        holder.tvMaterial.setOnClickListener(this);
        holder.tvMaterial.setOnLongClickListener(this);
        holder.tvMaterial.setTag(Integer.MAX_VALUE, position);
    }

    @Override
    public int getItemCount() {
        return  materials == null ? 0 : materials.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_item_input_material:
                if (clickListener != null){
                    int position = (int) v.getTag(Integer.MAX_VALUE);
                    clickListener.onItemClick(0, position);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.tv_item_input_material:
                if (longClickListener != null){
                    int position = (int) v.getTag(Integer.MAX_VALUE);
                    longClickListener.onItemLongClick(0, position);
                }
                break;
            default:
                break;
        }
        return false;
    }

    class MaterialHolder extends RecyclerView.ViewHolder{
        private TextView tvMaterial;
        MaterialHolder(View itemView) {
            super(itemView);
            tvMaterial = (TextView) itemView.findViewById(R.id.tv_item_input_material);
        }
    }

}
