package edu.gatech.travel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Keith on 11/30/2014.
 *
 * This is an orphan and not actually connected to anything yet. Needs to be linked to the proper
 * proper activity. Somehow the ViewListActivity needs to call this and pass in the array of
 * hash strings. From there I THINK that I can just use listoflists.size to getCount.
 *
 */
public abstract class ListAdapter extends BaseAdapter {

    @Override
    public int getCount(){
        //TODO Write method to get number of items (query from database!)

        return 0;
    }

    @Override
    public Object getItem(int arg0){
        //TODO autogenerated method stub
        return null;
    }

    public long getItemID(int arg0){
        //TODO autogenerated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2){
        //TODO Create the cell (View) and populate it with an element of the list
        return null;
    }

}
