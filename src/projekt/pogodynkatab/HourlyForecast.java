package projekt.pogodynkatab;

import android.text.format.Time;

public class HourlyForecast {
	public Time czas;
	public String monAbbrev; //skrót nazwy miesi¹ca po ang
	public String monthAbbrev; //skrót nazwy miesi¹ca po polsku
	public String pretty;
	public String weekdayNameAbbrev; //skrót dnia tygodnia
	public String weekdayNameNight; //na noc i wieczór
	public String weekday_name;
	//public String tz;// timezone
	//public String ampm;
	//public String epoch; //czas unixowy
	public String tempC;
	public String dewpointC; //punkt rosy
	public String condition; //s³owny krótki opis
	public String icon;
	public String iconUrl;
	public String fctcode;
	public String sky;
	public String windKph; 
	public String windDir;
	public String windDegrees;
	//public String uvi; //ultraviolet index (1-16, where 16 is extreme) 
	public String humidity;
	public String windchill; //if <-100 jest Ÿle
	public String heatindex; //if <-100 jest Ÿle
	public String feelslike;
	public String qpf;
	public String snow;
	public String pop;
	public String pressure; //MSLP - mean sea level pressure- ciœnienie?
}
