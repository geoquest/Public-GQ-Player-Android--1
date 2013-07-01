package com.uni.bonn.nfc4mg.wifitag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * This class manages system wifi related features.
 * 
 * @author shubham
 * 
 */
public final class WiFiService {

	/**
	 * Caller of the API must pass the activity context and action mode read
	 * from the nfc tag
	 * 
	 * @param ctx
	 * @param actionMode
	 * @return false : in case action mode is supported by framework, else true
	 */
	public static void handleWifiActionMode(Context ctx, int actionMode) {

		switch (actionMode) {
		case WiFiActionModes.WIFI_ON_OFF_AUTOMATICALLY:
			showAlertDialog(ctx,
					"Would you like to perform WiFi toogle operation?",
					actionMode);
			break;
		case WiFiActionModes.WIFI_PAIRING_SCREEN:
			showAlertDialog(ctx, "Do you want to launch WiFi Settings?",
					actionMode);
			break;
		}
	}

	/**
	 * API to toggle system Wifi service.
	 * 
	 * @return
	 */
	private static boolean toogleSystemWiFi(Context ctx) {

		WifiManager wifiManager = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);

		return wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
	}

	/**
	 * API to launch Wifi Setting activity
	 * 
	 * @param ctx
	 */
	private static void launchWiFiSetting(Context ctx) {

		Intent intentWifi = new Intent();
		intentWifi.setAction(android.provider.Settings.ACTION_WIFI_SETTINGS);
		ctx.startActivity(intentWifi);
	}

	private static void showAlertDialog(final Context ctx, String msg,
			final int actionMode) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

		// set title
		alertDialogBuilder.setTitle("WiFi Service");

		// set dialog message
		alertDialogBuilder.setMessage(msg).setCancelable(true)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						switch (actionMode) {
						case WiFiActionModes.WIFI_ON_OFF_AUTOMATICALLY:

							toogleSystemWiFi(ctx);
							break;
						case WiFiActionModes.WIFI_PAIRING_SCREEN:
							launchWiFiSetting(ctx);
							break;
						}
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}
