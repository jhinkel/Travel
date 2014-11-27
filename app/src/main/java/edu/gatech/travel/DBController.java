package edu.gatech.travel;

        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;


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

}