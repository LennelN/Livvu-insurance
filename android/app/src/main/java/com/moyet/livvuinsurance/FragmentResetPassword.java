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
import android.widget.LinearLayout;
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

public class FragmentResetPassword extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    private View rootView;
    private static final String TAG = "ResetPasswordFrag";
    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg,sendLink,cancelLink;
    EditText etMenuChoice,etUserPwd,etConfirmUserPwd;
    String firstName,lastName,userName,phone_no,frag,merchantName,merchantCode,
            merchantUserName,errorMessage,URL,phone_number,ip,registration_code,user_role,nameOfUser,country,
            password,confirmpassword,user_login_id,user_login_key,session_id;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public FragmentResetPassword() {
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
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
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
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView= inflater.inflate(R.layout.fragment_reset_password, container, false);
        
        initUI();
        return  rootView;
        
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

        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        welcomeMsg.setText(activity.getResources().getString(R.string.reset_password_form));
        etUserPwd = rootView.findViewById(R.id.etUserPwd);
        etUserPwd.requestFocus();
        etUserPwd.addTextChangedListener(registerTextWatcher);
        etConfirmUserPwd = rootView.findViewById(R.id.etConfirmUserPwd);
        etConfirmUserPwd.addTextChangedListener(registerTextWatcher);

        URL=ip +"reset_password.php";
        cancelLink.setOnClickListener(new View.OnClickListener() {
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

                if (activity != null)
                    activity.finish();

            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = etUserPwd.getText().toString();
                confirmpassword=etConfirmUserPwd.getText().toString();
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
                    epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                    txtview.setText(activity.getResources().getString(R.string.passwords_not_matching_reenter));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                            etUserPwd.requestFocus();
                        }
                    });

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
                    stringRequest = new StringRequest(Request.Method.POST, URL,
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
                                            //Send an sms to the number being registered
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

                                            if (fragmentActionListener != null) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "userMainFrag");
                                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);

                                            }
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
                                            epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                                            TextView txtview = epicDialog.findViewById(R.id.message_text);
                                            TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                            txtview.setText(message);

                                            epicDialog.show();
                                            Dialog finalEpicDialog = epicDialog;
                                            action_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finalEpicDialog.dismiss();
                                                    if (getActivity() != null)
                                                        getActivity().finish();
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

                                    errorMessage = activity.getResources().getString(R.string.unable_to_complete_password_reset);
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
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_login_id", user_login_id);
                            params.put("password", password);
                            params.put("login_country", country);
                            return params;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequest);
                }
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

            String usernamePassword = etUserPwd.getText().toString().trim();
            String userConfirmPasswordInput = etConfirmUserPwd.getText().toString().trim();
            int confirm_password_length = userConfirmPasswordInput.length();
            int password_length = usernamePassword.length();

            if( password_length == 4 && confirm_password_length == 4 ) {
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
