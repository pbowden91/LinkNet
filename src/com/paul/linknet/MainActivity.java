package com.paul.linknet;

import java.io.File;

import com.example.filenet.utils.FileUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static final int REQUEST_CODE = 6384; 

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	//ViewPager mViewPager;
//    SessionManagement session = new SessionManagement(getApplicationContext());
	//Context context = getApplicationContext();
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Tab 1");

		TabSpec spec2=tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("Tab 2");
		spec2.setContent(R.id.tab2);

		TabSpec spec3=tabHost.newTabSpec("Tab 3");
		spec3.setIndicator("Tab 3");
		spec3.setContent(R.id.tab3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		}
@Override
public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	
}
@Override
public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	
}
@Override
public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	// TODO Auto-generated method stub
	
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
    case R.id.button2:
          showChooser();
    break;
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
						Toast.makeText(MainActivity.this, 
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
