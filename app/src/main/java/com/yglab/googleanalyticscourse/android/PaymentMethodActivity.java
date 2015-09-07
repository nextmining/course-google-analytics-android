package com.yglab.googleanalyticscourse.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.nextmining.mobileanalysiscourse.R;
import com.ygbae.googleanalyticscourse.android.R;

import java.util.Date;


public class PaymentMethodActivity extends AppCompatActivity {

    private static final String TAG = "PaymentMethodActivity";

    /**
     * Google Analytics tracker.
     */
    private Tracker mTracker;

    private ProductContent.ProductItem mItem;
    private RadioGroup mRadioGroupPayMethod;

    private Date startTime;
    private Date endTime;

    /** PayButtonClickListener */
    View.OnClickListener mPayButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Payment button clicked!");

            String payMethod = "";
            int checkedResId = mRadioGroupPayMethod.getCheckedRadioButtonId();
            if (checkedResId == R.id.radioButton1) {
                payMethod = "신용카드";
            }
            else if (checkedResId == R.id.radioButton2) {
                payMethod = "은행계좌이체";
            }
            if (checkedResId == R.id.radioButton3) {
                payMethod = "휴대폰결제";
            }

            Log.d(TAG, "*** payment method = " + payMethod);

            endTime = new Date();

            // 상품조회에서 결제하기까지 걸린 시간
            long diff = endTime.getTime() - startTime.getTime();
            long diffSeconds = diff / 1000;

            // Build and send an Event.
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("상품결제하기")
                    .setAction("결제하기클릭")
                    .setLabel(payMethod)
                    .setValue(diffSeconds)
                    .build());

            // Measuring actions for ecommerce tracking.
            Product product =  new Product()
                    .setId(mItem.id)
                    .setName(mItem.title)
                    .setCategory("데이터분석과정")
                    .setBrand("넥스트")
                    .setVariant(mItem.schedule)
                    .setPrice(mItem.price)
                    .setQuantity(1);
            // Measuring a checkout step #2.
            // Add the step number and additional info about the checkout to the action.
            ProductAction productActionCheckoutStep = new ProductAction(ProductAction.ACTION_CHECKOUT)
                    .setCheckoutStep(2)
                    .setCheckoutOptions(payMethod);
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionCheckoutStep);

            mTracker.setScreenName("PaymentMethod");
            mTracker.send(builder.build());

            Intent intent = new Intent(PaymentMethodActivity.this, PaymentResultActivity.class);
            intent.putExtra("product", mItem);

            startActivityForResult(intent, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // Obtain the Google Analytics shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mItem = (ProductContent.ProductItem) getIntent().getSerializableExtra("product");
        startTime = (Date) getIntent().getSerializableExtra("startTime");

        mRadioGroupPayMethod = (RadioGroup) findViewById(R.id.radiogroupPayMethod);

        Button buttonPay = (Button) findViewById(R.id.buttonPay);
        buttonPay.setOnClickListener(mPayButtonClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "*** onResume");

        mTracker.setScreenName("PaymentMethod");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "*** onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode);

        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = getIntent();
        setResult(resultCode, intent);

        if (requestCode == 100 && resultCode == 1) {
            finish();
        }
    }
}
