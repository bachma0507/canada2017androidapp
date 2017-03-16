package org.bicsi.canada2014.activities;



import org.bicsi.canada2017.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_layout);

				

				    Intent i=new Intent(getBaseContext(),MainActivity.class);
					startActivity(i);
					
					finish();

		
}
	
	@Override
    protected void onDestroy() {
		
        super.onDestroy();
        
    }
}

