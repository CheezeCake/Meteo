package manu.meteo;

import android.app.Activity;
import android.app.LoaderManager;
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

    private String country;
    private String name;

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
            Intent serviceIntent = new Intent(this, FetchWeatherData.class);
            Log.d(TAG, name + " " + country);
            serviceIntent.putExtra(CityListActivity.CITY_URI,
                    WeatherContentProvider.getCityUri(country, name));
            startService(serviceIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CityListActivity.CITY_URI);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            name = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME));
            country = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY));
            String lastUpdate = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_LAST_UPDATE));
            String wind = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_WIND));
            String pressure = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_PRESSURE));
            String temperature = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_TEMPERATURE));

            City city = new City(name, country, lastUpdate, wind, pressure, temperature);

            TextView textView = (TextView)findViewById(R.id.nameTextView);
            textView.setText(name);

            textView = (TextView)findViewById(R.id.countryTextView);
            textView.setText(country);

            textView = (TextView)findViewById(R.id.windTextView);
            textView.setText(wind);

            textView = (TextView)findViewById(R.id.pressureTextView);
            textView.setText(pressure);

            textView = (TextView)findViewById(R.id.temperatureTextView);
            textView.setText(temperature);

            textView = (TextView)findViewById(R.id.dateTextView);
            textView.setText(lastUpdate);

            cursor.close();
        }
        else {
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
    {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }
}
