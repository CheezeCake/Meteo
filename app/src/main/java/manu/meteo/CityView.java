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
		City city = (City)intent.getSerializableExtra(CityListActivity.CITY);
		displayData(city);
	}

	private void displayData(City city)
	{
		TextView textView = (TextView)findViewById(R.id.nameTextView);
		textView.setText(city.getName());

		textView = (TextView)findViewById(R.id.countryTextView);
		textView.setText(city.getCountry());

		textView = (TextView)findViewById(R.id.windTextView);
		textView.setText(String.format("%s km/h (%s %s)",
				city.getWindSpeedInKmh(),
				getString(R.string.directionAbbreviation),
				city.getWindDirection()
				));

		textView = (TextView)findViewById(R.id.pressureTextView);
		textView.setText(city.getPressureInhPa() + " hPa");

		textView = (TextView)findViewById(R.id.temperatureTextView);
		textView.setText(city.getAirTemperatureInDegreesCelsius() + " °C");

		textView = (TextView)findViewById(R.id.dateTextView);
		textView.setText(city.getLastUpdate());
	}
}
