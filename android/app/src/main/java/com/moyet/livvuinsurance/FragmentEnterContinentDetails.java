package com.moyet.livvuinsurance;


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


public class FragmentEnterContinentDetails extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    FragmentActionListener fragmentActionListener;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "EnterContinentDetails";
    boolean flag = false;
    String firstName;
    String lastName;
    String userName,phoneNo,userRole,errorMessage,URL,user_login_id,country,country_region,country_continent;
    String ip,url;
    private ProgressDialog pd;
    private EditText etCountryName,etCountryRegion,etCountryContinent;
    String country_name,country_code,session_id,frag,main_frag;
    double currency_rate;
    int no_of_digits;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;


    public FragmentEnterContinentDetails() {
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

        rootView = inflater.inflate(R.layout.fragment_enter_continent_details, container, false);
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


        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        etCountryName = rootView.findViewById(R.id.etCountryName);
        etCountryName.addTextChangedListener(registerTextWatcher);
        etCountryRegion= rootView.findViewById(R.id.etCountryRegion);
        etCountryRegion.addTextChangedListener(registerTextWatcher);
        url=ip +"insert_country_region_continent_details.php";
        etCountryName.requestFocus();
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if(frag.contentEquals("SystemMaintenanceFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 90);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SystemMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 122);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuSystemMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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

                country_name = etCountryName.getText().toString();
                country_region = etCountryRegion.getText().toString();

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
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"EnterContinentDetailsFrag");
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
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"EnterContinentDetailsFrag");
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
                                                    if(frag.contentEquals("SystemMaintenanceFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 90);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SystemMaintenanceFrag");
                                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 122);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuSystemMaintenanceFrag");
                                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
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
                        param.put("country_name",country_name);
                        param.put("country_region",country_region);

                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
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
            String countryNameInput = etCountryName.getText().toString().trim();
            String countryRegionInput = etCountryRegion.getText().toString().trim();

            if(!countryNameInput.isEmpty()
                    && !countryRegionInput.isEmpty())
            {
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"EnterContinentDetailsFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"EnterContinentDetailsFrag");
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
