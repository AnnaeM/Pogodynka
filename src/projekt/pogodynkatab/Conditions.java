package projekt.pogodynkatab;

public class Conditions {
	public String observationTime;
	public String observationTimeRfc822; //Sprawdziæ co to znaczy
	public String observationEpoch;
	public String localTimeRfc822; //Sprawdziæ
	//public String localEpoch; //co to jest?
	public String localTzShort;
	public String localTzLong;
	public String localTzOffset;
	public String weather; 
	public String temperatureString;
	public String tempC;
	//public String tempF;
	public String relativeHumidity;
	public String windString;
	public String windDir;
	public String windDegrees;
	public String windMph;
	//public String windGustMph; //co to?
	public String windKph;
	//public String windGustKph; //podmuchy wiatru? 
	public String pressureMb; 
	//public String pressureIn;  //??
	public String pressureTrend;
	//public String dewpointF;
	public String dewpointC;
	public String dewpointString; //punkt rosy
	public String heatIndexString; //wskaŸnik ciep³a
	//public String heatIndexF;
	public String heatIndexC;
	public String windChillString; //temperatura odczuwalna wg wikipedii
	public String windChillC;
	//public String windChillF;
	public String feelslikeString; //to windchill + heatindex
	public String feelslikeC;
	//public String feelslikeF;
	//public String visibilityMi;
	public String visibilityKm;
	//public String solarRadiation;
	public String UV;
	public String icon;
	public String iconURL;
}
