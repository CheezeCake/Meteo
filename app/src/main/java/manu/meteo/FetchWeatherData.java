package manu.meteo;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class FetchWeatherData extends IntentService
{
	private static final String TAG = FetchWeatherData.class.getSimpleName();

	private static final String webServiceURL =
			"http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=%s&CountryName=%s";

	private static final String encoding = "UTF-8";

	public FetchWeatherData()
	{
		super("FetchWeatherData");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Uri cityUri = (Uri)intent.getParcelableExtra(CityListActivity.CITY_URI);
		Log.d(TAG, "onHandleIntent(): cityUri = " + cityUri);

		URL url;
		URLConnection con;
		InputStream is = null;
		XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
		List<String> infos = null;

		try {
			String name = URLEncoder.encode(WeatherContentProvider.getNameFromUri(cityUri), encoding);
			String country = URLEncoder.encode(WeatherContentProvider.getCountryFromUri(cityUri), encoding);

			url = new URL(String.format(webServiceURL, name, country));
			con = url.openConnection();

			is = con.getInputStream();
			infos = xmlResponseHandler.handleResponse(is, encoding);
		}
		catch (IOException e) {
			Log.e(TAG, cityUri + " : " + e.toString());
		}

		try {
			if (is != null)
				is.close();
		}
		catch (IOException e) {
			Log.e(TAG, cityUri + " : " + e.toString());
		}

		if (infos != null && infos.size() == 4) {
			ContentValues values = new ContentValues();
			values.put(WeatherDatabase.KEY_LAST_UPDATE, infos.get(XMLResponseHandler.LAST_UPDATE));
			values.put(WeatherDatabase.KEY_WIND, infos.get(XMLResponseHandler.WIND));
			values.put(WeatherDatabase.KEY_PRESSURE, infos.get(XMLResponseHandler.PRESSURE));
			values.put(WeatherDatabase.KEY_TEMPERATURE, infos.get(XMLResponseHandler.TEMPERATURE));

			getContentResolver().update(cityUri, values, null, null);
		}
		else {
			Log.e(TAG, "No data for " + cityUri);
		}
	}
}
