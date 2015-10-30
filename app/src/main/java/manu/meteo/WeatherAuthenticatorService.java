package manu.meteo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
		return authenticator.getIBinder();
	}
}