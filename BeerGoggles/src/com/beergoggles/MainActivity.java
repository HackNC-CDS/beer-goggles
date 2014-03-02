package com.beergoggles;

<<<<<<< HEAD
=======
import java.util.ArrayList;

>>>>>>> d0d17e3f04afce914217ec0dc891c9d2275fe01c
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
<<<<<<< HEAD
	
=======
	public ArrayList<String> upcs;	
>>>>>>> d0d17e3f04afce914217ec0dc891c9d2275fe01c
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
//        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//        intent.setPackage("com.google.zxing.client.android");
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//        startActivityForResult(intent, 0);
//    	
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    	
    	TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText("Found barcodes.");
    }
    
    public void RateBeers(View view) {
    	TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText("Beers rated.");
    }
    
    public void Visualize(View view) {
    	TextView textview = (TextView) findViewById(R.id.TopText);
    	textview.setText("Visualizing...");
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	Toast t = Toast.makeText(this, "GOT IT", Toast.LENGTH_LONG);
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {
    		upcs.add(scanResult.getContents());
			TextView textview = (TextView) findViewById(R.id.TopText);
			String u = "";
    		for(String i : upcs) {
    			u += i + ", ";
    		}
			textview.setText(scanResult.getContents());
    	}
    }
    
}
