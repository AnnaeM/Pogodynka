package projekt.pogodynkatab;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class ListParser {
	 
    private static final String tag = "ListParser";
    private static final String FILE_EXTENSION= ".png";
     
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private final List<OkresowaPogoda> list;
    private List<HourlyForecast> godzinowa;
    private List<ForecastDay> wielodniowa;
    String rodzajListy;
 
    public ListParser(String rodzajListy) {
        this.list = new ArrayList<OkresowaPogoda>();
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
 
    public List<OkresowaPogoda> getList() {
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
            	
            	final String tekst = godzina+", "+pog.weekday_name+" "+
            			data+"\n"+pog.condition+"\nTemp. "+pog.tempC+"°C";
            	final String nazwaIkony = pog.icon;
            	
                // Construct Country object
                OkresowaPogoda pogoda = new OkresowaPogoda(tekst,nazwaIkony + FILE_EXTENSION);
                Log.i(String.valueOf(pog.czas.hour), pog.condition);
                
                 
                // Add to list
                this.list.add(pogoda);}}
            
            else{
            	wielodniowa = Pogoda10dni.getList();          	
            	int length = wielodniowa.size();
            	
            	for (int i = 0; i < length; i++) {           
            		ForecastDay pog = new ForecastDay();
                	pog = wielodniowa.get(i);
                	
                	final String tekst = pog.title+":\n" + pog.fcttextMetric;
                	final String nazwaIkony = pog.icon;
                	
                    // Construct Country object
                    OkresowaPogoda pogoda = new OkresowaPogoda(tekst,nazwaIkony + FILE_EXTENSION);
               
                    // Add to list
                    this.list.add(pogoda);}}
            
            
            }
     
    
}