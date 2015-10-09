package manu.meteo;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class FetchWeatherData extends IntentService
{
	private static final String TAG = FetchWeatherData.class.getSimpleName();

	public FetchWeatherData()
	{
		super("FetchWeatherData");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Uri cityUri = (Uri)intent.getParcelableExtra(CityListActivity.CITY_URI);
		Log.d(TAG, "onHandleIntent(): cityUri = " + cityUri);

		City city = new City(WeatherContentProvider.getNameFromUri(cityUri),
				WeatherContentProvider.getCountryFromUri(cityUri));

		WebServiceClient.getWeather(city);

		ContentValues values = new ContentValues();
		values.put(WeatherDatabase.KEY_LAST_UPDATE, city.getLastUpdate());
		values.put(WeatherDatabase.KEY_WIND, city.getWindSpeedInKmh());
		values.put(WeatherDatabase.KEY_PRESSURE, city.getPressureInhPa());
		values.put(WeatherDatabase.KEY_TEMPERATURE, city.getAirTemperatureInDegreesCelsius());

		getContentResolver().update(cityUri, values, null, null);
	}
}
