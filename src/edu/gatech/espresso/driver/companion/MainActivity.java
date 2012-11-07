
package edu.gatech.espresso.driver.companion;


import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements OnInitListener
{
	private static final String	TAG								= "TextToSpeechDemo";

	private TextToSpeech		mTts;
	private Button				mAgainButton;

	private static final int	VOICE_RECOGNITION_REQUEST_CODE	= 1234;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize text-to-speech. This is an asynchronous operation.
		// The OnInitListener (second argument) is called after initialization
		// completes.
		mTts = new TextToSpeech(this, this // TextToSpeech.OnInitListener
		);

		// The button is disabled in the layout.
		// It will be enabled upon initialization of the TTS engine.
		mAgainButton = (Button) findViewById(R.id.btn_listen);

		mAgainButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				startVoiceRecognitionActivity();
			}
		});
	}

	@Override
	public void onDestroy()
	{
		// Don't forget to shutdown!
		if (mTts != null)
		{
			mTts.stop();
			mTts.shutdown();
		}

		super.onDestroy();
	}

	// Implements TextToSpeech.OnInitListener.
	public void onInit(int status)
	{
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS)
		{
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = mTts.setLanguage(Locale.US);
			// Try this someday for some interesting results.
			// int result mTts.setLanguage(Locale.FRANCE);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
			}
			else
			{
				// Check the documentation for other possible result codes.
				// For example, the language may be available for the locale,
				// but not for the specified country and variant.

				// The TTS engine has been successfully initialized.
				// Allow the user to press the button for the app to speak
				// again.
				mAgainButton.setEnabled(true);
				// Greet the user.
				mTts.speak("Hello!", TextToSpeech.QUEUE_FLUSH, null);
			}
		}
		else
		{
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");

		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		// Specify how many results you want to receive. The results will be
		// sorted
		// where the first result is the one with higher confidence.
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)
		{
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.i(TAG, matches.toString());
			mTts.speak("You said: " + matches.get(0), TextToSpeech.QUEUE_FLUSH, null);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
