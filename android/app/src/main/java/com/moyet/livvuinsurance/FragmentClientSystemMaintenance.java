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


public class FragmentClientSystemMaintenance extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    View rootView;
    FragmentActionListener fragmentActionListener;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "ClientSystemMain";
    boolean flag = false;
    String firstName;
    String lastName;
    String userName,phoneNo,userRole,errorMessage,URL,user_login_id,country;
    String ip,session_id,frag,main_frag;
    EditText etMenuChoice;
    private ProgressDialog pd;

    public FragmentClientSystemMaintenance() {
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
        rootView = inflater.inflate(R.layout.fragment_client_system_maintenance, container, false);
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
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
        }
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


        sendLink = rootView.findViewById(R.id.tvSendHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.setFocusable(true);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));

        cancelLink.setOnClickListener(new View.OnClickListener() {
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

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);

                final int choice = Integer.parseInt(etMenuChoice.getText().toString());
                switch (choice){

                    case 1:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,133);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ClientSystemMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "DeactivateClientAccountFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 2:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 134);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ClientSystemMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ActivateClientAccountFrag");
                            fragmentActionListener.onActionPerformed(bundle);

                        }

                        break;


                    case 3:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 129);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "ClientSystemMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ActivateResetClientPasswordFrag");
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
