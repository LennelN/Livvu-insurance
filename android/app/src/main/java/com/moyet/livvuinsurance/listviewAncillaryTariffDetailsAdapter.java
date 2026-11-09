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

class listviewAncillaryTariffDetailsAdapter extends BaseAdapter {

    private static final String TAG = "lvancillarytariff";
    public ArrayList<TariffDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewAncillaryTariffDetailsAdapter(Context context, ArrayList<TariffDetails> itemList) {

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
        TextView mCode;
        TextView mDescription;
        TextView mPrice;
        TextView mCategory;
        TextView mType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        listviewAncillaryTariffDetailsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_ancillary_tariff_details, null, true);
            holder = new listviewAncillaryTariffDetailsAdapter.ViewHolder();
            holder.mCode = convertView.findViewById(R.id.sCode);
            holder.mDescription = convertView.findViewById(R.id.sDescription);
            holder.mPrice = convertView.findViewById(R.id.sPrice);
            holder.mCategory = convertView
                    .findViewById(R.id.sCategory);
            holder.mType = convertView.findViewById(R.id.sType);
            convertView.setTag(holder);
        } else {
            holder = (listviewAncillaryTariffDetailsAdapter.ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;

        DecimalFormat form = new DecimalFormat("0.00");
        TariffDetails item = itemList.get(position);
        holder.mCode.setText(item.getCode());
        holder.mDescription.setText(item.getDescription());
        double amount_returned = item.getPrice();
        holder.mPrice.setText(form.format(amount_returned));
        holder.mCategory.setText(item.getCategory());
        holder.mType.setText(item.getType());
        return convertView;
    }

}
