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
		
		if (ForecastActivity._mainActivity != null) {
			List<ForecastDay> simple10days = ForecastActivity._mainActivity.simple10day;
			dzien = simple10days.get(0);
			Log.i("WYPOCZYNEK - dzien", dzien.toString());
			if (ForecastActivity._mainActivity.display_location != null) {
				Log.i("WYPOCZYNEK", "Nie jest nullem");
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
				
					Log.i("Pogoda",pogoda.toString());	
					if(pogoda.equals("")){
						//bo w Pu³awach nie ma pogody :c
						String pogodaAwaryjna;
						pogodaAwaryjna = ForecastActivity._mainActivity.txt10day.get(0).fcttext.toString();
						Log.i("Pusta pogoda!", pogodaAwaryjna);
						
						int i=0;
						while(pogodaAwaryjna.charAt(i)!='.'){
							pogoda=pogoda+pogodaAwaryjna.charAt(i);
							i++;
						
					}
						Log.i("Pogoda awaryjna", pogoda);
					}
					wind = ForecastActivity._mainActivity.current_observation
							.getString("wind_kph");
					//godzina =Integer.parseInt(dzien.data.hour);
					miesiac = Integer.parseInt(dzien.data.month);
					dzienTygodnia=dzien.data.weekDay;
					
					String godzinaString ="";
					String aktualnaGodzina = ForecastActivity._mainActivity.aktualnaGodzina;
					int i=0;
					while(aktualnaGodzina.charAt(i)!=','){
						i++;}
					
					
					godzinaString = godzinaString+aktualnaGodzina.charAt(i+2)+aktualnaGodzina.charAt(i+3);
					godzina = Integer.parseInt(godzinaString);
					
					poraRoku();
					wyborWypoczynku();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
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
				
				poraRoku();
				wyborWypoczynku();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if (lista.isEmpty()) {
			lista.add("Zostañ w domu");
		}
		
		ArrayAdapter<String> array = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lista);
		setListAdapter(array);
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(),
					//	((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				
				String uri = "https://maps.google.pl/maps?q="+city+"+"+((TextView) view).getText();
				Log.i("URL",uri);
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wypoczynek, menu);
		return true;
	}
	
	
	public void poraDnia() {

		Log.i("Godzina", String.valueOf(godzina));
		if ((godzina >= 6) && (godzina < 10))
			poraDnia = 'r'; // ranek
		else if ((godzina >= 10) && (godzina < 14))
			poraDnia = 'p'; // "poludnie";
		else if ((godzina >= 14) && (godzina < 18))
			poraDnia = 'o'; // "popo³udnie";
		else if ((godzina >= 18) && (godzina < 22))
			poraDnia = 'w'; // "wieczór";
		else if ((godzina >= 22) && (godzina < 1))
			poraDnia = 'n'; // "noc";
		else
			poraDnia = 'g'; // "g³êboka noc";
		
		Log.i("Pora dnia", String.valueOf(poraDnia));
		
	
	}
	
	public void poraRoku() {

		// mo¿na dodaæ np. przedwioœnie

		if ((miesiac == 4) || (miesiac == 5)) { // kwiecien-maj
			poraRoku = 'w';
		} else if ((miesiac >= 6) && (miesiac <= 8)) { // czerwiec-sierpien
			poraRoku = 'l';
		} else if ((miesiac >= 9) && (miesiac <= 11)) { // wrzesien-list
			poraRoku = 'j';
		} else // grudzien-marzec
		{poraRoku = 'z';
		}


	}
	public void wyborWypoczynku(){
		
		poraDnia();
		String pogoda2= pogoda.toLowerCase();
		if (pogoda2.equals("pogodnie")) {
			ladnaPogoda();			
		} else if (pogoda2.equals("przewaga chmur"))
			ladnaPogoda();
		else if (pogoda2.equals("ob³oki zanikaj¹ce"))
			ladnaPogoda();
		else if (pogoda2.equals("œnieg"))
			deszczowaPogoda();
		else if (pogoda2.equals("niewielkie zachmurzenie"))
			ladnaPogoda();
		else if (pogoda2.equals("deszcz"))
			deszczowaPogoda();
		else if (pogoda2.equals("lekki deszcz"))
			deszczowaPogoda();
		else if (pogoda2.equals("pochmurno"))
			ladnaPogoda();
		else if (pogoda2.equals("p³atki mg³y"))
			ladnaPogoda();
		else if (pogoda2.equals("lekkie przelotne deszcze"))
			deszczowaPogoda();
		else if(pogoda2.equals("lekka m¿awka"))	
			ladnaPogoda();
		else if(pogoda2.equals("zamglenia"))
			ladnaPogoda();
		else if(pogoda.equals("m¿awka"))
			deszczowaPogoda();
		else if(pogoda.equals("mg³a"))
			ladnaPogoda();
		else if(pogoda.equals("lekka mg³a"))
			ladnaPogoda();
		else if(pogoda.equals("czêœciowe zamglenia"))
			ladnaPogoda();
		else
			lista.add("Nieznany rodzaj pogody");

		zalezne();	
	}
	
	
	public void ladnaPogoda(){
		podstawowe();
		podDachem();
		niePada();
		okazjonalne();
		
	}
	
	public void deszczowaPogoda(){
		podDachem();
		
		
	}
	public void podstawowe(){
		lista.add("Spacer");
		lista.add("Spotkanie z przyjació³mi");
		lista.add("Spacer z psem");		
		lista.add("Fotografowanie");
		lista.add("Rysowanie krajobrazu");
		lista.add("Wyjazd na dzia³kê/wieœ");
		lista.add("Wyprawa do lasu");
	}
	
	public void niePada(){
		
		lista.add("Zoo");
		
	}
	
	public void podDachem(){
		lista.add("Kino");
		lista.add("Kawa");
		lista.add("Krêgle");
		lista.add("Bilard");
		lista.add("Muzeum");
		lista.add("Biblioteka");
		lista.add("Teatr");
		lista.add("Aquapark");
		lista.add("Zakupy");
		lista.add("Kó³ko plastyczne/muzyczne/modelarskie");
	}
	
	
	public void okazjonalne(){		//dodaæ warunki
		lista.add("Koncert");
		lista.add("Cyrk");
		//lista.add("IdŸ na wydarzenie w mieœcie");
	}
	
	public void zalezne(){
				
		int wschodSlonca = ForecastActivity._mainActivity.astronomia.sunrise.hour;
		int zachodSlonca = ForecastActivity._mainActivity.astronomia.sunset.hour;
		
		String pogoda2=pogoda.toLowerCase();
		if((pogoda2.equals("pogodnie"))||(pogoda2.equals("niewielkie zachmurzenie"))
				||pogoda2.equals("ob³oki zanikaj¹ce"))
		{
			if((godzina>=wschodSlonca-1)&&(godzina<=wschodSlonca+1))
			{
				lista.add("Ogl¹danie wschodu s³oñca");
			}
			
			if((godzina>=zachodSlonca-1)&&(godzina<=zachodSlonca+1))
			{
				lista.add("Ogl¹danie zachodu s³oñca");
			}
			
			if((poraDnia=='n')||(poraDnia=='g')){
				lista.add("Obserwowanie gwiazd");
			}
			
		}
		
		if (!pogoda2.equals("pogodnie")){
			lista.add("Obserwowanie chmur");
		}
		//lista.add("Wyjœcie na pla¿ê");
		//lista.add("Przeja¿d¿ka promem");
		//lista.add("IdŸ na badania kontrolne");
		
		if(poraRoku!='z'){
			
			if(Double.valueOf(wind)>10)
				lista.add("Puszczanie latawca");
			
			if((poraDnia!='n')&&(poraDnia!='g')&&(poraDnia!='w'))
				lista.add("Piknik");
			
			if(pogoda2.equals("pogodnie"))
					lista.add("Opalanie");
		}
		
		if((poraDnia=='w')||(poraDnia=='n')||(poraDnia=='g')){
			lista.add("Impreza");		
			lista.add("Randka w ciemno");
		}
		
	}
	
	

}
