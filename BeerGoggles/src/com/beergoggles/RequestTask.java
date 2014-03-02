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

class RequestTask extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        Log.v("RESPONSE", result);
        
//                String regex_pattern = ".*?Description.*</tr>";

        String regex_pattern;
        Pattern pattern;
        Matcher m;
        String input;
        
        input = result;
        regex_pattern = ".*Description.*";
        
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        m.find();
        Log.v("RESPONSE", "mgroup0");
        String descriptionLine = m.group(0);
        
        Log.v("RESPONSE", descriptionLine);

        input = descriptionLine;
        regex_pattern = "<td>[^<]*</td>";
        
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        m.find();
        m.find();
        m.find();

        input = m.group(0);
        Log.v("RESPONSE",input);
        
        regex_pattern = "[a-z A-Z0-9]+";
        pattern = Pattern.compile(regex_pattern);
        m = pattern.matcher(input);
        
        m.find();m.find();
        
        String final_name = m.group(0);
        Log.v("RESPONSE", final_name);

//         while (t.find()) {
//         	for (int i = 0; i < m.groupCount(); i++) {
//         		Log.v("RESPONSE", m.group(i));
//         		// matched text: regexMatcher.group(i)
//         		// match start: regexMatcher.start(i)
//         		// match end: regexMatcher.end(i)
//         	}
//         } 

        //Do anything with response..
    }
}