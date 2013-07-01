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
import android.widget.TextView;

import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.groups.GroupManager;
import com.uni.bonn.nfc4mg.groups.GroupPermission;
import com.uni.bonn.nfc4mg.tagmodels.GroupTagModel;
import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class GroupTagFragment extends Fragment implements OnNewIntentListener,
		OnClickListener {

	private GroupManager mGroupManager;
	// Global Tag reference
	private Tag mTag = null;

	private TextView nfcStatus;
	private EditText groupData;
	private Button init, read, write, join, leave;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.group, container, false);
		groupData = (EditText) rootView.findViewById(R.id.groupData);
		init = (Button) rootView.findViewById(R.id.init);
		read = (Button) rootView.findViewById(R.id.read);
		write = (Button) rootView.findViewById(R.id.write);
		join = (Button) rootView.findViewById(R.id.join);
		leave = (Button) rootView.findViewById(R.id.leave);
		nfcStatus = (TextView) rootView.findViewById(R.id.nfcStatus);

		init.setOnClickListener(this);
		read.setOnClickListener(this);
		write.setOnClickListener(this);
		join.setOnClickListener(this);
		leave.setOnClickListener(this);

		mGroupManager = GroupManager.getGroupManager();

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

			GroupTagModel model = new GroupTagModel("group_1",
					GroupPermission.ALL_READ_WRITE, 1, 0,
					"Hallo, I am in group 1.");
			try {
				if (null != mTag) {
					if (mGroupManager.initializeGroupTag(model, mTag)) {
						nfcStatus.setText("Group Tag Initialized Successfully");
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
		case R.id.read:

			try {

				if (null != mTag) {
					GroupTagModel gModel = mGroupManager.readGroupData(
							getActivity(), mTag);

					groupData.setText(gModel.getData());

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

				if (null != mTag) {
					if (mGroupManager.writeDataToGroup(getActivity(), mTag,
							groupData.getEditableText().toString())) {
						nfcStatus.setText("data is written to group");
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
		case R.id.join:

			try {

				if (null != mTag) {
					if (mGroupManager.joinGroup(getActivity(), mTag)) {
						nfcStatus.setText("You are in group now.");
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
		case R.id.leave:

			try {

				if (null != mTag) {
					if (mGroupManager.leaveGroup(getActivity(), mTag)) {
						nfcStatus.setText("You are not in group now.");
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
		}
	}
}
