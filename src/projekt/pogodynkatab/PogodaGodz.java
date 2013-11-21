package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PogodaGodz extends ListActivity {
		
	public ArrayList<ElementListy> lista;
	public HourlyForecast dzien;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pogoda_godz);
		
		if (ForecastActivity._mainActivity != null) {
			List<HourlyForecast> godzinowa = ForecastActivity._mainActivity.hourlyForecast;
			
			for (int i = 0; i < godzinowa.size(); i++){
				dzien = godzinowa.get(i);
				ElementListy element = new ElementListy();
					//element.ikona = (ImageView) findViewById(R.id.ikonkaIV);
					//element.tytul = "Tu bêdzie tytu³";
					element.pogoda = "A tu pogoda";
	
				/*LayoutInflater inflater = (LayoutInflater) this.getBaseContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		           row = inflater.inflate(R.layout.listelement, parent, false);*/
				lista.add(element);
				
			}
			
			
		}
		
		if (!(lista.isEmpty())) {

		ArrayAdapter<ElementListy> array = new ArrayAdapter<ElementListy>(this,
				android.R.layout.simple_list_item_1, lista); 
		setListAdapter(array);
		ListView listView = getListView();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();

			}

		});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pogoda_godz, menu);
		return true;
	}

}
