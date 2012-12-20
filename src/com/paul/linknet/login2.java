package com.paul.linknet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login2 extends Activity {

    EditText un,pw;
	TextView error;
    Button ok;
    String url;
    

    //SessionManagement session = new SessionManagement(getApplicationContext()); 

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        un=(EditText)findViewById(R.id.et_un);
        pw=(EditText)findViewById(R.id.et_pw);
        ok=(Button)findViewById(R.id.btn_login);
        error=(TextView)findViewById(R.id.tv_error);

        ok.setOnClickListener(new View.OnClickListener() {
          SessionManagement session = new SessionManagement(getApplicationContext()); 

            @Override
            public void onClick(View v) {
            	new RetrieveLogin().execute();
            }
        });
        
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), register.class);
                startActivityForResult(myIntent, 0);
            }

        });
        
    }
   
    class RetrieveLogin extends AsyncTask<String, Void, String>{
        private Exception exception;
    	protected String doInBackground(String... urls) {
            try {

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", un.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", pw.getText().toString()));
			    String response = CustomHttpClient.executeHttpPost("http://cloud.cs50.net/~pbowden/linklogin.php", postParameters);
				    return response.trim();
            } catch (Exception e) {
                this.exception = e;
                return e.toString();
            }
        }
        protected void onPostExecute(String result) {
            try { 
                int id = Integer.parseInt(result);
                SessionManagement session = new SessionManagement(getApplicationContext()); 

                session.createLoginSession(id);
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            } catch(NumberFormatException e) { 
    		    Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT).show();
            }       
        }    
    }
}
