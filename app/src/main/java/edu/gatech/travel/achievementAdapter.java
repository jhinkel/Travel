package edu.gatech.travel;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import android.location.LocationManager;


public class achievementAdapter extends ArrayAdapter<String> {
    private ArrayList<String> listItems;
    double latitude, longitude;

    public achievementAdapter(Context context, ArrayList<String> listItems){
        super(context,R.layout.achievement_card,listItems);
        this.listItems = listItems;

    }
    public void setLatLong(double latitude,double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
    private static class ViewHolder {
        TextView title;
        TextView description;
        Button onShare;
        Button onComplete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.achievement_card, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDescription);
            viewHolder.onShare = (Button) convertView.findViewById(R.id.btnShare);
            viewHolder.onComplete = (Button) convertView.findViewById(R.id.btnComplete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DBController database = new DBController(this.getContext());
        ArrayList<HashMap<String, String>> ListofLists2 = database.getAllAchievements();
        //Populate the data into the template view using the data object
        int index = 0;
        while(!ListofLists2.get(index).get("id").equals(listItems.get(position)) && index<listItems.size())
        {
            index++;
        }

        viewHolder.title.setText(ListofLists2.get(index).get("title"));
        viewHolder.description.setText(ListofLists2.get(index).get("description"));

        //Set code for buttons!
        double myLat = Double.parseDouble(ListofLists2.get(index).get("latitude"));
        double myLong = Double.parseDouble(ListofLists2.get(index).get("longitude"));
        double achRadius = Double.parseDouble(ListofLists2.get(index).get("radius"));
        float results[] = {-1,-1,-1};
        Location.distanceBetween(myLat, myLong, latitude, longitude, results);

        Log.e("MYLAT", Double.toString(myLat));
        Log.e("MYLON", Double.toString(myLong));
        Log.e("latitude", Double.toString(latitude));
        Log.e("longitude", Double.toString(longitude));



        viewHolder.onComplete.setEnabled(false);

        if (ListofLists2.get(index).get("completed").equals("1"))
        {
            viewHolder.onShare.setEnabled(true);
        } else {
            viewHolder.onShare.setEnabled(false);
            if (achRadius > results[0]) {
                viewHolder.onComplete.setEnabled(true);
            }
        }

        //Return the view!
        return convertView;
    }
}