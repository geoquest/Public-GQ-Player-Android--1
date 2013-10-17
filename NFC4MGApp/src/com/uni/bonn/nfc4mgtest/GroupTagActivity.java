package com.uni.bonn.nfc4mgtest;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uni.bonn.nfc4mg.NFCEventManager;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.groups.GroupManager;
import com.uni.bonn.nfc4mg.groups.GroupPermission;
import com.uni.bonn.nfc4mg.tagmodels.GroupTagModel;

/*public class GroupTagActivity extends Activity implements OnClickListener {

	private static final String TAG = "InfoTagActivity";

	private TextView nfcStatus;

	private Context ctx;
	private GroupManager mGroupManager;

	// Global Tag reference
	private Tag mTag = null;

	private NFCEventManager mNFCEventManager = null;

	private Button init, read, write, join, leave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.group);

		init = (Button) findViewById(R.id.init);
		read = (Button) findViewById(R.id.read);
		write = (Button) findViewById(R.id.write);
		join = (Button) findViewById(R.id.join);
		leave = (Button) findViewById(R.id.leave);

		init.setOnClickListener(this);
		read.setOnClickListener(this);
		write.setOnClickListener(this);
		join.setOnClickListener(this);
		leave.setOnClickListener(this);

		this.ctx = this;
		mGroupManager = GroupManager.getGroupManager();

		try {
			mNFCEventManager = NFCEventManager.getInstance(ctx);
			mNFCEventManager.initialize(this.ctx, GroupTagActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this.ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mNFCEventManager) {
			mNFCEventManager.attachNFCListener(GroupTagActivity.this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mNFCEventManager) {
			mNFCEventManager.removeNFCListener(GroupTagActivity.this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.v(TAG, "Inside onNewIntent fn");

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Log.v(TAG, "Intent Action :: ACTION_TAG_DISCOVERED");

			mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			nfcStatus.setText("Ready to interact with NFC Tag");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.init:

			GroupTagModel model = new GroupTagModel("group_1",
					GroupPermission.ALL_READ_WRITE, 1, 0,
					"Hallo, I am in group 1.");
			try {
				if (mGroupManager.initializeGroupTag(model, mTag)) {
					nfcStatus.setText("Group Tag Initialized Successfully");
				}

			} catch (IOException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (FormatException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (NfcTagException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			}
			break;
		case R.id.read:

			try {
				GroupTagModel rModel = mGroupManager.readGroupData(this, mTag);
				nfcStatus.setText(rModel.getData());
			} catch (IOException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (FormatException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (NfcTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case R.id.write:
			try {
				if (mGroupManager.writeDataToGroup(this, mTag,
						"I am overwriting data")) {
					nfcStatus.setText("data is written to group");
				} else {
					nfcStatus.setText("Tag is not formatted for Group Tag.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (FormatException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (NfcTagException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			}
			break;
		case R.id.join:

			try {
				if (mGroupManager.joinGroup(this, mTag)) {
					nfcStatus.setText("You are in group now.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (FormatException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (NfcTagException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			}

			break;
		case R.id.leave:

			try {
				if (mGroupManager.leaveGroup(this, mTag)) {
					nfcStatus.setText("You are not in group now.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (FormatException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			} catch (NfcTagException e) {
				e.printStackTrace();
				nfcStatus.setText(e.getMessage());
			}

			break;
		}
	}
}
*/