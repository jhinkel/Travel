package edu.gatech.travel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/*
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.JsonHttpResponseHandler;
        import com.loopj.android.http.RequestParams;
        */


public class DBController  extends SQLiteOpenHelper implements AsyncResponse, AsyncResponse2{
    dbSync syncResult = new dbSync(this);
    dbSync2 syncResult2 = new dbSync2(this);
    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
    //Creates Table
    @Override

    public void onCreate(SQLiteDatabase database) {
        String query;
        String query2;
        query = "CREATE TABLE lists(ID integer NOT NULL, title varchar(50), description varchar(500), latitude varchar(500), longitude varchar(500), achievementids varchar(1000), PRIMARY KEY(ID))";
        query2 = "CREATE TABLE achievements(ID integer NOT NULL, title varchar(50), imageLink varchar(500), latitude varchar(500), longitude varchar(500), radius varchar(5), description varchar(500), completed boolean, PRIMARY KEY(ID))";
        database.execSQL(query);
        database.execSQL(query2);

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        String query2;
        query = "DROP TABLE IF EXISTS lists";
        query2 = "DROP TABLE IF EXISTS achievements";

        database.execSQL(query);
        database.execSQL(query2);
        onCreate(database);
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */

    public void insertList(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", queryValues.get("title"));
        values.put("description", queryValues.get("description"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("achievementids", queryValues.get("achievementids"));

        database.insert("lists", null, values);
        database.close();
    }
    public void UpdateListWithAchievements(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("achievementids", queryValues.get("achievementids"));


        String[] params = {queryValues.get("title"),queryValues.get("description")};

        database.update("lists",values,"title=? and description=?",params);
        database.close();
    }
    public void UpdateCompleted(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("completed", true);


        String[] params = {queryValues.get("title"),queryValues.get("description")};

        database.update("achievements",values,"title=? and description=?",params);
        database.close();
    }
    public int getAchievementCount(HashMap<String,String> listvals){
        String selectQuery = "SELECT achievementids FROM lists where title='" + listvals.get("title").toString() + "' and description='" + listvals.get("description").toString() + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        String sqlListIds = "";
        String[] indivIds = null;
        if (cursor.moveToFirst()) {
            indivIds = cursor.getString(0).split(", ");
            for(int i=0;i<indivIds.length;i++){

                if (i==0){
                    sqlListIds = indivIds[i];
                }
                else{
                    sqlListIds += "," + indivIds[i];
                }
            }
        }

        String sqlQuery2 = "select count(*) from achievements where id in(" + sqlListIds + ") and completed=1";
        Cursor cursor2 = database.rawQuery(sqlQuery2, null);
        HashMap<String,String> countCompleted = new HashMap<String,String>();
        Log.e("SQLQUERY2",sqlQuery2);
        if (cursor2.moveToFirst()) {
            do {

                countCompleted.put("count", cursor2.getString(0));


            } while (cursor2.moveToNext());
        }

        int countCompletedNumber = Integer.parseInt(countCompleted.get("count"));
        int totalAchievements = 0;
        try {
            totalAchievements = indivIds.length;
        }
        catch(Exception e){

        }
        float percentCompleted = 0;
        if(countCompletedNumber != 0 || totalAchievements != 0){
            percentCompleted = countCompletedNumber / totalAchievements;
        }
        Log.e("percentCompleted",Float.toString(percentCompleted));
        if(percentCompleted ==0){
            return 0;
        }
        else if((percentCompleted * 100) < 50){
            return 1;
        }
        else if((percentCompleted * 100)>= 50){
            return 2;
        }
        else if((percentCompleted * 100)==100){
            return 3;
        }
        else{
            return -1;
        }

    }
    public void insertAchievement(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", queryValues.get("title").toString());
        values.put("imageLink", queryValues.get("imageLink").toString());
        values.put("description", queryValues.get("description").toString());
        values.put("latitude", queryValues.get("latitude").toString());
        values.put("longitude", queryValues.get("longitude").toString());
        values.put("radius", queryValues.get("radius").toString());
        values.put("completed",queryValues.get("completed").toString());

        database.insert("achievements", null, values);
        database.close();
    }



    public ArrayList<HashMap<String, String>> getAllLists() {
        ArrayList<HashMap<String, String>> ListOfLists;
        ListOfLists = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM lists";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", cursor.getString(1));
                map.put("description", cursor.getString(2));
                map.put("latitude", cursor.getString(3));
                map.put("longitude", cursor.getString(4));
                map.put("achievements", cursor.getString(5));

                ListOfLists.add(map);
                

            } while (cursor.moveToNext());
        }

        return ListOfLists;
    }

