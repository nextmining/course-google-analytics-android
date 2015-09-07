package com.yglab.googleanalyticscourse.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ProductListFragment extends ListFragment {

    /**
     * Google Analytics tracker.
     */
    private Tracker mTracker;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the Google Analytics shared Tracker instance.
        MyApplication application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setListAdapter(new ProductListAdapter(getActivity(), ProductContent.ITEMS));

        // Measuring impressions for ecommerce tracking.
        int position = 1;
        for (ProductContent.ProductItem item : ProductContent.ITEMS) {
            Product product = new Product()
                    .setId(item.id)
                    .setName(item.title)
                    .setCategory("데이터분석과정")
                    .setBrand("넥스트")
                    .setVariant(item.schedule)
                    .setPosition(position);
            //.setCustomDimension(1, "Member");
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addImpression(product, "Product List");

            mTracker.setScreenName("ProductList");
            mTracker.send(builder.build());

            position++;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName("ProductList");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            ProductContent.ProductItem item = ProductContent.ITEMS.get(position);
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(item);

            // Build and send an Event.
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("상품리스트")
                    .setAction("상품클릭")
                    .setLabel(item.title)
                    .build());


            // Measuring actions for ecommerce tracking.
            Product product =  new Product()
                    .setId(item.id)
                    .setName(item.title)
                    .setCategory("데이터분석과정")
                    .setBrand("넥스트")
                    .setVariant(item.schedule)
                    .setPosition(position + 1);
                    //.setCustomDimension(1, "Member");
            // Action product click
            ProductAction productActionClick = new ProductAction(ProductAction.ACTION_CLICK)
                    .setProductActionList("Product List");
            HitBuilders.ScreenViewBuilder builderClick = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionClick);
            // Action product views
            ProductAction productActionView = new ProductAction(ProductAction.ACTION_DETAIL)
                    .setProductActionList("Product List");
            HitBuilders.ScreenViewBuilder builderView = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productActionView);

            mTracker.setScreenName("ProductList");
            mTracker.send(builderClick.build());
            mTracker.send(builderView.build());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(ProductContent.ProductItem item);
    }

}
