package com.moyet.livvuinsurance;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class MyCustomDialogProcessing extends DialogFragment {

    private static final String TAG = "MyCustomDialogProcessing";


    public static MyCustomDialogProcessing getInstance() {
        MyCustomDialogProcessing fragment = new MyCustomDialogProcessing();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_my_custom_processing, container, false);
        TextView txtMessage = view.findViewById(R.id.message_text);

        String delete_message= getResources().getString(R.string.processing);

        txtMessage.setText(delete_message);

        return view;
    }

}


















