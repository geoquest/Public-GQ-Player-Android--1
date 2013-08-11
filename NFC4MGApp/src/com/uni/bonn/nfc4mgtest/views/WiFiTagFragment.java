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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.tagmodels.WiFiTagModel;
import com.uni.bonn.nfc4mg.wifitag.WiFiActionModes;
import com.uni.bonn.nfc4mg.wifitag.WiFiTag;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class WiFiTagFragment extends Fragment implements OnNewIntentListener,
		OnClickListener {

	private EditText id, ssid, password;
	private RadioGroup action_mode;
	private Button read, write;

	// Global Tag reference
	private Tag mTag = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.wifi_tag, container, false);

		id = (EditText) rootView.findViewById(R.id.id);
		ssid = (EditText) rootView.findViewById(R.id.ssid);
		password = (EditText) rootView.findViewById(R.id.password);

		action_mode = (RadioGroup) rootView.findViewById(R.id.action_mode);
		action_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {

				switch (id) {
				case R.id.action3:
					ssid.setVisibility(View.VISIBLE);
					password.setVisibility(View.VISIBLE);
					break;
				default:
					ssid.setVisibility(View.GONE);
					password.setVisibility(View.GONE);
					break;
				}
			}
		});

		read = (Button) rootView.findViewById(R.id.read);
		write = (Button) rootView.findViewById(R.id.write);

		read.setOnClickListener(this);
		write.setOnClickListener(this);

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
		case R.id.read:

			try {

				if (null != mTag) {
					WiFiTagModel model = WiFiTag.getInstance()
							.readTagData(mTag);

					if (null != model) {
						id.setText(model.getId());

						RadioButton btn = null;
						switch (model.getActionMode()) {

						case WiFiActionModes.WIFI_ON_OFF_AUTOMATICALLY:
							btn = (RadioButton) action_mode.getChildAt(0);
							btn.toggle();
							break;
						case WiFiActionModes.WIFI_PAIRING_SCREEN:
							btn = (RadioButton) action_mode.getChildAt(1);
							btn.toggle();
							break;
						case WiFiActionModes.WIFI_AUTO_CONNECT:
							btn = (RadioButton) action_mode.getChildAt(2);
							btn.toggle();
							ssid.setVisibility(View.VISIBLE);
							password.setVisibility(View.VISIBLE);
							ssid.setText(model.getSsid());
							password.setText(model.getPassword());
							break;
						}
					}
				} else {
					AppErrors.showToastMsg(getActivity(),
							"Touch NFC Tag First.");
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

			break;
		case R.id.write:

			try {

				int mode = WiFiActionModes.WIFI_ON_OFF_AUTOMATICALLY;
				final String wId = id.getEditableText().toString();
				WiFiTagModel model = null;
				
				switch (action_mode.getCheckedRadioButtonId()) {

				case R.id.action1:
					mode = WiFiActionModes.WIFI_ON_OFF_AUTOMATICALLY;
					model = new WiFiTagModel(wId, mode);
					break;
				case R.id.action2:
					mode = WiFiActionModes.WIFI_PAIRING_SCREEN;
					model = new WiFiTagModel(wId, mode);
					break;
				case R.id.action3:
					mode = WiFiActionModes.WIFI_AUTO_CONNECT;
					String ssidVal = ssid.getEditableText().toString();
					String pwdVal = password.getEditableText().toString();
					model = new WiFiTagModel(wId, mode, ssidVal, pwdVal);
					break;
				}


				if (null != mTag) {
					if (WiFiTag.getInstance().write2Tag(model, mTag)) {

						AppErrors.showToastMsg(getActivity(), "Success.");
					}

				} else {
					AppErrors.showToastMsg(getActivity(),
							"Touch NFC Tag First.");
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

			break;

		default:
			break;
		}
	}
}
