package com.uni.bonn.nfc4mgtest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uni.bonn.nfc4mg.NFCEventManager;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;
import com.uni.bonn.nfc4mgtest.views.AutoTagFragment;
import com.uni.bonn.nfc4mgtest.views.BluetoothTagFragment;
import com.uni.bonn.nfc4mgtest.views.DefaultFragment;
import com.uni.bonn.nfc4mgtest.views.GpsTagFragment;
import com.uni.bonn.nfc4mgtest.views.GroupTagFragment;
import com.uni.bonn.nfc4mgtest.views.InfoTagFragment;
import com.uni.bonn.nfc4mgtest.views.ManageResourceFragment;
import com.uni.bonn.nfc4mgtest.views.ResourceTagFragment;
import com.uni.bonn.nfc4mgtest.views.VideoFragment;
import com.uni.bonn.nfc4mgtest.views.WiFiTagFragment;

public class SliderActivityExample extends Activity {

	private static final String TAG = "SliderActivityExample";

	// Array holding left drawer items
	private String[] mLeftDrawerTitles = {};

	// Array holding left drawer items
	private String[] mRightDrawerTitles = {};

	private ListView mDrawerList;
	private ListView mRightDrawerList;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private NFCEventManager mNFCEventManager = null;
	// Global Tag reference
	private Tag mTag = null;
	private OnNewIntentListener mOnNewIntentListener = null;

	private Fragment fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mLeftDrawerTitles = getResources().getStringArray(
				R.array.left_drawer_item_array);
		mRightDrawerTitles = getResources().getStringArray(
				R.array.right_drawer_item_array);

		// layout references
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mRightDrawerList = (ListView) findViewById(R.id.right_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mLeftDrawerTitles));

		mRightDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mRightDrawerTitles));

		// Setting click listener on drawer list menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mRightDrawerList
				.setOnItemClickListener(new RightDrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		try {
			mNFCEventManager = NFCEventManager.getInstance(this);
			mNFCEventManager.initialize(this, SliderActivityExample.this);
		} catch (Exception e) {
			e.printStackTrace();

			// TODO show alert

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mNFCEventManager) {
			mNFCEventManager.attachNFCListener(SliderActivityExample.this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mNFCEventManager) {
			mNFCEventManager.removeNFCListener(SliderActivityExample.this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.v(TAG, "Inside onNewIntent fn");

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Log.v(TAG, "Intent Action :: ACTION_TAG_DISCOVERED");

			mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

			if (null != mOnNewIntentListener) {
				mOnNewIntentListener.onNewNfcIntent(mTag);
			}
		}
	}

	/**
	 * Class to handle click events for left drawer menu
	 * 
	 * @author shubham
	 * 
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {

			selectItem(position);
		}
	}

	/**
	 * Class to handle click events for right drawer menu
	 * 
	 * @author shubham
	 * 
	 */
	private class RightDrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {

			selectRightDrawerItem(position);
		}
	}

	private void selectRightDrawerItem(int position) {

		mRightDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mRightDrawerList);

		switch (position) {
		case 0:
			fragment = new ManageResourceFragment();
			
			break;
		case 1:
			Intent intent = new Intent(SliderActivityExample.this,
					BeamActivity.class);
			startActivity(intent);
			return;
		case 2:
			fragment = new VideoFragment();
			break;
		}
		
		Bundle args = new Bundle();
		args.putString(AppConstants.ARG_TAG_TYPES,
				mRightDrawerTitles[position]);
		fragment.setArguments(args);

		// adding this line of code to pass onNewIntent activity callback to
		// fragment.
		mOnNewIntentListener = (OnNewIntentListener) fragment;

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position

		Log.d(TAG, "position = " + position);

		switch (position) {
		case AppConstants.TAG_INTRODUCTION:
			fragment = new DefaultFragment();
			break;
		case AppConstants.TAG_TYPE_INFO:
			fragment = new InfoTagFragment();
			break;
		case AppConstants.TAG_TYPE_GPS:
			fragment = new GpsTagFragment();
			break;
		case AppConstants.TAG_TYPE_GROUP:
			fragment = new GroupTagFragment();
			break;
		case AppConstants.TAG_TYPE_RESOURCE:
			fragment = new ResourceTagFragment();
			break;
		case AppConstants.TAG_TYPE_BT:
			fragment = new BluetoothTagFragment();
			break;
		case AppConstants.TAG_TYPE_WIFI:
			fragment = new WiFiTagFragment();
			break;
		case AppConstants.TAG_TYPE_AUTO:
			fragment = new AutoTagFragment();
			break;
		}

		Bundle args = new Bundle();
		args.putString(AppConstants.ARG_TAG_TYPES, mLeftDrawerTitles[position]);
		fragment.setArguments(args);

		// adding this line of code to pass onNewIntent activity callback to
		// fragment.
		mOnNewIntentListener = (OnNewIntentListener) fragment;

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mLeftDrawerTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
