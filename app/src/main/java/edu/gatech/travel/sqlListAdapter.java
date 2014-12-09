package edu.gatech.travel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class sqlListAdapter extends ArrayAdapter<HashMap<String,String>>{ //change string
    public sqlListAdapter(Context context, ArrayList<HashMap<String,String>> listItems){
        super(context,R.layout.list_card,listItems);
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
            convertView = inflater.inflate(R.layout.list_card, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DBController database = new DBController(this.getContext());
        ArrayList<HashMap<String, String>> ListofLists2 = database.getAllLists();
        //Populate the data into the template view using the data object

        viewHolder.title.setText(ListofLists2.get(position).get("title"));
        viewHolder.description.setText(ListofLists2.get(position).get("description"));

        final String achievementIds = ListofLists2.get(position).get("achievements");
        android.view.View.OnClickListener yourClickListener = new android.view.View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ViewIndividualList.class);
                intent.putExtra("Achievements", achievementIds);
                getContext().startActivity(intent);

            }
        };

        convertView.setOnClickListener(yourClickListener);
        //Return the view!
        return convertView;
    }

    //THIS MAY OR NOT FIX THIS
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

}