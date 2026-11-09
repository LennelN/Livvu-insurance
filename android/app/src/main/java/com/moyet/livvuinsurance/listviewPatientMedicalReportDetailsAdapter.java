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

class listviewPatientMedicalReportDetailsAdapter extends BaseAdapter {

    private static final String TAG = "lvpatientmedical";
    public ArrayList<PatientMedicalReportDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewPatientMedicalReportDetailsAdapter(Context context, ArrayList<PatientMedicalReportDetails> itemList) {

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
        TextView mSNo;
        TextView mCode;
        TextView mDescription;
        TextView mDoctor;
        TextView mBillDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        listviewPatientMedicalReportDetailsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_patient_medical_report, null, true);
            holder = new listviewPatientMedicalReportDetailsAdapter.ViewHolder();
            holder.mBillDate = convertView.findViewById(R.id.sBillDate);
            holder.mSNo = convertView.findViewById(R.id.sNo);
            holder.mCode = convertView.findViewById(R.id.sCode);
            holder.mDescription = convertView
                    .findViewById(R.id.sDescription);
            holder.mDoctor = convertView.findViewById(R.id.sDoctor);
            convertView.setTag(holder);
        } else {
            holder = (listviewPatientMedicalReportDetailsAdapter.ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;
        //DecimalFormat form = new DecimalFormat("0.00");
        PatientMedicalReportDetails item = itemList.get(position);
        holder.mBillDate.setText(item.getTransactionDate());
        holder.mSNo.setText(item.getFrequency());
        holder.mCode.setText(item.getCode());
        holder.mDescription.setText(item.getDescription());
        holder.mDoctor.setText(item.getDoctor());
        return convertView;
    }

}
