package pre.future.mobile.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pre.future.R;
import pre.future.connector.DeviceConnectionManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DocumentsListActivity extends ListActivity {
	private static final String DOCU_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pre.future/documents");
	private ArrayList<Docu> docs = new ArrayList<Docu>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.songlist);
			updateDocuList();
		} catch (NullPointerException e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		}
	}

	public void updateDocuList() {
		File home = new File(DOCU_PATH);
		if (home.listFiles().length > 0) {
			for (File file : home.listFiles()) {
				Docu doc = new Docu(file.getName(), R.drawable.file_icon);
				docs.add(doc);
			}

			DocuAdapter docuList = new DocuAdapter(this,R.layout.list_row,docs);
			setListAdapter(docuList);
		}    	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			if (DeviceConnectionManager.getInstance().getConnectionStatus() == true) {
				DocumentTransferController dtc = new DocumentTransferController();
				Log.d("test", DOCU_PATH + "/" + docs.get(position).getName());
				WaitDlg waitDlg = new WaitDlg(DocumentsListActivity.this, "Transfer file to PC", "Transfering...");
	            waitDlg.start();
	            dtc.sendFile(DOCU_PATH + "/" + docs.get(position).getName());//이 함수 전후로 엑티비티 띄워야함
	            WaitDlg.stop(waitDlg);
	            finish();
			} else {
				showConnectionAlert();
			}

		} catch(Exception e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		} 
	}

	public void showConnectionAlert() {
		new AlertDialog.Builder(this)
		.setTitle("Connection Error")
		.setMessage("PC와 연결을 먼저 해주세요.")
		.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
			}
		}
				).show();
	}
	
	private class DocuAdapter extends ArrayAdapter<Docu> {

		private ArrayList<Docu> items;

		public DocuAdapter(Context context, int textViewResourceId, ArrayList<Docu> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_row, null);
			}
			Docu p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.listTitle);
				ImageView bt = (ImageView) v.findViewById(R.id.listImage);
				if (tt != null){
					tt.setText(p.getName());                            
				}
				if(bt != null){
					bt.setImageResource(R.drawable.file_icon);
				}
			}
			return v;
		}
	}
}

class Docu {
    
    private String Name;
    private int image;
    
    public Docu(String _Name, int _image){
    	this.Name = _Name;
    	this.image = _image;
    }
    
    public String getName() {
        return Name;
    }

    public int getImage() {
        return image;
    }
}


//* HendlerThread로부터 상속
//루퍼를 가지고있는 쓰레드입니다.
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
  
   protected void onLooperPrepared() {//기존의 run메소드가 아니라 onLooperPrepared()로 처리합니다.
         //프로그래스 대화상자를 만드는 부분
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
         // 대화상자가 사라지기 전까지 대기해 줘야 함
         try { Thread.sleep(100); } catch (InterruptedException e) {;}
         // 메시지 루프 종료해야 함
         dlg.getLooper().quit();//루퍼를 종료할때는 getLooper로 불러와서 quit로 종료하면됩니다.
   }
}
