package manu.meteo;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class WebServiceClient
{
	private static final String webServiceURL =
			"http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=%s&CountryName=%s";

	private static final String encoding = "UTF-8";

	public static void getWeather(List<City> cities)
	{
		XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();

		for (City city : cities) {
			URL url;
			URLConnection con;
			InputStream is = null;

			try {
				String name = URLEncoder.encode(city.getName(), encoding);
				String country = URLEncoder.encode(city.getCountry(), encoding);

				url = new URL(String.format(webServiceURL, name, country));
				con = url.openConnection();

				is = con.getInputStream();
				List<String> infos = xmlResponseHandler.handleResponse(is, encoding);

				if (infos.size() == 4) {
					city.setWindSpeedInKmh(infos.get(XMLResponseHandler.WIND));
					city.setAirTemperatureInDegreesCelsius(infos.get(XMLResponseHandler.TEMPERATURE));
					city.setPressureInhPa(infos.get(XMLResponseHandler.PRESSURE));
					city.setLastUpdate(infos.get(XMLResponseHandler.LAST_UPDATE));
				}
				else {
					Log.e("webServiceClient", "No data for " + city);
				}
			}
			catch (IOException e) {
				Log.e("webServiceClient", city + " : " + e.toString());
			}

			try {
				if (is != null)
					is.close();
			}
			catch (IOException e) {
				Log.e("webServiceClient", city + " : " + e.toString());
			}
		}
	}
}
