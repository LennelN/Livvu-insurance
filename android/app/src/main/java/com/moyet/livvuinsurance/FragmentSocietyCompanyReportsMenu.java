package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentSocietyCompanyReportsMenu extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "SocietyCompRepMenuFrag";
    boolean flag = false;
    View rootView;
    String ip,URL,errorMessage,name;
    EditText etMenuChoice;
    FragmentActionListener fragmentActionListener;
    private ProgressDialog pd;
    String merchantName,merchantCode,merchantUserName,session_id,country_name;

    public FragmentSocietyCompanyReportsMenu() {
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
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
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
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
            merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            country_name = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
        }
        rootView= inflater.inflate(R.layout.fragment_society_company_reports_menu, container, false);
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
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));


        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);

                final int choice = Integer.parseInt(etMenuChoice.getText().toString());
                switch (choice) {

                    case 1:
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 73);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMemberStatusFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 2:
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 74);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyBillRecordsFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 3:
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 76);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyPointsPurchasesFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
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
