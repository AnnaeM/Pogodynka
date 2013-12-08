package projekt.pogodynkatab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ForecastActivity extends TabActivity {
	public static String WEATHER_URL = "http://api.wunderground.com/api/c9d15b10ff3ed303/alerts/conditions/forecast/forecast10day/astronomy/hourly/lang:PL/q/";
	public String nazwaMiasta;
	public String json_string_response = null;
	public Location2 disLoc = null;
	public Location2 obsLoc = null;
	public JSONObject display_location = null;
	public JSONObject current_observation = null;
	public Conditions cndtns = null;
	public List<ForecastDay> txtForecast;
	public List<ForecastDay> simpleForecast;
	public List<HourlyForecast> hourlyForecast;
	public List<ForecastDay> txt10day;
	public List<ForecastDay> simple10day;
	public JSONObject jObject;
	public JSONObject Lublin = new JSONObject();
	public boolean pobrano;
	public String aktualnaGodzina;
	public Astronomy astronomia;
	//public double temperaturaOdczuwalna;

	public static ForecastActivity _mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pobrano = false;

		super.onCreate(savedInstanceState);
		_mainActivity = this;
		setContentView(R.layout.activity_forecast);
		// Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nazwaMiasta = extras.getString("Lokacja");
			pobierzPrognoze(nazwaMiasta);
		}

		if (pobrano) {
			// ZAK£ADKI
			TabHost tabHost = getTabHost();

			// aktualna pogoda
			TabSpec pogospec = tabHost.newTabSpec("Pogoda");
			pogospec.setIndicator("Pogoda",
					getResources().getDrawable(R.drawable.icon_pogoda_tab)); // tytu³
																				// i
																				// zdjecie
			Intent pogodaIntent = new Intent(this, Pogoda.class);
			pogospec.setContent(pogodaIntent);

			// godzinowa
			TabSpec godzSpec = tabHost.newTabSpec("Godzinowa");
			godzSpec.setIndicator("Godzinowa",
					getResources().getDrawable(R.drawable.icon_pog_godz_tab));
			Intent godzIntent = new Intent(this, PogodaGodz.class);
			godzSpec.setContent(godzIntent);

			// nastêpne dni
			TabSpec dni10Spec = tabHost.newTabSpec("10 dni");
			dni10Spec.setIndicator("10 dni",
					getResources().getDrawable(R.drawable.icon_pog_10dni_tab));
			Intent dni10Intent = new Intent(this, Pogoda10dni.class);
			dni10Spec.setContent(dni10Intent);

			// sporty
			TabSpec sportSpec = tabHost.newTabSpec("Sporty");
			sportSpec.setIndicator("Sporty",
					getResources().getDrawable(R.drawable.icon_sport_tab));
			Intent sportIntent = new Intent(this, Sporty.class);
			sportSpec.setContent(sportIntent);

			// wypoczynek
			TabSpec wypSpec = tabHost.newTabSpec("Wypoczynek");
			wypSpec.setIndicator("Wypoczynek",
					getResources().getDrawable(R.drawable.icon_wypoczynek_tab));
			Intent wypIntent = new Intent(this, Wypoczynek.class);
			wypSpec.setContent(wypIntent);

			// ubrania
			TabSpec ubraniaSpec = tabHost.newTabSpec("Ubrania");
			ubraniaSpec.setIndicator("Ubrania",
					getResources().getDrawable(R.drawable.icon_ubrania_tab));
			Intent ubraniaIntent = new Intent(this, Ubrania.class);
			ubraniaSpec.setContent(ubraniaIntent);

			// wstawienie TabSpec do TabHost
			tabHost.addTab(pogospec); // pogoda na teraz
			tabHost.addTab(godzSpec); // godzinowa
			tabHost.addTab(dni10Spec); // 10dniowa
			tabHost.addTab(sportSpec); // sporty
			tabHost.addTab(wypSpec); // wypoczynek
			tabHost.addTab(ubraniaSpec); // ubrania

		}
	}

	public void pobierzPrognoze(String city) {

		// ustawianie adresu URL w zale¿noœci od wybranej opcji (wpisane
		// miasto/gps)
		final String GET_WEATHER_URL;
		if (Character.isDigit(city.charAt(0))) { // jeœli przekazana zosta³a
													// d³ugoœæ i szerokoœæ geog.
			GET_WEATHER_URL = WEATHER_URL + city + ".json";
		} else {
			GET_WEATHER_URL = WEATHER_URL + "Poland/" + city + ".json";
		}

		String request = GET_WEATHER_URL;
		HttpResponse rp = null;

		// pobieranie danych pogodowych
		jObject = null;
		try {
			rp = (new DefaultHttpClient()).execute(new HttpPost(request));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (rp != null
				&& rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Log.i("Pogoda pobrana", "Pogoda pobrana");
			HttpEntity entity = rp.getEntity();
			InputStream inputStream = null;
			try {
				inputStream = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			try {
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// tworzenie obiektu JSON
			try {
				inputStream.close();
				json_string_response = total.toString();
				// Log.i("COS", json_string_response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				jObject = new JSONObject(json_string_response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				current_observation = jObject
						.getJSONObject("current_observation");
				JSONObject forecast = jObject.getJSONObject("forecast");

				// obróbka pobranych danych - tworzenie obiektów klas
				// pomocniczych
				obrabianieConditions(current_observation);
				obrabianieTxtForecast(forecast);
				obrabianieSimpleForecast(forecast);
				obrabianieAstronomii(jObject);
				obrabianieGodzinowej(jObject);

				JSONObject forecast10d = jObject.getJSONObject("forecast");

				/*
				 * Forecast10day txt_forecast
				 */
				JSONObject txt10 = forecast10d.getJSONObject("txt_forecast");
				JSONArray txt10forecast = txt10.getJSONArray("forecastday");
				this.txt10day = new ArrayList<ForecastDay>();
				for (int i = 0; i < txt10forecast.length(); i++) {
					JSONObject tmp = txt10forecast.getJSONObject(i);
					ForecastDay dzien = new ForecastDay();
					dzien.period = tmp.getString("period");
					dzien.icon = tmp.getString("icon");
					dzien.iconUrl = tmp.getString("icon_url");
					dzien.title = tmp.getString("title");
					dzien.fcttext = tmp.getString("fcttext");
					dzien.fcttextMetric = tmp.getString("fcttext_metric");
					dzien.pop = tmp.getString("pop");
					this.txt10day.add(dzien);
				}

				/*
				 * Forecast10day simple
				 */
				JSONObject smp10 = forecast10d.getJSONObject("simpleforecast");
				JSONArray smp10day = smp10.getJSONArray("forecastday");
				this.simple10day = new ArrayList<ForecastDay>();
				for (int i = 0; i < smp10.length(); i++) {
					JSONObject tmp = smp10day.getJSONObject(i);
					JSONObject tempDate = tmp.getJSONObject("date");
					Date data = new Date();
					ForecastDay dzien = new ForecastDay();
					// pomocnicze zmienne do konwersji ze stringa do int
					int pom1 = 0;
					String pom2 = "";
					JSONObject pom3 = new JSONObject();

					data.day = tempDate.getString("day");
					data.epoch = tempDate.getString("epoch");
					data.hour = tempDate.getString("hour");
					data.min = tempDate.getString("min");
					data.month = tempDate.getString("month");
					data.monthName = tempDate.getString("monthname");
					data.pretty = tempDate.getString("pretty");
					data.ampm = tempDate.getString("ampm");
					data.tzLong = tempDate.getString("tz_long");
					data.tzShort = tempDate.getString("tz_short");
					data.weekDay = tempDate.getString("weekday");
					data.weekDayShort = tempDate.getString("weekday_short");
					data.yday = tempDate.getString("yday");
					data.year = tempDate.getString("year");
					dzien.data = data;
					dzien.period = tmp.getString("period");
					dzien.icon = tmp.getString("icon");
					dzien.iconUrl = tmp.getString("icon_url");
					dzien.pop = tmp.getString("pop");
					dzien.conditions = tmp.getString("conditions");
					dzien.avehumidity = Integer.parseInt(tmp
							.getString("avehumidity"));
					dzien.minhumidity = Integer.parseInt(tmp
							.getString("minhumidity"));
					dzien.maxhumidity = Integer.parseInt(tmp
							.getString("maxhumidity"));
					// wiatr max i ave
					pom3 = tmp.getJSONObject("maxwind");
					dzien.maxwind_degrees = pom3.getString("degrees");
					dzien.maxwind_dir = pom3.getString("dir");
					dzien.maxwind_kph = Integer.parseInt(pom3.getString("kph"));
					dzien.maxwind_mph = Integer.parseInt(pom3.getString("mph"));
					pom3 = tmp.getJSONObject("avewind");
					dzien.avewind_degrees = pom3.getString("degrees");
					dzien.avewind_dir = pom3.getString("dir");
					dzien.avewind_kph = Integer.parseInt(pom3.getString("kph"));
					dzien.avewind_mph = Integer.parseInt(pom3.getString("mph"));
					// temperatury i opady
					pom3 = tmp.getJSONObject("high");
					dzien.highTempC = pom3.getString("celsius");
					pom3 = tmp.getJSONObject("low");
					dzien.lowTempC = pom3.getString("celsius");
					pom3 = tmp.getJSONObject("qpf_allday");
					dzien.qpfAllDay = pom3.getString("mm");
					pom3 = tmp.getJSONObject("snow_allday");
					dzien.snowAllDay = pom3.getString("cm");
					this.simple10day.add(dzien);

					pobrano = true;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block

				// na serwerze nie ma podanego miasta
				Log.i("ERROR", "Nie znaleziono miasta");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Przepraszamy!")
						.setMessage(
								"W podanym mieœcie nie ma stacji pomiarowej.")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// TODO Auto-generated method stub
										dialog.cancel();
										finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}

		}

		else {
			// brak po³¹czenia z serwerem
			Log.i("ERROR", "Nie uda³o siê po³¹czyæ z serwerem");

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Przepraszamy!")
					.setMessage("Nie uda³o siê po³¹czyæ z serwerem.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// TODO Auto-generated method stub
									dialog.cancel();
									finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	public void obrabianieGodzinowej(JSONObject jObject) throws JSONException {
		/*
		 * OBRABIANIE DANYCH HOURLY
		 */
		JSONArray hourly = jObject.getJSONArray("hourly_forecast");

		this.hourlyForecast = new ArrayList<HourlyForecast>();

		for (int i = 0; i < hourly.length(); i++) {

			HourlyForecast hf = new HourlyForecast();
			JSONObject pom3 = new JSONObject();
			JSONObject pom2 = new JSONObject();
			pom2 = hourly.getJSONObject(i);
			pom3 = pom2.getJSONObject("FCTTIME");
			Time pom = new Time();
			pom.hour = Integer.parseInt(pom3.getString("hour"));
			pom.second = Integer.parseInt(pom3.getString("sec"));
			pom.month = Integer.parseInt(pom3.getString("mon"));
			pom.year = Integer.parseInt(pom3.getString("year"));
			pom.yearDay = Integer.parseInt(pom3.getString("yday"));
			pom.monthDay = Integer.parseInt(pom3.getString("mday"));

			hf.czas = pom;

			hf.monAbbrev = pom3.getString("mon_abbrev");
			hf.monthAbbrev = pom3.getString("month_name_abbrev");
			hf.weekdayNameAbbrev = pom3.getString("weekday_name_abbrev");
			hf.weekdayNameNight = pom3.getString("weekday_name_night");
			hf.weekday_name = pom3.getString("weekday_name");
			hf.pretty = pom3.getString("pretty");
			hf.condition = pom2.getString("condition");
			pom3 = pom2.getJSONObject("dewpoint");
			hf.dewpointC = pom3.getString("metric");
			pom3 = pom2.getJSONObject("temp");
			hf.tempC = pom3.getString("metric");
			hf.fctcode = pom2.getString("fctcode");
			pom3 = pom2.getJSONObject("feelslike");
			hf.feelslike = pom3.getString("metric");
			pom3 = pom2.getJSONObject("heatindex");
			hf.heatindex = pom3.getString("metric");
			hf.humidity = pom2.getString("humidity");
			hf.icon = pom2.getString("icon");
			hf.iconUrl = pom2.getString("icon_url");
			hf.pop = pom2.getString("pop");
			pom3 = pom2.getJSONObject("mslp");
			hf.pressure = pom3.getString("metric");
			hf.sky = pom2.getString("sky");
			pom3 = pom2.getJSONObject("wspd");
			hf.windKph = pom3.getString("metric");
			pom3 = pom2.getJSONObject("wdir");
			hf.windDir = pom3.getString("dir");
			hf.windDegrees = pom3.getString("degrees");
			pom3 = pom2.getJSONObject("windchill");
			hf.windchill = pom3.getString("metric");
			pom3 = pom2.getJSONObject("qpf");
			hf.qpf = pom3.getString("metric");

			pom3 = pom2.getJSONObject("snow");
			hf.snow = pom3.getString("metric");

			hourlyForecast.add(hf);
			aktualnaGodzina = current_observation.getString("observation_time");
		}
	}

	public void obrabianieConditions(JSONObject current_observation)
			throws JSONException {
		// Obrabianie conditions

		disLoc = new Location2();
		display_location = current_observation
				.getJSONObject("display_location");
		disLoc.setFull(display_location.getString("full"));
		disLoc.setCity(display_location.getString("city"));
		disLoc.setCountry(display_location.getString("country"));
		disLoc.setCountry_iso3166(display_location.getString("country_iso3166"));
		disLoc.setElevation(display_location.getString("elevation"));
		disLoc.setLatitude(display_location.getString("latitude"));
		disLoc.setLongitude(display_location.getString("longitude"));

		obsLoc = new Location2();
		JSONObject observation_location = current_observation
				.getJSONObject("observation_location");
		obsLoc.setFull(observation_location.getString("full"));
		obsLoc.setCity(observation_location.getString("city"));
		obsLoc.setCountry(observation_location.getString("country"));
		obsLoc.setCountry_iso3166(observation_location
				.getString("country_iso3166"));
		obsLoc.setElevation(observation_location.getString("elevation"));
		obsLoc.setLatitude(observation_location.getString("latitude"));
		obsLoc.setLongitude(observation_location.getString("longitude"));

		cndtns = new Conditions();
		cndtns.dewpointC = current_observation.getString("dewpoint_c");
		cndtns.dewpointString = current_observation
				.getString("dewpoint_string");
		cndtns.feelslikeString = current_observation
				.getString("feelslike_string");
		cndtns.feelslikeC = current_observation.getString("feelslike_c");
		cndtns.heatIndexC = current_observation.getString("heat_index_c");
		cndtns.heatIndexString = current_observation
				.getString("heat_index_string");
		cndtns.icon = current_observation.getString("icon");
		cndtns.iconURL = current_observation.getString("icon_url");
		cndtns.localTimeRfc822 = current_observation
				.getString("local_time_rfc822");
		cndtns.localTzLong = current_observation.getString("local_tz_long");
		cndtns.localTzOffset = current_observation.getString("local_tz_offset");
		cndtns.localTzShort = current_observation.getString("local_tz_short");
		cndtns.observationEpoch = current_observation
				.getString("observation_epoch");
		cndtns.observationTime = current_observation
				.getString("observation_time");
		cndtns.observationTimeRfc822 = current_observation
				.getString("observation_time_rfc822");
		cndtns.pressureMb = current_observation.getString("pressure_mb");
		cndtns.pressureTrend = current_observation.getString("pressure_trend");
		cndtns.relativeHumidity = current_observation
				.getString("relative_humidity");
		cndtns.tempC = current_observation.getString("temp_c");
		cndtns.temperatureString = current_observation
				.getString("temperature_string");
		cndtns.UV = current_observation.getString("UV");
		cndtns.visibilityKm = current_observation.getString("visibility_km");
		cndtns.windChillC = current_observation.getString("windchill_c");
		cndtns.windChillString = current_observation
				.getString("windchill_string");
		cndtns.windDegrees = current_observation.getString("wind_degrees");
		cndtns.windDir = current_observation.getString("wind_dir");
		cndtns.windKph = current_observation.getString("wind_kph");
		cndtns.windMph = current_observation.getString("wind_mph");
		cndtns.windString = current_observation.getString("wind_string");
		cndtns.weather = current_observation.getString("weather");
		
		cndtns.temperaturaOdczuwalna = obliczTemperatureOdczuwalna(cndtns.tempC, cndtns.windKph);
		
		
	}

	public void obrabianieTxtForecast(JSONObject forecast) throws JSONException {
		JSONObject txtForecast = forecast.getJSONObject("txt_forecast");
		JSONArray txtForecastDay = txtForecast.getJSONArray("forecastday");
		this.txtForecast = new ArrayList<ForecastDay>();
		for (int i = 0; i < txtForecastDay.length(); i++) {
			JSONObject tmp = txtForecastDay.getJSONObject(i);
			ForecastDay dzien = new ForecastDay();
			dzien.period = tmp.getString("period");
			dzien.icon = tmp.getString("icon");
			dzien.iconUrl = tmp.getString("icon_url");
			dzien.title = tmp.getString("title");
			dzien.fcttext = tmp.getString("fcttext");
			dzien.fcttextMetric = tmp.getString("fcttext_metric");
			dzien.pop = tmp.getString("pop");
			this.txtForecast.add(dzien);
		}

	}

	public void obrabianieSimpleForecast(JSONObject forecast)
			throws JSONException {
		/*
		 * Obrabianie FORECAST - simpleforecast
		 */
		JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
		JSONArray splForecastDay = simpleForecast.getJSONArray("forecastday");
		this.simpleForecast = new ArrayList<ForecastDay>();
		for (int i = 0; i < splForecastDay.length(); i++) {
			JSONObject tmp = splForecastDay.getJSONObject(i);
			JSONObject tempDate = tmp.getJSONObject("date");
			Date data = new Date();
			ForecastDay dzien = new ForecastDay();
			// pomocnicze zmienne do konwersji ze stringa do int
			int pom1 = 0;
			String pom2 = "";
			JSONObject pom3 = new JSONObject();

			data.day = tempDate.getString("day");
			data.epoch = tempDate.getString("epoch");
			data.hour = tempDate.getString("hour");
			data.min = tempDate.getString("min");
			data.month = tempDate.getString("month");
			data.monthName = tempDate.getString("monthname");
			data.pretty = tempDate.getString("pretty");
			data.ampm = tempDate.getString("ampm");
			data.tzLong = tempDate.getString("tz_long");
			data.tzShort = tempDate.getString("tz_short");
			data.weekDay = tempDate.getString("weekday");
			data.weekDayShort = tempDate.getString("weekday_short");
			data.yday = tempDate.getString("yday");
			data.year = tempDate.getString("year");
			dzien.data = data;
			dzien.period = tmp.getString("period");
			dzien.icon = tmp.getString("icon");
			dzien.iconUrl = tmp.getString("icon_url");
			dzien.pop = tmp.getString("pop");
			dzien.conditions = tmp.getString("conditions");
			dzien.avehumidity = Integer.parseInt(tmp.getString("avehumidity"));
			dzien.minhumidity = Integer.parseInt(tmp.getString("minhumidity"));
			dzien.maxhumidity = Integer.parseInt(tmp.getString("maxhumidity"));
			// wiatr max i ave
			pom3 = tmp.getJSONObject("maxwind");
			dzien.maxwind_degrees = pom3.getString("degrees");
			dzien.maxwind_dir = pom3.getString("dir");
			dzien.maxwind_kph = Integer.parseInt(pom3.getString("kph"));
			dzien.maxwind_mph = Integer.parseInt(pom3.getString("mph"));
			pom3 = tmp.getJSONObject("avewind");
			dzien.avewind_degrees = pom3.getString("degrees");
			dzien.avewind_dir = pom3.getString("dir");
			dzien.avewind_kph = Integer.parseInt(pom3.getString("kph"));
			dzien.avewind_mph = Integer.parseInt(pom3.getString("mph"));
			// temperatury i opady
			pom3 = tmp.getJSONObject("high");
			dzien.highTempC = pom3.getString("celsius");
			pom3 = tmp.getJSONObject("low");
			dzien.lowTempC = pom3.getString("celsius");
			pom3 = tmp.getJSONObject("qpf_allday");
			dzien.qpfAllDay = pom3.getString("mm");
			pom3 = tmp.getJSONObject("snow_allday");
			dzien.snowAllDay = pom3.getString("cm");
			this.simpleForecast.add(dzien);
		}

	}

	public void obrabianieAstronomii(JSONObject jObject) throws JSONException {
		/*
		 * OBRABIANIE DANYCH ZWI¥ZANYCH Z ASTRONOMI¥-S£OÑCE I KSIÊ¯YÆ
		 */
		JSONObject astronomy = jObject.getJSONObject("moon_phase");
		astronomia = new Astronomy();
		astronomia.ageOfMoon = astronomy.getString("ageOfMoon");
		astronomia.phaseofMoon = astronomy.getString("phaseofMoon");

		JSONObject pom3 = new JSONObject();
		pom3 = astronomy.getJSONObject("sunrise");
		Time g = new Time();
		g.hour = Integer.parseInt(pom3.getString("hour"));
		g.minute = Integer.parseInt(pom3.getString("minute"));
		astronomia.moonrise = g;
		g = new Time();
		pom3 = astronomy.getJSONObject("sunset");
		g.hour = Integer.parseInt(pom3.getString("hour"));
		g.minute = Integer.parseInt(pom3.getString("minute"));
		astronomia.moonset = g;
		astronomy = jObject.getJSONObject("sun_phase");
		g = new Time();
		pom3 = astronomy.getJSONObject("sunrise");
		g.hour = Integer.parseInt(pom3.getString("hour"));
		g.minute = Integer.parseInt(pom3.getString("minute"));
		astronomia.sunrise = g;
		pom3 = astronomy.getJSONObject("sunset");
		g = new Time();
		g.hour = Integer.parseInt(pom3.getString("hour"));
		g.minute = Integer.parseInt(pom3.getString("minute"));
		astronomia.sunset = g;
	}

	public double obliczTemperatureOdczuwalna (String temperatura, String wiatr){
		double wynik=-999.999;      	
		
		double V=0.0;
		double tempWC = Double.valueOf(temperatura);
		
		V=Math.pow(Double.valueOf(wiatr), 0.16);
		wynik=13.12+(0.6215*tempWC)-(11.37*V)+(0.3965*tempWC*V);
			
		Log.i("Temp odczuwalna",String.valueOf(wynik));
		return wynik;
	}
}
