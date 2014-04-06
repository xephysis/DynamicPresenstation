package pre.future.mobile.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pre.future.R;
import android.app.ListActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SoundsListActivity extends ListActivity {
	private static final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pre.future/sounds");
	private ArrayList<Song> songs = new ArrayList<Song>();
	private MediaPlayer mp = new MediaPlayer();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        try {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.songlist);
        	updateSongList();
        } catch (NullPointerException e) {
        	Log.v(getString(R.string.app_name), e.getMessage());
        }
    }
    
    public void updateSongList() {
    	File home = new File(MEDIA_PATH);
		if (home.listFiles( new SoundFilter()).length > 0) {
    		for (File file : home.listFiles( new SoundFilter())) {
    			Song s = new Song(file.getName(), R.drawable.sound_icon);
    			songs.add(s);
    		}
		
    		SongAdapter songList = new SongAdapter(this, R.layout.list_row, songs);
    		setListAdapter(songList);
		}    	
    }
    
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			mp.reset();
			mp.setDataSource(MEDIA_PATH + "/" + songs.get(position).getName());
			mp.prepare();
			mp.start();
		} catch(IOException e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		} 
	}
	
	private class SongAdapter extends ArrayAdapter<Song> {

		private ArrayList<Song> items;

		public SongAdapter(Context context, int textViewResourceId, ArrayList<Song> items) {
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
			Song p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.listTitle);
				ImageView bt = (ImageView) v.findViewById(R.id.listImage);
				if (tt != null){
					tt.setText(p.getName());                            
				}
				if(bt != null){
					bt.setImageResource(R.drawable.sound_icon);
				}
			}
			return v;
		}
	}
}

class SoundFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".3gp"));
    }
}

class Song {
    
    private String Name;
    private int image;
    
    public Song(String _Name, int _image){
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
