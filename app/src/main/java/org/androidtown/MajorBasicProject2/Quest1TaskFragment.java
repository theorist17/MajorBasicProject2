package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Quest1TaskFragment extends Fragment implements View.OnClickListener {
    final int FIELD = 1;

    EditText etField;
    Button btnField;

    TaskFragment taskFragment;

    ArrayList<String> id_task_list;
    String id_field;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rd1_task, container, false);

        etField = view.findViewById(R.id.etField);
        btnField = view.findViewById(R.id.btnField);

        btnField.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskFragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mode", "multiple");
        bundle.putInt("num", 5);
        taskFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.questTaskContainer, taskFragment);
        transaction.commit();
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnField:
                intent = new Intent(getActivity(), PageFieldActivity.class);
                startActivityForResult(intent, FIELD);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FIELD:
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra("select");
                    HandlerDB download_task = new HandlerDB("download_field_title_to_id.php");
                    HashMap<String, String> input = new HashMap<>();
                    input.put("name", name);
                    download_task.execute(input);
                    download_task.setDbResponse(new HandlerDB.DBResponse() {
                        @Override
                        public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                            if(log.equals("json")) {
                                id_field = dbResult.get(0).get("id_field");
                                etField.setText(dbResult.get(0).get("name"));
                            } else {
                                Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(id_task_list == null)
            return;
        ((RegisterActivity)getActivity()).id_task_list.clear();
        for(int i = 0; i< id_task_list.size(); i++)
            ((RegisterActivity)getActivity()).id_task_list.add(id_task_list.get(i).toString());
        if (id_field==null) id_field = "0";
        ((RegisterActivity)getActivity()).id_field = id_field;
    }


}