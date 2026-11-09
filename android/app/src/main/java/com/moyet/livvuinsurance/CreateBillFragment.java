package com.moyet.livvuinsurance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateBillFragment extends Fragment implements AdapterView.OnItemClickListener{
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestCountry;
    StringRequest stringRequestTariff;
    StringRequest stringRequestDoctor;
    StringRequest stringRequestPatient;
    View rootView;
    private static final String TAG = "CreateBillFrag";
    private CreateBillFragment mFragment;
    FragmentActionListener fragmentActionListener;
    boolean flag = false;
    boolean bill_number_flag = false;
    String quantity,bill_date;
    TextView tvdescription,addBill,footer,searchBill;
    Spinner spinner;
    List<DoctorBillingDetails> doctorBillingDetails;
    boolean flag_desc = false;
    boolean flag_sector = false;
    boolean flag_region = false;
    String doctor_name_data_selected;
    String doctor_profession_data_selected;
    String doctor_phone_number_data_selected;
    String doctor_reg_no_data_selected;
    List<TariffDetails> tariffList;
    Spinner spinner_quantity,spinner_doctors;
    String URL,url_get_bill_items;
    String server_url;
    ArrayList<String> TariffCode;
    String code,description,category,type;
    String code2,description2,category2,type2;
    double price,price2;
    double total_price;
    double total_bill;
    TextView createBill;
    Button cancel,btnView,btnCancel;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    ArrayList<Model> itemList;
    ArrayList<Model> itemListDialog;
    ArrayList<String> listViewItems;
    String[] quantityName;
    EditText etDoctor,etPhone,etQuantity,total;
    ListView lv;
    listviewAdapter adapter;
    listviewAdapterDialog adapterDialog;
    ProgressDialog pd;
    AlertDialog.Builder alert;
    String ip;
    List<Items> items;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int key_tariff_position;
    boolean screenChanged = false;
    boolean screenChangedTariff = false;
    boolean screenChangedCountry = false;

    String tariffDate,country,sending_frag,user_login_key,user_role,user_login_id,nameOfUser,
            phone_number,bill_processed_by,session_id,code_returned_message,billed_phone_number;
    private  int screenWidth,screenHeight;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    double tariff_code_amount,tariff_code_total_amount,total_bill_amount;
    String date_transaction_created,merchant_doctor,tariff_code_billed,tariff_code_description,
            bill_number,tariff_code_frequency;
    String url_get_bill_doctor_details,url_spinner,country_name;
    int key_doctor_description_position;
    int key_country_position;
    ArrayList<String> CountryName;
    Spinner spinner_country;
    EditText etPatientName,etPatientBalance,etPatientBillNo;
    androidx.appcompat.app.AlertDialog.Builder builder;


    public CreateBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (CreateBillFragment)getFragmentManager().findFragmentByTag("CreateBillFrag");
        }
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
        setRetainInstance(true);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        setRetainInstance(true);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            }
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }


    public void onDestroy() {
        super.onDestroy();

        if(stringRequestAdapter != null){
            stringRequestAdapter.cancel();
        }
        if(stringRequest != null){
            stringRequest.cancel();
        }
        if(stringRequestCountry != null){
            stringRequestCountry.cancel();
        }
        if(stringRequestTariff != null){
            stringRequestTariff.cancel();
        }
        if(stringRequestDoctor != null){
            stringRequestDoctor.cancel();
        }
        if(stringRequestPatient != null){
            stringRequestPatient.cancel();
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_create_bill, container, false);

        if(savedInstanceState==null) {
            initUI();
        }else {

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


            String server_logged_in = ip + "check_login_status.php";
            checkLoginStatus(server_logged_in);
            screenChanged=true;
            screenChangedTariff=true;
            screenChangedCountry=true;

            lv = rootView.findViewById(R.id.list);
            //itemList = new ArrayList<Model>();
            //itemListDialog = new ArrayList<Model>();
            //adapter = new listviewAdapter(getContext(), itemList);
            //adapterDialog = new listviewAdapterDialog(getContext(), itemListDialog);
            tvdescription=rootView.findViewById(R.id.etDescription);
            //total_price = 0.00;
            //total_bill = 0.00;

            addBill=rootView.findViewById(R.id.add_bill);
            searchBill=rootView.findViewById(R.id.search_bill);
            TariffCode = new ArrayList<>();
            //spinner_quantity=rootView.findViewById(R.id.Quantity);
            tariffList = new ArrayList<>();
            items = new ArrayList<>();
            doctorBillingDetails = new ArrayList<>();
            spinner = rootView.findViewById(R.id.Code);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            spinner_doctors = rootView.findViewById(R.id.etDoctorPhone);
            URL=ip +"spinner_get_ancillary_tariffs.php";
            url_get_bill_items=ip+"get_patient_billing_details.php";
            url_get_bill_doctor_details=ip+"get_merchant_doctor_details.php";
            url_spinner=ip +"spinner_country.php";
            loadSpinnerCountryData(url_spinner);
            loadSpinnerData(URL);
            loadSpinnerDoctorData(url_get_bill_doctor_details);
            CountryName = new ArrayList<>();
            spinner_country = rootView.findViewById(R.id.country_Name);
            //loadSavedBillItems(url_get_bill_items);
            //loadSpinnerDataQuantity();

            //View headerView = getLayoutInflater().inflate(R.layout.listview_header,null);
            //lv.addHeaderView(headerView);
            //View footerView = getLayoutInflater().inflate(R.layout.listview_footer,null);
            //footer = footerView.findViewById(R.id.footer);
            total = rootView.findViewById(R.id.etTotal);
            createBill=rootView.findViewById(R.id.create_bill);
            cancel=rootView.findViewById(R.id.btnCancel);
            btnView=rootView.findViewById(R.id.btnView);
            //cancel.setEnabled(false);
            //createBill.addTextChangedListener(registerTextWatcher);
            DecimalFormat form = new DecimalFormat("0.00");
            total.setText(String.format(form.format(total_bill)));
            //lv.addFooterView(footerView,null,false);
            etQuantity=rootView.findViewById(R.id.etQuantity);
            etQuantity.addTextChangedListener(registerTextWatcher);
            etDoctor=rootView.findViewById(R.id.etDoctor);

            //etDoctor.requestFocus();
            etDoctor.addTextChangedListener(registerTextWatcher);
            etPhone=rootView.findViewById(R.id.etPhone);
            etPhone.addTextChangedListener(registerTextWatcher);
            etPhone.requestFocus();
            etPatientName=rootView.findViewById(R.id.etPatientName);
            etPatientBalance=rootView.findViewById(R.id.etPatientBalance);
            etPatientBillNo=rootView.findViewById(R.id.etPatientBillNo);

            etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        //loadSpinnerCountryData (url_spinner);
                        etPatientBillNo.setText("");
                        etPatientName.setText("");
                        etPatientBalance.setText("");
                    }
                }
            });

            searchBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    billed_phone_number = etPhone.getText().toString().trim();

                    if(billed_phone_number.length() > 0 && country_name != null)
                    {
                        if(!country_name.contentEquals("Select Country")) {
                            loadSpinnerPatientDetails(url_get_bill_items);
                        }
                    }
                }
            });

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int cnt = lv.getCount();

                    //itemListDialog = new ArrayList<Model>();
                    if(cnt > 0){

                        Dialog epicDialog = null;

                        if(getContext() != null && isAdded()) {
                            if(screenWidth==720 || screenWidth==1184 || screenWidth==1465) {
                                epicDialog = new Dialog(getContext(), R.style.MyDialogThemeSize16sp);
                            }else{
                                epicDialog = new Dialog(getContext());
                            }
                        }else{
                            context = getContextNullSafety();
                            if(screenWidth==720 || screenWidth==1184 || screenWidth==1465) {
                                epicDialog = new Dialog(context, R.style.MyDialogThemeSize16sp);
                            }else{
                                epicDialog = new Dialog(context);
                            }
                        }

                        if (getActivity() != null && isAdded()){
                            activity=getActivity();
                        }else{
                            activity=getActivityNullSafety();
                        }
                        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_cash_out, null);
                        btnCancel = footerView.findViewById(R.id.cancel);

                        itemListDialog = new ArrayList<Model>();
                        epicDialog.setContentView(R.layout.dialog_my_custom_listview_cancel_ok);
                        TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                        ListView listview = epicDialog.findViewById(R.id.list);

                        for (int i = 0; i < adapter.getCount(); i++) {
                            Model item = itemList.get(i);
                            String date_of_transaction = item.getBill_date().trim();
                            String no_of_transactions = item.getsNo().trim();
                            String code_to_array = item.getCode().trim();
                            String desc = item.getDescription().trim();
                            double price_to_array = item.getPrice();
                            double total_price_to_array = item.getTotalPrice();

                            Model item2;
                            item2 = new Model(no_of_transactions, code_to_array
                                    , desc, price_to_array, total_price_to_array, date_of_transaction);

                            itemListDialog.add(item2);

                        }
                        adapterDialog = new listviewAdapterDialog(context, itemListDialog);
                        listview.setAdapter(adapterDialog);
                        listview.addFooterView(footerView, null, false);
                        adapterDialog.notifyDataSetChanged();

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                int cnt2 = listview.getCount();

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

                                epicDialog.setContentView(R.layout.dialog_my_custom_message_cancel_ok);
                                TextView txtview = epicDialog.findViewById(R.id.message_text);
                                TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                TextView action_cancel = epicDialog.findViewById(R.id.action_cancel);
                                TextView txtview_title = epicDialog.findViewById(R.id.title_text);
                                txtview_title.setText(activity.getResources().getString(R.string.delete_dialog));
                                txtview.setText(activity.getResources().getString(R.string.do_you_want_to_delete_this_item));

                                epicDialog.show();
                                Dialog finalEpicDialog = epicDialog;
                                action_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalEpicDialog.dismiss();
                                        DecimalFormat form = new DecimalFormat("0.00");

                                        Model item = itemList.get(position);
                                        double totalPrice = item.getTotalPrice();
                                        total_bill = total_bill - totalPrice;
                                        itemList.remove(item);
                                        adapter = new listviewAdapter(context, itemList);
                                        lv.setAdapter(adapter);
                                        total.setText(String.format(form.format(total_bill)));

                                        Model item2 = itemListDialog.get(position);
                                        itemListDialog.remove(item2);
                                        adapterDialog = new listviewAdapterDialog(context, itemListDialog);
                                        listview.setAdapter(adapterDialog);

                                        //total.setText(String.format(form.format(total_bill)));
                                    }
                                });
                                Dialog finalEpicDialog1 = epicDialog;
                                action_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        itemListDialog = new ArrayList<Model>();
                                        finalEpicDialog1.dismiss();
                                    }
                                });


                            }
                        });
                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemListDialog = new ArrayList<Model>();
                                finalEpicDialog.dismiss();

                            }
                        });

                    }

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (fragmentActionListener != null) {
                        if (sending_frag.contentEquals("companyMainMenu")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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

                }
            });

            if(checkPermission(Manifest.permission.SEND_SMS)){
                //sendSmsButton.setEnabled(true);

            }else{

                ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
            }


            createBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listViewItems = new ArrayList<String>();
                    List<Model> dataArray;
                    dataArray = new ArrayList<Model>();

                    if (adapter.getCount() <= 0) {

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
                        txtview.setText(activity.getResources().getString(R.string.please_enter_tariff_details));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });

                    } else {
                        final String name_of_doctor;
                        final String phone_bill_no;
                        final String doctor_phone_no;
                        final String bill_no;
                        final String country_phone_no;
                        final String account_balance;

                        name_of_doctor = etDoctor.getText().toString().trim();
                        phone_bill_no = etPhone.getText().toString().trim();
                        bill_no = etPatientBillNo.getText().toString().trim();
                        account_balance = etPatientBalance.getText().toString().trim();

                        if(sending_frag.contentEquals("companyMainMenu")) {
                            bill_processed_by=merchantUserName;
                        }else{
                            bill_processed_by=user_login_id;
                        }
                        for (int i = 0; i < adapter.getCount(); i++) {
                            Model item = itemList.get(i);
                            String date_of_transaction = item.getBill_date().trim();
                            String no_of_transactions = item.getsNo().trim();
                            String code_to_array = item.getCode().trim();
                            String desc = item.getDescription().trim();
                            double price_to_array = item.getPrice();
                            double total_price_to_array = item.getTotalPrice();

                            Model dt = new Model(no_of_transactions, code_to_array, desc, price_to_array, total_price_to_array, date_of_transaction);

                            // add into list array
                            dataArray.add(dt);
                        }

                        Gson gson = new Gson();
                        final String newDataArray;
                        newDataArray = gson.toJson(dataArray);
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
                        server_url=ip +"create_bill_record.php";


                        stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
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
                                                String no_of_points_billed = jsonResponse.getString("total_amount_billed");
                                                if (checkPermission(Manifest.permission.SEND_SMS)) {
                                                    String billMessage;

                                                    billMessage = " Hi. You Have Been Billed by " + merchantName + " Your Total Bill Is " + no_of_points_billed +
                                                            " Please Make Arrangements To Pay Your Bill Within 30 days Thank You.";

                                                    try {
                                                        SmsManager smsManager = SmsManager.getDefault();
                                                        smsManager.sendTextMessage(phone_bill_no, null, billMessage, null, null);

                                                        Toast.makeText(context,activity.getResources().getString(R.string.message_sent), Toast.LENGTH_LONG).show();

                                                    } catch (Exception ex) {

                                                        Toast.makeText(context,ex.toString(), Toast.LENGTH_LONG).show();
                                                        ex.printStackTrace();
                                                    }
                                                } else {

                                                    Toast.makeText(context,activity.getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                                                }
                                                if (fragmentActionListener!=null) {
                                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                        fragmentActionListener.onActionPerformed(bundle);

                                                    }
                                                }
                                            } else {
                                                pd.dismiss();
                                                if (fragmentActionListener!=null) {
                                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
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
                                        txtview.setText(activity.getResources().getString(R.string.unable_to_process));

                                        epicDialog.show();
                                        Dialog finalEpicDialog = epicDialog;
                                        action_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finalEpicDialog.dismiss();
                                                if (fragmentActionListener != null) {
                                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                            }
                                        });
                                        error.printStackTrace();
                                        //error.getMessage();
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<String, String>();
                                DecimalFormat form = new DecimalFormat("0.00");
                                param.put("merchant_phone_number", merchantUserName);
                                param.put("bill_processed_by",bill_processed_by);
                                param.put("merchant_code", merchantCode);
                                param.put("name_of_doctor", name_of_doctor);
                                param.put("phone_bill_no", phone_bill_no);
                                param.put("total_bill_amount", String.format(form.format(total_bill)));
                                param.put("array", newDataArray);
                                param.put("doctor_phone_no",doctor_phone_number_data_selected);
                                param.put("bill_no",bill_no);
                                param.put("country_phone_no",country_name);
                                param.put("account_balance",account_balance);



                                return param;
                            }
                        };
                        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley
                    }
                }
            });

            View.OnClickListener addlistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSelectedUser(v);
                    adapter.notifyDataSetChanged();
                }
            };

            addBill.setOnClickListener(addlistener);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(screenChangedTariff) {
                        spinner.setSelection(key_tariff_position);
                        screenChangedTariff=false;
                    }else key_tariff_position = i;

                    TariffDetails tariffDetails = (TariffDetails) adapterView.getSelectedItem();

                    //key_tariff_position=i;
                    if (!flag)
                        flag = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });

            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(screenChangedCountry) {
                        spinner_country.setSelection(key_country_position);
                        screenChangedCountry=false;
                    }else key_country_position = i;

                    country_name = spinner_country.getItemAtPosition(spinner_country.getSelectedItemPosition()).toString();
                    billed_phone_number = etPhone.getText().toString().trim();

                    if(billed_phone_number.length() > 0 && country_name != null)
                    {
                        if(!country_name.contentEquals("Select Country")) {
                            loadSpinnerPatientDetails(url_get_bill_items);
                        }
                    }
                    //key_country_name=country_name;
                    if (!flag)
                        flag = true;

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });



            spinner_doctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // description = spinner_health_desc.getItemAtPosition(spinner_health_desc.getSelectedItemPosition()).toString();
                    if(screenChanged) {
                        spinner_doctors.setSelection(key_doctor_description_position);
                        screenChanged=false;
                        DoctorBillingDetails billRequestDetails = (DoctorBillingDetails) adapterView.getSelectedItem();
                        spinner_doctors.setSelection(key_doctor_description_position);
                        doctor_name_data_selected=billRequestDetails.getDoctorName();
                        if(doctor_name_data_selected != null){
                            etDoctor.setText(doctor_name_data_selected);
                        }
                        doctor_phone_number_data_selected=billRequestDetails.getDoctorPhone();
                        doctor_profession_data_selected=billRequestDetails.getDoctorProfession();
                        doctor_reg_no_data_selected=billRequestDetails.getDoctorRegNo();


                    }else{

                        key_doctor_description_position=i;

                        DoctorBillingDetails billRequestDetails = (DoctorBillingDetails) adapterView.getSelectedItem();
                        spinner_doctors.setSelection(key_doctor_description_position);
                        doctor_name_data_selected=billRequestDetails.getDoctorName();
                        if(doctor_name_data_selected != null){
                            etDoctor.setText(doctor_name_data_selected);
                        }
                        doctor_phone_number_data_selected=billRequestDetails.getDoctorPhone();
                        doctor_profession_data_selected=billRequestDetails.getDoctorProfession();
                        doctor_reg_no_data_selected=billRequestDetails.getDoctorRegNo();


                        //mobile=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    }



                    if (!flag_desc) {
                        flag_desc = true;
                        //etUsername.requestFocus();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);



        }
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


        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);

        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<Model>();
        itemListDialog = new ArrayList<Model>();
        adapter = new listviewAdapter(context, itemList);
        adapterDialog = new listviewAdapterDialog(context, itemListDialog);
        tvdescription=rootView.findViewById(R.id.etDescription);
        total_price = 0.00;
        total_bill = 0.00;

        addBill=rootView.findViewById(R.id.add_bill);
        searchBill=rootView.findViewById(R.id.search_bill);
        TariffCode = new ArrayList<>();
        //spinner_quantity=rootView.findViewById(R.id.Quantity);
        tariffList = new ArrayList<>();
        items = new ArrayList<>();
        doctorBillingDetails = new ArrayList<>();
        spinner = rootView.findViewById(R.id.Code);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        spinner_doctors = rootView.findViewById(R.id.etDoctorPhone);
        URL=ip +"spinner_get_ancillary_tariffs.php";
        url_get_bill_items=ip+"get_patient_billing_details.php";
        url_get_bill_doctor_details=ip+"get_merchant_doctor_details.php";
        url_spinner=ip +"spinner_country.php";
        loadSpinnerCountryData(url_spinner);
        loadSpinnerData(URL);
        loadSpinnerDoctorData(url_get_bill_doctor_details);
        CountryName = new ArrayList<>();
        spinner_country = rootView.findViewById(R.id.country_Name);
        //loadSavedBillItems(url_get_bill_items);
        //loadSpinnerDataQuantity();

        //View headerView = getLayoutInflater().inflate(R.layout.listview_header,null);
        //lv.addHeaderView(headerView);
        //View footerView = getLayoutInflater().inflate(R.layout.listview_footer,null);
        //footer = footerView.findViewById(R.id.footer);
        total = rootView.findViewById(R.id.etTotal);
        createBill=rootView.findViewById(R.id.create_bill);
        cancel=rootView.findViewById(R.id.btnCancel);
        btnView=rootView.findViewById(R.id.btnView);
        //cancel.setEnabled(false);
        //createBill.addTextChangedListener(registerTextWatcher);
        DecimalFormat form = new DecimalFormat("0.00");
        total.setText(String.format(form.format(total_bill)));
        //lv.addFooterView(footerView,null,false);
        etQuantity=rootView.findViewById(R.id.etQuantity);
        etQuantity.addTextChangedListener(registerTextWatcher);
        etDoctor=rootView.findViewById(R.id.etDoctor);

        //etDoctor.requestFocus();
        etDoctor.addTextChangedListener(registerTextWatcher);
        etPhone=rootView.findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(registerTextWatcher);
        etPhone.requestFocus();
        etPatientName=rootView.findViewById(R.id.etPatientName);
        etPatientBalance=rootView.findViewById(R.id.etPatientBalance);
        etPatientBillNo=rootView.findViewById(R.id.etPatientBillNo);

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //loadSpinnerCountryData (url_spinner);
                    etPatientBillNo.setText("");
                    etPatientName.setText("");
                    etPatientBalance.setText("");
                }
            }
        });

        searchBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billed_phone_number = etPhone.getText().toString().trim();

                if(billed_phone_number.length() > 0 && country_name != null)
                {
                    if(!country_name.contentEquals("Select Country")) {
                        loadSpinnerPatientDetails(url_get_bill_items);
                    }
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = lv.getCount();
                if(cnt > 0){

                    Dialog epicDialog = null;

                    if(getContext() != null && isAdded()) {

                        if(screenWidth==720 || screenWidth==1184 || screenWidth==1465) {
                            epicDialog = new Dialog(getContext(), R.style.MyDialogThemeSize16sp);
                        }else{
                            epicDialog = new Dialog(getContext());
                        }
                    }else{
                        context = getContextNullSafety();
                        if(screenWidth==720 || screenWidth==1184 || screenWidth==1465) {
                            epicDialog = new Dialog(context, R.style.MyDialogThemeSize16sp);
                        }else{
                            epicDialog = new Dialog(context);
                        }
                    }

                    if (getActivity() != null && isAdded()){
                        activity=getActivity();
                    }else{
                        activity=getActivityNullSafety();
                    }

                    itemListDialog = new ArrayList<Model>();

                    View footerView = getLayoutInflater().inflate(R.layout.listview_footer_cash_out, null);
                    btnCancel = footerView.findViewById(R.id.cancel);


                    epicDialog.setContentView(R.layout.dialog_my_custom_listview_cancel_ok);
                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                    ListView listview = epicDialog.findViewById(R.id.list);

                    for (int i = 0; i < adapter.getCount(); i++) {

                        Model item = itemList.get(i);
                        String date_of_transaction = item.getBill_date().trim();
                        String no_of_transactions = item.getsNo().trim();
                        String code_to_array = item.getCode().trim();
                        String desc = item.getDescription().trim();
                        double price_to_array = item.getPrice();
                        double total_price_to_array = item.getTotalPrice();

                        Model item2;
                        item2 = new Model(no_of_transactions, code_to_array
                                , desc, price_to_array, total_price_to_array, date_of_transaction);

                        itemListDialog.add(item2);

                    }

                    adapterDialog = new listviewAdapterDialog(context, itemListDialog);
                    listview.setAdapter(adapterDialog);
                    listview.addFooterView(footerView, null, false);
                    adapterDialog.notifyDataSetChanged();


                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            int cnt2 = listview.getCount();

                            Dialog epicDialogList = null;

                            if(getContext() != null && isAdded()) {
                                epicDialogList = new Dialog(getContext());
                            }else{
                                context = getContextNullSafety();
                                epicDialogList = new Dialog(context);
                            }

                            if (getActivity() != null && isAdded()){
                                activity=getActivity();
                            }else{
                                activity=getActivityNullSafety();
                            }
                            epicDialogList.setContentView(R.layout.dialog_my_custom_message_cancel_ok);
                            TextView txtview = epicDialogList.findViewById(R.id.message_text);
                            TextView action_ok = epicDialogList.findViewById(R.id.action_ok);
                            TextView action_cancel = epicDialogList.findViewById(R.id.action_cancel);
                            TextView txtview_title = epicDialogList.findViewById(R.id.title_text);
                            txtview_title.setText(activity.getResources().getString(R.string.delete_dialog));
                            txtview.setText(activity.getResources().getString(R.string.do_you_want_to_delete_this_item));

                            epicDialogList.show();
                            Dialog finalEpicDialogList = epicDialogList;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialogList.dismiss();
                                    DecimalFormat form = new DecimalFormat("0.00");

                                    Model item = itemList.get(position);
                                    double totalPrice = item.getTotalPrice();
                                    total_bill = total_bill - totalPrice;
                                    itemList.remove(item);
                                    adapter = new listviewAdapter(context, itemList);
                                    lv.setAdapter(adapter);
                                    total.setText(String.format(form.format(total_bill)));

                                    Model item2 = itemListDialog.get(position);
                                    itemListDialog.remove(item2);
                                    adapterDialog = new listviewAdapterDialog(context, itemListDialog);
                                    listview.setAdapter(adapterDialog);

                                    //total.setText(String.format(form.format(total_bill)));
                                }
                            });
                            Dialog finalEpicDialog1 = epicDialogList;
                            action_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    itemListDialog = new ArrayList<Model>();
                                    finalEpicDialog1.dismiss();
                                }
                            });


                        }
                    });
                    epicDialog.show();

                    Dialog finalEpicDialog = epicDialog;
                    btnCancel.setOnClickListener(new View.OnClickListener()
                    {
                            @Override
                            public void onClick(View v) {
                                itemListDialog = new ArrayList<Model>();
                                finalEpicDialog.dismiss();
                            }
                    });

                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fragmentActionListener != null) {
                    if (sending_frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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

            }
        });

        if(checkPermission(Manifest.permission.SEND_SMS)){
            //sendSmsButton.setEnabled(true);

        }else{

            ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listViewItems = new ArrayList<String>();
                List<Model> dataArray;
                dataArray = new ArrayList<Model>();

                if (adapter.getCount() <= 0) {

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
                    txtview.setText(activity.getResources().getString(R.string.please_enter_tariff_details));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });

                } else {
                    final String name_of_doctor;
                    final String phone_bill_no;
                    final String doctor_phone_no;
                    final String bill_no;
                    final String country_phone_no;
                    final String account_balance;

                    name_of_doctor = etDoctor.getText().toString().trim();
                    phone_bill_no = etPhone.getText().toString().trim();
                    bill_no = etPatientBillNo.getText().toString().trim();
                    account_balance = etPatientBalance.getText().toString().trim();

                    if(sending_frag.contentEquals("companyMainMenu")) {
                        bill_processed_by=merchantUserName;
                    }else{
                        bill_processed_by=user_login_id;
                    }
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Model item = itemList.get(i);
                        String date_of_transaction = item.getBill_date().trim();
                        String no_of_transactions = item.getsNo().trim();
                        String code_to_array = item.getCode().trim();
                        String desc = item.getDescription().trim();
                        double price_to_array = item.getPrice();
                        double total_price_to_array = item.getTotalPrice();

                        Model dt = new Model(no_of_transactions, code_to_array, desc, price_to_array, total_price_to_array, date_of_transaction);

                        // add into list array
                        dataArray.add(dt);
                    }

                    Gson gson = new Gson();
                    final String newDataArray;
                    newDataArray = gson.toJson(dataArray);

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
                    server_url=ip +"create_bill_record.php";


                    stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
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
                                            String no_of_points_billed = jsonResponse.getString("total_amount_billed");
                                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                                String billMessage;

                                                billMessage = " Hi. You Have Been Billed by " + merchantName + " Your Total Bill Is " + no_of_points_billed +
                                                        " Please Make Arrangements To Pay Your Bill Within 30 days Thank You.";

                                                try {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(phone_bill_no, null, billMessage, null, null);

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


                                                    Toast.makeText(context,activity.getResources().getString(R.string.message_sent), Toast.LENGTH_LONG).show();

                                                } catch (Exception ex) {

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


                                                    Toast.makeText(context,ex.toString(), Toast.LENGTH_LONG).show();
                                                    ex.printStackTrace();
                                                }
                                            } else {

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


                                                Toast.makeText(context,activity.getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                                            }
                                            if (fragmentActionListener!=null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);

                                                }
                                            }
                                        } else {
                                            pd.dismiss();
                                            if (fragmentActionListener!=null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 14);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_bill_no);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "RespBillFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }
                                            }
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
                                    txtview.setText(activity.getResources().getString(R.string.unable_to_process));

                                    epicDialog.show();
                                    Dialog finalEpicDialog = epicDialog;
                                    action_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finalEpicDialog.dismiss();
                                            if (fragmentActionListener != null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                        }
                                    });
                                    error.printStackTrace();
                                    //error.getMessage();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            DecimalFormat form = new DecimalFormat("0.00");
                            param.put("merchant_phone_number", merchantUserName);
                            param.put("bill_processed_by",bill_processed_by);
                            param.put("merchant_code", merchantCode);
                            param.put("name_of_doctor", name_of_doctor);
                            param.put("phone_bill_no", phone_bill_no);
                            param.put("total_bill_amount", String.format(form.format(total_bill)));
                            param.put("array", newDataArray);
                            param.put("doctor_phone_no",doctor_phone_number_data_selected);
                            param.put("bill_no",bill_no);
                            param.put("country_phone_no",country_name);
                            param.put("account_balance",account_balance);



                            return param;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley
                }
            }
        });

        View.OnClickListener addlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedUser(v);
                adapter.notifyDataSetChanged();
            }
        };

        addBill.setOnClickListener(addlistener);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TariffDetails tariffDetails = (TariffDetails) adapterView.getSelectedItem();

                key_tariff_position=i;
                if (!flag)
                    flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                key_country_position = i;

                country_name = spinner_country.getItemAtPosition(spinner_country.getSelectedItemPosition()).toString();
                billed_phone_number = etPhone.getText().toString().trim();

                if(billed_phone_number.length() > 0 && country_name != null)
                {
                    if(!country_name.contentEquals("Select Country")) {
                        loadSpinnerPatientDetails(url_get_bill_items);
                    }
                }
                //key_country_name=country_name;
                if (!flag)
                    flag = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });



        spinner_doctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // description = spinner_health_desc.getItemAtPosition(spinner_health_desc.getSelectedItemPosition()).toString();
                key_doctor_description_position=i;

                DoctorBillingDetails billRequestDetails = (DoctorBillingDetails) adapterView.getSelectedItem();
                spinner_doctors.setSelection(key_doctor_description_position);
                doctor_name_data_selected=billRequestDetails.getDoctorName();
                if(doctor_name_data_selected != null){
                    etDoctor.setText(doctor_name_data_selected);
                }
                doctor_phone_number_data_selected=billRequestDetails.getDoctorPhone();
                doctor_profession_data_selected=billRequestDetails.getDoctorProfession();
                doctor_reg_no_data_selected=billRequestDetails.getDoctorRegNo();


                if (!flag_desc) {
                    flag_desc = true;
                    //etUsername.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        context=getContext();

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
        epicDialog.setContentView(R.layout.dialog_my_custom_message_cancel_ok);
        TextView txtview = epicDialog.findViewById(R.id.message_text);
        TextView action_ok = epicDialog.findViewById(R.id.action_ok);
        TextView action_cancel = epicDialog.findViewById(R.id.action_cancel);
        TextView txtview_title = epicDialog.findViewById(R.id.title_text);
        txtview_title.setText(activity.getResources().getString(R.string.delete_dialog));
        txtview.setText(activity.getResources().getString(R.string.do_you_want_to_delete_this_item));

        epicDialog.show();
        Dialog finalEpicDialog = epicDialog;
        action_ok.setOnClickListener(new View.OnClickListener() {
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

                finalEpicDialog.dismiss();
                DecimalFormat form = new DecimalFormat("0.00");

                Model item = itemList.get(position);

                double total = item.getTotalPrice();
                total_bill = total_bill - total;
                itemList.remove(item);
                adapter = new listviewAdapter(context, itemList);
                lv.setAdapter(adapter);
                footer.setText(String.format(form.format(total_bill)));
            }
        });
        Dialog finalEpicDialog1 = epicDialog;
        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalEpicDialog1.dismiss();
            }
        });


    }
    private void checkLoginStatus(String server_logged_in) {
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

        stringRequest = new StringRequest(Request.Method.POST, server_logged_in,
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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
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

                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                if(sending_frag.contentEquals("companyMainMenu")) {
                    param.put("username", merchantUserName);
                }else{
                    param.put("username", user_login_id);
                }
                param.put("ssid",session_id);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

    }

    public void getSelectedUser(View v) {
        TariffDetails tariffDetails = (TariffDetails) spinner.getSelectedItem();
        displayUserData(tariffDetails);
    }

    private void displayUserData(TariffDetails tariffDetails) {
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


        String code = tariffDetails.getCode();
        quantity = etQuantity.getText().toString();
        double price = tariffDetails.getPrice();
        total_bill = total_bill + (Integer.parseInt(quantity) * price);
        total_price = (Integer.parseInt(quantity) * price);
        String description = tariffDetails.getDescription();
        DecimalFormat form = new DecimalFormat("0.00");
        double accountBalance = Double.parseDouble(etPatientBalance.getText().toString().trim());
        if(total_bill > accountBalance){
            total_bill = total_bill - (Integer.parseInt(quantity) * price);
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
            txtview.setText(activity.getResources().getString(R.string.bill_cannot_be_more_than_balance));

            epicDialog.show();
            Dialog finalEpicDialog = epicDialog;
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalEpicDialog.dismiss();
                }
            });

        }else {
            total.setText(String.format(form.format(total_bill)));
            bill_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            Model item;
            item = new Model(quantity, code, description, price, total_price, bill_date);
            itemList.add(item);
        }

    }

    private void loadSpinnerPatientDetails(String url){
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


        stringRequestPatient = new StringRequest(Request.Method.POST, url_get_bill_items,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                boolean success = jsonResponse.getBoolean("success");
                                code_returned_message = jsonResponse.getString("message");

                                if (success) {
                                    pd.dismiss();
                                    bill_number_flag=true;
                                    String patientName = jsonResponse.getString("full_name");
                                    String patientBalance = jsonResponse.getString("total_balance");
                                    String patientBillNo = jsonResponse.getString("bill_no");

                                    etPatientName.setText(patientName);
                                    etPatientBalance.setText(patientBalance);
                                    etPatientBillNo.setText(patientBillNo);

                                }else{
                                    pd.dismiss();
                                    bill_number_flag=false;
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
                                    txtview.setText(code_returned_message);

                                    epicDialog.show();
                                    Dialog finalEpicDialog = epicDialog;
                                    action_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finalEpicDialog.dismiss();
                                            if (fragmentActionListener != null) {
                                                if (sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                        }
                                    });
                                }
                            }

                        } catch (JSONException e) {

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
                            epicDialog.setContentView(R.layout.dialog_my_custom_message);
                            TextView txtview = epicDialog.findViewById(R.id.message_text);
                            TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                            txtview.setText(code_returned_message);

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                    if (fragmentActionListener != null) {
                                        if (sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                txtview.setText(activity.getResources().getString(R.string.unable_to_download_client_data));

                epicDialog.show();
                Dialog finalEpicDialog = epicDialog;
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalEpicDialog.dismiss();
                        if (fragmentActionListener != null) {
                            if (sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                    }
                });

                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", billed_phone_number);
                param.put("country_name",country_name);
                return param;
            }


        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestPatient);


    }
    private void loadSpinnerData(String url) {
        //RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

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
        //pd.setMessage(getResources().getString(R.string.processing_please_wait_dialog));
        //pd.setMessage(getResources().getString(R.string.loading_tariff_codes));
        //pd.show();

        stringRequestTariff = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            code_returned_message = jsonResponse.getString("message");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                code = jsonObject1.getString("tariff_code");
                                description = jsonObject1.getString("tariff_description");
                                price =  Double.parseDouble(jsonObject1.getString("tariff_price"));
                                category = jsonObject1.getString("tariff_category");
                                type = jsonObject1.getString("tariff_type");
                                //TariffCode.add(code);

                                TariffDetails tariffDetails = new TariffDetails(code,description,price,category,type);

                                tariffList.add(tariffDetails);

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

                            pd.dismiss();
                            ArrayAdapter<TariffDetails> adapter_spinner = new ArrayAdapter<>(context,
                                    R.layout.my_spinner, tariffList);
                            adapter_spinner.setDropDownViewResource(R.layout.my_spinner);
                            spinner.setAdapter(adapter_spinner);
                            //cancel.setEnabled(true);
                        } catch (JSONException e) {

                            pd.dismiss();
                            //cancel.setEnabled(true);
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
                            txtview.setText(code_returned_message);

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                    if (fragmentActionListener != null) {
                                        if(sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                txtview.setText(activity.getResources().getString(R.string.unable_to_download_tariff_data));

                epicDialog.show();
                Dialog finalEpicDialog = epicDialog;
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalEpicDialog.dismiss();
                        if (fragmentActionListener != null) {
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                    }
                });

                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", merchantUserName);
                return param;
            }


        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestTariff);

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


            String userDoctorInput = etDoctor.getText().toString().trim();
            String userPhoneInput = etPhone.getText().toString().trim();
            String userQuantityInput = etQuantity.getText().toString().trim();


            if(!userQuantityInput.isEmpty() && !userDoctorInput.isEmpty() && !userPhoneInput.isEmpty()) {
                addBill.setEnabled(true);
                addBill.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                addBill.setEnabled(false);
                addBill.setTextColor(activity.getResources().getColor(R.color.colorCell));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private boolean checkPermission(String permission) {
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

        int checkPermission = ContextCompat.checkSelfPermission(context,permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //sendSmsButton.setEnabled(true);
            }
        }
    }

    private void loadSpinnerDoctorData(String url) {

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


        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequestDoctor = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    boolean success = jsonResponse.getBoolean("success");
                    code_returned_message = jsonResponse.getString("message");
                    doctorBillingDetails = new ArrayList<>();

                    if(success) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String merchant_name_doctor = jsonObject1.getString("full_name");
                            String merchant_profession_doctor = jsonObject1.getString("profession");
                            String merchant_phone_number_doctor = jsonObject1.getString("phone_number");
                            String merchant_reg_no_doctor = jsonObject1.getString("registration_no");

                            DoctorBillingDetails billRequestDetails =
                                    new DoctorBillingDetails(merchant_name_doctor,
                                            merchant_phone_number_doctor, merchant_profession_doctor,
                                            merchant_reg_no_doctor);

                            doctorBillingDetails.add(billRequestDetails);
                            //spinner_region.notifyDataSetChanged();
                        }
                        //spinner_region.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryRegions));
                        ArrayAdapter<DoctorBillingDetails> spinnerArrayAdapter = new ArrayAdapter<>(context,
                                R.layout.my_spinner, doctorBillingDetails);
                        //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.my_spinner,doctorBillingDetails);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner_doctors.setAdapter(spinnerArrayAdapter);
                    }
                } catch (JSONException e) {

                    description=null;
                    ArrayAdapter<DoctorBillingDetails> spinnerArrayAdapter = new ArrayAdapter<>(context,
                            R.layout.my_spinner, doctorBillingDetails);
                    //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.my_spinner,doctorBillingDetails);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                    spinner_doctors.setAdapter(spinnerArrayAdapter);

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
                    txtview.setText(code_returned_message);

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("merchant_phone_number", merchantUserName);
                param.put("merchant_code", merchantCode);
                return param;
            }
        };

        Vconnection.getnInstance(activity).addRequestQue(stringRequestDoctor);

    }

    private void loadSpinnerCountryData(String url) {

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


        //RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequestCountry = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int cnt = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    CountryName.add("Select Country");
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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.my_spinner, CountryName);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner_country.setAdapter(spinnerArrayAdapter);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                    builder = new
                            androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {
                    builder = new
                            androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){
                    builder = new
                            androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                    builder = new
                            androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                }else{
                    builder = new
                            androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                }
                builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                        .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
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
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });

        Vconnection.getnInstance(getActivity()).addRequestQue(stringRequestCountry);


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