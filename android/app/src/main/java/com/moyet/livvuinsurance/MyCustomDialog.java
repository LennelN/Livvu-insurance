package com.moyet.livvuinsurance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class MyCustomDialog extends DialogFragment {

    //public static final String TAG = "MyCustomDialog";
    public static final String TAG = MyCustomDialog.class.getSimpleName();

    private TextView mActionOk, mActionCancel;
    FragmentActionListener fragmentActionListener;
    String firstName,lastName,merchantName,merchantUserName,merchantCode,bill_number,noOfPoints,transactionNo;
    String userName,phone_no,media,message,ip,sending_frag,billDate,billNo,phone_number,frag;
    String user_login_key,nameOfUser,user_role,user_login_id,country,session_id;

    public static MyCustomDialog getInstance() {
        MyCustomDialog fragment = new MyCustomDialog();
        return fragment;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(sending_frag==null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            transactionNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
        }else {

            if (sending_frag.contentEquals("CashOutDetailsFragment")) {
                //Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
                    message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
                    transactionNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                    if (frag.contentEquals("companyMainMenu")) {
                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    }

                }
            } else if (sending_frag.contentEquals("BillClientDetailsFragment")) {

                //Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                    lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                    noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                    billNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    billDate = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                }
            } else if (sending_frag.contentEquals("BillMerchantDetailsFragment")) {
                //Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
                    noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                    phone_number = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                    billNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                    billDate = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                    if (frag.contentEquals("companyMainMenu")) {
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_my_custom, container, false);

        if(sending_frag==null) {
            getDialog().dismiss();
        }else {
            if (sending_frag.contentEquals("CashOutDetailsFragment")) {

                Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
                    message = bundle.getString(FragmentActionListener.KEY_MESSAGE);
                    transactionNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                    if (frag.contentEquals("companyMainMenu")) {
                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    }
                }
            } else if (sending_frag.contentEquals("BillClientDetailsFragment")) {

                Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                    lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                    noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                    billNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    billDate = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                }
            } else if (sending_frag.contentEquals("BillMerchantDetailsFragment")) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
                    frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
                    noOfPoints = bundle.getString(FragmentActionListener.KEY_NO_OF_POINTS);
                    phone_number = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                    billNo = bundle.getString(FragmentActionListener.KEY_BILL_NO);
                    ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
                    billDate = bundle.getString(FragmentActionListener.KEY_DATE_BILL_CREATED);
                    if (frag.contentEquals("companyMainMenu")) {
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    }
                }
            }


            mActionCancel = view.findViewById(R.id.action_cancel);
            mActionOk = view.findViewById(R.id.action_ok);
            final RadioGroup surveyRadioGroup = view.findViewById(R.id.moodRadioGroup); //pull group
            if (sending_frag.contentEquals("CashOutDetailsFragment")) {
                final RadioButton radioButton = view.findViewById(R.id.radioButton1);
                final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);

                radioButton.setText(getResources().getString(R.string.confirm_payment_received));
                radioButton2.setText(getResources().getString(R.string.send_sms_reminder));

            } else if (sending_frag.contentEquals("BillMerchantDetailsFragment")) {
                final RadioButton radioButton = view.findViewById(R.id.radioButton1);
                final RadioButton radioButton2 = view.findViewById(R.id.radioButton2);

                radioButton.setText(getResources().getString(R.string.view_bill_statement));
                radioButton2.setText(getResources().getString(R.string.send_sms_reminder));
            }

            mActionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            if (sending_frag.contentEquals("CashOutDetailsFragment")) {
                mActionOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedId = surveyRadioGroup.getCheckedRadioButtonId();
                        final RadioButton selectedRadioButton = view.findViewById(selectedId); //get r button val via ID
                        String choice = selectedRadioButton.getText().toString();

                        if (choice.contentEquals("Confirm Payment Received")) {
                            if (fragmentActionListener != null) {
                                if (frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 31);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, transactionNo);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmCashOutFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 31);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, transactionNo);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmCashOutFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
                            }
                        } else if (choice.contentEquals("Send SMS Reminder")) {
                            Toast.makeText(getContext(), " 2  is selected", Toast.LENGTH_SHORT).show();

                        }

                        getDialog().dismiss();
                    }
                });
            } else if (sending_frag.contentEquals("BillClientDetailsFragment")) {
                mActionOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedId = surveyRadioGroup.getCheckedRadioButtonId();
                        final RadioButton selectedRadioButton = view.findViewById(selectedId); //get r button val via ID
                        String choice = selectedRadioButton.getText().toString();

                        if (choice.contentEquals("Pay The Bill")) {
                            message = "Are You Sure You Want To Pay Merchant " + merchantName + " Outstanding Bill Number " + billNo + " A Total Of " + noOfPoints + " Points ";
                            if (fragmentActionListener != null) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 16);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                                bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmationPayBillFrag");
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } else if (choice.contentEquals("View Bill Statement")) {
                            if (fragmentActionListener != null) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 18);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillStatementFrag");
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        }

                        getDialog().dismiss();
                    }
                });
            } else if (sending_frag.contentEquals("BillMerchantDetailsFragment")) {
                mActionOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = surveyRadioGroup.getCheckedRadioButtonId();
                        final RadioButton selectedRadioButton = view.findViewById(selectedId); //get r button val via ID
                        String choice = selectedRadioButton.getText().toString();

                        if (choice.contentEquals("View Bill Statement")) {

                            if (fragmentActionListener != null) {
                                if (frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 36);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                    bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantStatementFragment");
                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 36);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                    bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantStatementFragment");
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
                            }
                        } else if (choice.contentEquals("Send SMS Reminder")) {

                            if (fragmentActionListener != null) {
                                if (frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                    bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                    bundle.putString(FragmentActionListener.KEY_NO_OF_POINTS, noOfPoints);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                } else if (frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 35);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_number);
                                    bundle.putString(FragmentActionListener.KEY_BILL_NO, billNo);
                                    bundle.putString(FragmentActionListener.KEY_DATE_BILL_CREATED, billDate);
                                    bundle.putString(FragmentActionListener.KEY_NO_OF_POINTS, noOfPoints);
                                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU, frag);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "MyCustomDialog");
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SendSMSFragment");
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
                            }

                        }

                        getDialog().dismiss();
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        sending_frag = tag;
    }


}


















