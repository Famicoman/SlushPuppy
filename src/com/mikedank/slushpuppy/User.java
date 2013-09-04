/*
 * SlushPuppy 0.1
 * Mike Dank
 * 2013
 */

package com.mikedank.slushpuppy;

import java.util.ArrayList;

// User
// Stores user variables
public class User {
	
	public static String username = "";
	public static String wallet = "";
	public static String hashrate = "";
	public static String send_threshold = "";
	public static String send_threshold_usd = "";
	
	public static String BTCUSD = "";
	public static String wallet_balance = "";
	public static String wallet_balance_usd = "";
	
	public static String confirmed_reward = "";
	public static String unconfirmed_reward = "";
	public static String estimated_reward = "";
	public static String total_reward = "";
	
	public static String confirmed_reward_usd = "";
	public static String unconfirmed_reward_usd = "";
	public static String estimated_reward_usd = "";
	public static String total_reward_usd = "";
	
	//public static String confirmed_nmc_reward  = "";
	//public static String unconfirmed_nmc_reward  = "";
	//public static String nmc_send_threshold = "";
	
	public static final String SLUSH_BASE = "https://mining.bitcoin.cz/accounts/profile/json/";
	public static final String MTGOX_BASE = "http://data.mtgox.com/api/1/BTCUSD/ticker";
	public static final String BLOCKCHAIN_BASE = "http://blockchain.info/address/";
	public static final String BLOCKCHAIN_TAIL = "?format=json";
	public static final String SLUSH_APIKEY = "YOUR_API_TOKEN_HERE";
	
	public static final String BTC_SYMBOL = "\u0e3f";
	
	public static ArrayList<Worker> workers = new ArrayList<Worker>();

}
