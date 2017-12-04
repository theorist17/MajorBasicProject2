package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class TaskFragment extends Fragment implements View.OnClickListener{

    //setup
    String mode;
    int num;

    //view
    EditText etTaskQuery;
    Button btnTaskFind, btnTaskRegister;

    //list
    ListView lvTask;
    AdapterTaskListView adapterTask;
    LinearLayout llSelectedView;

    //data
    ArrayList<String> alSelectedList = new ArrayList<>();
    OnSendListener onSendDataListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onSendDataListener = (OnSendListener) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m2_task, container, false);


        llSelectedView = view.findViewById(R.id.llSelected);
        etTaskQuery = view.findViewById(R.id.etTaskQuery);
        btnTaskFind = view.findViewById(R.id.btnTaskFind);
        btnTaskRegister = view.findViewById(R.id.btnTaskRegister);

        lvTask = view.findViewById(R.id.lvTask);
        adapterTask = new AdapterTaskListView() ;
        lvTask.setAdapter(adapterTask);

        btnTaskFind.setOnClickListener(this);
        btnTaskRegister.setOnClickListener(this);

        etTaskQuery.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { downloadTask(); }
            public void afterTextChanged(Editable s) {}
        });

        mode = this.getArguments().getString("mode");
        num = this.getArguments().getInt("num");

        if(mode.equals("unique")) {
            lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    ItemTask item = (ItemTask) parent.getItemAtPosition(position);

                    alSelectedList.add(item.getId_task());

                    etTaskQuery.setText("");
                    onSendDataListener.onSendData(alSelectedList);
                    alSelectedList.clear();
                }
            }) ;
        } else if(mode.equals("multiple")) {
            lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    ItemTask item = (ItemTask) parent.getItemAtPosition(position) ;

                    addSelectedView(item.getTitle());
                    alSelectedList.add(item.getId_task());

                    etTaskQuery.setText("");
                    onSendDataListener.onSendData(alSelectedList);
                }
            }) ;
        }

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTaskFind:
                downloadTask();
                break;
            case R.id.btnTaskRegister:
                Intent intent = new Intent(getActivity(), PageTaskActivity.class);
                intent.putExtra("mode", "new");
                intent.putExtra("title", etTaskQuery.getText().toString());
                startActivityForResult(intent, 0);
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    etTaskQuery.setText("");
                    adapterTask.clearAll();

                    String id_task = data.getStringExtra("id_task");
                    String title = data.getStringExtra("title");
                    alSelectedList.add(id_task);
                    addSelectedView(title);
                }
                break;
        }
    }
    public void downloadTask(){
        HandlerDB download_task = new HandlerDB("download_task.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("input", etTaskQuery.getText().toString());
        download_task.execute(input);
        download_task.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json"))
                    updateTaskList(dbResult);
                else
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void updateTaskList(ArrayList<HashMap<String,String>>loadedList){
        lvTask.setAdapter(adapterTask);
        adapterTask.clearAll();
        for(int i = 0; i < num &&i < loadedList.size(); i++) {
            Drawable drawable =  ContextCompat.getDrawable(getActivity(), R.drawable.ic_assignment);
            adapterTask.addItem(drawable, loadedList.get(i).get("id_task"), loadedList.get(i).get("title"), loadedList.get(i).get("summary"), loadedList.get(i).get("count"));
        }
        setListViewHeightBasedOnChildren(lvTask);
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
    public void addSelectedView(String title){
        final TextView tvDynamicTask = new TextView(getActivity());
        tvDynamicTask.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tvDynamicTask.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvDynamicTask.setPadding(20, 10, 10, 10);
        tvDynamicTask.setTextColor(Color.parseColor("#FF7200"));
        tvDynamicTask.setTextSize(17);
        tvDynamicTask.setText(title);
        tvDynamicTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((ViewGroup) v.getParent()).indexOfChild(v);
                alSelectedList.remove(index);
                llSelectedView.removeView(v);

                onSendDataListener.onSendData(alSelectedList);
            }
        });
        llSelectedView.addView(tvDynamicTask);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnSendListener {
        void onSendData(ArrayList<String> selected);
    }
}
