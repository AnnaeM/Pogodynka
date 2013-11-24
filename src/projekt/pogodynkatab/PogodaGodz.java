package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class PogodaGodz extends Activity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pogoda_godz, menu);
		return true;
	}
	
	private List<PogodaZListy> godzinowa= new ArrayList<PogodaZListy>();
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        // Set the View layer
        setContentView(R.layout.listview);
        setTitle("TestIconizedListView");
 
        // Create Parser for raw/countries.xml
        ListParser listParser = new ListParser("godzinowa");
        
        // Parse the inputstream 
        listParser.parse();
 
        // Get Countries
        List<PogodaZListy> listaPogody = listParser.getList();
         
         
        // Create a customized ArrayAdapter
        ListAdapter adapter = new ListAdapter(
                getApplicationContext(), R.layout.listitem, listaPogody);
         
        // Get reference to ListView holder
        ListView lv = (ListView) this.findViewById(R.id.countryLV);
         
        // Set the ListView adapter
        lv.setAdapter(adapter);
    }
    
    public static List<HourlyForecast> getList()
    {
        return ForecastActivity._mainActivity.hourlyForecast;
    }

}
