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

class listviewMemberPointsRecordsAdapter extends BaseAdapter {

    private static final String TAG = "lvmembpointrec";
    public ArrayList<MemberPointsRecordDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewMemberPointsRecordsAdapter(Context context, ArrayList<MemberPointsRecordDetails> itemList) {

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
        TextView mPurchaseDate;
        TextView mTransactionType;
        TextView mTransactionNo;
        TextView mPurchaseAmount;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        listviewMemberPointsRecordsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_member_points_records, null, true);
            holder = new listviewMemberPointsRecordsAdapter.ViewHolder();
            holder.mPurchaseDate = convertView.findViewById(R.id.sPurchaseDate);
            holder.mTransactionType = convertView.findViewById(R.id.sTransactionType);
            holder.mTransactionNo = convertView
                    .findViewById(R.id.sTransactionNo);
            holder.mPurchaseAmount = convertView.findViewById(R.id.sAmount);


            convertView.setTag(holder);
        } else {
            holder = (listviewMemberPointsRecordsAdapter.ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        DecimalFormat form = new DecimalFormat("0.00");
        MemberPointsRecordDetails item = itemList.get(position);
        holder.mPurchaseDate.setText(item.getDate_of_purchase());
        holder.mTransactionType.setText(item.getTransaction_type());
        holder.mTransactionNo.setText(item.getTransaction_no());
        double amount_returned = item.getPurchase_amount();
        holder.mPurchaseAmount.setText(form.format(amount_returned));
        
        return convertView;
    }

}
