package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class Pogoda10dni extends Activity {

	private List<PogodaZListy> wielodniowa= new ArrayList<PogodaZListy>();
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        setContentView(R.layout.listview);
        setTitle("TestIconizedListView");
 
        ListParser listParser = new ListParser("dzienna");
          
        // wype³nienie listy danymi  
        listParser.parse();
        List<PogodaZListy> listaPogody = listParser.getList();
         
         
        // utworzenie adaptera listy
        ListAdapter adapter = new ListAdapter(
                getApplicationContext(), R.layout.listitem, listaPogody);
         
        // referencja ListView do holdera
        ListView lv = (ListView) this.findViewById(R.id.listLV);
        lv.setAdapter(adapter);
    }
    
    //funkcja zwracaj¹ca listê obiektów ForecastDay z klasy ForecastActivity
    public static List<ForecastDay> getList()
    {
        return ForecastActivity._mainActivity.txt10day;
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