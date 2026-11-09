package com.moyet.livvuinsurance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class fragment_contact_us extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestCountry;
    StringRequest stringRequestTariff;
    StringRequest stringRequestDoctor;
    StringRequest stringRequestPatient;
    View rootView;
    private static final String TAG = "ContactUsFragment";
    FragmentActionListener fragmentActionListener;
    String merchant_name,merchant_code,username;
    String[] contactData;
    int[] contactImages;
    Button cancel;
    String sending_frag,ip;
    String user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,session_id;
    String firstName,lastName,userName,country_name,merchantName,merchantCode,merchantUserName,
            sms_contact_phone_no,url_sms;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    public fragment_contact_us() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_NAME);
                merchant_code = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                username = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchant_code = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                username = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }


    public void onDestroy() {
        super.onDestroy();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_NAME);
                merchant_code = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                username = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchant_code = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                username = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchant_name = bundle.getString(FragmentActionListener.KEY_NAME);
                merchant_code = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                username = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchant_code = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                username = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
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

        contactData = new String[]{activity.getResources().getString(R.string.address), sms_contact_phone_no,
                activity.getResources().getString(R.string.email),activity.getResources().getString(R.string.website),
                activity.getResources().getString(R.string.schedule)};
        contactImages = new int[]{R.drawable.address, R.drawable.mobile,R.drawable.email,R.drawable.website,R.drawable.schedule};

        final ListView mListView = rootView.findViewById(R.id.listView);

        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_client, null);
        cancel = footerView.findViewById(R.id.cancel);
        mListView.addFooterView(footerView, null, false);
        final CustomAdapter customAdapter = new CustomAdapter();
        mListView.setAdapter(customAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
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
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, username);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RemoveMembersFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position)
                {

                    case 1:

                        if (fragmentActionListener != null) {

                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, sms_contact_phone_no);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, username);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, sms_contact_phone_no);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, sms_contact_phone_no);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else{
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, sms_contact_phone_no);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        }

                        break;

                    case 2:
                        if (fragmentActionListener != null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 39);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, username);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "emailForm");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 39);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "emailForm");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){

                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 39);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "emailForm");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);

                            }else{
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 39);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "emailForm");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        }
                        break;

                    case 3:
                        String url="http:www.livvu.com";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;

                    case 4:
                        String ph="078053534";
                        Intent in = new Intent(Intent.ACTION_DIAL);
                        in.setData(Uri.parse("tel:"+ph));
                        try{
                            startActivity(in);
                        }catch (android.content.ActivityNotFoundException ex){
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
                            Toast.makeText(context,"Could not find an activity to make the call",
                                    Toast.LENGTH_LONG).show();
                        }
                        break;


                }
            }
        });

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

        stringRequest = new StringRequest(Request.Method.POST, url_sms,
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
                param.put("country", country);
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

    private class CustomAdapter extends BaseAdapter {
        private Context mContext;
        private int mResource;
        private int lastPosition = -1;
        @Override
        public int getCount() {
            return contactImages.length;
        }

        @Override
        public Object getItem(int position) {
            return contactImages[position];
        }

        @Override
        public long getItemId(int position) {
            return contactImages[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View viewHolder;

            viewHolder = getLayoutInflater().inflate(R.layout.adapter_contact_layout,null);
            TextView name = viewHolder.findViewById(R.id.textView1);
            ImageView image =viewHolder.findViewById(R.id.image);

            name.setText(contactData[position]);
            name.setTextColor(getResources().getColor(R.color.colorBlack));
            image.setImageResource(contactImages[position]);

            return viewHolder;
        }
    }
}
