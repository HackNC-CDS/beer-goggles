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
import android.app.ListActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
        
        ArrayAdapter<Beer> adapter = new ArrayAdapter<Beer>(this, android.R.layout.simple_expandable_list_item_1, beers);
        ListView list_view = (ListView) findViewById(R.id.beer_list);
        list_view.setAdapter(adapter);
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
    	}
    }
    
    public Beer getBeerFromUPC(String upc) {
    	RequestTask rt = new RequestTask();
    	rt.execute("http://www.upcdatabase.com/item/" + upc);
    	String beerJSON = "";
    	try {
    		beerJSON = rt.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

        Gson gson = new Gson();
        Log.v("JSON", beerJSON.substring(1, beerJSON.length()-2));
        return gson.fromJson(beerJSON.substring(1, beerJSON.length()-2), Beer.class); 
    }
    
}
