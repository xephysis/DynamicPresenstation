package pre.future.mobile.sound;

import java.net.SocketException;

import pre.future.connector.DeviceConnectionManager;
import android.R.string;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.os.StrictMode;
import android.util.Log;

public class TransmitSoundController {
	static AudioGroup audioGroup;

	private boolean transmitStatus = false;

	private static TransmitSoundController instance;

	public static TransmitSoundController getInstance() {
		if (instance == null) {
			instance = new TransmitSoundController();
		}

		return instance;
	}

	public boolean getTransmitSoundStatus() {
		return transmitStatus;
	}

	public void transferVoiceInit()//음성 전송 시작
	{
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

					AudioStream audioStream = new AudioStream(DeviceConnectionManager
					.getInstance().getLocalIpAddress());
			audioGroup = new AudioGroup();
			audioStream.associate(
					DeviceConnectionManager.getInstance().clientSocket
							.getInetAddress(), 10001);
			audioStream.setMode(AudioStream.MODE_NORMAL);
			audioStream.setCodec(AudioCodec.PCMU);
			audioStream.join(audioGroup);
			audioGroup.setMode(AudioGroup.MODE_ON_HOLD);
			TalkSpurtThread talkSpurtThread = new TalkSpurtThread();
			talkSpurtThread.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void transferVoiceResume() {
		audioGroup.setMode(AudioGroup.MODE_NORMAL);
		transmitStatus = true;
	}

	public void transferVoicePause()// 이 함수 콜해서 세션 클로즈 시작
	{
		audioGroup.setMode(AudioGroup.MODE_ON_HOLD);
		transmitStatus = false;
	}

	// 이하 함수는 일단 남겨놓았습니다.
	@Deprecated
	public void loudspeaker() {
		Log.i("Check Log", "loudspeaker_toggle ");
	}

	// 안쓰는 함수
	@Deprecated
	public void transmitToPC() {
		Log.i("Check Log", "transmit_pc_toggle ");
	}

	// 안쓰는 함수
	@Deprecated
	public void recordAction() {
		Log.i("Check Log", "record ");
	}

	public class TalkSpurtThread extends Thread {
		public void run() {
			audioGroup.sendDtmf(1);
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
