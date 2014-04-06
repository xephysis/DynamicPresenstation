package pre.future.mobile.sound;

import android.app.Activity;
import android.media.MediaPlayer;

public class RecordedSoundPlayController  extends Activity{
	//좆망. 버리는 코드임.
	// SoundListActivity에서 모두 처리.
	private MediaPlayer player = new MediaPlayer();
 
	public void initPlayerController(String soundFilePath) {
		try {
			// init player
			player.setDataSource(soundFilePath);
		} catch (Exception e) {
		}
	}
 
	public void startPlay() {
		try {
			player.prepare();
			player.start();
		} catch (Exception e) {
		}
	}
 
	public void stopPlay() {
		player.stop();
		player.reset();
		player.release();
	}
}