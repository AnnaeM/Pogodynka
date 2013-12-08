package projekt.pogodynkatab;

import java.io.IOException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Pogoda extends Activity {

	public ImageView ikonka;
	public TextView lokacja;
	public TextView dzisiaj;
	public ForecastDay dzien;
	public TextView pogoda1;
	 private static final String ASSETS_DIR = "images/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pogoda);
		if (ForecastActivity._mainActivity != null) {
			ikonka = (ImageView) findViewById(R.id.iconIV);		
			lokacja = (TextView) findViewById(R.id.lokacja2TB);
			dzisiaj = (TextView) this.findViewById(R.id.data);
			try {
				Log.i("POGODA", ForecastActivity._mainActivity.disLoc.getCity());
				lokacja.setText(ForecastActivity._mainActivity.disLoc.getCity());
				dzien = ForecastActivity._mainActivity.simpleForecast.get(0);
				dzisiaj.setText("Dzisiaj jest " + dzien.data.weekDay + ", "
						+ dzien.data.day + " " + dzien.data.monthName + " "
						+ dzien.data.year + "\n");
				pogoda1 = (TextView) this.findViewById(R.id.pogoda1);
				String cisnTrend = "";
				if (ForecastActivity._mainActivity.cndtns.pressureTrend
						.equals("-")) {
					cisnTrend = "spada";
				} else if (ForecastActivity._mainActivity.cndtns.pressureTrend
						.equals("+")) {
					cisnTrend = "roœnie";
				} else {
					cisnTrend = "stale / brak danych";
					Log.i("CIŒNIENIE",
							ForecastActivity._mainActivity.cndtns.pressureTrend);
				}
				
				String warunkiPogodowe;
				if(ForecastActivity._mainActivity.cndtns.weather.equals("")){
					warunkiPogodowe = ForecastActivity._mainActivity.hourlyForecast.get(0).condition;								
	            	
	            	}
				else
					warunkiPogodowe = ForecastActivity._mainActivity.cndtns.weather;
				
				String warunkiPogodowe2;
				char first = Character.toUpperCase(warunkiPogodowe.charAt(0));
            	warunkiPogodowe2 = first + warunkiPogodowe.substring(1);

            	
            	String kierunekWiatru = wiatr(ForecastActivity._mainActivity.cndtns.windDir);          	            	
            	DecimalFormat df = new DecimalFormat("#.#");
            	            	
				String pogoda = warunkiPogodowe2
						+ "\nTemp: "
						+ ForecastActivity._mainActivity.cndtns.tempC
						+ "°C 	Odczuwalna: "
						+ df.format(ForecastActivity._mainActivity.cndtns.temperaturaOdczuwalna)
						+ "°C\nWiatr "
						+ kierunekWiatru
						+ ",  w porywach do "
						+ ForecastActivity._mainActivity.cndtns.windKph
						+ "km/h"
						+ "\nOpady "
						+ ForecastActivity._mainActivity.current_observation
								.getString("precip_today_metric")
						+ "\nCiœnienie 	"
						+ ForecastActivity._mainActivity.cndtns.pressureMb
						+ "hPa, "
						+ cisnTrend
						+ "\nWidocznoœæ "
						+ ForecastActivity._mainActivity.cndtns.visibilityKm
						+ "km"
						+ "\nWilgotnoœæ "
						+ ForecastActivity._mainActivity.cndtns.relativeHumidity;
				pogoda1.setText(pogoda);
				
				Time now = new Time();
				now.setToNow();
				Log.i("Teraz",now.toString());
				
				String folder;
				if((now.hour>=ForecastActivity._mainActivity.astronomia.sunrise.hour)&&(now.hour<=ForecastActivity._mainActivity.astronomia.sunset.hour))
					folder="day/";
				else
					folder="night/";
				
				 String imgFilePath = ASSETS_DIR +folder+ ForecastActivity._mainActivity.cndtns.icon+".png"; 
			        try {
			        	Bitmap bitmap = BitmapFactory.decodeStream(getApplicationContext().getResources().getAssets()
			                    .open(imgFilePath));
			            ikonka.setImageBitmap(bitmap);
			        //	Log.i("Obrazki","Dodano obrazek");        	
			        	
			        } catch (IOException e) {
			            e.printStackTrace();
			            Log.i("Obrazki","B³¹d");
			            
			        }
			} catch (Exception ex) {
				Log.i(ex.getMessage(), ex.toString());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.about:
	        Intent intent = new Intent(this, Info.class);
	        startActivity(intent);
	        return true;            
	    }
	    return false;
	}
	
	public String wiatr(String kierunekWiatru){
		String wynik;
		if (kierunekWiatru.equals("East"))
			wynik = "wschodni";
    	else if (kierunekWiatru.equals("West"))
    		wynik = "zachodni";
    	else if (kierunekWiatru.equals("North"))
    		wynik = "pó³nocny";
    	else if (kierunekWiatru.equals("South"))
    		wynik = "po³udniowy";
    	else if (kierunekWiatru.equals("Variable"))
    		wynik = "zmienny";
    	else wynik=kierunekWiatru;
		return wynik;
	}
}
