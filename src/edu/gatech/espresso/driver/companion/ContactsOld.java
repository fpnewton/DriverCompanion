
package edu.gatech.espresso.driver.companion;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;


public class ContactsOld
{
	public static final int	INDEX_NAME		= 0;
	public static final int	INDEX_PHONENUM	= 1;

	private Cursor		contactCursor;

	public ContactsOld(Activity activity)
	{
		contactCursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
	}

	public ArrayList<String[]> getContactsDictionary()
	{
		ArrayList<String[]> dict = new ArrayList<String[]>();

		while (contactCursor.moveToNext())
		{
			String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String phone = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));

			dict.add(new String[] { name, phone });
		}

		contactCursor.close();

		return dict;
	}

	public boolean inContacts(String name)
	{
		ArrayList<String[]> contacts = getContactsDictionary();
		Pattern pattern = Pattern.compile(name);

		for (String person[] : contacts)
		{
			Matcher matcher = pattern.matcher(person[INDEX_NAME]);

			if (matcher.find())
			{
				return true;
			}
		}

		return false;
	}
}
