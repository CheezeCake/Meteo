package manu.meteo;

import android.app.Activity;
import android.content.Intent;
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
		City city = (City)intent.getSerializableExtra(CityListActivity.CITY_URI);
		displayData(city);
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
