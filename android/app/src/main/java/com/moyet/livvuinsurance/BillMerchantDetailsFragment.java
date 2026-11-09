package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class BillMerchantDetailsFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private BillMerchantDetailsFragment mFragment;
    private static final String TAG = "BillMercDetFrag";
    ArrayList<BillMerchantDetails> itemList;
    ArrayList<String> listViewItems;
    String URL;
    ListView lv;
    listviewMerchantAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String date_bill_created,code,bill_number;
    String no_of_points,no_of_days;
    String merchantName;
    String  merchantCode;
    String userName;
    String phone_no,merchantUserName;
    String message;
    Button cancel;
    FragmentActionListener fragmentActionListener;
    String ip,sending_frag,user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,session_id;
    int checkedItem;
    private  int screenWidth,screenHeight;

    public BillMerchantDetailsFragment() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (BillMerchantDetailsFragment)getFragmentManager().findFragmentByTag("BillMerchantDetailsFrag");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }


    public void onDestroy() {
        super.onDestroy();

        if(stringRequestAdapter != null){
            stringRequestAdapter.cancel();
        }
        if(stringRequest != null){
            stringRequest.cancel();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contextNullSafe == null) getContextNullSafety();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_bill_merchant_details, container, false);
        initUI();
        return rootView;
    }
    private void initUI() {

        if (getContext() != null && isAdded()){
            context=getContext();
        }else{
            context=getContextNullSafety();
        }
        if (getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<BillMerchantDetails>();
        URL=ip +"bill_get_merchant_details.php";
        loadBillMerchantData(URL);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_merchant, null);
        cancel = footerView.findViewById(R.id.cancel);
        lv.addFooterView(footerView, null, false);
        adapter = new listviewMerchantAdapter(context, itemList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (getContext() != null && isAdded()){
                    context=getContext();
                }else{
                    context=getContextNullSafety();
                }
                if (getActivity() != null && isAdded()){
                    activity=getActivity();
                }else{
                    activity=getActivityNullSafety();
                }

                if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                }else{
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                }

                final String[] choice = new String[]{activity.getResources().getString(R.string.view_bill_statement),
                        activity.getResources().getString(R.string.send_sms_reminder)};
                builder.setTitle(activity.getResources().getString(R.string.please_select));

                BillMerchantDetails item = itemList.get(position);
                final String billNo = item.getBillNumber();
                final String phone_number =item.getPhone_no();
                final String billDate = item.getBill_date();
                final String no_of_points = item.getNoOfPoints();

                builder.setSingleChoiceItems(choice, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        switch(which)
                        {
                            case 0:
                                checkedItem = 0;
                                break;

                            case 1:
                                checkedItem = 1;
                                break;
                        }

                    }
                });

                builder.setPositiveButton(activity.getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(checkedItem)
                        {
                            case 0:
                                if (fragmentActionListener != null) {
                                    if (sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 36);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                        bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                        bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantStatementFragment");
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, sending_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    } else if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 36);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                        bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                        bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantStatementFragment");
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                                break;

                            case 1:

                                if (fragmentActionListener != null) {
                                    if (sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                        bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                        bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                        bundle.putString(FragmentActionListener.KEY_NO_OF_POINTS, no_of_points);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, sending_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    } else if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                        bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                        bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                        bundle.putString(FragmentActionListener.KEY_NO_OF_POINTS, no_of_points);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                                break;

                            default:
                                throw new IllegalStateException(activity.getResources().getString(R.string.unexpected_value) + checkedItem);
                        }

                    }
                });


                builder.setNegativeButton(activity.getResources().getString(R.string.cancel_dialog), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(sending_frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            }
        });



    }
    private void loadBillMerchantData(String url) {

        if (getContext() != null && isAdded()){
            context=getContext();
        }else{
            context=getContextNullSafety();
        }
        if (getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);

        if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
            pd = new ProgressDialog(context, R.style.MyDialogTheme240);
        }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                || screenWidth==720 || screenWidth==1184) {
            pd = new ProgressDialog(context, R.style.MyDialogTheme16sp);
        }else if(screenWidth==600 || screenWidth==1024){
            pd = new ProgressDialog(context, R.style.MyDialogTheme600);
        }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
            pd = new ProgressDialog(context, R.style.MyDialogTheme720);
        }else{
            pd = new ProgressDialog(context, R.style.MyDialogTheme18sp);
        }
        pd.setMessage(activity.getResources().getString(R.string.processing_please_wait_dialog));
        pd.show();
        stringRequestAdapter = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                boolean success = jsonResponse.getBoolean("success");
                                String message = jsonResponse.getString("message");

                                if (success) {
                                    pd.dismiss();
                                    date_bill_created = jsonResponse.getString("date_bill_created");
                                    phone_no = jsonResponse.getString("phone_number_billed");
                                    bill_number = jsonResponse.getString("bill_number");
                                    no_of_points = jsonResponse.getString("total_no_of_points");
                                    no_of_days = jsonResponse.getString("no_of_days");
                                    BillMerchantDetails billMerchantDetails = new BillMerchantDetails(date_bill_created, phone_no, no_of_points, bill_number,no_of_days);

                                    itemList.add(billMerchantDetails);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    pd.dismiss();
                                    if (fragmentActionListener != null) {
                                        if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillMerchantDetailsFrag");
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (getContext() != null && isAdded()){
                            context=getContext();
                        }else{
                            context=getContextNullSafety();
                        }
                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }

                        pd.dismiss();

                        if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                            builder = new
                                    AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                        }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                                || screenWidth==720 || screenWidth==1184) {
                            builder = new
                                    AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                        }else if(screenWidth==600 || screenWidth==1024){
                            builder = new
                                    AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                        }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                            builder = new
                                    AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                        }else{
                            builder = new
                                    AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                        }

                        builder.setMessage(activity.getResources().getString(R.string.unable_to_download))
                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (fragmentActionListener != null) {
                                            if(sending_frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }

                                    }
                                })
                                .create()
                                .show();
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", merchantUserName);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
    }

    private void checkLoginStatus(String server_logged_in) {

        if (getContext() != null && isAdded()){
            context=getContext();
        }else{
            context=getContextNullSafety();
        }
        if (getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        stringRequest = new StringRequest(Request.Method.POST, server_logged_in,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (!success) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 99);
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseLogoutSessionFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                if(sending_frag.contentEquals("companyMainMenu")) {
                    param.put("username", merchantUserName);
                }else{
                    param.put("username", user_login_id);
                }
                param.put("ssid",session_id);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

    }

    public Context getContextNullSafety() {

        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }

    /**CALL THIS IF YOU NEED ACTIVITY*/
    public FragmentActivity getActivityNullSafety() {

        if (getContextNullSafety() != null && getContextNullSafety() instanceof FragmentActivity) {
            /*It is observed that if context it not null then it will be
             * the related host/container activity always*/
            return (FragmentActivity) getContextNullSafety();
        }
        return null;
    }



}
