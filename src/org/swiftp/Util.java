package org.swiftp;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

abstract public class Util {
	static MyLog myLog = new MyLog(Util.class.getName());
	static String getAndroidId() {
		ContentResolver cr = Globals.getContext().getContentResolver();
		return Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
	}
	
	/**
	 * Get the SwiFTP version from the manifest.
	 * @return The version as a String.
	 */
	public static String getVersion() {
		String packageName = Globals.getContext().getPackageName();
		try {
			return Globals.getContext().getPackageManager().getPackageInfo(packageName, 0).versionName;
		} catch ( NameNotFoundException e) {
			myLog.l(Log.ERROR, "NameNotFoundException looking up SwiFTP version");
			return null;
		}
	}
	
	
	public static byte byteOfInt(int value, int which) {
		int shift = which * 8;
		return (byte)(value >> shift); 
	}
	
	public static String ipToString(int addr, String sep) {
		//myLog.l(Log.DEBUG, "IP as int: " + addr);
		if(addr > 0) {
			StringBuffer buf = new StringBuffer();
			buf.
			append(byteOfInt(addr, 0)).append(sep).
			append(byteOfInt(addr, 1)).append(sep).
			append(byteOfInt(addr, 2)).append(sep).
			append(byteOfInt(addr, 3));
			myLog.l(Log.DEBUG, "ipToString returning: " + buf.toString());
			return buf.toString();
		} else {
			return null;
		}	
	}
	
	public static InetAddress intToInet(int value) {
		byte[] bytes = new byte[4];
		for(int i = 0; i<4; i++) {
			bytes[i] = byteOfInt(value, i);
		}
		try {
			return InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			// This only happens if the byte array has a bad length
			return null;
		}
	}
	
	public static String ipToString(int addr) {
		if(addr == 0) {
			// This can only occur due to an error, we shouldn't blindly
			// convert 0 to string.
			myLog.l(Log.INFO, "ipToString won't convert value 0");
			return null;
		}
		return ipToString(addr, ".");
	}
	
	// This exists to avoid cluttering up other code with 
	// UnsupportedEncodingExceptions.
	static byte[] jsonToByteArray(JSONObject json) throws JSONException {
		try {
			return json.toString().getBytes(Defaults.STRING_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	// This exists to avoid cluttering up other code with 
	// UnsupportedEncodingExceptions.
	static JSONObject byteArrayToJson(byte[] bytes) throws JSONException {
		try {
			return new JSONObject(new String(bytes, Defaults.STRING_ENCODING));
		} catch (UnsupportedEncodingException e) {
			// This will never happen because we use valid encodings
			return null;
		}
	}
	
	public static void newFileNotify(String path) {
		myLog.l(Log.DEBUG, "Notifying others about new file: " + path);
		new MediaScannerNotifier(Globals.getContext(), path);
	}
	
	public static void deletedFileNotify(String path) {
		// This probably doesn't work, I couldn't find an API call for this.
		myLog.l(Log.DEBUG, "Notifying others about deleted file: " + path);
		new MediaScannerNotifier(Globals.getContext(), path);
	}
	
	// A class to help notify the Music Player and other media services when
	// a file has been uploaded. Thanks to Dave Sparks in his post to the
	// Android Developers mailing list on 14 Feb 2009.
	private static class MediaScannerNotifier implements MediaScannerConnectionClient {
	    private MediaScannerConnection connection;
	    private String path;

	    public MediaScannerNotifier(Context context, String path) {
	        this.path = path;
	        connection = new MediaScannerConnection(context, this);
	        connection.connect();
	    }

	    public void onMediaScannerConnected() {
	        connection.scanFile(path, null); // null: we don't know MIME type
	    }

	    public void onScanCompleted(String path, Uri uri) {
            connection.disconnect();
 	    }
	}

	
}
