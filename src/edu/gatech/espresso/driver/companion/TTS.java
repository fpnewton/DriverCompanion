package edu.gatech.espresso.driver.companion;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class TTS extends Activity implements OnInitListener
{
	private static final int TTS_CHECK_CODE = 1234;

	private TextToSpeech tts;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent checkIntent = new Intent();

		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		//startActivityForResult(checkIntent, TTS_CHECK_CODE);
		
		tts = new TextToSpeech(this, this);

	}

	public void onInit(int status)
	{
		if (status == TextToSpeech.SUCCESS)
		{
			int result = tts.setLanguage(Locale.US);
			
			if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED)
			{
				String text = getIntent().getExtras().getString("TTSMessage");
		
				if (text != null)
				{
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				} else
				{
					// TODO Handle error
					Toast.makeText(this, "Did not receive a message to speak.", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				// TODO Handle error
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == TTS_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} else
			{
				// missing data, install it
				Intent installIntent = new Intent();

				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	public void onDestroy()
	{
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}

		super.onDestroy();
	}
}
