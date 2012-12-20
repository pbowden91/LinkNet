package com.paul.linknet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Context;
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

            @Override
            public void onClick(View v) {
            	new RetrieveLogin().execute();
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
				//String valid = "1";
				//String response = null;
			    String response = CustomHttpClient.executeHttpPost("http://cloud.cs50.net/~pbowden/linklogin.php", postParameters);
				    return response;
            } catch (Exception e) {
                this.exception = e;
			    //un.setText("heytest");
                return e.toString();
            }
        }
        protected void onPostExecute(String result) {
		    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
       }    
    }
}
