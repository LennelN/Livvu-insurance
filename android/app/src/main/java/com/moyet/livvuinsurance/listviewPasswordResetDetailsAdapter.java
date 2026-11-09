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

public class listviewPasswordResetDetailsAdapter extends BaseAdapter{

    private static final String TAG = "lvpasswddetadapter";
    public ArrayList<ResetPasswordDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewPasswordResetDetailsAdapter(Context context, ArrayList<ResetPasswordDetails> itemList) {

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
        TextView mName;
        TextView mRequestDate;
        TextView mCategory;
        TextView mPhoneNo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_password_reset_details, null, true);
            holder = new ViewHolder();
            holder.mPhoneNo = convertView.findViewById(R.id.sPhoneNo);
            holder.mRequestDate = convertView.findViewById(R.id.sRequestDate);
            holder.mName = convertView.findViewById(R.id.sName);
            holder.mCategory = convertView.findViewById(R.id.sCategory);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        ResetPasswordDetails item = itemList.get(position);
        holder.mPhoneNo.setText(item.getPhone_number());
        holder.mRequestDate.setText(item.getRequest_date());
        holder.mName.setText(item.getRequest_processed());
        holder.mCategory.setText(item.getRequest_completed());


        return convertView;
    }
}