package manu.meteo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityListActivity extends ListActivity
{
	public static final String CITY = "manu.meteo.city";
	public final static int ADD_CITY_REQUEST = 1;

	private List<City> cityArrayList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		cityArrayList.add(new City("Paris", "France"));
		cityArrayList.add(new City("Tokyo", "Japan"));
		cityArrayList.add(new City("London", "United Kingdom"));
		cityArrayList.add(new City("Moscow", "Russia"));

		ArrayAdapter<City> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, cityArrayList);
		setListAdapter(adapter);

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				final City city = cityArrayList.get(i);

				new AlertDialog.Builder(CityListActivity.this).setTitle(getString(R.string.confirm))
						.setMessage(getString(R.string.removeCity))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int whichButton)
							{
								cityArrayList.remove(city);
								((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
								Log.i("CityListActivity", "city " + city + " removed");
							}
						}).setNegativeButton(android.R.string.no, null).show();

				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_city_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_add) {
			Intent intent = new Intent(this, AddCityActivity.class);
			startActivityForResult(intent, ADD_CITY_REQUEST);
		}
		else if (id == R.id.action_refresh) {
			new FetchData().execute(cityArrayList);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK) {
			if (requestCode == ADD_CITY_REQUEST) {
				City city = (City)data.getSerializableExtra(AddCityActivity.CITY_SAVED);
				cityArrayList.add(city);
				((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = new Intent(this, CityView.class);
		City city = cityArrayList.get(position);

		intent.putExtra(CITY, city);
		startActivity(intent);
	}


	private class FetchData extends AsyncTask<List<City>, Void, Void>
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
		protected Void doInBackground(List<City>... cities)
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
