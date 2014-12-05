package edu.gatech.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


public class AddNewAchievement extends Activity {
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            View parentView = (View) v.getParent();
            EditText title = (EditText) parentView.findViewById(R.id.tfTitle);
            EditText ImageLink = (EditText) parentView.findViewById(R.id.tfImageLink);
            EditText description = (EditText) parentView.findViewById(R.id.tfDescription);
            queryValues.put("title", title.getText().toString());
            queryValues.put("imageLink", ImageLink.getText().toString());
            queryValues.put("description", description.getText().toString());
            queryValues.put("latitude", "0");
            queryValues.put("longitude", "0");
            queryValues.put("radius", "2");


            controller.insertAchievement(queryValues);

            syncSQLiteMySQLDBAchievement(queryValues);
            startActivity(new Intent(getApplicationContext(), AchievementList.class));
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
