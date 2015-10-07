package manu.meteo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddCityActivity extends Activity
{
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

		Uri uri = getContentResolver().insert(WeatherContentProvider.getCityUri(country, name), null);

		if (uri != null) {
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);

			Log.d("AddCityActivity", "saved city uri = " + uri);

			finish();
		}
		else {
			Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancel(View v)
	{
		finish();
	}
}
