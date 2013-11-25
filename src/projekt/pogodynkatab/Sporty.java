package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Sporty extends ListActivity {
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
	public ArrayList<String> listArray;
	public JSONObject jObject;

	public char poraRoku;
	public char poraDnia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sporty);

		listArray = new ArrayList<String>();

		if (ForecastActivity._mainActivity != null) {
			List<ForecastDay> simple10days = ForecastActivity._mainActivity.simple10day;
			dzien = simple10days.get(0);
			Log.i("SPORTY - dzien", dzien.toString());
			if (ForecastActivity._mainActivity.display_location != null) {
				Log.i("SPORTY", "Nie jest nullem");
				try {
					city = ForecastActivity._mainActivity.display_location
							.getString("city");
					latitude = ForecastActivity._mainActivity.display_location
							.getString("latitude");
					longitude = ForecastActivity._mainActivity.display_location
							.getString("longitude");
					temp = Double
							.valueOf(ForecastActivity._mainActivity.current_observation
									.getString("feelslike_c"));
					pogoda = ForecastActivity._mainActivity.current_observation
							.getString("weather");

					Log.i("Pogoda", pogoda.toString());
					if (pogoda.equals("")) {
						// bo w Pu³awach nie ma pogody :c
						pogoda = ForecastActivity._mainActivity.hourlyForecast.get(0).condition;					
						Log.i("Pogoda awaryjna", pogoda);
					}
					wind = ForecastActivity._mainActivity.current_observation
							.getString("wind_kph");
					// godzina =Integer.parseInt(dzien.data.hour);

					miesiac = Integer.parseInt(dzien.data.month);

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
					Log.i("Wyluskana godzina", String.valueOf(godzina));

					dzienTygodnia = dzien.data.weekDay;
					poraRoku = poraRoku();
					poraDnia = poraDnia();

					wyborSportow();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Log.i("COSTAM", "Jest nullem :C");
			}
		}

		else {
			Log.i("B³¹d","Pusty parametr");

		}

		if (listArray.isEmpty()) {
			listArray.add("Zostañ w domu");
		}

		ArrayAdapter<String> array = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listArray);
		setListAdapter(array);
		ListView listView = getListView();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),((TextView)
				// view).getText(), Toast.LENGTH_SHORT).show();

				// String uri = "http://maps.google.com/maps?saddr="+"geo:"+
				// latitude + "," + longitude +"&q="+((TextView)
				// view).getText();

				String wyszukiwanie = fraza((String) ((TextView) view)
						.getText());

				String uri = "https://maps.google.pl/maps?q=" + city + "+"
						+ wyszukiwanie;
				Log.i("URL", uri);
				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(uri)));

			}

		});
	}

	public void wyborSportow() {

		char poraDnia = poraDnia();
		// pogoda = pogoda.toLowerCase();
		String pogoda2 = pogoda.toLowerCase();
		if (pogoda2.equals("pogodnie")) {
			ladnaPogoda(poraDnia);
		} else if (pogoda2.equals("przewaga chmur"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("ob³oki zanikaj¹ce"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("œnieg"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("niewielkie zachmurzenie"))
			ladnaPogoda(poraDnia);
		else if ((pogoda2.equals("deszcz"))||(pogoda2.equals("lekki deszcz"))||(pogoda2.equals("gêsty deszcz")))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("pochmurno"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("p³atki mg³y"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("lekkie przelotne deszcze"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("lekka m¿awka"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("zamglenia"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("m¿awka"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("mg³a"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("lekka mg³a"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("czêœciowe zamglenia"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("gêsta mg³a"))
			innaPogoda(poraDnia);
		else if ((pogoda2.equals("lekki œnie¿ek"))||(pogoda2.equals("œnie¿ek")))
			ladnaPogoda(poraDnia);
		else
			// listArray.add("Nieznany rodzaj pogody");
			innaPogoda(poraDnia);

	}

	public char poraDnia() {

		char pora;
		Log.i("Godzina", String.valueOf(godzina));
		if ((godzina >= 6) && (godzina < 10))
			pora = 'r'; // ranek
		else if ((godzina >= 10) && (godzina < 14))
			pora = 'p'; // "poludnie";
		else if ((godzina >= 14) && (godzina < 18))
			pora = 'o'; // "popo³udnie";
		else if ((godzina >= 18) && (godzina < 22))
			pora = 'w'; // "wieczór";
		else if ((godzina >= 22) && (godzina < 1))
			pora = 'n'; // "noc";
		else
			pora = 'g'; // "g³êboka noc";

		Log.i("Pora dnia", String.valueOf(pora));

		return pora;
	}

	public char poraRoku() {

		// mo¿na dodaæ np. przedwioœnie
		char c;

		if ((miesiac == 4) || (miesiac == 5)) { // kwiecien-maj
			c = 'w';
		} else if ((miesiac >= 6) && (miesiac <= 8)) { // czerwiec-sierpien
			c = 'l';
		} else if ((miesiac >= 9) && (miesiac <= 11)) { // wrzesien-list
			c = 'j';
		} else // grudzien-marzec
		{
			c = 'z';
		}

		return c;

	}

	public void ladnaPogoda(char poraDnia) {
		if ((temp > -30) && (temp < 35)) {
			standardowe();
			naHali();
			naDworze();
		}
	}

	public void deszczowaPogoda(char poraDnia) {
		naHali();
	}

	public void innaPogoda(char poraDnia) {
		// gêsta mgla itd
		if ((temp > -30) && (temp < 35)) {
			listArray.add("Bieganie");
			listArray.add("Joga");
			listArray.add("Nordic walking");
		}
		naHali();

	}

	public void standardowe() {
		listArray.add("Bieganie");
		listArray.add("Rower");
		listArray.add("Joga");
		listArray.add("Nordic walking");
		listArray.add("Rolki");
		listArray.add("Wrotki");
		listArray.add("Deskorolka");

	}

	public void naHali() {

		if ((poraDnia == 'p') || (poraDnia == 'o') || (poraDnia == 'w')) {
			if (!dzienTygodnia.equals("Niedziela")) {
				listArray.add("Siatkówka");
				listArray.add("Koszykówka");
				listArray.add("Pi³ka no¿na");
				listArray.add("Badminton");
				listArray.add("Squash");
				listArray.add("Si³ownia");
				listArray.add("Szermierka");
				listArray.add("£ucznictwo");
				listArray.add("Strzelnica");
				listArray.add("Wspinaczka");
				listArray.add("Trening sztuk walki");
				listArray.add("Basen");
				listArray.add("Ping-pong");
				listArray.add("Gocarty");
			}
		}
	}

	public void naDworze() {

		if ((poraDnia == 'p') || (poraDnia == 'o') || (poraDnia == 'w')) {
			if (poraRoku != 'z') {
				listArray.add("BMX");			
				listArray.add("Golf");
				listArray.add("Jazda konna");

				if (!dzienTygodnia.equals("Niedziela")) {
					listArray.add("Quady");
					listArray.add("Paintball");
					listArray.add("Tenis");
				}
			}

			else {
				listArray.add("£y¿wy");
				listArray.add("Narciarstwo");
				listArray.add("Hokej");
				listArray.add("Sanki");

			}
		}

	}

	public void okazjonalne() {
		listArray.add("P³ywanie");
		listArray.add("Kajaki");
		listArray.add("P³ywanie ³ódk¹"); // pontonem?
		listArray.add("Nurkowanie");
		listArray.add("Narty wodne");
		listArray.add("Pi³ka wodna");

		listArray.add("Serfowanie");
		listArray.add("Siatkówka pla¿owa");

	}

	// gdy bêdzie zaznaczony checkbox
	public void ekstremalne() {
		listArray.add("Parkour");
		listArray.add("Bungee");
		listArray.add("Paralotnia");
		listArray.add("Skok ze spadochronem");
		listArray.add("Windsurfing");
		listArray.add("Lot balonem");
	}

	public String fraza(String wybrany) {
		String f = "";

		if (wybrany.equals("Rower")) {
			f = "Œcie¿ka+rowerowa";
		} else if (wybrany.equals("Siatkówka")) {
			f = "Boisko+siatkówka";
		} else if (wybrany.equals("Koszykówka")) {
			f = "Boisko+koszykówka";
		} else if (wybrany.equals("Pi³ka no¿na")) {
			f = "Boisko+do+pi³ki+no¿nej";
		} else if (wybrany.equals("Wspinaczka")) {
			f = "Œciana+wspinaczkowa";
		} else if (wybrany.equals("Tenis")) {
			f = "Kort+tenisowy";
		} else if (wybrany.equals("Jazda konna")) {
			f = "Stadnina+koni";
		} else if ((wybrany.equals("£y¿wy")) || (wybrany.equals("Hokej"))) {
			f = "Lodowisko";
		} else if (wybrany.equals("Narciarstwo")) {
			f = "Stok+narciarski";
		} else if (wybrany.equals("Trening sztuk walki")) {
			f = "Szko³a+sztuk+walki";
		} else {
			wybrany = wybrany.replace(' ', '+');
			f = wybrany;
		}
		return f;

	}

}
