package com.uni.bonn.nfc4mgtest.views;

import java.io.IOException;

import android.app.Fragment;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uni.bonn.nfc4mg.bttag.BluetoothService;
import com.uni.bonn.nfc4mg.constants.TagConstants;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.nfctag.ParseTagListener;
import com.uni.bonn.nfc4mg.nfctag.TagHandler;
import com.uni.bonn.nfc4mg.wifitag.WiFiService;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class AutoTagFragment extends Fragment implements OnNewIntentListener,
		ParseTagListener, OnClickListener {

	// Global Tag reference
	private Tag mTag = null;

	private TextView info;

	private TagHandler mTagHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.auto_detection, container,
				false);

		info = (TextView) rootView.findViewById(R.id.info);

		try {
			mTagHandler = new TagHandler(getActivity(), this);
		} catch (NfcTagException e) {
			e.printStackTrace();
			AppErrors.showToastMsg(getActivity(), e.getMessage());
		}

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));
		return rootView;

	}

	@Override
	public void onNewNfcIntent(Tag tag) {
		this.mTag = tag;

		try {
			mTagHandler.parseScannedTag(mTag);
		} catch (IOException e) {

			e.printStackTrace();
			AppErrors.showToastMsg(getActivity(), e.getMessage());

		} catch (FormatException e) {

			e.printStackTrace();
			AppErrors.showToastMsg(getActivity(), e.getMessage());

		} catch (NfcTagException e) {
			e.printStackTrace();
			AppErrors.showToastMsg(getActivity(), e.getMessage());
		}
	}

	@Override
	public void onStartParsing(String msg) {

	}

	@Override
	public void onParseComplete(int tagType) {

		switch (tagType) {
		case TagConstants.TAG_TYPE_INFO:
			info.setText("Info Tag detected. Go to Info Tag to view tag content");
			break;
		case TagConstants.TAG_TYPE_GPS:
			info.setText("GPS Tag detected. Go to GPS Tag to view tag content");

			break;
		case TagConstants.TAG_TYPE_GROUP:
			info.setText("Group Tag detected. Go to Group Tag to view tag content");

			break;
		case TagConstants.TAG_TYPE_RESOURCE:
			info.setText("Resource Tag detected. Go to Resource Tag to view tag content");
			break;
		case TagConstants.TAG_TYPE_BT:

			int action_mode = mTagHandler.getmBTTagModel().getActionMode();
			BluetoothService.handleBluetoothActionMode(getActivity(),
					action_mode);
			break;
		case TagConstants.TAG_TYPE_WIFI:
			// info.setText("WiFi Tag detected. Go to WiFi Tag to view tag content");
			int mode = mTagHandler.getmWiFiTagModel().getActionMode();
			WiFiService.handleWifiActionMode(getActivity(), mode);
			break;

		default:
			info.setText("This Tag cannot be parsed by the framework.");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

}
