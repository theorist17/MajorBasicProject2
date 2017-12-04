package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PageTaskActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText etTitle, etSummary, etBody;
    TextView etCount;
    ListView lvTag; AdapterTagListView adapter;
    EditText etValue, etTag;
    Button btnTagger, btnRegister;

    String mode, id_task, id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_task_new);

        etTitle = (EditText) findViewById(R.id.etPageTaskTitle);
        etSummary = (EditText) findViewById(R.id.etPageTaskSummary);
        etBody = (EditText) findViewById(R.id.etPageTaskBody);
        etCount = (TextView) findViewById(R.id.tvPageTaskCount);
        etValue = (EditText) findViewById(R.id.etPageTaskValue);
        etTag = (EditText) findViewById(R.id.etPageTaskTag);
        btnRegister = (Button) findViewById(R.id.btnPageTaskRegister);
        btnTagger = (Button) findViewById(R.id.btnPageTaskTagger);

        lvTag = (ListView) findViewById(R.id.lvTag);
        adapter = new AdapterTagListView();
        lvTag.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvTag);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        id_user = intent.getStringExtra("id_user");
        if(mode.equals("new")) {
            String title = intent.getStringExtra("title");
            etTitle.setText(title);
        } else if(mode.equals("edit")){
            id_task = intent.getStringExtra("id_task");
            selectTask(id_task);
        }

        btnTagger.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPageTaskRegister:
                if(mode.equals("new"))
                     uploadTask();
                 else if(mode.equals("edit"))
                     updateTask();
                break;
            case R.id.btnPageTaskTagger:
                uploadTag();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void uploadTask(){
        HandlerDB upload_task = new HandlerDB("upload_task.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("title", etTitle.getText().toString());
        input.put("summary", etSummary.getText().toString());
        input.put("body", etBody.getText().toString());
        upload_task.execute(input);
        upload_task.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    Intent intent = new Intent();
                    intent.putExtra("id_task", dbResult.get(0).get("id_task"));
                    intent.putExtra("title", etTitle.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else
                    Toast.makeText(PageTaskActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateTask(){
        HandlerDB update_task = new HandlerDB("update_task.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("id_task", id_task);
        input.put("title", etTitle.getText().toString());
        input.put("summary", etSummary.getText().toString());
        input.put("body", etBody.getText().toString());
        update_task.execute(input);
        update_task.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("success")){
                    finish();
                } else
                    Toast.makeText(PageTaskActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void uploadTag(){
        HandlerDB upload_tag = new HandlerDB("upload_tag.php");
        HashMap<String, String> input = new HashMap<>();
        double value = Double.parseDouble(etValue.getText().toString());
        String strVal = String.valueOf(value*10);
        input.put("id_task", id_task);
        input.put("id_user", id_user);
        input.put("value", strVal);
        input.put("tag", etTag.getText().toString());
        upload_tag.execute(input);
        upload_tag.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("success")) {
                    downloadTag(id_task);
                } else
                    Toast.makeText(PageTaskActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void selectTask(String id_task){
        HandlerDB select_task = new HandlerDB("select_task.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("id_task", id_task);
        select_task.execute(input);
        select_task.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    updateTask(dbResult.get(0));
                } else
                    Toast.makeText(PageTaskActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateTask(HashMap<String,String> dbTask){
        etTitle.setText(dbTask.get("title"));
        etSummary.setText(dbTask.get("summary"));
        etBody.setText(dbTask.get("body"));
        etCount.setText(dbTask.get("count"));

        downloadTag(dbTask.get("id_task"));
    }
    public void downloadTag(String id_task){
        HandlerDB download_tag = new HandlerDB("download_tag.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("id_task", id_task);
        download_tag.execute(input);
        download_tag.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    updateTag(dbResult);
                } else
                    Toast.makeText(PageTaskActivity.this, log, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateTag(ArrayList<HashMap<String,String >> db){
        adapter.clearAll();
        for (int i=0; i<db.size();i++){
            double value = Double.parseDouble(db.get(i).get("value"));
            String strValue = Double.toString(value/10);
            adapter.addItem(strValue, db.get(i).get("tag"));
        }
        lvTag.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvTag);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
