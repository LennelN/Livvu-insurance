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

public class listviewSocietyCompanyBillRecordsAdapter extends BaseAdapter{

    private static final String TAG = "lvsoccombillrecdapter";
    public ArrayList<SocietyCompanyBillRecordDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewSocietyCompanyBillRecordsAdapter(Context context, ArrayList<SocietyCompanyBillRecordDetails> itemList) {

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

        TextView sBillDate;
        TextView sBillNo;
        TextView sName;
        TextView sPhoneNo;
        TextView sPaid;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_society_company_bill_records, null, true);
            holder = new ViewHolder();
            holder.sBillDate = convertView.findViewById(R.id.sBillDate);
            holder.sBillNo = convertView.findViewById(R.id.sBillNo);
            holder.sName = convertView.findViewById(R.id.sName);
            holder.sPhoneNo = convertView
                    .findViewById(R.id.sPhoneNo);
            holder.sPaid = convertView
                    .findViewById(R.id.sPaid);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;
        SocietyCompanyBillRecordDetails item = itemList.get(position);
        holder.sBillDate.setText(item.getBill_date());
        holder.sBillNo.setText(item.getBill_no());
        holder.sPhoneNo.setText(item.getPhone_no());
        holder.sName.setText(item.getMerchant_name());
        holder.sPaid.setText(item.getBill_paid());

        return convertView;
    }
}