package edu.gatech.espresso.driver.companion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

public class OldSpeechRecognizer
{
	public static final int VR_REQUEST_CODE = 1337;
	public static final int VR_REQUEST_LISTEN = 1338;
	public static final int VR_REQUEST_SMS = 1339;
	
	private Activity activity;
	private String results[];
	
	
	public OldSpeechRecognizer(Activity activity)
	{
		this.activity = activity;
		results = null;
	}
	
	
	public void startRecognizer(int functionCode)
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getClass().getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.getString(R.string.speech_dialog_title));
		
		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		// Specify how many results you want to receive. The results will be
		// sorted
		// where the first result is the one with higher confidence.
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

		activity.startActivityForResult(intent, functionCode);
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ((requestCode == VR_REQUEST_LISTEN || requestCode == VR_REQUEST_SMS) && resultCode == Activity.RESULT_OK)
		{
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			results = new String[matches.size()];

			for (int i = 0; i < results.length; i++)
			{
				results[i] = matches.get(i);
			}
		}

		//parent.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public String[] getResults()
	{
		return results;
	}
}
