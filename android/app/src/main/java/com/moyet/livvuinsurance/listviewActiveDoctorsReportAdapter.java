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

class listviewActiveDoctorsReportAdapter extends BaseAdapter {

    private static final String TAG = "lvactdocreport";
    public ArrayList<DoctorActiveReportDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewActiveDoctorsReportAdapter(Context context, ArrayList<DoctorActiveReportDetails> itemList) {

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
        TextView mDateJoined;
        TextView mDoctorName;
        TextView mDoctorPhone;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        listviewActiveDoctorsReportAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_active_doctor_report, null, true);
            holder = new listviewActiveDoctorsReportAdapter.ViewHolder();
            holder.mDateJoined = convertView.findViewById(R.id.sDateJoined);
            holder.mDoctorName = convertView.findViewById(R.id.sDoctorName);
            holder.mDoctorPhone = convertView.findViewById(R.id.sDoctorPhone);

            convertView.setTag(holder);
        } else {
            holder = (listviewActiveDoctorsReportAdapter.ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        DecimalFormat form = new DecimalFormat("0.00");
        DoctorActiveReportDetails item = itemList.get(position);
        holder.mDateJoined.setText(item.getDateJoined());
        holder.mDoctorName.setText(item.getDoctorName());
        holder.mDoctorPhone.setText(item.getDoctorPhone());

        return convertView;
    }

}
