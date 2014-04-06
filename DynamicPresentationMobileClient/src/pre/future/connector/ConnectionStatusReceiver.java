package pre.future.connector;

import pre.future.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ConnectionStatusReceiver extends BroadcastReceiver {

	static final int DPNOTI = 1;
	NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Notification notification;
		Boolean isStable;
		Bundle bundle = intent.getExtras();
		isStable = bundle.getBoolean("isUsable");
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(), 0);

		if (isStable) {
			notification = new Notification(R.drawable.green, "Usable Service",
					System.currentTimeMillis());
			notification.setLatestEventInfo(context, "DynamicPresentation",
					"Usable Service", pendingIntent);
		} else {
			notification = new Notification(R.drawable.red, "Unusable Service",
					System.currentTimeMillis());
			notification.setLatestEventInfo(context, "DynamicPresentation",
					"Unusable Service", pendingIntent);
		}
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}
}
