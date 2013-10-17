package com.uni.bonn.nfc4mgtest.views;

import android.app.Fragment;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;

public class DefaultFragment extends Fragment implements OnNewIntentListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.default_intro, container,
				false);

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));
		return rootView;

	}

	@Override
	public void onNewNfcIntent(Tag tag) {

	}
}
