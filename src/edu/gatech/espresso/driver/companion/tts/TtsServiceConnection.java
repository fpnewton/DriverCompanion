package edu.gatech.espresso.driver.companion.tts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class TtsServiceConnection
{
	private boolean				mBound			= false;
	private TtsService			mService		= null;
	private ServiceConnection	mConnection		= null;
	
	
	public TtsServiceConnection()
	{		
		mConnection = new ServiceConnection()
		{
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				TtsService.TtsBinder binder = (TtsService.TtsBinder) service;

				mService = binder.getService();
				mBound = true;
			}

			public void onServiceDisconnected(ComponentName name)
			{
				mBound = false;
			}
		};
	}
	

	public TtsService getService()
	{
		return mService;
	}
	
	
	public boolean isBound()
	{
		return mBound;
	}
	
	
	public void onStart(Activity context)
	{
		Intent intent = new Intent(context, TtsService.class);
		
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	public void onStop(Activity context)
	{
		if (mBound)
		{
			context.unbindService(mConnection);
			
			mBound = false;
		}
	}
}
