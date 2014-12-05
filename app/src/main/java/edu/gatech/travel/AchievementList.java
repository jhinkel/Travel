package edu.gatech.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class AchievementList extends Activity {
    ArrayList<String> listItems=new ArrayList<String>();
    achievementAdapter adapter;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);

        adapter=new achievementAdapter(this, listItems);

        DBController database = new DBController(this);
        ListView x = (ListView) this.findViewById(R.id.listView);
        x.setAdapter(adapter);
        ArrayList<HashMap<String,String>> temp = controller.getAllAchievements();
        for(int i=0;i<temp.size();i++){
            listItems.add(temp.get(i).get("id"));
        }

    }

    public void onNewClick(View v){
        startActivity(new Intent(getApplicationContext(), AddNewAchievement.class));
    }

    public void onExistingClick(View v){
        //show list of existing ones
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
