package manu.meteo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
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
	private static final String TAG = CityListActivity.class.getSimpleName();

	public static final String CITY_URI = "manu.meteo.city_uri";

	public static final String ACCOUNT_TYPE = "webservicex.net";
	public static final String ACCOUNT = "dummyaccount";

	private static final int LOADER_ID = 1;

	public static Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		account = createSyncAccount(this);

		getLoaderManager().initLoader(LOADER_ID, null, this);

		SimpleCursorAdapter adapter = new  SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null,
				new String[] { WeatherDatabase.KEY_NAME, WeatherDatabase.KEY_COUNTRY },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(adapter);


		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				final String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
				final String country = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
				final String cityStr = name + " (" + country + ")";

				new AlertDialog.Builder(CityListActivity.this).setTitle(getString(R.string.confirm))
						.setMessage(String.format(getString(R.string.removeCity), cityStr))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								int rowsDeleted = getContentResolver()
										.delete(WeatherContentProvider.getCityUri(country, name), null, null);
								Log.d(TAG, "rowsDeleted =  " + rowsDeleted);
								Log.d(TAG, "city " + cityStr + " removed");
							}
						})
						.setNegativeButton(android.R.string.no, null)
						.show();

				return true;
			}
		});
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
			Intent intent = new Intent(this, AddCityActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id)
	{
		final String name = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
		final String country = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

		Intent intent = new Intent(this, CityView.class);
		intent.putExtra(CITY_URI, WeatherContentProvider.getCityUri(country, name));
		startActivityForResult(intent, 0);
	}

	private static Account createSyncAccount(Context context)
	{
		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
		AccountManager accountManager = (AccountManager)context.getSystemService(ACCOUNT_SERVICE);
		accountManager.addAccountExplicitly(newAccount, null, null);

		return newAccount;
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
	{
		Log.d(TAG, "onCreateLoader()");
		return new CursorLoader(this, WeatherContentProvider.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		Log.d(TAG, "onLoadFinished()");
		((SimpleCursorAdapter)getListAdapter()).changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		Log.d(TAG, "onLoaderReset()");
	}
}
