package com.moyet.livvuinsurance;

import static android.Manifest.permission.SEND_SMS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FragmentActionListener{

    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();


        if (!checkPermission()) {
            requestPermission();

        }

        if(findViewById(R.id.activity_main_portrait)!= null){
            if(savedInstanceState==null) {
                addMainFragment();
                // MyCustomDialog newFragment = new MyCustomDialog();
                //newFragment.show(getFragmentManager(),"myCustomDialog");
            }
        }else{
            if(savedInstanceState==null) {
                addMainFragment();
            }
        }


    }

    @Override
    public void onActionPerformed(Bundle bundle) {

        int actionPerformed = bundle.getInt(FragmentActionListener.ACTION_KEY);

        switch (actionPerformed) {
            case 1:
                addRegistrationTypeFragment(bundle);
                break;

            case 2:
                addRegisterIndividualFragment(bundle);
                break;

            case 3:
                addRegisterCompanyFragment(bundle);
                break;

            case 4:
                addIndividualMainMenuFragment(bundle);
                break;

            case 5:
                addCompanyMainMenuFragment(bundle);
                break;

            case 6:
                addBankingServicesFragment(bundle);
                break;

            case 7:
                addBankingDetailsFragment(bundle);
                break;

            case 8:
                addPhoneNoFragment(bundle);
                break;

            case 9:
                addNoOfPointsFragment(bundle);
                break;

            case 10:
                addConfirmationFragment(bundle);
                break;

            case 11:
                addMainFragment_FromRegistration();
                break;

            case 12:
                addCreateBillFragment(bundle);
                break;

            case 13:
                addResponseFragment(bundle);
                break;

            case 14:
                addResponseBillFragment(bundle);
                break;

            case 15:
                addBillClientDetailsFragment(bundle);
                break;

            case 16:
                addConfirmationPayBillFragment(bundle);
                break;

            case 17:
                addNoOfPointsFragmentMerchant(bundle);
                break;

            case 18:
                addBillStatementFragment(bundle);
                break;

            case 19:
                addPrivateCompanyRegistrationFragment(bundle);
                break;

            case 20:
                addSocietyGroupRegistrationFragment(bundle);
                break;

            case 21:
                addBillMerchantDetailsFragment(bundle);
                break;

            case 22:
                addClientStatementFragment(bundle);
                break;

            case 23:
                addPurchasePointsFragment(bundle);
                break;

            case 24:
                addTermsConditionsIndividualFragment(bundle);
                break;

            case 25:
                addTermsConditionsHealthFragment(bundle);
                break;

            case 26:
                addTermsConditionsCompanyFragment(bundle);
                break;

            case 27:
                addTermsConditionsSocietyFragment(bundle);
                break;

            case 28:
                addPointsCashOutFragment(bundle);
                break;

            case 29:
                addConfirmationCashOutFragment(bundle);
                break;

            case 30:
                addCashOutDetailsFragment(bundle);
                break;

            case 31:
                addConfirmCashOutFragment(bundle);
                break;


            case 32:
                addNoInternetFragment(bundle);
                break;

            case 33:
                addMainFragment_FromNoInternetCoonection();
                break;

            case 34:
                addNoDataToDisplay(bundle);
                break;

            case 35:
                addSendSmsFragment(bundle);
                break;

            case 36:
                addBillMerchantStatementFragment(bundle);
                break;

            case 37:
                addReportsMainFragment(bundle);
                break;

            case 38:
                addContactFragment(bundle);
                break;

            case 39:
                addEmailFragment(bundle);
                break;

            case 40:
                addDemographicFragment(bundle);
                break;

            case 41:
                addPaymentDetailsFragment(bundle);
                break;

            case 42:
                addPaymentStatementFragment(bundle);
                break;

            case 43:
                addLivvuMainAdminMenuFragment(bundle);
                break;

            case 44:
                addProcessPaymentsFragment(bundle);
                break;

            case 45:
                addLivvuReportsMenuFragment(bundle);
                break;

            case 46:
                addCashedOutPointsFragment(bundle);
                break;

            case 47:
                addProcessCashedOutPointsFragment(bundle);
                break;

            case 48:
                addCreateSystemUsersFragment(bundle);
                break;

            case 49:
                addCompleteUserRegistrationFragment(bundle);
                break;

            case 50:
                addMobilePurchasesFragment(bundle);
                break;

            case 51:
                addSelectMobileOperatorFragment(bundle);
                break;

            case 52:
                addProcessMobilePurchasesFragment(bundle);
                break;

            case 53:
                addDeactivateUserFragment(bundle);
                break;

            case 54:
                addCreateMerchantUsersFragment(bundle);
                break;

            case 55:
                addMerchantAccountsMainMenuFragment(bundle);
                break;

            case 56:
                addMerchantAdminMainMenuFragment(bundle);
                break;

            case 57:
                addBrainTreeCardFormFragment(bundle);
                break;

            case 58:
                addMerchantAdminReportMenuFrag(bundle);
                break;

            case 59:
                addPatientMedicalReportsFrag(bundle);
                break;

            case 60:
                addPatientMedicalStatementFrag(bundle);
                break;

            case 61:
                addPatientMedicalSummaryFrag(bundle);
                break;

            case 62:
                addPatientMedicalBillsFrag(bundle);
                break;

            case 63:
                addPatientMedicalStatusFrag(bundle);
                break;

            case 64:
                addBillingStatusResponseFrag(bundle);
                break;

            case 65:
                addSocietyCompanyMainMenuFrag(bundle);
                break;

            case 66:
                addEnterMembersFrag(bundle);
                break;

            case 67:
                addResponseSocietyCompanyFrag(bundle);
                break;

            case 68:
                addRemoveSocietyCompanyFrag(bundle);
                break;

            case 69:
                addFragmentSocietyCompanyPurchasePoints(bundle);
                break;

            case 70:
                addFragmentPurchaseAllMembers(bundle);
                break;

            case 71:
                addFragmentPurchaseSelectedMembers(bundle);
                break;

            case 72:
                addFragmentSocietyCompanyReportsMenu(bundle);
                break;

            case 73:
                addFragmentSocietyCompanyMemberStatus(bundle);
                break;

            case 74:
                addFragmentSocietyCompanyBillRecords(bundle);
                break;

            case 75:
                addFragmentSocietyCompanyBillPaymentsReport(bundle);
                break;

            case 76:
                addFragmentSocietyCompanyPointsPurchasesReport(bundle);
                break;

            case 77:
                addFragmentMemberPointsPurchasesReport(bundle);
                break;

            case 78:
                addFragmentFragmentProcessSelectedPointsPurchases(bundle);
                break;

            case 79:
                addFragmentAncilliaryTariffMaintenance(bundle);
                break;

            case 80:
                addFragmentCreateAncilliaryTariff(bundle);
                break;

            case 81:
                addFragmentUpdateDeleteAncilliaryTariff(bundle);
                break;

            case 82:
                addFragmentLivvuResponse(bundle);
                break;

            case 83:
                addFragmentUpdateAncillaryTariff(bundle);
                break;

            case 84:
                addFragmentDeleteAncillaryTariff(bundle);
                break;

            case 85:
                addFragmentCurrencyRatesMaintenance(bundle);
                break;

            case 86:
                addFragmentCreateCurrencyRate(bundle);
                break;

            case 87:
                addFragmentUpdateCurrencyRate(bundle);
                break;

            case 88:
                addFragmentEnterCountryDetails(bundle);
                break;

            case 89:
                addFragmentSelectCountry(bundle);
                break;

            case 90:
                addFragmentSystemMaintenance(bundle);
                break;

            case 91:
                addFragmentCreateTypeOfInstitution(bundle);
                break;

            case 92:
                addFragmentEnterMobileOperator(bundle);
                break;

            case 93:
                addFragmentNoDataCompanyInternet(bundle);
                break;

            case 94:
                addFragmentNoDataMerchantInternet(bundle);
                break;

            case 95:
                addFragmentMerchantUserMaintenance(bundle);
                break;

            case 96:
                addFragmentDeactivateMerchantUser(bundle);
                break;

            case 97:
                addFragmentEnterAmountIndividual(bundle);
                break;

            case 98:
                addFragmentEnterAmountSociety(bundle);
                break;

            case 99:
                addFragmentResponseLogoutSession(bundle);
                break;

            case 100:
                addFragmentSelectContinent(bundle);
                break;

            case 101:
                addFragmentDemographicContinent(bundle);
                break;

            case 102:
                addFragmentHospitalCountryReport(bundle);
                break;

            case 103:
                addFragmentHospitalContinentReport(bundle);
                break;

            case 104:
                addFragmentEnterContinentDetails(bundle);
                break;

            case 105:
                addFragmentUserSystemMaintenance(bundle);
                break;

            case 106:
                addFragmentMerchantSystemMaintenance(bundle);
                break;

            case 107:
                addFragmentLivvuMainAccountsMenu(bundle);
                break;

            case 108:
                addFragmentDeactivateMerchantAccount(bundle);
                break;

            case 109:
                addFragmentActivateMerchantAccount(bundle);
                break;

            case 110:
                addFragmentActivateUser(bundle);
                break;

            case 111:
                addFragmentActivateMerchantUser(bundle);
                break;

            case 120:
                addSendSmsCashOutReminderFragment(bundle);
                break;

            case 121:
                addLivvuMainMenuFragment(bundle);
                break;

            case 122:
                addLivvuSystemMaintenanceFragment(bundle);
                break;

            case 123:
                addCreateLivvuSystemUsersFragment(bundle);
                break;

            case 124:
                addInternationalPurchasePointsFragment(bundle);
                break;

            case 125:
                addInternationalProcessSelectedPointsPurchasesFragment(bundle);
                break;

            case 126:
                addInternationalPurchaseAllMembersFragment(bundle);
                break;

            case 127:
                addResetPasswordFragment(bundle);
                break;

            case 128:
                addClientSystemMaintenanceFragment(bundle);
                break;

            case 129:
                addActivateResetClientPasswordFragment(bundle);
                break;

            case 130:
                addActivateResetMerchantAccountPassword(bundle);
                break;

            case 131:
                addActivateResetMerchantUserPasswordFragment(bundle);
                break;

            case 132:
                addActivateResetUserPasswordFragment(bundle);
                break;

            case 133:
                addDeactivateClientAccountFragment(bundle);
                break;

            case 134:
                addActivateClientAccountFragment(bundle);
                break;

            case 135:
                addSubmitPhoneNumberFragment(bundle);
                break;

            case 136:
                addResetPasswordStatementFragment(bundle);
                break;

            case 137:
                addContactMenuFragment(bundle);
                break;

            case 138:
                addPurchasePointsSouthAfricaFragment(bundle);
                break;

            case 139:
                addFragmentPurchaseSouthAfricaAllMembers(bundle);
                break;

            case 140:
                addFragmentFragmentProcessSelectedPointsSouthAfricaPurchases(bundle);
                break;

            case 141:
                addFragmentGroupCategoryCountReport(bundle);
                break;

            case 142:
                addFragmentHospitalReportsMenu(bundle);
                break;

            case 143:
                addFragmentHospitalDemographicCountryCount(bundle);
                break;

            case 144:
                addFragmentHospitalDemographicContinentCount(bundle);
                break;

            case 145:
                addFragmentHospitalDemographicCountryRegionCount(bundle);
                break;

            case 146:
                addFragmentPharmacyDemographicCountryCount(bundle);
                break;

            case 147:
                addFragmentPharmacyDemographicContinentCount(bundle);
                break;

            case 148:
                addFragmentEnterDateRange(bundle);
                break;

            case 149:
                addFragmentSelectContinentDateRange(bundle);
                break;

            case 150:
                addFragmentSelectCountryDateRange(bundle);
                break;

            case 151:
                addFragmentNoOfAdmissionsCountryCount(bundle);
                break;

            case 152:
                addFragmentNoOfAttendedPatientsCountryCount(bundle);
                break;

            case 153:
                addFragmentNoOfAdmissionsContinentCount(bundle);
                break;

            case 154:
                addFragmentNoOfPatientsAttendedContinentCount(bundle);
                break;

            case 155:
                addFragmentNoOfAdmissionsMerchantCount(bundle);
                break;

            case 156:
                addFragmentNoOfPatientsAttendedMerchantCount(bundle);
                break;

            case 157:
                addFragmentCostOfAdmissionsMerchantReport(bundle);
                break;

            case 158:
                addFragmentEnterAdmissionAncillaryTariff(bundle);
                break;

            case 159:
                addFragmentDeleteAdmissionAncillaryTariff(bundle);
                break;

            case 160:
                addFragmentRemoveAdmissionAncillaryTariff(bundle);
                break;

            case 161:
                addFragmentCostOfMedicinesMerchantReport(bundle);
                break;

            case 162:
                addFragmentTariffCostReport(bundle);
                break;

            case 163:
                addFragmentTariffCostOfMedicinesMerchantReport(bundle);
                break;

            case 164:
                addFragmentSelectContinentTariffDateRange(bundle);
                break;

            case 165:
                addFragmentSelectCountryTariffDateRange(bundle);
                break;

            case 166:
                addFragmentCostOfMedicinesCountryReport(bundle);
                break;

            case 167:
                addFragmentCostOfMedicinesContinentReport(bundle);
                break;

            case 168:
                addFragmentTariffCostOfMedicinesCountryReport(bundle);
                break;

            case 169:
                addFragmentTariffCostOfMedicinesContinentReport(bundle);
                break;

            case 170:
                addResponseCapturePatientDetailsFragment(bundle);
                break;

            case 171:
                addFragmentCapturePatientDetails(bundle);
                break;

            case 172:
                addFragmentPatientMedicalHistory(bundle);
                break;

            case 173:
                addFragmentBillPatientStatement(bundle);
                break;

            case 174:
                addFragmentPatientCurrentBill(bundle);
                break;

            case 175:
                addFragmentBillCurrentClientDetails(bundle);
                break;

            case 176:
                addConfirmationPayCurrentBillFragment(bundle);
                break;

            case 177:
                addFragmentBillCurrentStatement(bundle);
                break;

            case 178:
                addSendLivvuSmsFragment(bundle);
                break;

            case 179:
                addFragmentMerchantDoctorMaintenance(bundle);
                break;

            case 180:
                addFragmentRegisterMedicalDoctors(bundle);
                break;

            case 181:
                addFragmentDeactivateDoctorAccount(bundle);
                break;

            case 182:
                addFragmentActivateDoctorAccount(bundle);
                break;

            case 183:
                addFragmentActiveDoctorsReport(bundle);
                break;

            case 184:
                addFragmentDeActivatedDoctorsReport(bundle);
                break;

            case 185:
                addFragmentConfirmOTP(bundle);
                break;
            case 186:
                addFragmentTransferAmountContribution(bundle);
                break;
            case 187:
                addFragmentConfirmTransferAmountContribution(bundle);
                break;
            case 188:
                addFragmentTransferPhoneNo(bundle);
                break;
            case 189:
                addFragmentRequestBill(bundle);
                break;
            case 190:
                addFragmentEnterPaymentDetailsDateRange(bundle);
                break;
            case 191:
                addPhoneNoPaymentDetailsFragment(bundle);
                break;
            case 192:
                addFragmentSelectPhonePaymentDetailsDateRange(bundle);
                break;
            case 193:
                addFragmentCountryContributionsMaintenance(bundle);
                break;
            case 194:
                addFragmentCreateContributingRate(bundle);
                break;
            case 195:
                addFragmentUpdateContributingRate(bundle);
                break;
            case 196:
                addFragmentCreateCurrencyType(bundle);
                break;
            case 197:
                addFragmentUpdateCurrencyType(bundle);
                break;
            case 198:
                addFragmentCurrencyRateReport(bundle);
                break;
            case 199:
                addFragmentCurrencyTypeReport(bundle);
                break;

        }

    }

    private void addFragmentCurrencyTypeReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCurrencyTypeReport fragmentCurrencyTypeReport=new FragmentCurrencyTypeReport();
        fragmentCurrencyTypeReport.setFragmentActionListener(this);
        fragmentCurrencyTypeReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCurrencyTypeReport,"CurrencyTypeReportFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCurrencyRateReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCurrencyRateReport fragmentCurrencyRateReport=new FragmentCurrencyRateReport();
        fragmentCurrencyRateReport.setFragmentActionListener(this);
        fragmentCurrencyRateReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCurrencyRateReport,"CurrencyRateReportFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentUpdateCurrencyType(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUpdateCurrencyType fragmentUpdateCurrencyType=new FragmentUpdateCurrencyType();
        fragmentUpdateCurrencyType.setFragmentActionListener(this);
        fragmentUpdateCurrencyType.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUpdateCurrencyType,"UpdateCurrencyTypeFrag");
        fragmentTransaction.commit();

    }


    private void addFragmentCreateCurrencyType(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateCurrencyType fragmentCreateCurrencyType=new FragmentCreateCurrencyType();
        fragmentCreateCurrencyType.setFragmentActionListener(this);
        fragmentCreateCurrencyType.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateCurrencyType,"CreateCurrencyTypeFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentUpdateContributingRate(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUpdateContributingRate fragmentUpdateContributingRate=new FragmentUpdateContributingRate();
        fragmentUpdateContributingRate.setFragmentActionListener(this);
        fragmentUpdateContributingRate.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUpdateContributingRate
                ,"UpdateContributingRateFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCreateContributingRate(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateContributingRate fragmentCreateContributingRate=new FragmentCreateContributingRate();
        fragmentCreateContributingRate.setFragmentActionListener(this);
        fragmentCreateContributingRate.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,
                fragmentCreateContributingRate,"CreateContributingRateFrag");
        fragmentTransaction.commit();

    }


    private void addFragmentCountryContributionsMaintenance(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCountryContributionsMaintenance fragmentCountryContributionsMaintenance
                =new FragmentCountryContributionsMaintenance();
        fragmentCountryContributionsMaintenance.setArguments(bundle);
        fragmentCountryContributionsMaintenance.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,
                fragmentCountryContributionsMaintenance,"CountryContributionsMaintenanceFrag");

        fragmentTransaction.commit();

    }


    private void addFragmentSelectPhonePaymentDetailsDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectPhonePaymentDetailsDateRange fragmentSelectPhonePaymentDetailsDateRange
                =new FragmentSelectPhonePaymentDetailsDateRange();
        fragmentSelectPhonePaymentDetailsDateRange.setArguments(bundle);
        fragmentSelectPhonePaymentDetailsDateRange.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,
                fragmentSelectPhonePaymentDetailsDateRange,"SelectPhonePaymentDetailsDateRange");

        fragmentTransaction.commit();

    }


    private void addPhoneNoPaymentDetailsFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        PhoneNoPaymentDetailsFragment phoneNoPaymentDetailsFragment=new PhoneNoPaymentDetailsFragment();
        phoneNoPaymentDetailsFragment.setFragmentActionListener(this);
        phoneNoPaymentDetailsFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,phoneNoPaymentDetailsFragment,"PhoneNoPaymentDetailsFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }


    private void addFragmentEnterPaymentDetailsDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_enter_date_payment_details_range_report
                fragment_enter_date_payment_details_range_report=new fragment_enter_date_payment_details_range_report();
        fragment_enter_date_payment_details_range_report.setArguments(bundle);
        fragment_enter_date_payment_details_range_report.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment_enter_date_payment_details_range_report,"EnterPaymentDetailsDateRangeFrag");

        fragmentTransaction.commit();

    }


    private void addFragmentRequestBill(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentRequestBill
                fragmentRequestBill=new FragmentRequestBill();
        fragmentRequestBill.setFragmentActionListener(this);
        fragmentRequestBill.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentRequestBill,
                "RequestBillFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentTransferPhoneNo(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTransferPhoneNo
                fragmentTransferPhoneNo=new FragmentTransferPhoneNo();
        fragmentTransferPhoneNo.setFragmentActionListener(this);
        fragmentTransferPhoneNo.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTransferPhoneNo,
                "TransferPhoneNoFrag");
        fragmentTransaction.commit();


    }


    private void addFragmentConfirmTransferAmountContribution(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentConfirmTransferAmountContribution
                fragmentConfirmTransferAmountContribution=new FragmentConfirmTransferAmountContribution();
        fragmentConfirmTransferAmountContribution.setFragmentActionListener(this);
        fragmentConfirmTransferAmountContribution.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentConfirmTransferAmountContribution,
                "ConfirmTransferAmountContributionFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentTransferAmountContribution(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTransferAmountContribution
                fragmentTransferAmountContribution=new FragmentTransferAmountContribution();
        fragmentTransferAmountContribution.setFragmentActionListener(this);
        fragmentTransferAmountContribution.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTransferAmountContribution,
                "TransferAmountContributionFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentConfirmOTP(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        ConfirmOTPFragment confirmOTPFragment=new ConfirmOTPFragment();
        confirmOTPFragment.setFragmentActionListener(this);
        confirmOTPFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,confirmOTPFragment,"ConfirmOTPFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentDeActivatedDoctorsReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        DeActivatedDoctorsReportFragment deActivatedDoctorsReportFragment=new DeActivatedDoctorsReportFragment();
        deActivatedDoctorsReportFragment.setFragmentActionListener(this);
        deActivatedDoctorsReportFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,deActivatedDoctorsReportFragment,"DeActivatedDoctorsReportFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentActiveDoctorsReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        ActiveDoctorsReportFragment activeDoctorsReportFragment=new ActiveDoctorsReportFragment();
        activeDoctorsReportFragment.setFragmentActionListener(this);
        activeDoctorsReportFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,activeDoctorsReportFragment,"ActiveDoctorsReportFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentActivateDoctorAccount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateDoctorAccount fragmentActivateDoctorAccount=new FragmentActivateDoctorAccount();
        fragmentActivateDoctorAccount.setFragmentActionListener(this);
        fragmentActivateDoctorAccount.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateDoctorAccount,"ActivateDoctorAccountFrag");
        fragmentTransaction.commit();


    }


    private void addFragmentDeactivateDoctorAccount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeactivateDoctorAccount fragmentDeactivateDoctorAccount=new FragmentDeactivateDoctorAccount();
        fragmentDeactivateDoctorAccount.setFragmentActionListener(this);
        fragmentDeactivateDoctorAccount.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeactivateDoctorAccount,"DeactivateDoctorAccountFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentRegisterMedicalDoctors(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentRegisterMedicalDoctors fragmentRegisterMedicalDoctors=new FragmentRegisterMedicalDoctors();
        fragmentRegisterMedicalDoctors.setFragmentActionListener(this);
        fragmentRegisterMedicalDoctors.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentRegisterMedicalDoctors,"RegisterMedicalDoctorsFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentMerchantDoctorMaintenance(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantDoctorMaintenance fragmentMerchantDoctorMaintenance=new FragmentMerchantDoctorMaintenance();
        fragmentMerchantDoctorMaintenance.setFragmentActionListener(this);
        fragmentMerchantDoctorMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantDoctorMaintenance,"MerchantDoctorMaintFrag");
        fragmentTransaction.commit();
    }


    private void addSendLivvuSmsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_send_livvu_sms fragmentSendLivvuSms=new fragment_send_livvu_sms();
        fragmentSendLivvuSms.setFragmentActionListener(this);
        fragmentSendLivvuSms.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSendLivvuSms);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addFragmentBillCurrentStatement(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        BillCurrentStatementFragment billCurrentStatementFragment=new BillCurrentStatementFragment();
        billCurrentStatementFragment.setFragmentActionListener(this);
        billCurrentStatementFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,billCurrentStatementFragment,"BillCurrentStatementFrag");
        fragmentTransaction.commit();

    }

    private void addConfirmationPayCurrentBillFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        ConfirmationPayCurrentBillFragment confirmationPayCurrentBillFragment=new ConfirmationPayCurrentBillFragment();
        confirmationPayCurrentBillFragment.setFragmentActionListener(this);
        confirmationPayCurrentBillFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,confirmationPayCurrentBillFragment,"ConfirmationPayCurrentBillFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentBillCurrentClientDetails(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        BillCurrentClientDetailsFragment billCurrentClientDetailsFragment=new BillCurrentClientDetailsFragment();
        billCurrentClientDetailsFragment.setFragmentActionListener(this);
        billCurrentClientDetailsFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,billCurrentClientDetailsFragment,"BillCurrentClientDetailsFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentPatientCurrentBill(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientCurrentBill fragmentPatientCurrentBill=new FragmentPatientCurrentBill();
        fragmentPatientCurrentBill.setArguments(bundle);
        fragmentPatientCurrentBill.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientCurrentBill,"PatientCurrentBillFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentBillPatientStatement(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        BillPatientStatementFragment billPatientStatementFragment=new BillPatientStatementFragment();
        billPatientStatementFragment.setArguments(bundle);
        billPatientStatementFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,billPatientStatementFragment,"BillPatientStatementFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentPatientMedicalHistory(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalHistory fragmentPatientMedicalHistory=new FragmentPatientMedicalHistory();
        fragmentPatientMedicalHistory.setArguments(bundle);
        fragmentPatientMedicalHistory.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalHistory,"PatientMedicalHistoryReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentCapturePatientDetails(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCapturePatientDetails fragmentCapturePatientDetails=new FragmentCapturePatientDetails();
        fragmentCapturePatientDetails.setArguments(bundle);
        fragmentCapturePatientDetails.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCapturePatientDetails,"CapturePatientDetailsFrag");

        fragmentTransaction.commit();

    }

    private void addResponseCapturePatientDetailsFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        ResponseCapturePatientDetailsFragment responseCapturePatientDetailsFragment=new ResponseCapturePatientDetailsFragment();
        responseCapturePatientDetailsFragment.setArguments(bundle);
        responseCapturePatientDetailsFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,responseCapturePatientDetailsFragment,"ResponseCapturePatientDetailsFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentTariffCostOfMedicinesContinentReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTariffCostOfMedicinesContinentReport fragmentTariffCostOfMedicinesContinentReport=new FragmentTariffCostOfMedicinesContinentReport();
        fragmentTariffCostOfMedicinesContinentReport.setArguments(bundle);
        fragmentTariffCostOfMedicinesContinentReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTariffCostOfMedicinesContinentReport,"TariffCostOfMedicinesContinentReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentTariffCostOfMedicinesCountryReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTariffCostOfMedicinesCountryReport fragmentTariffCostOfMedicinesCountryReport=new FragmentTariffCostOfMedicinesCountryReport();
        fragmentTariffCostOfMedicinesCountryReport.setArguments(bundle);
        fragmentTariffCostOfMedicinesCountryReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTariffCostOfMedicinesCountryReport,"TariffCostOfMedicinesCountryReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentCostOfMedicinesContinentReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCostOfMedicinesContinentReport fragmentCostOfMedicinesContinentReport=new FragmentCostOfMedicinesContinentReport();
        fragmentCostOfMedicinesContinentReport.setArguments(bundle);
        fragmentCostOfMedicinesContinentReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCostOfMedicinesContinentReport,"CostOfMedicinesContinentReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentCostOfMedicinesCountryReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCostOfMedicinesCountryReport fragmentCostOfMedicinesCountryReport=new FragmentCostOfMedicinesCountryReport();
        fragmentCostOfMedicinesCountryReport.setArguments(bundle);
        fragmentCostOfMedicinesCountryReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCostOfMedicinesCountryReport,"CostOfMedicinesCountryReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentSelectCountryTariffDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectCountryTariffDateRange fragmentSelectCountryTariffDateRange=new FragmentSelectCountryTariffDateRange();
        fragmentSelectCountryTariffDateRange.setArguments(bundle);
        fragmentSelectCountryTariffDateRange.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectCountryTariffDateRange,"SelectCountryTariffDateRangeFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentSelectContinentTariffDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectContinentTariffDateRange fragmentSelectContinentTariffDateRange=new FragmentSelectContinentTariffDateRange();
        fragmentSelectContinentTariffDateRange.setArguments(bundle);
        fragmentSelectContinentTariffDateRange.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectContinentTariffDateRange,"SelectContinentTariffDateRangeFrag");

        fragmentTransaction.commit();

    }


    private void addFragmentTariffCostOfMedicinesMerchantReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTariffCostOfMedicinesReport fragmentTariffCostOfMedicinesReport=new FragmentTariffCostOfMedicinesReport();
        fragmentTariffCostOfMedicinesReport.setArguments(bundle);
        fragmentTariffCostOfMedicinesReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTariffCostOfMedicinesReport,"TariffCostOfMedicinesMerchantReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentTariffCostReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentTariffCostReport fragmentTariffCostReport=new FragmentTariffCostReport();
        fragmentTariffCostReport.setArguments(bundle);
        fragmentTariffCostReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentTariffCostReport,"TariffCostReportFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentCostOfMedicinesMerchantReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCostOfMedicinesReport fragmentCostOfMedicinesReport=new FragmentCostOfMedicinesReport();
        fragmentCostOfMedicinesReport.setArguments(bundle);
        fragmentCostOfMedicinesReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCostOfMedicinesReport,"CostOfMedicinesMerchantReportFrag");

        fragmentTransaction.commit();
    }

    private void addFragmentRemoveAdmissionAncillaryTariff(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentRemoveAdmissionAncillaryTariff fragmentRemoveAdmissionAncillaryTariff=new FragmentRemoveAdmissionAncillaryTariff();
        fragmentRemoveAdmissionAncillaryTariff.setArguments(bundle);
        fragmentRemoveAdmissionAncillaryTariff.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentRemoveAdmissionAncillaryTariff,"RemoveAdmissionAncillaryTariffFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentDeleteAdmissionAncillaryTariff(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeleteAdmissionAncillaryTariff fragmentDeleteAdmissionAncillaryTariff=new FragmentDeleteAdmissionAncillaryTariff();
        fragmentDeleteAdmissionAncillaryTariff.setArguments(bundle);
        fragmentDeleteAdmissionAncillaryTariff.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeleteAdmissionAncillaryTariff,"DeleteAdmissionAncillaryTariffFrag");

        fragmentTransaction.commit();
    }

    private void addFragmentEnterAdmissionAncillaryTariff(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentEnterAdmissionAncillaryTariff fragmentEnterAdmissionAncillaryTariff=new FragmentEnterAdmissionAncillaryTariff();
        fragmentEnterAdmissionAncillaryTariff.setArguments(bundle);
        fragmentEnterAdmissionAncillaryTariff.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterAdmissionAncillaryTariff,"EnterAdmissionAncillaryTariffFrag");

        fragmentTransaction.commit();
    }

    private void addFragmentCostOfAdmissionsMerchantReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCostOfAdmissionsReport fragmentCostOfAdmissionsReport=new FragmentCostOfAdmissionsReport();
        fragmentCostOfAdmissionsReport.setArguments(bundle);
        fragmentCostOfAdmissionsReport.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCostOfAdmissionsReport,"CostOfAdmissionsMerchantReportFrag");

        fragmentTransaction.commit();


    }

    private void addFragmentNoOfPatientsAttendedMerchantCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfPatientsAttendedMerchantCount fragmentNoOfPatientsAttendedMerchantCount=new FragmentNoOfPatientsAttendedMerchantCount();
        fragmentNoOfPatientsAttendedMerchantCount.setArguments(bundle);
        fragmentNoOfPatientsAttendedMerchantCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfPatientsAttendedMerchantCount,"NoOfPatientsAttendedMerchantCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentNoOfAdmissionsMerchantCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfAdmissionsMerchantCount fragmentNoOfAdmissionsMerchantCount=new FragmentNoOfAdmissionsMerchantCount();
        fragmentNoOfAdmissionsMerchantCount.setArguments(bundle);
        fragmentNoOfAdmissionsMerchantCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfAdmissionsMerchantCount,"NoOfAdmissionsMerchantCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentNoOfPatientsAttendedContinentCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfPatientsAttendedContinentCount fragmentNoOfPatientsAttendedContinentCount=new FragmentNoOfPatientsAttendedContinentCount();
        fragmentNoOfPatientsAttendedContinentCount.setArguments(bundle);
        fragmentNoOfPatientsAttendedContinentCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfPatientsAttendedContinentCount,"NoOfPatientsAttendedContinentCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentNoOfAdmissionsContinentCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfAdmissionsContinentCount fragmentNoOfAdmissionsContinentCount=new FragmentNoOfAdmissionsContinentCount();
        fragmentNoOfAdmissionsContinentCount.setArguments(bundle);
        fragmentNoOfAdmissionsContinentCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfAdmissionsContinentCount,"NoOfAdmissionsContinentCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentNoOfAttendedPatientsCountryCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfPatientsAttendedCountryCount fragmentNoOfPatientsAttendedCountryCount=new FragmentNoOfPatientsAttendedCountryCount();
        fragmentNoOfPatientsAttendedCountryCount.setArguments(bundle);
        fragmentNoOfPatientsAttendedCountryCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfPatientsAttendedCountryCount,"NoOfPatientsAttendedCountryCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentNoOfAdmissionsCountryCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentNoOfAdmissionsCountryCount fragmentNoOfAdmissionsCountryCount=new FragmentNoOfAdmissionsCountryCount();
        fragmentNoOfAdmissionsCountryCount.setArguments(bundle);
        fragmentNoOfAdmissionsCountryCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoOfAdmissionsCountryCount,"NoOfAdmissionsCountryCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentSelectCountryDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectCountryDateRange fragmentSelectCountryDateRange=new FragmentSelectCountryDateRange();
        fragmentSelectCountryDateRange.setArguments(bundle);
        fragmentSelectCountryDateRange.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectCountryDateRange,"SelectCountryDateRangeFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentSelectContinentDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectContinentDateRange fragmentSelectContinentDateRange=new FragmentSelectContinentDateRange();
        fragmentSelectContinentDateRange.setArguments(bundle);
        fragmentSelectContinentDateRange.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectContinentDateRange,"SelectContinentDateRangeFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentEnterDateRange(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_enter_date_range_report fragment_enter_date_range_report=new fragment_enter_date_range_report();
        fragment_enter_date_range_report.setArguments(bundle);
        fragment_enter_date_range_report.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment_enter_date_range_report,"EnterDateRangeFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentPharmacyDemographicContinentCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPharmacyDemographicContinentCount fragmentPharmacyDemographicContinentCount=new FragmentPharmacyDemographicContinentCount();
        fragmentPharmacyDemographicContinentCount.setArguments(bundle);
        fragmentPharmacyDemographicContinentCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPharmacyDemographicContinentCount,"PharmacyDemographicContinentCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentPharmacyDemographicCountryCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPharmacyDemographicCountryCount fragmentPharmacyDemographicCountryCount=new FragmentPharmacyDemographicCountryCount();
        fragmentPharmacyDemographicCountryCount.setArguments(bundle);
        fragmentPharmacyDemographicCountryCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPharmacyDemographicCountryCount,"PharmacyDemographicCountryCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentHospitalDemographicCountryRegionCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalDemographicCountryRegionCount fragmentHospitalDemographicCountryRegionCount=new FragmentHospitalDemographicCountryRegionCount();
        fragmentHospitalDemographicCountryRegionCount.setArguments(bundle);
        fragmentHospitalDemographicCountryRegionCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalDemographicCountryRegionCount,"HospitalDemographicCountryRegionCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentHospitalDemographicContinentCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalDemographicCountContinent fragmentHospitalDemographicCountContinent=new FragmentHospitalDemographicCountContinent();
        fragmentHospitalDemographicCountContinent.setArguments(bundle);
        fragmentHospitalDemographicCountContinent.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalDemographicCountContinent,"HospitalDemographicContinentCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentHospitalDemographicCountryCount(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalDemographicCountryCount fragmentHospitalDemographicCountryCount=new FragmentHospitalDemographicCountryCount();
        fragmentHospitalDemographicCountryCount.setArguments(bundle);
        fragmentHospitalDemographicCountryCount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalDemographicCountryCount,"HospitalDemographicCountryCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentHospitalReportsMenu(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalReportsMenu fragmentHospitalReportsMenu=new FragmentHospitalReportsMenu();
        fragmentHospitalReportsMenu.setArguments(bundle);
        fragmentHospitalReportsMenu.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalReportsMenu,"HospitalReportsMenuFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentGroupCategoryCountReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_group_category_count_report fragment_group_category_count_report=new fragment_group_category_count_report();
        fragment_group_category_count_report.setArguments(bundle);
        fragment_group_category_count_report.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment_group_category_count_report,"GroupCategoryCountFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentFragmentProcessSelectedPointsSouthAfricaPurchases(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentProcessSelectedPointsSouthAfricaPurchases fragmentProcessSelectedPointsSouthAfricaPurchases=new FragmentProcessSelectedPointsSouthAfricaPurchases();
        fragmentProcessSelectedPointsSouthAfricaPurchases.setArguments(bundle);
        fragmentProcessSelectedPointsSouthAfricaPurchases.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentProcessSelectedPointsSouthAfricaPurchases,"ProcessSelectedPointsSAFrag");

        fragmentTransaction.commit();

    }

    private void addFragmentPurchaseSouthAfricaAllMembers(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPurchaseSouthAfricaAllMembers fragmentPurchaseSouthAfricaAllMembers=new FragmentPurchaseSouthAfricaAllMembers();
        fragmentPurchaseSouthAfricaAllMembers.setArguments(bundle);
        fragmentPurchaseSouthAfricaAllMembers.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPurchaseSouthAfricaAllMembers,"PurchaseSAAllMembersFrag");

        fragmentTransaction.commit();

    }

    private void addPurchasePointsSouthAfricaFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        PurchasePointsSouthAfricaFragment purchasePointsSouthAfricaFragment=new PurchasePointsSouthAfricaFragment();
        purchasePointsSouthAfricaFragment.setArguments(bundle);
        purchasePointsSouthAfricaFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,purchasePointsSouthAfricaFragment,"PurchasePointsSAFrag");

        fragmentTransaction.commit();

    }

    private void addMainFragment(){

        fragmentTransaction=fragmentManager.beginTransaction();

        MainFragment mainFragment=new MainFragment();
        mainFragment.setFragmentActionListener(this);

        fragmentTransaction.add(R.id.fragmentContainer, mainFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addMainFragment_FromRegistration(){

        fragmentTransaction=fragmentManager.beginTransaction();

        MainFragment mainFragment=new MainFragment();
        mainFragment.setFragmentActionListener(this);

        fragmentTransaction.replace(R.id.fragmentContainer, mainFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addMainFragment_FromNoInternetCoonection(){

        fragmentTransaction=fragmentManager.beginTransaction();

        MainFragment mainFragment=new MainFragment();
        mainFragment.setFragmentActionListener(this);

        fragmentTransaction.replace(R.id.fragmentContainer, mainFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }


    private void addContactMenuFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentContactMenu fragmentContactMenu=new FragmentContactMenu();
        fragmentContactMenu.setArguments(bundle);
        fragmentContactMenu.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentContactMenu,"ContactMenuFrag");

        fragmentTransaction.commit();
    }


    private void addResetPasswordStatementFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentResetPasswordStatement fragmentResetPasswordStatement=new FragmentResetPasswordStatement();
        fragmentResetPasswordStatement.setArguments(bundle);
        fragmentResetPasswordStatement.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentResetPasswordStatement,"ResetPasswordStatementFrag");

        fragmentTransaction.commit();
    }

    private void addSubmitPhoneNumberFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSubmitPhoneNumber fragmentSubmitPhoneNumber=new FragmentSubmitPhoneNumber();
        fragmentSubmitPhoneNumber.setArguments(bundle);
        fragmentSubmitPhoneNumber.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSubmitPhoneNumber,"SubmitPhoneNumberFrag");

        fragmentTransaction.commit();

    }

    private void addActivateClientAccountFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateClientAccount fragmentActivateClientAccount=new FragmentActivateClientAccount();
        fragmentActivateClientAccount.setArguments(bundle);
        fragmentActivateClientAccount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateClientAccount,"ActivateClientAccountFrag");

        fragmentTransaction.commit();


    }


    private void addDeactivateClientAccountFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeactivateClientAccount fragmentDeactivateClientAccount=new FragmentDeactivateClientAccount();
        fragmentDeactivateClientAccount.setArguments(bundle);
        fragmentDeactivateClientAccount.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeactivateClientAccount,"DeactivateClientAccountFrag");

        fragmentTransaction.commit();

    }

    private void addActivateResetUserPasswordFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateResetUserPassword fragmentActivateResetUserPassword=new FragmentActivateResetUserPassword();
        fragmentActivateResetUserPassword.setArguments(bundle);
        fragmentActivateResetUserPassword.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateResetUserPassword,"ActivateResetUserPasswordFrag");

        fragmentTransaction.commit();

    }

    private void addActivateResetMerchantUserPasswordFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateResetMerchantUserPassword fragmentActivateResetMerchantUserPassword=new FragmentActivateResetMerchantUserPassword();
        fragmentActivateResetMerchantUserPassword.setArguments(bundle);
        fragmentActivateResetMerchantUserPassword.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateResetMerchantUserPassword,"ActivateResetMerchantUserPasswordFrag");

        fragmentTransaction.commit();

    }

    private void addActivateResetMerchantAccountPassword(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateResetMerchantAccountPassword fragmentActivateResetMerchantAccountPassword=new FragmentActivateResetMerchantAccountPassword();
        fragmentActivateResetMerchantAccountPassword.setArguments(bundle);
        fragmentActivateResetMerchantAccountPassword.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateResetMerchantAccountPassword,"ActivateResetMerchantAccountPasswordFrag");

        fragmentTransaction.commit();

    }

    private void addActivateResetClientPasswordFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateResetClientPassword fragmentActivateResetClientPassword=new FragmentActivateResetClientPassword();
        fragmentActivateResetClientPassword.setArguments(bundle);
        fragmentActivateResetClientPassword.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateResetClientPassword,"ActivateResetClientPasswordFrag");

        fragmentTransaction.commit();

    }

    private void addClientSystemMaintenanceFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentClientSystemMaintenance fragmentClientSystemMaintenance=new FragmentClientSystemMaintenance();
        fragmentClientSystemMaintenance.setArguments(bundle);
        fragmentClientSystemMaintenance.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentClientSystemMaintenance,"ClientSystemMaintenanceFrag");

        fragmentTransaction.commit();

    }

    private void addResetPasswordFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentResetPassword fragmentResetPassword=new FragmentResetPassword();
        fragmentResetPassword.setArguments(bundle);
        fragmentResetPassword.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentResetPassword,"ResetPasswordFrag");

        fragmentTransaction.commit();

    }

    private void addInternationalPurchaseAllMembersFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentInternationalPurchaseAllMembers fragmentInternationalPurchaseAllMembers=new FragmentInternationalPurchaseAllMembers();
        fragmentInternationalPurchaseAllMembers.setArguments(bundle);
        fragmentInternationalPurchaseAllMembers.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentInternationalPurchaseAllMembers,"InternationalPurchaseAllMembersFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addInternationalProcessSelectedPointsPurchasesFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentInternationalProcessSelectedPointsPurchases fragmentInternationalProcessSelectedPointsPurchases=new FragmentInternationalProcessSelectedPointsPurchases();
        fragmentInternationalProcessSelectedPointsPurchases.setArguments(bundle);
        fragmentInternationalProcessSelectedPointsPurchases.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentInternationalProcessSelectedPointsPurchases,"InternationalProcessSelectedPointsPurchasesFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addInternationalPurchasePointsFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        InternationalPurchasePointsFragment internationalPurchasePointsFragment=new InternationalPurchasePointsFragment();
        internationalPurchasePointsFragment.setArguments(bundle);
        internationalPurchasePointsFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,internationalPurchasePointsFragment,"InternationalPurchasePointsFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addCreateLivvuSystemUsersFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateLivvuSystemUsers fragmentCreateLivvuSystemUsers=new FragmentCreateLivvuSystemUsers();
        fragmentCreateLivvuSystemUsers.setFragmentActionListener(this);
        fragmentCreateLivvuSystemUsers.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateLivvuSystemUsers,"CreateLivvuSystemUsersFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addLivvuSystemMaintenanceFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuSystemMaintenance fragmentLivvuSystemMaintenance=new FragmentLivvuSystemMaintenance();
        fragmentLivvuSystemMaintenance.setFragmentActionListener(this);
        fragmentLivvuSystemMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuSystemMaintenance,"LivvuSystemMaintenanceFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addLivvuMainMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuMainMenu fragmentLivvuMainMenu=new FragmentLivvuMainMenu();
        fragmentLivvuMainMenu.setFragmentActionListener(this);
        fragmentLivvuMainMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuMainMenu,"LivvuMainMenuFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }


    private void addSendSmsCashOutReminderFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_send_sms_cash_out_reminder fragmentSendSmsCashOutReminder=new fragment_send_sms_cash_out_reminder();
        fragmentSendSmsCashOutReminder.setFragmentActionListener(this);
        fragmentSendSmsCashOutReminder.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSendSmsCashOutReminder,"SendSMSCashOutReminder");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addFragmentActivateMerchantUser(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateMerchantUser fragmentActivateMerchantUser=new FragmentActivateMerchantUser();
        fragmentActivateMerchantUser.setFragmentActionListener(this);
        fragmentActivateMerchantUser.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateMerchantUser,"ActivateMerchantUserFrag");
        fragmentTransaction.commit();
    }


    private void addFragmentActivateUser(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateUser fragmentActivateUser=new FragmentActivateUser();
        fragmentActivateUser.setFragmentActionListener(this);
        fragmentActivateUser.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateUser,"ActivateUserFrag");
        fragmentTransaction.commit();
    }


    private void addFragmentActivateMerchantAccount(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentActivateMerchantAccount fragmentActivateMerchantAccount=new FragmentActivateMerchantAccount();
        fragmentActivateMerchantAccount.setFragmentActionListener(this);
        fragmentActivateMerchantAccount.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentActivateMerchantAccount,"ActivateMerchantAccountFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentDeactivateMerchantAccount(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeactivateMerchantAccount fragmentDeactivateMerchantAccount=new FragmentDeactivateMerchantAccount();
        fragmentDeactivateMerchantAccount.setFragmentActionListener(this);
        fragmentDeactivateMerchantAccount.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeactivateMerchantAccount,"DeactivateMerchantAccountFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentLivvuMainAccountsMenu(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuMainAccountsMenu fragmentLivvuMainAccountsMenu=new FragmentLivvuMainAccountsMenu();
        fragmentLivvuMainAccountsMenu.setFragmentActionListener(this);
        fragmentLivvuMainAccountsMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuMainAccountsMenu,"LivvuMainAccountsMenu");
        fragmentTransaction.commit();
    }

    private void addFragmentMerchantSystemMaintenance(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantSystemMaintenance fragmentMerchantSystemMaintenance=new FragmentMerchantSystemMaintenance();
        fragmentMerchantSystemMaintenance.setFragmentActionListener(this);
        fragmentMerchantSystemMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantSystemMaintenance,"MerchantSystemMaintenanceFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentUserSystemMaintenance(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUserSystemMaintenance fragmentUserSystemMaintenance=new FragmentUserSystemMaintenance();
        fragmentUserSystemMaintenance.setFragmentActionListener(this);
        fragmentUserSystemMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUserSystemMaintenance,"UserSystemMaintenanceFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentEnterContinentDetails(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentEnterContinentDetails fragmentEnterContinentDetails=new FragmentEnterContinentDetails();
        fragmentEnterContinentDetails.setFragmentActionListener(this);
        fragmentEnterContinentDetails.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterContinentDetails,"EnterContinentDetailsFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentHospitalContinentReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalContinentReport fragmentHospitalContinentReport=new FragmentHospitalContinentReport();
        fragmentHospitalContinentReport.setFragmentActionListener(this);
        fragmentHospitalContinentReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalContinentReport,"HospitalContinentReportFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentHospitalCountryReport(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentHospitalCountryReport fragmentHospitalCountryReport=new FragmentHospitalCountryReport();
        fragmentHospitalCountryReport.setFragmentActionListener(this);
        fragmentHospitalCountryReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentHospitalCountryReport,"HospitalCountryReportFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentDemographicContinent(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDemographicContinent fragmentDemographicContinent=new FragmentDemographicContinent();
        fragmentDemographicContinent.setFragmentActionListener(this);
        fragmentDemographicContinent.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDemographicContinent,"DemographicContinentFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentSelectContinent(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectContinent fragmentSelectContinent=new FragmentSelectContinent();
        fragmentSelectContinent.setFragmentActionListener(this);
        fragmentSelectContinent.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectContinent,"SelectContinentFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentResponseLogoutSession(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();
        ResponseLogoutSessionFragment responseLogoutSessionFragment = new ResponseLogoutSessionFragment();
        responseLogoutSessionFragment.setFragmentActionListener(this);
        responseLogoutSessionFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,responseLogoutSessionFragment,"ResponseLogoutSessionFrag");
        fragmentTransaction.commit();
    }


    private void addFragmentEnterAmountSociety(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();
        FragmentEnterAmountSociety fragmentEnterAmountSociety = new FragmentEnterAmountSociety();
        fragmentEnterAmountSociety.setFragmentActionListener(this);
        fragmentEnterAmountSociety.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterAmountSociety,"EnterAmountSocietyFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentEnterAmountIndividual(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();
        FragmentEnterAmountIndividual fragmentEnterAmountIndividual = new FragmentEnterAmountIndividual();
        fragmentEnterAmountIndividual.setFragmentActionListener(this);
        fragmentEnterAmountIndividual.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterAmountIndividual,"EnterAmountIndividualFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentDeactivateMerchantUser(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        FragmentDeactivateMerchantUser fragmentDeactivateMerchantUser = new FragmentDeactivateMerchantUser();
        fragmentDeactivateMerchantUser.setFragmentActionListener(this);
        fragmentDeactivateMerchantUser.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeactivateMerchantUser,"DeactivateMerchantUserFrag");
        fragmentTransaction.commit();
    }


    private void addFragmentMerchantUserMaintenance(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantUserMaintenance fragmentMerchantUserMaintenance=new FragmentMerchantUserMaintenance();
        fragmentMerchantUserMaintenance.setFragmentActionListener(this);
        fragmentMerchantUserMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantUserMaintenance,"MerchantUserMaintFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentNoDataMerchantInternet(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_no_data_merchant_internet_connection fragment_no_data_merchant_internet_connection=new fragment_no_data_merchant_internet_connection();
        fragment_no_data_merchant_internet_connection.setFragmentActionListener(this);
        fragment_no_data_merchant_internet_connection.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragment_no_data_merchant_internet_connection,"NoDataMerchantFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentNoDataCompanyInternet(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_no_data_company_internet_connection fragment_no_data_company_internet_connection=new fragment_no_data_company_internet_connection();
        fragment_no_data_company_internet_connection.setFragmentActionListener(this);
        fragment_no_data_company_internet_connection.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragment_no_data_company_internet_connection,"NoDataCompanyFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentEnterMobileOperator(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentEnterMobileOperator fragmentEnterMobileOperator=new FragmentEnterMobileOperator();
        fragmentEnterMobileOperator.setFragmentActionListener(this);
        fragmentEnterMobileOperator.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterMobileOperator,"EnterMobileOperatorFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCreateTypeOfInstitution(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateTypeOfInstitution fragmentCreateTypeOfInstitution=new FragmentCreateTypeOfInstitution();
        fragmentCreateTypeOfInstitution.setFragmentActionListener(this);
        fragmentCreateTypeOfInstitution.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateTypeOfInstitution,"CreateTypeInstitutionFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentSystemMaintenance(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSystemMaintenance fragmentSystemMaintenance=new FragmentSystemMaintenance();
        fragmentSystemMaintenance.setFragmentActionListener(this);
        fragmentSystemMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSystemMaintenance,"SystemMaintenanceFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentSelectCountry(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectCountry fragmentSelectCountry=new FragmentSelectCountry();
        fragmentSelectCountry.setFragmentActionListener(this);
        fragmentSelectCountry.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectCountry,"SelectCountryFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentEnterCountryDetails(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentEnterCountryDetails fragmentEnterCountryDetails=new FragmentEnterCountryDetails();
        fragmentEnterCountryDetails.setFragmentActionListener(this);
        fragmentEnterCountryDetails.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterCountryDetails,"EnterCountryDetailsFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentUpdateCurrencyRate(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUpdateCurrencyRate fragmentUpdateCurrencyRate=new FragmentUpdateCurrencyRate();
        fragmentUpdateCurrencyRate.setFragmentActionListener(this);
        fragmentUpdateCurrencyRate.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUpdateCurrencyRate,"UpdateCurrencyRateFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCreateCurrencyRate(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateCurrencyRate fragmentCreateCurrencyRate=new FragmentCreateCurrencyRate();
        fragmentCreateCurrencyRate.setFragmentActionListener(this);
        fragmentCreateCurrencyRate.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateCurrencyRate,"CreateCurrencyRateFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCurrencyRatesMaintenance(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCurrencyRatesMaintenance fragmentCurrencyRatesMaintenance=new FragmentCurrencyRatesMaintenance();
        fragmentCurrencyRatesMaintenance.setFragmentActionListener(this);
        fragmentCurrencyRatesMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCurrencyRatesMaintenance,"CurrencyRatesMaintenanceFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentDeleteAncillaryTariff(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeleteAncillaryTariff fragmentDeleteAncillaryTariff=new FragmentDeleteAncillaryTariff();
        fragmentDeleteAncillaryTariff.setFragmentActionListener(this);
        fragmentDeleteAncillaryTariff.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeleteAncillaryTariff,"DeleteAncilliaryTariffFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentUpdateAncillaryTariff(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUpdateAncillaryTariff fragmentUpdateAncillaryTariff=new FragmentUpdateAncillaryTariff();
        fragmentUpdateAncillaryTariff.setFragmentActionListener(this);
        fragmentUpdateAncillaryTariff.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUpdateAncillaryTariff,"UpdateAncilliaryTariffFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentLivvuResponse(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuResponse fragmentLivvuResponse=new FragmentLivvuResponse();
        fragmentLivvuResponse.setFragmentActionListener(this);
        fragmentLivvuResponse.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuResponse,"livvuResponseFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentUpdateDeleteAncilliaryTariff(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentUpdateDeleteAncillaryTariff fragmentUpdateDeleteAncillaryTariff=new FragmentUpdateDeleteAncillaryTariff();
        fragmentUpdateDeleteAncillaryTariff.setFragmentActionListener(this);
        fragmentUpdateDeleteAncillaryTariff.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentUpdateDeleteAncillaryTariff,"UpdateDeleteAncilliaryTariffFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentCreateAncilliaryTariff(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateAncillaryTariff fragmentCreateAncilliaryTariff=new FragmentCreateAncillaryTariff();
        fragmentCreateAncilliaryTariff.setFragmentActionListener(this);
        fragmentCreateAncilliaryTariff.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateAncilliaryTariff,"CreateAncilliaryTariffFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentAncilliaryTariffMaintenance(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentAncilliaryTariffMaintenance fragmentAncilliaryTariffMaintenance=new FragmentAncilliaryTariffMaintenance();
        fragmentAncilliaryTariffMaintenance.setFragmentActionListener(this);
        fragmentAncilliaryTariffMaintenance.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentAncilliaryTariffMaintenance,"AncilliaryTariffMaintenanceFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentFragmentProcessSelectedPointsPurchases(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentProcessSelectedPointsPurchases fragmentProcessSelectedPointsPurchases=new FragmentProcessSelectedPointsPurchases();
        fragmentProcessSelectedPointsPurchases.setFragmentActionListener(this);
        fragmentProcessSelectedPointsPurchases.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentProcessSelectedPointsPurchases,"ProcessSelectedPointsFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentMemberPointsPurchasesReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMemberPointsPurchasesReport fragmentMemberPointsPurchasesReport=new FragmentMemberPointsPurchasesReport();
        fragmentMemberPointsPurchasesReport.setFragmentActionListener(this);
        fragmentMemberPointsPurchasesReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMemberPointsPurchasesReport,"MemberPointsPurchasesReportFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentSocietyCompanyPointsPurchasesReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyPointsPurchasesReport fragmentSocietyCompanyPointsPurchasesReport=new FragmentSocietyCompanyPointsPurchasesReport();
        fragmentSocietyCompanyPointsPurchasesReport.setFragmentActionListener(this);
        fragmentSocietyCompanyPointsPurchasesReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyPointsPurchasesReport,"SocietyCompanyPointsPurchasesFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentSocietyCompanyBillPaymentsReport(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyBillPaymentsReport fragmentSocietyCompanyBillPaymentsReport=new FragmentSocietyCompanyBillPaymentsReport();
        fragmentSocietyCompanyBillPaymentsReport.setFragmentActionListener(this);
        fragmentSocietyCompanyBillPaymentsReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyBillPaymentsReport,"SocietyCompanyBillPaymentsFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentSocietyCompanyBillRecords(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyBillRecords fragmentSocietyCompanyBillRecords=new FragmentSocietyCompanyBillRecords();
        fragmentSocietyCompanyBillRecords.setFragmentActionListener(this);
        fragmentSocietyCompanyBillRecords.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyBillRecords,"SocietyCompanyBillRecordsFrag");
        fragmentTransaction.commit();


    }

    private void addFragmentSocietyCompanyMemberStatus(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyMemberStatus fragmentSocietyCompanyMemberStatus=new FragmentSocietyCompanyMemberStatus();
        fragmentSocietyCompanyMemberStatus.setFragmentActionListener(this);
        fragmentSocietyCompanyMemberStatus.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyMemberStatus,"SocietyCompanyMemberStatusFrag");
        fragmentTransaction.commit();
    }

    private void addFragmentSocietyCompanyReportsMenu(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyReportsMenu fragmentSocietyCompanyReportsMenu=new FragmentSocietyCompanyReportsMenu();
        fragmentSocietyCompanyReportsMenu.setFragmentActionListener(this);
        fragmentSocietyCompanyReportsMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyReportsMenu,"SocietyCompanyReportsMenuFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentPurchaseSelectedMembers(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPurchaseSelectedMembers fragmentPurchaseSelectedMembers=new FragmentPurchaseSelectedMembers();
        fragmentPurchaseSelectedMembers.setFragmentActionListener(this);
        fragmentPurchaseSelectedMembers.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPurchaseSelectedMembers,"PurchaseSelectedMembersFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentPurchaseAllMembers(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPurchaseAllMembers fragmentPurchaseAllMembers=new FragmentPurchaseAllMembers();
        fragmentPurchaseAllMembers.setFragmentActionListener(this);
        fragmentPurchaseAllMembers.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPurchaseAllMembers,"PurchaseAllMembersFrag");
        fragmentTransaction.commit();

    }

    private void addFragmentSocietyCompanyPurchasePoints(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyPurchasePoints fragmentSocietyCompanyPurchasePoints=new FragmentSocietyCompanyPurchasePoints();
        fragmentSocietyCompanyPurchasePoints.setFragmentActionListener(this);
        fragmentSocietyCompanyPurchasePoints.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyPurchasePoints,"SocietyCompanyPurchPointsFrag");
        fragmentTransaction.commit();

    }

    private void addRemoveSocietyCompanyFrag(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentRemoveMembersFromCompany fragmentRemoveMembersFromCompany=new FragmentRemoveMembersFromCompany();
        fragmentRemoveMembersFromCompany.setFragmentActionListener(this);
        fragmentRemoveMembersFromCompany.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentRemoveMembersFromCompany,"RemoveMembersFrag");
        fragmentTransaction.commit();


    }

    private void addResponseSocietyCompanyFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        ResponseSocietyCompanyFragment responseSocietyCompanyFragment=new ResponseSocietyCompanyFragment();
        responseSocietyCompanyFragment.setFragmentActionListener(this);
        responseSocietyCompanyFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,responseSocietyCompanyFragment,"ResponseSocietyCompanyFrag");
        fragmentTransaction.commit();
    }

    private void addEnterMembersFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentEnterMembers fragmentEnterMembers=new FragmentEnterMembers();
        fragmentEnterMembers.setFragmentActionListener(this);
        fragmentEnterMembers.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEnterMembers,"EnterMembersFrag");
        fragmentTransaction.commit();
    }

    private void addSocietyCompanyMainMenuFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSocietyCompanyMainMenu fragmentSocietyCompanyMainMenu=new FragmentSocietyCompanyMainMenu();
        fragmentSocietyCompanyMainMenu.setFragmentActionListener(this);
        fragmentSocietyCompanyMainMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSocietyCompanyMainMenu,"SocietyCompanyMainMenuFrag");
        fragmentTransaction.commit();

    }

    private void addBillingStatusResponseFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentBillingStatusResponse fragmentBillingStatusResponse=new FragmentBillingStatusResponse();
        fragmentBillingStatusResponse.setFragmentActionListener(this);
        fragmentBillingStatusResponse.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentBillingStatusResponse,"FragmentBillingResponseFrag");
        fragmentTransaction.commit();

    }

    private void addPatientMedicalStatusFrag(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalStatus fragmentPatientMedicalStatus=new FragmentPatientMedicalStatus();
        fragmentPatientMedicalStatus.setFragmentActionListener(this);
        fragmentPatientMedicalStatus.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalStatus,"PatientMedicalStatusFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addPatientMedicalBillsFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalBills fragmentPatientMedicalBills=new FragmentPatientMedicalBills();
        fragmentPatientMedicalBills.setFragmentActionListener(this);
        fragmentPatientMedicalBills.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalBills,"PatientMedicalBillsFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addPatientMedicalSummaryFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalSummary fragmentPatientMedicalSummary=new FragmentPatientMedicalSummary();
        fragmentPatientMedicalSummary.setFragmentActionListener(this);
        fragmentPatientMedicalSummary.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalSummary,"PatientMedicalSummaryFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addPatientMedicalStatementFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalStatement fragmentPatientMedicalStatement=new FragmentPatientMedicalStatement();
        fragmentPatientMedicalStatement.setFragmentActionListener(this);
        fragmentPatientMedicalStatement.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalStatement,"PatientMedicalStatementFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addPatientMedicalReportsFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentPatientMedicalReports fragmentPatientMedicalReports=new FragmentPatientMedicalReports();
        fragmentPatientMedicalReports.setFragmentActionListener(this);
        fragmentPatientMedicalReports.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPatientMedicalReports,"PatientMedicalReportFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addMerchantAdminReportMenuFrag(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantAdminReportsMenu fragmentMerchantAdminReportsMenu=new FragmentMerchantAdminReportsMenu();
        fragmentMerchantAdminReportsMenu.setFragmentActionListener(this);
        fragmentMerchantAdminReportsMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantAdminReportsMenu,"MerchantAdminReportFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addBrainTreeCardFormFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentBrainTreeCardForm fragmentBrainTreeCardForm=new FragmentBrainTreeCardForm();
        fragmentBrainTreeCardForm.setFragmentActionListener(this);
        fragmentBrainTreeCardForm.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentBrainTreeCardForm,"BrainTreeCardFormFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addMerchantAdminMainMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantAdminMainMenu fragmentMerchantAdminMainMenu=new FragmentMerchantAdminMainMenu();
        fragmentMerchantAdminMainMenu.setFragmentActionListener(this);
        fragmentMerchantAdminMainMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantAdminMainMenu,"MerchantAdminMenuFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addMerchantAccountsMainMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentMerchantAccountsMainMenu fragmentMerchantAccountsMainMenu=new FragmentMerchantAccountsMainMenu();
        fragmentMerchantAccountsMainMenu.setFragmentActionListener(this);
        fragmentMerchantAccountsMainMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentMerchantAccountsMainMenu,"MerchantMainMenuFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addCreateMerchantUsersFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateMerchantUserAccounts fragmentCreateMerchantUserAccounts=new FragmentCreateMerchantUserAccounts();
        fragmentCreateMerchantUserAccounts.setFragmentActionListener(this);
        fragmentCreateMerchantUserAccounts.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateMerchantUserAccounts,"CreateMerchantUserFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addDeactivateUserFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentDeactivateUser fragmentDeactivateUser=new FragmentDeactivateUser();
        fragmentDeactivateUser.setFragmentActionListener(this);
        fragmentDeactivateUser.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDeactivateUser,"DeactivateUserFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addProcessMobilePurchasesFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentProcessMobilePurchases fragmentProcessMobilePurchases=new FragmentProcessMobilePurchases();
        fragmentProcessMobilePurchases.setFragmentActionListener(this);
        fragmentProcessMobilePurchases.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentProcessMobilePurchases,"OperatorPurchasesFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addSelectMobileOperatorFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentSelectMobileOperator fragmentSelectMobileOperator=new FragmentSelectMobileOperator();
        fragmentSelectMobileOperator.setFragmentActionListener(this);
        fragmentSelectMobileOperator.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSelectMobileOperator,"SelectMobileOperatorFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addMobilePurchasesFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentAirtimePurchases fragmentAirtimePurchases=new FragmentAirtimePurchases();
        fragmentAirtimePurchases.setFragmentActionListener(this);
        fragmentAirtimePurchases.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentAirtimePurchases,"MobilePurchasesFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addCompleteUserRegistrationFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCompleteUserRegistration fragmentCompleteUserRegistration=new FragmentCompleteUserRegistration();
        fragmentCompleteUserRegistration.setFragmentActionListener(this);
        fragmentCompleteUserRegistration.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCompleteUserRegistration,"CompleteUserRegFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addCreateSystemUsersFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCreateSystemUsers fragmentCreateSystemUsers=new FragmentCreateSystemUsers();
        fragmentCreateSystemUsers.setFragmentActionListener(this);
        fragmentCreateSystemUsers.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCreateSystemUsers,"CreateSystemUsersFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addProcessCashedOutPointsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentProcessCashedOutPoints fragmentProcessCashedOutPoints=new FragmentProcessCashedOutPoints();
        fragmentProcessCashedOutPoints.setFragmentActionListener(this);
        fragmentProcessCashedOutPoints.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentProcessCashedOutPoints,"ProcessCashedOutPointsFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addCashedOutPointsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentCashedOutPoints fragmentCashedOutPoints=new FragmentCashedOutPoints();
        fragmentCashedOutPoints.setFragmentActionListener(this);
        fragmentCashedOutPoints.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentCashedOutPoints,"CashedOutPointsFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addLivvuReportsMenuFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuReportsMenu fragmentLivvuReportsMenu=new FragmentLivvuReportsMenu();
        fragmentLivvuReportsMenu.setFragmentActionListener(this);
        fragmentLivvuReportsMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuReportsMenu,"livvuReportsMenuFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addProcessPaymentsFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentProcessPayments fragmentProcessPayments=new FragmentProcessPayments();
        fragmentProcessPayments.setFragmentActionListener(this);
        fragmentProcessPayments.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentProcessPayments);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addLivvuMainAdminMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        FragmentLivvuMainAdminMenu fragmentLivvuMainAdminMenu=new FragmentLivvuMainAdminMenu();
        fragmentLivvuMainAdminMenu.setFragmentActionListener(this);
        fragmentLivvuMainAdminMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentLivvuMainAdminMenu,"LivvuMainAdminMenuFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addPaymentStatementFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_payment_statement fragmentPaymentStatement=new fragment_payment_statement();
        fragmentPaymentStatement.setFragmentActionListener(this);
        fragmentPaymentStatement.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPaymentStatement);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addPaymentDetailsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_payment_details_report fragmentPaymentDetailsReport=new fragment_payment_details_report();
        fragmentPaymentDetailsReport.setFragmentActionListener(this);
        fragmentPaymentDetailsReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentPaymentDetailsReport);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addDemographicFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_demographic_report fragmentDemographicReport=new fragment_demographic_report();
        fragmentDemographicReport.setFragmentActionListener(this);
        fragmentDemographicReport.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentDemographicReport,"DemographicCountryFrag");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addEmailFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_email_form fragmentEmailForm=new fragment_email_form();
        fragmentEmailForm.setFragmentActionListener(this);
        fragmentEmailForm.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentEmailForm);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addContactFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_contact_us fragmentContactUs=new fragment_contact_us();
        fragmentContactUs.setFragmentActionListener(this);
        fragmentContactUs.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentContactUs);
        //fragmentTransaction.addToBackStack(String.valueOf(true));
        fragmentTransaction.commit();

    }

    private void addReportsMainFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_reports_main_menu fragmentReportsMainMenu=new fragment_reports_main_menu();
        fragmentReportsMainMenu.setFragmentActionListener(this);
        fragmentReportsMainMenu.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentReportsMainMenu);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addBillMerchantStatementFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        BillMerchantStatementFragment billMerchantStatementFragment=new BillMerchantStatementFragment();
        billMerchantStatementFragment.setFragmentActionListener(this);
        billMerchantStatementFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,billMerchantStatementFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addSendSmsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_send_sms fragmentSendSms=new fragment_send_sms();
        fragmentSendSms.setFragmentActionListener(this);
        fragmentSendSms.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentSendSms);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addNoDataToDisplay(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_no_data_internet_connection fragmentNoDataInternetConnection=new fragment_no_data_internet_connection();
        fragmentNoDataInternetConnection.setFragmentActionListener(this);
        fragmentNoDataInternetConnection.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoDataInternetConnection);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }


    private void addNoInternetFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        fragment_no_internet_connection fragmentNoInternetConnection=new fragment_no_internet_connection();
        fragmentNoInternetConnection.setFragmentActionListener(this);
        fragmentNoInternetConnection.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,fragmentNoInternetConnection);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addCompanyMainMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        CompanyMainMenuFragment companyMainMenuFragment=new CompanyMainMenuFragment();
        companyMainMenuFragment.setFragmentActionListener(this);
        companyMainMenuFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,companyMainMenuFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addIndividualMainMenuFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        IndividualMainMenuFragment IndividualMainMenuFragment=new IndividualMainMenuFragment();
        IndividualMainMenuFragment.setFragmentActionListener(this);
        IndividualMainMenuFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,IndividualMainMenuFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addRegisterCompanyFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        RegisterCompanyFragment companyRegistrationFragment=new RegisterCompanyFragment();
        companyRegistrationFragment.setArguments(bundle);
        companyRegistrationFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,companyRegistrationFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addRegisterIndividualFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        RegisterIndividualFragment registerIndividualFragment=new RegisterIndividualFragment();

        registerIndividualFragment.setArguments(bundle);
        registerIndividualFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,registerIndividualFragment,"individualReg");
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addRegistrationTypeFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        RegistrationTypeFragment registrationTypeFragment=new RegistrationTypeFragment();
        registrationTypeFragment.setFragmentActionListener(this);
        registrationTypeFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,registrationTypeFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addConfirmCashOutFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        ConfirmCashOutFragment confirmCashOutFragment=new ConfirmCashOutFragment();
        confirmCashOutFragment.setArguments(bundle);
        confirmCashOutFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,confirmCashOutFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addCashOutDetailsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        CashOutDetailsFragment cashOutDetailsFragment=new CashOutDetailsFragment();
        cashOutDetailsFragment.setArguments(bundle);
        cashOutDetailsFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,cashOutDetailsFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addConfirmationCashOutFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        ConfirmationCashOutFragment confirmationCashOutFragment=new ConfirmationCashOutFragment();
        confirmationCashOutFragment.setArguments(bundle);
        confirmationCashOutFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,confirmationCashOutFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addPointsCashOutFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        PointsCashOutFragment pointsCashOutFragment=new PointsCashOutFragment();
        pointsCashOutFragment.setArguments(bundle);
        pointsCashOutFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,pointsCashOutFragment);
        ////fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addTermsConditionsSocietyFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        TermsConditionsSocietyFragment termsConditionsSocietyFragment=new TermsConditionsSocietyFragment();
        termsConditionsSocietyFragment.setArguments(bundle);
        termsConditionsSocietyFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,termsConditionsSocietyFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addTermsConditionsCompanyFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        TermsConditionsCompanyFragment termsConditionsCompanyFragment=new TermsConditionsCompanyFragment();
        termsConditionsCompanyFragment.setArguments(bundle);
        termsConditionsCompanyFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,termsConditionsCompanyFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }


    private void addTermsConditionsHealthFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        TermsConditionsHealthFragment termsConditionsHealthFragment=new TermsConditionsHealthFragment();
        termsConditionsHealthFragment.setArguments(bundle);
        termsConditionsHealthFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,termsConditionsHealthFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addTermsConditionsIndividualFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        TermsConditionsIndividualFragment termsConditionsIndividualFragment=new TermsConditionsIndividualFragment();
        termsConditionsIndividualFragment.setArguments(bundle);
        termsConditionsIndividualFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,termsConditionsIndividualFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addPurchasePointsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        PurchasePointsFragment purchasePointsFragment=new PurchasePointsFragment();
        purchasePointsFragment.setArguments(bundle);
        purchasePointsFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,purchasePointsFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addClientStatementFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        ClientStatementFragment clientStatementFragment=new ClientStatementFragment();
        clientStatementFragment.setArguments(bundle);
        clientStatementFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,clientStatementFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addBillMerchantDetailsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        BillMerchantDetailsFragment billMerchantDetailsFragment=new BillMerchantDetailsFragment();
        billMerchantDetailsFragment.setArguments(bundle);
        billMerchantDetailsFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,billMerchantDetailsFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addSocietyGroupRegistrationFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        SocietyGroupRegistrationFragment societyGroupRegistrationFragment=new SocietyGroupRegistrationFragment();
        societyGroupRegistrationFragment.setArguments(bundle);
        societyGroupRegistrationFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,societyGroupRegistrationFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addPrivateCompanyRegistrationFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        PrivateCompanyRegistrationFragment privateCompanyRegistrationFragment=new PrivateCompanyRegistrationFragment();
        privateCompanyRegistrationFragment.setArguments(bundle);
        privateCompanyRegistrationFragment.setFragmentActionListener(this);
        fragmentTransaction.replace(R.id.fragmentContainer,privateCompanyRegistrationFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addBillStatementFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        BillStatementFragment billStatementFragment=new BillStatementFragment();
        billStatementFragment.setFragmentActionListener(this);
        billStatementFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,billStatementFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addNoOfPointsFragmentMerchant(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        NoOfPointsFragmentMerchant noOfPointsFragmentMerchant=new NoOfPointsFragmentMerchant();
        noOfPointsFragmentMerchant.setFragmentActionListener(this);
        noOfPointsFragmentMerchant.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,noOfPointsFragmentMerchant);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addConfirmationPayBillFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        ConfirmationPayBillFragment confirmationPayBillFragment=new ConfirmationPayBillFragment();
        confirmationPayBillFragment.setFragmentActionListener(this);
        confirmationPayBillFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,confirmationPayBillFragment,"ConfirmationPayBillFrag");
        fragmentTransaction.commit();
    }

    private void addBillClientDetailsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();
        BillClientDetailsFragment billClientDetailsFragment=new BillClientDetailsFragment();
        billClientDetailsFragment.setFragmentActionListener(this);
        billClientDetailsFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,billClientDetailsFragment,"BillClientDetailsFrag");
        fragmentTransaction.commit();

    }

    private void addResponseBillFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();
        ResponseBillFragment responseBillFragment=new ResponseBillFragment();
        responseBillFragment.setFragmentActionListener(this);
        responseBillFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,responseBillFragment,"RespBillFrag");
        fragmentTransaction.commit();
    }

    private void addResponseFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();
        ResponseFragment responseFragment=new ResponseFragment();
        responseFragment.setFragmentActionListener(this);
        responseFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,responseFragment,"ResponseFrag");
        fragmentTransaction.commit();

    }

    private void addCreateBillFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        CreateBillFragment createBillFragment=new CreateBillFragment();
        createBillFragment.setFragmentActionListener(this);
        createBillFragment.setArguments(bundle);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.replace(R.id.fragmentContainer,createBillFragment,"CreateBillFrag");
        fragmentTransaction.commit();

    }

    private void addConfirmationFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        ConfirmationFragment confirmationFragment=new ConfirmationFragment();
        confirmationFragment.setFragmentActionListener(this);
        confirmationFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,confirmationFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addNoOfPointsFragment(Bundle bundle) {
        fragmentTransaction=fragmentManager.beginTransaction();

        NoOfPointsFragment noOfPointsFragment=new NoOfPointsFragment();
        noOfPointsFragment.setFragmentActionListener(this);
        noOfPointsFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,noOfPointsFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }

    private void addPhoneNoFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        PhoneNoFragment phoneNoFragment=new PhoneNoFragment();
        phoneNoFragment.setFragmentActionListener(this);
        phoneNoFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,phoneNoFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }

    private void addBankingDetailsFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        BankingDetailsFragment bankingDetailsFragment=new BankingDetailsFragment();
        bankingDetailsFragment.setFragmentActionListener(this);
        bankingDetailsFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,bankingDetailsFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();


    }

    private void addBankingServicesFragment(Bundle bundle) {

        fragmentTransaction=fragmentManager.beginTransaction();

        BankingServicesFragment bankingServicesFragment=new BankingServicesFragment();
        bankingServicesFragment.setFragmentActionListener(this);
        bankingServicesFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,bankingServicesFragment);
        //fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted)
                        Log.e(TAG, " Permission Granted, Now you can send sms");
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                                showMessageOKCancel("You need to allow permissions to send sms",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{SEND_SMS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}