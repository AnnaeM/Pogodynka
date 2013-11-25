package projekt.pogodynkatab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements LocationListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void doPogody(View view) {
		Intent intent = new Intent(this, ForecastActivity.class);
		EditText editText = (EditText) findViewById(R.id.miasto);
		String message = editText.getText().toString();
		// getWeather gw = new getWeather(message, "Poland");
		intent.putExtra("Lokacja", message);
		// intent
		startActivity(intent);
	}

	public void GPS(View view) {
		uzyjGPS();
	}
	
	public void uzyjGPS() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("GPS nie jest w³¹czony")
					.setMessage("Czy chcesz przejœæ do ustawieñ GPS?")
					.setCancelable(true)
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// TODO Auto-generated method stub
									startActivity(new Intent(
											Settings.ACTION_SECURITY_SETTINGS));
								}
							})
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// TODO Auto-generated method stub
									dialog.cancel();
									//finish();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();

		}

		else {

		/*	getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
		 	*/
			Location location = null;
			
			getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			
			int i = 0;
			while ((location == null) && (i <= 100)) {
			//while (location == null) {
				
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				i++;

			}

			//obs³uga b³êdu GPS
			if (i >= 100) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Przepraszamy!")
						.setMessage("Nie uda³o siê pobraæ danych z GPS.")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}

			else {
				double szerokosc = (double) (location.getLatitude());
				double dlugosc = (double) (location.getLongitude());

			//	locationManager.removeUpdates(LocationManager.GPS_PROVIDER);
			//	locationManager.removeUpdates(listener);
				
				doPogody(dlugosc, szerokosc);
			}
					
		}
	}

	public void doPogody(double dlugosc, double szerokosc) {
		Intent intent = new Intent(this, ForecastActivity.class);

		String dl = String.format("%.4f", dlugosc);
		String szer = String.format("%.4f", szerokosc);

		dl = dl.replace(',', '.');
		szer = szer.replace(',', '.');

		String message = szer + "," + dl;

		intent.putExtra("Lokacja", message);
		startActivity(intent);

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}

