
package edu.gatech.espresso.driver.companion;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity
{
	private Contacts			contacts;
	private TTS					tts;
	private SpeechRecognizer	sr;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contacts = new Contacts(this);
		tts = new TTS(this);
		sr = new SpeechRecognizer(this);
	}

	public void onDestroy()
	{
		super.onDestroy();

		tts.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);

		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		int selectionId = item.getItemId();

		if (selectionId == R.id.menu_settings)
		{
			startActivity(new Intent(this, SettingsActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	public void btnListenClicked(View v)
	{
		// TODO
		sr.startRecognizer();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		sr.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

		String results[] = sr.getResults();

		if (results != null)
		{
			String split[] = results[0].split(" ");
			
			for (String s : split)
			{
				Log.i(getString(R.string.log_tag), contacts.getContact(s));
				//tts.speak("I Heard: " + s);
				//Phone.call(contacts.getContact(s), this);
			}
		}
		else
		{
			Log.e(getString(R.string.log_tag), "No speech results received.");
		}
	}
}
