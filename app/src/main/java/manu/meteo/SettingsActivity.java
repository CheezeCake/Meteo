package manu.meteo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}
}