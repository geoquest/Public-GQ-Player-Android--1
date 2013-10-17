package com.uni.bonn.nfc4mgtest.utility;

import android.app.Activity;
import android.widget.Toast;

public class AppErrors {

	public static void showToastMsg(Activity activity, String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
				.show();
	}
	
	

}
