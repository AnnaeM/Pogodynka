package projekt.pogodynkatab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import android.util.Log;

public class ListParser {
	 
    private static final String tag = "ListParser";
    private static final String FILE_EXTENSION= ".png";
     
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private final List<PogodaZListy> list;
    private List<HourlyForecast> godzinowa;
    private List<ForecastDay> wielodniowa;
    String rodzajListy;
 
    public ListParser(String rodzajListy) {
        this.list = new ArrayList<PogodaZListy>();
        this.rodzajListy = rodzajListy;
    }
 
    private String getNodeValue(NamedNodeMap map, String key) {
        String nodeValue = null;
        Node node = map.getNamedItem(key);
        if (node != null) {
            nodeValue = node.getNodeValue();
        }
        return nodeValue;
    }
 
    public List<PogodaZListy> getList() {
        return this.list;
    }
 
    /**
     * Parse XML file containing body part X/Y/Description
     *
     * @param inStream
     */
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
            			data+"\n"+warunkiUpperCase+"\nTemp. "+pog.tempC+"°C";
            	final String nazwaIkony = pog.icon;
            	
            	String folder;
            	if((pog.czas.hour<=6)||(pog.czas.hour>=21))
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
                	
                //	Calendar c = Calendar.getInstance(); 
                	//int day = c.DATE;
                //	String data = String.valueOf(c.DAY_OF_MONTH)+"."+String.valueOf(c.MONTH)+"."+String.valueOf(c.YEAR);
                	
                	
                	
                	final String tekst = pog.title+":\n" + metric;
                	final String nazwaIkony = pog.icon;
                	
                	String folder;
                	//if((pog.czas.hour<=6)||(pog.czas.hour>=21))
                	if(pog.title.contains("wieczór i noc"))	              		
                			folder="night/";
                	             	                	
                	else  
                		folder="day/";
                	
                    // Construct Country object
                    PogodaZListy pogoda = new PogodaZListy(tekst, nazwaIkony + FILE_EXTENSION, folder);
               
                    // Add to list
                    this.list.add(pogoda);}}
            
            
            }
     
    
}