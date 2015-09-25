package manu.meteo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AddCityActivity extends Activity
{
	public static final String CITY_SAVED = "manu.meteo.CITY_SAVED";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_city);
	}

	public void save(View v)
	{
		TextView text = (TextView)findViewById(R.id.cityNameInput);
		String name = text.getText().toString();
		text = (TextView)findViewById(R.id.countryInput);
		String country = text.getText().toString();

		if (name.isEmpty() || country.isEmpty())
			return;

		City city = new City(name, country);

		Intent intent = new Intent();
		intent.putExtra(CITY_SAVED, city);
		setResult(Activity.RESULT_OK, intent);

		Log.d("AddCityActivity", "saved = " + city);

		finish();
	}

	public void cancel(View v)
	{
		finish();
	}
}
