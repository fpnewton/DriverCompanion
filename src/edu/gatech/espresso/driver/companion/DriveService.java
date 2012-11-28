package edu.gatech.espresso.driver.companion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DriveService extends Service {
	public Service() {
	}

	@Override
	public IBinder onBind(Intent intent) {	
		// TODO: Return the communication channel to the service.
		return null;
	}
	
	
}
