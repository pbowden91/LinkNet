package com.paul.linknet;


import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View.OnClickListener;


import com.paul.linknet.R;
import com.paul.linknet.R.id;
import com.paul.linknet.R.layout;
import com.paul.linknet.R.menu;
import com.paul.linknet.R.string;
import com.example.filenet.utils.FileChooserActivity;
import com.example.filenet.utils.FileListAdapter;
import com.example.filenet.utils.FileListFragment;
import com.example.filenet.utils.FileLoader;
import com.example.filenet.utils.FileUtils;
import com.example.filenet.utils.MimeTypeParser;
import com.example.filenet.utils.MimeTypes;

public class Activity2 extends Activity {
	private static final int REQUEST_CODE = 6384; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	  // This method is called at button click because we assigned the name to the
	  // "OnClick property" of the button
	  public void buttonClick(View view) {
	    switch (view.getId()) {
	    case R.id.button1:
	      Button button1 = (Button) findViewById(R.id.button1);
	      if (button1.getText() == "Turn On File Sharing") {
	    	button1.setText("Turn Off File Sharing");
	        Toast.makeText(this, "Turning on file sharing...",
	            Toast.LENGTH_LONG).show();
	      }
	      
	      else{
	        button1.setText("Turn On File Sharing");
	        Toast.makeText(this, "Turning off file sharing...",
		            Toast.LENGTH_LONG).show();
	      }
	      break;
//	    case R.id.button2:
	//      showChooser();
	  //    break;
	    }
	  
	  }
	  
	  private void showChooser() {
			// Use the GET_CONTENT intent from the utility class
			Intent target = FileUtils.createGetContentIntent();
			// Create the chooser Intent
			Intent intent = Intent.createChooser(
					target, getString(R.string.chooser_title));
			try {
				startActivityForResult(intent, REQUEST_CODE);
			} catch (ActivityNotFoundException e) {
				// The reason for the existence of aFileChooser
			}				
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
			case REQUEST_CODE:	
				// If the file selection was successful
				if (resultCode == RESULT_OK) {		
					if (data != null) {
						// Get the URI of the selected file
						final Uri uri = data.getData();

						try {
							// Create a file instance from the URI
							final File file = FileUtils.getFile(uri);
							Toast.makeText(Activity2.this, 
									"File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							Log.e("FileSelectorTestActivity", "File select error", e);
						}
					}
				} 
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
}
}
