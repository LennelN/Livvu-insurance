package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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


public class RegistrationTypeFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    private static final String TAG = "RegTypeFragment";
    private EditText userInput;
    private TextView tvSend;
    private TextView tvCancel;
    View rootView;
    String ip;
    FragmentActionListener fragmentActionListener;


    public RegistrationTypeFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_registration_type, container, false);
        initUI();
        return rootView;
    }

    private void initUI(){

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


        userInput = rootView.findViewById(R.id.etMenuChoice);
        tvSend = rootView.findViewById(R.id.tvSendHere);
        tvCancel = rootView.findViewById(R.id.tvCancelHere);
        userInput.setFocusable(true);
        userInput.requestFocus();
        userInput.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int choice = Integer.parseInt(userInput.getText().toString());
                switch (choice){

                    case 1:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,2);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "individualReg");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 2:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,3);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CompanyReg");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 3:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,19);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PrivateCompanyFragReg");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 4:
                        if (fragmentActionListener!=null) {
                            Bundle bundle = new Bundle();

                            bundle.putInt(FragmentActionListener.ACTION_KEY,20);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyGroupFragReg");
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
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

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RegistrationTypeFrag");
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        userInput.addTextChangedListener(registerTextWatcher);
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


            String choice = userInput.getText().toString();
            int cleanlength = choice.length();
            if(cleanlength>0) {
                tvSend.setEnabled(true);
                tvSend.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                tvSend.setEnabled(false);
                tvSend.setTextColor(activity.getResources().getColor(R.color.colorCell));
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
