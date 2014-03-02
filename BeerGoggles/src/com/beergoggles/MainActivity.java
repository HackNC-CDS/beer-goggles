package com.beergoggles;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public ArrayList<String> upcs = new ArrayList<String>();	
	public ArrayList<Beer> beers = new ArrayList<Beer>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void FindBarcodes(View view) {
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    	
    	TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText("Found barcodes.");
    }
    public void RateBeers(View view) {
    	for (String i : upcs) {
    		Beer beer = FindBeerFromUPC(i);
    		beers.add(beer);
    		// Do something with the beer
    	}
    }
    public void Visualize(View view) {
    	TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText("Visualizing...");
    }
    
    private Beer FindBeerFromUPC(String upc) {
    	new RequestTask().execute("http://www.upcdatabase.com/item/" + upc);
    	
    	return null;
    }
    
    public void UpdateAndDisplayBeerName(String s) {
		TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText(s);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Toast t = Toast.makeText(this, "GOT IT", Toast.LENGTH_SHORT);
    	t.show();
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {
    		upcs.add(scanResult.getContents());
    		String u = "";
    		for (String i : upcs) {
    			u += i + ", ";
    		}
    		TextView textview = (TextView) findViewById(R.id.TopText);
        	textview.setText(u);
    	}
    }
    
}
