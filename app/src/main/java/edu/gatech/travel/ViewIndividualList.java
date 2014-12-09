package edu.gatech.travel;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.HashMap;


public class ViewIndividualList extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener { //change string{
    achievementAdapter adapter;
    private UiLifecycleHelper uiHelper;
    DBController database = new DBController(this);
    LocationClient myLocationClient;
    double latitude;
    double longitude;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_individual_list);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        myLocationClient = new LocationClient(getApplicationContext(), this, this);
        if(myLocationClient != null)
            myLocationClient.connect();

        //Get the string of achievements from the intent, split it, and put it in an array.
        String achieveString = getIntent().getExtras().getString("Achievements");
        Log.e("achieveString",achieveString);
        if(achieveString != null) {
            String[] achievements = achieveString.split(", ");
            ArrayList<String> intentAchievements = new ArrayList<String>();
            intentAchievements.addAll(Arrays.asList(achievements));

            adapter = new achievementAdapter(this, intentAchievements, latitude, longitude);

            ListView x = (ListView) this.findViewById(R.id.list);
            x.setAdapter(adapter);
        }
        else{
            Log.e("EVENT FIRED ACHIEVEMENT!!!!", "EVENT FIRED");
        }
    }

    String achievementTitle = "";
    public void onShareClick(View v){
        View parentView = (View) v.getParent().getParent();
        EditText id = (EditText) parentView.findViewById(R.id.tvTitle);
        achievementTitle = id.getText().toString();
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink("http://fc00.deviantart.net/fs70/i/2010/333/9/a/abe_lincoln_riding_a_grizzly_by_sharpwriter-d33u2nl.png")
                .setPicture("http://fc00.deviantart.net/fs70/i/2010/333/9/a/abe_lincoln_riding_a_grizzly_by_sharpwriter-d33u2nl.png")
                .setCaption("Abraham Lincoln- Father of the Internet")
                .setName(achievementTitle)
                .setDescription("I completed the \"" + achievementTitle + "\" achievement!\nMan it's hard to be awesome.")
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    public void onCompleteClick(View v) {
        View parentView = (View) v.getParent().getParent();
        EditText id = (EditText) parentView.findViewById(R.id.tvTitle);
        achievementTitle = id.getText().toString();

        ArrayList<HashMap<String,String>> listAchievements = new ArrayList<HashMap<String,String>>();
        listAchievements = database.getAllAchievements();

        int position = 0;
        while(!listAchievements.get(position).get("title").equals(achievementTitle) && position < listAchievements.size()) {
            position++;
        }

        database.UpdateCompleted(listAchievements.get(position));
        updateCompletedServer(listAchievements.get(position));

        adapter.notifyDataSetChanged();
    }

    public void updateCompletedServer( HashMap<String, String> listvals) {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
// Http Request Params Object
        RequestParams params = new RequestParams();
        params.put("title",listvals.get("title"));
        params.put("description",listvals.get("description"));
        client.post("http://www.johnhinkel.com/sqlitemysqlsync/updateAchievementCompleted.php", params, new AsyncHttpResponseHandler() {
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_individual_list, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
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
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

