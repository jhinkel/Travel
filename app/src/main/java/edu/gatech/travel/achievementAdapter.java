package edu.gatech.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class achievementAdapter extends ArrayAdapter<String>{ //change string
    private ArrayList<String> listItems;

    public achievementAdapter(Context context, ArrayList<String> listItems){
        super(context,R.layout.achievement_card,listItems);
        this.listItems = listItems;
    }

    private static class ViewHolder {
        TextView title;
        TextView description;
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


        //Return the view!
        return convertView;
    }
}