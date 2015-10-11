package com.yglab.googleanalyticscourse.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ProductListFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Tracking
        Tracker tracker = ((MyApplication) getApplication()).getDefaultTracker();

        // In this example, campaign information is set using
        // a url string with Google Analytics campaign parameters.
        // Note: This is for illustrative purposes. In most cases campaign
        //       information would come from an incoming Intent.
        /*
        String campaignData = "http://examplepetstore.com/index.html?" +
                "utm_source=email&utm_medium=email_marketing&utm_campaign=summersale" +
                "&utm_content=email_variation_1";
        */

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri campaignData = intent.getData();

        Log.d("MainActivity", "*** Campaign data == " + campaignData);

        // Campaign data sent with this hit.
        if (campaignData != null && !campaignData.toString().equals("")) {
            tracker.send(new HitBuilders.ScreenViewBuilder()
                            .setCampaignParamsFromUrl(campaignData.toString())
                            .build()
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        Tracker tracker = ((MyApplication) getApplication()).getDefaultTracker();
        tracker.setScreenName("Main");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        */
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = null;

        if (position == 0) {
            fragment = ProductListFragment.newInstance("", "");
        }
        else {
            fragment = PlaceholderFragment.newInstance(position + 1);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        /*
        String eventLabel = "";
        if (position == 0) {
            eventLabel = "교육상품리스트";
        }
        else if (position == 1) {
            eventLabel = "수강이력";
        }
        else if (position == 2) {
            eventLabel = "내프로필";
        }

        // Build and send an Event.
        Tracker tracker = ((MyApplication) getApplication()).getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("네비게이션메뉴")
                .setAction("메뉴선택")
                .setLabel(eventLabel)
                .build());
        */
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onFragmentInteraction(ProductContent.ProductItem item) {

        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("product", item);

        startActivityForResult(intent, 100);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
