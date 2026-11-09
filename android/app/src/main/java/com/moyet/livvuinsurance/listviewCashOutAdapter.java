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

public class listviewCashOutAdapter extends BaseAdapter {

    public ArrayList<CashOutStatementDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewCashOutAdapter(Context context, ArrayList<CashOutStatementDetails> itemList) {

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
        TextView mNoOfDays;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_cash_out, null, true);
            holder = new ViewHolder();
            holder.mBillDate = convertView.findViewById(R.id.sBillDate);
            holder.sTransactionNo = convertView.findViewById(R.id.sTransactionNo);
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
        CashOutStatementDetails item = itemList.get(position);
        holder.mBillDate.setText(item.getTransaction_date());
        holder.sTransactionNo.setText(item.getCashed_out_transaction_no());
        holder.mNoOfPoints.setText(item.getNoOfPoints());
        holder.mNoOfDays.setText(item.getNo_of_days());


        return convertView;
    }

}
