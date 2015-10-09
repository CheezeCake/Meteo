package manu.meteo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class CityView extends Activity
{
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
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY)));

            textView = (TextView)findViewById(R.id.pressureTextView);
            textView.setText(String.format("%s hPa", cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_COUNTRY))));

            textView = (TextView)findViewById(R.id.temperatureTextView);
            textView.setText(String.format("%s °C", cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_TEMPERATURE))));

            textView = (TextView)findViewById(R.id.dateTextView);
            textView.setText(cursor.getString(cursor.getColumnIndex(WeatherDatabase.KEY_LAST_UPDATE)));

			cursor.close();
		}
		else {
			finish();
		}
	}
}
