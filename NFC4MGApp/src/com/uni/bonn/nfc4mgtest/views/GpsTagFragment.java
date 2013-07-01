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
import android.widget.Button;
import android.widget.EditText;

import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.gpstag.GpsTag;
import com.uni.bonn.nfc4mg.infotag.InfoTag;
import com.uni.bonn.nfc4mg.tagmodels.GPSTagModel;
import com.uni.bonn.nfc4mg.tagmodels.InfoTagModel;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class GpsTagFragment extends Fragment implements OnNewIntentListener,
		OnClickListener {

	private EditText id, latitude, longitude;
	private Button read, write, init;

	// Global Tag reference
	private Tag mTag = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.gps_tag, container, false);

		id = (EditText) rootView.findViewById(R.id.id);
		latitude = (EditText) rootView.findViewById(R.id.latitude);
		longitude = (EditText) rootView.findViewById(R.id.longitude);
		read = (Button) rootView.findViewById(R.id.read);
		write = (Button) rootView.findViewById(R.id.write);
		init = (Button) rootView.findViewById(R.id.init);

		read.setOnClickListener(this);
		write.setOnClickListener(this);
		init.setOnClickListener(this);

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));
		return rootView;

	}

	@Override
	public void onNewNfcIntent(Tag tag) {
		this.mTag = tag;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:

			GPSTagModel iModel = new GPSTagModel("876", "50.733141",
					"7.103556", "University Bonn");

			writeGpsData(iModel);
			break;
		case R.id.read:

			if (null != mTag) {
				try {
					InfoTag tag = InfoTag.getInstance();
					InfoTagModel model = tag.readTagData(mTag);

					if (null != model) {
						id.setText(model.getId());
						longitude.setText(model.getData());
					} else {
						id.setText("");
						longitude.setText("");
						longitude.setText("");

						AppErrors.showToastMsg(getActivity(),
								"GPS Tag Formatted Exception.");

					}

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
			} else {
				AppErrors.showToastMsg(getActivity(), "Touch NFC Tag First.");
			}
			break;
		case R.id.write:

			String ii = id.getEditableText().toString();
			String mm = latitude.getEditableText().toString();
			String dd = longitude.getEditableText().toString();

			GPSTagModel model = new GPSTagModel(ii, mm, dd, "");
			writeGpsData(model);
			break;
		default:
			break;
		}
	}

	/**
	 * Private function to handle write data to gps tag
	 * 
	 * @param model
	 */
	private void writeGpsData(GPSTagModel model) {
		if (null != mTag) {
			try {
				GpsTag tag = GpsTag.getInstance();
				boolean status = tag.write2Tag(model, mTag);

				if (status) {

					AppErrors.showToastMsg(getActivity(), "Success.");
				}

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
		} else {

			AppErrors.showToastMsg(getActivity(), "Touch NFC Tag First.");
		}
	}

}
