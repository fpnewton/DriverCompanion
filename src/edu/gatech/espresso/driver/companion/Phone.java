
package edu.gatech.espresso.driver.companion;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
}
