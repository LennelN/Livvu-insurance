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

public class listviewPaymentDetailsAdapter extends BaseAdapter{

    private static final String TAG = "lvpaydetadapter";
    public ArrayList<PaymentDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewPaymentDetailsAdapter(Context context, ArrayList<PaymentDetails> itemList) {

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
        TextView mNoOfPoints;
        TextView mBillDate;
        TextView sTransactionNo;
        TextView mPhoneNo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_payment_details, null, true);
            holder = new ViewHolder();
            holder.mBillDate = convertView.findViewById(R.id.sBillDate);
            holder.sTransactionNo = convertView.findViewById(R.id.sBillNo);
            holder.mNoOfPoints = convertView
                    .findViewById(R.id.noOfPointsPaid);
            holder.mPhoneNo = convertView
                    .findViewById(R.id.sPhoneNo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        PaymentDetails item = itemList.get(position);
        holder.mBillDate.setText(item.getTransaction_date());
        holder.sTransactionNo.setText(item.getBill_number());
        holder.mNoOfPoints.setText(item.getNoOfPointsPaid());
        holder.mPhoneNo.setText(item.getPhone_number());

        return convertView;
    }
}