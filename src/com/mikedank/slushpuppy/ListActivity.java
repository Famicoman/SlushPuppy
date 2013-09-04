/*
 * SlushPuppy 0.1
 * Mike Dank
 * 2013
 */

package com.mikedank.slushpuppy;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

// ListActivty
// The activity that displays all the stats
public class ListActivity extends Activity {
	
	public ArrayList<TextView> textViewList = new ArrayList<TextView>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);	    
	    
		textViewList.add(textViewBuilder("Wallet: "+User.wallet));
		textViewList.add(textViewBuilder("Current AVG Price BTC: "+User.BTC_SYMBOL+User.BTCUSD));
		textViewList.add(textViewBuilder("Wallet Balance BTC: "+User.BTC_SYMBOL+User.wallet_balance));
		textViewList.add(textViewBuilder("Wallet Balance USD: $"+User.wallet_balance_usd));
		textViewList.add(textViewBuilder(""));
				
		textViewList.add(textViewBuilder("Username: "+User.username));
		textViewList.add(textViewBuilder("Hashrate: "+User.hashrate+"Mhash/s"));
		textViewList.add(textViewBuilder("Send Threshold BTC: "+User.BTC_SYMBOL+User.send_threshold));
		textViewList.add(textViewBuilder("Send Threshold USD: $"+User.send_threshold_usd));
		textViewList.add(textViewBuilder(""));
		
		textViewList.add(textViewBuilder("Estimated Reward BTC: "+User.BTC_SYMBOL+User.estimated_reward));
		textViewList.add(textViewBuilder("Unconfirmed Reward BTC: "+User.BTC_SYMBOL+User.unconfirmed_reward));
		textViewList.add(textViewBuilder("Confirmed Reward BTC: "+User.BTC_SYMBOL+User.confirmed_reward));
		textViewList.add(textViewBuilder("Total Reward BTC: "+User.BTC_SYMBOL+User.total_reward));
		textViewList.add(textViewBuilder(""));
		
		textViewList.add(textViewBuilder("Estimated Reward USD: $"+User.estimated_reward_usd));
		textViewList.add(textViewBuilder("Unconfirmed Reward USD: $"+User.unconfirmed_reward_usd));
		textViewList.add(textViewBuilder("Confirmed Reward USD: $"+User.confirmed_reward_usd));
		textViewList.add(textViewBuilder("Total Reward USD: $"+User.total_reward_usd));
		textViewList.add(textViewBuilder(""));
		
		for(int i=0; i<User.workers.size();i++){
			textViewList.add(textViewBuilder("Worker Name: "+User.workers.get(i).getName()));
			textViewList.add(textViewBuilder("Last Share: "+User.workers.get(i).getLastShare()));
			textViewList.add(textViewBuilder("Score: "+User.workers.get(i).getScore()));
			textViewList.add(textViewBuilder("Shares: "+User.workers.get(i).getShares()));
			textViewList.add(textViewBuilder("Hashrate: "+User.workers.get(i).getHashrate()+"Mhash/s"));
			textViewList.add(textViewBuilder("Alive: "+User.workers.get(i).getAlive()));
			textViewList.add(textViewBuilder(""));
		}
		
		// Just a refresh button
		Button refreshButton = new Button(this);
		refreshButton.setText("Refresh Stats");
		refreshButton.setGravity(Gravity.CENTER);
		refreshButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	
	        	PostTask task = new PostTask(ListActivity.this, 1);
	    		task.execute();
	        }
	    });
		
		// Create a LinearLayout
	    LinearLayout ll = new LinearLayout(this);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    
	    // Add all our views to the LinearLayout
	    for(int i=0;i<textViewList.size();i++){
	    	ll.addView(textViewList.get(i));
	    }
	    
	    ll.addView(refreshButton);
	    
	    // The page is long, so let it be scrollable
	    ScrollView scroll = new ScrollView(getBaseContext());
	    scroll.addView(ll);
	    
	    setContentView(scroll);
	}
	
	// textViewBuilder
	// Builds the TextView for the screen
	public TextView textViewBuilder(String text){
		TextView t = new TextView(this);
	    t.setTextSize(15);
	    t.setGravity(Gravity.TOP);
	    t.setText(text);
	    
	    return t;
	}
	
	// textViewBuilderHTML
	// Builds the TextView for the screen, with HTML
	// Currently not used
	public TextView textViewBuilderHTML(String text){
		TextView t = new TextView(this);
	    t.setTextSize(15);
	    t.setGravity(Gravity.TOP);
	    t.setText(Html.fromHtml(text));
	    
	    return t;
	}

}
