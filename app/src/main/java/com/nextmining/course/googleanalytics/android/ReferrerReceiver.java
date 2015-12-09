package com.nextmining.course.googleanalytics.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReferrerReceiver extends BroadcastReceiver {

    private static final String TAG = "ReferrerReceiver";

    public static final String REFERRER = "REFERRER";

    public static final String UTM_CAMPAIGN = "utm_campaign";
    public static final String UTM_SOURCE = "utm_source";
    public static final String UTM_MEDIUM = "utm_medium";
    public static final String UTM_TERM = "utm_term";
    public static final String UTM_CONTENT = "utm_content";

    private final String[] sources = {
            UTM_CAMPAIGN, UTM_SOURCE, UTM_MEDIUM, UTM_TERM, UTM_CONTENT
    };

    public ReferrerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        String referrerString = extras.getString("referrer");

        Log.d(TAG, "*** referrer: " + referrerString);

        try {
            Map<String, String> getParams = getHashMapFromQuery(referrerString);

            SharedPreferences preferences = context
                    .getSharedPreferences(REFERRER, Context.MODE_PRIVATE);

            SharedPreferences.Editor preferencesEditor = preferences.edit();

            for (String sourceType : sources) {
                String source = getParams.get(sourceType);

                if (source != null) {
                    preferencesEditor.putString(sourceType, source);
                }
            }

            preferencesEditor.commit();
        } catch (UnsupportedEncodingException e) {
            Log.e("Referrer Error", e.getMessage());
        } finally {

            // Pass along to google
            CampaignTrackingReceiver receiver = new CampaignTrackingReceiver();
            receiver.onReceive(context, intent);
        }
    }

    public static Map<String, String> getHashMapFromQuery(String query)
            throws UnsupportedEncodingException {

        Map<String, String> queryPairs = new LinkedHashMap<String, String>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryPairs;
    }
}
