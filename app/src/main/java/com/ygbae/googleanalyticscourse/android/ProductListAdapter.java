package com.ygbae.googleanalyticscourse.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextmining.mobileanalysiscourse.R;
import com.ygbae.googleanalyticscourse.android.dummy.ProductContent;

import java.util.ArrayList;
import java.util.List;

/**
 * The adapter for product list.
 * 
 * @author Younggue Bae
 */
public class ProductListAdapter extends BaseAdapter {

    private static final String TAG = "ProductListAdapter";

	Context mContext;
	LayoutInflater mInflater;
	private List<ProductContent.ProductItem> mRowList;

	public ProductListAdapter(Context context) {
		this(context, new ArrayList<ProductContent.ProductItem>());
	}

	public ProductListAdapter(Context context, List<ProductContent.ProductItem> items) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = context;
		this.mRowList = items;
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_product, parent, false);

			holder.mImage = (ImageView) convertView.findViewById(R.id.imageView);
			holder.mTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.mSchedule = (TextView) convertView.findViewById(R.id.textViewSchedule);
			holder.mPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
            holder.mImage.setId(position);
            holder.mTitle.setId(position);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ProductContent.ProductItem item = (ProductContent.ProductItem) getItem(position);
        //Log.d(TAG, "position = " + position);

        // holder.mImage.setImageResource(item.getImageResourceId());
		holder.mTitle.setText(item.title);
        holder.mSchedule.setText(item.schedule);
        holder.mPrice.setText(item.price + "원");


		return convertView;
	}
	
	@Override
	public int getCount() {
		return mRowList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRowList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * View holder for a list item.
	 * 
	 */
	class ViewHolder {
		ImageView mImage;
		TextView mTitle;
		TextView mSchedule;
		TextView mPrice;
	}

}