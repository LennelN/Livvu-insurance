package com.moyet.livvuinsurance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class ResponseFragment extends Fragment {

    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg;
    TextView ok;
    View rootView;
    String msg;
    String frag;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    String firstName;
    String lastName;
    String userName,user_role,user_login_id,country,country_name,mobile;
    String user_login_key,nameOfUser,phone_number,main_menu_frag,session_id,phoneNo,userRole;
    String phone_no;

    private static final String TAG = "ResponseFragment";

    public ResponseFragment() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }

        Bundle bundle = getArguments();
        if(bundle!=null) {
            msg = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);


            if(frag.contentEquals("ConfirmCashOutFragment") || frag.contentEquals("ConfirmationCashOutFragment") || frag.contentEquals("CashOutDetailsFrag")
            || frag.contentEquals("BillPatientStatementFrag")){
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                if(main_menu_frag.contentEquals("companyMainMenu")) {
                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                }else if(main_menu_frag.contentEquals("MerchantMainMenuFrag")){
                    user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                    nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                    phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                    user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                }
            }else if (frag.contentEquals("CompanyMainMenu") ||
                    frag.contentEquals("ConfirmCashOutFragment") || frag.contentEquals("ConfirmationCashOutFragment") ||
                    frag.contentEquals("NoOfPointsFragMerchant") || frag.contentEquals("bankingDetails") ||
                    frag.contentEquals("BillMerchantDetailsFrag") || frag.contentEquals("PaymentStatementFrag")
                    || frag.contentEquals("DeactivateMerchantUserFrag") || frag.contentEquals("ActivateMerchantUserFrag")
                    || frag.contentEquals("NoOfAdmissionsMerchantCountFrag") || frag.contentEquals("NoOfPatientsAttendedMerchantCountFrag")
            || frag.contentEquals("CostOfAdmissionsMerchantReportFrag") || frag.contentEquals("CostOfMedicinesMerchantReportFrag")
                    || frag.contentEquals("TariffCostOfMedicinesMerchantReportFrag")
                    || frag.contentEquals("ActiveDoctorsReportFrag")
                    || frag.contentEquals("DeActivatedDoctorsReportFrag")){

                if(frag.contentEquals("DeactivateMerchantUserFrag") || frag.contentEquals("ActivateMerchantUserFrag")) {
                    main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                    if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {
                        main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                    } else {

                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    }
                }else {

                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                }
            } else if(frag.contentEquals("ProcessPaymentsFrag") || frag.contentEquals("CashedOutPointsFrag")
            || frag.contentEquals("ProcessCashedOutPoints") || frag.contentEquals("MobilePurchasesFrag")){
                main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("DeactivateUserFrag") || frag.contentEquals("ActivateUserFrag")){
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("DeactivateMerchantAccountFrag") || frag.contentEquals("ActivateMerchantAccountFrag")
            || frag.contentEquals("DeactivateClientAccountFrag") || frag.contentEquals("ActivateClientAccountFrag")
            || frag.contentEquals("ActivateResetClientPasswordFrag")){
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("OperatorPurchasesFrag")){
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                mobile = bundle.getString(FragmentActionListener.KEY_MOBILE_OPERATOR);
                country_name  = bundle.getString(FragmentActionListener.KEY_MOBILE_OPERATOR_COUNTRY);

            }else if(frag.contentEquals("MerchantMainMenuFrag") || frag.contentEquals("NoOfPointsMerchantMainMenuFrag")
                    || frag.contentEquals("MerchantBalanceMainMenuFrag") || frag.contentEquals("MerchantMainMenuBankFrag")) {
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);

            }else if(frag.contentEquals("DemographicCountryFrag") || frag.contentEquals("HospitalCountryReportFrag")
                    || frag.contentEquals("DemographicContinentFrag") || frag.contentEquals("HospitalContinentReportFrag")
                    || frag.contentEquals("GroupCategoryCountFrag") || frag.contentEquals("HospitalDemographicCountryCountFrag")
                    || frag.contentEquals("HospitalDemographicContinentCountFrag") || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")
                    || frag.contentEquals("PharmacyDemographicCountryCountFrag") || frag.contentEquals("PharmacyDemographicContinentCountFrag")
                    || frag.contentEquals("NoOfAdmissionsCountryCountFrag") || frag.contentEquals("NoOfPatientsAttendedCountryCountFrag")
                    || frag.contentEquals("NoOfAdmissionsContinentCountFrag") || frag.contentEquals("NoOfPatientsAttendedContinentCountFrag")
                    || frag.contentEquals("CostOfMedicinesCountryReportFrag") || frag.contentEquals("CostOfMedicinesContinentReportFrag")
                    || frag.contentEquals("TariffCostOfMedicinesCountryReportFrag") || frag.contentEquals("TariffCostOfMedicinesContinentReportFrag")){
                        main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                        firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                        phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else{
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
        }
        welcomeMsg.setText(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            msg = bundle.getString(FragmentActionListener.KEY_MESSAGE);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            if(frag.contentEquals("ConfirmCashOutFragment") || frag.contentEquals("ConfirmationCashOutFragment") || frag.contentEquals("CashOutDetailsFrag")
            || frag.contentEquals("BillPatientStatementFrag")){

                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);

                if(main_menu_frag.contentEquals("companyMainMenu" )) {

                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                }else if(main_menu_frag.contentEquals("MerchantMainMenuFrag")){
                    user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                    nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                    phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                    user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                    merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                    userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                }

            }else if (frag.contentEquals("CompanyMainMenu") ||
                    frag.contentEquals("ConfirmCashOutFragment") || frag.contentEquals("ConfirmationCashOutFragment") ||
                    frag.contentEquals("NoOfPointsFragMerchant") || frag.contentEquals("bankingDetails") ||
                    frag.contentEquals("BillMerchantDetailsFrag") || frag.contentEquals("PaymentStatementFrag")
                    || frag.contentEquals("DeactivateMerchantUserFrag") || frag.contentEquals("ActivateMerchantUserFrag")
                    || frag.contentEquals("NoOfAdmissionsMerchantCountFrag") || frag.contentEquals("NoOfPatientsAttendedMerchantCountFrag")
            || frag.contentEquals("CostOfAdmissionsMerchantReportFrag") || frag.contentEquals("CostOfMedicinesMerchantReportFrag")
                    || frag.contentEquals("TariffCostOfMedicinesMerchantReportFrag")
                    || frag.contentEquals("ActiveDoctorsReportFrag")
                    || frag.contentEquals("DeActivatedDoctorsReportFrag")){

                if(frag.contentEquals("DeactivateMerchantUserFrag") || frag.contentEquals("ActivateMerchantUserFrag")){
                    main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);

                    if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {
                        main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                        user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                        phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                        user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                        merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                        userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                    } else {

                        merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                        merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                        merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                        country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                    }

                }else {

                    merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                    merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                }

            } else if(frag.contentEquals("ProcessPaymentsFrag") || frag.contentEquals("CashedOutPointsFrag")
                    || frag.contentEquals("ProcessCashedOutPoints") || frag.contentEquals("MobilePurchasesFrag") ){
                main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if ( frag.contentEquals("DeactivateUserFrag") || frag.contentEquals("ActivateUserFrag")){
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("DeactivateMerchantAccountFrag") || frag.contentEquals("ActivateMerchantAccountFrag")
                    || frag.contentEquals("DeactivateClientAccountFrag") || frag.contentEquals("ActivateClientAccountFrag")
                    || frag.contentEquals("ActivateResetClientPasswordFrag")){
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(frag.contentEquals("OperatorPurchasesFrag")){
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
                user_role= bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                mobile = bundle.getString(FragmentActionListener.KEY_MOBILE_OPERATOR);
                country_name  = bundle.getString(FragmentActionListener.KEY_MOBILE_OPERATOR_COUNTRY);
            }else if(frag.contentEquals("MerchantMainMenuFrag") || frag.contentEquals("NoOfPointsMerchantMainMenuFrag")
                    || frag.contentEquals("MerchantBalanceMainMenuFrag") || frag.contentEquals("MerchantMainMenuBankFrag")) {
                main_menu_frag= bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                user_login_key = bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);

            }else if(frag.contentEquals("DemographicCountryFrag") || frag.contentEquals("HospitalCountryReportFrag")
                    || frag.contentEquals("DemographicContinentFrag") || frag.contentEquals("HospitalContinentReportFrag")
                    || frag.contentEquals("GroupCategoryCountFrag") || frag.contentEquals("HospitalDemographicCountryCountFrag")
                    || frag.contentEquals("HospitalDemographicContinentCountFrag") || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")
                    || frag.contentEquals("PharmacyDemographicCountryCountFrag") || frag.contentEquals("PharmacyDemographicContinentCountFrag")
                    || frag.contentEquals("NoOfAdmissionsCountryCountFrag") || frag.contentEquals("NoOfPatientsAttendedCountryCountFrag")
                    || frag.contentEquals("NoOfAdmissionsContinentCountFrag") || frag.contentEquals("NoOfPatientsAttendedContinentCountFrag")
                    || frag.contentEquals("CostOfMedicinesCountryReportFrag") || frag.contentEquals("CostOfMedicinesContinentReportFrag")
                    || frag.contentEquals("TariffCostOfMedicinesCountryReportFrag") || frag.contentEquals("TariffCostOfMedicinesContinentReportFrag")){
                main_menu_frag = bundle.getString(FragmentActionListener.KEY_MAIN_MENU);
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else{

                    firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                    lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                    userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                    phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);

            }
        }

        rootView = inflater.inflate(R.layout.fragment_response, container, false);
        initUI();
        return rootView;
    }
    private void initUI() {

        welcomeMsg = rootView.findViewById(R.id.textView);
        ok = rootView.findViewById(R.id.tvSendHere);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag.contentEquals("CashOutDetailsFrag") || frag.contentEquals("BillPatientStatementFrag")) {
                    if (main_menu_frag.contentEquals("companyMainMenu")) {

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    } else if (frag.contentEquals("MerchantMainMenuFrag")) {

                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {

                                bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            }
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
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                            fragmentActionListener.onActionPerformed(bundle);

                        }
                    }

                } else if (frag.contentEquals("ConfirmationCashOutFragment")) {
                    if (main_menu_frag.contentEquals("companyMainMenu")) {

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
                    } else if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {
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
                    }

                } else if (frag.contentEquals("ConfirmCashOutFragment")) {

                    if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
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
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }


                } else if (frag.contentEquals("CompanyMainMenu")
                        || frag.contentEquals("ConfirmCashOutFragment") ||
                        frag.contentEquals("ConfirmationCashOutFragment") || frag.contentEquals("bankingDetails") ||
                        frag.contentEquals("NoOfPointsFragMerchant") || frag.contentEquals("BillMerchantDetailsFrag")
                        || frag.contentEquals("PaymentStatementFrag") || frag.contentEquals("NoOfAdmissionsMerchantCountFrag")
                        || frag.contentEquals("NoOfPatientsAttendedMerchantCountFrag") || frag.contentEquals("CostOfAdmissionsMerchantReportFrag")
                        || frag.contentEquals("CostOfMedicinesMerchantReportFrag") || frag.contentEquals("TariffCostOfMedicinesMerchantReportFrag")
                        || frag.contentEquals("ActiveDoctorsReportFrag")
                        || frag.contentEquals("DeActivatedDoctorsReportFrag")) {

                    if (frag.contentEquals("PaymentStatementFrag") || frag.contentEquals("NoOfAdmissionsMerchantCountFrag")
                            || frag.contentEquals("NoOfPatientsAttendedMerchantCountFrag") || frag.contentEquals("CostOfAdmissionsMerchantReportFrag")
                            || frag.contentEquals("CostOfMedicinesMerchantReportFrag") || frag.contentEquals("TariffCostOfMedicinesMerchantReportFrag")) {
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    } else {


                        if (fragmentActionListener != null) {
                            if (frag.contentEquals("ActiveDoctorsReportFrag")
                                    || frag.contentEquals("DeActivatedDoctorsReportFrag")) {

                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "companyMainMenu");
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);

                            } else{
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
                } else if (frag.contentEquals("ProcessPaymentsFrag") || frag.contentEquals("CashedOutPointsFrag")
                || frag.contentEquals("MobilePurchasesFrag")){
                    if (main_menu_frag.contentEquals("LivvuMainAccountsMenu")) {
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAccountsMenu");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }else{
                        if (fragmentActionListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 43);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAdminMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }
                }else if(frag.contentEquals("DeactivateMerchantAccountFrag") || frag.contentEquals("ActivateMerchantAccountFrag")) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 106);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantSystemMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }else if (frag.contentEquals("DeactivateClientAccountFrag") || frag.contentEquals("ActivateClientAccountFrag")
                        || frag.contentEquals("ActivateResetClientPasswordFrag")){
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 128);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ClientSystemMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }else if(frag.contentEquals("DeactivateUserFrag") || frag.contentEquals("ActivateUserFrag")) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 105);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "UserSystemMaintenanceFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                } else if (frag.contentEquals("ProcessCashedOutPoints")) {
                    //Log.e(TAG," phila frag now is "+frag+" and main menu frag is "+main_menu_frag);
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 46);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ProcessCashedOutPointsFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                } else if (frag.contentEquals("OperatorPurchasesFrag")) {
                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 50);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MobilePurchasesFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_MOBILE_OPERATOR, mobile);
                        bundle.putString(FragmentActionListener.KEY_MOBILE_OPERATOR_COUNTRY, country_name);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_MAIN_MENU,main_menu_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                } else if (frag.contentEquals("MerchantMainMenuFrag")) {

                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {

                            bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        } else {

                            bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                        }
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        fragmentActionListener.onActionPerformed(bundle);

                    }
                } else if (frag.contentEquals("DeactivateMerchantUserFrag") || frag.contentEquals("ActivateMerchantUserFrag")) {

                    if (main_menu_frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 95);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantUserMaintFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }else if (frag.contentEquals("NoOfPointsMerchantMainMenuFrag") || frag.contentEquals("MerchantBalanceMainMenuFrag")
                        || frag.contentEquals("MerchantMainMenuBankFrag")){
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
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                    fragmentActionListener.onActionPerformed(bundle);
                }else if(frag.contentEquals("DemographicCountryFrag") || frag.contentEquals("DemographicContinentFrag")
                        || frag.contentEquals("GroupCategoryCountFrag") || frag.contentEquals("PharmacyDemographicCountryCountFrag")
                        || frag.contentEquals("PharmacyDemographicContinentCountFrag")){

                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 45);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                    bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuReportsMenuFrag");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU,main_menu_frag);
                    fragmentActionListener.onActionPerformed(bundle);
                }else if(frag.contentEquals("HospitalCountryReportFrag") || frag.contentEquals("HospitalContinentReportFrag")
                        || frag.contentEquals("HospitalDemographicCountryCountFrag") || frag.contentEquals("HospitalDemographicContinentCountFrag")
                        || frag.contentEquals("HospitalDemographicCountryRegionCountFrag")  || frag.contentEquals("NoOfAdmissionsCountryCountFrag")
                        || frag.contentEquals("NoOfPatientsAttendedCountryCountFrag") || frag.contentEquals("NoOfAdmissionsContinentCountFrag")
                        || frag.contentEquals("NoOfPatientsAttendedContinentCountFrag") || frag.contentEquals("CostOfMedicinesCountryReportFrag")
                        || frag.contentEquals("CostOfMedicinesContinentReportFrag") || frag.contentEquals("TariffCostOfMedicinesCountryReportFrag")
                        || frag.contentEquals("TariffCostOfMedicinesContinentReportFrag")){

                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 142);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_NAME,firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME,phoneNo);
                    bundle.putString(FragmentActionListener.KEY_USERNAME,userName);
                    bundle.putString(FragmentActionListener.KEY_USER_ROLE,userRole);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "HospitalReportsMenuFrag");
                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, main_menu_frag);
                    bundle.putString(FragmentActionListener.KEY_MAIN_MENU,main_menu_frag);
                    fragmentActionListener.onActionPerformed(bundle);
                }else{

                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }

            }
        });
    }
}
