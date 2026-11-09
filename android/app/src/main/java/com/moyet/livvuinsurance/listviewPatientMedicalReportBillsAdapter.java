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

class listviewPatientMedicalReportBillsAdapter extends BaseAdapter {

    private static final String TAG = "lvpatbillsmedical";
    public ArrayList<PatientMedicalBillDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewPatientMedicalReportBillsAdapter(Context context, ArrayList<PatientMedicalBillDetails> itemList) {

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
        TextView mBillDate;
        TextView mBillNo;
        TextView mMerchantName;
        TextView mBillAmount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        listviewPatientMedicalReportBillsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_patient_bills_report, null, true);
            holder = new listviewPatientMedicalReportBillsAdapter.ViewHolder();
            holder.mBillDate = convertView.findViewById(R.id.sBillDate);
            holder.mBillNo = convertView.findViewById(R.id.sBillNo);
            holder.mMerchantName = convertView
                    .findViewById(R.id.sMerchantName);
            holder.mBillAmount = convertView.findViewById(R.id.sAmount);
            //holder.mNoOfPoints = convertView.findViewById(R.id.sNoOfPoints);
            convertView.setTag(holder);
        } else {
            holder = (listviewPatientMedicalReportBillsAdapter.ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        DecimalFormat form = new DecimalFormat("0.00");
        PatientMedicalBillDetails item = itemList.get(position);
        holder.mBillDate.setText(item.getTransactionDate());
        holder.mBillNo.setText(item.getBillNo());
        holder.mMerchantName.setText(item.getMerchant_name());
        double amount_returned = Double.parseDouble(item.getNo_of_points());
        holder.mBillAmount.setText(form.format(amount_returned));
        //holder.mNoOfPoints.setText(item.getNo_of_points().toString());
        return convertView;
    }

}
