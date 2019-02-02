package com.modaberi.widget.stepper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.modaberi.widget.R;
import com.modaberi.widget.stepper.StepperHeader.EnumStep;

import java.util.ArrayList;
import java.util.List;

public class StepperAdapter extends RecyclerView.Adapter<StepperAdapter.StepperVH> {
    private Context context;
    private List<StepperHeader> stepperHeaderList;

    interface OnStepperItemClickListener {
        void onItemClick(int clickedItemPosition);
    }

    private OnStepperItemClickListener mListener;

    StepperAdapter(Context context, List<StepperHeader> stepperHeaderList, OnStepperItemClickListener listener) {
        this.context = context;
        this.stepperHeaderList = stepperHeaderList;
        mListener = listener;
    }

    public void setStepperHeaderList(List<StepperHeader> stepperList) {
        stepperHeaderList = new ArrayList<>();
        stepperHeaderList.addAll(stepperList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepperVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.moda_list_item_stepper, parent, false);
        return new StepperVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepperVH holder, int position) {

        StepperHeader item = stepperHeaderList.get(position);
        holder.mTvStepTitle.setText(item.title);

        if (EnumStep.DONE.methodName.equals(item.step.methodName)) {
            holder.mStepLine.setBackgroundColor(ContextCompat.getColor(context, R.color.color_green));
            holder.mTvStepTitle.setTextColor(ContextCompat.getColor(context, R.color.color_green));
            holder.mTvStepTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            holder.mStepIcon.setVisibility(View.VISIBLE);
            holder.mStepIcon.setImageResource(R.drawable.ic_check_circle);
            holder.mTvStepCode.setVisibility(View.GONE);
        } else if (EnumStep.CURRENT.methodName.equals(item.step.methodName)) {
            holder.mStepLine.setBackgroundColor(ContextCompat.getColor(context, R.color.text_color));
            holder.mTvStepTitle.setTextColor(ContextCompat.getColor(context, R.color.text_color));
            holder.mTvStepTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            holder.mStepIcon.setVisibility(View.VISIBLE);
            holder.mStepIcon.setImageResource(R.drawable.ic_current_item);
            holder.mTvStepCode.setVisibility(View.GONE);

        } else if (EnumStep.NEXT.methodName.equals(item.step.methodName)) {
            holder.mStepLine.setBackgroundColor(ContextCompat.getColor(context, R.color.text_sub_color));
            holder.mTvStepTitle.setTextColor(ContextCompat.getColor(context, R.color.text_sub_color));
            holder.mTvStepTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            holder.mStepIcon.setVisibility(View.GONE);
            holder.mTvStepCode.setVisibility(View.VISIBLE);
            holder.mTvStepCode.setText(String.valueOf(item.position + 1));
        }

        if (position + 1 == stepperHeaderList.size()) {
            holder.mStepLine.setVisibility(View.INVISIBLE);
        } else {
            holder.mStepLine.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));

    }

    @Override
    public int getItemCount() {
        return stepperHeaderList != null ? stepperHeaderList.size() : 0;
    }

    class StepperVH extends RecyclerView.ViewHolder {

        private TextView mTvStepCode;
        private TextView mTvStepTitle;
        private ImageView mStepIcon;
        private View mStepLine;

        StepperVH(View contentView) {
            super(contentView);
            mTvStepCode = contentView.findViewById(R.id.tv_step_code);
            mTvStepTitle = contentView.findViewById(R.id.tv_step_title);
            mStepIcon = contentView.findViewById(R.id.image_step_icon);
            mStepLine = contentView.findViewById(R.id.step_line);
        }
    }
}

