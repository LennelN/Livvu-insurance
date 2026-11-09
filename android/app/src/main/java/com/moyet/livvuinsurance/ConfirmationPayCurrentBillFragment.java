package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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


public class ConfirmationPayCurrentBillFragment extends Fragment {

    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg;
    TextView sendLink;
    TextView cancelLink;
    View rootView;
    EditText etMenuChoice;
    String firstName;
    String lastName,merchantName,merchantCode,bill_number;
    String userName;
    String phone_no,noOfPoints;
    String media;
    String message,ip;
    String sending_frag,server_url,session_id,country_name;
    private static final String TAG = "ConfPayCurrBillFrag";
    ProgressDialog pd;
    private  int screenWidth,screenHeight;

    public ConfirmationPayCurrentBillFragment() {
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
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            userName = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            sending_frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
        }
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        welcomeMsg.setText(message);
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
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
            bill_number = bundle.getString(FragmentActionListener.KEY_BILL_NO);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            userName = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            sending_frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_confirmation_pay_bill, container, false);
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

        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,activity.getResources().getString(R.string.trans_cancelled), Toast.LENGTH_LONG).show();

                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 175);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillCurrentClientDetailsFrag");
                    fragmentActionListener.onActionPerformed(bundle);
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


                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
                final int choice = Integer.parseInt(etMenuChoice.getText().toString());

                switch(choice){
                    case 1:
                        media="mobile";

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
                        server_url=ip +"pay_current_client_bill.php";


                        stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
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
                                                if (fragmentActionListener != null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ConfirmationPayCurrentBillFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseFrag");
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            } else {
                                                pd.dismiss();
                                                if (fragmentActionListener != null) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 13);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,lastName);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ConfirmationPayCurrentBillFrag");
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
                                        error.printStackTrace();
                                        //error.getMessage();
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("merchantCode",merchantCode);
                                param.put("merchantName",merchantName);
                                param.put("bill_number",bill_number);
                                param.put("phone_no",phone_no);
                                param.put("total_no_of_points",noOfPoints);


                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley

                        break;

                    case 2:

                        Toast.makeText(context,activity.getResources().getString(R.string.trans_cancelled), Toast.LENGTH_LONG).show();

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }


                        break;

                    default:



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
                            txtview.setText(activity.getResources().getString(R.string.invalid_choice));

                            epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });


                        break;
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

            String choice = etMenuChoice.getText().toString();
            int cleanlength = choice.length();
            if(cleanlength>0) {
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ConfirmationPayCurrentBillFrag");
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


                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"ConfirmationPayCurrentBillFrag");
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
