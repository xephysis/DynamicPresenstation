package pre.future.mobile;

import java.io.File;

import pre.future.R;
import pre.future.connector.DeviceConnectionActivity;
import pre.future.connector.DeviceConnectionManager;
import pre.future.mobile.file.DocumentsListActivity;
import pre.future.mobile.file.SoundsListActivity;
import pre.future.mobile.keyboard.KeyboardActionController;
import pre.future.mobile.mouse.MouseClickController;
import pre.future.mobile.mouse.MousePointerByMotionController;
import pre.future.mobile.mouse.MousePointerByTouchpadController;
import pre.future.mobile.sound.SoundRecordController;
import pre.future.mobile.sound.TransmitSoundController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MobileClientActivity extends Activity implements OnClickListener,
		OnTouchListener, SensorListener {
	private boolean motion_onoff;

	public static int count = 0;
	public float deltaX = 0.0f;

	private float mLastX, mLastY;
	private boolean mInitialized = false;

	public static float prevXpos_touch;
	public static float prevYpos_touch;

	private SensorManager sm;

	private Button connectionButton;
	private Button documentslistButton;
	private Button soundslistButton;

	private ToggleButton selfRecordToggle;
	private ToggleButton transmissionToggle;

	private TextView motion_text;
	private Button change_mouse_button;
	private Button leftclick_Button;
	private Button rightclick_Button;
	private View touchpad_View;

	private Button leftkey_Button;
	private Button rightkey_Button;
	private Button slide_start_Button;
	private Button slide_end_Button;

	private KeyboardActionController keyboardCon;
	private MousePointerByMotionController motionCon;
	private MousePointerByTouchpadController touchpadCon;
	private MouseClickController mouseClickCon;
	private SoundRecordController soundRecordCon;
	private TransmitSoundController transmitSoundController;
	private DeviceConnectionManager deviceConnectionManager;

	private String docuDirPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/pre.future/documents";
	private String soundDirPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/pre.future/sounds";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Heejoon Added
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);

		final LinearLayout layout = (LinearLayout) findViewById(R.id.mainlayout);

		keyboardCon = new KeyboardActionController();
		mouseClickCon = new MouseClickController();
		touchpadCon = new MousePointerByTouchpadController();
		motionCon = new MousePointerByMotionController();
		soundRecordCon = new SoundRecordController();
		transmitSoundController = TransmitSoundController.getInstance();
		deviceConnectionManager = DeviceConnectionManager.getInstance();

		documentslistButton = (Button) findViewById(R.id.documentslist_button);
		documentslistButton.setOnTouchListener(this);
		soundslistButton = (Button) findViewById(R.id.soundslist_button);
		soundslistButton.setOnTouchListener(this);

		selfRecordToggle = (ToggleButton) findViewById(R.id.selfrecord_toggle);
		selfRecordToggle.setOnClickListener(this);
		transmissionToggle = (ToggleButton) findViewById(R.id.transmit_pc_toggle);
		transmissionToggle.setOnClickListener(this);

		sm = (SensorManager) getSystemService(SENSOR_SERVICE); // SensorManager
																// 인스턴스를 가져옴

		motion_text = (TextView) findViewById(R.id.motion_text);
		motion_text.setVisibility(View.INVISIBLE);
		setMotionMouse(false);
		change_mouse_button = (Button) findViewById(R.id.change_mouse_type);
		change_mouse_button.setOnClickListener(this);

		leftclick_Button = (Button) findViewById(R.id.leftclick_button);
		leftclick_Button.setOnClickListener(this);
		rightclick_Button = (Button) findViewById(R.id.rightclick_button);
		rightclick_Button.setOnClickListener(this);
		touchpad_View = (View) findViewById(R.id.touchpad_view);
		touchpad_View.setOnTouchListener(this);

		leftkey_Button = (Button) findViewById(R.id.leftkey_button);
		leftkey_Button.setOnClickListener(this);
		rightkey_Button = (Button) findViewById(R.id.rightkey_button);
		rightkey_Button.setOnClickListener(this);
		slide_start_Button = (Button) findViewById(R.id.slide_start);
		slide_start_Button.setOnClickListener(this);
		slide_end_Button = (Button) findViewById(R.id.slide_end);
		slide_end_Button.setOnClickListener(this);

		// 희준
		connectionButton = (Button) findViewById(R.id.btn_connecttry);
		connectionButton.setOnClickListener(this);

		makeFileDirs();
	}

	public void showConnectionAlert() {
		new AlertDialog.Builder(this).setTitle("Connection Error")
				.setMessage("PC와 연결을 먼저 해주세요.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				}).show();
	}

	public void setMotionMouse(boolean onoff) {
		motion_onoff = onoff;

		if (motion_onoff == true)
			motion_text.setVisibility(View.VISIBLE);
		else
			motion_text.setVisibility(View.INVISIBLE);
	}

	// Documents, Sounds 폴더 생성
	public void makeFileDirs() {
		File docufile = new File(docuDirPath);
		File soundfile = new File(soundDirPath);

		if (!docufile.exists()) {
			docufile.mkdirs();
			Toast.makeText(this, docuDirPath, Toast.LENGTH_SHORT).show();
		}

		if (!soundfile.exists()) {
			soundfile.mkdirs();
			Toast.makeText(this, soundDirPath, Toast.LENGTH_SHORT).show();
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.documentslist_button://파일 전송 액티비티 호출
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent(this, DocumentsListActivity.class);
				startActivity(intent);
				
				return true;
			}
			break;

		case R.id.soundslist_button://녹음된 파일 목록 액티비티 호출
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent(this, SoundsListActivity.class);
				startActivity(intent);

				return true;
			}
			break;

		case R.id.touchpad_view:
			if (motion_onoff == false) { //터치패드에 의한 마우스 이동
				if (deviceConnectionManager.getConnectionStatus() == true) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						prevXpos_touch = event.getX();
						prevYpos_touch = event.getY();
						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						touchpadCon.touchpadMoveAction((int)(event.getX()-prevXpos_touch),(int)(event.getY()-prevYpos_touch));
						prevXpos_touch = event.getX();
						prevYpos_touch = event.getY();
						return true;
					}
				} else {
					showConnectionAlert();
				}
			} else {//모션에 의한 마우스 이동 
				if (deviceConnectionManager.getConnectionStatus() == true) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						sm.registerListener(this,
								SensorManager.SENSOR_MAGNETIC_FIELD
										| SensorManager.SENSOR_ACCELEROMETER,
								SensorManager.SENSOR_DELAY_GAME);

						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						sm.unregisterListener(this); // 리스너제거
						mInitialized = false;
						
						return true;
					}
				} else {
					showConnectionAlert();
				}
			}

			break;
		}
		return false;
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_connecttry:
			// 희준 : 여기서 액티비티 호출
			Intent intent = new Intent(this, DeviceConnectionActivity.class);
			startActivity(intent);
			break;

		case R.id.selfrecord_toggle:
			if (selfRecordToggle.isChecked()) {
				selfRecordToggle.setChecked(true);
				transmissionToggle.setChecked(false);

				if (transmitSoundController.getTransmitSoundStatus() == true)
					transmitSoundController.transferVoicePause();

				selfRecordToggle.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.record));
				soundRecordCon.initRecordController(soundDirPath);
				soundRecordCon.startRecord();
			} else {
				selfRecordToggle.setChecked(false);
				selfRecordToggle.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.record_off));
				soundRecordCon.stopRecord();
			}
			break;

		case R.id.transmit_pc_toggle:
			if (deviceConnectionManager.getConnectionStatus() == true) {
				// 희준 / 태선-예외처리, 레코드동작 멈춤.
				if (transmissionToggle.isChecked()) {
					selfRecordToggle.setChecked(false);
					transmissionToggle.setChecked(true);

					if (soundRecordCon.getRecordStatus() == true)
						soundRecordCon.stopRecord();

					transmitSoundController.transferVoiceResume();
					transmissionToggle.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.transmit));
				} else {
					transmissionToggle.setChecked(false);
					transmitSoundController.transferVoicePause();
					transmissionToggle.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.trans_off));
				}
			} else {
				showConnectionAlert();
				transmissionToggle.setChecked(false);
			}
			break;

		case R.id.change_mouse_type:
			// 마우스 전환.
			if (motion_onoff == true)
				setMotionMouse(false);
			else
				setMotionMouse(true);
			break;

		case R.id.leftclick_button:
			if (deviceConnectionManager.getConnectionStatus() == true)
				mouseClickCon.leftClickAction();
			else
				showConnectionAlert();
			break;

		case R.id.rightclick_button:
			if (deviceConnectionManager.getConnectionStatus() == true)
				mouseClickCon.rightClickAction();
			else
				showConnectionAlert();
			break;

		case R.id.leftkey_button:
			if (deviceConnectionManager.getConnectionStatus() == true)
				keyboardCon.leftAction();
			else
				showConnectionAlert();
			break;

		case R.id.rightkey_button:
			if (deviceConnectionManager.getConnectionStatus() == true)
				keyboardCon.rightAction();
			else
				showConnectionAlert();
			break;
			
		case R.id.slide_start:
			if (deviceConnectionManager.getConnectionStatus() == true)
				keyboardCon.f5Actioin();
			else
				showConnectionAlert();
			break;
			
		case R.id.slide_end:
			if (deviceConnectionManager.getConnectionStatus() == true)
				keyboardCon.escAction();
			else
				showConnectionAlert();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// sm.registerListener(acc, accSensor, SensorManager.SENSOR_DELAY_GAME);
		// // 가속도 센서 리스너 오브젝트를 등록
	}

	@Override
	public void onPause() {
		super.onPause();
		sm.unregisterListener(this); // 리스너제거
	}

	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public void onSensorChanged(int sensor, float[] values) {

		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
			mLastX = values[0];
			mLastY = values[1];
			
			motionCon.motionMoveAction(Math.round(mLastX), Math.round(mLastY));
		}

//		자기장센서 
//		if (sensor == SensorManager.SENSOR_MAGNETIC_FIELD) {
//			float x = values[0];
//
//			if (mInitialized == false) {
//				mLastX = x;
//				deltaX = 0.0f;
//				mInitialized = true;
//			} else {
//				deltaX = mLastX - x;
//			}
//		}
//
//		motionCon.motionMoveAction(Math.round(-(deltaX)), Math.round(mLastY));
	}
}