package com.uni.bonn.nfc4mgtest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uni.bonn.nfc4mg.NFCEventManager;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.inventory.InventoryManager;
import com.uni.bonn.nfc4mg.inventory.InventoryManager.OnResourceCallback;
import com.uni.bonn.nfc4mg.inventory.InventoryModel;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.utility.AppErrors;

public class BeamActivity extends Activity implements
		CreateNdefMessageCallback, OnNdefPushCompleteCallback,
		OnResourceCallback {

	NfcAdapter mNfcAdapter;
	TextView mInfoText;
	EditText data;
	private static final int MESSAGE_SENT = 1;
	private Context ctx;
	private NFCEventManager mNFCEventManager = null;

	private InventoryModel model = null;
	private boolean resTransferMode = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beam);

		// general data beaming
		mInfoText = (TextView) findViewById(R.id.textView);
		data = (EditText) findViewById(R.id.data);

		model = (InventoryModel) getIntent().getSerializableExtra(
				AppConstants.ARG_RES_TRANSFER);

		// Check for available NFC Adapter
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			mInfoText = (TextView) findViewById(R.id.textView);
			mInfoText.setText("NFC is not available on this device.");
		}
		// Register callback to set NDEF message
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		// Register callback to listen for message-sent success
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

		this.ctx = this;
		try {
			mNFCEventManager = NFCEventManager.getInstance(ctx);
			mNFCEventManager.initialize(this.ctx, BeamActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this.ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		if (null != model) {

			resTransferMode = true;
			data.setVisibility(View.INVISIBLE);
			// Resource transfer mode
			mInfoText
					.setText("Ready to transfer resource, bring device to another NFC enabled device");

		}
	}

	/**
	 * Implementation for the CreateNdefMessageCallback interface
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		Time time = new Time();
		time.setToNow();

		NdefMessage msg = null;

		if (resTransferMode) {

			try {
				msg = InventoryManager.getInventoryManager()
						.composeRessourceTransferMessage(model);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			String text = data.getEditableText().toString();
			msg = new NdefMessage(new NdefRecord[] { createMimeRecord(
					"application/com.example.android.beam", text.getBytes()) });
		}
		return msg;
	}

	/**
	 * Implementation for the OnNdefPushCompleteCallback interface
	 */
	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		// A handler is needed to send messages to the activity when this
		// callback occurs, because it happens from a binder thread
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
		if (resTransferMode) {

			// on successfull resource transfer, remove resource from inventory
			InventoryManager.getInventoryManager().removeItem(model.getId());
			finish();
		}
	}

	/** This handler receives a message from onNdefPushComplete */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "Message sent!",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();

		if (null != mNFCEventManager) {
			mNFCEventManager.attachNFCListener(BeamActivity.this);
		}

		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mNFCEventManager) {
			mNFCEventManager.removeNFCListener(BeamActivity.this);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		// setIntent(intent);
		processIntent(intent);
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present

		NdefRecord records[] = msg.getRecords();

		if (records.length == 1) {
			mInfoText.setText(new String(msg.getRecords()[0].getPayload()));
		} else {
			processResTransfer(intent);
		}

	}

	/**
	 * Handling resource tag
	 * 
	 * @param intent
	 */
	private void processResTransfer(Intent intent) {

		try {
			if (InventoryManager.getInventoryManager().handleTransferResource(
					intent)) {
				AppErrors.showToastMsg(this,
						"Resource added into your repository");
			}
		} catch (IOException e) {

			e.printStackTrace();
			AppErrors.showToastMsg(this, e.getMessage());

		} catch (FormatException e) {

			e.printStackTrace();
			AppErrors.showToastMsg(this, e.getMessage());

		} catch (NfcTagException e) {
			e.printStackTrace();
			AppErrors.showToastMsg(this, e.getMessage());
		}
	}

	/**
	 * Creates a custom MIME type encapsulated in an NDEF record
	 * 
	 * @param mimeType
	 */
	public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// If NFC is not available, we won't be needing this menu
		if (mNfcAdapter == null) {
			return super.onCreateOptionsMenu(menu);
		}
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResourceEmpty(int error, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEarnedResource(InventoryModel addedModel) {
		// TODO Auto-generated method stub

	}
}
