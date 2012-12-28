/**
 * Sample code for "Making Musical Apps" by Peter Brinkmann
 * http://shop.oreilly.com/product/0636920022503.do
 */

package com.noisepages.nettoyeur.guitartuner;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.IoUtils;

import com.noisepages.nettoyeur.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GuitarTunerActivity extends Activity implements OnClickListener {

	private static final String TAG = "GuitarTuner";
	private PdUiDispatcher dispatcher;

	private Button eButton;
	private Button aButton;
	private Button dButton;
	private Button gButton;
	private Button bButton;
	private Button eeButton;
	private TextView pitchLabel;
	private PitchView pitchView;

	private PdService pdService = null;

	private final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder)service).getService();
			try {
				initPd();
				loadPatch();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				finish();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// this method will never be called
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGui();
		initSystemServices();
		bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(pdConnection);
	}

	private void initGui() {
		setContentView(R.layout.main);
		eButton = (Button) findViewById(R.id.e_button);
		eButton.setOnClickListener(this);
		aButton = (Button) findViewById(R.id.a_button);
		aButton.setOnClickListener(this);
		dButton = (Button) findViewById(R.id.d_button);
		dButton.setOnClickListener(this);
		gButton = (Button) findViewById(R.id.g_button);
		gButton.setOnClickListener(this);
		bButton = (Button) findViewById(R.id.b_button);
		bButton.setOnClickListener(this);
		eeButton = (Button) findViewById(R.id.ee_button);
		eeButton.setOnClickListener(this);
		pitchLabel = (TextView) findViewById(R.id.pitch_label);
		pitchView = (PitchView) findViewById(R.id.pitch_view);
		pitchView.setCenterPitch(45);
		pitchLabel.setText("A-String");
	}

	private void  initPd() throws IOException {
		// Configure the audio glue
		AudioParameters.init(this);
		int sampleRate = AudioParameters.suggestSampleRate();
		pdService.initAudio(sampleRate, 1, 2, 10.0f);
		start();

		// Create and install the dispatcher
		dispatcher = new PdUiDispatcher();
		PdBase.setReceiver(dispatcher);
		dispatcher.addListener("pitch", new PdListener.Adapter() {
			@Override
			public void receiveFloat(String source, final float x) {
				pitchView.setCurrentPitch(x);
			}
		});
	}

	private void start() {
		if (!pdService.isRunning()) {
			Intent intent = new Intent(GuitarTunerActivity.this,
					GuitarTunerActivity.class);
			pdService.startAudio(intent, R.drawable.icon,
					"GuitarTuner", "Return to GuitarTuner.");
		}
	}

	private void loadPatch() throws IOException {
		File dir = getFilesDir();
		IoUtils.extractZipResource(
				getResources().openRawResource(R.raw.tuner), dir, true);
		File patchFile = new File(dir, "tuner.pd");
		PdBase.openPatch(patchFile.getAbsolutePath());
	}

	private void initSystemServices() {
		TelephonyManager telephonyManager =
				(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				if (pdService == null) return;
				if (state == TelephonyManager.CALL_STATE_IDLE) {
					start(); } else {
						pdService.stopAudio(); }
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void triggerNote(int n) {
		PdBase.sendFloat("midinote", n);
		PdBase.sendBang("trigger");
		pitchView.setCenterPitch(n);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.e_button:
			triggerNote(40); // E (low) is MIDI note 40.
			pitchLabel.setText("E-String");
			break;
		case R.id.a_button:
			triggerNote(45); // A is MIDI note 45.
			pitchLabel.setText("A-String");
			break;
		case R.id.d_button:
			triggerNote(50); // D is MIDI note 50.
			pitchLabel.setText("D-String");
			break;
		case R.id.g_button:
			triggerNote(55); // G is MIDI note 55.
			pitchLabel.setText("G-String");
			break;
		case R.id.b_button:
			triggerNote(59); // B is MIDI note 59.
			pitchLabel.setText("B-String");
			break;
		case R.id.ee_button:
			triggerNote(64); // E (high) is MIDI note 64.
			pitchLabel.setText("E-String");
			break;
		}
	}
}