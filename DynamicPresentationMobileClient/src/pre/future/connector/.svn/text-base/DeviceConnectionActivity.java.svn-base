package pre.future.connector;

import java.util.regex.Pattern;

import pre.future.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class DeviceConnectionActivity extends Activity  implements OnTouchListener{
	private EditText addressText;
	private EditText credentialText;
	private Button connectButton;
	private Button cancelButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);
		
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
		
		addressText = (EditText)findViewById(R.id.edittext_address);
		credentialText = (EditText)findViewById(R.id.edittextcredential);
		connectButton = (Button)findViewById(R.id.btn_connect);
		connectButton.setOnTouchListener(this);
		cancelButton = (Button)findViewById(R.id.btn_cancel);
		cancelButton.setOnTouchListener(this);
		
		Intent intent = new Intent();
		intent.setAction("xep.ConnectionStatusReceiver");
		intent.putExtra("isUsable", false);
		sendBroadcast(intent);
	}
	@Override  
	protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first)  
	{  
	    super.onApplyThemeResource(theme, resid, first);  
	} 
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_connect:
			if((event.getAction() == MotionEvent.ACTION_DOWN))//터치 이벤트 한번만 발생하도록 처리
			{
				Log.d("DP","Try Connection");		
				//credential 및 address 유효 확인 
				DeviceConnectionManager deviceConnectionManager = DeviceConnectionManager.getInstance();
				
				if(addressText.getText().length()==0)//길이 0일경우
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "IP address must not 0 length" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}}).show();
					return true;
				}
				if(!addressValidate(addressText.getText().toString()))//형식에 어긋나는 경우
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "Invalid address type, you should input valid type of address ex)121.136.38.149" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}}).show();
					return true;
				}
				if(credentialText.getText().length()==0)//길이 0일경우 
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "Credential must not 0 length" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}}).show();
					return true;
				}
				if(!credentialValidate(credentialText.getText().toString()))
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "Invalid credential, you should input valid type of credential ex)1234" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}}).show();
					return true;
				}

				//이후 호출
				WaitDlg dlg = new WaitDlg(DeviceConnectionActivity.this, "Connect to PC Client", "Connecting...");
	            dlg.start();
	            
	            String resultString = deviceConnectionManager.connect(getApplicationContext(),addressText.getText().toString(),credentialText.getText().toString());
	            WaitDlg.stop(dlg);
	            
				if(resultString.equalsIgnoreCase("UnknownHost")||resultString.equalsIgnoreCase("Cannot Connection"))
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "Cannot connect to PC client" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}
					}).show();
				}
				else if(resultString.equalsIgnoreCase("CredentialNot"))
				{
					AlertDialog alert = new AlertDialog.Builder( this )
					.setMessage( "Invalid credential" )
					.setPositiveButton( "OK", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							dialog.dismiss();
						}}).show();
				}
				else if(resultString.equalsIgnoreCase("CredentialOK"))
				{
					finish();
				}
			}
			break;
		case R.id.btn_cancel:
			Log.d("DP","Cancel Connection");
			finish();
			break;
		}
		return true;
	}
	public boolean addressValidate(String address)//형식 어긋나면 어긋나면 무효 
	{
		final Pattern ipAdd= Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
		return ipAdd.matcher(address).matches();
	}
	public boolean credentialValidate(String credential)//문자 포함되어 있으면 무효 
	{
		return Pattern.matches("^[0-9]*$", credential);
	}
}

class WaitDlg extends HandlerThread {
     Context mContext;
     String mTitle;
     String mMsg;
     ProgressDialog mProgress;
    
     WaitDlg(Context context, String title, String msg) {
           super("waitdlg");
           mContext = context;
           mTitle = title;
           mMsg = msg;
          
           setDaemon(true);
     }
    
     protected void onLooperPrepared() {
           mProgress = new ProgressDialog(mContext);
           mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
           mProgress.setTitle(mTitle);
           mProgress.setMessage(mMsg);
           mProgress.setCancelable(false);
           mProgress.show();
     }
    
     // 스레드 외부에서 종료를 위해 호출된다.
     static void stop(WaitDlg dlg) {
           dlg.mProgress.dismiss();
           try { Thread.sleep(100); } catch (InterruptedException e) {;}
           dlg.getLooper().quit();
     }
}
