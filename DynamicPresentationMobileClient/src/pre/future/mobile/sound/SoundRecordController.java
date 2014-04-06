package pre.future.mobile.sound;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.media.MediaRecorder;

public class SoundRecordController extends Activity{
	private MediaRecorder recorder;
	
	private boolean recordStatus = false;
	
	private File outfile = null;
 
	public boolean getRecordStatus() {
		return recordStatus;
	}
	
	public void initRecordController(String savePath) {
		try {
			// the soundfile
			File storageDir = new File(savePath);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(System.currentTimeMillis());
			String time = dateFormat.format(date);
			
			outfile = File.createTempFile(time, ".3gp", storageDir);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public void startRecord() {
		try {
			recorder = new MediaRecorder();
			
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(outfile.getAbsolutePath());
			recorder.prepare();
			recorder.start();
			recordStatus = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public void stopRecord() {
		recorder.stop();
		recorder.reset();
		recorder.release();
		recordStatus = false;
	}
}