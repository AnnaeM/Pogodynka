package projekt.pogodynkatab;

public class Location2 {
	private String full;
	private String city;
	private String state; //mo¿na wyrzuciæ, stanów u nas nie ma
	private String state_name;
	private String country;
	private String country_iso3166; //sprawdziæ co to znaczy
	private String latitude; //d³ugoœæ 
	private String longitude; //szerokoœæ;
	private String elevation; //wysokoœæ?
	public String getFull() {
		return full;
	}
	public void setFull(String full) {
		this.full = full;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry_iso3166() {
		return country_iso3166;
	}
	public void setCountry_iso3166(String country_iso3166) {
		this.country_iso3166 = country_iso3166;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getElevation() {
		return elevation;
	}
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}
}
