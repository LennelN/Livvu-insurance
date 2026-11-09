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

public class listviewCountryCurrencyTypeAdapter extends BaseAdapter{

    private static final String TAG = "lvcurrtypeadap";
    public ArrayList<CountryCurrencyTypeDetails> itemList;
    private Context context;
    private int lastPosition = -1;

    public listviewCountryCurrencyTypeAdapter(Context context, ArrayList<CountryCurrencyTypeDetails> itemList) {

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
        TextView mCategory;
        TextView mNoCategory;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_row_country_currency_types, null, true);
            holder = new ViewHolder();
            holder.mRegion = convertView.findViewById(R.id.sRegion);
            holder.mCountry = convertView.findViewById(R.id.sCountry);
            holder.mCategory = convertView.findViewById(R.id.sCategory);
            holder.mNoCategory = convertView.findViewById(R.id.sNo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);

        convertView.startAnimation(animation);
        lastPosition=position;


        CountryCurrencyTypeDetails item = itemList.get(position);
        holder.mRegion.setText(item.getCurrencyName());
        holder.mCountry.setText(item.getCurrencyCode());
        holder.mCategory.setText(item.getCurrencySymbol());
        holder.mNoCategory.setText(item.getCountry());

        return convertView;
    }
}