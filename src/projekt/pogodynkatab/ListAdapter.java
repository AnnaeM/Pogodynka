package projekt.pogodynkatab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListAdapter extends ArrayAdapter<PogodaZListy> {
    private static final String ASSETS_DIR = "images/";
    private Context context;
    private ImageView icon;
    private TextView name;
    private List<PogodaZListy> lista = new ArrayList<PogodaZListy>();
 
    public ListAdapter(Context context, int textViewResourceId,
            List<PogodaZListy> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.lista = objects;
    }
 
    public int getCount() {
        return this.lista.size();
    }
 
    public PogodaZListy getItem(int index) {
        return this.lista.get(index);
    }
 
    private class viewHolder{

    	ImageView image;
    	TextView tekst;
    	
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;	
    	View row = null;
    	PogodaZListy elementListy = new PogodaZListy();
    	
        if (row == null) {
        	
        	holder = new viewHolder();
            // ROW INFLATION
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            row = inflater.inflate(R.layout.listitem, null);
           
            
            
           convertView = inflater.inflate(R.layout.listview, null);   
         // Get item
        elementListy = getItem(position);
         
        icon = (ImageView) row.findViewById(R.id.icon);
         holder.image = icon;
        name = (TextView) row.findViewById(R.id.text);
        holder.tekst = name;
       
        holder.tekst.setText("cos.toString()");      
        holder.tekst.setTextColor(Color.BLACK);
        convertView.setTag(holder); 
        
        }
        else{
        	
        	holder= (viewHolder) convertView.getTag();
        }
 
        name.setText(elementListy.name);
        
        String imgFilePath = ASSETS_DIR +elementListy.folder+ elementListy.resourceId; 
        try {
        	Bitmap bitmap = BitmapFactory.decodeStream(this.context.getResources().getAssets()
                    .open(imgFilePath));
            holder.image.setImageBitmap(bitmap);
        //	Log.i("Obrazki","Dodano obrazek");        	
        	
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Obrazki","B³¹d");
            
        }

        return row;
    }
    
}
    	
