package manu.meteo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityListActivity extends ListActivity
{
	public static final String CITY = "manu.meteo.city";

	private ArrayList<City> cityArrayList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		cityArrayList.add(new City("Paris", "France"));
		cityArrayList.add(new City("Tokyo", "Japan"));
		cityArrayList.add(new City("London", "United Kingdom"));
		cityArrayList.add(new City("Moscow", "Russia"));

		final String col = "infos";
		List<HashMap<String, String>> data = new ArrayList<>();
		for (City city : cityArrayList) {
			HashMap<String, String> element = new HashMap<>();
			element.put(col, city.getName() + " (" + city.getCountry() + ")");
			data.add(element);
		}

		SimpleAdapter adapter =
				new SimpleAdapter(CityListActivity.this, data, android.R.layout.simple_list_item_1,
						new String[] { col }, new int[] { android.R.id.text1 });
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_city_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_add) {
			return true;
		}
		else if (id == R.id.action_refresh) {
			new FetchData().execute(cityArrayList);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = new Intent(this, CityView.class);
		City city = cityArrayList.get(position);

		intent.putExtra(CITY, city);
		startActivity(intent);
	}


	private class FetchData extends AsyncTask<ArrayList<City>, Void, Void>
	{
		ProgressDialog progress;

		@Override
		protected void onPreExecute()
		{
			progress = new ProgressDialog(CityListActivity.this);
			progress.setMessage(getString(R.string.updatingData));
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setCancelable(false);
			progress.setIndeterminate(true);
			progress.show();
		}

		@Override
		protected Void doInBackground(ArrayList<City>... cities)
		{
			WebServiceClient.getWeather(cities[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			progress.cancel();
			Toast.makeText(CityListActivity.this,
					getString(R.string.dataRefreshed), Toast.LENGTH_LONG) .show();
		}
	}
}
