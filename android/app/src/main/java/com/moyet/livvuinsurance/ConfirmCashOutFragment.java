package com.moyet.livvuinsurance;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class ConfirmCashOutFragment extends Fragment {

    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private static final String TAG = "ConfirmCashOutFragment";
    private ConfirmCashOutFragment mFragment;
    FragmentActionListener fragmentActionListener;
    String URL;
    boolean screenChanged = false;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    String message;
    String transactionNo;
    String ip,frag,user_login_key,user_role,user_login_id,nameOfUser,country,phone_no,cashOutDate,session_id;
    EditText etDate;
    TextView welcomeMsg;
    TextView sendLink;
    TextView cancelLink;
    ProgressDialog pd;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public ConfirmCashOutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            mFragment = (ConfirmCashOutFragment)getFragmentManager().findFragmentByTag("ConfirmCashOutFrag");
        }
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            frag= bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            transactionNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);

            if(frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }

            welcomeMsg.setText(message);

        }
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
            frag= bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            transactionNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);

            if(frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
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

        //if(savedInstanceState==null) {
            rootView = inflater.inflate(R.layout.fragment_confirm_cash_out, container, false);

        if(savedInstanceState==null) {
            initUI();
        }else {

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

            screenChanged=true;
            welcomeMsg=rootView.findViewById(R.id.tvWelcomeMsg);
            mDisplayDate = rootView.findViewById(R.id.etDate);

            if(screenChanged){
                mDisplayDate.setText(cashOutDate);
            }


            mDisplayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                    dialog.show();
                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;

                    String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                            String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                    cashOutDate=date;
                    mDisplayDate.setText(date);
                }
            };



            // etDate.addTextChangedListener(dateWatcher);
            mDisplayDate.addTextChangedListener(registerTextWatcher);
            mDisplayDate.addTextChangedListener(dateEditWatcher);
            sendLink = rootView.findViewById(R.id.tvSendHere);
            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            //welcomeMsg.setText((R.string.enter_phone_no));
            cancelLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    Toast.makeText(context,activity.getResources().getString(R.string.trans_cancelled), Toast.LENGTH_LONG).show();

                    if (fragmentActionListener!=null) {
                        if(frag.contentEquals("companyMainMenu")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }


                }
            });

            sendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                    URL=ip +"update_cash_out_payments.php";
                    stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    //JSONArray jsonArray = null;
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                        boolean success = jsonResponse.getBoolean("success");
                                        String message = jsonResponse.getString("message");

                                        if (success) {
                                            pd.dismiss();
                                            if (fragmentActionListener != null) {
                                                if(frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                    fragmentActionListener.onActionPerformed(bundle);

                                                }
                                            }
                                        } else {
                                            pd.dismiss();
                                            if (fragmentActionListener != null) {
                                                if(frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
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
                                    builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                            .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(frag.contentEquals("companyMainMenu")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                        fragmentActionListener.onActionPerformed(bundle);
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
                            param.put("username", merchantUserName);
                            param.put("cashed_out_transaction_no",transactionNo);
                            param.put("paid_out_date", mDisplayDate.getText().toString().trim());
                            param.put("merchant_code",merchantCode);

                            return param;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley

                }
            });

        }
        //}
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

        //etDate = rootView.findViewById(R.id.etDate);
        welcomeMsg=rootView.findViewById(R.id.tvWelcomeMsg);
        mDisplayDate = rootView.findViewById(R.id.etDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                        String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                cashOutDate=date;
                mDisplayDate.setText(date);

            }
        };


        // etDate.addTextChangedListener(dateWatcher);
        mDisplayDate.addTextChangedListener(registerTextWatcher);
        mDisplayDate.addTextChangedListener(dateEditWatcher);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        //welcomeMsg.setText((R.string.enter_phone_no));
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Toast.makeText(context,activity.getResources().getString(R.string.trans_cancelled), Toast.LENGTH_LONG).show();

                if (fragmentActionListener!=null) {
                    if(frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }


            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                URL=ip +"update_cash_out_payments.php";
                stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //JSONArray jsonArray = null;
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                    boolean success = jsonResponse.getBoolean("success");
                                    String message = jsonResponse.getString("message");

                                    if (success) {
                                        pd.dismiss();
                                        if (fragmentActionListener != null) {
                                            if(frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                fragmentActionListener.onActionPerformed(bundle);

                                            }
                                        }
                                    } else {
                                        pd.dismiss();
                                        if (fragmentActionListener != null) {
                                            if(frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
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
                                builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                        .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
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
                        param.put("username", merchantUserName);
                        param.put("cashed_out_transaction_no",transactionNo);
                        param.put("paid_out_date", mDisplayDate.getText().toString().trim());
                        param.put("merchant_code",merchantCode);

                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley

            }
        });
    }


    private TextWatcher registerTextWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            String userDateInput = mDisplayDate.getText().toString().trim();
            String clean = userDateInput.replaceAll("[^\\d.]", "");
            int cleanlength = clean.length();
            String lenofDate;

            if(cleanlength>6) {
                sendLink.setEnabled(true);
                sendLink.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                sendLink.setEnabled(false);
                sendLink.setTextColor(activity.getResources().getColor(R.color.colorCell));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher dateEditWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            String userDateInput = mDisplayDate.getText().toString().trim();

            if(userDateInput.length() > 0)
            {
                String clean = userDateInput.replaceAll("[^\\d.]", "");

                int inputDay =Integer.parseInt(clean.substring(0, 2));
                int inputYear=Integer.parseInt(clean.substring(4, 8));
                int inputMonth = Integer.parseInt(clean.substring(2, 4));

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                        String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                if(inputYear > year){
                    Dialog epicDialog = null;

                        if(getContext() != null && isAdded()) {
                            epicDialog = new Dialog(getContext());
                        }else{
                            context = getContextNullSafety();
                            epicDialog = new Dialog(context);
                        }

                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }

                    epicDialog.setContentView(R.layout.dialog_my_custom_message);
                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                    txtview.setText(activity.getResources().getString(R.string.year_cannot_be_greater_than_current_year));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    mDisplayDate.setText(date);
                }else if(inputYear == year){
                    if(inputMonth > month)
                    {
                        Dialog epicDialog = null;

                        if(getContext() != null && isAdded()) {
                            epicDialog = new Dialog(getContext());
                        }else{
                            context = getContextNullSafety();
                            epicDialog = new Dialog(context);
                        }

                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }

                        epicDialog.setContentView(R.layout.dialog_my_custom_message);
                        TextView txtview = epicDialog.findViewById(R.id.message_text);
                        TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                        txtview.setText(activity.getResources().getString(R.string.month_cannot_be_greater_current_month));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                        mDisplayDate.setText(date);
                    }else{
                        if(inputMonth > month)
                        {
                            Dialog epicDialog = null;

                        if(getContext() != null && isAdded()) {
                            epicDialog = new Dialog(getContext());
                        }else{
                            context = getContextNullSafety();
                            epicDialog = new Dialog(context);
                        }

                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }

                            epicDialog.setContentView(R.layout.dialog_my_custom_message);
                            TextView txtview = epicDialog.findViewById(R.id.message_text);
                            TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                            txtview.setText(activity.getResources().getString(R.string.month_cannot_be_greater_current_month));

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });
                            mDisplayDate.setText(date);
                        }else{
                            if(inputMonth == month) {
                                if (inputDay > day) {
                                    Dialog epicDialog = null;

                        if(getContext() != null && isAdded()) {
                            epicDialog = new Dialog(getContext());
                        }else{
                            context = getContextNullSafety();
                            epicDialog = new Dialog(context);
                        }

                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }

                                    epicDialog.setContentView(R.layout.dialog_my_custom_message);
                                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                    txtview.setText(activity.getResources().getString(R.string.day_cannot_be_greater_tan_today));

                                    epicDialog.show();
                                    Dialog finalEpicDialog = epicDialog;
                                    action_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finalEpicDialog.dismiss();
                                        }
                                    });

                                    mDisplayDate.setText(date);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
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

                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ConfirmCashOutFragment");
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
