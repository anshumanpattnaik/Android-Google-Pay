package com.example.android.pay;

import android.util.Pair;

import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.List;

public class PayConstants {

    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;
    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

    public static final String GATEWAY_TOKENIZATION_NAME = "example";

    public static final List<Integer> SUPPORTED_NETWORKS = Arrays.asList(
            WalletConstants.CARD_NETWORK_AMEX,
            WalletConstants.CARD_NETWORK_DISCOVER,
            WalletConstants.CARD_NETWORK_VISA,
            WalletConstants.CARD_NETWORK_MASTERCARD
    );

    public static final List<Integer> SUPPORTED_METHODS = Arrays.asList(
            WalletConstants.PAYMENT_METHOD_CARD,
            WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
    );

    public static final String CURRENCY_CODE = "USD";

    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList(
            "US",
            "GB"
    );

    public static final List<Pair<String, String>> GATEWAY_TOKENIZATION_PARAMETERS = Arrays.asList(
            Pair.create("gatewayMerchantId", "exampleGatewayMerchantId")
    );
}