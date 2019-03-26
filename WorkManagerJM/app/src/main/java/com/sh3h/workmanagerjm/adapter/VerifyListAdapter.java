package com.sh3h.workmanagerjm.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDownloadVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.util.TransformUtil;

/**
 * Created by LiMeng on 2018/2/9.
 * 审核
 */
public class VerifyListAdapter extends AbstractAdapter implements CompoundButton.OnCheckedChangeListener{

    @Override
    public int getItemViewType(int position) {
        if (position < list.size()) {
            return list.get(position).getApplyType();
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ConstantUtil.ApplyType.DEFAULT:
                return new RejectHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_reject_list, parent, false));
            case ConstantUtil.ApplyType.DELAY:
                return new DelayHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_delay_list, parent, false));
            case ConstantUtil.ApplyType.HANG_UP:
                return new HangUpHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_hang_up_list, parent, false));
            case ConstantUtil.ApplyType.RECOVERY:
                return new RecoveryHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_recovery_list, parent, false));
            case ConstantUtil.ApplyType.ASSIST:
                return new AssistHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_assist_list, parent, false));
            case ConstantUtil.ApplyType.REPORT:
                return new ReportHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_report_list, parent, false));
            default:
                return new RejectHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_reject_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DUData duData = list.get(position);
        DUTask task = duData.getDuTask();
        DUHandle duHandle = duData.getHandle();
        if (task == null || duHandle == null){
            return;
        }

        DUDownloadVerifyHandle handle = duHandle.toDUDownloadVerifyHandle();
        if (viewHolder instanceof RejectHolder) {
            RejectHolder rejectHolder = (RejectHolder) viewHolder;
            rejectHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            rejectHolder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(rejectHolder.itemView.getContext(), task));
            rejectHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            rejectHolder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            rejectHolder.tvResolvePerson.setText(duData.getHostPerson());
            rejectHolder.tvRejectPerson.setText(handle.getApplyPerson());
            rejectHolder.tvRejectReason.setText(handle.getApplyReason());
            rejectHolder.tvRejectDate.setText(handle.getStrApplyTime());

            rejectHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
            rejectHolder.cbCheck.setChecked(duData.isCheck());
            rejectHolder.cbCheck.setOnCheckedChangeListener(this);

            rejectHolder.cardView.setOnClickListener(this);
            rejectHolder.cardView.setTag(Integer.MAX_VALUE, position);
        } else if (viewHolder instanceof DelayHolder){
            DelayHolder delayHolder = (DelayHolder) viewHolder;
            delayHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            delayHolder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(delayHolder.itemView.getContext(), task));
            delayHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            delayHolder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            delayHolder.tvResolvePerson.setText(duData.getHostPerson());
            delayHolder.tvApplyPerson.setText(handle.getApplyPerson());
            delayHolder.tvApplyReason.setText(handle.getApplyReason());
            delayHolder.tvCompleteDate.setText(task.getStrEndDate());
            delayHolder.tvCompleteEndDate.setText(handle.getStrDelayTime());
            delayHolder.tvApplyDate.setText(handle.getStrApplyTime());

            delayHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
            delayHolder.cbCheck.setChecked(duData.isCheck());
            delayHolder.cbCheck.setOnCheckedChangeListener(this);

            delayHolder.cardView.setOnClickListener(this);
            delayHolder.cardView.setTag(Integer.MAX_VALUE, position);
        } else if (viewHolder instanceof HangUpHolder){
            HangUpHolder hangUpHolder = (HangUpHolder) viewHolder;
            hangUpHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            hangUpHolder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(hangUpHolder.itemView.getContext(), task));
            hangUpHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            hangUpHolder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            hangUpHolder.tvResolvePerson.setText(duData.getHostPerson());
            hangUpHolder.tvApplyPerson.setText(handle.getApplyPerson());
            hangUpHolder.tvApplyReason.setText(handle.getApplyReason());
            hangUpHolder.tvApplyDate.setText(handle.getStrApplyTime());

            hangUpHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
            hangUpHolder.cbCheck.setChecked(duData.isCheck());
            hangUpHolder.cbCheck.setOnCheckedChangeListener(this);

            hangUpHolder.cardView.setOnClickListener(this);
            hangUpHolder.cardView.setTag(Integer.MAX_VALUE, position);
        } else if (viewHolder instanceof RecoveryHolder){
            RecoveryHolder recoveryHolder = (RecoveryHolder) viewHolder;
            recoveryHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            recoveryHolder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(recoveryHolder.itemView.getContext(), task));
            recoveryHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            recoveryHolder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            recoveryHolder.tvResolvePerson.setText(duData.getHostPerson());
            recoveryHolder.tvApplyPerson.setText(handle.getApplyPerson());
            recoveryHolder.tvApplyReason.setText(handle.getApplyReason());
            recoveryHolder.tvApplyDate.setText(handle.getStrApplyTime());

            recoveryHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
            recoveryHolder.cbCheck.setChecked(duData.isCheck());
            recoveryHolder.cbCheck.setOnCheckedChangeListener(this);

            recoveryHolder.cardView.setOnClickListener(this);
            recoveryHolder.cardView.setTag(Integer.MAX_VALUE, position);
        } else if (viewHolder instanceof AssistHolder){
            AssistHolder assistHolder = (AssistHolder) viewHolder;
            assistHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            assistHolder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(assistHolder.itemView.getContext(), task));
            assistHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            assistHolder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            assistHolder.tvResolvePerson.setText(duData.getHostPerson());
            assistHolder.tvApplyPerson.setText(handle.getApplyPerson());
            assistHolder.tvApplyReason.setText(handle.getApplyReason());
            assistHolder.tvApplyPersonCount.setText(String.valueOf(handle.getApplyPersonCount()));
            assistHolder.tvApplyDate.setText(handle.getStrApplyTime());

            assistHolder.btnAssist.setTag(Integer.MAX_VALUE, position);
            assistHolder.btnAssist.setOnClickListener(this);

            assistHolder.cardView.setOnClickListener(this);
            assistHolder.cardView.setTag(Integer.MAX_VALUE, position);
        } else if (viewHolder instanceof ReportHolder){
            ReportHolder reportHolder = (ReportHolder) viewHolder;
            reportHolder.tvTaskId.setText(String.valueOf(task.getTaskId()));
            reportHolder.tvTaskType.setText(TransformUtil.getTaskTypeResourceId(task.getType()));
            reportHolder.tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(duData.getApplyType()));
            reportHolder.tvReportPerson.setText(task.getProcessPerson());
            reportHolder.tvCardId.setText(task.getCardId());
            reportHolder.tvCardName.setText(task.getCardName());
            reportHolder.tvAddress.setText(task.getAddress());

            reportHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
            reportHolder.cbCheck.setChecked(duData.isCheck());
            reportHolder.cbCheck.setOnCheckedChangeListener(this);

            reportHolder.cardView.setOnClickListener(this);
            reportHolder.cardView.setTag(Integer.MAX_VALUE, position);
        }
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener == null) {
            return;
        }

        int position = (int) view.getTag(Integer.MAX_VALUE);
        switch (view.getId()) {
            case R.id.card_view:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_HANDLE, position);
                break;
            case R.id.btn_item_assist:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ASSIST_VERIFY_DISPATCH, position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.btn_item_check:
                Integer position = (Integer) buttonView.getTag(Integer.MAX_VALUE);
                DUData data = list.get(position);
                data.setCheck(isChecked);
                break;
            default:
                break;
        }
    }

    class DelayHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvResolvePerson, tvApplyPerson,
                tvApplyReason, tvCompleteDate, tvCompleteEndDate, tvApplyDate;
        private LinearLayout llResolvePerson;
        private CheckBox cbCheck;
        private CardView cardView;

        DelayHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvApplyPerson = (TextView) view.findViewById(R.id.tv_item_apply_person);
            tvApplyReason = (TextView) view.findViewById(R.id.tv_item_apply_reason);
            tvCompleteDate = (TextView) view.findViewById(R.id.tv_item_complete_date);
            tvCompleteEndDate = (TextView) view.findViewById(R.id.tv_item_complete_end_date);
            tvApplyDate = (TextView) view.findViewById(R.id.tv_item_apply_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class RejectHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvRejectPerson,  tvResolvePerson,
                tvRejectReason, tvRejectDate;
        private LinearLayout llResolvePerson;
        private CheckBox cbCheck;
        private CardView cardView;

        RejectHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvRejectPerson = (TextView) view.findViewById(R.id.tv_item_reject_person);
            tvRejectReason = (TextView) view.findViewById(R.id.tv_item_reject_reason);
            tvRejectDate = (TextView) view.findViewById(R.id.tv_item_reject_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class AssistHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvResolvePerson,
                tvApplyPerson, tvApplyReason, tvApplyPersonCount, tvApplyDate;
        private LinearLayout llResolvePerson;
        private Button btnAssist;
        private CardView cardView;

        AssistHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            btnAssist = (Button) view.findViewById(R.id.btn_item_assist);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvApplyPerson = (TextView) view.findViewById(R.id.tv_item_apply_person);
            tvApplyReason = (TextView) view.findViewById(R.id.tv_item_apply_reason);
            tvApplyPersonCount = (TextView) view.findViewById(R.id.tv_item_apply_person_count);
            tvApplyDate = (TextView) view.findViewById(R.id.tv_item_apply_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class HangUpHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvResolvePerson,
                tvApplyPerson, tvApplyReason, tvApplyDate;
        private LinearLayout llResolvePerson;
        private CheckBox cbCheck;
        private CardView cardView;

        HangUpHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvApplyPerson = (TextView) view.findViewById(R.id.tv_item_apply_person);
            tvApplyReason = (TextView) view.findViewById(R.id.tv_item_apply_reason);
            tvApplyDate = (TextView) view.findViewById(R.id.tv_item_apply_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class RecoveryHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvResolvePerson,
                tvApplyPerson, tvApplyReason, tvApplyDate;
        private LinearLayout llResolvePerson;
        private CheckBox cbCheck;
        private CardView cardView;

        RecoveryHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvApplyPerson = (TextView) view.findViewById(R.id.tv_item_apply_person);
            tvApplyReason = (TextView) view.findViewById(R.id.tv_item_apply_reason);
            tvApplyDate = (TextView) view.findViewById(R.id.tv_item_apply_date);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class ReportHolder extends RecyclerView.ViewHolder{
        private TextView tvTaskId, tvTaskType, tvVerifyStatus, tvReportPerson, tvCardId,
                tvCardName, tvAddress;
        private CheckBox cbCheck;
        private CardView cardView;

        ReportHolder(View view) {
            super(view);
            tvTaskId = (TextView) view.findViewById(R.id.tv_item_taskId);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_task_type);
            tvVerifyStatus = (TextView) view.findViewById(R.id.tv_item_verify_status);
            tvReportPerson = (TextView) view.findViewById(R.id.tv_item_report_person);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_card_number);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_user_number);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

}
