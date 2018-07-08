package com.example.android.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mGoogglePayBtn;

    private PaymentsClient mPaymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogglePayBtn = (RelativeLayout) findViewById(R.id.googlepay_button);

        mPaymentsClient = PayUtils.createPaymentsClient(this);
        isReadyToPay();

        mGoogglePayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googlepay_button:
                intiatePayment();
                break;
            default:
                break;
        }
    }

    private void intiatePayment() {
        mGoogglePayBtn.setClickable(false);

        TransactionInfo transaction = PayUtils.createTransaction("499");
        PaymentDataRequest request = PayUtils.createPaymentDataRequest(transaction);
        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);

        AutoResolveHelper.resolveTask(futurePaymentData, this, 1);
    }

    private void isReadyToPay() {
        PayUtils.isReadyToPay(mPaymentsClient).addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            setGooglePayAvailable(result);
                        } catch (ApiException exception) {
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            mGoogglePayBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        Log.w("failed", String.format("Error code: %d", status));
                        break;
                }
                mGoogglePayBtn.setClickable(true);
                break;
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        PaymentMethodToken token = paymentData.getPaymentMethodToken();
        if (token != null) {
            if (token.getToken().equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway.")
                        .setPositiveButton("OK", null)
                        .create();
                alertDialog.show();
            }
        }
    }
}