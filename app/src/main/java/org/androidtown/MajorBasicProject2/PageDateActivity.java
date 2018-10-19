package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Date;

public class PageDateActivity extends AppCompatActivity implements View.OnClickListener{
    CalendarPickerView calendar_view;
    Button btnClearDates, btnSaveDates, btnPassDates;
    TextView tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_date);

        btnClearDates = (Button) findViewById(R.id.btnClearDates);
        btnSaveDates = (Button) findViewById(R.id.btnSaveDates);
        btnPassDates = (Button) findViewById(R.id.btnPassDates);
        btnClearDates.setOnClickListener(this);
        btnSaveDates.setOnClickListener(this);
        btnPassDates.setOnClickListener(this);

        tvType = (TextView) findViewById(R.id.tvDateType);

        setStage(getIntent().getStringExtra("stage"));
        initCalendar(getIntent().getStringExtra("type"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClearDates:
                clearDates();
                break;
            case R.id.btnSaveDates:
                saveDates();
                break;
            case R.id.btnPassDates:
                passDates();
                break;
        }
    }
    public void setStage(String stage){
        if(stage.equals("init")){
            btnSaveDates.setVisibility(View.GONE);
            btnPassDates.setVisibility(View.VISIBLE);
        } else if(stage.equals("edit")){
            btnSaveDates.setVisibility(View.VISIBLE);
            btnPassDates.setVisibility(View.GONE);
        }
    }
    public void initCalendar(String type){
        //Now add the following code to your activity’s class file to get the calendar functioning
        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);

        //getting current
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        //add one year to calendar from todays date
        if(type.equals("일일 의뢰")||type.equals("시간제 근무")) {
            tvType.setText("원하시는 모집일을 모두 선택하세요");
            calendar_view.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        } else if(type.equals("정규적인 고용")) {
            tvType.setText("모집기간의 시작과 끝을 선택하세요");
            calendar_view.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        } else if(type.equals("조건 계약")){
            tvType.setText("계약기간을 설정하세요");
            calendar_view.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        }

        //Using the following code we can give actions while user is selecting/unselecting a date
        //action while clicking on a date
        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
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

    public void clearDates(){
        //Following code is clear all selected dates while clicking on the button.
        calendar_view.clearHighlightedDates();
    }
    public void saveDates(){

    }
    public void passDates(){
        ArrayList<String> date = new ArrayList<>();
        for (int i = 0; i< calendar_view.getSelectedDates().size();i++){
            //here you can fetch all dates
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
            String tmpDate = sdFormat.format(calendar_view.getSelectedDates().get(i));
            String phpDate = tmpDate.substring(0, 4) + "-" + tmpDate.substring(4, 6) + "-" + tmpDate.substring(6, 8);
            date.add(phpDate);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
