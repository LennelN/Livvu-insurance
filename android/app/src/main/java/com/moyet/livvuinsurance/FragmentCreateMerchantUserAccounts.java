package com.moyet.livvuinsurance;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentCreateMerchantUserAccounts extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    private View rootView;
    private static final String TAG = "CreateMercUsersFrag";
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    FragmentActionListener fragmentActionListener;
    String merchantName,merchantCode,merchantUserName,ip,user_role,URL,address,
            name, password, branch, phone_no,idno,confirmPassword,email,errorMessage,country,
            userName,user_login_id,nameOfUser,frag,user_login_key,session_id;
    private EditText etFullName,etEmailAddress,etIDNo,etBranch,etPhoneNumber,etPassword,
            etAddress,etConfirmPassword,etCountry;
    private TextView registerLink;
    private TextView cancelLink;
    AlertDialog.Builder builder;
    RadioGroup radioGroup;
    ProgressDialog pd;
    private  int screenWidth,screenHeight;

    public FragmentCreateMerchantUserAccounts() {
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
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(frag.contentEquals("MerchantMainMenuFrag")){
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
            }else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
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

        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(frag.contentEquals("MerchantMainMenuFrag")){
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
            }else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }

        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_merchant_user_accounts, container, false);
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

        registerLink = rootView.findViewById(R.id.tvRegisterHere);
        radioGroup = rootView.findViewById(R.id.radio);
        etFullName = rootView.findViewById(R.id.etName);
        etFullName.requestFocus();
        etFullName.addTextChangedListener(registerTextWatcher);
        etPhoneNumber = rootView.findViewById(R.id.etPhoneNumber);
        etPhoneNumber.addTextChangedListener(registerTextWatcher);
        etAddress = rootView.findViewById(R.id.etAddress);
        etAddress.addTextChangedListener(registerTextWatcher);
        etEmailAddress = rootView.findViewById(R.id.etEmailAddress);
        etEmailAddress.addTextChangedListener(registerTextWatcher);
        etIDNo= rootView.findViewById(R.id.etIDNo);
        etIDNo.addTextChangedListener(registerTextWatcher);
        etPassword = rootView.findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(registerTextWatcher);
        etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
        etConfirmPassword.addTextChangedListener(registerTextWatcher);
        etBranch= rootView.findViewById(R.id.etBranch);
        etBranch.addTextChangedListener(registerTextWatcher);
        etCountry= rootView.findViewById(R.id.etCountry);
        etCountry.addTextChangedListener(registerTextWatcher);

        URL=ip +"register_merchant_user_profile.php";

        final TextView cancelLink = rootView.findViewById(R.id.tvCancelHere);

        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rootView);
            }
        });

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }
            }
        });

        if(checkPermission(Manifest.permission.SEND_SMS)){
            //sendSmsButton.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioGroup.getCheckedRadioButtonId() == -1) {
                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
                    user_role = selectedRadioButton.getText().toString();
                }

                name = etFullName.getText().toString();
                address = etAddress.getText().toString();
                password = etPassword.getText().toString();
                branch = etBranch.getText().toString();
                phone_no = etPhoneNumber.getText().toString();
                idno = etIDNo.getText().toString();
                confirmPassword=etConfirmPassword.getText().toString();
                email = etEmailAddress.getText().toString();
                country = etCountry.getText().toString();

                if (!(password.equals(confirmPassword))) {
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
                    txtview.setText(activity.getResources().getString(R.string.passwords_not_matching));

                    epicDialog.show();

                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                            etPassword.requestFocus();
                        }
                    });

                }else if(!isValidEmail(email)) {
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
                    txtview.setText(activity.getResources().getString(R.string.invalid_email_address));

                    epicDialog.show();


                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                            etEmailAddress.requestFocus();
                        }
                    });

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

                    stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                        boolean success = jsonResponse.getBoolean("success");
                                        String message = jsonResponse.getString("message");

                                        if (success) {

                                            pd.dismiss();
                                            String confirmcode = jsonResponse.getString("confirmcode");
                                            String user_login_id_retrieved = jsonResponse.getString("user_login_id");
                                            //Send an sms to the number being registered
                                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                                String verificationMessage;

                                                verificationMessage=" Your User LogIn ID Is " + user_login_id_retrieved +
                                                        " Please Enter The Verification Code "+ confirmcode +
                                                        " To Complete Your Registration. Thank You. ";
                                                try{
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(phone_no, null, verificationMessage, null, null);

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

                                                    Toast.makeText(context,activity.getResources().getString(R.string.message_sent), Toast.LENGTH_LONG).show();

                                                }catch (Exception ex){

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
                                                    Toast.makeText(context,ex.toString(), Toast.LENGTH_LONG).show();
                                                    ex.printStackTrace();
                                                }
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

                                                Toast.makeText(context,activity.getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                                            }
                                            if (fragmentActionListener != null) {
                                                if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
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
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else{
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            }
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


                                            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                        } else {
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
                                    error.printStackTrace();
                                    //error.getMessage();

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
                                    txtview.setText(activity.getResources().getString(R.string.unable_to_process));

                                    epicDialog.show();


                                    Dialog finalEpicDialog = epicDialog;
                                    action_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finalEpicDialog.dismiss();
                                            if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else{
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }
                                    });

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name",name);
                            params.put("email",email);
                            params.put("user_role",user_role);
                            params.put("phone_no",phone_no);
                            params.put("idno",idno);
                            params.put("password",password);
                            params.put("branch", branch);
                            params.put("address", address);
                            params.put("country", country);
                            params.put("merchant_phone_number", merchantUserName);
                            params.put("merchant_code", merchantCode);
                            if(frag.contentEquals("MerchantMainMenuFrag")) {
                                params.put("user_created_by", user_login_id);
                            }else{
                                params.put("user_created_by", merchantUserName);
                            }
                            return params;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);

                }

            }
        });

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

            String usernameInput = etPhoneNumber.getText().toString().trim();
            String usernamePassword = etPassword.getText().toString().trim();
            String userAddressInput = etAddress.getText().toString().trim();
            int password_length = usernamePassword.length();
            String nameInput = etFullName.getText().toString().trim();
            String userEmailAddressInput = etEmailAddress.getText().toString().trim();
            String userIDInput = etIDNo.getText().toString().trim();
            String userConfirmPasswordInput = etConfirmPassword.getText().toString().trim();
            String userBranchInput = etBranch.getText().toString().trim();
            String userCountryInput = etCountry.getText().toString().trim();
            int confirm_password_length = userConfirmPasswordInput.length();

            if(confirm_password_length == 4 && !userIDInput.isEmpty() && !userAddressInput.isEmpty()
                    && !usernameInput.isEmpty() && password_length == 4 && !userBranchInput.isEmpty()
                    && !userEmailAddressInput.isEmpty() && !nameInput.isEmpty() && !userCountryInput.isEmpty())
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

    private boolean checkPermission(String permission) {
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
        int checkPermission = ContextCompat.checkSelfPermission(context,permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case SEND_SMS_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //sendSmsButton.setEnabled(true);
                }
                break;
        }
    }

    private void onRadioButtonClicked(View view) {
        // Check to see if a button has been clicked.
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        int id = view.getId();
        if (id == R.id.admin) {
            if (checked)
                user_role = "Admin";
            etPhoneNumber.requestFocus();
        } else if (id == R.id.accounts) {
            if (checked)
                user_role = "Accounts";
            etPhoneNumber.requestFocus();
        }
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                if(frag.contentEquals("MerchantMainMenuFrag")){
                    param.put("username", user_login_id);
                }else{
                    param.put("username", merchantUserName);
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
