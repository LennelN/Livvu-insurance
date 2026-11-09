package com.moyet.livvuinsurance;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class fragment_no_data_merchant_internet_connection extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    FragmentActionListener fragmentActionListener;
    View rootView;
    TextView tvRetry,cancelLink;
    String msg;
    String frag;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    String user_role,user_login_id,country,ip;
    String user_login_key,nameOfUser;
    String phone_no,session_id;


    public fragment_no_data_merchant_internet_connection() {
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
            user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
            user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
            merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
            user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
            merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
            user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
            merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
        }

        rootView = inflater.inflate(R.layout.fragment_no_data_internet_connection, container, false);
        tvRetry =rootView.findViewById(R.id.tvRetry);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
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
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this.dismiss()
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

                activity.finish();//finish();
            }
        });

        return rootView;
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
