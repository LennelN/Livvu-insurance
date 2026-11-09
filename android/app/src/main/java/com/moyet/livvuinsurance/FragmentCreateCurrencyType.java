package com.moyet.livvuinsurance;


import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;


public class FragmentCreateCurrencyType extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    View rootView;
    FragmentActionListener fragmentActionListener;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "CreateCurrTypeFrag";
    boolean flag = false;
    String firstName;
    String lastName;
    String userName,phoneNo,userRole,errorMessage,URL,user_login_id,country;
    String ip,url;
    EditText etMenuChoice;
    private ProgressDialog pd;
    private EditText etCurrencyType,etCurrencyRate,etNoOfPoints;
    String currency_type;
    double currency_rate;
    int no_of_points;
    Spinner spinner;
    int key_country_position;
    boolean screenChanged=false;
    ArrayList<String> CountryName;
    String url_spinner,country_name,key_country_name,session_id,main_frag,frag;
    private FragmentCreateCurrencyType mFragment;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    private EditText etCurrencyName,etCurrencyCode,etCurrencySymbol;
    String login_branch,type_of_reg,merchantCode,merchantUserName,merchantName,user_role,
            url_insert,currency_name,currency_code,currency_symbol;


    public FragmentCreateCurrencyType() {
        // Required empty public constructor
    }


    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentCreateCurrencyType) getFragmentManager().findFragmentByTag("CreateCurrencyTypeFrag");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
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
        if(stringRequestSpinner != null){
            stringRequestSpinner.cancel();
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
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_enter_currency_type, container, false);
        if(savedInstanceState==null) {
            initUI();
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

            String server_logged_in = ip + "check_login_status.php";
            checkLoginStatus(server_logged_in);


            screenChanged=true;
            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            sendLink = rootView.findViewById(R.id.tvSendHere);
            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            etCurrencyName = rootView.findViewById(R.id.etCurrencyName);
            etCurrencyName.addTextChangedListener(registerTextWatcher);
            etCurrencyCode = rootView.findViewById(R.id.etCurrencyCode);
            etCurrencyCode.addTextChangedListener(registerTextWatcher);
            etCurrencySymbol = rootView.findViewById(R.id.etCurrencySymbol);
            etCurrencySymbol.addTextChangedListener(registerTextWatcher);

            url_insert=ip+"insert_currency_types.php";

            url=ip +"create_currency_rate.php";
            url_spinner=ip +"spinner_country.php";
            etCurrencyName.requestFocus();
            CountryName = new ArrayList<>();
            spinner = rootView.findViewById(R.id.country_Name);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            //spinner.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            loadSpinnerData(url_spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(screenChanged) {
                        spinner.setSelection(key_country_position);
                        screenChanged=false;
                    }else key_country_position = i;

                    country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    if (!flag)
                        flag = true;
                    else
                        sendLink.requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });

            cancelLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        fragmentActionListener.onActionPerformed(bundle);
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
                    pd.setMessage(activity.getResources().getString(R.string.processing));
                    pd.show();

                    currency_name = etCurrencyName.getText().toString();
                    currency_code = etCurrencyCode.getText().toString();
                    currency_symbol = etCurrencySymbol.getText().toString();


                    stringRequestAdapter = new StringRequest(Request.Method.POST, url_insert,
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
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuResponseFrag");
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }

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
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
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
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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
                            param.put("currency_name", currency_name);
                            param.put("currency_code", currency_code);
                            param.put("currency_symbol", currency_symbol);
                            param.put("currency_country",country_name);
                            param.put("username", user_login_id);
                            return param;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                }
            });


        }
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


        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etCurrencyName = rootView.findViewById(R.id.etCurrencyName);
        etCurrencyName.addTextChangedListener(registerTextWatcher);
        etCurrencyCode = rootView.findViewById(R.id.etCurrencyCode);
        etCurrencyCode.addTextChangedListener(registerTextWatcher);
        etCurrencySymbol = rootView.findViewById(R.id.etCurrencySymbol);
        etCurrencySymbol.addTextChangedListener(registerTextWatcher);

        url_insert=ip+"insert_currency_types.php";

        url=ip +"create_currency_rate.php";
        url_spinner=ip +"spinner_country.php";
        etCurrencyName.requestFocus();
        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        //spinner.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        loadSpinnerData(url_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                key_country_position = i;

                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                key_country_name=country_name;
                if (!flag)
                    flag = true;
                else
                    sendLink.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
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
                pd.setMessage(activity.getResources().getString(R.string.processing));
                pd.show();

                currency_name = etCurrencyName.getText().toString();
                currency_code = etCurrencyCode.getText().toString();
                currency_symbol = etCurrencySymbol.getText().toString();

                stringRequestAdapter = new StringRequest(Request.Method.POST, url_insert,
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
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
                                                bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuResponseFrag");
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }

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
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
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
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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
                        param.put("currency_name", currency_name);
                        param.put("currency_code", currency_code);
                        param.put("currency_symbol", currency_symbol);
                        param.put("currency_country",country_name);
                        param.put("username", user_login_id);
                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
            }
        });

    }

    private void loadSpinnerData(String url) {

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

        stringRequestSpinner = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int cnt = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("country_name");
                        cnt = cnt + 1;
                        CountryName.add(country);
                        //   }
                    }
                    //spinner.setAdapter(new ArrayAdapter<String>(context(), android.R.layout.simple_spinner_dropdown_item, CountryName));
                    //spinner.setDropDownViewResource(R.layout.my_spinner);
                    if(cnt > 0) {
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
                        ArrayAdapter<String> spinnerArrayAdapter =
                                new ArrayAdapter<String>(context, R.layout.my_spinner, CountryName);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinner); // vConnection i claas which used to connect volley

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

            String userCurrencyName = etCurrencyName.getText().toString();
            String userCurrencyCode = etCurrencyCode.getText().toString();
            String userCurrencySymbol = etCurrencySymbol.getText().toString();


            if(!userCurrencyName.isEmpty() && !userCurrencyCode.isEmpty()
                    && !userCurrencySymbol.isEmpty() && country_name != null)
            {
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"CreateCurrencyTypeFrag");
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
