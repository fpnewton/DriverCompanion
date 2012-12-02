package edu.gatech.espresso.driver.companion.tts;

import java.util.Locale;

import edu.gatech.espresso.driver.companion.Contacts;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsMessage;
import android.util.Log;

public class TtsService extends Service implements OnInitListener
{
	private static final String LOG_TAG = "DriverCompanion - TTS Service";
	
	
	private IBinder ttsBinder = new TtsBinder();
	private TextToSpeech tts;
	private SmsReceiver mSmsReceiver;
	private IntentFilter mIntentFilter;

	
	public class TtsBinder extends Binder
	{
		public TtsService getService()
		{
			return TtsService.this;
		}
	}
	
	
	private class SmsReceiver extends BroadcastReceiver
	{
	    private final String TAG = this.getClass().getSimpleName();

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle extras = intent.getExtras();

	        String strMessage = "";

	        if ( extras != null )
	        {
	            Object[] smsextras = (Object[]) extras.get( "pdus" );

	            for ( int i = 0; i < smsextras.length; i++ )
	            {
	                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

	                String strMsgBody = smsmsg.getMessageBody().toString();
	                String strMsgSrc = smsmsg.getOriginatingAddress();
	                
	                String name = getContactName(TtsService.this.getApplicationContext(), strMsgSrc);
	                
	                strMessage += "Message from: " + name + " : " + strMsgBody;
	                
	                TtsService.this.speak(strMessage);

	                Log.i(TAG, strMessage);
	            }

	        }
		}
		
		public String getContactName(Context context, String number)
		{
			// define the columns I want the query to return
			String projection[] = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };

			// encode the phone number and build the filter URI
			Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

			// query time
			Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

			if (cursor.moveToFirst())
			{
			    // Get values from contacts database:
			    return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
			}
			
			StringBuilder name = new StringBuilder();
			
			for (int i = 2; i < number.length(); i++)
			{
				name.append(number.charAt(i));
				name.append(" ");
			}
			
			return name.toString().trim();
		}
	}
	

	@Override
	public IBinder onBind(Intent intent)
	{
		return ttsBinder;
	}

	
	@Override
	public void onCreate()
	{
		// Create the Text-To-Speech object
		tts = new TextToSpeech(this, this);
		
		mSmsReceiver = new SmsReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver, mIntentFilter);
        
        super.onCreate();
	}

	
	@Override
	public void onDestroy()
	{
		// Check if the Text-To-Speech Engine object is still valid
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		
		unregisterReceiver(mSmsReceiver);

		super.onDestroy();
	}

	
	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO
	}

	
	public void onInit(int status)
	{
		if (status == TextToSpeech.SUCCESS)
		{
			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Log.e(LOG_TAG, "Failed to initialize the Text-To-Speech engine.");
			}
		}
	}

	
	public void speak(String message)
	{
		while (isSpeaking());
		tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
		while (isSpeaking());
	}
	
	
	public boolean isSpeaking()
	{
		return tts.isSpeaking();
	}
}
