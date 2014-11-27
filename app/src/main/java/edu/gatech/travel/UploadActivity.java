package edu.gatech.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;


public class UploadActivity extends Activity {
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }
    public void onEditClick(View v){
        startActivity(new Intent(getApplicationContext(),SelectList.class));
    }
    public void onAddClick(View v){
        View parentView = (View) v.getParent();
        EditText title = (EditText) parentView.findViewById(R.id.tfTitle);
        EditText description = (EditText) parentView.findViewById(R.id.tfDescription);


        queryValues.put("title", title.getText().toString());
        queryValues.put("description", description.getText().toString());
        queryValues.put("latitude", "0");
        queryValues.put("longitude", "0");
        queryValues.put("achievements", "");
        controller.insertList(queryValues);

        startActivity(new Intent(getApplicationContext(), AchievementList.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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
    public void syncSQLiteMySQLDBList( HashMap<String, String> listvals) {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        params.put("title",listvals.get("title"));
        params.put("description",listvals.get("description"));
        params.put("latitude",listvals.get("latitude"));
        params.put("longitude",listvals.get("longitude"));
        params.put("achievements",listvals.get("achievements"));

        client.post("http://www.johnhinkel.com/sqlitemysqlsync/insertList.php", params, new AsyncHttpResponseHandler() {

        });
    }
}
