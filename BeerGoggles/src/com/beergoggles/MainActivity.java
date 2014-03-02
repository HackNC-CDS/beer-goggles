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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.*;

public class MainActivity extends Activity {
	
	//private ArrayList<String> upcs = new ArrayList<String>();	
	private ArrayList<Beer> beers = new ArrayList<Beer>();
	private ArrayList<String> beer_strings = new ArrayList<String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView blView = (ListView) findViewById(R.id.beer_list);

//        beer_strings.add("test beer");
        
        // This is the array adapter, it takes the context of the activity as a 
        // first parameter, the type of list view as a second parameter and your 
        // array as a third parameter.
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this, 
//                R.id.beer_list,
//                beer_strings );
//
//        blView.setAdapter(arrayAdapter); 
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
    	//Toast.makeText(this, "GOT IT", Toast.LENGTH_SHORT).show();
    	IntentResult iresult = IntentIntegrator.parseActivityResult(requestCode, 
    			resultCode, intent);
    	if (iresult != null) {
    		Beer beer = getBeerFromUPC(iresult.getContents()); 
    		beers.add(beer);
    		beer_strings.add(beer.beer_name);
    		
    		//upcs.add(iresult.getContents());
    	}
    }
    
    public Beer getBeerFromUPC(String upc) {
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
    	
    	beerName = beerName.replaceAll("\\s+", "\\+");
    	
    	HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        try {
            response = httpclient.execute(new HttpGet("http://beergoggles-freemancw.rhcloud.com/beerSearch/"+beerName));
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
    }
    
}
