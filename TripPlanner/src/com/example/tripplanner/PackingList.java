//This is for the pack list activity

package com.example.tripplanner;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

public class PackingList extends Activity implements OnClickListener, OnItemClickListener, OnInitListener {

	private EditText input;
	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView lvItems;
	private ImageView img = null;
	private int itemsId;
	private TextToSpeech speaker;
	private static final String tag = "Widgets";
	//private SQLHelper helper;
	//private Trip trip;
	//private PackList list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.packing_list);

		input = (EditText)findViewById(R.id.editText);
		lvItems = (ListView)findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		itemsAdapter = new ArrayAdapter<String>(
			this,
			android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		lvItems.setOnItemClickListener(this);
		
		img = (ImageView)findViewById(R.id.image);
		img.setImageResource(R.drawable.packinglist);
		
		// Initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);
        // Create object of SQLHelper class
        //helper = new SQLHelper(this);
        //trip = helper.getNewestTrip();
		//list = trip.getPackList();
       
        Button add = (Button)findViewById(R.id.button_add);
        add.setOnClickListener(this);
       
	}
	
	// Speaks the contents of output
	@SuppressWarnings("deprecation")
	public void speak(String output){
		speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);
	}
		
	// Implements TextToSpeech.OnInitListener
	@Override
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US English.
		    // If a language is not be available, the result will indicate it.
		    int result = speaker.setLanguage(Locale.US);
		            
		    if (result == TextToSpeech.LANG_MISSING_DATA ||
		        result == TextToSpeech.LANG_NOT_SUPPORTED) {
	        // Language data is missing or the language is not supported.
		    Log.e(tag, "Language is not available.");
		    } else {
	        // The TTS engine has been successfully initialized
		    Log.i(tag, "TTS Initialization successful.");
		    }
	    } else {
	        // Initialization failed.
	    	Log.e(tag, "Could not initialize TextToSpeech.");	
		}
	}

	// On destroy
	public void onDestroy(){
	    
	// Shut down TTS engine
		if(speaker != null){
		   speaker.stop();
		   speaker.shutdown();
	    }
	   	super.onDestroy();
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.packinglist_menu, menu);
	    return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String text = items.get(position);
		itemsId = items.indexOf(text);
		input.setText(text);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {					
			case R.id.deleteEntry:
				String deleteEntry = input.getText().toString();
				items.remove(deleteEntry);
				itemsAdapter.notifyDataSetChanged();
				input.setText("");
				
				// If speaker is talking, stop it, and speak deleteEntry deleted
				if (speaker.isSpeaking()) {
					Log.i(tag, "Speaker speaking");
					speaker.stop();
					speak(deleteEntry + "deleted");
				} else {
					Log.i(tag, "Speaker is ready");
					speak(deleteEntry + "deleted");
				}
				return true;
				
			case R.id.updateEntry:
				String updateEntry = input.getText().toString();
				items.set(itemsId, updateEntry);    // ArrayList.set(index, object) => Can directly update the selected item using itemsId
				itemsAdapter.notifyDataSetChanged();
				input.setText("");
				return true;	
			
			default:
	            return super.onOptionsItemSelected(item);
		}
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_add:
			String newItem = input.getText().toString();
			// Two ArrayList both add the newItem
			items.add(newItem);
			itemsAdapter.notifyDataSetChanged();
			input.setText("");
			
			// If speaker is talking, stop it, and speak newItem added
			if (speaker.isSpeaking()) {
				Log.i(tag, "Speaker speaking");
				speaker.stop();
				speak(newItem + "added");
			} else {
				Log.i(tag, "Speaker is ready");
				speak(newItem + "added");
			}
			break;
		}
	}
    
/*    public void addItem(String s){
    	list.add(new TripListItem(list,s));
    	items.add(s);
    }
    
    public void saveList(){
    	this.helper.saveList(list);
    }
    
    public void checkItem(TripListItem item){
    	item.setChecked();
    }
    
    public void unCheckItem(TripListItem item){
    	item.setUnChecked();
    }
*/
    
	/***
	 * INSERT CODE HERE. The code here will populate the list view widget. Use
	 * the methods in the helper class to load the list from the database.
	 */
}
