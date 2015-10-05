package manu.meteo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDatabase extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "WeatherDatabase";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_WEATHER = "weather";
	public static final String KEY_ID = "_id";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_NAME = "name";
	public static final String KEY_LAST_UPDATE = "last_update";
	public static final String KEY_WIND = "wind";
	public static final String KEY_PRESSURE = "pressure";
	public static final String KEY_TEMPERATURE = "temperature";

	public WeatherDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_COUNTRY + " TEXT,"
				+ KEY_NAME + " TEXT,"
				+ KEY_LAST_UPDATE + " TEXT,"
				+ KEY_WIND + " TEXT,"
				+ KEY_PRESSURE + " TEXT,"
				+ KEY_TEMPERATURE + " TEXT,"
				+ "UNIQUE(" + KEY_COUNTRY + "," + KEY_NAME + ")"
				+ ")";
		sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
	{
		if (oldVersion != newVersion) {
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
			onCreate(sqLiteDatabase);
		}
	}

	public void addCity(City city)
	{
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_COUNTRY, city.getCountry());
		values.put(KEY_NAME, city.getName());
		values.put(KEY_LAST_UPDATE, city.getLastUpdate());
		values.put(KEY_WIND, city.getWindSpeedInKmh());
		values.put(KEY_PRESSURE, city.getPressureInhPa());
		values.put(KEY_TEMPERATURE, city.getAirTemperatureInDegreesCelsius());

		db.insert(TABLE_WEATHER, null, values);
		db.close();
	}

	public void deleteCity(String country, String name)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_WEATHER, KEY_COUNTRY + " = ? AND " + KEY_NAME + " = ?",
				new String[] { country, name });
		db.close();
	}

	/*
	public List<City> getAllCities()
	{
		SQLiteDatabase db = getReadableDatabase();
		List<City> cityList = new ArrayList<>();
		final String selectQuery = "SELECT * FROM " + TABLE_WEATHER;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				City city = new City(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
						cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
				cityList.add(city);
			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		return cityList;
	}
	*/

	public Cursor getAllCities()
	{
		SQLiteDatabase db = getReadableDatabase();
		final String selectQuery = "SELECT * FROM " + TABLE_WEATHER;

		return db.rawQuery(selectQuery, null);
	}
}
