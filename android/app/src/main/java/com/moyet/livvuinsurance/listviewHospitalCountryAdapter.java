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

public class listviewHospitalCountryAdapter extends BaseAdapter{

    private static final String TAG = "lvhospcounadapter";
    public ArrayList<HospitalCountryDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewHospitalCountryAdapter(Context context, ArrayList<HospitalCountryDetails> itemList) {

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
        TextView mRegion;
        TextView mName;
        TextView mPointsPaid;
        TextView mPointsCashed;
        TextView mBalance;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_hospital_country, null, true);
            holder = new ViewHolder();
            holder.mRegion = convertView.findViewById(R.id.sRegion);
            holder.mName = convertView.findViewById(R.id.sName);
            holder.mPointsPaid = convertView.findViewById(R.id.sPointsPaid);
            holder.mPointsCashed = convertView.findViewById(R.id.sPointsCashed);
            holder.mBalance = convertView.findViewById(R.id.sBalance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        HospitalCountryDetails item = itemList.get(position);
        holder.mRegion.setText(item.getRegion());
        holder.mName.setText(item.getName());
        holder.mPointsPaid.setText(item.getPoints_cashed_out());
        holder.mPointsCashed.setText(item.getPoints_paid());
        holder.mBalance.setText(item.getBalance());

        return convertView;
    }
}