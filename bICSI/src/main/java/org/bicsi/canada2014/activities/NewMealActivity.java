package org.bicsi.canada2014.activities;

import org.bicsi.canada2014.Meal;
import org.bicsi.canada2014.fragment.fragment_new_upload;
import org.bicsi.canada2017.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/*
 * NewMealActivity contains two fragments that handle
 * data entry and capturing a photo of a given meal.
 * The Activity manages the overall meal data.
 */
public class NewMealActivity extends Activity {

	private Meal meal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		meal = new Meal();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		meal = new Meal();
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.actvity_new_meal, container, false);*/

		// Begin with main data entry view,
		// NewMealFragment
		setContentView(R.layout.actvity_new_meal);
		FragmentManager manager = getFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = new fragment_new_upload();
			manager.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}
		
	}

	public Meal getCurrentMeal() {
		return meal;
	}

}

