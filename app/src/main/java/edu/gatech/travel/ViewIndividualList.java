package edu.gatech.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashMap;


public class ViewIndividualList extends Activity {
    //ArrayList<String> listItems=new ArrayList<String>();
    achievementAdapter adapter;
    //DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_individual_list);

        //Get the string of achievements from the intent, split it, and put it in an array.
        String achieveString = getIntent().getExtras().getString("Achievements");
        Log.e("achieveString",achieveString);
        if(achieveString != null) {
            String[] achievements = achieveString.split(", ");
            ArrayList<String> intentAchievements = new ArrayList<String>();
            intentAchievements.addAll(Arrays.asList(achievements));


            adapter = new achievementAdapter(this, intentAchievements);

            //DBController database = new DBController(this);
            ListView x = (ListView) this.findViewById(R.id.list);
            x.setAdapter(adapter);

            //listItems.addAll(achievements);
        }
        else{
            Log.e("EVENT FIRED ACHIEVEMENT!!!!", "EVENT FIRED");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_individual_list, menu);
        return true;
    }
    public void onAchievementClick(View v){
        startActivity(new Intent(getApplicationContext(),ViewIndividualAchievement.class));
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
