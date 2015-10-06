package manu.meteo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CityListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
	public static final String CITY_URI = "manu.meteo.city_uri";
	private static final int ADD_CITY_REQUEST = 1;
	private static final int LOADER_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        /*
		WeatherDatabase db = new WeatherDatabase(this);
		db.addCity(new City("Glasgow", "United Kingdom"));
		db.addCity(new City("Tokyo", "Japan"));
		db.addCity(new City("Milan", "Italy"));
		db.addCity(new City("Moscow", "Russia"));
		*/

		getLoaderManager().initLoader(LOADER_ID, null, this);

		SimpleCursorAdapter adapter = new  SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null,
				new String[] { WeatherDatabase.KEY_NAME, WeatherDatabase.KEY_COUNTRY },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(adapter);

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				final String name = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
				final String country = ((TextView)view.findViewById(android.R.id.text2)) .getText().toString();
				final String cityStr = name + " (" + country + ")";

				new AlertDialog.Builder(CityListActivity.this).setTitle(getString(R.string.confirm))
						.setMessage(String.format(getString(R.string.removeCity), cityStr))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int whichButton)
							{
                                int rowsDeleted = getContentResolver()
                                        .delete(WeatherContentProvider.getCityUri(country, name), null, null);
								getLoaderManager().restartLoader(LOADER_ID, null, CityListActivity.this);
                                Log.d("CityListActivity", "rowsDeleted =  " + rowsDeleted);
								Log.d("CityListActivity", "city " + cityStr + " removed");
							}
						})
						.setNegativeButton(android.R.string.no, null)
						.show();

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
			Intent serviceIntent = new Intent(this, FetchWeatherData.class);
			startService(serviceIntent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == ADD_CITY_REQUEST)
                getLoaderManager().restartLoader(LOADER_ID, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id)
	{
        final String name = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
        final String country = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

		Intent intent = new Intent(this, CityView.class);
		intent.putExtra(CITY_URI, WeatherContentProvider.getCityUri(country, name));
		startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
	{
		Log.d("CityListActivity", "onCreateLoader()");
		return new CursorLoader(this, WeatherContentProvider.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		Log.d("CityListActivity", "onLoadFinished()");
		((SimpleCursorAdapter)getListAdapter()).swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		Log.d("CityListActivity", "onLoaderReset()");
	}

	/*
	private class FetchData extends AsyncTask<List<City>, Void, Void>
	{
		private ProgressDialog progress;

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
			progress.dismiss();
			Toast.makeText(CityListActivity.this,
					getString(R.string.dataRefreshed), Toast.LENGTH_LONG).show();
		}
	}
	*/
}
