package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
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
					podstawowe();
					
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
				podstawowe();
				
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
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wypoczynek, menu);
		return true;
	}
	
	
	public void podstawowe(){
		lista.add("Spacer");
		lista.add("Spotkanie z przyjació³mi");
		lista.add("Spacer z psem");		
		lista.add("Fotografowanie");
		lista.add("Rysowanie krajobrazu");
		lista.add("Wyjazd na dzia³kê/wieœ");
	}
	
	public void niePada(){
		lista.add("Piknik");
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
	
	public void wLesie(){
		lista.add("Grzybobranie");
		lista.add("Zbieranie jagód");
		
	}
	
	public void okazjonalne(){
		lista.add("Koncert");
		lista.add("Cyrk");
		lista.add("IdŸ na wydarzenie w mieœcie");
	}
	
	public void wieczorem(){
		lista.add("Impreza");		
		lista.add("Randka w ciemno");
		
	}
	
	public void zalezne(){
		lista.add("Ogl¹danie zachodu s³oñca");
		lista.add("Ogl¹danie wschodu s³oñca");
		lista.add("Obserwowanie gwiazd");
		lista.add("Obserwowanie chmur");
		
		lista.add("Opalanie");
		lista.add("Puszczanie latawca");
		
		lista.add("Wyjœcie na pla¿ê");
		lista.add("Przeja¿d¿ka promem");
		lista.add("IdŸ na badania kontrolne");
		
	}

}
