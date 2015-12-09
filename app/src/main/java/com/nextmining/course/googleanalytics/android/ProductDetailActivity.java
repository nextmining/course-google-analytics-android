package com.nextmining.course.googleanalytics.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.yglab.googleanalyticscourse.android.R;

import java.util.Date;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";

    private ProductContent.ProductItem mItem;

    private Date startTime;

    /** OrderButtonClickListener */
    View.OnClickListener mOrderButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Payment button clicked!");

            Tracker tracker = ((MyApplication) getApplication()).getDefaultTracker();

            // Build and send an Event.
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("상품상세보기")
                    .setAction("주문하기클릭")
                    .setLabel(mItem.title)
                    .build());

            // Measuring actions for ecommerce tracking.
            Product product = new Product()
                    .setId(mItem.id)
                    .setName(mItem.title)
                    .setCategory("데이터분석과정")
                    .setBrand("넥스트")
                    .setVariant(mItem.schedule)
                    .setPrice(mItem.price)
                    .setQuantity(1);

            // Measuring a checkout step #1(라벨: 주문하기).
            // Add the step number and additional info about the checkout to the action.
            ProductAction productActionCheckoutStep = new ProductAction(ProductAction.ACTION_CHECKOUT)
                    .setCheckoutStep(1);
            HitBuilders.ScreenViewBuilder builderCheckoutStep = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionCheckoutStep);

            // Action add to cart(장바구니 담기)
            ProductAction productActionAdd = new ProductAction(ProductAction.ACTION_ADD)
                    .setProductActionList("Product List");
            HitBuilders.ScreenViewBuilder builderAdd = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionAdd);

            tracker.setScreenName("ProductDetail");
            tracker.send(builderCheckoutStep.build());
            tracker.send(builderAdd.build());


            Intent intent = new Intent(ProductDetailActivity.this, PaymentMethodActivity.class);
            intent.putExtra("product", mItem);
            intent.putExtra("startTime", startTime);

            startActivityForResult(intent, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = new Date();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mItem = (ProductContent.ProductItem) getIntent().getSerializableExtra("product");

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewSchedule = (TextView) findViewById(R.id.textViewSchedule);
        TextView textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);

        textViewTitle.setText(mItem.title);
        textViewSchedule.setText(mItem.schedule);
        textViewPrice.setText(mItem.price + "원");
        textViewDesc.setText(mItem.desc);

        Button buttonOrder = (Button) findViewById(R.id.buttonOrder);
        buttonOrder.setOnClickListener(mOrderButtonClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "*** onResume");

        Tracker tracker = ((MyApplication) getApplication()).getDefaultTracker();
        tracker.setScreenName("ProductDetail");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
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
