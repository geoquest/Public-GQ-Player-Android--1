package com.uni.bonn.nfc4mgtest.views;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.uni.bonn.nfc4mgtest.R;
import com.uni.bonn.nfc4mgtest.appInterfacing.OnNewIntentListener;
import com.uni.bonn.nfc4mgtest.constants.AppConstants;

public class VideoFragment extends Fragment implements OnNewIntentListener,
		OnClickListener {

	private static final String TAG = "VideoFragment";

	private static final int MSG_REPLAY_TILL_DURATION = 1;
	private static final int MSG_STOP_ON_GIVEN_DURATION = 2;
	private static final int MSG_STARTS_FROM_GIVEN_DURATION = 3;
	private static final int MSG_PAUSE = 4;
	private static final int MSG_PLAY = 5;

	private static VideoView video_src;
	private Button replay;
	private String mVideoPath = "";

	private static int mLastDuration = 20000; // In Milliseconds

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.video, container, false);

		video_src = (VideoView) rootView.findViewById(R.id.video_src);
		replay = (Button) rootView.findViewById(R.id.replay);

		replay.setOnClickListener(this);

		getActivity().setTitle(
				getArguments().getString(AppConstants.ARG_TAG_TYPES));

		mVideoPath = "android.resource://" + getActivity().getPackageName()
				+ "/" + R.raw.test_video;

		// video_src.setOnPreparedListener(mOnPreparedListener);
		video_src.setVideoURI(Uri.parse(mVideoPath));

		return rootView;
	}

	@Override
	public void onNewNfcIntent(Tag tag) {

		// remove old callback first
		mHandler.removeMessages(MSG_PAUSE);

		Message m = new Message();
		m.what = MSG_PLAY;
		mHandler.sendMessage(m);
		Message m1 = new Message();
		m1.what = MSG_PAUSE;
		mHandler.sendMessageDelayed(m1, mLastDuration);
	}

	/**
	 * Handler Attached to activity to execute video related events.
	 */
	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_REPLAY_TILL_DURATION:

				break;
			case MSG_STOP_ON_GIVEN_DURATION:
				break;
			case MSG_STARTS_FROM_GIVEN_DURATION:
				break;
			case MSG_PLAY:
				video_src.start();
				break;
			case MSG_PAUSE:
				video_src.pause();
				break;
			default:
				break;
			}
		}

	};

	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {

			/*
			 * int duration = mp.getDuration(); int curr_pos =
			 * mp.getCurrentPosition();
			 * 
			 * mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			 * 
			 * @Override public void onSeekComplete(MediaPlayer mp) {
			 * 
			 * Log.d(TAG, "Seek operation complete"); mp.start(); } });
			 * 
			 * Log.d(TAG, "duration = " + duration); Log.d(TAG, "curr_pos = " +
			 * curr_pos);
			 * 
			 * if(video_src.canSeekForward()){ Log.d(TAG,
			 * "Seek operation supported."); mp.seekTo(50); }
			 */
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.replay:

			// remove old callback first
			mHandler.removeMessages(MSG_PAUSE);

			video_src.setVideoURI(Uri.parse(mVideoPath));
			Message m = new Message();
			m.what = MSG_PLAY;
			mHandler.sendMessage(m);

			Message m1 = new Message();
			m1.what = MSG_PAUSE;
			mHandler.sendMessageDelayed(m1, mLastDuration);
			break;
		}
	}

}
