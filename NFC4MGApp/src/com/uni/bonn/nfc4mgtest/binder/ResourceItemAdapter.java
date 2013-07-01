package com.uni.bonn.nfc4mgtest.binder;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uni.bonn.nfc4mg.inventory.InventoryModel;
import com.uni.bonn.nfc4mg.tagmodels.ResourceTagModel;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;

public class ResourceItemAdapter extends BaseAdapter {

	private static final String TAG = "AdvertisementCategoryAdapter";

	private LayoutInflater inflator = null;
	private ArrayList<InventoryModel> objects;
	private static Context ctx;

	private static class ViewHolder {
		public TextView resName;
		public Button transfer;

	}

	public ResourceItemAdapter(Context context,
			ArrayList<InventoryModel> objects) {

		this.objects = objects;
		this.ctx = context;

		Log.d(TAG, "Resource size = " + objects.size());

		this.inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		Log.d(TAG, "position = " + position);

		if (convertView == null) {
			convertView = inflator.inflate(R.layout.resources_item, null);

			TextView resName = (TextView) convertView
					.findViewById(R.id.resName);

			Button transfer = (Button) convertView.findViewById(R.id.transfer);

			holder = new ViewHolder();
			holder.resName = resName;
			holder.transfer = transfer;

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.transfer.setTag(position);
		holder.transfer.setOnClickListener(mTransferListener);

		InventoryModel obj = objects.get(position);

		if (null != holder.resName) {

			holder.resName.setText(obj.getName());
		}

		return convertView;
	}

	private OnClickListener mTransferListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			int position = (Integer) v.getTag();
			Log.d(TAG, "position = " + position);

			Intent intent = new Intent("com.uni.bonn.nfc4mgtest.BeamActivity");
			InventoryModel model = objects.get(position);
			intent.putExtra(AppConstants.ARG_RES_TRANSFER, model);
			ctx.startActivity(intent);

		}
	};

	@Override
	public int getCount() {

		if (null != objects) {
			return objects.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
