/*
 * SlushPuppy 0.1
 * Mike Dank
 * 2013
 */

package com.mikedank.slushpuppy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mikedank.slushandroid.R;

// MainActivity
// Generate the home screen
public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Call background task at startup to shortcut home screen
		PostTask task = new PostTask(this, 0);
		task.execute();
		
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       
       //Have a button on the home screen to refresh the stats
       Button myButton = (Button)findViewById(R.id.button1);
	    myButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	PostTask task = new PostTask(MainActivity.this, 0);
	    		task.execute();
	        }
	    }); 
	}
}
