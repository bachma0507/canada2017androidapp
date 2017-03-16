package org.bicsi.canada2014.application;



import org.bicsi.canada2014.Meal;
import org.bicsi.canada2014.activities.MainActivity;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;



import android.app.Application;



public class BICSIApplication extends Application {
	  @Override
	  public void onCreate() {
	    // The following line triggers the initialization of ACRA
		  
		  ParseObject.registerSubclass(Meal.class);

	    Parse.initialize(this, "hBYfmdNtJEjzBKMJYrE3rNrHmggbI8TeCJ3j1zj8", "g4V5YCaLfi0SDIB7LzcU0T4D9RJUhQ7loSc1Mzbt");
	    PushService.setDefaultPushCallback(this, MainActivity.class);
	    
	    super.onCreate();
	  }



}
