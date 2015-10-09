package manu.meteo;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

        Cursor cursor = getContentResolver().query(cityUri, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            City city = new City(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_LAST_UPDATE)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_WIND)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_PRESSURE)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_TEMPERATURE)));
            cursor.close();


            WebServiceClient.getWeather(city);

            ContentValues values = new ContentValues();
            values.put(WeatherDatabase.KEY_COUNTRY, city.getCountry());
            values.put(WeatherDatabase.KEY_NAME, city.getName());
            values.put(WeatherDatabase.KEY_LAST_UPDATE, city.getLastUpdate());
            values.put(WeatherDatabase.KEY_WIND, city.getWindSpeedInKmh());
            values.put(WeatherDatabase.KEY_PRESSURE, city.getPressureInhPa());
            values.put(WeatherDatabase.KEY_TEMPERATURE, city.getAirTemperatureInDegreesCelsius());

            getContentResolver().update(cityUri, values, null, null);
        }
    }
}
