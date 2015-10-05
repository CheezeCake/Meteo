package manu.meteo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class WeatherContentProvider extends ContentProvider
{
	public static final String AUTHORITY = "manu.meteo.provider";
	public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
			.authority(WeatherContentProvider.AUTHORITY)
			.appendEncodedPath(WeatherDatabase.TABLE_WEATHER)
			.build();

	private static final int WEATHER = 1;
	private static final int WEATHER_ID = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static
	{
		uriMatcher.addURI(AUTHORITY, "weather", WEATHER);
		uriMatcher.addURI(AUTHORITY, "weather/*/*", WEATHER_ID);
	}

	private WeatherDatabase weatherDatabase = null;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO: Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri)
	{
		Log.d("WeatherContentProvider", "getType()");
		int match = uriMatcher.match(uri);
		switch (match)
		{
			case WEATHER:
				return "vnd.android.cursor.dir/weather";
			case WEATHER_ID:
				return "vnd.android.cursor.item/weather";
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
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
			case WEATHER_ID:
				return null;
			default:
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
