package manu.meteo;

import java.io.Serializable;

public class City implements Serializable
{
	private String name;
	private String country;
	private String lastUpdate;
	private String windSpeedInKmh;
	private String windDirection;
	private String pressureInhPa;
	private String airTemperatureInDegreesCelsius;

	public City(String name, String country)
	{
		this.name = name;
		this.country = country;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getLastUpdate()
	{
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public String getWindSpeedInKmh()
	{
		return windSpeedInKmh;
	}

	public void setWindSpeedInKmh(String windSpeedInKmh)
	{
		this.windSpeedInKmh = windSpeedInKmh;
	}

	public String getWindDirection()
	{
		return windDirection;
	}

	public void setWindDirection(String windDirection)
	{
		this.windDirection = windDirection;
	}

	public String getPressureInhPa()
	{
		return pressureInhPa;
	}

	public void setPressureInhPa(String pressureInhPa)
	{
		this.pressureInhPa = pressureInhPa;
	}

	public String getAirTemperatureInDegreesCelsius()
	{
		return airTemperatureInDegreesCelsius;
	}

	public void setAirTemperatureInDegreesCelsius(String airTemperatureInDegreesCelsius)
	{
		this.airTemperatureInDegreesCelsius = airTemperatureInDegreesCelsius;
	}
}
