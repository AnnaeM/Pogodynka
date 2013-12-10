package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Wypoczynek extends ListActivity {

	public ArrayList<String> lista;
	public String pogoda;
	public ForecastDay dzien;
	public String city;
	public String latitude;
	public String longitude;
	public Double temp;
	public String wind;
	public String dzienTygodnia;
	public int miesiac;
	public int godzina;
	public JSONObject jObject;
	public char poraRoku;
	public char poraDnia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wypoczynek);

		lista = new ArrayList<String>();

		// je�li pobrano pogod�
		if (ForecastActivity._mainActivity != null) {
			List<ForecastDay> simple10days = ForecastActivity._mainActivity.simple10day;
			dzien = simple10days.get(0);
			Log.i("WYPOCZYNEK - dzien", dzien.toString());
			if (ForecastActivity._mainActivity.display_location != null) {
				Log.i("WYPOCZYNEK", "Nie jest nullem");
				try {
					// pobranie potrzebnych danych
					city = ForecastActivity._mainActivity.display_location
							.getString("city");
					latitude = ForecastActivity._mainActivity.display_location
							.getString("latitude");
					longitude = ForecastActivity._mainActivity.display_location
							.getString("longitude");
					temp = Double
							.valueOf(ForecastActivity._mainActivity.cndtns.temperaturaOdczuwalna);
					pogoda = ForecastActivity._mainActivity.current_observation
							.getString("weather");

					// Log.i("Pogoda", pogoda.toString());

					// gdy stacja meteorologiczna nie poda rodzaju aktualnej
					// pogody zostanie ona pobrana z prognozy godzinowej
					if (pogoda.equals("")) {
						// bo w Pu�awach nie ma pogody :c
						pogoda = ForecastActivity._mainActivity.hourlyForecast
								.get(0).condition;
						Log.i("Pogoda awaryjna", pogoda);
					}
					wind = ForecastActivity._mainActivity.current_observation
							.getString("wind_kph");
					// godzina =Integer.parseInt(dzien.data.hour);
					miesiac = Integer.parseInt(dzien.data.month);
					dzienTygodnia = dzien.data.weekDay;

					// wyszukanie godziny pobranej z serwera
					String godzinaString = "";
					String aktualnaGodzina = ForecastActivity._mainActivity.aktualnaGodzina;
					int i = 0;
					while (aktualnaGodzina.charAt(i) != ',') {
						i++;
					}

					godzinaString = godzinaString
							+ aktualnaGodzina.charAt(i + 2)
							+ aktualnaGodzina.charAt(i + 3);
					godzina = Integer.parseInt(godzinaString);

					poraRoku();
					wyborWypoczynku();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.i("B��d", "Pusty parametr");
			}
		}

		else {
			Log.i("B��d", "Pusty parametr");
		}

		//gdy nie uda�o si� �adnego rodzaju wypoczynku
		if (lista.isEmpty()) {
			lista.add("Zosta� w domu");
		}

		//utworzenie listy
		ArrayAdapter<String> array = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lista);
		setListAdapter(array);
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			//gdy zostanie wybrany element z listy
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// wyszukiwanie w Google Maps
				String wyszukiwanie = fraza((String) ((TextView) view)
						.getText());

				if (!wyszukiwanie.equals("1")) {
					String uri = "https://maps.google.pl/maps?q=" + city + "+"
							+ wyszukiwanie;
					Log.i("URL", uri);
					startActivity(new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
				}
			}
		});
	}

	public void poraDnia() {

		Log.i("Godzina", String.valueOf(godzina));
		if ((godzina >= 6) && (godzina < 10))
			poraDnia = 'r'; // ranek
		else if ((godzina >= 10) && (godzina < 14))
			poraDnia = 'p'; // "poludnie";
		else if ((godzina >= 14) && (godzina < 18))
			poraDnia = 'o'; // "popo�udnie";
		else if ((godzina >= 18) && (godzina < 22))
			poraDnia = 'w'; // "wiecz�r";
		else if ((godzina >= 22) && (godzina < 1))
			poraDnia = 'n'; // "noc";
		else
			poraDnia = 'g'; // "g��boka noc";

		Log.i("Pora dnia", String.valueOf(poraDnia));

	}

	public void poraRoku() {

		// mo�na doda� np. przedwio�nie

		if ((miesiac == 4) || (miesiac == 5)) { // kwiecien-maj
			poraRoku = 'w';
		} else if ((miesiac >= 6) && (miesiac <= 8)) { // czerwiec-sierpien
			poraRoku = 'l';
		} else if ((miesiac >= 9) && (miesiac <= 11)) { // wrzesien-list
			poraRoku = 'j';
		} else // grudzien-marzec
		{
			poraRoku = 'z';
		}

	}

	public void wyborWypoczynku() {

		poraDnia();
		//ujednolicenie znak�w w pogodzie
		String pogoda2 = pogoda.toLowerCase();
		if ((pogoda2.equals("pogodnie")) || (pogoda2.equals("przewaga chmur"))
				|| (pogoda2.equals("ob�oki zanikaj�ce"))
				|| (pogoda2.equals("niewielkie zachmurzenie"))
				|| (pogoda2.equals("pochmurno")))
			ladnaPogoda();

		else if ((pogoda2.contains("deszcz"))
				|| (pogoda2.contains("przelotne deszcze")))
			deszczowaPogoda();

		else if (pogoda2.contains("m�awka"))
			deszczowaPogoda();

		else if (pogoda2.equals("p�atki mg�y"))
			ladnaPogoda();
		else if (pogoda2.contains("zamglenia"))
			ladnaPogoda();
		else if (pogoda2.equals("g�sta mg�a"))
			innaPogoda();
		else if (pogoda2.contains("mg�a"))
			ladnaPogoda();

		else if (pogoda2.equals("g�sty �nieg"))
			deszczowaPogoda();
		else if (pogoda2.contains("�nieg"))
			ladnaPogoda();
		else if (pogoda2.contains("�nie�ek"))
			ladnaPogoda();

		else
			// listArray.add("Nieznany rodzaj pogody");
			innaPogoda();

		// nie ma burzy - i tak powinno by� "zosta� w domu

		zalezne();
	}

	public void ladnaPogoda() {
		if ((temp > -30) && (temp < 35)) {
			podstawowe();
			okazjonalne();
		}

		podDachem();

	}

	public void deszczowaPogoda() {
		podDachem();

	}

	public void innaPogoda() {
		// g�sta mgla itd
		if ((temp > -30) && (temp < 35)) {
			lista.add("Spacer");
			lista.add("Spotkanie z przyjaci�mi");
			lista.add("Spacer z psem");
		}
		
		podDachem();

	}

	public void podstawowe() {
		lista.add("Spacer");
		lista.add("Spotkanie z przyjaci�mi");
		lista.add("Spacer z psem");
		lista.add("Fotografowanie");
		lista.add("Rysowanie krajobrazu");
		// lista.add("Gra na gitarze");

	}

	public void podDachem() {
		if ((poraDnia == 'p') || (poraDnia == 'o') || (poraDnia == 'w')) {
			lista.add("Kino");
			lista.add("Kawiarnia");
			lista.add("Restauracja");
			lista.add("Kr�gle");
			lista.add("Bilard");
			lista.add("Muzeum");
			lista.add("Biblioteka");
			lista.add("Teatr");
			lista.add("Aquapark");
			lista.add("Zakupy");
			lista.add("Zaj�cia plastyczne");
			lista.add("Zaj�cia muzyczne");
		}
		if ((poraDnia == 'w') || (poraDnia == 'n') || (poraDnia == 'g')) {
			lista.add("Impreza");
			lista.add("Randka w ciemno");
			lista.add("Pub");
			lista.add("Koncert");
		}

	}

	public void okazjonalne() { 	
		 lista.add("Id� na wydarzenie w mie�cie");
	}

	public void zalezne() {

		int wschodSlonca, zachodSlonca;

		if (ForecastActivity._mainActivity != null) {
			wschodSlonca = ForecastActivity._mainActivity.astronomia.sunrise.hour;
			zachodSlonca = ForecastActivity._mainActivity.astronomia.sunset.hour;
		} else {
			wschodSlonca = Integer.parseInt("wschod");
			zachodSlonca = Integer.parseInt("zachod");

		}

		Log.i("Zachod slonca", String.valueOf(zachodSlonca));
		String pogoda2 = pogoda.toLowerCase();
		if ((pogoda2.equals("pogodnie"))
				|| (pogoda2.equals("niewielkie zachmurzenie"))
				|| pogoda2.equals("ob�oki zanikaj�ce")) {
			if ((godzina >= wschodSlonca - 1) && (godzina <= wschodSlonca)) {
				lista.add("Podziwiaj wsch�d s�o�ca");
			}

			if ((godzina >= zachodSlonca - 1) && (godzina <= zachodSlonca)) {
				lista.add("Podziwiaj zach�d s�o�ca");
			}

			if ((godzina > zachodSlonca + 1) || (godzina < wschodSlonca - 1)) {
				lista.add("Podziwiaj gwiazdy");
			}

		}
		
		if ((godzina <= zachodSlonca) && (godzina >= wschodSlonca))
				lista.add("Podziwiaj niebo");
		

		if (poraRoku != 'z') {

			if ((Double.valueOf(wind) >= 5) && (Double.valueOf(wind) <= 20)&& (godzina > wschodSlonca)
					&& (godzina < zachodSlonca))
				lista.add("Puszczanie latawca");

			if ((godzina < zachodSlonca) && (godzina > wschodSlonca)) {

				lista.add("Wyjazd na dzia�k�/wie�");
				lista.add("Wyprawa do lasu");
				if (pogoda2.equals("pogodnie")) {
					lista.add("Opalanie");
					lista.add("Piknik");
				}
			}
		}

	}

	public String fraza(String wybrany) {
		String f = "";

		if ((wybrany.equals("Spacer")) || (wybrany.equals("Spacer z psem"))
				|| (wybrany.equals("Fotografowanie"))
				|| (wybrany.equals("Rysowanie krajobrazu"))
				|| (wybrany.equals("Piknik"))) {
			f = "Park";
		} else if ((wybrany.equals("Impreza"))
				|| (wybrany.equals("Randka w ciemno"))) {
			f = "Club";
		} else if (wybrany.equals("Zakupy")) {
			f = "Centrum+handlowe";
		} else if (wybrany.equals("Zoo")) {
			f = "Ogr�d+zoologogiczny";
		} else if ((wybrany.equals("Zaj�cia plastyczne"))
				|| (wybrany.equals("Zaj�cia muzyczne"))) {
			f = "Dom+kultury";
		} else if ((wybrany.equals("Podziwiaj wsch�d s�o�ca"))
				|| (wybrany.equals("Podziwiaj zach�d s�o�ca"))
				|| (wybrany.equals("Podziwiaj gwiazdy"))
				|| (wybrany.equals("Podziwiaj niebo"))
				|| (wybrany.equals("Puszczanie latawca"))
				|| (wybrany.equals("Wyjazd na dzia�k�/wie�"))
				|| (wybrany.equals("Opalanie"))) {
			f = "1";
		} else if ((wybrany.equals("Kawiarnia"))||(wybrany.equals("Spotkanie z przyjaci�mi"))) {
			f = "Kawiarnia";
		}

		else {
			wybrany = wybrany.replace(' ', '+');
			f = wybrany;
		}

		return f;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent intent = new Intent(this, Info.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
}
