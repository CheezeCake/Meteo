package manu.meteo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class WeatherContentProvider extends ContentProvider
{
	public static final String AUTHORITY = "manu.meteo.provider";
	public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
			.authority(WeatherContentProvider.AUTHORITY)
			.appendEncodedPath(WeatherDatabase.TABLE_WEATHER)
			.build();

	private static final int COUNTRY_SEGMENT = 1;
	private static final int NAME_SEGMENT = 2;

	private static final int WEATHER = 1;
	private static final int WEATHER_CITY = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static
	{
		uriMatcher.addURI(AUTHORITY, "weather", WEATHER);
		uriMatcher.addURI(AUTHORITY, "weather/*/*", WEATHER_CITY);
	}

	private WeatherDatabase weatherDatabase = null;

	public static Uri getCityUri(String country, String name)
	{
		return WeatherContentProvider.CONTENT_URI.buildUpon().appendPath(country).appendPath(name).build();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		Log.d("WeatherContentProvider", "delete()");
		if (uriMatcher.match(uri) != WEATHER_CITY)
			return 0;

		List<String> pathSegments = uri.getPathSegments();
		String country = pathSegments.get(COUNTRY_SEGMENT);
		String name = pathSegments.get(NAME_SEGMENT);
		Log.d("WeatherContentProvider", "delete(): country = " + country + ", name = " + name);

		return weatherDatabase.deleteCity(country, name);
	}

	@Override
	public String getType(Uri uri)
	{
		Log.d("WeatherContentProvider", "getType()");
		int match = uriMatcher.match(uri);
		switch (match)
		{
			case WEATHER:
				return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + ".weather";
			case WEATHER_CITY:
				return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + ".weather";
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		Log.d("WeatherContentProvider", "insert()");
		if (uriMatcher.match(uri) != WEATHER_CITY)
			return null;

		List<String> pathSegments = uri.getPathSegments();
		String country = pathSegments.get(COUNTRY_SEGMENT);
		String name = pathSegments.get(NAME_SEGMENT);
		Log.d("WeatherContentProvider", "insert(): country = " + country + ", name = " + name);

		return (weatherDatabase.addCity(country, name) == -1) ? null : getCityUri(country, name);
	}

	@Override
	public boolean onCreate()
	{
		weatherDatabase = new WeatherDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Log.d("WeatherContentProvider", "query()");
		Log.d("WeatherContentProvider", "uriMatcher.match(uri) = " + uriMatcher.match(uri));
		int match = uriMatcher.match(uri);

		switch (match)
		{
			case WEATHER:
				return weatherDatabase.getAllCities();
			case WEATHER_CITY:
				List<String> pathSegments = uri.getPathSegments();
				String country = pathSegments.get(COUNTRY_SEGMENT);
				String name = pathSegments.get(NAME_SEGMENT);
				Log.d("WeatherContentProvider", "query(): country = " + country + ", name = " + name);
				return weatherDatabase.getCity(country, name);
			default:
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		Log.d("WeatherContentProvider", "insert()");
		if (uriMatcher.match(uri) == WEATHER_CITY) {
			List<String> pathSegments = uri.getPathSegments();
			String country = pathSegments.get(COUNTRY_SEGMENT);
			String name = pathSegments.get(NAME_SEGMENT);
			Log.d("WeatherContentProvider", "insert(): country = " + country + ", name = " + name);
			return weatherDatabase.update(country, name, values);
		}

		return -1;
	}
}
