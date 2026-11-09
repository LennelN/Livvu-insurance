package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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


public class MainFragmentOriginal extends Fragment {

    private static final String TAG = "MainFragment";
    boolean flag = false;
    View rootView;
    Context context;
    FragmentActionListener fragmentActionListener;
    EditText etUsername;
    EditText etPassword;
    TextView loginLink;
    TextView registerLink,forgotPassword;
    TextView cancelLink;
    String username,errorMessage;
    String password;
    String ip;
    String URL;
    ProgressDialog pd;
    private  int screenWidth,screenHeight;
    AlertDialog.Builder builder;


    public MainFragmentOriginal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initUI();
        return rootView;

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
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Log.i("OrintationChange","CountriesFragment onSaveInstanceState");
    }


    private void initUI() {
        context = getContext();
        etUsername = rootView.findViewById(R.id.etPhoneNumber);
        etUsername.addTextChangedListener(loginTextWatcher);
        etUsername.setTextColor(getResources().getColor(R.color.colorBlack));
        etUsername.requestFocus();
        etPassword = rootView.findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(loginTextWatcher);
        etPassword.setTextColor(getResources().getColor(R.color.colorBlack));
        registerLink = rootView.findViewById(R.id.tvRegisterHere);
        loginLink = rootView.findViewById(R.id.tvLoginHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        forgotPassword = rootView.findViewById(R.id.tvForgotPassword);
        ip=getResources().getString(R.string.ip_address);
        URL=ip +"login.php";


    //TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt(FragmentActionListener.ACTION_KEY, 135);
                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                fragmentActionListener.onActionPerformed(bundle);

            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 1);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "registrationType");
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this.dismiss()
                getActivity().finish();//finish();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {

                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {

                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){

                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){

                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme720);
                }else{

                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme18sp);
                }

                flag = false;
                if (username.length() < 10) {
                    flag = true;
                    final Dialog epicDialog = new Dialog(getContext());
                    epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                    txtview.setText(getResources().getString(R.string.phone_length));

                    epicDialog.show();
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            epicDialog.dismiss();
                        }
                    });

                } else {
                    if (password.length() < 4) {
                        flag = true;

                        final Dialog epicDialog = new Dialog(getContext());
                        epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                        TextView txtview = epicDialog.findViewById(R.id.message_text);
                        TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                        txtview.setText(getResources().getString(R.string.password_length));

                        epicDialog.show();
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                epicDialog.dismiss();
                            }
                        });

                    }
                }

                if (!flag) {

                    if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                        pd = new ProgressDialog(getContext(), R.style.MyDialogTheme240);
                    }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                            || screenWidth==720 || screenWidth==1184) {
                        pd = new ProgressDialog(getContext(), R.style.MyDialogTheme16sp);
                    }else if(screenWidth==600 || screenWidth==1024){
                        pd = new ProgressDialog(getContext(), R.style.MyDialogTheme600);
                    }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                        pd = new ProgressDialog(getContext(), R.style.MyDialogTheme720);
                    }else{
                        pd = new ProgressDialog(getContext(), R.style.MyDialogTheme18sp);
                    }

                    pd.setMessage(getResources().getString(R.string.processing));
                    pd.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
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
                                            int type = jsonResponse.getInt("type");
                                            if (type == 1) {


                                                if(message.contentEquals("Reset Password")) {
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else {
                                                    String name = jsonResponse.getString("name");
                                                    String surname = jsonResponse.getString("surname");
                                                    String country_name = jsonResponse.getString("country_name");
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");

                                                    if (fragmentActionListener != null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, name);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, surname);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            } else if(type == 2){

                                                if(message.contentEquals("Reset Password")) {
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else{
                                                    String merchant_name = jsonResponse.getString("merchant_name");
                                                    String merchant_code = jsonResponse.getString("merchant_code");
                                                    String type_of_reg = jsonResponse.getString("type_of_registration");
                                                    String merchant_country = jsonResponse.getString("merchant_country");
                                                    String session_id = jsonResponse.getString("session_id");

                                                    if (fragmentActionListener != null) {
                                                        if (type_of_reg.equals("Health")) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, merchant_country);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        } else {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, merchant_country);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }
                                                    }
                                                }
                                            }else if(type == 3){

                                                if(message.contentEquals("Successful Login")) {
                                                    String username = jsonResponse.getString("name");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    String user_role = jsonResponse.getString("user_role");
                                                    String country = jsonResponse.getString("country");
                                                    String user_login_id = jsonResponse.getString("user_login_id");
                                                    String session_id = jsonResponse.getString("session_id");

                                                    if(user_role.contentEquals("Accounts")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuMainAccountsMenu");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(user_role.contentEquals("Admin")){
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 43);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAdminMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else{
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 121);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }else{
                                                    if(message.contentEquals("Reset Password")) {
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String session_id = jsonResponse.getString("session_id");
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else {
                                                        String username = jsonResponse.getString("name");
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String user_role = jsonResponse.getString("user_role");
                                                        String country = jsonResponse.getString("country");
                                                        String user_login_id = jsonResponse.getString("user_login_id");
                                                        String session_id = jsonResponse.getString("session_id");

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 49);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CompleteUserRegFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            }else if(type == 4){

                                                if(message.contentEquals("Successful Login")) {
                                                    String username = jsonResponse.getString("name");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    String user_role = jsonResponse.getString("user_role").trim();
                                                    String country = jsonResponse.getString("country");
                                                    String user_login_id = jsonResponse.getString("user_login_id");
                                                    String merchant_phone_number=jsonResponse.getString("merchant_phone_number");
                                                    String merchant_code=jsonResponse.getString("merchant_code");
                                                    String merchant_name=jsonResponse.getString("merchant_name");
                                                    String session_id = jsonResponse.getString("session_id");
                                                    if(user_role.contentEquals("Accounts")) {

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(user_role.contentEquals("Admin")) {

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }else{
                                                    if(message.contentEquals("Reset Password")) {
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String session_id = jsonResponse.getString("session_id");
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else{
                                                        String username = jsonResponse.getString("name");
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String user_role = jsonResponse.getString("user_role").trim();
                                                        String country = jsonResponse.getString("country");
                                                        String user_login_id = jsonResponse.getString("user_login_id");
                                                        String merchant_phone_number=jsonResponse.getString("merchant_phone_number");
                                                        String merchant_code=jsonResponse.getString("merchant_code");
                                                        String merchant_name=jsonResponse.getString("merchant_name");
                                                        String session_id = jsonResponse.getString("session_id");

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 49);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CompleteUserRegFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            }
                                        } else {
                                            pd.dismiss();

                                            final Dialog epicDialog = new Dialog(getContext());
                                            epicDialog.setContentView(R.layout.dialog_my_custom_message);
                                            TextView txtview = epicDialog.findViewById(R.id.message_text);
                                            TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                            txtview.setText(message);

                                            epicDialog.show();
                                            action_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    epicDialog.dismiss();
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
                                    errorMessage=getResources().getString(R.string.unable_to_sign_in_please_check_your_internet_connection_and_try_again);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                    error.printStackTrace();
                                    //error.getMessage();

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("username",username);
                            param.put("password",password);

                            return param;
                        }
                    };
                    Vconnection.getnInstance(getActivity()).addRequestQue(stringRequest);
                }
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = etUsername.getText().toString().trim();
            String usernamePassword = etPassword.getText().toString().trim();
            int confirm_password_length = usernamePassword.length();

            if(s.length() == 10){
                etPassword.requestFocus();
            }
                    if(!usernameInput.isEmpty() && !usernamePassword.isEmpty() && confirm_password_length== 4)
                    {
                        loginLink.setEnabled(true);
                        loginLink.setTextColor(getResources().getColor(R.color.colorBlack));
                    }else{
                        loginLink.setEnabled(false);
                        loginLink.setTextColor(getResources().getColor(R.color.colorCell));
                    }
            //loginLink.setTextColor(000);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



}
