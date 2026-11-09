package com.moyet.livvuinsurance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class BillClientDetailsFragment extends Fragment implements AdapterView.OnItemClickListener{
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private BillClientDetailsFragment mFragment;
    private static final String TAG = "BillClientDetFrag";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 1;
    ArrayList<BillClientDetails> itemList;
    ArrayList<String> listViewItems;
    String URL;
    String ip;
    ListView lv;
    listviewClientAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String date_bill_created,code,bill_number,merchant_name,merchant_phone_number,session_id,country_name;
    String total_bill;
    String no_of_points;
    String firstName;
    String lastName;
    String userName;
    String phone_no;
    String message;
    Button cancel;
    FragmentActionListener fragmentActionListener;
    int checkedItem;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AlertDialog.Builder detailsDialog;
    private  int screenWidth,screenHeight;
    String sending_frag,main_frag,phoneNo,userRole,user_login_id,country,bill_amount,phone_number,phone_number_country;
    public BillClientDetailsFragment() {
        // Required empty public constructor
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
            mFragment = (BillClientDetailsFragment)getFragmentManager().findFragmentByTag("BillClientDetailsFrag");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //fragmentManager = getSupportFragmentManager();
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fragmentManager = getSupportFragmentManager();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
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

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        rootView = inflater.inflate(R.layout.fragment_bill_client_details, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            main_frag=bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
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

        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);
        lv = rootView.findViewById(R.id.list);
        itemList = new ArrayList<BillClientDetails>();
        URL=ip +"bill_get_client_details.php";
        loadBillClientData(URL);
        View footerView = getLayoutInflater().inflate(R.layout.listview_footer_client, null);
        cancel = footerView.findViewById(R.id.cancel);
        lv.addFooterView(footerView, null, false);
        //cancel=rootView.findViewById(R.id.cancel);
        adapter = new listviewClientAdapter(context, itemList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sending_frag.contentEquals("LivvuMainAccountsMenu")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAccountsMenu");
                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }else {

                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 121);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainMenuFrag");
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
        });



    }
    private void loadBillClientData(String url) {

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

        if((screenWidth==240 && screenHeight==320) ||(screenWidth==320 && screenHeight==240)) {
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

        stringRequestAdapter = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonResponse = jsonArray.getJSONObject(i);
                                    boolean success = jsonResponse.getBoolean("success");
                                    String message = jsonResponse.getString("message");
                                    if (success) {
                                        pd.dismiss();
                                        date_bill_created = jsonResponse.getString("date_bill_created");
                                        code = jsonResponse.getString("merchant_code");
                                        merchant_phone_number = jsonResponse.getString("merchant_phone_number");
                                        merchant_name = jsonResponse.getString("merchant_name");
                                        bill_number = jsonResponse.getString("bill_number");
                                        total_bill = jsonResponse.getString("total_bill_amount");
                                        bill_amount = jsonResponse.getString("bill_amount");
                                        phone_number = jsonResponse.getString("phone_number");
                                        phone_number_country = jsonResponse.getString("country");

                                        BillClientDetails billClientDetails =
                                                new BillClientDetails(date_bill_created,
                                                        merchant_name, code, total_bill,bill_amount,bill_number,phone_number,
                                                        phone_number_country);

                                        itemList.add(billClientDetails);
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        pd.dismiss();
                                        if (fragmentActionListener != null) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY,82);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_MESSAGE,message);
                                            bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME,userName);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME,firstName);
                                            bundle.putString(FragmentActionListener.KEY_PHONE_NO,userName);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG,"BillClientDetailsFrag");
                                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuResponseFrag");
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
                        error.printStackTrace();
                        ////error.getMessage();
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
                        txtview.setText(activity.getResources().getString(R.string.unable_to_download));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                                if (fragmentActionListener!=null) {
                                    if(sending_frag.contentEquals("LivvuMainAccountsMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAccountsMenu");
                                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {

                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 121);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainMenuFrag");
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", userName);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int deletePosition = position;
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

        final String[] choice = new String[]{activity.getResources().getString(R.string.pay_the_bill),
                activity.getResources().getString(R.string.view_bill_statement),
                activity.getResources().getString(R.string.view_details)};
        builder.setTitle(activity.getResources().getString(R.string.please_select));

        BillClientDetails item = itemList.get(deletePosition);
        final String billDate = item.getBill_date();
        final String billNo = item.getBill_number();
        final String merchantName = item.getName();
        final String totalBillAmount = item.getTotal_bill_amount();
        final String billAmount = item.getBill_amount();
        final String merchantCode = item.getCode();
        final String phoneNumber = item.getPhone_number();
        final String phoneCountry = item.getPhone_country();

        final String details = "Phone Number Billed: "+phoneNumber+"\n"+
                " Phone Number Country: "+phoneCountry+"\n"+
                " Original Bill Amount: "+billAmount;



        builder.setSingleChoiceItems(choice, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                switch(which)
                {
                    case 0:
                        checkedItem = 0;
                        break;

                    case 1:
                        checkedItem = 1;
                        break;

                    case 2:
                        checkedItem = 2;
                        break;
                }

            }
        });

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(checkedItem)
                {
                    case 0:
                        message = activity.getResources().getString(R.string.are_you_sure_you_want_to_pay_merchant)+ " " + merchantName + " " + activity.getResources().getString(R.string.outstanding_bill_number)
                                + " " + billNo + " " + activity.getResources().getString(R.string.a_total_sum_of) + " " + totalBillAmount;

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 16);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                            bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillClientDetailsFragment");
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmationPayBillFrag");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                        break;

                    case 1:

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 18);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_frag);
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, userName);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                            bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillStatementFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillClientDetailsFragment");
                            bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                        break;

                    case 2:

                        int cnt = 0;
                        //if(!clientName.contentEquals("Total")) {
                        if (details.length() > 0) {
                            cnt = cnt + 1;
                            if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                                detailsDialog = new
                                        AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                            }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                                    || screenWidth==720 || screenWidth==1184) {
                                detailsDialog = new
                                        AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                            }else if(screenWidth==600 || screenWidth==1024){
                                detailsDialog = new
                                        AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                            }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                                detailsDialog = new
                                        AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                            }else{
                                detailsDialog = new
                                        AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                            }

                            detailsDialog.setMessage(details);

                            detailsDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogDetails, int which) {
                                    dialogDetails.dismiss();
                                }
                            });

                            AlertDialog dialogDetails = detailsDialog.create();
                            dialogDetails.show();
                        }

                        break;

                    default:
                        throw new IllegalStateException(activity.getResources().getString(R.string.unexpected_value)+ " " + checkedItem);
                }

            }
        });


        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillClientDetailsFragment");
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

                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "BillClientDetailsFragment");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("username", user_login_id);
                    param.put("ssid", session_id);
                    return param;
                }

        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

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
