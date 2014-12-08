package edu.gatech.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import android.location.LocationManager;
import android.widget.Toast;

public class AddNewAchievement extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues = new HashMap<String, String>();
    LocationClient myLocationClient;
    double latitude;
    double longitude;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    public void onConnected(Bundle bundle) {
        myLocationClient.requestLocationUpdates(REQUEST, this);

    }
    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myLocationClient = new LocationClient(getApplicationContext(), this, this);

        if(myLocationClient != null)
            myLocationClient.connect();
        LocationManager locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_achievement);
    }
    public void onAddClick(View v){
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //mobile
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();

        //wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
        if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
            if(latitude == 0 || longitude== 0){
                toast("Still obtaining your location... please try again.");
            }else {
                View parentView = (View) v.getParent();
                EditText title = (EditText) parentView.findViewById(R.id.tfTitle);
                EditText ImageLink = (EditText) parentView.findViewById(R.id.tfImageLink);
                EditText description = (EditText) parentView.findViewById(R.id.tfDescription);
                queryValues.put("title", title.getText().toString());
                queryValues.put("imageLink", ImageLink.getText().toString());
                queryValues.put("description", description.getText().toString());
                queryValues.put("latitude", Double.toString(latitude));
                queryValues.put("longitude", Double.toString(longitude));
                queryValues.put("radius", "2");


                controller.insertAchievement(queryValues);

                syncSQLiteMySQLDBAchievement(queryValues);
                String titlepass = getIntent().getExtras().getString("title");
                String descriptionpass = getIntent().getExtras().getString("description");
                Intent intent = new Intent(getApplicationContext(), AchievementList.class);
                intent.putExtra("title", titlepass);
                intent.putExtra("description", descriptionpass);
                startActivity(intent);
            }

        }
    }
    public void syncSQLiteMySQLDBAchievement( HashMap<String, String> listvals) {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
// Http Request Params Object
        RequestParams params = new RequestParams();

        params.put("title",listvals.get("title"));
        params.put("imageLink",listvals.get("title"));
        params.put("description",listvals.get("description"));
        params.put("latitude",listvals.get("latitude"));
        params.put("longitude",listvals.get("longitude"));
        params.put("radius",listvals.get("radius"));
        client.post("http://www.johnhinkel.com/sqlitemysqlsync/insertAchievement.php", params, new AsyncHttpResponseHandler() {
        });
    }
    private void toast(String text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
