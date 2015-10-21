package manu.meteo;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

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
		Log.d(TAG, "onPerformSync");
	}
}
