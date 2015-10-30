package manu.meteo;

import android.accounts.Account;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CityView extends Activity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = CityView.class.getSimpleName();

    private static final int LOADER_ID = 1;

	public static final String CITY_COUNTRY = "manu.meteo.city_country";
	public static final String CITY_NAME = "manu.meteo.city_name";

	private Account account;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_city_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            TextView text = (TextView)findViewById(R.id.nameTextView);
            String name = text.getText().toString();
            text = (TextView)findViewById(R.id.countryTextView);
            String country = text.getText().toString();

			Bundle settingsBundle = new Bundle();
			settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
			settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
			settingsBundle.putString(CITY_COUNTRY, country);
			settingsBundle.putString(CITY_NAME, name);

			ContentResolver.requestSync(account, WeatherContentProvider.AUTHORITY,
					settingsBundle);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CityListActivity.CITY_URI);
		account = intent.getParcelableExtra(CityListActivity.ACCOUNT);

        Bundle bundle = new Bundle();
        bundle.putParcelable(CityListActivity.CITY_URI, uri);
        getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
    {
        return new CursorLoader(this, (Uri)bundle.getParcelable(CityListActivity.CITY_URI), null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        Log.d(TAG, "onLoadFinished()");

        if (cursor != null) {
            cursor.moveToFirst();

            TextView textView = (TextView)findViewById(R.id.nameTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME)));

            textView = (TextView)findViewById(R.id.countryTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY)));

            textView = (TextView)findViewById(R.id.windTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_WIND)));

            textView = (TextView)findViewById(R.id.pressureTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_PRESSURE)));

            textView = (TextView)findViewById(R.id.temperatureTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_TEMPERATURE)));

            textView = (TextView)findViewById(R.id.dateTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_LAST_UPDATE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }
}
