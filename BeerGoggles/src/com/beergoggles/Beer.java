/**
 * @author Dave Perra <perra@cs.unc.edu>
 * @author Steven Love <slove13@cs.unc.edu>
 * @author Clinton Freeman <freeman@cs.unc.edu>
 */

package com.beergoggles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;


public class Beer {
	public static final int RATING = 1;
	public static final int ABV = 2;
	public static final int BRO = 3;
	public static final int PRICE = 4;
	public static final int BANG = 5;
	public static int MODE = RATING;
	
	public String beer_name = "";
	public String beer_style = "";
	public String beer_abv = "";
	public String brewery_name = "";
	public String brewery_state = "";
	public String brewery_country = "";
	public String ba_score = "";
	public String ba_rating = "";
	public String bros_score = "";
	public String bros_rating = "";
	public String ratings = "";
	public String reviews = "";
	public String rAvg = "";
	public String pDev = "";
	
	String price;
	String amt;
	String upc;
	
	public Beer() {}
	
	boolean initialized = false;
	public Beer(String a, String b, String c, String d,String e){
		beer_name = a;
		beer_abv = b;
		ba_score = c;
		bros_score = d;
		upc = e;
	}
	public double abv(){
		double result;
		try{
			result = Double.parseDouble(this.beer_abv.replaceAll("%", ""));
		}
		catch(NumberFormatException e){
			result = 0;
		}
		return result;
	}
	public double price(){
		if(!initialized){
			googleInfo();
		}
		double result;
		try{
			result = Double.parseDouble(this.price.replaceAll("[^0-9\\.]",""));
		}
		catch(NumberFormatException e){
			result = 0;
		}
		return result;
	}
	public double amt(){
		if(!initialized){
			googleInfo();
		}
		double result;
		try{
			result = Double.parseDouble(this.amt.replaceAll("[^0-9\\.]",""));
		}
		catch(NumberFormatException e){
			result = 0;
		}
		return result;
	}
	public double bang(){
		double flozperdrink = 0.6;
		double floz = (abv()/100)*amt();
		double drinks = floz/flozperdrink;
		return drinks/price();
	}
	private void googleInfo(){
//		String html = getHTML(getLink(upc));
		
		//price = "$"+upc.charAt(3)+"."+upc.charAt(1)+upc.charAt(4);
		//amt = ""+upc.charAt(2)+upc.charAt(5)+"." + upc.charAt(6) + upc.charAt(7)+" fl oz";
		
		price = "$4.00";
		amt = "12 fl oz";
//		price = getPrice(html);
//		amt = getAmt(html);
		initialized=true;
	}
	
	public double bro(){
		double result;
		try{
			result = Double.parseDouble(this.bros_score);
		}
		catch(NumberFormatException e){
			result = 0;
		}
		
		return result;
	}
	public double ba(){
		double result;
		try{
			result = Double.parseDouble(this.ba_score);
		}
		catch(NumberFormatException e){
			result = 0;
		}
		
		return result;
	}
//	public String toString(){
//		return "$"+price() + " " + amt() +  " fl oz " +
//				beer_name + " " + beer_abv + " " + ba_score + " " + bros_score + "   BANG!: " + bang();
//	}
	
	
	
	
	
	
	
	
	
	

	public static String getAmt(String html){
		
		String regex;
		Pattern pattern;
		Matcher m;
		regex = "[0-9]+\\.?[0-9]* fl oz";
		pattern = Pattern.compile(regex);
		m = pattern.matcher(html);
		
		m.find();
		String result = m.group(0);
		return(result);
	}
	public static String getPrice(String html){
		
		String regex = "\\$[0-9]+\\.?[0-9]*";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(html);
		
		m.find();
		String result = m.group(0);
		
		return(result);
	}
	
	public static String getLink(String searchTerm){
		String s = read(makeSearchString(searchTerm,1,1));
		int start = s.indexOf("http",s.indexOf("link"));
		int end = s.indexOf("\"",start);
		s = s.substring(start,end);
		return s;
	}
	
	public static String getHTML(String link){
		URL url;
		try {
			url = new URL(link);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			url = null;
			e.printStackTrace();
		}
		 InputStream is;
		try {
			is = (InputStream) url.getContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			is = null;
			e.printStackTrace();
		}
		 BufferedReader br = new BufferedReader(new InputStreamReader(is));
		 String line = null;
		 StringBuffer sb = new StringBuffer();
		 try {
			while((line = br.readLine()) != null){
			   sb.append(line);
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String htmlContent = sb.toString();
		 return htmlContent;
	}
	

	
	//base url for the search query
	final static  String searchURL = "https://www.googleapis.com/customsearch/v1?";
    final static String apiKey = "AIzaSyDZNcAJOAAckBKkegg0X3imVU2aqjW_p0c";
    final static String customSearchEngineKey = "010703878332669907762:cncpj0q8qoy";
	 
	private static String makeSearchString(String qSearch,int start,int numOfResults)
	{

	    
	    String toSearch = searchURL + "key=" + apiKey + "&cx=" + customSearchEngineKey+"&q=";
	     
	    //replace spaces in the search query with +
	    String keys[]=qSearch.split("[ ]+");
	    for(String key:keys)
	    {   
	        toSearch += key +"+"; //append the keywords to the url
	   }        
	     
	        //specify response format as json
	        toSearch+="&alt=json";
	    
	        //specify starting result number
	        toSearch+="&start="+start;
	     
	        //specify the number of results you need from the starting position
	        toSearch+="&num="+numOfResults;
	     
	    return toSearch;
	}

	private static String read(String pUrl)
	{
	    //pUrl is the URL we created in previous step
	    try
	   {
	         URL url=new URL(pUrl);
	        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        StringBuffer buffer=new StringBuffer();
	        while((line=br.readLine())!=null){
	            buffer.append(line);
	        }
	        return buffer.toString();
	    }catch(Exception e){
	        e.printStackTrace();
	   }
	    return null;
	}
	
	public static ArrayList<Beer> sort(ArrayList<Beer> beers){
		//beers.
		Comparator<Beer> comp;
		Log.v("MODE",String.valueOf(MODE));
		if(MODE==RATING){
			comp = new BAComparator();
		}
		else if(MODE == ABV){
			comp = new ABVComparator();
		}
		else if(MODE == BRO){
			comp = new BroComparator();
		}
		else if(MODE == PRICE){
			comp = new PriceComparator();
		}
		else if(MODE == BANG){
			comp = new BangComparator();
		}
		else{
			comp = null;
		}
		
//		for(Beer b : beers){
//			System.out.println(b.toString());
//		}
		//System.out.println(beers);
		Collections.sort(beers,comp);
		return beers;
//		System.out.println();
//		for(Beer b : beers){
//			System.out.println(b.toString());
//		}
	}

	
	
	@Override
	public String toString() {
		if(MODE == RATING) {
			return this.beer_name + ": " + this.ba_score;
		} else if (MODE == ABV) {
			return this.beer_name + ": " + this.beer_abv;
		} else if (MODE == BRO) {
			return this.beer_name + ": " + this.bros_score;
		}
		else if(MODE == PRICE){
			return this.beer_name + ": " + this.price();
		}
		else if(MODE == BANG){
			return this.beer_name + ": " + this.bang();

		}
		return this.beer_name;
	}
}
