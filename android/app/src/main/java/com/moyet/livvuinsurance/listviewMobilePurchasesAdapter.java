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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class listviewMobilePurchasesAdapter extends BaseAdapter{

    private static final String TAG = "lvmobpurchadapter";
    public ArrayList<MobileNetworkPurchaseDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewMobilePurchasesAdapter(Context context, ArrayList<MobileNetworkPurchaseDetails> itemList) {

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
        TextView sPurchaseDate;
        TextView sPhoneNo;
        TextView sPurchaseAmt;
        TextView mNoOfDays;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_mobile_purchases, null, true);
            holder = new ViewHolder();
            holder.sPurchaseDate = convertView.findViewById(R.id.sPurchaseDate);
            holder.sPhoneNo = convertView.findViewById(R.id.sPhoneNo);
            holder.sPurchaseAmt = convertView
                    .findViewById(R.id.sPurchaseAmt);
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

        DecimalFormat form = new DecimalFormat("0.00");
        MobileNetworkPurchaseDetails item = itemList.get(position);
        holder.sPurchaseDate.setText(item.getTransaction_date());
        holder.sPhoneNo.setText(item.getPhone_no());
        double amount_returned = item.getPurchase_amount();
        holder.sPurchaseAmt.setText(form.format(amount_returned));
        holder.mNoOfDays.setText(item.getNo_of_days());


        return convertView;
    }
}