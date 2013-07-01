package com.uni.bonn.nfc4mgtest.appInterfacing;

import android.nfc.Tag;

public interface OnNewIntentListener {
	
	public void onNewNfcIntent(Tag tag);
	
}
