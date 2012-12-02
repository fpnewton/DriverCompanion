package edu.gatech.espresso.driver.companion.speed;

import edu.gatech.espresso.driver.companion.MainActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class SpeedService extends Service implements LocationListener
{
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	
	@Override
	public void onCreate()
	{
		super.onCreate();

		Log.i("DriverCompanion - Speed Service", "Speed Service created.");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		Log.i("DriverCompanion - Speed Service", "Speed Service destroyed.");
	}

	@Override
	public void onStart(Intent intent, int startId)
	{		
		LocationManager locationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 100.0f, this);
		Log.i("DriverCompanion - Speed Service", "Speed Service started.");
	}


	public void onLocationChanged(Location location)
	{
		if (location != null)
		{
			Log.i("DriverCompanion - Speed Service", "Current Speed: " + location.getSpeed());
			
			if (location.getSpeed() > 11.176)
			{
				startActivity(new Intent(this, MainActivity.class));
			}
		}
	}


	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}


	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}


	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		
	}
}
