package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class FragmentAdminReportMenu extends Fragment {
    FragmentActionListener fragmentActionListener;
    private ProgressDialog pd;
    private static final String TAG = "MerAdminMenuFragment";
    boolean flag = false;
    View rootView;
    Context context;
    public FragmentAdminReportMenu() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_report_menu, container, false);
        return rootView;
    }

}
