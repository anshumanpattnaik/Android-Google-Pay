package com.example.android.pay;

import android.app.Activity;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.ShippingAddressRequirements;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PayUtils {

    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(PayConstants.PAYMENTS_ENVIRONMENT)
                .build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    public static PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo) {
        PaymentMethodTokenizationParameters.Builder paramsBuilder =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                        .addParameter("gateway", PayConstants.GATEWAY_TOKENIZATION_NAME);
        for (Pair<String, String> param : PayConstants.GATEWAY_TOKENIZATION_PARAMETERS) {
            paramsBuilder.addParameter(param.first, param.second);
        }

        return createPaymentDataRequest(transactionInfo, paramsBuilder.build());
    }

    public static PaymentDataRequest createPaymentDataRequestDirect(TransactionInfo transactionInfo) {
        PaymentMethodTokenizationParameters params =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_DIRECT)
                        .addParameter("publicKey", PayConstants.DIRECT_TOKENIZATION_PUBLIC_KEY)
                        .build();

        return createPaymentDataRequest(transactionInfo, params);
    }

    private static PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo, PaymentMethodTokenizationParameters params) {
        PaymentDataRequest request =
                PaymentDataRequest.newBuilder()
                        .setPhoneNumberRequired(false)
                        .setEmailRequired(true)
                        .setShippingAddressRequired(true)

                        .setShippingAddressRequirements(
                                ShippingAddressRequirements.newBuilder()
                                        .addAllowedCountryCodes(PayConstants.SHIPPING_SUPPORTED_COUNTRIES)
                                        .build())

                        .setTransactionInfo(transactionInfo)
                        .addAllowedPaymentMethods(PayConstants.SUPPORTED_METHODS)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(PayConstants.SUPPORTED_NETWORKS)
                                        .setAllowPrepaidCards(true)
                                        .setBillingAddressRequired(true)
                                        .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                                        .build())
                        .setPaymentMethodTokenizationParameters(params)
                        .setUiRequired(true)
                        .build();

        return request;
    }

    public static Task<Boolean> isReadyToPay(PaymentsClient client) {
        IsReadyToPayRequest.Builder request = IsReadyToPayRequest.newBuilder();
        for (Integer allowedMethod : PayConstants.SUPPORTED_METHODS) {
            request.addAllowedPaymentMethod(allowedMethod);
        }
        return client.isReadyToPay(request.build());
    }

    public static TransactionInfo createTransaction(String price) {
        return TransactionInfo.newBuilder()
                .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                .setTotalPrice(price)
                .setCurrencyCode(PayConstants.CURRENCY_CODE)
                .build();
    }

    /*public static String microsToString(long micros) {
        return new BigDecimal(micros).divide(MICROS).setScale(2, RoundingMode.HALF_EVEN).toString();
    }*/
}