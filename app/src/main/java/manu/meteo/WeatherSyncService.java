package manu.meteo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WeatherSyncService extends Service
{
	private static WeatherSyncAdapter syncAdapter = null;

	// Object to use as a thread-safe lock
	private static final Object syncAdapterLock = new Object();

	@Override
	public void onCreate()
	{
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
		synchronized (syncAdapterLock) {
			if (syncAdapter == null)
				syncAdapter = new WeatherSyncAdapter(getApplicationContext(), true);
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return syncAdapter.getSyncAdapterBinder();
	}
}
