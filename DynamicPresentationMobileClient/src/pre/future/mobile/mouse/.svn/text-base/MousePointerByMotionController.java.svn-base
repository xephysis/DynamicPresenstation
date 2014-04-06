package pre.future.mobile.mouse;

import pre.future.connector.DeviceConnectionManager;
import android.hardware.SensorEvent;
import android.util.Log;

public class MousePointerByMotionController {
	public void motionMoveAction(int prevXpos, int prevYpos) {
		Log.i("Check Log", "motion mouse action");
		
		//DeviceConnectionManager.getInstance().sendMouse(prevXpos, prevYpos);
		DeviceConnectionManager.getInstance().sendMouseByMotion(prevXpos, prevYpos);
		}
}
