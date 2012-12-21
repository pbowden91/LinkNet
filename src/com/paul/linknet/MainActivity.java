package com.paul.linknet;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.filenet.utils.FileUtils;
import com.paul.linknet.ItemDetails;
import com.paul.linknet.ItemListBaseAdapter;
import com.paul.linknet.login2.RetrieveLogin;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static final int REQUEST_CODE = 9; 
	private int mRowCount = 0;
	sqllite db = new sqllite(this);
	int uid;
	boolean state;
	String saveFile;

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
	ViewPager mViewPager;
//    SessionManagement session = new SessionManagement(getApplicationContext());
	//Context context = getApplicationContext();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.logout:
	        	SessionManagement manager = new SessionManagement(MainActivity.this);
	            manager.logoutUser();
	            break;
	    }
        return true;

	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SessionManagement manager = new SessionManagement(MainActivity.this);
		
		manager.checkLogin();
		uid = manager.getUserDetails(); 
		state = manager.getState();
		saveFile = manager.getShare();
		
        EditText name = (EditText) findViewById(R.id.editText1);
        name.setText(saveFile);
		
        if(state)
        {
            Button button1 = (Button) findViewById(R.id.button1);
        	button1.performClick();
        }
        
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Host Settings");

		TabSpec spec2=tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("Download Files");
		spec2.setContent(R.id.tab2);

		TabSpec spec3=tabHost.newTabSpec("Tab 3");
		spec3.setIndicator("Set Permissions");
		spec3.setContent(R.id.tab3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);		

        ArrayList<ItemDetails> image_details = GetSearchResults();
        
        final ListView lv1 = (ListView) findViewById(R.id.lv_country);
        lv1.setAdapter(new ItemListBaseAdapter(this, image_details));
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object o = lv1.getItemAtPosition(position);
            	final ItemDetails obj_itemDetails = (ItemDetails)o;
                new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Do you really want to delete " + obj_itemDetails.getName() +  "'s permission for file(s) at " + obj_itemDetails.getItemDescription() + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Stop the activity
                        permission p = new permission(uid, obj_itemDetails.getName(), obj_itemDetails.getItemDescription(), 0);
                        db.deletePermission(p);
                    }

                })
                .setNegativeButton("No", null)
                .show();
        	}  
        });
		}
	
	private ArrayList<ItemDetails> GetSearchResults(){
    	ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
    
    	List<permission> permissionList = db.getAllPermissions(uid);
//    	
    	for (permission p: permissionList)
    	{
           
        	ItemDetails item_details = new ItemDetails();
        	item_details.setName(p.getName());
        	item_details.setItemDescription(p.getFile());
        	Log.d("Expires:", Integer.toString(p.getExpiration()));
        	if(p.getExpiration() != 0)
        	   // item_details.setPrice(p.getExpiration() + " hours");
        		item_details.setPrice("NEVER EXPIRES");
        	else
        		item_details.setPrice("NEVER EXPIRES");
        	item_details.setPrice(p.getExpiration() + " hours");
        	results.add(item_details);
    	}

    	return results;
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
    SessionManagement manager = new SessionManagement(MainActivity.this);

  switch (view.getId()) {
  case R.id.button1:
    Button button1 = (Button) findViewById(R.id.button1);
    //Toast.makeText(this, button1.getText(), Toast.LENGTH_LONG).show();
      if (button1.getText().toString() == "Turn Off File Sharing") {
          button1.setText("Turn On File Sharing");
          Toast.makeText(this, "Turning off file sharing...",
    	            Toast.LENGTH_LONG).show();
          sharingOff();
          manager.saveState(false);
      }
    
    else{
 		button1.setText("Turn Off File Sharing");
        Toast.makeText(this, "Turning on file sharing...",
        Toast.LENGTH_LONG).show();
        sharingOn();
        manager.saveState(true);

    }
    break;
    case R.id.button2:
          showChooser(100);
          break;
    case R.id.button3:
          showChooser(100);
          break;
    case R.id.button4:
    	showChooser(101);
    	break;
    case R.id.button5:
        EditText name,file;
        name = (EditText) findViewById(R.id.editText3);
        file = (EditText) findViewById(R.id.editText4);
    	permission p = new permission(uid, name.getText().toString(), file.getText().toString(), 0);
    	name.setText("");
    	file.setText("");
    	db.addPermission(p);
    	break;
  }

}

