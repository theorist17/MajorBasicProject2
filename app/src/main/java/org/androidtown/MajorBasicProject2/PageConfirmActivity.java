package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PageConfirmActivity extends AppCompatActivity {
    String id_quest;

    LinearLayout llApplyView, llConfirmView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_confirm);

        llApplyView = (LinearLayout)findViewById(R.id.llApplyView);
        llConfirmView =  (LinearLayout) findViewById(R.id.llConfirmView);

        id_quest = getIntent().getStringExtra("id_quest");
        downloadContact(id_quest);
    }

    public void downloadContact(String id_quest){
        HandlerDB handler = new HandlerDB("download_contract.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_quest", id_quest);
        handler.execute(input);
        handler.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    writeContract(dbResult);
                } else {
                    Toast.makeText(PageConfirmActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void writeContract(ArrayList<HashMap<String, String>> dbResult){

        llApplyView.removeAllViews();
        llConfirmView.removeAllViews();

        final LayoutInflater inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i< dbResult.size(); i++){

            // inflate child
            final View contractbar = inflator.inflate(R.layout.item_contractbar, null);

            // initialize review UI
            Button name = contractbar.findViewById(R.id.btnName);
            LinearLayout llDateView = contractbar.findViewById(R.id.llDateView);
            TextView id_agent = contractbar.findViewById(R.id.id_agent);
            ImageView ivAgent = contractbar.findViewById(R.id.ivClientCover);

            ivAgent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // set inputData
            name.setText(dbResult.get(i).get("name"));
            id_agent.setText(dbResult.get(i).get("id_agent"));


            //add child
            if(dbResult.get(i).get("state").equals("apply")){
                llApplyView.addView(contractbar);

                //add spacer
                if(i != dbResult.size() -1) {
                    View spacer = inflator.inflate(R.layout.item_spacer, null);
                    llApplyView.addView(spacer);
                }
            } else if(dbResult.get(i).get("state").equals("confirm")) {
                llConfirmView.addView(contractbar);

                //add spacer
                if(i != dbResult.size() -1) {
                    View spacer = inflator.inflate(R.layout.item_spacer, null);
                    llConfirmView.addView(spacer);
                }
            }
        }
    }
}
