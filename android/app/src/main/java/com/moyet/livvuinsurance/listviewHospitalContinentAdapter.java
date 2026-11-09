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

public class listviewHospitalContinentAdapter extends BaseAdapter{

    private static final String TAG = "lvhospcontadapter";
    public ArrayList<HospitalContinentDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewHospitalContinentAdapter(Context context, ArrayList<HospitalContinentDetails> itemList) {

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
        TextView mCountry;
        TextView mPointsPaid;
        TextView mPointsCashed;
        TextView mBalance;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_hospital_continent, null, true);
            holder = new ViewHolder();
            holder.mRegion = convertView.findViewById(R.id.sRegion);
            holder.mCountry = convertView.findViewById(R.id.sCountry);
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

        HospitalContinentDetails item = itemList.get(position);
        holder.mRegion.setText(item.getRegion());
        holder.mCountry.setText(item.getCountry());
        holder.mPointsPaid.setText(item.getPoints_cashed_out());
        holder.mPointsCashed.setText(item.getPoints_paid());
        holder.mBalance.setText(item.getBalance());

        return convertView;
    }
}