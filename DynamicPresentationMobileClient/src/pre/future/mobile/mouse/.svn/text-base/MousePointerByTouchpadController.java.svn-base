package pre.future.mobile.mouse;

import pre.future.connector.DeviceConnectionManager;
import android.util.Log;
import android.view.MotionEvent;

public class MousePointerByTouchpadController {

	public void touchpadMoveAction(MotionEvent event, float prevXpos, float prevYpos) {
		//Log.i("Check Log", "touchpad action");
		
		DeviceConnectionManager cc = DeviceConnectionManager.getInstance();
		//cc.sendMouse(Math.round((event.getX()-prevXpos)), Math.round((event.getY()-prevYpos)));
		cc.sendMouse((int)((event.getX()-prevXpos)/1), (int)((event.getY()-prevYpos))/1);
	}
	public void touchpadMoveAction(int x, int y)
	{
		DeviceConnectionManager cc = DeviceConnectionManager.getInstance();
		//cc.sendMouse(Math.round((event.getX()-prevXpos)), Math.round((event.getY()-prevYpos)));
		cc.sendMouse(x,y);
	
	}
}