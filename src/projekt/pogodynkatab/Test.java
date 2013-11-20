package projekt.pogodynkatab;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Test extends Activity {

	public JSONObject testowy = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	public void sporty(View view) {

		try {
			wlozDane();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(this, Sporty.class);
		intent.putExtra("Pogoda", testowy.toString());
		
		startActivity(intent);

	}
	
	public void ubrania(View view){
		
		try {
			wlozDane();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(this, Ubrania.class);
		intent.putExtra("Pogoda", testowy.toString());
		
		startActivity(intent);
		
	}
	
	public void wypoczynek(View view){
		
		try {
			wlozDane();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(this, Wypoczynek.class);
		intent.putExtra("Pogoda", testowy.toString());
		
		startActivity(intent);
		
	}

	public void wlozDane() throws JSONException {

		EditText miasto = (EditText) findViewById(R.id.miasto);
		EditText temp = (EditText) findViewById(R.id.temperatura);
		EditText pogoda = (EditText) findViewById(R.id.pogoda);
		EditText wiatr = (EditText) findViewById(R.id.wiatr);

		EditText dzienTygodnia = (EditText) findViewById(R.id.dzienTygodnia);
		EditText dzien = (EditText) findViewById(R.id.dzien);
		EditText miesiac = (EditText) findViewById(R.id.miesiac);
		EditText rok = (EditText) findViewById(R.id.rok);
		EditText godzina = (EditText) findViewById(R.id.godzina);
		EditText minuty = (EditText) findViewById(R.id.minuty);
		
		EditText wschod = (EditText) findViewById(R.id.wschod);
		EditText zachod = (EditText) findViewById(R.id.zachod);
		
		testowy.put("city", miasto.getText().toString());

		// String message = "Mon, 11 Nov 2014 12:00:00 +0100"; 	
		
		testowy.put("day", dzien.getText());
		testowy.put("month", miesiac.getText());
		testowy.put("hour", godzina.getText());
		testowy.put("min", minuty.getText());
		testowy.put("weekDay", dzienTygodnia.getText().toString());
		testowy.put("city", miasto.getText().toString());
		testowy.put("latitude", "51.22000122");
		testowy.put("longitude","22.39999962");
		testowy.put("feelslike_c", temp.getText());
		testowy.put("weather", pogoda.getText().toString());
		testowy.put("wschod", wschod.getText());
		testowy.put("zachod", zachod.getText());
		
		if(wiatr.getText()==null){
			testowy.put("wind_mph", 0);
		}
		else
			testowy.put("wind_mph", wiatr.getText());


	}

	

	
}

