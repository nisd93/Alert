package com.alert.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alert.Alarm;
import com.alert.AppUtils.Fonts;
import com.alert.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kena on 3/4/2017.
 */

public class AlarmAdapter extends
        RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private ArrayList<Alarm> itemModels;
    private Context context;
    private OnItemClickListener clickListener;
    public AlarmAdapter(Context context,
                          ArrayList<Alarm> itemModels, OnItemClickListener clickListener) {
        this.context = context;
        this.itemModels = itemModels;
        this.clickListener=clickListener;
    }

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.row_alarm, viewGroup, false);
        AlarmHolder viewHolder = new AlarmHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int i) {
        Alarm cm = itemModels.get(i);

        holder.tvTime.setTypeface(Fonts.setUtahCondensedBold(context));
        holder.tvTitle.setTypeface(Fonts.setUtahCondensedBold(context));

        holder.tvTime.setText(cm.getStatus());
        holder.tvTitle.setText(cm.getTitle());

    }

    public Alarm getAppModel(int position) {
        return itemModels.get(position);
    }

    public ArrayList<Alarm> getAllAppModel() {
        return itemModels;
    }

    @Override
    public int getItemCount() {
        return itemModels == null ? 0 : itemModels.size();
    }
    public void addAll(List<Alarm> temp) {
        itemModels.addAll(temp);
        AlarmAdapter.this.notifyItemInserted(itemModels.size() - 1);
    }
    public void animateTo(ArrayList<Alarm> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    public Alarm removeItem(int position) {
        final Alarm model = itemModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Alarm model) {
        itemModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Alarm model = itemModels.remove(fromPosition);
        itemModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(ArrayList<Alarm> newModels) {
        for (int i = itemModels.size() - 1; i >= 0; i--) {
            final Alarm model = itemModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Alarm> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Alarm model = newModels.get(i);
            if (!itemModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Alarm> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Alarm model = newModels.get(toPosition);
            final int fromPosition = itemModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position, Alarm data);
    }

    class AlarmHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView tvTitle;
        public TextView tvTime;
        
                

        public AlarmHolder(View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getPosition(),itemModels.get(getPosition()));
            }
        }
    }
}
