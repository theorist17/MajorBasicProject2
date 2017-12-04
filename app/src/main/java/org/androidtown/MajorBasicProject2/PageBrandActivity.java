package org.androidtown.MajorBasicProject2;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PageBrandActivity extends ExpandableListActivity
{
    // Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Load data
        HandlerDB download_field_brand = new HandlerDB("download_brand.php");
        HashMap<String, String> input = new HashMap<>();
        download_field_brand.execute(input);
        download_field_brand.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json"))
                    initExpandableList(dbResult);
                else
                    Toast.makeText(PageBrandActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initExpandableList(ArrayList<HashMap<String, String>>  dbResult){
        // Create Expandable List and set it's properties
        ExpandableListView expandableList = getExpandableListView();
        expandableList.setDividerHeight(3);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // Set The Data
        setData(dbResult);

        // Create the Adapter
        AdapterExpListView adapter = new AdapterExpListView(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);

        // Set the Adapter to expandableList
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);
    }

    // method to set child data of each parent
    public void setData(ArrayList<HashMap<String, String>> dbResult)
    {

        String industry = dbResult.get(0).get("industry");
        parentItems.add(industry);
        ArrayList<String> child = new ArrayList<>();

        for(int i =0; i < dbResult.size();i++){
            if(!industry.equals(dbResult.get(i).get("industry"))) {
                childItems.add(child);
                industry = dbResult.get(i).get("industry");
                parentItems.add(industry);
                child = new ArrayList<>();
            }
            child.add(dbResult.get(i).get("name"));
        }
        childItems.add(child);
    }
}