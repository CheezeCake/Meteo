package manu.meteo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class CityView extends Activity
{
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
            Log.d("CityView", name + " " + country);
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

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CityListActivity.CITY_URI);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            name = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME));
            country = cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY));

            City city = new City(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_LAST_UPDATE)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_WIND)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_PRESSURE)),
                    cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_TEMPERATURE)));

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

            cursor.close();
        }
        else {
            finish();
        }
    }
}
