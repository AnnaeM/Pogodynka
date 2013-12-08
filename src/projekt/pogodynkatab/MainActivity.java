package projekt.pogodynkatab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements LocationListener {

	public Intent intent;
	public EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//wy³¹cza wyœwietlanie paska z nazw¹ aktywnoœci
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		intent = new Intent(this, ForecastActivity.class);
		editText = (EditText) findViewById(R.id.miasto);

		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					String message = editText.getText().toString();
					if (!message.equals("")) {
						intent.putExtra("Lokacja", message);
						startActivity(intent);
					}			
					return true;
				}
				return false;
			}
		});
		
		ImageView logo = (ImageView)findViewById(R.id.mainLogo);		
		logo.setClickable(true); 
		
		logo.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	String uri = "http://www.wunderground.com/?apiref=5eb71539bdb4d721";
				Log.i("URL", uri);
				startActivity(new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void doPogody(View view) {

		String message = editText.getText().toString();
		// getWeather gw = new getWeather(message, "Poland");
		if (!message.equals("")) {
			intent.putExtra("Lokacja", message);
			startActivity(intent);
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("")
					.setMessage("Podaj miasto!")
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
	}

	public void GPS(View view) {
		uzyjGPS();
	}

	public void uzyjGPS() {
		//blokowanie przycisku na czas szukania lokalizacji
		 //Button button = (Button) findViewById(R.id.GPS);
		// button.setEnabled(false);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//jeœli GPS nie jest w³¹czony:
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
											Settings.ACTION_SECURITY_SETTINGS)); //otwiera ustawienia telefonu
								}
							})
					.setNegativeButton("Nie",
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

		//jeœli GPS jest w³¹czony:
		else {

			Location location = null;

			getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);

			//wielokrotna próba pobrania danych z GPSa
			int i = 0;
			while ((location == null) && (i <= 100)) {
				// while (location == null) {

				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				i++;

			}

			// gdy nie uda³o siê pobraæ danych z GPS
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
			
			
			//gdy pobrano dane z GPS
			else {
				double szerokosc = (double) (location.getLatitude());
				double dlugosc = (double) (location.getLongitude());

				//wy³¹cz GPS
				locationManager.removeUpdates(this);
				
				//przejœcie do pobierania prognozy
				doPogody(dlugosc, szerokosc);
			}

		}

		// button.setEnabled(true);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent intent = new Intent(MainActivity.this, Info.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

}
