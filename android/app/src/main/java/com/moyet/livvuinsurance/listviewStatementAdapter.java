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

public class listviewStatementAdapter extends BaseAdapter{

    private static final String TAG = "lvstatementadapter";
    public ArrayList<BillStatementDetails> itemList;
    private Context context;
    private int lastPosition = -1;


    public listviewStatementAdapter(Context context, ArrayList<BillStatementDetails> itemList) {

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
        TextView mDescription;
        TextView mTariffCode;
        TextView mNoOfPoints;
        TextView mNoOfPointsPaid;
        TextView mTransactionDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_client_statement, null, true);
            holder = new ViewHolder();
            holder.mTransactionDate = convertView.findViewById(R.id.sBillDate);
            holder.mTariffCode = convertView.findViewById(R.id.sCode);
            holder.mDescription = convertView.findViewById(R.id.sMerchantName);
            holder.mNoOfPoints = convertView
                    .findViewById(R.id.noOfPoints);
            holder.mNoOfPointsPaid = convertView
                    .findViewById(R.id.noOfPointsPaid);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        BillStatementDetails item = itemList.get(position);
        holder.mTransactionDate.setText(item.getTransaction_date());
        holder.mTariffCode.setText(item.getCode());
        holder.mDescription.setText(item.getDescription());
        holder.mNoOfPoints.setText(item.getNoOfPoints());
        holder.mNoOfPointsPaid.setText(item.getNoOfPointsPaid());


        return convertView;
    }
}