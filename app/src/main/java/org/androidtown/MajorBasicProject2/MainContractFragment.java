package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainContractFragment extends Fragment implements View.OnClickListener {
    Button btRegister;
    LinearLayout llClientView, llAgentView;

    public ArrayList<HashMap<String, String>> qeustClientList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> qeustAgentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m3_order, container, false);
        btRegister = view.findViewById(R.id.btnRegister);
        btRegister.setOnClickListener(this);

        llClientView = view.findViewById(R.id.llClientView);
        llAgentView = view.findViewById(R.id.llAgentView);

        questClient();
        questAgent();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("id_user", ((MainActivity)getActivity()).id_user);
                startActivity(intent);
                break;
        }
    }
    public void questClient(){
        HandlerDB handler = new HandlerDB("quest_client.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", ((MainActivity)getActivity()).id_user);
        handler.execute(input);
        handler.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    writeQuestClient(dbResult);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void questAgent(){
        HandlerDB handler = new HandlerDB("quest_agent.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", ((MainActivity)getActivity()).id_user);
        handler.execute(input);
        handler.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    writeQuestAgent(dbResult);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void writeQuestClient(ArrayList<HashMap<String, String>> dbResult){

        llClientView.removeAllViews();
        qeustClientList.clear();

        final LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i< dbResult.size(); i++){

            // inflate child
            final View questbar = inflator.inflate(R.layout.item_questbar, null);

            // initialize review UI
            TextView tvfield = questbar.findViewById(R.id.orderbar_item_field);
            TextView tvType = questbar.findViewById(R.id.orderbar_item_type);
            TextView tvValue = questbar.findViewById(R.id.orderbar_item_value);

            TextView tvTitle = questbar.findViewById(R.id.orderbar_item_title);
            LinearLayout llTaskLayout = questbar.findViewById(R.id.orderbar_item_task_layout);
            TextView tvPrice = questbar.findViewById(R.id.orderbar_item_price);

            TextView tvCell = questbar.findViewById(R.id.orderbar_item_cell);
            TextView tvAddress = questbar.findViewById(R.id.orderbar_item_address);

            // set inputData
            tvfield.setText(dbResult.get(i).get("field"));
            tvType.setText(dbResult.get(i).get("type"));
            //tvValue.setText(dbResult.get(i).get("type"));
            tvPrice.setText(dbResult.get(i).get("price"));
            tvCell.setText(dbResult.get(i).get("cell"));
            tvTitle.setText(dbResult.get(i).get("title"));
            tvAddress.setText(dbResult.get(i).get("address"));

            questbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indexOfMyView = ((LinearLayout) v.getParent()).indexOfChild(v)/2;
                    Intent intent = new Intent(getActivity(), PageConfirmActivity.class);
                    intent.putExtra("id_quest", qeustClientList.get(indexOfMyView).get("id_quest"));
                    startActivity(intent);
                }
            });

            //add child
            llClientView.addView(questbar);

            //add spacer
            if(i != dbResult.size() -1) {
                View spacer = inflator.inflate(R.layout.item_spacer, null);
                llClientView.addView(spacer);
            }

            //add data
            HashMap<String, String> quest = new HashMap<String, String>();
            quest.put("index", i+"");
            quest.put("id_quest", dbResult.get(i).get("id_quest"));
            quest.put("latitude", dbResult.get(i).get("latitude"));
            quest.put("longitude", dbResult.get(i).get("longitude"));
            qeustClientList.add(quest);
        }
    }
    public void writeQuestAgent(ArrayList<HashMap<String, String>> db){

        llAgentView.removeAllViews();
        qeustAgentList.clear();

        ArrayList<HashMap<String, String>> dbResult = new ArrayList<>();
        for (int i = 0; i < db.size(); i++) {
            if (!dbResult.contains(db.get(i))) {
                dbResult.add(db.get(i));
            }
        }

        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i< dbResult.size(); i++){

            // inflate child
            View questbar = inflator.inflate(R.layout.item_questbar, null);

            // initialize review UI
            TextView tvfield = questbar.findViewById(R.id.orderbar_item_field);
            TextView tvType = questbar.findViewById(R.id.orderbar_item_type);
            TextView tvValue = questbar.findViewById(R.id.orderbar_item_value);

            TextView tvTitle = questbar.findViewById(R.id.orderbar_item_title);
            LinearLayout llTaskLayout = questbar.findViewById(R.id.orderbar_item_task_layout);
            TextView tvPrice = questbar.findViewById(R.id.orderbar_item_price);

            TextView tvCell = questbar.findViewById(R.id.orderbar_item_cell);
            TextView tvAddress = questbar.findViewById(R.id.orderbar_item_address);

            // set inputData
            tvfield.setText(dbResult.get(i).get("field"));
            tvType.setText(dbResult.get(i).get("type"));
            //tvValue.setText(dbResult.get(i).get("type"));
            tvPrice.setText(dbResult.get(i).get("price"));
            tvCell.setText(dbResult.get(i).get("cell"));
            tvTitle.setText(dbResult.get(i).get("title"));
            tvAddress.setText(dbResult.get(i).get("address"));



            //add child
            llAgentView.addView(questbar);

            //add spacer
            if(i != dbResult.size() -1) {
                View spacer = inflator.inflate(R.layout.item_spacer, null);
                llAgentView.addView(spacer);
            }

            //add data
            HashMap<String, String> quest = new HashMap<String, String>();
            quest.put("index", i+"");
            quest.put("id_quest", dbResult.get(i).get("id_quest"));
            quest.put("latitude", dbResult.get(i).get("latitude"));
            quest.put("longitude", dbResult.get(i).get("longitude"));
            qeustAgentList.add(quest);
        }
    }
}
