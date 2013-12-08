package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

public class ListParser {

	private static final String FILE_EXTENSION = ".png";
	private final List<PogodaZListy> list;
	private List<HourlyForecast> godzinowa;
	private List<ForecastDay> wielodniowa;
	String rodzajListy;

	public ListParser(String rodzajListy) {
		this.list = new ArrayList<PogodaZListy>();
		// dane okreœlaj¹ce zawartoœæ listy
		this.rodzajListy = rodzajListy;
	}

	// funkcja zwracaj¹ca zawartoœæ listy
	public List<PogodaZListy> getList() {
		return this.list;
	}

	// wstawianie zawartoœci do list
	public void parse() {

		// wczytywana jest prognoza godzinowa:
		if (rodzajListy.equals("godzinowa")) {

			godzinowa = PogodaGodz.getList();
			int length = godzinowa.size();

			for (int i = 0; i < length; i++) {
				HourlyForecast pog = new HourlyForecast();
				pog = godzinowa.get(i);

				//aktualna data
				String data = pog.czas.monthDay + " " + pog.monthAbbrev + " "
						+ pog.czas.year;
							
				//formatowanie wyœwietlania godziny
				String godzina;
				if (pog.czas.hour < 10) {
					godzina = "0" + pog.czas.hour + ":" + pog.czas.minute + "0";
				} else {
					godzina = pog.czas.hour + ":" + pog.czas.minute + "0";
				}

				String warunkiPog = pog.condition;
				
				//formatowanie warunków pogodowych, aby zosta³y wyœwietlane zaczynaj¹c siê od du¿ej litery
				char first = Character.toUpperCase(warunkiPog.charAt(0));
				String warunkiUpperCase = first + warunkiPog.substring(1);

				//koñcowy wyœwietlany tekst
				final String tekst = godzina + ", " + pog.weekday_name + " "
						+ data + "\n" + warunkiUpperCase + "\nTemp. "
						+ pog.tempC + "°C";
				
				//wstawianie ikony zale¿nie od wschodu i zachodu s³oñca
				final String nazwaIkony = pog.icon;

				String folder;
				Astronomy astronomia = PogodaGodz.getAstronomy();
				if ((pog.czas.hour <= astronomia.sunrise.hour)
						|| (pog.czas.hour >= astronomia.sunset.hour))
					folder = "night/";
				else
					folder = "day/";

				//kontruowanie obiektu PogodaZListy
				PogodaZListy pogoda = new PogodaZListy(tekst, nazwaIkony
						+ FILE_EXTENSION, folder);
				Log.i(String.valueOf(pog.czas.hour), pog.condition);

				//dodanie elementu do listy
				this.list.add(pogoda);
			}
		}

		// wczytywana jest prognoza 10dniowa:
		else {

			wielodniowa = Pogoda10dni.getList();
			int length = wielodniowa.size();

			for (int i = 0; i < length; i++) {
				ForecastDay pog = new ForecastDay();
				pog = wielodniowa.get(i);

				String metric = pog.fcttextMetric;

				//spolszczenie prognozy "Light Wind"
				metric = metric.replace("Light Wind", "Lekki wiatr");

				// Time now = new Time();
				// now.setToNow();
				// String data =
				// String.valueOf(now.MONTH_DAY)+"."+String.valueOf(now.MONTH)+"."+String.valueOf(now.YEAR);

				//koñcowy wyœwietlany tekst
				final String tekst = pog.title + ":\n" + metric;
				final String nazwaIkony = pog.icon;

				//ustawienie ikony
				String folder;
				if (pog.title.contains("wieczór i noc"))
					folder = "night/";

				else
					folder = "day/";

				PogodaZListy pogoda = new PogodaZListy(tekst, nazwaIkony
						+ FILE_EXTENSION, folder);

				//dodanie elementu do listy
				this.list.add(pogoda);
			}
		}

	}

}