package com.moyet.livvuinsurance;


import android.app.Dialog;
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
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class PrivateCompanyRegistrationFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    private static final String TAG = "PrivateCompanyFragment";
    private PrivateCompanyRegistrationFragment mFragment;
    AlertDialog.Builder builder;
    String name, password, licence_number, username, confirmpassword, description, country_name, address, region,media,type;
    EditText etLicenceNo, etDescription, etPassword, etName, etUsername, etAddress,  etConfirmPassword, etRegion;
    Spinner spinner;
    Spinner spinner_region;

    String URL,URL_REGION;
    ArrayList<String> CountryName;
    ArrayList<String> CountryRegions;
    boolean flag = false;
    private TextView registerLink;
    View rootView;
    FragmentActionListener fragmentActionListener;
    TextView cancelLink;
    ProgressDialog pd;
    String ip,code_returned_message;
    boolean flag_region = false;
    int key_country_position;
    int key_region_position;
    boolean screenChangedCountry=false;
    boolean screenChangedRegion=false;
    boolean screenChangedDescription=false;
    private  int screenWidth,screenHeight;

    public PrivateCompanyRegistrationFragment() {
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
        setRetainInstance(true);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (PrivateCompanyRegistrationFragment) getFragmentManager().findFragmentByTag("PrivateCompanyFragReg");
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

        if(stringRequest != null){
            stringRequest.cancel();
        }
        if(stringRequestAdapter != null){
            stringRequestAdapter.cancel();
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
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
        }


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_private_company_registration, container, false);
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

            screenChangedCountry=true;
            screenChangedRegion=true;
            screenChangedDescription=true;
            registerLink = rootView.findViewById(R.id.tvRegisterHere);
            etName = rootView.findViewById(R.id.etName);
            etName.requestFocus();
            etName.addTextChangedListener(registerTextWatcher);
            etUsername = rootView.findViewById(R.id.etPhoneNumber);
            etUsername.addTextChangedListener(registerTextWatcher);
            etDescription = rootView.findViewById(R.id.etDescription);
            etDescription.addTextChangedListener(registerTextWatcher);
            etAddress = rootView.findViewById(R.id.etAddress);
            etAddress.addTextChangedListener(registerTextWatcher);
            etLicenceNo = rootView.findViewById(R.id.etLicenceNo);
            etDescription.addTextChangedListener(registerTextWatcher);
            etPassword = rootView.findViewById(R.id.etPassword);
            etPassword.addTextChangedListener(registerTextWatcher);
            etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
            etConfirmPassword.addTextChangedListener(registerTextWatcher);
            //etRegion = rootView.findViewById(R.id.etRegion);
            //etRegion.addTextChangedListener(registerTextWatcher);


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
            URL=ip +"spinner_country.php";
            URL_REGION=ip +"spinner_regions.php";
            CountryName = new ArrayList<>();
            spinner = rootView.findViewById(R.id.country_Name);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            loadSpinnerData(URL);

            CountryRegions = new ArrayList<>();
            spinner_region = rootView.findViewById(R.id.region_desc);
            spinner_region.setFocusable(true);
            spinner_region.setFocusableInTouchMode(true);

            spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
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
                    InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                    //inputMethodManager.showSoftInput(getView(),0);

                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(!screenChangedCountry) {
                        region="";
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
                    }/*else {
                        spinner_region.requestFocus();
                        InputMethodManager inputMethodManager=(InputMethodManager)context().getSystemService(Context.INPUT_METHOD_SERVICE);
                        //inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                        inputMethodManager.showSoftInput(getView(),0);
                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });

            spinner_region.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
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
                    InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                    //inputMethodManager.showSoftInput(getView(),0);

                }
            });
            spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(screenChangedRegion) {
                        spinner_region.setSelection(key_region_position);
                        screenChangedRegion=false;
                        region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                    }else{
                        key_region_position=position;
                        region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                    }


                    region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                    if (!flag_region) {
                        flag_region = true;
                        //etUsername.requestFocus();
                    }else {
                        registerLink.requestFocus();
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
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "CompanyRegFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            });

            registerLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = etName.getText().toString();
                    licence_number = etLicenceNo.getText().toString();
                    description = etDescription.getText().toString();
                    address = etAddress.getText().toString();
                    username = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    confirmpassword = etConfirmPassword.getText().toString();
                    media="mobile";
                    type="Private";


                    if (!(password.equals(confirmpassword))) {

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
                        txtview.setText(activity.getResources().getString(R.string.passwords_not_matching_reenter));
                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                        etPassword.requestFocus();
                    }else if (region.isEmpty()){
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
                        txtview.setText(activity.getResources().getString(R.string.region_cannot_be_empty));
                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                    } else {

                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 26);
                            bundle.putString(FragmentActionListener.KEY_NAME, name);
                            bundle.putString(FragmentActionListener.KEY_LICENCE_NO, licence_number);
                            bundle.putString(FragmentActionListener.KEY_ADDRESS, address);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                            bundle.putString(FragmentActionListener.KEY_PASSWORD, password);
                            bundle.putString(FragmentActionListener.KEY_CONFIRMPASSWORD, confirmpassword);
                            bundle.putString(FragmentActionListener.KEY_MEDIA, media);
                            bundle.putString(FragmentActionListener.KEY_TYPE, type);
                            bundle.putString(FragmentActionListener.KEY_DESCRIPTION, description);
                            bundle.putString(FragmentActionListener.KEY_REGION, region);
                            bundle.putString(FragmentActionListener.KEY_COUNTRYNAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TermsConditionsCompanyFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "CompanyRegFrag");
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

        registerLink = rootView.findViewById(R.id.tvRegisterHere);
        etName = rootView.findViewById(R.id.etName);
        etName.requestFocus();
        etName.addTextChangedListener(registerTextWatcher);
        etUsername = rootView.findViewById(R.id.etPhoneNumber);
        etUsername.addTextChangedListener(registerTextWatcher);
        etDescription = rootView.findViewById(R.id.etDescription);
        etDescription.addTextChangedListener(registerTextWatcher);
        etAddress = rootView.findViewById(R.id.etAddress);
        etAddress.addTextChangedListener(registerTextWatcher);
        etLicenceNo = rootView.findViewById(R.id.etLicenceNo);
        etDescription.addTextChangedListener(registerTextWatcher);
        etPassword = rootView.findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(registerTextWatcher);
        etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
        etConfirmPassword.addTextChangedListener(registerTextWatcher);

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
        URL=ip +"spinner_country.php";
        URL_REGION=ip +"spinner_regions.php";
        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        loadSpinnerData(URL);

        CountryRegions = new ArrayList<>();
        spinner_region = rootView.findViewById(R.id.region_desc);
        spinner_region.setFocusable(true);
        spinner_region.setFocusableInTouchMode(true);

        spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
                InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                //inputMethodManager.showSoftInput(getView(),0);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                region="";
                loadSpinnerRegionData(URL_REGION);
                key_country_position=i;

                if (!flag) {
                    flag = true;
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
                    spinner_region.requestFocus();
                    InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                    inputMethodManager.showSoftInput(getView(),0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        spinner_region.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
                InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etAddress.getWindowToken(),0);
                //inputMethodManager.showSoftInput(getView(),0);

            }
        });
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region=spinner_region.getItemAtPosition(spinner_region.getSelectedItemPosition()).toString();
                key_region_position=position;
                if (!flag_region) {
                    flag_region = true;
                    //etUsername.requestFocus();
                }else {
                    registerLink.requestFocus();
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
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "CompanyRegFrag");
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                licence_number = etLicenceNo.getText().toString();
                description = etDescription.getText().toString();
                address = etAddress.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirmpassword = etConfirmPassword.getText().toString();
                media="mobile";
                type="Private";


                if (!(password.equals(confirmpassword))) {

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
                    txtview.setText(activity.getResources().getString(R.string.passwords_not_matching_reenter));
                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    etPassword.requestFocus();
                }else if (region.isEmpty()){
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
                    txtview.setText(activity.getResources().getString(R.string.region_cannot_be_empty));
                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                }else {

                    if (fragmentActionListener!=null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 26);
                        bundle.putString(FragmentActionListener.KEY_NAME, name);
                        bundle.putString(FragmentActionListener.KEY_LICENCE_NO, licence_number);
                        bundle.putString(FragmentActionListener.KEY_ADDRESS, address);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                        bundle.putString(FragmentActionListener.KEY_PASSWORD, password);
                        bundle.putString(FragmentActionListener.KEY_CONFIRMPASSWORD, confirmpassword);
                        bundle.putString(FragmentActionListener.KEY_MEDIA, media);
                        bundle.putString(FragmentActionListener.KEY_TYPE, type);
                        bundle.putString(FragmentActionListener.KEY_DESCRIPTION, description);
                        bundle.putString(FragmentActionListener.KEY_REGION, region);
                        bundle.putString(FragmentActionListener.KEY_COUNTRYNAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TermsConditionsCompanyFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "CompanyRegFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }


                }
            }
        });

    }
    public void displayAlert(final String code) {

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

        builder.setPositiveButton(activity.getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (code.equals("input_error")) {

                    etPassword.setText("");
                    etConfirmPassword.setText("");
                } else {

                    if (code.equals(getResources().getString(R.string.true_value))) {

                        activity.finish();
                    } else {

                        if (code.equals(getResources().getString(R.string.false_value))) {
                            etName.setText("");
                            etAddress.setText("");
                            etDescription.setText("");
                            etUsername.setText("");
                            etPassword.setText("");
                            etConfirmPassword.setText("");
                        }
                    }
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

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

        stringRequestAdapter = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                error.printStackTrace();
            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
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
            String usernameInput = etUsername.getText().toString().trim();
            String usernamePassword = etPassword.getText().toString().trim();
            int password_length = usernamePassword.length();
            String nameInput = etName.getText().toString().trim();
            String userDescriptionInput = etDescription.getText().toString().trim();
            String userAddressInput = etAddress.getText().toString().trim();
            String userLicenceNo = etLicenceNo.getText().toString().trim();
            String userConfirmPasswordInput = etConfirmPassword.getText().toString().trim();
            int confirm_password_length = userConfirmPasswordInput.length();


                    if( password_length== 4 && confirm_password_length== 4 && !userLicenceNo.isEmpty() &&
                            !usernameInput.isEmpty() &&  !nameInput.isEmpty() && !userDescriptionInput.isEmpty()
                            && !userAddressInput.isEmpty())
                    {
                        registerLink.setEnabled(true);
                        registerLink.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                    }else{
                        registerLink.setEnabled(false);
                        registerLink.setTextColor(activity.getResources().getColor(R.color.colorCell));
                    }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
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
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CountryRegions = new ArrayList<>();
                try {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    boolean success = jsonResponse.getBoolean("success");
                    code_returned_message = jsonResponse.getString("message");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name_of_region = jsonObject1.getString("name_of_region");
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

        Vconnection.getnInstance(activity).addRequestQue(stringRequest);

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
