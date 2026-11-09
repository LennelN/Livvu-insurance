package com.moyet.livvuinsurance;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentResetPasswordStatement extends Fragment implements AdapterView.OnItemClickListener{
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    StringRequest stringRequestAdapterReset;
    View rootView;
    FragmentActionListener fragmentActionListener;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "ResetPasswdStatement";
    boolean flag = false;
    String firstName;
    String lastName;
    String userName,phoneNo,userRole,errorMessage,URL,user_login_id,country;
    String ip;
    EditText etMenuChoice;
    private ProgressDialog pd;
    ArrayList<ResetPasswordDetails> itemList;
    ArrayList<String> listViewItems;
    listviewPasswordResetDetailsAdapter adapter;
    ListView lv;
    Button cancel;
    String code,description,category,type,country_name,session_id,frag,main_frag,display_details;
    double price;
    int checkedItem;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;


    public FragmentResetPasswordStatement() {
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
            country_name = bundle.getString(FragmentActionListener.KEY_COUNTRY_NAME);
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
            country_name = bundle.getString(FragmentActionListener.KEY_COUNTRY_NAME);
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
        if(stringRequestAdapterReset != null){
            stringRequestAdapterReset.cancel();
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
        rootView = inflater.inflate(R.layout.fragment_password_reset_statement, container, false);
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
            country_name = bundle.getString(FragmentActionListener.KEY_COUNTRY_NAME);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

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

        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<ResetPasswordDetails>();
        URL = ip + "get_reset_passwords_phone_numbers.php";
        loadResetPasswordPhoneNumbersData(URL);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_close, null);
        cancel = footerView.findViewById(R.id.cancel);
        lv.addFooterView(footerView, null, false);
        adapter = new listviewPasswordResetDetailsAdapter(context, itemList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        cancel.setOnClickListener(new View.OnClickListener() {
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



    }

    private void loadResetPasswordPhoneNumbersData(String url) {

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

                                    String phone_number = jsonResponse.getString("phone_number");
                                    String request_date = jsonResponse.getString("request_date");
                                    String request_processed = jsonResponse.getString("request_processed");
                                    String request_completed = jsonResponse.getString("request_completed");
                                    String name  = jsonResponse.getString("name");
                                    String dob_licence_no_email = jsonResponse.getString("dob_licence_no_email");
                                    String idno_merchant_code = jsonResponse.getString("idno_merchant_code");
                                    String gender_branch_description = jsonResponse.getString("gender_branch_description");
                                    String date_joined = jsonResponse.getString("date_joined");
                                    String member_active = jsonResponse.getString("member_active");
                                    String country_name = jsonResponse.getString("country_name");
                                    String group_category_user_role = jsonResponse.getString("group_category_user_role");
                                    String category_returned = jsonResponse.getString("category");

                                    ResetPasswordDetails resetPasswordDetails = new ResetPasswordDetails(name,dob_licence_no_email,
                                            idno_merchant_code,gender_branch_description,date_joined,member_active,country_name,
                                            group_category_user_role,phone_number,request_date,
                                            request_completed,category_returned, request_processed);

                                    itemList.add(resetPasswordDetails);
                                    adapter.notifyDataSetChanged();

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
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ResetPasswordStatementFrag");
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
                        builder.setMessage(activity.getResources().getString(R.string.unable_to_download))
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
                param.put("username", user_login_id);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        final String[] choice = new String[]{(activity.getResources().getString(R.string.view_details)),
                (activity.getResources().getString(R.string.reset_selected_password))};
        builder.setTitle(activity.getResources().getString(R.string.please_select));

        ResetPasswordDetails item = itemList.get(position);
        final String name = item.getName();
        final String category = item.getCategory();
        final String dob_licence_no_email =item.getDob_licence_no_email();
        final String idno_merchant_code = item.getIdno_merchant_code();
        final String gender_branch_description = item.getGender_branch_description();
        final String date_joined = item.getDate_joined();
        final String member_active = item.getMember_active();
        final String country_name = item.getCountry_name();
        final String group_category_user_role = item.getGroup_category_user_role();
        final String phone_number = item.getPhone_number();

        if(category.contentEquals("LivvuUser"))
        {
            display_details = "Name: "+name+"\n" +
                    "Email: "+dob_licence_no_email+"\n"+
                    "ID Number: "+idno_merchant_code+"\n"+
                    "User Branch: "+gender_branch_description+"\n"+
                    "Date Joined "+date_joined+"\n"+
                    "Active: "+member_active+"\n"+
                    "Country: "+country_name+"\n"+
                    "User Role: "+group_category_user_role;
            URL = ip+"reset_user_profile.php";
        }else if(category.contentEquals("Merchant")){

            display_details = "Name: "+name+"\n" + "Licence No: "+dob_licence_no_email+"\n"+
                    "Merchant Code: "+idno_merchant_code+"\n"+
                    "Merchant Description: "+gender_branch_description+"\n"+
                    "Date Joined "+date_joined+"\n"+
                    "Active: "+member_active+"\n"+
                    "Country: "+country_name+"\n"+
                    "Registered As: "+group_category_user_role;
            URL = ip+"reset_main_merchant_account_profile.php";
        }else if(category.contentEquals("Client")){
            display_details = "Name: "+name+"\n" +"Date Of Birth: "+dob_licence_no_email+"\n"+
                    "ID Number: "+idno_merchant_code+"\n"+
                    "Gender: "+gender_branch_description+"\n"+
                    "Date Joined "+date_joined+"\n"+
                    "Active: "+member_active+"\n"+
                    "Country: "+country_name+"\n"+
                    "Group : "+group_category_user_role;
            URL = ip+"reset_client_profile.php";
        }else if(category.contentEquals("MerchantUser"))
        {
            display_details = "User Name: "+name+"\n" +
                    "Email: "+dob_licence_no_email+"\n"+
                    "ID Number: "+idno_merchant_code+"\n"+
                    "User Branch: "+gender_branch_description+"\n"+
                    "Date Joined "+date_joined+"\n"+
                    "Active: "+member_active+"\n"+
                    "Country: "+country_name+"\n"+
                    "User Role: "+group_category_user_role;
            URL = ip+"reset_merchant_user_profile.php";
        }


        builder.setSingleChoiceItems(choice, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                switch(which)
                {
                    case 0:
                        checkedItem = 0;
                        break;

                    case 1:
                        checkedItem = 1;
                        break;

                }

            }
        });

        builder.setPositiveButton(activity.getResources().getString(R.string.ok_dialog),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (checkedItem) {
                    case 0:
                        if(display_details.length() > 0) {
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
                            txtview.setText(display_details);

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });
                        }
                        break;

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


                        stringRequestAdapterReset = new StringRequest(Request.Method.POST, URL,
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
                                                String smsphone = jsonResponse.getString("smsphone");
                                                String resetpassword = jsonResponse.getString("resetpassword");

                                                if (checkPermission(Manifest.permission.SEND_SMS)) {

                                                    String resetMessage;

                                                    resetMessage = " Your Login Password Is " + resetpassword + " Thank You.";

                                                    try {
                                                        SmsManager smsManager = SmsManager.getDefault();
                                                        smsManager.sendTextMessage(smsphone, null, resetMessage, null, null);

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

                                                        Toast.makeText(context,getResources().getString(R.string.message_sent), Toast.LENGTH_LONG).show();

                                                    } catch (Exception ex) {

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
                                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ResetPasswordStatementFrag");
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
                                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ResetPasswordStatementFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuResponseFrag");
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
                                                        if (fragmentActionListener!=null) {
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
                                param.put("username", user_login_id);
                                param.put("user_deactivated_id", phone_number);
                                param.put("login_country", country_name);

                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapterReset); // vConnection i claas which used to connect volley


                        break;



                    default:
                        throw new IllegalStateException(activity.getResources().getString(R.string.unexpected_value)+ " " + checkedItem);
                }
            }
        });


        builder.setNegativeButton(activity.getResources().getString(R.string.cancel_dialog), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();



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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ResetPasswordStatementFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ResetPasswordStatementFrag");
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
