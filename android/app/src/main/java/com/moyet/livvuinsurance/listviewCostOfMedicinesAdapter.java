package com.moyet.livvuinsurance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class listviewCostOfMedicinesAdapter extends BaseAdapter{

    private static final String TAG = "lvcostmedicadap";
    public ArrayList<CostOfMedicinesDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewCostOfMedicinesAdapter(Context context, ArrayList<CostOfMedicinesDetails> itemList) {

        super();
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView mCode;
        TextView mPhone;
        TextView mAmount;
        TextView mCount;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_cost_of_medicines_merchant, null, true);
            holder = new ViewHolder();
            holder.mCode = convertView.findViewById(R.id.sCode);
            holder.mPhone = convertView.findViewById(R.id.sPhoneNo);
            holder.mAmount = convertView.findViewById(R.id.sAmount);
            holder.mCount = convertView.findViewById(R.id.sCount);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        CostOfMedicinesDetails item = itemList.get(position);
        holder.mCode.setText(item.getCode());
        holder.mPhone.setText(item.getTotal_amount());
        holder.mAmount.setText(item.getPhone_no());
        holder.mCount.setText(item.getCount());

        return convertView;
    }
}