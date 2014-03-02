package com.beergoggles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

class RequestTask extends AsyncTask<String, String, String> {

	private String getBeerName(String upc) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(upc));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }//                String regex_pattern = ".*?Description.*</tr>";

        String regex_pattern;
        Pattern pattern;
        Matcher m;
        String input;
        boolean found;
        
        input = responseString;
        regex_pattern = ".*Description.*";
        
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        found = m.find();
        Log.v("RESPONSE", "mgroup0");
        String descriptionLine;
        if (found) {
        	descriptionLine = m.group(0);
        } else {
        	descriptionLine = "none";
        }
        
        Log.v("RESPONSE", descriptionLine);

        input = descriptionLine;
        regex_pattern = "<td>[^<]*</td>";
        
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        m.find();
        m.find();
        found = m.find();

        if (found) {
        	input = m.group(0);
        } else {
        	input = "none";
        }

        Log.v("RESPONSE",input);
        
        regex_pattern = "[a-z A-Z0-9]+";
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        m.find();
        found = m.find();
        String final_name;
        if (found) {
        	final_name = m.group(0);
        } else {
        	final_name = "none";
        }
        Log.v("RESPONSE", final_name);
        
        return final_name;
	}
	
	private String getBeerJSON(String name) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        try {
            response = httpclient.execute(new HttpGet("http://beergoggles-freemancw.rhcloud.com/beerSearch/"+name.replaceAll("\\s+","\\+")));
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
        
        Log.v("JSON", responseString);
        
        return responseString;
    }
	
    @Override
    protected String doInBackground(String... uri) {
    	return getBeerJSON(getBeerName(uri[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}