package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentProcessSelectedPointsSouthAfricaPurchases extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    FragmentActionListener fragmentActionListener;
    String merchantName,merchantCode,merchantUserName,ip;
    View rootView;
    private static final String TAG = "ProcSeleSAPurchaes";
    private FragmentProcessSelectedPointsSouthAfricaPurchases mFragment;
    String URL;
    TextView welcomeMsg;
    TextView textView;
    TextView textView1;
    TextView ok;
    private TextView sendLink;
    private TextView cancelLink;
    EditText etMenuChoice;
    String transactionNumber,transactionCurrency,transactionType,purchase_by,dataArray,session_id,country_name;
    double purchaseAmount;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public FragmentProcessSelectedPointsSouthAfricaPurchases() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            dataArray= bundle.getString(FragmentActionListener.KEY_NEW_DATA_ARRAY);
            purchaseAmount=bundle.getDouble(FragmentActionListener.KEY_PURCHASE_AMOUNT);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

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
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            dataArray= bundle.getString(FragmentActionListener.KEY_NEW_DATA_ARRAY);
            purchaseAmount=bundle.getDouble(FragmentActionListener.KEY_PURCHASE_AMOUNT);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView =  inflater.inflate(R.layout.fragment_process_selected_points_purchases_south_africa, container, false);
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


        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        textView= rootView.findViewById(R.id.textView);
        textView1= rootView.findViewById(R.id.textView1);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.setFocusable(true);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL=ip +"enter_part_society_company_points_purchased.php";
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }


            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
                Random generator = new Random();
                int i = 0;
                int length = 6;
                StringBuilder stringBuilder = new StringBuilder();
                while(i < length){
                    char temp =(char) (generator.nextInt(92)+32);
                    if(Character.isDigit(temp))
                    {
                        stringBuilder.append(temp);
                        ++i;
                    }
                }
                final int choice = Integer.parseInt(etMenuChoice.getText().toString());
                switch (choice) {

                    case 1:
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

                        transactionNumber="REFTSA"+stringBuilder;
                        transactionType="EFT";
                        purchase_by="EFT";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;

                    case 2:

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

                        //transactionCurrency="ZWL";
                        transactionNumber="ASAO"+stringBuilder;
                        transactionType="Airtime";
                        purchase_by="Econet";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;

                    case 3:

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

                        transactionNumber="Cre3"+stringBuilder;
                        transactionType="CreditCard";
                        purchase_by="CreditCard";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);

                        break;

                    case 4:

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

                        //transactionCurrency="ZWL";
                        transactionNumber="RMSA0"+stringBuilder;
                        transactionType="MTNMomo";
                        purchase_by="MTNMomo";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;

                    case 5:

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

                        //transactionCurrency="ZWL";
                        transactionNumber="REaSA0"+stringBuilder;
                        transactionType="EasyPay";
                        purchase_by="EasyPay";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;

                    case 6:
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

                        //transactionCurrency="ZWL";
                        transactionNumber="RSAP0"+stringBuilder;
                        transactionType="PayNowSA";
                        purchase_by="PayNowSA";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;

                    case 7:

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

                        //transactionCurrency="ZWL";
                        transactionNumber="MSA0"+stringBuilder;
                        transactionType="Mukuru";
                        purchase_by="Mukuru";
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
                        pd.setMessage(activity.getResources().getString(R.string.processing));
                        pd.show();
                        stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
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
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                } else {
                                                    pd.dismiss();
                                                    if (fragmentActionListener!=null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 67);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                                        builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (fragmentActionListener != null) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }                                                        }
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
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("merchant_code", merchantCode);
                                param.put("transaction_type", transactionType);
                                param.put("transaction_number",transactionNumber);
                                param.put("purchase_amount", String.valueOf(purchaseAmount));
                                param.put("array",dataArray);
                                param.put("purchase_by",purchase_by);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                        break;



                    default:
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
                        txtview.setText(activity.getResources().getString(R.string.invalid_choice));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                        break;

                }
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
            String choice = etMenuChoice.getText().toString();
            int cleanlength = choice.length();
            if(cleanlength>0) {
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ProcessSelectedPointsSAFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username",merchantUserName);
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
