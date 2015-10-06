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

			cursor.close();
			displayData(city);
		}
		else {
			finish();
		}
	}

	private void displayData(City city)
	{
		TextView textView = (TextView)findViewById(R.id.nameTextView);
		textView.setText(city.getName());

		textView = (TextView)findViewById(R.id.countryTextView);
		textView.setText(city.getCountry());

		textView = (TextView)findViewById(R.id.windTextView);
		textView.setText(city.getWindSpeedInKmh());

		textView = (TextView)findViewById(R.id.pressureTextView);
		textView.setText(String.format("%s hPa", city.getPressureInhPa()));

		textView = (TextView)findViewById(R.id.temperatureTextView);
		textView.setText(String.format("%s °C", city.getAirTemperatureInDegreesCelsius()));

		textView = (TextView)findViewById(R.id.dateTextView);
		textView.setText(city.getLastUpdate());
	}
}