public void sharingOn()
{
	new setStateOn().execute();
}
public void sharingOff()
{
	new setStateOff().execute();
}


private void showChooser(int request) {
		// Use the GET_CONTENT intent from the utility class
		Intent target = FileUtils.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(
				target, getString(R.string.chooser_title));
		try {
			startActivityForResult(intent, request);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
		}				
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:	
			// If the file selection was successful
			if (resultCode == RESULT_OK) {		
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();

					try {
						// Create a file instance from the URI
						final File file = FileUtils.getFile(uri);
				        EditText name;
				        name = (EditText) findViewById(R.id.editText1);
				        name.setText(uri.toString());
				        SessionManagement manager = new SessionManagement(MainActivity.this);
				        manager.saveShare(uri.toString());
						// Toast.makeText(MainActivity.this, 
							//	"File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			} 
			break;
		
	case 101:	
		// If the file selection was successful
		if (resultCode == RESULT_OK) {		
			if (data != null) {
				// Get the URI of the selected file
				final Uri uri = data.getData();

				try {
					// Create a file instance from the URI
					final File file = FileUtils.getFile(uri);
			        EditText name;
			        name = (EditText) findViewById(R.id.editText4);
			        name.setText(uri.toString());
					// Toast.makeText(MainActivity.this, 
						//	"File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Log.e("FileSelectorTestActivity", "File select error", e);
				}
			}
		} 
		break;
		}
		super.onActivityResult(requestCode, resultCode, data);
}

	// poor design, don't care, don't feel like figuring out the arguments
    class setStateOn extends AsyncTask<String, Void, String>{
        private Exception exception;
    	protected String doInBackground(String... urls) {
            try {
            	String ip = getLocalIpAddress();
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("id", Integer.toString(uid)));
				postParameters.add(new BasicNameValuePair("state", "1"));
				postParameters.add(new BasicNameValuePair("ip", ip));

				String response = CustomHttpClient.executeHttpPost("http://cloud.cs50.net/~pbowden/linkshare.php", postParameters);
				    return response.trim();
            } catch (Exception e) {
                this.exception = e;
                return e.toString();
            }
        }
        protected void onPostExecute(String result) {
          //  try { 
            //    int id = Integer.parseInt(result);
              //  SessionManagement session = new SessionManagement(getApplicationContext()); 

//                session.createLoginSession(id);
  //              Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
    //            startActivityForResult(myIntent, 0);
      //      } catch(NumberFormatException e) { 
    		    Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT).show();
        //    }       
        }     
    }
    public String getLocalIpAddress()
    {
//    	String ipv4;
//            try {
//                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                    NetworkInterface intf = en.nextElement();
//                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                        if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())){
//                            return inetAddress.getHostAddress().toString();
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//                Log.e("IP Address", ex.toString());
//            }
//            return null;
    	
    		try {
				Document doc = Jsoup.connect("http://automation.whatismyip.com/n09230945.asp").get();
				return doc.select("body").html().toString() + ":53855";
    		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    		
        }
    class setStateOff extends AsyncTask<String, Void, String>{
        private Exception exception;
    	protected String doInBackground(String... urls) {
            try {

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("id", Integer.toString(uid)));
				postParameters.add(new BasicNameValuePair("state", "0"));
				postParameters.add(new BasicNameValuePair("ip", ""));

				String response = CustomHttpClient.executeHttpPost("http://cloud.cs50.net/~pbowden/linkshare.php", postParameters);
				    return response.trim();
            } catch (Exception e) {
                this.exception = e;
                return e.toString();
            }
        }
        protected void onPostExecute(String result) {
          //  try { 
            //    int id = Integer.parseInt(result);
              //  SessionManagement session = new SessionManagement(getApplicationContext()); 

//                session.createLoginSession(id);
  //              Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
    //            startActivityForResult(myIntent, 0);
      //      } catch(NumberFormatException e) { 
    		    Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT).show();
        //    }       
        }    
    }
	
}
