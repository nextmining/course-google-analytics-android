package com.ygbae.googleanalyticscourse.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nextmining.mobileanalysiscourse.R;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    /**
     * Google Analytics tracker.
     */
    private Tracker mTracker;

    private EditText mEditTextUsername;
    private EditText mEditTextEmail;

    /** SignUpButtonClickListener */
    View.OnClickListener mSignUpButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Sign-up button clicked!");

            if (mEditTextUsername.getText().toString().equals("") ||
                    mEditTextEmail.getText().toString().equals("")) {
                Toast.makeText(
                        SignUpActivity.this,
                        "이름과 이메일을 입력해 주세요.",
                        Toast.LENGTH_SHORT).show();

                return;
            }


            String username = mEditTextUsername.getText().toString();
            String email = mEditTextEmail.getText().toString();
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(SignUpActivity.this);
            sp.edit().putString("username", username).apply();
            sp.edit().putString("email", email).apply();

            // Build and send an Event.
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("회원가입")
                    .setAction("가입완료")
                    .setLabel(username) /** 원래는 GA에 유저 개인정보(이름, 이메일, 주민번호, 모바일 디바이스 ID 등)를 남기면 안됨! */
                    .build());

            // In this example, campaign information is set using
            // a url string with Google Analytics campaign parameters.
            // Note: This is for illustrative purposes. In most cases campaign
            //       information would come from an incoming Intent.
            String campaignData = "http://examplepetstore.com/index.html?" +
                    "utm_source=email&utm_medium=email_marketing&utm_campaign=summersale" +
                    "&utm_content=email_variation_1";

            // Campaign data sent with this hit.
            mTracker.send(new HitBuilders.ScreenViewBuilder()
                            .setCampaignParamsFromUrl(campaignData)
                            .build()
            );

            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

            Toast.makeText(
                    SignUpActivity.this,
                    "환영합니다^^ 회원가입 처리가 완료 되었습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Obtain the Google Analytics shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(SignUpActivity.this);
        if (!sp.getString("email", "").equals("")) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mEditTextUsername = (EditText) findViewById(R.id.editTextUsername);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);

        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(mSignUpButtonClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "*** onResume");

        mTracker.setScreenName("SignUp");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
