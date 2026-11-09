package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
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


public class BillStatementFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    TextView footer_debit,footer_credit,footer_balance,footer_balance_120days,footer_balance_90days,footer_balance_60days,footer_balance_30days,total;
    private BillStatementFragment mFragment;
    private static final String TAG = "BillStatementFrag";
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
    String firstName;
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
    String ip,session_id,country_name,sending_frag,main_frag,phoneNo,userRole,user_login_id,country;
    private  int screenWidth,screenHeight;
    double p1,p2,p3,p4;
    BigDecimal b1,b2,b3,b4;

    public BillStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (BillStatementFragment)getFragmentManager().findFragmentByTag("BillClientStatementFrag");
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView =  inflater.inflate(R.layout.fragment_bill_statement, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
        }

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
        String substring_day = bill_date.substring(0,2);
        //String strDay = substr(bill_date, 0, 4);
        String substring_month = bill_date.substring(3,5);
        String substring_year = bill_date.substring(6,10);
        strBillDate=  substring_day + "/" + substring_month + "/"+ substring_year;
        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<BillStatementDetails>();
        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        String msg = activity.getResources().getString(R.string.statement_for_merchant) + " " +
                merchant_name + " "+ activity.getResources().getString(R.string.bill_number_header)+" "+bill_number;
        welcomeMsg.setText(msg);
        welcomeMsg.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL=ip +"bill_get_client_statement.php";
        loadBillClientData(URL);
        //View headerView = getLayoutInflater().inflate(R.layout.listview_header_client_statement, null);
        //lv.addHeaderView(headerView);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_client_statement, null);
        cancel = footerView.findViewById(R.id.cancel);
        footer_debit = footerView.findViewById(R.id.footer_debit);
        footer_credit = footerView.findViewById(R.id.footer_credit);
        footer_balance= footerView.findViewById(R.id.footer_balance);
        footer_balance_120days= footerView.findViewById(R.id.footer_balance_120days);
        footer_balance_90days= footerView.findViewById(R.id.footer_balance_90days);
        footer_balance_60days= footerView.findViewById(R.id.footer_balance_60days);
        footer_balance_30days= footerView.findViewById(R.id.footer_balance_30days);
        total = footerView.findViewById(R.id.total);
        footer_balance.setText(String.valueOf(balance));
        lv.addFooterView(footerView, null, false);
        adapter = new listviewStatementAdapter(context, itemList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lv.setOnItemLongClickListener(this);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(main_frag.contentEquals("LivvuMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 121);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAccountsMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
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

                                    p1=Double.parseDouble(no_of_points);
                                    p2=Double.parseDouble(no_of_points_paid);
                                    b1=BigDecimal.valueOf(p1);
                                    b2=BigDecimal.valueOf(p2);
                                    b3 = b1.subtract(b2);
                                    b4 = b4.add(b3);

                                    balance = balance.add(b3);
                                    debit_total = debit_total.add(b1);
                                    //balance = balance + Integer.parseInt(no_of_points) - Integer.parseInt(no_of_points_paid);
                                    //debit_total = debit_total + Integer.parseInt(no_of_points);
                                    //credit_total= credit_total + Integer.parseInt(no_of_points_paid);
                                    credit_total= credit_total.add(b2);
                                    footer_debit.setText(String.valueOf(debit_total));
                                    footer_credit.setText(String.valueOf(credit_total));

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

                                    try{
                                        Date dateAfterConversion = simpleDateFormat.parse(strDate);
                                        Date dateAfter=simpleDateFormat.parse(strBillDate);
                                       long difference = dateAfterConversion.getTime() - dateAfter.getTime();
                                        daysBetween = (difference/(1000*60*60*24));


                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    footer_balance_120days.setText(String.valueOf(balance_120_days));
                                    footer_balance_90days.setText(String.valueOf(balance_90_days));
                                    footer_balance_60days.setText(String.valueOf(balance_60_days));
                                    footer_balance_30days.setText(String.valueOf(balance_30_days));
                                    footer_balance.setText(String.valueOf(balance_current));

                                    if(daysBetween < 30)
                                    {
                                        footer_balance.setText(String.valueOf(balance));
                                    }else{
                                        if(daysBetween >= 30 && daysBetween <60)
                                        {
                                            footer_balance_30days.setText(String.valueOf(balance));
                                        }else{
                                            if(daysBetween >= 60 && daysBetween <90) {
                                                footer_balance_60days.setText(String.valueOf(balance));
                                            }else{
                                                if(daysBetween >= 90 && daysBetween <120) {
                                                    footer_balance_90days.setText(String.valueOf(balance));
                                                }else{
                                                    if(daysBetween >= 120) {
                                                        footer_balance_120days.setText(String.valueOf(balance));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    //

                                    BillStatementDetails billStatementDetails = new BillStatementDetails(transaction_date,tariff_code, description,no_of_points,no_of_points_paid);

                                    itemList.add(billStatementDetails);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    pd.dismiss();
                                    if (fragmentActionListener != null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY,82);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"BillStatementFrag");
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuResponseFrag");
                                        fragmentActionListener.onActionPerformed(bundle);
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
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("bill_number", bill_number);
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"BillStatementFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"BillStatementFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username",user_login_id);
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
