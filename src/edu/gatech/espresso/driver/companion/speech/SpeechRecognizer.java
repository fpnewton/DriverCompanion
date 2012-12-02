package edu.gatech.espresso.driver.companion.speech;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import edu.gatech.espresso.driver.companion.R;

public class SpeechRecognizer extends Activity
{
	private static final int VR_REQUEST = 1337;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.blank_layout);
		
		Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please say a command.");
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		
		startActivityForResult(listenIntent, VR_REQUEST);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == VR_REQUEST && resultCode == RESULT_OK)
		{
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			Intent returnIntent = new Intent();
			
			returnIntent.putExtra("SpeechResults", results.get(0));
			
			setResult(RESULT_OK, returnIntent);
			finish();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
