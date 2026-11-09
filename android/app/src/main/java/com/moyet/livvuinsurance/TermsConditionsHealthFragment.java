package com.moyet.livvuinsurance;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class TermsConditionsHealthFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    FragmentActionListener fragmentActionListener;
    View rootView;
    String name, password, licence_number, username, confirmpassword, description, country_name,
            address, region,media,type,sector;
    TextView welcomeMsg;
    TextView textView;
    TextView textView1;
    TextView accept;
    TextView decline;
    String frag;
    String ip,server_url;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public TermsConditionsHealthFragment() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            name = bundle.getString(FragmentActionListener.KEY_NAME);
            licence_number = bundle.getString(FragmentActionListener.KEY_LICENCE_NO);
            username = bundle.getString(FragmentActionListener.KEY_USERNAME);
            password = bundle.getString(FragmentActionListener.KEY_PASSWORD);
            confirmpassword = bundle.getString(FragmentActionListener.KEY_CONFIRMPASSWORD);
            description = bundle.getString(FragmentActionListener.KEY_DESCRIPTION);
            sector = bundle.getString(FragmentActionListener.KEY_SECTOR);
            address = bundle.getString(FragmentActionListener.KEY_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_COUNTRYNAME);
            region = bundle.getString(FragmentActionListener.KEY_REGION);
            media = bundle.getString(FragmentActionListener.KEY_MEDIA);
            type = bundle.getString(FragmentActionListener.KEY_TYPE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
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
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            name = bundle.getString(FragmentActionListener.KEY_NAME);
            licence_number = bundle.getString(FragmentActionListener.KEY_LICENCE_NO);
            username = bundle.getString(FragmentActionListener.KEY_USERNAME);
            password = bundle.getString(FragmentActionListener.KEY_PASSWORD);
            confirmpassword = bundle.getString(FragmentActionListener.KEY_CONFIRMPASSWORD);
            description = bundle.getString(FragmentActionListener.KEY_DESCRIPTION);
            sector = bundle.getString(FragmentActionListener.KEY_SECTOR);
            address = bundle.getString(FragmentActionListener.KEY_ADDRESS);
            country_name = bundle.getString(FragmentActionListener.KEY_COUNTRYNAME);
            region = bundle.getString(FragmentActionListener.KEY_REGION);
            media = bundle.getString(FragmentActionListener.KEY_MEDIA);
            type = bundle.getString(FragmentActionListener.KEY_TYPE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_terms_conditions_health, container, false);
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
        accept = rootView.findViewById(R.id.tvAccept);
        decline = rootView.findViewById(R.id.tvDecline);
        textView= rootView.findViewById(R.id.textView);
        textView1= rootView.findViewById(R.id.textView1);

        decline.setOnClickListener(new View.OnClickListener() {
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

                Toast.makeText(context,activity.getResources().getString(R.string.registration_has_been_cancelled), Toast.LENGTH_LONG).show();

                //if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "TermsConditionsHealthFrag");
                    fragmentActionListener.onActionPerformed(bundle);
                //}
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
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
                server_url=ip +"register_health_institutions.php";


                stringRequest = new StringRequest(Request.Method.POST, server_url,
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
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 11);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            });
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
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("name",name);
                        param.put("licence_number",licence_number);
                        param.put("description",description);
                        param.put("sector",sector);
                        param.put("address",address);
                        param.put("region",region);
                        param.put("media",media);
                        param.put("username",username);
                        param.put("password",password);
                        param.put("country_name", country_name);
                        param.put("type", type);

                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
            }
        });

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
