package com.moyet.livvuinsurance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentSubmitPhoneNumber extends Fragment{
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg;
    TextView sendLink;
    TextView cancelLink;
    private static final String TAG = "SubmitPhoneNoFrag";
    boolean flag = false;
    View rootView;
    EditText etMenuChoice;
    String firstName;
    String lastName;
    String userName;
    int key_country_position;
    Spinner spinner;
    ArrayList<String> CountryName;
    String url_spinner;
    String key_country_name,country_name;
    String phone_no,phone_number;
    String frag,ip;
    String message,server_url,errorMessage;
    private ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    int c;

    public FragmentSubmitPhoneNumber() {
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
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
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

        rootView = inflater.inflate(R.layout.fragment_submit_phone_no, container, false);
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


        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.setFocusable(true);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        welcomeMsg.setText(activity.getResources().getString(R.string.enter_phone_no));

        url_spinner=ip +"spinner_country.php";

        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        loadSpinnerData(url_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                key_country_position = i;

                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                key_country_name=country_name;
                if (!flag)
                    flag = true;
                else
                    sendLink.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                message = activity.getResources().getString(R.string.cancelled_forgot_password_submission);
                TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                txtview.setText(message);

                epicDialog.show();
                Dialog finalEpicDialog = epicDialog;
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalEpicDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                });

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

                phone_no = (etMenuChoice.getText().toString());

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
                server_url=ip +"enter_password_reset_phone_number.php";

                stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //JSONArray jsonArray = null;
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                    boolean success = jsonResponse.getBoolean("success");
                                    message = jsonResponse.getString("message");

                                    if (success) {
                                        pd.dismiss();

                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                                        fragmentActionListener.onActionPerformed(bundle);

                                    } else {
                                        pd.dismiss();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
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
                                errorMessage=activity.getResources().getString(R.string.unable_to_sign_in_please_check_your_internet_connection_and_try_again);
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
                        param.put("phone_no",phone_no);
                        param.put("login_country",country_name);
                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley

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

    private void loadSpinnerData(String url_spinner) {
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

        stringRequestSpinner = new StringRequest(Request.Method.GET, url_spinner, new Response.Listener<String>() {
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
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                }                                                        }
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinner); // vConnection i claas which used to connect volley

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
