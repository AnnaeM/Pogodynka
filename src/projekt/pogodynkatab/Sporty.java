package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
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
				
					Log.i("G³upi jestem",pogoda.toString());	
					if(pogoda.equals("")){
						//bo w Pu³awach nie ma pogody :c
						String pogodaAwaryjna;
						pogodaAwaryjna = ForecastActivity._mainActivity.txt10day.get(0).fcttext.toString();
						Log.i("Pusta pogoda", pogodaAwaryjna);
						
						int i=0;
						while(pogodaAwaryjna.charAt(i)!='.'){
							pogoda=pogoda+pogodaAwaryjna.charAt(i);
							i++;
						
					}
						Log.i("Pogoda awaryjna", pogoda);
					}
					wind = ForecastActivity._mainActivity.current_observation
							.getString("wind_kph");
					godzina =Integer.parseInt(dzien.data.hour);
					miesiac = Integer.parseInt(dzien.data.month);
					dzienTygodnia=dzien.data.weekDay;
					wyborSportow();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
						Toast.makeText(getApplicationContext(),
								((TextView) view).getText(), Toast.LENGTH_SHORT).show();
					}

				});

				
			} else {
				Log.i("COSTAM", "Jest nullem :C");
			}
		}
		
		else{
			try {
				jObject = new JSONObject(getIntent().getStringExtra("Pogoda"));
				Log.i("JSON w sportach", jObject.toString());
				
				city = jObject.getString("city");
				temp = jObject.getDouble("feelslike_c"); // temp odczuwalna
				pogoda = jObject.getString("weather");
				// miesiac = jObject.getInt("month");
				String m = jObject.getString("month");
				miesiac = Integer.parseInt(m);

				godzina = jObject.getInt("hour");
				dzienTygodnia = jObject.getString("weekDay");

				wyborSportow();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					Toast.makeText(getApplicationContext(),
							((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
public void wyborSportow(){

		
		char poraDnia = poraDnia();
		//pogoda = pogoda.toLowerCase();
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
		else if (pogoda2.equals("deszcz"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("lekki deszcz"))
			deszczowaPogoda(poraDnia);
		else if (pogoda2.equals("pochmurno"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("p³atki mg³y"))
			ladnaPogoda(poraDnia);
		else if (pogoda2.equals("lekkie przelotne deszcze"))
			deszczowaPogoda(poraDnia);
		else if(pogoda2.equals("lekka m¿awka"))	
			ladnaPogoda(poraDnia);
		else if(pogoda2.equals("zamglenia"))
			ladnaPogoda(poraDnia);
		else if(pogoda.equals("m¿awka"))
			deszczowaPogoda(poraDnia);
		else if(pogoda.equals("mg³a"))
			ladnaPogoda(poraDnia);
		else if(pogoda.equals("lekka mg³a"))
			ladnaPogoda(poraDnia);
		else if(pogoda.equals("czêœciowe zamglenia"))
			ladnaPogoda(poraDnia);
		else
			listArray.add("Nieznany rodzaj pogody");

	}

	public char poraDnia() {

		char pora;

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

		return pora;
	}

	public void ladnaPogoda(char poraDnia) {

		char poraRoku = poraRoku();

		switch (poraRoku) {
		case 'w': {
			// wiosna
			switch (poraDnia) {
			case 'r': {

				// jest ladna pogoda, wiosna, ranek, dowolny dzieñ tygodnia (bo
				// do 10 i tak wszystko zamkniête)

				if ((temp >= -5) && (temp <= 25)) {
					bieganie();
					standardowe();
				}
				break;
			}

			case 'p':
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest ladna pogoda, wiosna, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-10)&&(temp<0)){
						naHali();
					}
					else if ((temp >= 0) && (temp < 35)) {
						bieganie();
						standardowe();
						naHali();
						naDworze();

					}
				}
					else {

						// jest ladna pogoda, wiosna, po³udnie+dzien+popoludnie+wieczór,
						// niedziela
						if ((temp >= 5) && (temp <= 35)) {
							bieganie();
							standardowe();
							listArray.add("Jazda konna");
						}

					
				}
				break;
			}

			case 'n':
			case 'g': {
				// jest ladna pogoda, wiosna, noc+g³êboka noc, dowolny dzien
				if ((temp >= -5) && (temp <= 30)) {
					bieganie();
					standardowe();
				}
				break;
			}
			default: {
				Log.i("B³¹d", "Nie ma poryDnia");
			}

			}

			break;
		}
		case 'l': {// lato

			switch (poraDnia) {
			case 'r': {

				// jest ladna pogoda, lato, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)

				if ((temp >= 5) && (temp <= 30)) {
					bieganie();
					standardowe();
				}

				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest ladna pogoda, lato, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-5)&&(temp<5)){
						naHali();
					}
					else if ((temp >= 5) && (temp <= 35)) {
						bieganie();
						standardowe();
						naHali();
						naDworze();

					}
				}
					else {

						// jest ladna pogoda, lato, po³udnie+dzien+popoludnie+wieczór,
						// niedziela
						if ((temp >= 5) && (temp <= 35)) {
							bieganie();
							standardowe();
							listArray.add("Jazda konna");
						}					
					}
				break;
			}

			case 'n': 
			case 'g': {
				// jest ladna pogoda, lato, noc+g³êboka noc, dowolny dzien
				if ((temp >= 0) && (temp <= 30)) {
					bieganie();
					standardowe();
				}
				break;
			}
			default: {
				Log.i("B³¹d", "Nie ma poryDnia");
			}
				break;
			}
		}
		case 'j': { // jesien
			
			switch (poraDnia) {
			case 'r': {

				// jest ladna pogoda, jesien, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)

				if ((temp >= -5) && (temp <= 30)) {
					bieganie();
					standardowe();
				}

				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest ladna pogoda, jesien, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if((temp>=-30)&&(temp<-5)){						
						naHali();
						
					}
					else if ((temp >= -5) && (temp <= 35)) {
						bieganie();
						standardowe();
						naHali();
						naDworze();

					}
				}

					else {

						// jest ladna pogoda, jesien, po³udnie+dzien+popoludnie+wieczór,
						// niedziela
						if ((temp >= -5) && (temp <= 35)) {
							bieganie();
							standardowe();
							listArray.add("Jazda konna");
						}

					}
				
				break;
			}

			case 'n': 
			case 'g': {
				// jest ladna pogoda, jesien, noc+g³êboka noc, dowolny dzien
				if ((temp >= -5) && (temp <= 25)) {
					bieganie();
					standardowe();
				}

				break;
			}
			default: {
				Log.i("B³¹d", "Nie ma poryDnia");
			}
				break;
			}
			
			break;
		}
		case 'z': { // zima
			
			switch (poraDnia) {
			case 'r': {

				// jest ladna pogoda, zima, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)
			
				if ((temp >= -15) && (temp <= 20)) {
					bieganie();
					standardowe();
					listArray.add("Sanki");
				}

				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest ladna pogoda, zima, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-30)&&(temp<=-15)){				
						naHali();
					}
						
					else if ((temp >= -15) && (temp < 20)) {
						bieganie();
						standardowe();
						naHali();
						naDworze();
						wZimie();
						listArray.add("Sanki");

					}
				}
					else {

						// jest ladna pogoda, zima, po³udnie+dzien+popoludnie+wieczór,
						// niedziela
						if ((temp >= -10) && (temp <= 20)) {
							bieganie();
							standardowe();							
						}

					}
				
				break;
			}

			case 'n': 
			case 'g': {
				// jest ladna pogoda, zima, noc+g³êboka noc, dowolny dzien
				if ((temp >= -10) && (temp <= 20)) {
					bieganie();
					standardowe();
				}

				break;
			}
			default: {
				Log.i("B³¹d", "Nie ma poryDnia");
			}
				break;
			}
			
			break;
		}
		default: {
			Log.i("B³¹d","Z³a pora roku");
		}

		}
	}
	
	public void deszczowaPogoda(char poraDnia) {

		char poraRoku = poraRoku();

		switch (poraRoku) {
		case 'w': {
			// wiosna
			switch (poraDnia) {
			case 'r': {

				// jest deszczowa pogoda, wiosna, ranek, dowolny dzieñ tygodnia (bo
				// do 10 i tak wszystko zamkniête)

				if ((temp >= -5) && (temp <= 25)) {
					//co siê robi rano, kiedy pada?
					listArray.add("Œpij dalej");
					listArray.add("Zostañ w domu");
				}
				break;
			}

			case 'p':
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest deszczowa pogoda, wiosna, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-10)&&(temp<0)){
						naHali();
					}
					else if ((temp >= 0) && (temp < 35)) {					
						naHali();					

					}
				}
				
				break;
			}

			// w nocy i tak doda "Zostañ w domu"
			
			default: {		
				Log.i("Deszczowo", "Zostañ w domu");
			}
			}
			break;
		}
		case 'l': {// lato

			switch (poraDnia) {
			case 'r': {

				// jest deszczowa pogoda, lato, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)
				listArray.add("Czytaj ksi¹¿kê");
				listArray.add("Zostañ w domu");
				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest deszczowa pogoda, lato, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-5)&&(temp<=35)){
						naHali();
					}

				}
				break;
			}

			
			default: {
				Log.i("Deszczowo", "Zostañ w domu");
			}
				break;
			}
		}
		case 'j': { // jesien
			
			switch (poraDnia) {
			case 'r': {

				// jest deszczowa pogoda, jesien, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)
				listArray.add("Œpij dalej");
				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest deszczowa pogoda, jesien, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if((temp>=-20)&&(temp<=35)){						
						naHali();							
					}
					
				}
					
				
				break;
			}			
			default: {
				Log.i("Deszczowo", "Zostañ w domu");
			}
				break;
			}
			
			break;
		}
		case 'z': { // zima
			
			switch (poraDnia) {
			case 'r': {

				// jest deszczowa pogoda, zima, ranek, dowolny dzieñ tygodnia (bo do
				// 10 i tak wszystko zamkniête)
				listArray.add("Œpij dalej");
				break;
			}

			case 'p': 
			case 'o':
			case 'w': {
				if ((dzienTygodnia.equals("Poniedzia³ek"))
						|| (dzienTygodnia.equals("Wtorek"))
						|| (dzienTygodnia.equals("Œroda"))
						|| (dzienTygodnia.equals("Czwartek"))
						|| (dzienTygodnia.equals("Pi¹tek"))
						|| (dzienTygodnia.equals("Sobota"))) {

					// jest deszczowa pogoda, zima, po³udnie+dzien+popoludnie+wieczór, na
					// tygodniu + sobota
					
					if ((temp>=-30)&&(temp<=20)){				
						naHali();
					}																
				}
				break;
			}

			
			default: {
				Log.i("Deszczowo", "Zostañ w domu");
			}
				break;
			}
			
			break;
		}
		default: {
			Log.i("B³¹d","Z³a pora roku");
		}

		}
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

	public void bieganie() {
		listArray.add("Bieganie");

	}

	public void standardowe() {
		listArray.add("Rower");
		listArray.add("Joga");
		listArray.add("Nordic walking");
		listArray.add("Rolki");
		listArray.add("Wrotki");
		listArray.add("Deskorolka");

	}

	public void naHali() {

		listArray.add("Siatkówka");
		listArray.add("Koszykówka");
		listArray.add("Pi³ka no¿na");
		listArray.add("Badminton");
		listArray.add("Squash");
		listArray.add("Si³ownia");
		listArray.add("Szermierka");
		listArray.add("£ucznictwo");
		listArray.add("Strzelnica");
		listArray.add("Œciana wspinaczkowa");
		listArray.add("Trening sztuk walki");
		listArray.add("Basen");
		listArray.add("Ping-pong");

	}

	public void naDworze() {

		listArray.add("BMX");
		listArray.add("Quady");
		listArray.add("Gocardy");
		listArray.add("Golf");
		listArray.add("Jazda konna");
		listArray.add("Paintball");
		listArray.add("Tenis");

	}

	public void wodne() {
		listArray.add("P³ywanie");
		listArray.add("Kajaki");
		listArray.add("P³ywanie ³ódk¹"); // pontonem?
		listArray.add("Nurkowanie");
		listArray.add("Narty wodne");
		listArray.add("Pi³ka wodna");
	}

	public void wZimie() {
		listArray.add("£y¿wy");
		listArray.add("Narciarstwo");
		listArray.add("Hokej");
		listArray.add("Sanki");

	}

	public void nadMorzem() {
		listArray.add("Serfowanie");
		listArray.add("Siatkówka pla¿owa");

	}

	public void ekstremalne() {
		listArray.add("Parkour");
		listArray.add("Bungee");
		listArray.add("Paralotnia");
		listArray.add("Skok ze spadochronem");
		listArray.add("Windsurfing");
		// polowanie jednak nie
		listArray.add("Lot balonem");
	}

	

}

