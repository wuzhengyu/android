package com.aozhi.myplayer.t2;

import com.aozhi.myplayer.MusicService;
import com.aozhi.myplayer.R;
import com.aozhi.myplayer.R.id;
import com.aozhi.myplayer.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	/**
	 * The URL we suggest as default when adding by URL. This is just so that the user doesn't have to find an URL to
	 * test this sample.
	 */
	final String SUGGESTED_URL = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";

	Button mPlayButton;
	Button mPauseButton;
	Button mSkipButton;
	Button mRewindButton;
	Button mStopButton;
	Button mEjectButton;

	/**
	 * Called when the activity is first created. Here, we simply set the event listeners and start the background
	 * service ({@link MusicService}) that will handle the actual media playback.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mPlayButton = (Button) findViewById(R.id.playbutton);
		mPauseButton = (Button) findViewById(R.id.pausebutton);
		mSkipButton = (Button) findViewById(R.id.skipbutton);
		mRewindButton = (Button) findViewById(R.id.rewindbutton);
		mStopButton = (Button) findViewById(R.id.stopbutton);
		mEjectButton = (Button) findViewById(R.id.ejectbutton);

		mPlayButton.setOnClickListener(this);
		mPauseButton.setOnClickListener(this);
		mSkipButton.setOnClickListener(this);
		mRewindButton.setOnClickListener(this);
		mStopButton.setOnClickListener(this);
		mEjectButton.setOnClickListener(this);
	}

	public void onClick(View target) {
		// Send the correct intent to the MusicService, according to the button that was clicked
		if (target == mPlayButton)
			startService(new Intent(MusicService.ACTION_PLAY));
		else if (target == mPauseButton)
			startService(new Intent(MusicService.ACTION_PAUSE));
		else if (target == mSkipButton)
			startService(new Intent(MusicService.ACTION_SKIP));
		else if (target == mRewindButton)
			startService(new Intent(MusicService.ACTION_REWIND));
		else if (target == mStopButton)
			startService(new Intent(MusicService.ACTION_STOP));
		else if (target == mEjectButton) {
			showUrlDialog();
		}
	}

	/**
	 * Shows an alert dialog where the user can input a URL. After showing the dialog, if the user confirms, sends the
	 * appropriate intent to the {@link MusicService} to cause that URL to be played.
	 */
	void showUrlDialog() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle("Manual Input");
		alertBuilder.setMessage("Enter a URL (must be http://)");
		final EditText input = new EditText(this);
		alertBuilder.setView(input);

		input.setText(SUGGESTED_URL);

		alertBuilder.setPositiveButton("Play!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int whichButton) {
				// Send an intent with the URL of the song to play. This is expected by
				// MusicService.
				Intent i = new Intent(MusicService.ACTION_URL);
				Uri uri = Uri.parse(input.getText().toString());
				i.setData(uri);
				startService(i);
			}
		});
		alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int whichButton) {
			}
		});

		alertBuilder.show();
	}

}
