
package edu.gatech.espresso.driver.companion;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;


public class Phone
{
	public static void call(String contactNumber, Activity activity)
	{
		try
		{
			Intent callIntent = new Intent(Intent.ACTION_CALL);

			callIntent.setData(Uri.parse("tel:" + contactNumber));
			activity.startActivity(callIntent);
		}
		catch (ActivityNotFoundException e)
		{
			Log.e("@string/log_tag", "Could not place call. Error: " + e.getMessage());
		}
	}

	public static void sendSMS(String contactNumber, String message, Activity activity)
	{
		SmsManager sms = SmsManager.getDefault();
		String sent = "android.telephony.SmsManager.STATUS_ON_ICC_SENT";
		PendingIntent piSent = PendingIntent.getBroadcast(activity, 0, new Intent("SMS_SENT"), 0);
		PendingIntent piDeliver = PendingIntent.getBroadcast(activity, 0, new Intent("SMS_DELIVERED"), 0);

		sms.sendTextMessage(contactNumber, null, message, piSent, piDeliver);
	}
}
