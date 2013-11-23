package projekt.pogodynkatab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListAdapter extends ArrayAdapter<OkresowaPogoda> {
    private static final String tag = "CountryArrayAdapter";
    private static final String ASSETS_DIR = "images/";
    private Context context;
    private ImageView countryIcon;
    private TextView countryName;
    private TextView countryAbbrev;
    private List<OkresowaPogoda> countries = new ArrayList<OkresowaPogoda>();
 
    public ListAdapter(Context context, int textViewResourceId,
            List<OkresowaPogoda> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.countries = objects;
    }
 
    public int getCount() {
        return this.countries.size();
    }
 
    public OkresowaPogoda getItem(int index) {
        return this.countries.get(index);
    }
 
    private class viewHolder{

    	ImageView image;
    	TextView tekst;
    	//TextView tekst2;
    	
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;	
    	View row = null;
    	//View vi = convertView;
    	//Country country = null;
    	OkresowaPogoda elementListy = new OkresowaPogoda();
    	
        if (row == null) {
        	
        	holder = new viewHolder();
            // ROW INFLATION
        	
        //	Log.d(tag, "Starting XML Row Inflation ... ");
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            row = inflater.inflate(R.layout.listitem, null);
           
        //    Log.d(tag, "Successfully completed XML Row Inflation!");
            
            
           convertView = inflater.inflate(R.layout.listview, null);   
            // Get item
        elementListy = getItem(position);
         
        countryIcon = (ImageView) row.findViewById(R.id.icon);
         holder.image = countryIcon;
        countryName = (TextView) row.findViewById(R.id.text);
        holder.tekst = countryName;
      //  countryAbbrev = (TextView) row.findViewById(R.id.country_abbrev);
      //  holder.tekst2 = countryAbbrev;
       
        holder.tekst.setText("cos.toString()"); 
      //  holder.tekst2.setText("cos2.toString()");
        
        
        convertView.setTag(holder); 
        
        }
        else{
        	
        	holder= (viewHolder) convertView.getTag();
        }
        
        //Set country name
        countryName.setText(elementListy.name);
         
        // Set country icon usign File path
       // String imgFilePath = ASSETS_DIR + country.resourceId; 
        String imgFilePath = ASSETS_DIR + elementListy.resourceId; 
        try {
        	Bitmap bitmap = BitmapFactory.decodeStream(this.context.getResources().getAssets()
                    .open(imgFilePath));
           // countryIcon.setImageBitmap(bitmap);
            holder.image.setImageBitmap(bitmap);
        	Log.i("Obrazki","Dodano obrazek");        	
        	
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Obrazki","B³¹d");
            
        }
         
        // Set country abbreviation
      //  countryAbbrev.setText(country.abbreviation);
        return row;
    }
    
}
    	
