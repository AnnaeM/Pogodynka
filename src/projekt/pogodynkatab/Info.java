package projekt.pogodynkatab;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Info extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		TextView opisTV = (TextView) findViewById(R.id.infoOpis);
		TextView tytulTV = (TextView) findViewById(R.id.infoTytul);
		
		String tytul = "System wspomagania organizowania wolnego czasu dla systemów WP i Android\n";
		tytulTV.setText(tytul);
		
		String opis = "Pogodynka ver 1.0.0\nAutorki\\dyplomantki: Anna Mazur & Iwona Krocz\n" +
				"Promotor: dr in¿. Piotr Kopniak" + "\n\nPowsta³e dziêki serwisowi pogodowemu WeatherUnderground";
		opisTV.setText(opis);
		
		ImageView logo = (ImageView)findViewById(R.id.infoLogo);		
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
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
