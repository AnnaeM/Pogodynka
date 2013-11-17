package projekt.pogodynkatab;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class Ubrania extends Activity {
	// public Conditions cond;
	public String pogoda;
	public ForecastDay dzien;
	public ImageView obrazek;
	public String city;
	public String latitude;
	public String longitude;
	public Double temp;
	public String wind;
	public boolean bedzieDeszcz;
	JSONObject jObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubrania);
		if (ForecastActivity._mainActivity != null) {
			//List<ForecastDay> simple10days = ForecastActivity._mainActivity.simple10day;

			if (ForecastActivity._mainActivity.display_location != null) {
				Log.i("COSTAM", "Nie jest nullem");
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
					wind = ForecastActivity._mainActivity.current_observation
							.getString("wind_kph");
					
					
					//sprawdza, czy w ci¹gu 6h nie bêdzie deszczu
					bedzieDeszcz=false;				
					String pogodaZaGodzine="";		
					int j;
						
					for(int i=0;i<5;i++){	
						
						pogodaZaGodzine = ForecastActivity._mainActivity.txt10day.get(i).fcttext.toString();
						String zapowiedz="";
						j=0;
						while(pogodaZaGodzine.charAt(j)!='.'){
							zapowiedz=zapowiedz+pogodaZaGodzine.charAt(j);
							j++;					
						}
						if((zapowiedz.equals("Mo¿liwy deszcz"))||(zapowiedz.equals("Mo¿liwe burze"))){
							bedzieDeszcz=true;							
						}						
					
					}
					if((pogoda.equals("deszcz"))||(pogoda.equals("lekki deszcz"))||pogoda.equals(("lekkie przelotne deszcze"))||
							(pogoda.equals("m¿awka"))||(bedzieDeszcz))
						ubierzDeszczowo();
					else
						ubierz();
					
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
					temp = jObject.getDouble("feelslike_c");
					pogoda = jObject.getString("weather");
					
					Log.i("Pogoda",pogoda.toString());
					if((pogoda.equals("deszcz"))||pogoda.equals("lekki deszcz")||pogoda.equals("lekkie przelotne deszcze")||
							pogoda.equals("m¿awka"))
						ubierzDeszczowo();
					else
						ubierz();		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
			}
				
		}
	}

	public void ubierz() {

		obrazek = (ImageView) findViewById(R.id.obrazek);
		Resources r = getResources();
		/*
		 * obrazek.setImageDrawable(getResources().getDrawable(R.layout.layer));
		 * //Resources r = getResources(); Drawable[] layers = new Drawable[4];
		 * layers[0] = r.getDrawable(R.drawable.kobieta); layers[1] =
		 * r.getDrawable(R.drawable.buty_k); layers[2] =
		 * r.getDrawable(R.drawable.spodniedl_k); layers[3] =
		 * r.getDrawable(R.drawable.dlrekaw_k); LayerDrawable layerDrawable =
		 * new LayerDrawable(layers); obrazek.setImageDrawable(layerDrawable);
		 */

		if (temp < 10) {
			Drawable[] layers = new Drawable[6];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);
			layers[3] = r.getDrawable(R.drawable.kurtka_k);
			layers[4] = r.getDrawable(R.drawable.szalik_k);
			layers[5] = r.getDrawable(R.drawable.czapka_k);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);

		}

		else if (temp < 18) {
			// 10-17
			Drawable[] layers = new Drawable[5];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);
			layers[3] = r.getDrawable(R.drawable.kurtka_k);
			layers[4] = r.getDrawable(R.drawable.szalik_k);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);

		}

		else if (temp < 23) {
			// 18-22
			Drawable[] layers = new Drawable[4];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);
			layers[3] = r.getDrawable(R.drawable.dlrekaw_k);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);

		}

		else if (temp < 28) {
			// 23-27
			Drawable[] layers = new Drawable[4];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.sandalki_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);
			layers[3] = r.getDrawable(R.drawable.tshirt_k);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
		}

		else {
			// 28 i w górê
			Drawable[] layers = new Drawable[4];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.sandalki_k);
			layers[2] = r.getDrawable(R.drawable.spodniekr_k);
			layers[3] = r.getDrawable(R.drawable.podkoszulek_k);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
		}

	}
	
	
public void ubierzDeszczowo(){
		
		
		obrazek = (ImageView)findViewById(R.id.obrazek);
		Resources r = getResources();
		
		if(temp<2){	
			//b³¹d zbyt ma³ej pamiêci przy 7 elementach na emulatorze
			Drawable[] layers = new Drawable[7];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);		
			layers[3] = r.getDrawable(R.drawable.kurtka_k);	
			layers[4] = r.getDrawable(R.drawable.szalik_k);	
			layers[5] = r.getDrawable(R.drawable.czapka_k);	
			layers[6] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
			
		}
		
		else if(temp<10){				
			Drawable[] layers = new Drawable[6];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);		
			layers[3] = r.getDrawable(R.drawable.kurtka_k);	
			layers[4] = r.getDrawable(R.drawable.szalik_k);	
			layers[5] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
			
		}
		
		else if(temp<18){
			//10-17
			Drawable[] layers = new Drawable[6];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);		
			layers[3] = r.getDrawable(R.drawable.kurtka_k);	
			layers[4] = r.getDrawable(R.drawable.szalik_k);	
			layers[5] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
			
		}
		
		else if(temp<23){
			//18-22
			Drawable[] layers = new Drawable[5];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.buty_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);		
			layers[3] = r.getDrawable(R.drawable.dlrekaw_k);	
			layers[4] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
			
		}
		
		else if(temp<28){
			//23-27
			Drawable[] layers = new Drawable[5];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.sandalki_k);
			layers[2] = r.getDrawable(R.drawable.spodniedl_k);		
			layers[3] = r.getDrawable(R.drawable.tshirt_k);	
			layers[4] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers); 
			obrazek.setImageDrawable(layerDrawable);
		}
		
		else{
			//28 i w górê
			Drawable[] layers = new Drawable[5];
			layers[0] = r.getDrawable(R.drawable.kobieta);
			layers[1] = r.getDrawable(R.drawable.sandalki_k);
			layers[2] = r.getDrawable(R.drawable.spodniekr_k);		
			layers[3] = r.getDrawable(R.drawable.podkoszulek_k);
			layers[4] = r.getDrawable(R.drawable.parasolka_k);	
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			obrazek.setImageDrawable(layerDrawable);
		}
		
		
	}

}

