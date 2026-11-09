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

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class listviewAdapterDialog extends BaseAdapter{

    private static final String TAG = "listviewadapter";
    public ArrayList<Model> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewAdapterDialog(Context context, ArrayList<Model> itemList) {

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
        TextView mPrice;
        TextView mTotalPrice;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row, null, true);
            holder = new ViewHolder();
            holder.mSNo = convertView.findViewById(R.id.sNo);
            holder.mCode = convertView.findViewById(R.id.code);
            holder.mDescription = convertView
                    .findViewById(R.id.Description);
            holder.mPrice = convertView.findViewById(R.id.price);
            holder.mTotalPrice = convertView.findViewById(R.id.totalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        DecimalFormat form = new DecimalFormat("0.00");
        Model item = itemList.get(position);

        holder.mSNo.setText(item.getsNo());
        holder.mCode.setText(item.getCode());
        holder.mDescription.setText(item.getDescription());
        double price_returned = item.getPrice();
        holder.mPrice.setText(form.format(price_returned));
        double total_price_returned = item.getTotalPrice();
        holder.mTotalPrice.setText(form.format(total_price_returned));

        return convertView;
    }
}
