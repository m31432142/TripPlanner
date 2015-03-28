package com.example.tripplanner;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewTrip extends Activity implements OnClickListener{

	//Declare variables for widgets
	private EditText tripName;
	private EditText destination;
	private EditText startDate;
	private EditText endDate;
	private Button btnCancel;
	private Button btnSave;
	private Button btnPackingList;
	private Button btnToDoList;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_trip);		
		
		//Link Java variables with xml widgets
		//Set OnClickListener on Buttons
		tripName = (EditText) findViewById(R.id.inputName);
		destination = (EditText) findViewById(R.id.inputDestination);
		startDate = (EditText) findViewById(R.id.startDate);
		endDate = (EditText) findViewById(R.id.endDate);
		btnCancel = (Button) findViewById(R.id.btnCancelNT);
		btnCancel.setOnClickListener(this);
		btnSave = (Button) findViewById(R.id.btnSaveNT);
		btnSave.setOnClickListener(this);
		btnPackingList = (Button) findViewById(R.id.btnPackingListNT);
		btnPackingList.setOnClickListener(this);
		btnToDoList = (Button) findViewById(R.id.btnToDoListNT);
		btnToDoList.setOnClickListener(this);	
   }
	
	public void onClick(View v){
		//Currently test code to make sure buttons are working. 
		switch(v.getId()){
		
		//If Cancel Button is clicked. 
		case R.id.btnCancelNT:
			Toast.makeText(this,"You clicked the " + 
					btnCancel.getText().toString()+ " Button.",Toast.LENGTH_SHORT)
			.show();
			break;
		
		//If Save Button is clicked. 
		case R.id.btnSaveNT:
			Toast.makeText(this,"You clicked the " + 
					btnSave.getText().toString()+ " Button.",Toast.LENGTH_SHORT)
			.show();
			break;
		
		//If Packing List Button is clicked. 
		case R.id.btnPackingListNT:
			startActivity(new Intent (this,PackingList.class));
			break;
		
		//If To Do List Button is clicked.
		case R.id.btnToDoListNT:
			Toast.makeText(this,"You clicked the " + 
					btnToDoList.getText().toString()+ " Button.",Toast.LENGTH_SHORT)
			.show();
			break;		
		}
		
	}
}
