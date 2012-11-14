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
	
	public String[] getContact(String name)
	{		
		while (contactsCursor.moveToNext())
		{			
			int indexName = contactsCursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			int indexPhoneNum = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
			
			String cntName = contactsCursor.getString(indexName);
			String phoneNum = contactsCursor.getString(indexPhoneNum);
			String nameSplit[] = cntName.split(" ");
			
			//Log.i(activity.getString(R.string.log_tag), cntName + ":" + phoneNum);
			
			if (cntName.compareToIgnoreCase(name) == 0)
			{
				String result[] = new String[2];
				Log.i(activity.getString(R.string.log_tag), "Found: " + cntName);
				result[0] = cntName;
				result[1] = phoneNum;
				
				return result;
			}
			else
			{
				for (String part : nameSplit)
				{
					if (name.compareToIgnoreCase(part) == 0)
					{
						Log.i(activity.getString(R.string.log_tag), "Found: " + cntName);
						
						String result[] = new String[2];
						
						result[0] = cntName;
						result[1] = phoneNum;
						
						return result;
					}
				}
			}
		}
		
		return null;
	}
}
