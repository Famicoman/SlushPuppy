/*
 * SlushPuppy 0.1
 * Mike Dank
 * 2013
 */

package com.mikedank.slushpuppy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// PostTask
// Runs in the background, pulls down JSON and stores it
public class PostTask extends AsyncTask<String, Integer, String> {
	
	private Context context;
	private int id;
	private boolean pass = true;
	protected ProgressDialog progressDialog;
	
	public PostTask(Context c, int i) {
        context = c;
        id = i;
    } 
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		//Pop up a loading window
		progressDialog = new ProgressDialog(context);
		int message = (int)(Math.random() * ((5 - 1) + 1));
		if(message==1)
			progressDialog.setMessage("Loading your stats...");
		else if(message==2)
			progressDialog.setMessage("Herding sheep...");
		else if(message==3)
			progressDialog.setMessage("Reticulating splines...");
		else if(message==4)
			progressDialog.setMessage("Hang on for a sec...");
		else
			progressDialog.setMessage("Some minor housekeeping...");
		
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	   
	@Override
	protected String doInBackground(String... params) {
		
		processSlush();
		processMTGOX();
		processBlockchain();
		   
		return null;
   }
	
	// getJsonString
	// Takes in a URL and returns the JSON from page in a String
	protected String getJsonString(String urlString){
		StringBuffer buff = new StringBuffer();
		URL url = null;
		InputStream input = null;

		//Construct a URL
		try {
			url = new URL(urlString);
			input = url.openStream();
			
			//Read in the json
			BufferedReader rd = new BufferedReader(new InputStreamReader(input));
			String line = new String();
			while ((line = rd.readLine()) != null) {
				buff.append(line);
			}
			return buff.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			pass = false;
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			pass = false;
			return null;
		}
			
	}
	
	// processSlush
	// Pulls the data out of the Slush JSON and stores it
	protected void processSlush(){
		String s = getJsonString(User.SLUSH_BASE+User.SLUSH_APIKEY);
		if(pass){
			JsonElement slushJson = new JsonParser().parse(s);
			JsonObject slushObj = slushJson.getAsJsonObject();
			User.username = slushObj.get("username").getAsString();
			User.wallet = slushObj.get("wallet").getAsString();
			User.hashrate = slushObj.get("hashrate").getAsString();
			User.send_threshold = slushObj.get("send_threshold").getAsString();
			User.unconfirmed_reward = slushObj.get("unconfirmed_reward").getAsString();
			User.confirmed_reward = slushObj.get("confirmed_reward").getAsString();
			User.estimated_reward = slushObj.get("estimated_reward").getAsString();
			User.total_reward =  Double.toString(Double.parseDouble(User.unconfirmed_reward) + Double.parseDouble(User.confirmed_reward));
			
			// Namecoin not implemented on server
			//User.confirmed_nmc_reward  = obj.get("confirmed_nmc_reward").getAsString();
			//User.unconfirmed_nmc_reward  = obj.get("unconfirmed_nmc_reward").getAsString();
			//User.nmc_send_threshold = obj.get("nmc_send_threshold").getAsString();
			
			JsonObject worker = slushObj.get("workers").getAsJsonObject();
			User.workers = new ArrayList<Worker>();
			for (Map.Entry<String,JsonElement> entry : worker.entrySet()) {
				String worker_name = entry.getKey().toString();
				int last_share = entry.getValue().getAsJsonObject().get("last_share").getAsInt();
				String score = entry.getValue().getAsJsonObject().get("score").getAsString();
				int shares = entry.getValue().getAsJsonObject().get("shares").getAsInt();
				int hashrate = entry.getValue().getAsJsonObject().get("hashrate").getAsInt();
				Boolean alive = entry.getValue().getAsJsonObject().get("alive").getAsBoolean();
				
				User.workers.add(new Worker(worker_name, last_share, score, shares, hashrate, alive));
			}
		}
	}
	
	// processMTGOX
	// Gets JSON from Mt. Gox, stores it, and computes values from it and stored Slush info.
	protected void processMTGOX(){
		String m = getJsonString(User.MTGOX_BASE);
		if(pass){
			JsonElement exchangeJson = new JsonParser().parse(m);
			JsonObject exchangeObj = exchangeJson.getAsJsonObject();
			User.BTCUSD = exchangeObj.get("return").getAsJsonObject().get("avg").getAsJsonObject().get("value").getAsString();
			User.send_threshold_usd = Double.toString(Double.parseDouble(User.send_threshold)*Double.parseDouble(User.BTCUSD));
			User.unconfirmed_reward_usd =  Double.toString(Double.parseDouble(User.unconfirmed_reward)*Double.parseDouble(User.BTCUSD));
			User.confirmed_reward_usd =  Double.toString(Double.parseDouble(User.confirmed_reward)*Double.parseDouble(User.BTCUSD));
			User.estimated_reward_usd =  Double.toString(Double.parseDouble(User.estimated_reward)*Double.parseDouble(User.BTCUSD));
			User.total_reward_usd =  Double.toString((Double.parseDouble(User.unconfirmed_reward) + Double.parseDouble(User.confirmed_reward))*Double.parseDouble(User.BTCUSD));
		}
	}
	
	// processBlockchain
	// Gets JSON from Blockchain, stores it, and computes values from it and stored Mt. Gox info.
	protected void processBlockchain(){
		String b = getJsonString(User.BLOCKCHAIN_BASE+User.wallet+User.BLOCKCHAIN_TAIL);
		if(pass){
			JsonElement blockchainJson = new JsonParser().parse(b);
			JsonObject blockchainObj = blockchainJson.getAsJsonObject();
			User.wallet_balance = Double.toString(blockchainObj.get("final_balance").getAsDouble()/100000000);
			User.wallet_balance_usd = Double.toString(Double.parseDouble(User.wallet_balance)*Double.parseDouble(User.BTCUSD));
		}
	}
		
   @Override
   protected void onPostExecute(String result) {
	   super.onPostExecute(result);
	   
	   if(pass){ // No errors, so start ListActivity
		   Intent statsList = new Intent();
		   statsList.setClass(context,ListActivity.class);
		   if(id==1)
			   ((Activity) context).finish();
		   context.startActivity(statsList);
		   progressDialog.cancel();
	   }
	   else{ // Error pulling JSON, put up a message
		   Toast.makeText(context,"Network error, try again later.", Toast.LENGTH_LONG).show();
		   progressDialog.cancel();
	   }
		   
				  
   }
}		