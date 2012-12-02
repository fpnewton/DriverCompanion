package edu.gatech.espresso.driver.companion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import edu.gatech.espresso.driver.companion.tts.TtsServiceConnection;

public class MainActivity extends Activity
{
	private TtsServiceConnection tts = null;
	private static final int SR_REQUEST = 1337;
	
	private static final int CMD_NULL = 0;
	private static final int CMD_PROMPT = 1;
	private static final int CMD_CALL = 2;
	private static final int CMD_SMS = 3;
	private static final int CMD_SMS_MESSAGE = 4;
	private static final int CMD_SMS_CONFIRM = 5;
	
	private int lastCommand = CMD_NULL;
	
	private Contacts contacts;
	private String cmdContact[];
	private String cmdMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO
		contacts = new Contacts(this);
		tts = new TtsServiceConnection();
	}

	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);

		return true;
	}

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}

	 
	@Override
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
		//startActivityForResult(new Intent(this, SpeechRecognizer.class), SR_REQUEST);
		
		startVoiceRecognition(getString(R.string.say_command), 1, CMD_PROMPT);
	}
	
	
	protected void startVoiceRecognition(String prompt, int numResults, int command)
	{
		Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, numResults);
		
		this.lastCommand = command;
		
		startActivityForResult(listenIntent, SR_REQUEST);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO
		if (requestCode == SR_REQUEST && resultCode == RESULT_OK)
		{
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			Log.i(getString(R.string.log_tag), "I Heard: " + results.toString());
			
			processCommands(results.toArray(new String[0]));
		}
	}

	
	@Override
	protected void onStart()
	{
		super.onStart();

		// TODO
		tts.onStart(this);
	}

	
	@Override
	protected void onStop()
	{
		super.onStop();
		
		// TODO
		tts.onStop(this);
	}
	
	
	private void processCommands(String commands[])
	{
		String cmd = commands[0];
		
		
		if (cmd.equalsIgnoreCase("Goodbye"))
		{
			return;
		}
		
		
		if (lastCommand == CMD_NULL)
		{
			// Do absolutely nothing.
		}
		else if (lastCommand == CMD_PROMPT)
		{
			String parts[] = cmd.split(" ");
			
			if (parts[0].equalsIgnoreCase("Call"))
			{
				StringBuilder partialName = new StringBuilder();
				
				for (int i = 1; i < parts.length; i++)
				{
					partialName.append(parts[i]);
					partialName.append(" ");
				}
				
				
				// TODO Convert to Contact object instead of String[]
				String contact[] = contacts.getContact(partialName.toString().trim());
				cmdContact = contact;
				
				
				tts.getService().speak("Did you say: Call " + contact[0] + "?");				
				startVoiceRecognition(getString(R.string.did_you_say), 1, CMD_CALL);
			}
			else if (parts[0].equalsIgnoreCase("Message") || parts[0].equalsIgnoreCase("SMS"))
			{
				StringBuilder partialName = new StringBuilder();
				
				for (int i = 1; i < parts.length; i++)
				{
					partialName.append(parts[i]);
					partialName.append(" ");
				}
				
				
				// TODO Convert to Contact object instead of String[]
				String contact[] = contacts.getContact(partialName.toString().trim());
				cmdContact = contact;
				
				
				tts.getService().speak("Did you say: Message " + contact[0] + "?");				
				startVoiceRecognition(getString(R.string.did_you_say), 1, CMD_SMS);
			}
			else
			{
				tts.getService().speak("I'm sorry I don't understand. Please try again.");
				startVoiceRecognition(getString(R.string.did_you_say), 1, CMD_PROMPT);
			}
		}
		else if (lastCommand == CMD_CALL)
		{
			if (cmd.equalsIgnoreCase("Yes"))
			{
				tts.getService().speak("Calling: " + cmdContact[0]);
				Phone.call(cmdContact[1], this);
			}
			else
			{
				tts.getService().speak(getString(R.string.say_command));
				startVoiceRecognition(getString(R.string.say_command), 1, CMD_PROMPT);
			}
		}
		else if (lastCommand == CMD_SMS)
		{
			if (cmd.equalsIgnoreCase("Yes"))
			{
				tts.getService().speak(getString(R.string.what_sms));			
				startVoiceRecognition(getString(R.string.what_sms), 1, CMD_SMS_MESSAGE);
			}
			else
			{
				tts.getService().speak(getString(R.string.say_command));
				startVoiceRecognition(getString(R.string.say_command), 1, CMD_PROMPT);
			}
		}
		else if (lastCommand == CMD_SMS_MESSAGE)
		{
			cmdMessage = cmd;
			
			tts.getService().speak("Did you say: " + cmdMessage + "?");				
			startVoiceRecognition(getString(R.string.did_you_say), 1, CMD_SMS_CONFIRM);
		}
		else if (lastCommand == CMD_SMS_CONFIRM)
		{
			if (cmd.equalsIgnoreCase("Yes"))
			{
				Phone.sendSMS(cmdContact[1], cmdMessage, this);
				tts.getService().speak("Message sent.");
			}
			else
			{
				tts.getService().speak(getString(R.string.what_sms));			
				startVoiceRecognition(getString(R.string.what_sms), 1, CMD_SMS_MESSAGE);
			}
		}
		else
		{
			// TODO Handle WTF error
		}
	}
}
