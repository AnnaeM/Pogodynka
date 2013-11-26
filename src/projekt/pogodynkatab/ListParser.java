package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

public class ListParser {
	 
    private static final String FILE_EXTENSION= ".png";
    private final List<PogodaZListy> list;
    private List<HourlyForecast> godzinowa;
    private List<ForecastDay> wielodniowa;
    String rodzajListy;
 
    public ListParser(String rodzajListy) {
        this.list = new ArrayList<PogodaZListy>();
        this.rodzajListy = rodzajListy;
    }

    public List<PogodaZListy> getList() {
        return this.list;
    }
 

    public void parse() {

    	if(rodzajListy.equals("godzinowa")){
       
    		godzinowa = PogodaGodz.getList();        
            int length = godzinowa.size();
 
            for (int i = 0; i < length; i++) {           
            	HourlyForecast pog = new HourlyForecast();
            	pog = godzinowa.get(i);
            	
            	String data = pog.czas.monthDay+" "+pog.monthAbbrev+" "+pog.czas.year;
            	String godzina;
            	if(pog.czas.hour<10){
            		godzina = "0"+pog.czas.hour+":"+pog.czas.minute+"0";}
            	else{
            		godzina = pog.czas.hour+":"+pog.czas.minute+"0";}
            	
            	String warunkiPog = pog.condition;
            	char first = Character.toUpperCase(warunkiPog.charAt(0));
            	String warunkiUpperCase = first + warunkiPog.substring(1);
            	
            	final String tekst = godzina+", "+pog.weekday_name+" "+
            			data+"\n"+warunkiUpperCase+"\nTemp. "+pog.tempC+"�C";
            	final String nazwaIkony = pog.icon;
            	
            	String folder;
            	Astronomy astronomia = PogodaGodz.getAstronomy();
            	if((pog.czas.hour<=astronomia.sunrise.hour)||(pog.czas.hour>=astronomia.sunset.hour))
            			folder="night/";
            	else  
            		folder="day/";
            	
                // Construct Country object
                PogodaZListy pogoda = new PogodaZListy(tekst, nazwaIkony + FILE_EXTENSION, folder);
                Log.i(String.valueOf(pog.czas.hour), pog.condition);
                
                 
                // Add to list
                this.list.add(pogoda);}}
            
            else{
            	
            	wielodniowa = Pogoda10dni.getList();          	
            	int length = wielodniowa.size();
            	
            	for (int i = 0; i < length; i++) {           
            		ForecastDay pog = new ForecastDay();
                	pog = wielodniowa.get(i);
                	
                	String metric =  pog.fcttextMetric;
                	

                	metric = metric.replace("Light Wind","Lekki wiatr");
               
                	//Time now = new Time();
                	//now.setToNow(); 
                	//String data = String.valueOf(now.MONTH_DAY)+"."+String.valueOf(now.MONTH)+"."+String.valueOf(now.YEAR);              	
                			
                	final String tekst = pog.title+":\n" + metric;
                	final String nazwaIkony = pog.icon;
                	
                	String folder;
                	if(pog.title.contains("wiecz�r i noc"))	              		
                			folder="night/";
                	             	                	
                	else  
                		folder="day/";
                	
                    // Construct Country object
                    PogodaZListy pogoda = new PogodaZListy(tekst, nazwaIkony + FILE_EXTENSION, folder);
               
                    // Add to list
                    this.list.add(pogoda);}}
            
            
            }
     
    
}