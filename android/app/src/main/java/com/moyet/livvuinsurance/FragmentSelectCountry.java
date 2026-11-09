package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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


public class FragmentSelectCountry extends Fragment {
    View rootView;
    FragmentActionListener fragmentActionListener;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink,tvSearchCountry;
    private static final String TAG = "SelectCountryFrag";
    boolean flag = false;
    String firstName;
    String lastName;
    String userName,phoneNo,userRole,errorMessage,URL,user_login_id,country;
    String ip,frag;
    Spinner spinner;
    int key_country_position;
    boolean screenChanged=false;
    ArrayList<String> CountryName;
    String url_spinner,country_name,key_country_name,session_id,main_frag;
    private FragmentSelectCountry mFragment;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    EditText etCountrySearch;

    public FragmentSelectCountry() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentSelectCountry) getFragmentManager().findFragmentByTag("SelectCountryFrag");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }
        setRetainInstance(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_select_country, container, false);
        if(savedInstanceState==null) {
            initUI();
        }else{
            String server_logged_in = ip + "check_login_status.php";
            checkLoginStatus(server_logged_in);


            screenChanged=true;
            cancelLink = rootView.findViewById(R.id.tvCancelHere);
            sendLink = rootView.findViewById(R.id.tvSendHere);
            url_spinner=ip +"spinner_country.php";

            CountryName = new ArrayList<>();
            spinner = rootView.findViewById(R.id.country_Name);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            loadSpinnerData(url_spinner);

           spinner.focusSearch(View.FOCUS_DOWN);

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
                    if (fragmentActionListener != null) {
                        if(frag.contentEquals("AncilliaryTariffMaintenanceFrag") || frag.contentEquals("DeleteAdmissionAncillaryTariffFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 79);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "AncilliaryTariffMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("DemographicCountryFrag") || frag.contentEquals("DemographicHospitalCountryFrag")
                                 || frag.contentEquals("GroupCategoryCountFrag") || frag.contentEquals("PharmacyDemographicCountryCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,45);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuReportsMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("HospitalCountryReportFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("HospitalDemographicCountryCountFrag") || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("UpdateCurrencyTypeFrag") || frag.contentEquals("UpdateCurrencyRateFrag")){
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else{
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 193);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CountryContributionsMaintenanceFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
                }
            });

            sendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {

                        if (frag.contentEquals("AncilliaryTariffMaintenanceFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 81);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateDeleteAncilliaryTariffFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("DemographicCountryFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 40);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "DemographicCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("GroupCategoryCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 141);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "GroupCategoryCountFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("HospitalCountryReportFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 102);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalCountryReportFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("HospitalDemographicCountryCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 143);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalDemographicCountryCountFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("PharmacyDemographicCountryCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 146);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PharmacyDemographicCountryCountFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (frag.contentEquals("HospitalDemographicCountryRegionCountFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 145);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalDemographicCountryRegionCountFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("DeleteAdmissionAncillaryTariffFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 159);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "DeleteAdmissionAncillaryTariffFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if (frag.contentEquals("CountryContributionsMaintenanceFrag")){
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 195);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateContributingRateFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("UpdateCurrencyRateFrag")){
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 87);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateCurrencyRateFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(frag.contentEquals("UpdateCurrencyTypeFrag")){
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 197);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateCurrencyTypeFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                    }
                }
            });


        }
        return rootView;

    }

    private void initUI() {


        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);

        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        sendLink = rootView.findViewById(R.id.tvSendHere);
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
                if (fragmentActionListener != null) {
                    if(frag.contentEquals("AncilliaryTariffMaintenanceFrag") || frag.contentEquals("DeleteAdmissionAncillaryTariffFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 79);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "AncilliaryTariffMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("DemographicCountryFrag") || frag.contentEquals("DemographicHospitalCountryFrag")
                             || frag.contentEquals("GroupCategoryCountFrag") || frag.contentEquals("PharmacyDemographicCountryCountFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY,45);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuReportsMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("HospitalCountryReportFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("HospitalDemographicCountryCountFrag") || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")
                    ) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("UpdateCurrencyTypeFrag") || frag.contentEquals("UpdateCurrencyRateFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 193);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CountryContributionsMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if(frag.contentEquals("AncilliaryTariffMaintenanceFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 81);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateDeleteAncilliaryTariffFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("DeleteAdmissionAncillaryTariffFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 159);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "DeleteAdmissionAncillaryTariffFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("GroupCategoryCountFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 141);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "GroupCategoryCountFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("DemographicCountryFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 40);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "DemographicCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (frag.contentEquals("HospitalCountryReportFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 102);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalCountryReportFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (frag.contentEquals("PharmacyDemographicCountryCountFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 146);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PharmacyDemographicCountryCountFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (frag.contentEquals("HospitalDemographicCountryRegionCountFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 145);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalDemographicCountryRegionCountFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (frag.contentEquals("HospitalDemographicCountryCountFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 143);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalDemographicCountryCountFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (frag.contentEquals("CountryContributionsMaintenanceFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 195);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateContributingRateFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("UpdateCurrencyRateFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 87);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateCurrencyRateFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(frag.contentEquals("UpdateCurrencyTypeFrag")){
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 197);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_COUNTRY_NAME, country_name);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UpdateCurrencyTypeFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }
            }
        });

    }

    private void loadSpinnerData(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                    //spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryName));
                    //spinner.setDropDownViewResource(R.layout.my_spinner);
                    if(cnt > 0) {
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.my_spinner, CountryName);
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


                if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {
                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){
                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme720);
                }else{
                    builder = new
                            AlertDialog.Builder(getActivity(), R.style.MyDialogTheme18sp);
                }
                builder.setMessage(getResources().getString(R.string.unable_to_process))
                        .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (fragmentActionListener != null) {
                                    if(frag.contentEquals("AncilliaryTariffMaintenanceFrag") || frag.contentEquals("DeleteAdmissionAncillaryTariffFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 79);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "AncilliaryTariffMaintenanceFrag");
                                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("DemographicCountryFrag")  || frag.contentEquals("GroupCategoryCountFrag")
                                            || frag.contentEquals("PharmacyDemographicCountryCountFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY,45);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuReportsMenuFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("HospitalDemographicCountryCountFrag") || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("HospitalCountryReportFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY,142);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else if(frag.contentEquals("UpdateCurrencyTypeFrag") || frag.contentEquals("UpdateCurrencyRateFrag")){
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 85);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CurrencyRatesMaintenanceFrag");
                                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }                                                        }
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    private void checkLoginStatus(String server_logged_in) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_logged_in,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (!success) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 99);
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseLogoutSessionFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage=getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "SelectCountryFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username",user_login_id);
                param.put("ssid",session_id);
                return param;
            }
        };
        Vconnection.getnInstance(getActivity()).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

    }

}
