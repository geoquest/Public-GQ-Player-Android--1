package com.uni.bonn.nfc4mgtest.views;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;

public class WiFiAutoConnectFragment extends Fragment implements
		OnNewIntentListener, OnClickListener {

	private EditText ssid, password;
	private Button init, autoConnect;

	// Global Tag reference
	private Tag mTag = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.wifi_auto_connect, container,
				false);

		ssid = (EditText) rootView.findViewById(R.id.ssid);
		password = (EditText) rootView.findViewById(R.id.password);

		init = (Button) rootView.findViewById(R.id.init);
		autoConnect = (Button) rootView.findViewById(R.id.autoconnect);

		init.setOnClickListener(this);
		autoConnect.setOnClickListener(this);

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

			break;
		case R.id.autoconnect:

			String networkId = ssid.getEditableText().toString();
			String pwd = password.getEditableText().toString();

			SimpleWifiInfo wifiInfo = new SimpleWifiInfo();
			wifiInfo.setSsid(networkId);
			wifiInfo.setPwd(pwd);
			setNewWifi(wifiInfo);
			break;
		default:
			break;
		}
	}

	protected void setNewWifi(SimpleWifiInfo wifiInfo) {

		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);

		wifiManager.setWifiEnabled(true);

		boolean foundAKnownNetwork = false;

		//In case of no configured network, this will be null
		List<WifiConfiguration> configuredNetworks = wifiManager
				.getConfiguredNetworks();

		if (null != configuredNetworks) {
			for (WifiConfiguration wifiConfiguration : configuredNetworks) {

				if (wifiConfiguration.SSID.equals("\"" + wifiInfo.getSsid()
						+ "\"")) {

					foundAKnownNetwork = true;

					boolean result = wifiManager.enableNetwork(
							wifiConfiguration.networkId, true);

					if (result) {
						showLongToast("Now connected to known network \""
								+ wifiInfo.getSsid()
								+ "\". If you want to set a new WPA key, please delete the network first.");
					} else {
						showLongToast("Connection to a known network failed.");
					}
				}
			}
		}

		if (!foundAKnownNetwork) {
			setupNewNetwork(wifiInfo, wifiManager);
		}
	}

	protected void setupNewNetwork(SimpleWifiInfo wifiInfo,
			WifiManager wifiManager) {

		WifiConfiguration wc = new WifiConfiguration();

		wc.SSID = "\"" + wifiInfo.getSsid() + "\"";
		wc.preSharedKey = "\"" + wifiInfo.getPwd() + "\"";

		int networkId = wifiManager.addNetwork(wc);
		boolean result = wifiManager.enableNetwork(networkId, true);

		if (result) {
			showLongToast("Now connected to \"" + wifiInfo.getSsid() + "\"");
			wifiManager.saveConfiguration();
		} else {
			showLongToast("Creating connection failed. " + wc);
		}
	}

	private void showLongToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	class SimpleWifiInfo {

		String ssid;
		String pwd;

		public String getSsid() {
			return ssid;
		}

		public void setSsid(String ssid) {
			this.ssid = ssid;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
	}
}
