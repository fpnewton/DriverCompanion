package edu.gatech.espresso.driver.companion;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class Contacts
{
	private Activity activity;
	private Cursor contactsCursor;
	
	public Contacts(Activity activity)
	{
		this.activity = activity;
		
		contactsCursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
	}
	
	public String getContact(String name)
	{		
		while (contactsCursor.moveToNext())
		{			
			int indexName = contactsCursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			int indexPhoneNum = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
			
			String cntName = contactsCursor.getString(indexName);
			String phoneNum = contactsCursor.getString(indexPhoneNum);
			
			//Log.i(activity.getString(R.string.log_tag), cntName + ":" + phoneNum);
			
			if (cntName.equalsIgnoreCase(name))
			{
				return phoneNum;
			}
		}
		
		return "";
	}
}
