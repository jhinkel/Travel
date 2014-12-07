package edu.gatech.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;


public class AchievementList extends Activity {
    ArrayList<String> listItems=new ArrayList<String>();
    achievementAdapterAdd adapter;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);

        adapter=new achievementAdapterAdd(this, listItems);

        DBController database = new DBController(this);
        ListView x = (ListView) this.findViewById(R.id.listView);
        x.setAdapter(adapter);
        ArrayList<HashMap<String,String>> temp = controller.getAllAchievements();
        for(int i=0;i<temp.size();i++){
            listItems.add(temp.get(i).get("id"));
        }


    }

    public void onNewClick(View v){
        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        Intent intent = new Intent(getApplicationContext(),AddNewAchievement.class);
        intent.putExtra("title", title);
        intent.putExtra("description",description);
        startActivity(intent);

    }

    String achievementQueue = "";
    public void onExistingClick(View v){
        View parentView = (View) v.getParent();
        EditText id = (EditText) parentView.findViewById(R.id.tvId);
        if (achievementQueue.equals("")){
            achievementQueue = achievementQueue+id.getText().toString();
        }
        else{
            achievementQueue = achievementQueue+", " + id.getText().toString();
        }
        toast("Achievement added to queue");

        //get textbox for id from v (Run app to see if there is an ID textbox.  if not, add it into the card).
        //add that id to a comma-separated list inside a textbox.
    }
    private void toast(String text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
    public void AddAchievementsToList(View v){
        //get title and description from upload activity

        HashMap<String,String> listofParams = new HashMap<String,String>();
        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");

        listofParams.put("title",title);
        listofParams.put("description",description);
        listofParams.put("achievementids",achievementQueue);
        controller.UpdateListWithAchievements(listofParams);
        updateSQLiteMySQLDBList(listofParams);
        toast("achievements successfully added");

    }
    public void updateSQLiteMySQLDBList( HashMap<String, String> listvals) {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
// Http Request Params Object
        RequestParams params = new RequestParams();
        params.put("title",listvals.get("title"));
        params.put("description",listvals.get("description"));
        params.put("id",listvals.get("achievementids"));
        Log.e("ACHIEVEMENT IDS",listvals.get("achievementids"));
        client.post("http://www.johnhinkel.com/sqlitemysqlsync/updateList.php", params, new AsyncHttpResponseHandler() {
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievement_list, menu);
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
