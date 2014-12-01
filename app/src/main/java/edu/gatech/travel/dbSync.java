package edu.gatech.travel;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John B. Hinkel III on 11/30/2014.
 */
class dbSync extends AsyncTask<URL,Void,String> {
    String responseStr = "";
    public String retStr = "";

    private AsyncResponse delegate;

    public dbSync(AsyncResponse listener){
        delegate = listener;
    }
    protected String doInBackground(URL... urls) {
        HttpClient httpclient;
        HttpGet request;
        HttpResponse response = null;
        String result = "";
        // TextView to display result

        // Try to connect using Apache HttpClient Library
        try {
            httpclient = new DefaultHttpClient();
            request = new HttpGet("http://www.johnhinkel.com/sqlitemysqlsync/viewList.php");
            response = httpclient.execute(request);
        }

        catch (Exception e) {
            // Code to handle exception
            //result = "error";
        }

        // response code
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {

                // Appending result to textview
                result = result + line ;
            }
        } catch (Exception e) {
            // Code to handle exception
            //result = "error";
        }
        return result;
    }

    protected void onProgressUpdate(Void values) {
    Log.e("MyAsyncTask", "onProgressUpdate");

    }

    protected void onPostExecute(String result) {
        if(result != null || result != "error") {
            Log.e("AsyncTask", result);
            responseStr = result;
        }
        delegate.processFinish(result);


    }
}
