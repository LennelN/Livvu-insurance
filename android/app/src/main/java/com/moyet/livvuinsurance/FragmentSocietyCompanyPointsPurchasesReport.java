package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentSocietyCompanyPointsPurchasesReport extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    FragmentActionListener fragmentActionListener;
    String merchantName,merchantCode,merchantUserName,ip;
    View rootView;
    private static final String TAG = "SociCompPoinPurcRepFrag";
    private FragmentSocietyCompanyPointsPurchasesReport mFragment;
    TextView cancelLink,sendLink;
    ProgressDialog pd;
    String URL_MOBILE_OPERATORS,URL,country_name,mobile,session_id;
    Spinner spinner,spinner_mobile;
    int key_country_position;
    int key_mobile_position;
    int key_merchant_description_position;
    boolean screenChangedCountry=false;
    boolean screenChangedMobile=false;
    ArrayList<String> phoneNumbers;
    ArrayList<MemberDetails> memberDetailsList;
    boolean flag = false;
    boolean flag_mobile = false;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;


    public FragmentSocietyCompanyPointsPurchasesReport() {
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
        setRetainInstance(true);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
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
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        rootView = inflater.inflate(R.layout.fragment_society_company_points_purchases_report, container, false);
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

            screenChangedCountry=true;
            screenChangedMobile=true;
            sendLink = rootView.findViewById(R.id.tvSendHere);
            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            URL=ip +"spinner_get_member_phone_nos.php";
            phoneNumbers = new ArrayList<>();
            memberDetailsList = new ArrayList<MemberDetails>();
            spinner=rootView.findViewById(R.id.phoneSpinner);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            loadSpinnerData(URL);

            cancelLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 72);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyReportsMenuFrag");
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

                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 77);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MemberPointsPurchasesReportFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_MOBILE_PHONE_NO, mobile);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }


                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(screenChangedMobile) {
                        spinner.setSelection(key_mobile_position);
                        screenChangedMobile=false;
                        //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        MemberDetails memberDetails = (MemberDetails)spinner.getSelectedItem();
                        //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        mobile=memberDetails.getPhone();
                    }else{
                        key_mobile_position=position;
                        MemberDetails memberDetails = (MemberDetails)spinner.getSelectedItem();
                        //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        mobile=memberDetails.getPhone();
                        //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    }
                    if (!flag_mobile) {
                        flag_mobile = true;
                        //etUsername.requestFocus();
                    }else {
                        sendLink.requestFocus();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
        URL=ip +"spinner_get_member_phone_nos.php";
        phoneNumbers = new ArrayList<>();
        memberDetailsList = new ArrayList<MemberDetails>();
        //memberDetails = new ArrayList<>();
        spinner=rootView.findViewById(R.id.phoneSpinner);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        loadSpinnerData(URL);

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 72);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyReportsMenuFrag");
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

                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 77);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MemberPointsPurchasesReportFrag");
                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_MOBILE_PHONE_NO, mobile);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }


            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MemberDetails memberDetails = (MemberDetails)spinner.getSelectedItem();
                //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                mobile=memberDetails.getPhone();
                key_mobile_position=position;

                if (!flag_mobile) {
                    flag_mobile = true;
                    //etUsername.requestFocus();
                }else {
                    sendLink.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        stringRequestAdapter = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    phoneNumbers = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String phone_member_no = jsonObject1.getString("phone_number");
                        String client_name = jsonObject1.getString("client_name");
                        //Log.e(TAG," phila clent name is "+client_name);
                        //Log.e(TAG," phila length is "+client_name.length());
                        cnt = cnt + 1;
                        phoneNumbers.add(client_name);
                        MemberDetails memberDetails = new MemberDetails(phone_member_no,client_name);
                        memberDetailsList.add(memberDetails);
                        //spinner_region.notifyDataSetChanged();
                    }

                    pd.dismiss();
                    //spinner_region.setAdapter(new ArrayAdapter<String>(context(), android.R.layout.simple_spinner_dropdown_item, CountryRegions));
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


                        ArrayAdapter<MemberDetails> spinnerArrayAdapter =
                                new ArrayAdapter<MemberDetails>(context, R.layout.my_spinner, memberDetailsList);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }
                } catch (JSONException e) {
                    pd.dismiss();

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
                    txtview.setText(activity.getResources().getString(R.string.no_phone_numbers_registered));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                            if (fragmentActionListener != null) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 72);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyReportsMenuFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
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
                txtview.setText(activity.getResources().getString(R.string.unable_to_download));

                epicDialog.show();
                Dialog finalEpicDialog = epicDialog;
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalEpicDialog.dismiss();
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 72);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyReportsMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
                });

                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", merchantUserName);
                param.put("merchantCode", merchantCode);
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SocietyCompanyPointsPurchaesReportFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SocietyCompanyPointsPurchaesReportFrag");
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
