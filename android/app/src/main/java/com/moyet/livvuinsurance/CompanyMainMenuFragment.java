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
import android.util.Log;
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

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class CompanyMainMenuFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "ComMainMenuFrag";
    boolean flag = false;
    View rootView;
    String merchantName,session_id;
    String merchantCode;
    String merchantUserName,URL,errorMessage,ip,country,server_url,url_sms,sms_contact_phone_no,url_country_name;
    EditText etMenuChoice;
    FragmentActionListener fragmentActionListener;
    private ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    StringRequest stringRequest;
    StringRequest stringRequestCountry;
    StringRequest stringRequestLogin;
    StringRequest stringRequestContact;
    StringRequest stringRequestBalance;

    public CompanyMainMenuFragment() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(stringRequest != null){
          stringRequest.cancel();
        }
        if(stringRequestCountry != null) {
            stringRequestCountry.cancel();
        }
        if(stringRequestLogin != null) {
            stringRequestLogin.cancel();
        }
        if(stringRequestContact != null) {
            stringRequestContact.cancel();
        }
        if(stringRequestBalance != null) {
            stringRequestBalance.cancel();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
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
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_company_main_menu, container, false);
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

        url_sms=ip +"get_country_sms_contact_phone_no.php";
        loadContactSMSNo(url_sms);
        url_country_name=ip +"get_country_name.php";
        if(country.isEmpty())
        {
            loadCountryName(url_country_name);
        }

        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        String createWelcomeMessage = activity.getResources().getString(R.string.welcome_space_2) +" "+ merchantName + " " + merchantCode;
        welcomeMsg.setText(createWelcomeMessage);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL = ip +"logout.php";
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getContext() != null && isAdded()){
                    context=getContext();
                }else{
                    context=getContextNullSafety();
                }

                if(getActivity() != null && isAdded()){
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
                pd.setMessage(activity.getResources().getString(R.string.processing));
                pd.show();

                stringRequest = new StringRequest(Request.Method.POST, URL,
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
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }

                                    } else {
                                        //boolean processingFlag = true;
                                        pd.dismiss();

                                        Dialog epicDialog = null;

                                        if(getContext() != null && isAdded()) {
                                            epicDialog = new Dialog(getContext());
                                        }else{
                                            context = getContextNullSafety();
                                            epicDialog = new Dialog(context);
                                        }

                                        epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                                        TextView txtview = epicDialog.findViewById(R.id.message_text);
                                        TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                        txtview.setText(message);

                                        epicDialog.show();
                                        Dialog finalEpicDialog = epicDialog;
                                        action_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finalEpicDialog.dismiss();
                                            }
                                        });

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
                                errorMessage=activity.getResources().getString(R.string.unable_to_sign_out_please_check_your_internet_connection_and_try_again);
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                                fragmentActionListener.onActionPerformed(bundle);
                                error.printStackTrace();
                                //////error.getMessage();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("username",merchantUserName);
                        param.put("name",merchantName);

                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequest);

            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);

                final int choice = Integer.parseInt(etMenuChoice.getText().toString());
                switch (choice){

                    case 1:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,8);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PhoneNoFrag");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 2:

                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,12);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CreateBillFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 3:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,28);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PointsCashOutFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                     break;

                    case 4:

                        if(getContext() != null && isAdded()){
                            context=getContext();
                        }else{
                            context=getContextNullSafety();
                        }

                        if(getActivity() != null && isAdded()){
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
                        server_url=ip +"check_points_balance.php";

                        stringRequestBalance = new StringRequest(Request.Method.POST, server_url,
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
                                                if (fragmentActionListener!=null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CompanyMainMenu");
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            } else {
                                                pd.dismiss();
                                                if (fragmentActionListener!=null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CompanyMainMenu");
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
                                        pd.dismiss();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 93);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CompanyMainMenu");
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "NoDataCompanyFrag");
                                        fragmentActionListener.onActionPerformed(bundle);
                                        error.printStackTrace();
                                        //////error.getMessage();
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("username",merchantUserName);
                                param.put("country_name",country);
                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestBalance); // vConnection i claas which used to connect volley


                        break;

                    case 5:

                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,6);//FragmentActionListener.ACTION_VALUE_REGTYPE_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BankingServFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            fragmentActionListener.onActionPerformed(bundle);

                        }
                        break;

                    case 6:

                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            fragmentActionListener.onActionPerformed(bundle);

                        }
                        break;

                    case 7:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 8:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 9:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 171);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CapturePatientDetailsFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 10:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    default:
                        //if(getActivity() != null) {

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
                        //}

                        break;

                }
            }
        });


    }

    private void loadCountryName(String url_country_name) {

        if(getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        stringRequestCountry = new StringRequest(Request.Method.POST, url_country_name,
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
                                if(success) {
                                    country = jsonResponse.getString("user_country");
                                }else{

                                    if(getActivity() != null && isAdded()){
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
                                    builder.setMessage(message)
                                            .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create()
                                            .show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(getActivity() != null && isAdded()){
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
                        builder.setMessage(activity.getResources().getString(R.string.unable_to_download_country_details))
                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                        error.printStackTrace();
                        //////error.getMessage();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", merchantUserName);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestCountry);


    }

    private void loadContactSMSNo(String url_sms) {

        if(getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        stringRequestContact = new StringRequest(Request.Method.POST, url_sms,
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
                                    sms_contact_phone_no = jsonResponse.getString("contact_phone_no");
                                } else {
                                    sms_contact_phone_no = activity.getResources().getString(R.string.mobile);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(getActivity() != null && isAdded()){
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
                        builder.setMessage(activity.getResources().getString(R.string.unable_to_download_sms_contact_no_details))
                                .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                        error.printStackTrace();
                        //////error.getMessage();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("country", country);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestContact);

    }

    private void checkLoginStatus(String server_logged_in) {

        if(getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        stringRequestLogin = new StringRequest(Request.Method.POST, server_logged_in,
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
                                bundle.putInt(FragmentActionListener.ACTION_KEY,99);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseLogoutSessionFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CompanyMainMenu");
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } catch (JSONException e) {

                            //pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pd.dismiss();
                        if(getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }
                        errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CompanyMainMenu");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
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
        Vconnection.getnInstance(activity).addRequestQue(stringRequestLogin); // vConnection i claas which used to connect volley


    }

    private TextWatcher registerTextWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String choice = etMenuChoice.getText().toString();
            int cleanlength = choice.length();
            if(cleanlength>0) {
                sendLink.setEnabled(true);
                sendLink.setTextColor(getResources().getColor(R.color.colorBlack));
            }else{
                sendLink.setEnabled(false);
                sendLink.setTextColor(getResources().getColor(R.color.colorCell));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
