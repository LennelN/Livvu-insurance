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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentProcessCashedOutPoints extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private FragmentProcessCashedOutPoints mFragment;
    private static final String TAG = "ProcessCashedOutFrag";
    FragmentActionListener fragmentActionListener;
    String phone_no,merchantUserName,sending_frag,firstName,userRole,phoneNo,ip,userName,transNo,
            transAmount,URL,transactionNo,cashed_out_transaction_type,cashed_out_transaction_date,user_login_id,country,session_id,
    main_frag;
    TextView tvCashOutTransNo,tvCashOutTransAmount,tvSendHere,tvCancelHere;
    EditText etTransactionNo,etTransactionType,etTransactionAmt,etTransactionDate;
    double transaction_amount,cashed_out_transaction_amount,cash_out_transaction_amount;
    ProgressDialog pd;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public FragmentProcessCashedOutPoints() {
        // Required empty public constructor
    }


    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            transNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            transAmount = bundle.getString(FragmentActionListener.KEY_BILL_AMOUNT);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentProcessCashedOutPoints)getFragmentManager().findFragmentByTag("ProcessCashedOutPointsFrag");
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

    @Override
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
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            transNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            transAmount = bundle.getString(FragmentActionListener.KEY_BILL_AMOUNT);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView =  inflater.inflate(R.layout.fragment_process_cashed_out_points, container, false);

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

        tvCashOutTransNo = rootView.findViewById(R.id.tvCashOutTransNo);
        tvCashOutTransAmount= rootView.findViewById(R.id.tvCashOutTransAmount);
        etTransactionNo= rootView.findViewById(R.id.etTransactionNo);
        etTransactionNo.addTextChangedListener(registerTextWatcher);
        etTransactionType= rootView.findViewById(R.id.etTransactionType);
        etTransactionType.addTextChangedListener(registerTextWatcher);
        etTransactionAmt= rootView.findViewById(R.id.etTransactionAmt);
        etTransactionAmt.addTextChangedListener(registerTextWatcher);
        tvSendHere=rootView.findViewById(R.id.tvSendHere);
        tvCancelHere=rootView.findViewById(R.id.tvCancelHere);
        etTransactionDate = rootView.findViewById(R.id.etTransactionDate);
        etTransactionDate.addTextChangedListener(dateEditWatcher);
        etTransactionDate.addTextChangedListener(registerTextWatcher);
        String processingNo = activity.getResources().getString(R.string.trans_no_header)+ " " + transNo;
        String processingAmount = activity.getResources().getString(R.string.trans_no_amount)+ " " + transAmount;
        tvCashOutTransNo.setText(processingNo);
        tvCashOutTransAmount.setText(processingAmount);

        etTransactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                String date = String.format(Locale.getDefault(),"%02d",day) + "/"
                        + String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);
                etTransactionDate.setText(date);
            }
        };


        tvCancelHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {

                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 46);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashedOutPointsFrag");
                        fragmentActionListener.onActionPerformed(bundle);

                }
            }
        });

        tvSendHere.setOnClickListener(new View.OnClickListener() {
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

                URL=ip +"update_cashed_out_points.php";
                cashed_out_transaction_amount= Double.parseDouble(String.valueOf(etTransactionAmt.getText()));
                cash_out_transaction_amount= Double.parseDouble(transAmount);
                cashed_out_transaction_date=etTransactionDate.getText().toString();
                cashed_out_transaction_type=etTransactionType.getText().toString();
                transactionNo=etTransactionNo.getText().toString();

                if(cashed_out_transaction_amount > cash_out_transaction_amount){
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
                    txtview.setText(activity.getResources().getString(R.string.cashed_amount_greater_than_entered_amount));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });

                }else {

                    if(cashed_out_transaction_amount <= 0){
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
                        //txtview.setText(R.string.amount_must_be_greater_than_zero);
                        txtview.setText(activity.getResources().getString(R.string.amount_must_be_greater_than_zero));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });

                    }else {
                        pd.setMessage(activity.getResources().getString(R.string.processing_please_wait_dialog));
                        pd.show();

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
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phoneNo);
                                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessCashedOutPoints");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            } else {
                                                pd.dismiss();
                                                if (fragmentActionListener != null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phoneNo);
                                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessCashedOutPoints");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    fragmentActionListener.onActionPerformed(bundle);
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
                                        if (getContext() != null && isAdded()) {
                                            context = getContext();
                                        } else {
                                            context = getContextNullSafety();
                                        }
                                        if (getActivity() != null && isAdded()) {
                                            activity = getActivity();
                                        } else {
                                            activity = getActivityNullSafety();
                                        }

                                        pd.dismiss();
                                        if ((screenWidth == 240 && screenHeight == 320) || (screenWidth == 320 && screenHeight == 240)) {
                                            builder = new
                                                    AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                                        } else if ((screenWidth == 240 && screenHeight == 432) || (screenWidth == 432 && screenHeight == 240)
                                                || screenWidth == 720 || screenWidth == 1184) {
                                            builder = new
                                                    AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                                        } else if (screenWidth == 600 || screenWidth == 1024) {
                                            builder = new
                                                    AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                                        } else if ((screenWidth == 800 && screenHeight == 1232) || screenWidth == 1280) {
                                            builder = new
                                                    AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                                        } else {
                                            builder = new
                                                    AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                                        }
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 46);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashedOutPointsFrag");
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
                                param.put("username", user_login_id);
                                param.put("cash_out_transaction_no", transNo);
                                param.put("cashed_out_transaction_no", transactionNo);
                                param.put("cashed_out_transaction_type", cashed_out_transaction_type);
                                param.put("cashed_out_transaction_amount", String.valueOf(cashed_out_transaction_amount));
                                param.put("cashed_out_transaction_date", cashed_out_transaction_date);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley
                    }

                }



            }
        });
    }

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

            String userDateInput = etTransactionDate.getText().toString().trim();

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
                    etTransactionDate.setText(date);
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
                        etTransactionDate.setText(date);
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
                                etTransactionDate.setText(date);
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

            String transactionNoInput = etTransactionNo.getText().toString().trim();
            String transactionAmtInput = etTransactionAmt.getText().toString().trim();
            String paymentTypeInput = etTransactionType.getText().toString().trim();
            String paymentDateInput = etTransactionDate.getText().toString().trim();
            String clean = paymentDateInput.replaceAll("[^\\d.]", "");
            int cleanlength = clean.length();

            if(cleanlength == 8 && !transactionNoInput.isEmpty() && !transactionAmtInput.isEmpty()
                    &&  !paymentDateInput.isEmpty() && !paymentTypeInput.isEmpty())
            {
                tvSendHere.setEnabled(true);
                tvSendHere.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
               tvSendHere.setEnabled(false);
                tvSendHere.setTextColor(activity.getResources().getColor(R.color.colorCell));
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessCashedOutPoints");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessCashedOutPoints");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
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
