package com.uni.bonn.nfc4mgtest.views;

import java.io.IOException;

import android.app.Fragment;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.infotag.InfoTag;
import com.uni.bonn.nfc4mg.tagmodels.InfoTagModel;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class InfoTagFragment extends Fragment implements OnClickListener,
		OnNewIntentListener {

	private static final String TAG = "InfoTagFragment";
	private EditText id, data;
	private Button read, write, initialize;

	// Global Tag reference
	private Tag mTag = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.info_tag, container, false);

		id = (EditText) rootView.findViewById(R.id.id);
		data = (EditText) rootView.findViewById(R.id.data);
		read = (Button) rootView.findViewById(R.id.read);
		write = (Button) rootView.findViewById(R.id.write);
		initialize = (Button) rootView.findViewById(R.id.init);

		initialize.setOnClickListener(this);
		read.setOnClickListener(this);
		write.setOnClickListener(this);

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:

			InfoTagModel iModel = new InfoTagModel("tag_1", "This is info tag.");
			writeInfoData(iModel);
			break;
		case R.id.read:

			if (null != mTag) {
				try {

					InfoTag tag = InfoTag.getInstance();
					InfoTagModel model = tag.readTagData(mTag);

					if (null != model) {
						id.setText(model.getId());
						data.setText(model.getData());
					} else {
						id.setText("");
						data.setText("");
						AppErrors.showToastMsg(getActivity(),
								"Info Tag Formatted Exception.");
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
			String dd = data.getEditableText().toString();

			InfoTagModel model = new InfoTagModel(ii, dd);
			writeInfoData(model);
			break;
		default:
			break;
		}
	}

	@Override
	public void onNewNfcIntent(Tag tag) {

		Log.d(TAG, "InfoTag on new intent");
		this.mTag = tag;
	}

	private void writeInfoData(InfoTagModel model) {
		if (null != mTag) {
			try {
				InfoTag tag = InfoTag.getInstance();
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
