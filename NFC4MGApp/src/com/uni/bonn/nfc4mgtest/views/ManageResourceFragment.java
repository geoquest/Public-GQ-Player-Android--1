package com.uni.bonn.nfc4mgtest.views;

import android.app.Fragment;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uni.bonn.nfc4mg.inventory.InventoryManager;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.binder.ResourceItemAdapter;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;

public class ManageResourceFragment extends Fragment implements
		OnNewIntentListener, OnClickListener {

	private static final String TAG = "ManageResourceFragment";

	// Global Tag reference
	private Tag mTag = null;

	private ListView resList;
	private ResourceItemAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.resources, container, false);

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));

		resList = (ListView) rootView.findViewById(R.id.resList);

		return rootView;

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "InsideonResume fn ");
		mAdapter = new ResourceItemAdapter(getActivity(), InventoryManager
				.getInventoryManager().getItemList());
		resList.setAdapter(mAdapter);
	}

	@Override
	public void onNewNfcIntent(Tag tag) {
		this.mTag = tag;
	}

	@Override
	public void onClick(View v) {
	}

}
