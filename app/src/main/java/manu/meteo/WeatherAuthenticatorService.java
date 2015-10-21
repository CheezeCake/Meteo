package manu.meteo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WeatherAuthenticatorService extends Service
{
	private WeatherAuthenticator authenticator;

	@Override
	public void onCreate()
	{
		authenticator = new WeatherAuthenticator(this);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.d("FUT", "WeatherAuthenticatorService.onBind()");
		return authenticator.getIBinder();
	}
}