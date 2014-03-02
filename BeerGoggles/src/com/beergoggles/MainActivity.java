/**
 * @author Dave Perra <perra@cs.unc.edu>
 * @author Steven Love <slove13@cs.unc.edu>
 * @author Clinton Freeman <freeman@cs.unc.edu>
 */

package com.beergoggles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.gson.*;

public class MainActivity extends Activity {
	
	private ArrayList<String> upcs = new ArrayList<String>();	
	private ArrayList<Beer> beers = new ArrayList<Beer>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu (add items to the action bar if it is present)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClickAddBeerButton(View view) {
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }
    
    public void onActivityResult(int requestCode, int resultCode, 
    		Intent intent) {
    	Toast.makeText(this, "GOT IT", Toast.LENGTH_SHORT).show();
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {
    		upcs.add(scanResult.getContents());
    		String u = "";
    		for (String i : upcs) {
    			u += i + ", ";
    		}
    	}
    }
    
    public void RateBeers(View view) {
    	for (String i : upcs) {
    		Beer beer = FindBeerFromUPC(i);
    		UpdateAndDisplayBeerName(beer.ba_rating);
    		beers.add(beer);
    		// Do something with the beer
    	}
    }
    
    private Beer FindBeerFromUPC(String upc) {
    	RequestTask rt = new RequestTask();
    	rt.execute("http://www.upcdatabase.com/item/" + upc);
    	String beerName = "";
    	try {
			//UpdateAndDisplayBeerName(rt.get());
    		beerName = rt.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        try {
            response = httpclient.execute(new HttpGet("http://beergoggles-freemancw.rhcloud.com/beer/"+beerName));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                // close the connection
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }

        Gson gson = new Gson();
        return gson.fromJson(responseString, Beer.class);
       
        /*
        class BagOfPrimitives {
        	  private int value1 = 1;
        	  private String value2 = "abc";
        	  private transient int value3 = 3;
        	  BagOfPrimitives() {
        	    // no-args constructor
        	  }
        	}

        	BagOfPrimitives obj = new BagOfPrimitives();
        	
        	String json = gson.toJson(obj);  
        	==> json is {"value1":1,"value2":"abc"}

        	Note that you can not serialize objects with circular references since that will result in infinite recursion. 

        	(Deserialization)
        	BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class);
        	*/
    	
    	//return null;
    }
    
    public void UpdateAndDisplayBeerName(String s) {
		//TextView textview = (TextView) findViewById(R.id.TopText);
    	//textview.setText(s);
    }
    
}
