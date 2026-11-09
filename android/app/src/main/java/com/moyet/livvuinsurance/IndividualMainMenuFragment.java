package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateUtils;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class IndividualMainMenuFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestCountry;
    StringRequest stringRequestLogin;
    StringRequest stringRequestContact;
    StringRequest stringRequestBalance;
    private TextView welcomeMsg,textview;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "IndMainMenuFragment";
    boolean flag = false;
    View rootView;
    String firstName;
    String lastName;
    String userName;
    String ip,URL,errorMessage,name,server_url,session_id,server_logged_in,country_name,
            url_sms,sms_contact_phone_no,url_country_name;
    EditText etMenuChoice;
    FragmentActionListener fragmentActionListener;
    private ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    int c;
    Timer timer;

    public IndividualMainMenuFragment() {
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
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.stopTimer();
        }
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
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        if(screenWidth <= 240) {
            final int screenOrientationLandscape = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            rootView = inflater.inflate(R.layout.fragment_individual_main_menu, container, false);
        }else{
            rootView = inflater.inflate(R.layout.fragment_individual_main_menu, container, false);
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

        Date date = new Date();
        String localizedDate = DateUtils.formatDateTime(context, date.getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String localizedDateFormated = simpleDateFormat.format(date);
        String localizedTimeFormated = simpleTimeFormat.format(date);


        server_logged_in=ip +"check_login_status.php";

        checkLoginStatus(server_logged_in);

        url_country_name=ip +"get_country_name.php";

        if(country_name.isEmpty())
        {

            loadCountryName(url_country_name);
        }

        url_sms=ip +"get_country_sms_contact_phone_no.php";
        loadContactSMSNo(url_sms);

        name = firstName + " " + lastName;
        String welcomeMessage = activity.getResources().getString(R.string.welcome_space_2) +" "+ name;
        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        welcomeMsg.setText(welcomeMessage);

        textview = rootView.findViewById(R.id.textview);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM", Locale.getDefault());
        textview.setText(localizedTimeFormated);
        timer = new Timer(new Runnable() {
            public void run() {
                Date date = new Date();
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String localizedTimeFormated = simpleTimeFormat.format(date);
                textview.setText(localizedTimeFormated);
            }
        }, 60000, true);
        //blink();
        sendLink = rootView.findViewById(R.id.tvSendHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.setFocusable(true);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        URL = ip +"logout.php";
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

                stringRequestLogin = new StringRequest(Request.Method.POST, URL,
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
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenu");
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }

                                    } else {
                                        //boolean processingFlag = true;
                                        pd.dismiss();
                                        //builder = new AlertDialog.Builder(getActivity());
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
                                errorMessage=activity.getResources().getString(R.string.unable_to_sign_out_please_check_your_internet_connection_and_try_again);
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenu");
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
                        param.put("name",name);

                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestLogin);


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
                            bundle.putInt(FragmentActionListener.ACTION_KEY,97);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "EnterAmountIndividualFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 2:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,188);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TransferPhoneNoFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 3:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,189);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RequestBillFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
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
                                            String message_retrieved = jsonResponse.getString("message");
                                            String amount = jsonResponse.getString("amount");
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
                                            String message = activity.getResources().getString(R.string.you_have)
                                                    + " " + amount + " " +activity.getResources().getString(R.string.points_in_your_account);
                                            if (success) {
                                                pd.dismiss();
                                                if (fragmentActionListener!=null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE,message_retrieved);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"IndividualMainMenu");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            } else {
                                                pd.dismiss();
                                                if (fragmentActionListener!=null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"IndividualMainMenu");
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
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 34);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"IndividualMainMenu");
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

                                param.put("username",userName);
                                param.put("country_name",country_name);
                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestBalance); // vConnection i claas which used to connect volley

                        break;

                    case 5:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,22);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ClientStatementFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 6:
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country_name);
                            bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenu");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

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

    private void loadCountryName(String url_country_name) {

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
                                    country_name = jsonResponse.getString("user_country");
                                }else{

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
                                        dialog.dismiss();
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
                param.put("phone_number", userName);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestCountry);

    }

    private void blink() {

        final Handler handler = new Handler();

        new Thread(new Runnable()
        {

            @Override

            public void run()
            {

                try {

                    Thread.sleep(60000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                handler.post(new Runnable() {

                    @Override
                    public void run()
                    {

                        Date date = new Date();
                        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String localizedTimeFormated = simpleTimeFormat.format(date);
                        textview.setText(localizedTimeFormated);

                        blink();

                    }

                });

            }

        }).start();


    }


    private void loadContactSMSNo(String url_sms) {

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
                                        dialog.dismiss();
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
                param.put("country", country_name);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestContact);

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
                                if (fragmentActionListener!=null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY,99);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"IndividualMainMenu");
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseLogoutSessionFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
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
                        errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "IndividualMainMenu");
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

    public class Timer {

        private Handler handler;
        private boolean paused;
        private int interval;
        private Runnable task = new Runnable () {
            @Override
            public void run() {
                if (!paused) {
                    runnable.run ();
                    Timer.this.handler.postDelayed (this, interval);
                }
            }
        };
        private Runnable runnable;
        public int getInterval() {
            return interval;
        }
        public void setInterval(int interval) {
            this.interval = interval;
        }
        public void startTimer () {
            paused = false;
            handler.postDelayed (task, interval);
        }
        public void stopTimer () {
            paused = true;
        }
        public Timer (Runnable runnable, int interval, boolean started) {
            handler = new Handler ();
            this.runnable = runnable;
            this.interval = interval;
            //https://riptutorial.com/ 680
            if (started)
                startTimer ();
        }
    }

    

}
