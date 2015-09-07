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
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.nextmining.mobileanalysiscourse.R;
import com.ygbae.googleanalyticscourse.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PaymentResultActivity extends AppCompatActivity {

    private static final String TAG = "PaymentResultActivity";

    /**
     * Google Analytics tracker.
     */
    private Tracker mTracker;

    private ProductContent.ProductItem mItem;

    /** OkButtonClickListener */
    View.OnClickListener mOkButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Measuring transactions for ecommerce tracking.
            Product product =  new Product()
                    .setId(mItem.id)
                    .setName(mItem.title)
                    .setCategory("데이터분석과정")
                    .setBrand("넥스트")
                    .setVariant(mItem.schedule)
                    .setPrice(mItem.price)
                    //.setCouponCode("APPARELSALE")
                    .setQuantity(1);
            ProductAction productActionTransaction = new ProductAction(ProductAction.ACTION_PURCHASE)
                    .setTransactionId(mItem.id + "_" + convertDateToString("yyyyMMddHHmmss", new Date()))
                    .setTransactionAffiliation("Google Store - Online")
                    .setTransactionRevenue(mItem.price)
                    .setTransactionTax(10)
                    .setTransactionShipping(50);
            //.setTransactionCouponCode("SUMMER2013");

            HitBuilders.ScreenViewBuilder builderTransaction = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionTransaction);

            // Measuring a checkout step #3.
            // Add the step number and additional info about the checkout to the action.
            ProductAction productActionCheckout = new ProductAction(ProductAction.ACTION_CHECKOUT)
                    .setCheckoutStep(3);

            HitBuilders.ScreenViewBuilder builderCheckoutStep = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionCheckout);

            mTracker.setScreenName("PaymentResult");
            //mTracker.set("&cu", "KRW");  // Set tracker currency to Korean Won.
            mTracker.send(builderTransaction.build());
            mTracker.send(builderCheckoutStep.build());


            Intent intent = getIntent();
            setResult(1, intent);

            finish();

            Toast.makeText(
                     PaymentResultActivity.this,
                    "주문이 완료되었습니다. 감사합니다.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        // Obtain the Google Analytics shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        mItem = (ProductContent.ProductItem) getIntent().getSerializableExtra("product");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(mOkButtonClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "*** onResume");

        mTracker.setScreenName("PaymentResult");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_result, menu);
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

    private static final String convertDateToString(String format, Date date) {
        SimpleDateFormat sdf = null;
        String returnValue = "";

        if (date == null) {
        } else {
            sdf = new SimpleDateFormat(format);
            returnValue = sdf.format(date);
        }

        return returnValue;
    }
}
