package edu.gatech.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class achievementAdapterAdd extends ArrayAdapter<String>{ //change string
    private ArrayList<String> listItems;

    public achievementAdapterAdd(Context context, ArrayList<String> listItems){
        super(context,R.layout.achievement_card_add,listItems);
        this.listItems = listItems;
    }

    private static class ViewHolder {
        TextView title;
        TextView description;
        TextView latitude;
        TextView longitude;
        TextView radius;
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
            viewHolder.latitude = (TextView) convertView.findViewById(R.id.tvLatitude);
            viewHolder.longitude = (TextView) convertView.findViewById(R.id.tvLongitude);
            viewHolder.radius = (TextView) convertView.findViewById(R.id.tvRadius);

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
        viewHolder.latitude.setText(ListofLists2.get(index).get("latitude"));
        viewHolder.longitude.setText(ListofLists2.get(index).get("longitude"));
        viewHolder.radius.setText(ListofLists2.get(index).get("radius"));

        //Return the view!
        return convertView;
    }
}