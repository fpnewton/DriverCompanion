package edu.gatech.espresso.driver.companion.speed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IntentReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent serviceIntent = new Intent();
		
		serviceIntent.setAction("edu.gatech.espresso.driver.companion.speed.SpeedService");
		context.startService(serviceIntent);
	}
}