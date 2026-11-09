package com.moyet.livvuinsurance;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import java.util.Calendar;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class RegisterIndividualFragment extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    private static final String TAG = "RegIndividualFragment";
    private RegisterIndividualFragment mFragment;
    boolean flag = false;
    View rootView;
    FragmentActionListener fragmentActionListener;
    RadioGroup radioGroup;
    AlertDialog.Builder builder;
    String name, password, surname, username, confirmpassword, dob, gender, country_name,idno,key_country_name;
    EditText etSurname, etPassword, etName, etUsername, etDob,  etConfirmPassword,etIDNo;
    Spinner spinner;
    String URL;
    ArrayList<String> CountryName;
    private TextView registerLink;
    private TextView cancelLink;
    ProgressDialog pd;
    Fragment fragment;
    String ip;
    int key_country_position;
    boolean screenChanged=false;
    private  int screenWidth,screenHeight;

    public RegisterIndividualFragment() {
        // Required empty public constructor
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
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_register_individual, container, false);

        if(savedInstanceState==null) {
            initUI();
        }else{

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

            if(bundle!=null) {

                registerLink = rootView.findViewById(R.id.tvRegisterHere);
                radioGroup = rootView.findViewById(R.id.radio);
                etName = rootView.findViewById(R.id.etName);
                etName.requestFocus();
                etName.addTextChangedListener(registerTextWatcher);
                etUsername = rootView.findViewById(R.id.etPhoneNumber);
                etUsername.addTextChangedListener(registerTextWatcher);
                etSurname = rootView.findViewById(R.id.etSurname);
                etSurname.addTextChangedListener(registerTextWatcher);
                etIDNo= rootView.findViewById(R.id.etIDNo);
                etIDNo.addTextChangedListener(registerTextWatcher);
                etDob = rootView.findViewById(R.id.etDob);
                etDob.addTextChangedListener(dateWatcher);
                etDob.addTextChangedListener(registerTextWatcher);
                etPassword = rootView.findViewById(R.id.etPassword);
                etPassword.addTextChangedListener(registerTextWatcher);
                etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
                etConfirmPassword.addTextChangedListener(registerTextWatcher);
                URL=ip +"spinner_country.php";
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
                screenChanged=true;
                cancelLink = rootView.findViewById(R.id.tvCancelHere);

                CountryName = new ArrayList<>();
                spinner = rootView.findViewById(R.id.country_Name);
                spinner.setFocusable(true);
                spinner.setFocusableInTouchMode(true);

                loadSpinnerData(URL);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if(screenChanged) {
                            spinner.setSelection(key_country_position);
                            screenChanged=false;
                        }else key_country_position = i;

                        country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        if (!flag)
                            flag = true;
                        else
                            registerLink.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });

            }
            radioGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRadioButtonClicked(rootView);
                }
            });


            cancelLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RegisterIndividualFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            });

            registerLink.setOnClickListener(new View.OnClickListener() {
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

                    name = etName.getText().toString();
                    surname = etSurname.getText().toString();
                    dob = etDob.getText().toString();
                    username = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    idno= etIDNo.getText().toString();
                    confirmpassword = etConfirmPassword.getText().toString();


                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                    } else {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
                        gender = selectedRadioButton.getText().toString();
                    }

                    if (!(password.equals(confirmpassword))) {
                        builder.setTitle("Something Went Wrong");
                        builder.setMessage("Your Passwords Are Not Matching....Please Re-Enter!!!!");
                        displayAlert("input_error");
                        etPassword.requestFocus();
                    } else {
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 24);
                            bundle.putString(FragmentActionListener.KEY_NAME, name);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, surname);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                            bundle.putString(FragmentActionListener.KEY_DOB, dob);
                            bundle.putString(FragmentActionListener.KEY_IDNO, idno);
                            bundle.putString(FragmentActionListener.KEY_PASSWORD, password);
                            bundle.putString(FragmentActionListener.KEY_CONFIRMPASSWORD, confirmpassword);
                            bundle.putString(FragmentActionListener.KEY_GENDER, gender);
                            bundle.putString(FragmentActionListener.KEY_COUNTRYNAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TermsConditionsIndivFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RegisterIndividualFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
                }
            });


        }
        return rootView;
    }


    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (RegisterIndividualFragment) getFragmentManager().findFragmentByTag("individualReg");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
        }
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
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

        registerLink = rootView.findViewById(R.id.tvRegisterHere);
        radioGroup = rootView.findViewById(R.id.radio);
        etName = rootView.findViewById(R.id.etName);
        etName.requestFocus();
        etName.addTextChangedListener(registerTextWatcher);
        etUsername = rootView.findViewById(R.id.etPhoneNumber);
        etUsername.addTextChangedListener(registerTextWatcher);
        etSurname = rootView.findViewById(R.id.etSurname);
        etSurname.addTextChangedListener(registerTextWatcher);
        etIDNo= rootView.findViewById(R.id.etIDNo);
        etIDNo.addTextChangedListener(registerTextWatcher);
        etDob = rootView.findViewById(R.id.etDob);
        etDob.addTextChangedListener(dateWatcher);
        etDob.addTextChangedListener(registerTextWatcher);
        etPassword = rootView.findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(registerTextWatcher);
        etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
        etConfirmPassword.addTextChangedListener(registerTextWatcher);
        URL=ip +"spinner_country.php";
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

        final TextView cancelLink = rootView.findViewById(R.id.tvCancelHere);

        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        //spinner.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        loadSpinnerData(URL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                key_country_position = i;

                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                key_country_name=country_name;
                if (!flag)
                    flag = true;
                else
                    registerLink.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rootView);
            }
        });


        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 33);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RegisterIndividualFrag");
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
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
                name = etName.getText().toString();
                surname = etSurname.getText().toString();
                dob = etDob.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                idno= etIDNo.getText().toString();
                confirmpassword = etConfirmPassword.getText().toString();


                if (radioGroup.getCheckedRadioButtonId() == -1) {
                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
                    gender = selectedRadioButton.getText().toString();
                }

                if (!(password.equals(confirmpassword))) {
                    builder.setTitle("Something Went Wrong");
                    builder.setMessage("Your Passwords Are Not Matching....Please Re-Enter!!!!");
                    displayAlert("input_error");
                    etPassword.requestFocus();
                } else {
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 24);
                            bundle.putString(FragmentActionListener.KEY_NAME, name);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, surname);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                            bundle.putString(FragmentActionListener.KEY_DOB, dob);
                            bundle.putString(FragmentActionListener.KEY_IDNO, idno);
                            bundle.putString(FragmentActionListener.KEY_PASSWORD, password);
                            bundle.putString(FragmentActionListener.KEY_CONFIRMPASSWORD, confirmpassword);
                            bundle.putString(FragmentActionListener.KEY_GENDER, gender);
                            bundle.putString(FragmentActionListener.KEY_COUNTRYNAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TermsConditionsIndivFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "RegisterIndividualFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                }
            }
        });

    }

    private void displayAlert(final String code) {

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

        builder.setPositiveButton(activity.getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (code.equals("input_error")) {

                    etPassword.setText("");
                    etConfirmPassword.setText("");
                } else {

                    if (code.equals(getResources().getString(R.string.true_value))) {

                        activity.finish();
                    } else {

                        if (code.equals(getResources().getString(R.string.false_value))) {
                            etName.setText("");
                            etSurname.setText("");
                            etUsername.setText("");
                            etPassword.setText("");
                            etConfirmPassword.setText("");
                            etDob.setText("");
                        }
                    }
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void loadSpinnerData(String url) {
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

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                error.printStackTrace();

            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        Vconnection.getnInstance(activity).addRequestQue(stringRequest);
    }


    private TextWatcher dateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());

                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    if(mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                etDob.setText(current);
                etDob.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
            String usernameInput = etUsername.getText().toString().trim();
            String usernamePassword = etPassword.getText().toString().trim();
            int password_length = usernamePassword.length();
            String nameInput = etName.getText().toString().trim();
            String userSurnameInput = etSurname.getText().toString().trim();
            String userIDInput = etIDNo.getText().toString().trim();
            String userDobInput = etDob.getText().toString().trim();
            String clean = userDobInput.replaceAll("[^\\d.]", "");
            int cleanlength = clean.length();
            String cleanAsString = Integer.toString(cleanlength);
            String userConfirmPasswordInput = etConfirmPassword.getText().toString().trim();
            int confirm_password_length = userConfirmPasswordInput.length();

            if(cleanlength == 8 && confirm_password_length == 4 && !userIDInput.isEmpty()
                    && !usernameInput.isEmpty() && password_length == 4 && !nameInput.isEmpty()
                    && !userSurnameInput.isEmpty() && !userDobInput.isEmpty() && country_name!=null)
            {
                registerLink.setEnabled(true);
                registerLink.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                registerLink.setEnabled(false);
                registerLink.setTextColor(activity.getResources().getColor(R.color.colorCell));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onRadioButtonClicked(View view) {
        // Check to see if a button has been clicked.
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        int id = view.getId();
        if (id == R.id.male) {
            if (checked)
                // Code for same day service ...
                gender = "Male";
            etUsername.requestFocus();
        } else if (id == R.id.female) {
            if (checked)
                // Code for next day delivery ...
                gender = "Female";
            etUsername.requestFocus();
        }
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
