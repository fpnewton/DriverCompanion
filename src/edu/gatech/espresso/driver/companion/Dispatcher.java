package edu.gatech.espresso.driver.companion;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

public class Dispatcher extends BroadcastReceiver implements OnInitListener
{
	TextToSpeech tts = null;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		/*Bundle extras = intent.getExtras();
		SmsMessage msgs[] = null;
		String str = "";
		
		tts = new TextToSpeech(context, this);
		
		if (extras != null)
		{
			Object pdus[] = (Object[]) extras.get("pdus");
			msgs = new SmsMessage[pdus.length];
			
			for (int i = 0; i < msgs.length; i++)
			{
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				
				str += "SMS from: " + msgs[i].getOriginatingAddress();
				str += "\n";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
				
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			}
		}*/
		
		tts = new TextToSpeech(context.getApplicationContext(), this);
		
		Toast.makeText(context, "This is a simple broadcast test.", Toast.LENGTH_SHORT).show();
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tts.speak("Hello World!", TextToSpeech.QUEUE_FLUSH, null);
	}

	public void onInit(int status)
	{
		if (status == TextToSpeech.SUCCESS)
		{
			// Set preferred language to US English
			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				// Language data is missing or the language is not supported
				Log.e("@string/log_tag", "Language is not available.");
			}
		}
		else
		{
			// Initialization failed
			Log.e("@string/log_tag", "Could not initialize TextToSpeech engine.");
		}
	}
}
