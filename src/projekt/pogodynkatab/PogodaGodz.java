package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class PogodaGodz extends Activity {

	
	private List<PogodaZListy> godzinowa= new ArrayList<PogodaZListy>();
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        setContentView(R.layout.listview);
        setTitle("TestIconizedListView");
 
        ListParser listParser = new ListParser("godzinowa");
        
        // wype³nienie listy danymi 
        listParser.parse();
        List<PogodaZListy> listaPogody = listParser.getList();
                 
        // utworzenie adaptera listy
        ListAdapter adapter = new ListAdapter(
                getApplicationContext(), R.layout.listitem, listaPogody);
         
        // referencja ListView do holdera
        ListView lv = (ListView) this.findViewById(R.id.listLV);
         
        // ustawienie adaptera listy 
        lv.setAdapter(adapter);
    }
    
    //funkcja zwracaj¹ca listê obiektów HourlyForecast z klasy ForecastActivity
    public static List<HourlyForecast> getList()
    {
        return ForecastActivity._mainActivity.hourlyForecast;
    }
    
    //funkcja zwracaj¹ca dane astronomiczne z klasy ForecastActivity
    public static Astronomy getAstronomy()
    {
        return ForecastActivity._mainActivity.astronomia;
    }
    
    
    //menu
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

}
