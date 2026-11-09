package com.moyet.livvuinsurance;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
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

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class fragment_send_sms_cash_out_reminder extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    View rootView;
    private static final String TAG = "SendSMSCashOutFrag";
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    EditText messageEditText,phoneNumberEditText;
    Button sendSmsButton,cancelButton;
    String no_of_points,description,bill_number,bill_date,merchant_name;
    String firstName,client_name,merchantName,merchantCode,merchantUserName;
    String lastName;
    String userName;
    String phone_no;
    String message,subject,sending_frag;
    TextView welcomeMsg;
    String ip;
    int characterCount;
    int pageCount;
    int totalChars;
    boolean pagefull = true;
    AppCompatTextView textView1,textView3,textView4;
    int firstPageCount = 157;
    int secondPageCount = 145;
    int constantPageCount;
    String user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,frag,session_id,url,
            sms_contact_phone_no;
    FragmentActionListener fragmentActionListener;
    private ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public fragment_send_sms_cash_out_reminder() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(sending_frag.contentEquals("companyMainMenu")){
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);

            }else if(sending_frag.contentEquals("MyCustomDialog")) {
                frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                no_of_points = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                if(frag.contentEquals("companyMainMenu")) {
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
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
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
                }
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
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
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if(sending_frag.contentEquals("companyMainMenu")){
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);

            }else if(sending_frag.contentEquals("MyCustomDialog")) {
                frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                no_of_points = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                if(frag.contentEquals("companyMainMenu")) {
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
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
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
                }
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
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
            }

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
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            if(sending_frag.contentEquals("companyMainMenu")){
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MyCustomDialog")) {
                frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                bill_date = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                no_of_points = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                if(frag.contentEquals("companyMainMenu")) {
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
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
                    merchant_name = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
                }
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
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
            }

        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_send_sms, container, false);
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

        messageEditText = rootView.findViewById(R.id.msgTxt);
        textView1 = rootView.findViewById(R.id.textView1);
        textView3 = rootView.findViewById(R.id.textView3);
        textView4 = rootView.findViewById(R.id.textView4);

        phoneNumberEditText = rootView.findViewById(R.id.mblTxt);
        phoneNumberEditText.setText(sms_contact_phone_no);
        sendSmsButton = rootView.findViewById(R.id.btnSend);
        cancelButton = rootView.findViewById(R.id.btnCancel);
        sendSmsButton.setEnabled(false);
        //phoneNumberEditText.requestFocus();
        messageEditText.requestFocus();
        if(sending_frag.contentEquals("MyCustomDialog")) {

            subject = activity.getResources().getString(R.string.outstanding_cash_out_transaction_no)
                    + " " + bill_number + " " +  activity.getResources().getString(R.string.balance) + " " + no_of_points + " " +
                    activity.getResources().getString(R.string.points) + ":";

            messageEditText.setText(subject);
            int lengthOfSubject = subject.length();
            if (lengthOfSubject > 0) {

                if (lengthOfSubject > firstPageCount) {
                    int countLessFirstPage = lengthOfSubject - firstPageCount;
                    int noOfPages = (countLessFirstPage / secondPageCount);
                    int noOfCharacters = countLessFirstPage - (noOfPages * secondPageCount);

                    if (noOfCharacters == 0) {
                        constantPageCount = 0;
                        pageCount = noOfPages + 1;
                        totalChars = ((pageCount - 1) * secondPageCount) + firstPageCount;
                    } else {

                        constantPageCount = secondPageCount - noOfCharacters;
                        pagefull = true;
                        pageCount = noOfPages + 2;
                        totalChars = ((pageCount - 1) * secondPageCount) + firstPageCount;
                    }
                    String secondCount;
                    secondCount = String.valueOf(constantPageCount);
                    textView1.setText(secondCount);

                    String pageCountString;
                    pageCountString = String.valueOf(pageCount);
                    textView3.setText(pageCountString);
                    String charLength;
                    charLength = String.valueOf(lengthOfSubject);
                    textView4.setText(charLength);

                } else {
                    pageCount = 1;
                }
            }
        }
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int len = s.length();
                String charLength;
                charLength= String.valueOf(len);
                textView4.setText(charLength);

                if(len<=firstPageCount) {
                    pagefull=false;
                    int pageCount = 1;
                    String pageCountString = String.valueOf(pageCount);
                    constantPageCount = firstPageCount - len;
                    String countNow;
                    countNow = String.valueOf(constantPageCount);
                    textView1.setText(countNow);
                    textView3.setText(pageCountString);
                }else{
                    if(len<=firstPageCount+secondPageCount){
                        pagefull=false;
                        pageCount = 2;
                        String pageCountString;
                        pageCountString = String.valueOf(pageCount);
                        constantPageCount = secondPageCount - (len - firstPageCount);
                        String secondCount;
                        secondCount=String.valueOf(constantPageCount);
                        textView1.setText(secondCount);
                        textView3.setText(pageCountString);
                    }else{

                        if(len > totalChars) pagefull = false;
                        if(!pagefull)
                        {
                            pageCount = pageCount + 1;
                            totalChars = ((pageCount - 1) * secondPageCount) + firstPageCount;
                            pagefull=true;
                        }

                        constantPageCount = totalChars - len;

                        if(constantPageCount == secondPageCount && pagefull){
                            pageCount = pageCount - 1;
                            totalChars = ((pageCount -1) * secondPageCount) + firstPageCount;
                            constantPageCount=0;
                        }

                        String secondCount;
                        secondCount=String.valueOf(constantPageCount);
                        textView1.setText(secondCount);

                        String pageCountString;
                        pageCountString = String.valueOf(pageCount);
                        textView3.setText(pageCountString);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if(checkPermission(Manifest.permission.SEND_SMS)){
            sendSmsButton.setEnabled(true);
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
            ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        sendSmsButton.setOnClickListener(new View.OnClickListener() {
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
                String message, number;

                message = messageEditText.getText().toString();
                number = phoneNumberEditText.getText().toString();

                if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(number)) {

                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        try{
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number, null, message, null, null);

                            Toast.makeText(context,getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
                            if(sending_frag.contentEquals("MyCustomDialog")) {
                                if (fragmentActionListener != null) {
                                    if(frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFrag");
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }else if(sending_frag.contentEquals("companyMainMenu")) {
                                if (fragmentActionListener != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                                    fragmentActionListener.onActionPerformed(bundle);
                                }

                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")){
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE,merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
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
                            Toast.makeText(context,ex.toString(), Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(context,activity.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(context,activity.getResources().getString(R.string.enter_phone_number_error), Toast.LENGTH_LONG).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sending_frag.contentEquals("MyCustomDialog")) {
                    if (fragmentActionListener != null) {
                        if(frag.contentEquals("companyMainMenu")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("MerchantMainMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
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
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
                }else if(sending_frag.contentEquals("companyMainMenu")) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME,merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_USERNAME,merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")){
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE,merchantCode);
                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

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
                    sendSmsButton.setEnabled(true);
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
