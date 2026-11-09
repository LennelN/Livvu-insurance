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
import java.util.Locale;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class listviewSocietyCompanyMemberStatusAdapter extends BaseAdapter{

    private static final String TAG = "lvsoccommemstatdapter";
    public ArrayList<SocietyCompanyMemberDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewSocietyCompanyMemberStatusAdapter(Context context, ArrayList<SocietyCompanyMemberDetails> itemList) {

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

        TextView sPhoneNo;
        TextView sName;
        TextView mNoOfPoints;
        TextView sDueDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_society_company_member_status, null, true);
            holder = new ViewHolder();
            holder.sPhoneNo = convertView.findViewById(R.id.sPhoneNo);
            holder.sName = convertView.findViewById(R.id.sName);
            holder.sDueDate = convertView
                    .findViewById(R.id.sDueDate);
            holder.mNoOfPoints = convertView
                    .findViewById(R.id.noOfPoints);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        SocietyCompanyMemberDetails item = itemList.get(position);
        String dateReturned = item.getDue_date();

        String cleanEnd = dateReturned.replaceAll("[^\\d.]", "");
        //20240210

        int dayDueEnd =Integer.parseInt(cleanEnd.substring(6, 8));

        int yearDueEnd =Integer.parseInt(cleanEnd.substring(0, 4));

        int monthDueEnd = Integer.parseInt(cleanEnd.substring(4, 6));

        String endReportDate = String.format(Locale.getDefault(),"%02d",dayDueEnd) + "-" +
                String.format(Locale.getDefault(),"%02d",monthDueEnd) + "-" +
                String.format(Locale.getDefault(),"%02d",yearDueEnd);

        DecimalFormat form = new DecimalFormat("0.00");
        holder.sPhoneNo.setText(item.getPhone_no());
        holder.sName.setText(item.getName());
        holder.sDueDate.setText(endReportDate);
        double amount_returned = Double.parseDouble(item.getNo_of_points());
        holder.mNoOfPoints.setText(form.format(amount_returned));
        return convertView;
    }
}