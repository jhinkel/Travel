package edu.gatech.travel;

        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.widget.Toast;

        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
    //Creates Table
    @Override

    public void onCreate(SQLiteDatabase database) {
        String query;
        String query2;
        query = "CREATE TABLE lists(ID integer NOT NULL, title varchar(50), description varchar(500), latitude varchar(500), longitude varchar(500), achievementids varchar(1000), PRIMARY KEY(ID))";
        query2 = "CREATE TABLE achievements(ID integer NOT NULL, title varchar(50), imageLink varchar(500), latitude varchar(500), longitude varchar(500), radius varchar(5), description varchar(500), PRIMARY KEY(ID))";
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
        values.put("achievements", queryValues.get("achievements"));

        database.insert("lists", null, values);
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
                map.put("title", cursor.getString(0));
                map.put("description", cursor.getString(1));
                map.put("latitude", cursor.getString(2));
                map.put("longitude", cursor.getString(3));
                map.put("achievements", cursor.getString(4));

                ListOfLists.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return ListOfLists;
    }


    public void syncDatabases(){
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        final SQLiteDatabase database = this.getWritableDatabase();
        RequestParams params = new RequestParams();
        client.post("http://www.johnhinkel.com/sqlitemysqlsync/viewList.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                //CLEAR LISTS SQLITE TABLE AND INSERT SERVER RESPONSE INTO CLEARED TABLE
                String query;
                String query2;
                query = "DROP TABLE IF EXISTS lists";
                query2 = "DROP TABLE IF EXISTS achievements";

                database.execSQL(query);
                database.execSQL(query2);
                onCreate(database);

                //insert data back into tables;
                try {
                    JSONArray arr = new JSONArray(response);
                    System.out.println(arr.length());
                    for(int i=0; i<arr.length();i++){
                        JSONObject obj = (JSONObject)arr.get(i);
                        System.out.println(obj.get("id"));
                        System.out.println(obj.get("status"));
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("title", obj.get("title").toString());
                        queryValues.put("description", obj.get("description").toString());
                        queryValues.put("latitude", obj.get("latitude").toString());
                        queryValues.put("longitude", obj.get("longitude").toString());
                        queryValues.put("achievements", obj.get("achievements").toString());
                        insertList(queryValues);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // TODO Auto-generated method stub

                System.out.println("FAIL");

            }



        });
        Cursor cursor = database.rawQuery("select * from lists", null);

        System.out.println(cursor.getCount());
    }

}