package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class BillPatientStatementFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    TextView footer_debit,footer_credit,total,footer_balance;
    private BillPatientStatementFragment mFragment;
    private static final String TAG = "BillPatientStatFrag";
    ArrayList<BillStatementDetails> itemList;
    ArrayList<String> listViewItems;
    String URL;
    FragmentActionListener fragmentActionListener;
    ListView lv;
    listviewStatementAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String transaction_date,tariff_code,phone_number,merchant_name,merchant_phone_number,bill_date;
    String bill_number;
    BigDecimal total_bill;
    BigDecimal balance;
    BigDecimal balance_90_days;
    BigDecimal balance_120_days;
    BigDecimal balance_60_days,balance_30_days,balance_current;
    BigDecimal debit_total;
    BigDecimal credit_total;
    String no_of_points,no_of_points_paid,description;
    String firstName,client_name,merchantName,merchantCode,merchantUserName;
    String lastName;
    String userName;
    String phone_no;
    String message;
    Button cancel;
    Calendar calendar;
    TextView welcomeMsg;
    int day,month,year;
    String strDate,strBillDate;
    float daysBetween;
    String ip,frag,user_login_key,user_role,user_login_id,nameOfUser,country,session_id;
    private  int screenWidth,screenHeight;
    String reportCountry;
    double p1,p2,p3,p4;
    BigDecimal b1,b2,b3,b4;

    public BillPatientStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (BillPatientStatementFragment)getFragmentManager().findFragmentByTag("BillPatientStatementFrag");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag= bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            phone_no = bundle.getString(FragmentActionListener.KEY_REPORT_PHONE_NO);
            reportCountry = bundle.getString(FragmentActionListener.KEY_REPORT_COUNTRY);

            if(frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag= bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            phone_no = bundle.getString(FragmentActionListener.KEY_REPORT_PHONE_NO);
            reportCountry = bundle.getString(FragmentActionListener.KEY_REPORT_COUNTRY);
            if(frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_bill_statement, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag= bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            phone_no = bundle.getString(FragmentActionListener.KEY_REPORT_PHONE_NO);
            reportCountry = bundle.getString(FragmentActionListener.KEY_REPORT_COUNTRY);
            if(frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

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


        p1=0;
        p2=0;
        p3=0;
        p4=0;
        b1= BigDecimal.valueOf(0);
        b2= BigDecimal.valueOf(0);
        b3= BigDecimal.valueOf(0);
        b4= BigDecimal.valueOf(0);
        balance = BigDecimal.valueOf(0);
        balance_current = BigDecimal.valueOf(0);
        balance_90_days =BigDecimal.valueOf(0);
        balance_120_days =BigDecimal.valueOf(0);
        balance_60_days =BigDecimal.valueOf(0);
        balance_30_days =BigDecimal.valueOf(0);
        debit_total = BigDecimal.valueOf(0);
        credit_total = BigDecimal.valueOf(0);

        calendar = Calendar.getInstance(Locale.getDefault());
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        month = month + 1;
        year=calendar.get(Calendar.YEAR);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        strDate =  mdformat.format(cal.getTime());
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<BillStatementDetails>();
        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        welcomeMsg.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL=ip +"bill_get_current_client_statement.php";
        loadBillClientData(URL);
        //View headerView = getLayoutInflater().inflate(R.layout.listview_header_client_statement, null);
        //lv.addHeaderView(headerView);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_current_client_statement, null);
        cancel = footerView.findViewById(R.id.cancel);
        footer_debit = footerView.findViewById(R.id.footer_debit);
        footer_credit = footerView.findViewById(R.id.footer_credit);
        footer_balance = footerView.findViewById(R.id.footer_balance);
        total = footerView.findViewById(R.id.total);
        lv.addFooterView(footerView, null, false);
        adapter = new listviewStatementAdapter(context, itemList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lv.setOnItemLongClickListener(this);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
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
    private void loadBillClientData(String url) {

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
                                    transaction_date = jsonResponse.getString("transaction_date");
                                    tariff_code = jsonResponse.getString("tariff_code");
                                    description = jsonResponse.getString("tariff_description");
                                    no_of_points = jsonResponse.getString("no_of_points");
                                    no_of_points_paid = jsonResponse.getString("no_of_points_paid");
                                    client_name = jsonResponse.getString("client_name");
                                    p1=Double.parseDouble(no_of_points);
                                    p2=Double.parseDouble(no_of_points_paid);
                                    b1=BigDecimal.valueOf(p1);
                                    b2=BigDecimal.valueOf(p2);
                                    b3 = b1.subtract(b2);
                                    b4 = b4.add(b3);


                                    //balance = balance + Double.parseDouble(no_of_points) - Double.parseDouble(no_of_points_paid);
                                    balance = balance.add(b3);
                                    //debit_total = debit_total + Double.parseDouble(no_of_points);
                                    debit_total = debit_total.add(b1);
                                    //credit_total= credit_total + Double.parseDouble(no_of_points_paid);//
                                    credit_total= credit_total.add(b2);
                                    footer_debit.setText(String.valueOf(debit_total));
                                    footer_credit.setText(String.valueOf(credit_total));
                                    footer_balance.setText(String.valueOf(balance));

                                    String headerMessage =getResources().getString(R.string.statement_for_client) + " " + client_name;
                                    welcomeMsg.setText(headerMessage);

                                    BillStatementDetails billStatementDetails = new BillStatementDetails(transaction_date,tariff_code, description,no_of_points,no_of_points_paid);

                                    itemList.add(billStatementDetails);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    pd.dismiss();
                                    if (fragmentActionListener != null) {
                                            if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillPatientStatementFrag");
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
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
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
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillPatientStatementFrag");
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
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
                        pd.dismiss();
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
                        builder.setMessage(activity.getResources().getString(R.string.unable_to_download))
                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (fragmentActionListener != null) {
                                            if(frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
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
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_no", phone_no);
                param.put("report_country",reportCountry);
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                if(frag.contentEquals("companyMainMenu")) {
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