    public ArrayList<HashMap<String, String>> getAllAchievements() {
        ArrayList<HashMap<String, String>> ListOfLists;
        ListOfLists = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM achievements";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", cursor.getString(0));
                map.put("title", cursor.getString(1));
                map.put("imageLink", cursor.getString(2));
                map.put("latitude", cursor.getString(3));
                map.put("longitude", cursor.getString(4));
                map.put("radius", cursor.getString(5));
                map.put("description", cursor.getString(6));
                map.put("completed",cursor.getString(7));

                ListOfLists.add(map);

            } while (cursor.moveToNext());
        }

        return ListOfLists;
    }


    public void syncDatabases(){
        // Http Request Params Object
        final SQLiteDatabase database = this.getWritableDatabase();
        //CLEAR LISTS SQLITE TABLE AND INSERT SERVER RESPONSE INTO CLEARED TABLE
        String query;
        String query2;
        query = "DROP TABLE IF EXISTS lists";
        query2 = "DROP TABLE IF EXISTS achievements";

        database.execSQL(query);
        database.execSQL(query2);
        String query3;
        String query4;
        query3 = "CREATE TABLE lists(ID integer NOT NULL, title varchar(50), description varchar(500), latitude varchar(500), longitude varchar(500), achievementids varchar(1000), PRIMARY KEY(ID))";
        query4 = query2 = "CREATE TABLE achievements(ID integer NOT NULL, title varchar(50), imageLink varchar(500), latitude varchar(500), longitude varchar(500), radius varchar(5), description varchar(500), completed boolean, PRIMARY KEY(ID))";
        database.execSQL(query3);
        database.execSQL(query4);
        URL viewListURL = null;
        try {
             viewListURL = new URL("http://www.johnhinkel.com/sqlitemysqlsync/viewList.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URL viewAchievementURL = null;
        try {
            viewAchievementURL = new URL("http://www.johnhinkel.com/sqlitemysqlsync/viewAchievement.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<HashMap<String, String>> retMap = null;

        syncResult.execute(viewListURL);
        syncResult2.execute(viewAchievementURL);





    }
    public void processFinish2(String output){
        final SQLiteDatabase database = this.getWritableDatabase();
        Log.e("PROCESS FINISHED SECOND ONE",output);
        JSONArray arr = null;
        ArrayList<HashMap<String, String>> ListofLists = new ArrayList<HashMap<String, String>>();
        try {
            arr = new JSONArray(output);
            System.out.println(arr.length());

            for(int i=0; i<arr.length();i++){
                JSONObject obj = (JSONObject)arr.get(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", obj.get("title").toString());
                map.put("imageLink", obj.get("imageLink").toString());
                map.put("description", obj.get("description").toString());
                map.put("latitude", obj.get("latitude").toString());
                map.put("longitude", obj.get("longitude").toString());
                map.put("radius", obj.get("radius").toString());
                if(obj.get("completed").toString().equals("yes")) {
                    map.put("completed", "1");
                }
                else{
                    map.put("completed","0");
                }
                ListofLists.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<ListofLists.size();i++){
            insertAchievement(ListofLists.get(i));
        }
        ArrayList<HashMap<String, String>> retAchievement = getAllAchievements();

    }
    public void processFinish(String output){
       final SQLiteDatabase database = this.getWritableDatabase();

        JSONArray arr = null;
        ArrayList<HashMap<String, String>> ListofLists = new ArrayList<HashMap<String, String>>();
        try {
            arr = new JSONArray(output);
            System.out.println(arr.length());

           for(int i=0; i<arr.length();i++){
               JSONObject obj = (JSONObject)arr.get(i);
               HashMap<String, String> map = new HashMap<String, String>();
               map.put("title", obj.get("title").toString());
               map.put("description", obj.get("description").toString());
               map.put("latitude", obj.get("latitude").toString());
               map.put("longitude", obj.get("longitude").toString());
               map.put("achievementids", obj.get("achievements").toString());
               ListofLists.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0;i<ListofLists.size();i++){
            insertList(ListofLists.get(i));
        }



    }
}