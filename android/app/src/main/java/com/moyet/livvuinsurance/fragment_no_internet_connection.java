package com.moyet.livvuinsurance;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class fragment_no_internet_connection extends Fragment {
    FragmentActionListener fragmentActionListener;
    View rootView;
    private TextView tvRetry,tvWelcomeMsg,tvCancelHere;
    String message,session_id;
    private static final String TAG = "nointernetfrag";

    public fragment_no_internet_connection() {
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
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
        }

        rootView = inflater.inflate(R.layout.fragment_no_internet_connection, container, false);
        tvRetry = rootView.findViewById(R.id.tvRetry);

        tvWelcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        if (!message.isEmpty()){
            tvWelcomeMsg.setText(message);
        }

        //cancelLink = rootView.findViewById(R.id.tvCancelHere);

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                fragmentActionListener.onActionPerformed(bundle);
            }
        });


        return rootView;
    }

}
