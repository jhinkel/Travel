package edu.gatech.travel;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewListActivity extends Activity {

    DBController controller = new DBController(this);
    ArrayList<HashMap<String,String>> listItems=new ArrayList<HashMap<String,String>>();
    sqlListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new sqlListAdapter(this, listItems);
        setContentView(R.layout.activity_view_list);
        DBController database = new DBController(this);
        ListView x = (ListView) this.findViewById(R.id.list);
        x.setAdapter(adapter);

        listItems = controller.getAllLists();
        adapter.addAll(listItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_list, menu);
        return true;
    }
    public void onViewClick(View v){
        View parent = (View)v.getParent();
        ListView x = (ListView)parent.findViewById(R.id.list);
        x.setAdapter(adapter);



        //startActivity(new Intent(getApplicationContext(),ViewIndividualList.class));
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
    public void onAchievementViewClick(View v){

    }
}
