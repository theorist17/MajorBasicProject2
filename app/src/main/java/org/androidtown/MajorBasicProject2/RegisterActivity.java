package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TaskFragment.OnSendListener {
    public Fragment curFragment;
    Fragment[] fragments = {new RegisterFragment(), new Business1SetFragment(), new Business2NameFragment(), new Business3LocaFragment(), new Business4GPSFragment(), new Business5EnviFragment(), new Business6ServFragment(),
        new Quest1TaskFragment(),  new Quest2PhotoFragment(), new Quest3BodyFragment(), new Quest4TitleFragment(),
            new Recruit2PriceFragment(), new Recruit3TypeFragment(), new Recruit4TimeFragment(),
    };

    Button btnClose, btnNext;

    //user
    public String id_user;

    //address
    public String country;
    public String province;
    public String city;
    public String town;
    public String road;
    public String postcode;
    public double latitude;
    public double longitude;

    //business
    public String id_address;
    public String id_field_busi;
    public String id_brand_busi;
    public String signature;
    public String license;
    public String space;
    public String[] accesses = new String[4];
    public String[] safeties = new String[2];
    public String[] facilities = new String[8];
    public String[] welfares = new String[11];

    //quest
    public ArrayList<String> id_task_list = new ArrayList<>();
    public String id_field;
    public ArrayList<String> id_image_list = new ArrayList<>();
    public String title;
    public String body;

    //recruit
    public String price;
    public String bonus;
    public String type;
    public String start;
    public String end;
    public int capacity;
    public ArrayList<String> id_date_list = new ArrayList<>();

    //links
    public String id_business;
    public String id_recruit;

    @Override
    public void onSendData(ArrayList<String> selected) {
        ((Quest1TaskFragment)curFragment).id_task_list = selected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");

        btnClose = (Button) findViewById(R.id.btnClose);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnClose.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        ((RegisterFragment)fragments[0]).phase = 1;
        initFragment();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btnClose):
                initFragment();
                break;
            case (R.id.btnNext):
                if(curFragment.getClass().equals(Business6ServFragment.class)) {
                    ((RegisterFragment) fragments[0]).phase = 2;
                    initFragment();
                    uploadBusiness();
                } else if(curFragment.getClass().equals(Quest4TitleFragment.class)) {
                    ((RegisterFragment) fragments[0]).phase = 3;
                    initFragment();
                    uploadQuest();
                }  else {
                    nextFragment();
                }
                break;
        }
    }


    public void initFragment(){
        clearFragmentBackStack();
        curFragment = fragments[0];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.registerContainer, curFragment);
        transaction.commit();
    }
    public void indexFragment(int index){
        curFragment = fragments[index];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.registerContainer, curFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void nextFragment(){
        int index;
        for(index=0; index<fragments.length; index++)
            if(curFragment.getClass().equals(fragments[index].getClass())) break;
        if(index==fragments.length-1) return;
        curFragment = fragments[index+1];
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.registerContainer, curFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void prevFragment(){
        int index;
        for(index=0; index<fragments.length; index++)
            if(curFragment.getClass().equals(fragments[index].getClass())) break;
        if(index==0) return;
        curFragment = fragments[index-1];
    }
    public void clearFragmentBackStack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void popupFragmentBackStack(int index){
        FragmentManager fm = getSupportFragmentManager();
        for(int i =1 ; i < index; i++)
            fm.popBackStack();
    }
    public boolean isVisibleClosePrevNext(){
        if(btnClose.getVisibility()==View.VISIBLE)
            return true;
        else
            return false;
    }
    public void setButtonVisible(){
        if(btnClose.getVisibility()==View.GONE){
            btnClose.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }
    public void setButtonGone(){
        if(btnClose.getVisibility()==View.VISIBLE) {
            btnClose.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
    }
    public void reverseClosePrevNext(){
        if(btnClose.getVisibility()==View.VISIBLE){
            btnClose.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }else{
            btnClose.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        prevFragment();
        super.onBackPressed();
    }
    public void uploadBusiness(){
        HandlerDB upload_business = new HandlerDB("upload_business.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", id_user);
        input.put("id_address", id_address);
        input.put("str_field", id_field_busi);input.put("str_brand", id_brand_busi);input.put("signature", signature);input.put("license", license);
        input.put("space", space);
        input.put("transport", accesses[0]); input.put("subway", accesses[1]); input.put("bus_stop", accesses[2]); input.put("parking_lot", accesses[3]);
        input.put("security", safeties[0]); input.put("fire_extingusher", safeties[1]);
        input.put("toilet", facilities[0]); input.put("air_conditioner", facilities[1]); input.put("heater", facilities[2]); input.put("desk", facilities[3]); input.put("wifi", facilities[4]); input.put("locker", facilities[5]); input.put("laptop", facilities[6]); input.put("wheel_chair", facilities[7]);
        input.put("breakfast", welfares[0]); input.put("lunch", welfares[1]); input.put("dinner", welfares[2]); input.put("lodgment", welfares[3]); input.put("uniform", welfares[4]); input.put("locker_room", welfares[5]); input.put("shower", welfares[6]); input.put("laundry", welfares[7]); input.put("drier", welfares[8]); input.put("fitness_center", welfares[9]); input.put("pregnancy", welfares[10]);
        upload_business.execute(input);
        upload_business.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")) {
                    id_business = dbResult.get(0).get("id_last");
                    Toast.makeText(RegisterActivity.this, "사업장 정보를 저장했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void uploadQuest() {

    }

    public void uploadRecruit(){
        HandlerDB upload_recruit = new HandlerDB("upload_recruit.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", id_user);
        input.put("price", price);
        input.put("bonus", bonus);
        input.put("type", type);
        input.put("capacity", capacity+"");
        input.put("start", start);
        input.put("end", end);
        upload_recruit.execute(input);
        upload_recruit.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")) {
                    id_recruit = dbResult.get(0).get("id_last");
                    Toast.makeText(RegisterActivity.this, "모집 정보를 저장했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void uploadRegister(){
        HandlerDB upload_register = new HandlerDB("upload_register.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", id_user);
        input.put("id_business", id_business);
        input.put("id_recruit", id_recruit);
        input.put("id_field", id_field);
        input.put("title", title);
        input.put("body", body);

        input.put("latitude", String.valueOf(latitude));
        input.put("longitude", String.valueOf(longitude));

        String json_task_list = new Gson().toJson(id_task_list);
        String json_image_list = new Gson().toJson(id_image_list);
        String json_date_list = new Gson().toJson(id_date_list);

        input.put("json_task_list", json_task_list);
        input.put("json_image_list", json_image_list);
        input.put("json_date_list", json_date_list);

        upload_register.execute(input);
        upload_register.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")) {
                    Toast.makeText(RegisterActivity.this, "의뢰를 등록했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
