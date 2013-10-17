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
import com.uni.bonn.nfc4mg.inventory.InventoryManager;
import com.uni.bonn.nfc4mg.inventory.InventoryManager.OnResourceCallback;
import com.uni.bonn.nfc4mg.inventory.InventoryModel;
import com.uni.bonn.nfc4mg.tagmodels.ResourceTagModel;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class ResourceTagFragment extends Fragment implements
		OnNewIntentListener, OnClickListener, OnResourceCallback {

	private EditText id, name, count;
	private Button read, write, initialize, addResource;

	// Global Tag reference
	private Tag mTag = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.resource_tag, container,
				false);

		id = (EditText) rootView.findViewById(R.id.id);
		name = (EditText) rootView.findViewById(R.id.name);
		count = (EditText) rootView.findViewById(R.id.count);

		read = (Button) rootView.findViewById(R.id.read);
		write = (Button) rootView.findViewById(R.id.write);
		initialize = (Button) rootView.findViewById(R.id.init);
		addResource = (Button) rootView.findViewById(R.id.addResource);

		initialize.setOnClickListener(this);
		read.setOnClickListener(this);
		write.setOnClickListener(this);
		addResource.setOnClickListener(this);

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

			ResourceTagModel iModel = new ResourceTagModel("mango", "Mango", 1);
			writeResourceData(iModel);
			break;
		case R.id.read:

			try {

				if (null != mTag) {
					ResourceTagModel rModel = InventoryManager
							.getInventoryManager().readData(mTag);

					if (null != rModel) {

						id.setText(rModel.getId());
						name.setText(rModel.getName());
						count.setText("" + rModel.getCount());
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

			final String rid = id.getEditableText().toString();
			final String rname = name.getEditableText().toString();

			try {
				final int rcount = Integer.parseInt(count.getEditableText()
						.toString());
				ResourceTagModel model = new ResourceTagModel(rid, rname,
						rcount);
				writeResourceData(model);
			} catch (NumberFormatException e) {
				AppErrors.showToastMsg(getActivity(),
						"Please input a valid number");
			}

			break;
		case R.id.addResource:

			if (null != mTag) {

				try {
					InventoryManager.getInventoryManager()
							.setOnResourceCallback(this);
					InventoryManager.getInventoryManager().handleResourceTag(
							mTag);
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
		default:
			break;
		}
	}

	private void writeResourceData(ResourceTagModel model) {

		if (null != mTag) {

			try {

				boolean status = InventoryManager.getInventoryManager()
						.writeData(model, mTag);

				if (status) {

					AppErrors.showToastMsg(getActivity(), "Success.");

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
		} else {
			AppErrors.showToastMsg(getActivity(), "Touch NFC Tag First.");
		}

	}

	@Override
	public void onResourceEmpty(int error, String msg) {
		AppErrors.showToastMsg(getActivity(), "Resource already taken.");
	}

	@Override
	public void onEarnedResource(InventoryModel addedModel) {
		AppErrors.showToastMsg(getActivity(), "Resource added to inventory.");

	}
}
