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

public class listviewMerchantAdapter extends BaseAdapter{

    private static final String TAG = "lvmerchatadapter";
    public ArrayList<BillMerchantDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewMerchantAdapter(Context context, ArrayList<BillMerchantDetails> itemList) {

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
        TextView mPhone;
        TextView mNoOfPoints;
        TextView mBillNumber;
        TextView mBillDate;
        TextView mNoOfDays;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_merchant_bill, null, true);
            holder = new ViewHolder();
            holder.mBillDate = convertView.findViewById(R.id.sBillDate);
            holder.mPhone = convertView.findViewById(R.id.sMerchantName);
            holder.mBillNumber = convertView.findViewById(R.id.totalPrice);
            holder.mNoOfPoints = convertView
                    .findViewById(R.id.noOfPoints);
            holder.mNoOfDays = convertView
                    .findViewById(R.id.noOfDays);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;


        BillMerchantDetails item = itemList.get(position);
        holder.mBillDate.setText(item.getBill_date());
        holder.mPhone.setText(item.getPhone_no());
        holder.mBillNumber.setText(item.getBillNumber());
        holder.mNoOfPoints.setText(item.getNoOfPoints());
        holder.mNoOfDays.setText(item.getNo_of_days());


        return convertView;
    }
}