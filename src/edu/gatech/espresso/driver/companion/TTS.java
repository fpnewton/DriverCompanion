
package edu.gatech.espresso.driver.companion;


import java.util.Locale;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;


public class TTS implements OnInitListener
{
	private Activity		activity;
	private TextToSpeech	tts;

	public TTS(Activity activity)
	{
		this.activity = activity;
		tts = new TextToSpeech(this.activity, this);
	}

	public void onDestroy()
	{
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
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

	public void speak(String text)
	{
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}
