package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class ClientStatementFragment extends Fragment {

    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestContact;
    View rootView;
    TextView footer_debit,footer_credit,footer_balance,footer_balance_120days,footer_balance_90days,footer_balance_60days,footer_balance_30days,total;
    private ClientStatementFragment mFragment;
    private static final String TAG = "ClientStatementFrag";
    ArrayList<ClientStatementDetails> itemList;
    ArrayList<String> listViewItems;
    String URL;
    FragmentActionListener fragmentActionListener;
    ListView lv;
    listviewClientStatementAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String transaction_type,credit,date_paid,phone_number,merchant_name,merchant_phone_number,bill_date;
    String bill_number;
    double total_bill;
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
    String ip,session_id,country_name;
    private  int screenWidth,screenHeight;

    public ClientStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (ClientStatementFragment)getFragmentManager().findFragmentByTag("ClientStatementFrag");
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
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
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
            firstName=bundle.getString(FragmentActionListener.KEY_NAME);
            lastName=bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName=bundle.getString(FragmentActionListener.KEY_USERNAME);
            phone_no=bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            bill_number=bundle.getString(FragmentActionListener.KEY_BILL_NO);
            merchant_name=bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            bill_date=bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            ip=bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView =  inflater.inflate(R.layout.fragment_client_statement, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName=bundle.getString(FragmentActionListener.KEY_NAME);
            lastName=bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName=bundle.getString(FragmentActionListener.KEY_USERNAME);
            phone_no=bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            bill_number=bundle.getString(FragmentActionListener.KEY_BILL_NO);
            merchant_name=bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            bill_date=bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
            ip=bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);

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

        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);

        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<ClientStatementDetails>();
        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        String welcomeMessage = "Statement For Client " + firstName + " "+ lastName;
        welcomeMsg.setText(welcomeMessage);
        welcomeMsg.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL=ip +"get_client_statement.php";
        loadClientStatementData(URL);
        //View headerView = getLayoutInflater().inflate(R.layout.listview_header_client_statement_mini, null);
        //lv.addHeaderView(headerView);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_client_statement_mini, null);
        cancel = footerView.findViewById(R.id.cancel);
        lv.addFooterView(footerView, null, false);
        adapter = new listviewClientStatementAdapter(context, itemList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lv.setOnItemLongClickListener(this);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });



    }
    private void loadClientStatementData(String url) {

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
                                    transaction_type = jsonResponse.getString("transaction_type");
                                    date_paid = jsonResponse.getString("date_paid");
                                    phone_number = jsonResponse.getString("phone_number");
                                    merchant_phone_number = jsonResponse.getString("merchant_phone_number");
                                    no_of_points_paid = jsonResponse.getString("no_of_points_paid");
                                    credit= jsonResponse.getString("Credit");

                                    ClientStatementDetails clientStatementDetails = new ClientStatementDetails(transaction_type,date_paid, phone_number,merchant_phone_number,no_of_points_paid,credit);

                                    itemList.add(clientStatementDetails);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    pd.dismiss();
                                    if (fragmentActionListener != null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                        bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ClientStatementFrag");
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
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
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 34);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                        bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ClientStatementFrag");
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "noDataConnectionFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", userName);
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ClientStatementFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ClientStatementFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username",userName);
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
