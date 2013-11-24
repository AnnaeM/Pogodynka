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
						// bo w Pu�awach nie ma pogody :c
						String pogodaAwaryjna;
						pogodaAwaryjna = ForecastActivity._mainActivity.txt10day
								.get(0).fcttext.toString();
						Log.i("Pusta pogoda", pogodaAwaryjna);

						int i = 0;
						while (pogodaAwaryjna.charAt(i) != '.') {
							pogoda = pogoda + pogodaAwaryjna.charAt(i);
							i++;

						}
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
			try {
				jObject = new JSONObject(getIntent().getStringExtra("Pogoda"));
				Log.i("JSON w sportach", jObject.toString());

				city = jObject.getString("city");
				temp = jObject.getDouble("feelslike_c"); // temp odczuwalna
				pogoda = jObject.getString("weather");
				// miesiac = jObject.getInt("month");
				String m = jObject.getString("month");
				miesiac = Integer.parseInt(m);

				String godzinaString = "";
				String aktualnaGodzina = ForecastActivity._mainActivity.aktualnaGodzina;
				int i = 0;
				while (aktualnaGodzina.charAt(i) != ',') {
					i++;
				}

				godzinaString = godzinaString + aktualnaGodzina.charAt(i + 2)
						+ aktualnaGodzina.charAt(i + 3);
				godzina = Integer.parseInt(godzinaString);

				dzienTygodnia = jObject.getString("weekDay");
				poraRoku = poraRoku();
				poraDnia = poraDnia();

				wyborSportow();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (listArray.isEmpty()) {
			listArray.add("Zosta� w domu");
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
				
				String wyszukiwanie = fraza((String) ((TextView) view).getText());
				
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
		else if (pogoda2.equals("ob�oki zanikaj�ce"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("�nieg"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("niewielkie zachmurzenie"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("deszcz"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("lekki deszcz"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("pochmurno"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("p�atki mg�y"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("lekkie przelotne deszcze"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("lekka m�awka"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("zamglenia"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("m�awka"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("mg�a"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("lekka mg�a"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("cz�ciowe zamglenia"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("g�sta mg�a"))
			innaPogoda(poraDnia);
		else
			//listArray.add("Nieznany rodzaj pogody");
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
			pora = 'o'; // "popo�udnie";
		else if ((godzina >= 18) && (godzina < 22))
			pora = 'w'; // "wiecz�r";
		else if ((godzina >= 22) && (godzina < 1))
			pora = 'n'; // "noc";
		else
			pora = 'g'; // "g��boka noc";

		Log.i("Pora dnia", String.valueOf(pora));

		return pora;
	}

	public char poraRoku() {

		// mo�na doda� np. przedwio�nie
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
	
	public void innaPogoda(char poraDnia){
		//g�sta mgla itd
		if((temp > -30) && (temp < 35)){
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
			listArray.add("Siatk�wka");
			listArray.add("Koszyk�wka");
			listArray.add("Pi�ka no�na");
			listArray.add("Badminton");
			listArray.add("Squash");
			listArray.add("Si�ownia");
			listArray.add("Szermierka");
			listArray.add("�ucznictwo");
			listArray.add("Strzelnica");
			listArray.add("Wspinaczka");
			listArray.add("Trening sztuk walki");
			listArray.add("Basen");
			listArray.add("Ping-pong");
			listArray.add("Gocarty");
		}
	}

	public void naDworze() {

		if ((poraDnia == 'p') || (poraDnia == 'o') || (poraDnia == 'w')) {
			if (poraRoku != 'z') {
				listArray.add("BMX");
				listArray.add("Quady");				
				listArray.add("Golf");
				listArray.add("Jazda konna");
				listArray.add("Paintball");
				listArray.add("Tenis");
			}

			else {
				listArray.add("�y�wy");
				listArray.add("Narciarstwo");
				listArray.add("Hokej");
				listArray.add("Sanki");

			}
		}

	}

	public void okazjonalne() {
		listArray.add("P�ywanie");
		listArray.add("Kajaki");
		listArray.add("P�ywanie ��dk�"); // pontonem?
		listArray.add("Nurkowanie");
		listArray.add("Narty wodne");
		listArray.add("Pi�ka wodna");

		listArray.add("Serfowanie");
		listArray.add("Siatk�wka pla�owa");

	}

	//gdy b�dzie zaznaczony checkbox
	public void ekstremalne() {
		listArray.add("Parkour");
		listArray.add("Bungee");
		listArray.add("Paralotnia");
		listArray.add("Skok ze spadochronem");
		listArray.add("Windsurfing");
		listArray.add("Lot balonem");
	}
	
	
	
	public String fraza(String wybrany){
		String f ="";
		
		if(wybrany.equals("Rower")){
			f = "�cie�ka+rowerowa";
		}
		else if(wybrany.equals("Siatk�wka")){
			f = "Boisko+siatk�wka";
		}
		else if(wybrany.equals("Koszyk�wka")){
			f = "Boisko+koszyk�wka";
		}
		else if(wybrany.equals("Pi�ka no�na")){
			f = "Boisko+do+pi�ki+no�nej";
		}
		else if(wybrany.equals("Wspinaczka")){
			f = "�ciana+wspinaczkowa";
		}
		else if(wybrany.equals("Tenis")){
			f = "Kort+tenisowy";
		}
		else if(wybrany.equals("Jazda konna")){
			f = "Stadnina+koni";
		}
		else if((wybrany.equals("�y�wy"))||(wybrany.equals("Hokej"))){
			f = "Lodowisko";
		}
		else if(wybrany.equals("Narciarstwo")){
			f = "Stok+narciarski";
		}
		else if(wybrany.equals("Trening sztuk walki")){
			f = "Szko�a+sztuk+walki";
		}
		else{			
			wybrany = wybrany.replace(' ','+');		
			f = wybrany;
		}
		return f;
		
	}

}
