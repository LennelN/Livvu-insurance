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

public class listviewClientStatementAdapter extends BaseAdapter{

    private static final String TAG = "clienttatementadapter";
    public ArrayList<ClientStatementDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewClientStatementAdapter(Context context, ArrayList<ClientStatementDetails> itemList) {

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
        TextView mTransactionType;
        TextView mMerchantPhoneNumber;
        TextView mNoOfPointsPaid;
        TextView mNoOfPointsCredit;
        TextView mTransactionDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_client_statement_mini, null, true);
            holder = new ViewHolder();
            holder.mTransactionDate = convertView.findViewById(R.id.sBillDate);
            holder.mTransactionType = convertView.findViewById(R.id.sCode);
            holder.mMerchantPhoneNumber = convertView.findViewById(R.id.sMerchantName);
            holder.mNoOfPointsPaid = convertView
                    .findViewById(R.id.noOfPoints);
            holder.mNoOfPointsCredit = convertView
                    .findViewById(R.id.noOfPointsPaid);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        ClientStatementDetails item = itemList.get(position);
        holder.mTransactionDate.setText(item.getDate_paid());
        holder.mTransactionType.setText(item.getTransaction_type());
        holder.mMerchantPhoneNumber.setText(item.getMerchant_phone_number());
        holder.mNoOfPointsCredit.setText(item.getNo_of_points_credit());
        holder.mNoOfPointsPaid.setText(item.getNoOfPointsPaid());


        return convertView;
    }
}