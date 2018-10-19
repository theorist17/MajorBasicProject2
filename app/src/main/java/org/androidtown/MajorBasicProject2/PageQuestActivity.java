package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PageQuestActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    String id_user, id_quest;
    String id_business, id_recruit, id_address;

    ViewFlipper vfQuestImage; String id_questCovers;
    Button back, like; TextView cation;

    TextView title;

    Button btnClient; CircleImageView clientCover; String id_client, id_image_client;
    TextView intro;

    TextView capacity, field, type, space;
    TextView body;

    LinearLayout llTaskView; String id_tasks;

    LinearLayout supply;

    MapView mapView; GoogleMap googleMap; String signature, address; Double latitude, longitude;
    LinearLayout transport, subway, busStop, parkingLot;

    CircleImageView commentCover; TextView commentName, commentDate;
    TextView comment;

    Button btnComment; TextView rate;

    TextView bonus;

    TextView bottomPrice, bottomRate, bottomUnit; Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_quest);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");
        id_quest = intent.getStringExtra("id_quest");

        vfQuestImage = (ViewFlipper) findViewById(R.id.vfQuestImage);

        back = (Button) findViewById(R.id.pageQuestBack);
        back.setOnClickListener(this);
        like = (Button) findViewById(R.id.pageQuestLike);
        like.setOnClickListener(this);
        cation = (TextView) findViewById(R.id.pageQuestCation);

        title = (TextView) findViewById(R.id.pageQuestTitle);

        btnClient = (Button)findViewById(R.id.btnClient);
        btnClient.setOnClickListener(this);
        clientCover = (CircleImageView) findViewById(R.id.ivClientCover);
        clientCover.setOnClickListener(this);

        intro = (TextView) findViewById(R.id.tvIntro);

        capacity = (TextView) findViewById(R.id.tv_capacity);
        field = (TextView) findViewById(R.id.tv_field);
        type = (TextView) findViewById(R.id.tv_type);
        space = (TextView) findViewById(R.id.tv_space);

        body = (TextView) findViewById(R.id.tvBody);

        llTaskView = (LinearLayout) findViewById(R.id.llTaskView);
        supply = (LinearLayout) findViewById(R.id.llSupply);

        mapView = (MapView) findViewById(R.id.mapView);

        transport = (LinearLayout) findViewById(R.id.llTransport);
        subway = (LinearLayout) findViewById(R.id.llSubway);
        busStop = (LinearLayout) findViewById(R.id.llBusStop);
        parkingLot = (LinearLayout) findViewById(R.id.llParkingLot);

        commentCover = (CircleImageView) findViewById(R.id.ivCommentCover);
        commentCover.setOnClickListener(this);
        commentName = (TextView) findViewById(R.id.tvCommentName);
        commentDate = (TextView) findViewById(R.id.tvCommentDate);
        comment = (TextView) findViewById(R.id.tvComment);

        btnComment = (Button) findViewById(R.id.btnComment);
        rate = (TextView) findViewById(R.id.tvRate);

        bonus = (TextView) findViewById(R.id.tvBonus);

        bottomPrice = (TextView) findViewById(R.id.tvBottomPrice);
        bottomRate = (TextView) findViewById(R.id.tvBottomRate);
        bottomUnit = (TextView) findViewById(R.id.tvBottomUnit);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        selectQuest();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnClient:
                intent = new Intent(this, PageProfileActivity.class);
                intent.putExtra("id_user", id_user);
                intent.putExtra("id_target", id_client);
                startActivity(intent);
                break;
            case R.id.btnCheck:
                intent = new Intent(this, PageCheckActivity.class);
                intent.putExtra("id_user", id_user);
                intent.putExtra("id_quest", id_quest);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("price", bottomPrice.getText().toString());
                intent.putExtra("unit", bottomUnit.getText().toString());
                intent.putExtra("rate", bottomRate.getText().toString());
                startActivity(intent);
                break;
        }
    }

    public void selectQuest(){
        HandlerDB select_quest = new HandlerDB("select_quest.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", id_user);
        input.put("id_quest", id_quest);
        select_quest.execute(input);
        select_quest.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    writeQuest(dbResult.get(0));
                } else {
                    Toast.makeText(PageQuestActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void writeQuest(HashMap<String, String> db){
        id_business = db.get("id_business");
        id_recruit = db.get("id_recruit");
        id_address = db.get("id_address");

        signature = db.get("signature");
        address = db.get("address");
        bonus.setText(db.get("bonus"));
        bottomPrice.setText(db.get("price"));

        latitude = Double.parseDouble(db.get("latitude"));
        longitude = Double.parseDouble(db.get("longitude"));

        type.setText(db.get("type"));
        title.setText(db.get("title"));
        body.setText(db.get("body"));
        space.setText(db.get("space"));
        capacity.setText(db.get("capacity")+"명");
        field.setText(db.get("field"));

        id_client = db.get("id_user");
        id_image_client = db.get("id_image_user");
        btnClient.setText("클라이언트 "+db.get("name_user")+"님을 만나보세요.");

        if(db.get("transport").equals("true"))
            transport.setVisibility(View.VISIBLE);
         else
            transport.setVisibility(View.INVISIBLE);

        if(db.get("subway").equals("true"))
            subway.setVisibility(View.VISIBLE);
        else
            subway.setVisibility(View.INVISIBLE);

        if(db.get("bus_stop").equals("true"))
            busStop.setVisibility(View.VISIBLE);
        else
            busStop.setVisibility(View.INVISIBLE);

        if(db.get("parking_lot").equals("true"))
            parkingLot.setVisibility(View.VISIBLE);
        else
            parkingLot.setVisibility(View.INVISIBLE);

        if(db.get("type").equals("시간제 근무")){
            bottomUnit.setText(" / 시간");
        } else if(db.get("type").equals("일일 의뢰")){
            bottomUnit.setText(" / 하루");
        } else if(db.get("type").equals("정규적인 고용")){
            bottomUnit.setText(" / 월");
        } else if(db.get("type").equals("조건 계약")){
            bottomUnit.setText(" / 건");
        }


        //마커 등록
        LatLng gps = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(gps);
        markerOptions.title(signature);
        markerOptions.snippet(address);
        googleMap.addMarker(markerOptions);

        //지도 이동
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
    }
}
