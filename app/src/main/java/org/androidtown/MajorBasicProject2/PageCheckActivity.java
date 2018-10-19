package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PageCheckActivity extends AppCompatActivity implements View.OnClickListener {
    
    TextView title, bottomPrice, bottomRate, bottomUnit; Button btnApply;
    CalendarPickerView calendarView;

    ArrayList<Date> dates = new ArrayList<>();

    String id_user, id_quest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_check);

        title = (TextView) findViewById(R.id.tvTitle);
        bottomPrice = (TextView) findViewById(R.id.tvBottomPrice);
        bottomRate = (TextView) findViewById(R.id.tvBottomRate);
        bottomUnit = (TextView) findViewById(R.id.tvBottomUnit);
        btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        title.setText(getIntent().getStringExtra("title"));
        bottomPrice.setText(getIntent().getStringExtra("price"));
        bottomRate.setText(getIntent().getStringExtra("rate"));
        bottomUnit.setText(getIntent().getStringExtra("unit"));
        id_user = getIntent().getStringExtra("id_user");
        id_quest = getIntent().getStringExtra("id_quest");

        initCalendar();
        downloadDates();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnApply:
                uploadContract();
                break;
        }
    }
    public void initCalendar(){
        //Now add the following code to your activity’s class file to get the calendar functioning
        calendarView = (CalendarPickerView) findViewById(R.id.calendar_view);

        //getting current
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        calendarView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(dates);

        //Using the following code we can give actions while user is selecting/unselecting a date
        //action while clicking on a date
        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //Toast.makeText(getApplicationContext(),"Selected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDateUnselected(Date date) {
                //Toast.makeText(getApplicationContext(),"UnSelected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void downloadDates(){
        HandlerDB handler = new HandlerDB("download_date.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_quest", id_quest);
        handler.execute(input);
        handler.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    for(int i=0; i< dbResult.size();i++){
                        String myStrDate =  dbResult.get(i).get("date");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date = format.parse(myStrDate);
                            dates.add(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(PageCheckActivity.this, log, Toast.LENGTH_SHORT).show();
                }
                initCalendar();
            }
        });

    }
    public void uploadContract(){
        ArrayList<String> selectedDates = new ArrayList<>();
        for (int i = 0; i< calendarView.getSelectedDates().size(); i++){
            //here you can fetch all dates
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            String phpDate = sdFormat.format(calendarView.getSelectedDates().get(i));
            selectedDates.add(phpDate);
        }

        String json_dates = new Gson().toJson(selectedDates);

        HandlerDB handler = new HandlerDB("upload_contract.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_quest", id_quest);
        input.put("id_agent", id_user);
        input.put("json_dates", json_dates);
        handler.execute(input);
        handler.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("success")){
                    Toast.makeText(PageCheckActivity.this, "의뢰에 지원했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PageCheckActivity.this, log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
