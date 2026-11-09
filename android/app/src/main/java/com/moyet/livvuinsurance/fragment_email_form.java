package com.moyet.livvuinsurance;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import com.android.volley.toolbox.StringRequest;


public class fragment_email_form extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    FragmentActionListener fragmentActionListener;
    Button btn_cancel;
    String merchant_name,merchant_code,username;
    AppCompatTextView textView1,textView3;
    int maxMessageCount=154;
    String ip,sending_frag,sms_contact_phone_no;
    String user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,session_id;
    String firstName,lastName,userName,country_name,main_frag,merchantName,merchantCode,merchantUserName;

    public fragment_email_form() {
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
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
                country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("ContactMenuFrag")){
                main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
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
                country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("ContactMenuFrag")){
                main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
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
                country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                sms_contact_phone_no = bundle.getString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO);
            }else if(sending_frag.contentEquals("ContactMenuFrag")){
                main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
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

        rootView =  inflater.inflate(R.layout.fragment_email_form, container, false);
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

        final EditText your_name        = rootView.findViewById(R.id.your_name);
        final EditText your_email       = rootView.findViewById(R.id.your_email);
        final EditText your_subject     = rootView.findViewById(R.id.your_subject);
        final EditText your_message     = rootView.findViewById(R.id.your_message);
        your_name.requestFocus();
        //your_subject.setText(name);
        textView1 = rootView.findViewById(R.id.textView1);
        textView3 = rootView.findViewById(R.id.textView3);

        final String strMaxMessageChoice;
        strMaxMessageChoice= String.valueOf(maxMessageCount);
        textView3.setText(strMaxMessageChoice);

        your_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();

                String charLength = String.valueOf(len);
                textView1.setText(charLength);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button email = rootView.findViewById(R.id.post_message);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name      = your_name.getText().toString();
                String email     = your_email.getText().toString();
                String subject   = your_subject.getText().toString();
                String message   = your_message.getText().toString();
                if (TextUtils.isEmpty(name)){
                    your_name.setError("Enter Your Name");
                    your_name.requestFocus();
                    return;
                }

                boolean onError = false;
                if (!isValidEmail(email)) {
                    onError = true;
                    your_email.setError("Invalid Email");
                    return;
                }

                if (TextUtils.isEmpty(subject)){
                    your_subject.setError("Enter Your Subject");
                    your_subject.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(message)){
                    your_message.setError("Enter Your Message");
                    your_message.requestFocus();
                    return;
                }

                Intent sendEmail = new Intent(Intent.ACTION_SEND);

                /* Fill it with Data */
                sendEmail.setType("plain/text");
                sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"qinisof@hotmail.com"});
                sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(Intent.EXTRA_TEXT,
                        "name:"+name+'\n'+"Email ID:"+email+'\n'+"Message:"+'\n'+message);

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(sendEmail, "Send mail..."));


            }
        });

        btn_cancel = rootView.findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if(sending_frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("SocietyCompanyMainMenuFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("IndividualMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 38);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("ContactMenuFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 137);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ContactMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
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
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, username);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE,merchant_code);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "contactForm");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                        bundle.putString(FragmentActionListener.KEY_SMS_CONTACT_PHONE_NO, sms_contact_phone_no);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
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
