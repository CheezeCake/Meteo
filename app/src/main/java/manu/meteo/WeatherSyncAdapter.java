package manu.meteo;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class WeatherSyncAdapter extends AbstractThreadedSyncAdapter
{
	private static final String TAG = WeatherSyncAdapter.class.getSimpleName();

	private static final String webServiceURL =
			"http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=%s&CountryName=%s";

	private static final String encoding = "UTF-8";

	public WeatherSyncAdapter(Context context, boolean autoInitialize)
	{
		super(context, autoInitialize);
	}

	public WeatherSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
	{
		super(context, autoInitialize, allowParallelSyncs);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
							  ContentProviderClient contentProviderClient, SyncResult syncResult)
	{
		String country = extras.getString(CityView.CITY_COUNTRY);
		String name = extras.getString(CityView.CITY_NAME);
		Log.d(TAG, "onPerformSync : " + country + " " + name);

		if (country != null && name != null)
			syncCity(country, name, contentProviderClient, syncResult);
		else
			syncAllCities(contentProviderClient, syncResult);

		Log.d(TAG, "SyncResult.stats = " + syncResult.stats);
	}

	private void syncAllCities(ContentProviderClient contentProviderClient, SyncResult syncResult)
	{
		Log.d(TAG, "syncAllCities");

		try {
			Cursor cursor = contentProviderClient.query(WeatherContentProvider.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					syncCity(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY)),
							cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME)),
							contentProviderClient, syncResult);
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void syncCity(String country, String name, ContentProviderClient contentProviderClient,
						  SyncResult syncResult)
	{
		Log.d(TAG, "syncCity : country = " + country + " name = " + name);

		URL url;
		URLConnection con;
		InputStream is = null;
		XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
		List<String> data = null;

		try {
			String encodedCountry = URLEncoder.encode(country, encoding);
			String encodedName = URLEncoder.encode(name, encoding);

			url = new URL(String.format(webServiceURL, encodedName, encodedCountry));
			con = url.openConnection();

			is = con.getInputStream();
			data = xmlResponseHandler.handleResponse(is, encoding);
		}
		catch (IOException e) {
			e.printStackTrace();
			++syncResult.stats.numIoExceptions;
		}

		try {
			if (is != null)
				is.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if (data != null && data.size() == XMLResponseHandler.DATA_SIZE) {
			ContentValues values = new ContentValues();
			values.put(WeatherDatabase.KEY_LAST_UPDATE, data.get(XMLResponseHandler.LAST_UPDATE));
			values.put(WeatherDatabase.KEY_WIND, data.get(XMLResponseHandler.WIND));
			values.put(WeatherDatabase.KEY_PRESSURE, data.get(XMLResponseHandler.PRESSURE));
			values.put(WeatherDatabase.KEY_TEMPERATURE, data.get(XMLResponseHandler.TEMPERATURE));

			try {
				contentProviderClient.update(WeatherContentProvider.getCityUri(country, name),
						values, null, null);
				++syncResult.stats.numUpdates;
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else {
			Log.e(TAG, "No data for " + country + " " + name);
		}

	}
}
