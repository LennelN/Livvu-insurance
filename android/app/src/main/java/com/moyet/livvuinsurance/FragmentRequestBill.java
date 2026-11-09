package com.moyet.livvuinsurance;


import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentRequestBill extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    StringRequest stringRequestSpinnerHealth;
    StringRequest stringRequestRegion;
    private FragmentRequestBill mFragment;
    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg;
    TextView sendLink;
    TextView cancelLink;
    AlertDialog.Builder builder;
    private static final String TAG = "RequestBillFrag";
    boolean flag = false;
    View rootView;
    EditText etMenuChoice;
    String firstName;
    String lastName;
    String userName;
    String phone_no,phone_number;
    String frag,ip;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    String user_login_key,nameOfUser,user_role,user_login_id,country,session_id;
    Spinner country_Name,region_desc,hospital_desc;
    Spinner spinner,spinner_health_desc,spinner_region,spinner_sector;
    String URL,URL_HEALTH,URL_REGION,URL_SECTOR;
    String name, password, licence_number, username, confirmpassword, description, country_name, address,
            region,media,type,sector,code_returned_message;
    ArrayList<String> CountryName;
    ArrayList<String> HealthDesc;
    ArrayList<String> CountryRegions;
    ArrayList<String> SectorDesc;
    List<BillRequestDetails> billRequestList;
    boolean flag_desc = false;
    boolean flag_sector = false;
    boolean flag_region = false;
    String merchant_name_data_selected;
    String merchant_code_data_selected;
    String merchant_phone_number_data_selected;
    String merchant_region_data_selected;
    int key_country_position;
    boolean screenChanged=false;
    String url_spinner,key_country_name,main_frag;
    String server_url;
    private ProgressDialog pd;
    int key_region_position;
    int key_merchant_description_position;
    int key_sector_position;
    boolean screenChangedCountry=false;
    boolean screenChangedRegion=false;
    boolean screenChangedDescription=false;
    boolean screenChangedSector=false;
    private  int screenWidth,screenHeight;
    public FragmentRequestBill() {
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
            assert getFragmentManager() != null;
            mFragment = (FragmentRequestBill)getFragmentManager().findFragmentByTag("RequestBillFrag");
        }
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
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if (frag.contentEquals("IndividualMainMenuFrag")) {
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            } else if(frag.contentEquals("MerchantMainMenuFrag")) {

                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);


            } else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
        }

        //welcomeMsg.setText(activity.getResources().getString(R.string.welcome_space_2) + firstName + " " + lastName);
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
        if(stringRequestSpinnerHealth != null){
            stringRequestSpinnerHealth.cancel();
        }
        if(stringRequestRegion != null){
            stringRequestRegion.cancel();
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
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if (frag.contentEquals("IndividualMainMenuFrag")) {
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            } else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        rootView = inflater.inflate(R.layout.fragment_bill_request, container, false);
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
            screenChangedRegion=true;
            screenChangedDescription=true;
            screenChangedSector=true;

            URL=ip +"spinner_country.php";
            URL_HEALTH=ip +"spinner_health_institutions.php";
            URL_REGION=ip +"spinner_regions.php";

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

            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            sendLink = rootView.findViewById(R.id.tvRegisterHere);
            CountryName = new ArrayList<>();
            spinner = rootView.findViewById(R.id.country_Name);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            loadSpinnerData(URL);
            HealthDesc = new ArrayList<>();
            billRequestList = new ArrayList<>();
            spinner_health_desc = rootView.findViewById(R.id.hospital_desc);
            spinner_health_desc.setFocusable(true);
            spinner_health_desc.setFocusableInTouchMode(true);
            //loadSpinnerHealthData(URL_HEALTH);
            CountryRegions = new ArrayList<>();
            spinner_region = rootView.findViewById(R.id.region_desc);
            spinner_region.setFocusable(true);
            spinner_region.setFocusableInTouchMode(true);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if(!screenChangedCountry) {

                        loadSpinnerRegionData(URL_REGION);
                        country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    }
                    if(key_country_position==0){
                        loadSpinnerRegionData(URL_REGION);
                    }

                    if(screenChangedCountry){
                        spinner.setSelection(key_country_position);
                        screenChangedCountry=false;
                        country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    }else{
                        key_country_position=i;

                    }

                    if (!flag) {
                        flag = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });


            spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    if(screenChangedRegion) {
                        spinner_region.setSelection(key_region_position);
                        screenChangedRegion=false;
                        loadSpinnerHealthData(URL_HEALTH);
                        region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                    }else{
                        loadSpinnerHealthData(URL_HEALTH);
                        key_region_position=position;
                        region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                    }
                    if (!flag_region) {
                        flag_region = true;
                        //etUsername.requestFocus();
                    }else {
                        sendLink.requestFocus();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            spinner_health_desc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(screenChangedDescription) {
                        spinner_health_desc.setSelection(key_merchant_description_position);
                        BillRequestDetails billRequestDetails = (BillRequestDetails) adapterView.getSelectedItem();

                        merchant_name_data_selected=billRequestDetails.getMerchant_name_data();
                        merchant_code_data_selected=billRequestDetails.getMerchant_code_data();
                        merchant_phone_number_data_selected=billRequestDetails.getMerchant_phone_number_data();
                        merchant_region_data_selected=billRequestDetails.getMerchant_region_data();

                        screenChangedDescription=false;
                    }else{
                        key_merchant_description_position=i;
                        spinner_health_desc.setSelection(key_merchant_description_position);
                        BillRequestDetails billRequestDetails = (BillRequestDetails) adapterView.getSelectedItem();
                        merchant_name_data_selected=billRequestDetails.getMerchant_name_data();
                        merchant_code_data_selected=billRequestDetails.getMerchant_code_data();
                        merchant_phone_number_data_selected=billRequestDetails.getMerchant_phone_number_data();
                        merchant_region_data_selected=billRequestDetails.getMerchant_region_data();

                    }
                    //description = spinner_health_desc.getItemAtPosition(spinner_health_desc.getSelectedItemPosition()).toString();
                    if (!flag_desc) {
                        flag_desc = true;
                        //etUsername.requestFocus();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
                    server_url=ip +"register_billing_request.php";

                    stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    //JSONArray jsonArray = null;
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                        boolean success = jsonResponse.getBoolean("success");
                                        String message_retrieved = jsonResponse.getString("message");
                                        if (success) {
                                            pd.dismiss();
                                            if (fragmentActionListener!=null) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE,message_retrieved);
                                                bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE,message_retrieved);
                                                bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                            param.put("country",country);
                            param.put("country_name",country_name);
                            param.put("region",merchant_region_data_selected);
                            param.put("nameOfHealth",merchant_name_data_selected);
                            param.put("merchantCode",merchant_code_data_selected);
                            param.put("merchantPhoneNo",merchant_phone_number_data_selected);
                            return param;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley


                }
            });

            cancelLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {
                        if (frag.contentEquals("IndividualMainMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
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
        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        sendLink = rootView.findViewById(R.id.tvRegisterHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        billRequestList = new ArrayList<>();

        URL=ip +"spinner_country.php";
        URL_HEALTH=ip +"spinner_health_institutions.php";
        URL_REGION=ip +"spinner_regions.php";

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

        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        loadSpinnerData(URL);

        HealthDesc = new ArrayList<>();
        spinner_health_desc = rootView.findViewById(R.id.hospital_desc);
        spinner_health_desc.setFocusable(true);
        spinner_health_desc.setFocusableInTouchMode(true);

        //loadSpinnerHealthData(URL_HEALTH);

        CountryRegions = new ArrayList<>();
        spinner_region = rootView.findViewById(R.id.region_desc);
        spinner_region.setFocusable(true);
        spinner_region.setFocusableInTouchMode(true);


        spinner_health_desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(cancelLink.getWindowToken(),0);
            }
        });


        spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(cancelLink.getWindowToken(),0);
                //inputMethodManager.showSoftInput(getView(),0);

            }
        });

        spinner_region.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(cancelLink.getWindowToken(),0);
            }
        });



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                loadSpinnerRegionData(URL_REGION);
                key_country_position=i;

                if (!flag) {
                    flag = true;
                }else {

                    spinner_region.requestFocus();
                    InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputMethodManager.hideSoftInputFromWindow(cancelLink.getWindowToken(),0);
                    inputMethodManager.showSoftInput(getView(),0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        spinner_health_desc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // description = spinner_health_desc.getItemAtPosition(spinner_health_desc.getSelectedItemPosition()).toString();
                key_merchant_description_position=i;

                BillRequestDetails billRequestDetails = (BillRequestDetails) adapterView.getSelectedItem();
                spinner_health_desc.setSelection(key_merchant_description_position);
                merchant_name_data_selected=billRequestDetails.getMerchant_name_data();
                merchant_code_data_selected=billRequestDetails.getMerchant_code_data();
                merchant_phone_number_data_selected=billRequestDetails.getMerchant_phone_number_data();
                merchant_region_data_selected=billRequestDetails.getMerchant_region_data();


                if (!flag_desc) {
                    flag_desc = true;
                    //etUsername.requestFocus();
                }else {
                    cancelLink.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                key_region_position=position;

                loadSpinnerHealthData(URL_HEALTH);
                if (!flag_region) {
                    flag_region = true;
                }else {
                    sendLink.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if (frag.contentEquals("IndividualMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
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

                pd.setMessage(activity.getResources().getString(R.string.processing_please_wait_dialog));
                pd.show();
                server_url=ip +"register_billing_request.php";

                stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //JSONArray jsonArray = null;
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                    boolean success = jsonResponse.getBoolean("success");
                                    String message_retrieved = jsonResponse.getString("message");
                                    if (success) {
                                        pd.dismiss();
                                        if (fragmentActionListener!=null) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY,13);
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE,message_retrieved);
                                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE,message_retrieved);
                                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                        param.put("country",country);
                        param.put("country_name",country_name);
                        param.put("region",merchant_region_data_selected);
                        param.put("nameOfHealth",merchant_name_data_selected);
                        param.put("merchantCode",merchant_code_data_selected);
                        param.put("merchantPhoneNo",merchant_phone_number_data_selected);
                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley


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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.my_spinner, CountryName);
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
                                if (fragmentActionListener != null) {
                                    if (frag.contentEquals("IndividualMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinner);


    }
    private void loadSpinnerHealthData(String url) {

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

        stringRequestSpinnerHealth = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    boolean success = jsonResponse.getBoolean("success");
                    code_returned_message = jsonResponse.getString("message");
                    billRequestList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String merchant_name_data = jsonObject1.getString("merchant_name_data");
                        String merchant_code_data = jsonObject1.getString("merchant_code_data");
                        String merchant_phone_number_data = jsonObject1.getString("merchant_phone_number_data");
                        String merchant_region_data = jsonObject1.getString("merchant_region_data");
                        cnt = cnt + 1;
                        BillRequestDetails billRequestDetails =
                                new BillRequestDetails(merchant_name_data,
                                        merchant_code_data,merchant_phone_number_data,
                                        merchant_region_data);

                        billRequestList.add(billRequestDetails);
                        //spinner_region.notifyDataSetChanged();
                    }
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
                        ArrayAdapter<BillRequestDetails> spinnerArrayAdapter = new ArrayAdapter<>(context,
                                R.layout.my_spinner, billRequestList);
                        //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context(),R.layout.my_spinner,billRequestList);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner_health_desc.setAdapter(spinnerArrayAdapter);
                    }
                } catch (JSONException e) {
                    description=null;
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
                    ArrayAdapter<BillRequestDetails> spinnerArrayAdapter = new ArrayAdapter<>(context,
                            R.layout.my_spinner, billRequestList);
                    //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context(),R.layout.my_spinner,billRequestList);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                    spinner_health_desc.setAdapter(spinnerArrayAdapter);

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
                    txtview.setText(code_returned_message);

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("country_name", country_name);
                param.put("region_name", region);
                return param;
            }
        };

        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinnerHealth);

    }

    private void loadSpinnerRegionData(String url) {

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

        stringRequestRegion = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    boolean success = jsonResponse.getBoolean("success");
                    code_returned_message = jsonResponse.getString("message");
                    CountryRegions = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name_of_region = jsonObject1.getString("name_of_region");
                        if(i==0){region=name_of_region;}
                        cnt = cnt + 1;
                        CountryRegions.add(name_of_region);
                        //spinner_region.notifyDataSetChanged();
                    }
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

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.my_spinner, CountryRegions);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner_region.setAdapter(spinnerArrayAdapter);
                    }
                } catch (JSONException e) {
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

                    region=null;
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,R.layout.my_spinner,CountryRegions);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                    spinner_region.setAdapter(spinnerArrayAdapter);

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
                    txtview.setText(code_returned_message);

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("country_name", country_name);
                return param;
            }
        };

        Vconnection.getnInstance(activity).addRequestQue(stringRequestRegion);


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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RemoveMembersFrag");
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
