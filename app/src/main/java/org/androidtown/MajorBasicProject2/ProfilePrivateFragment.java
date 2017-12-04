package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ProfilePrivateFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    Button btnUserSet;
    EditText etUserName, etUserEmail, etUserPassword, etUserPasswordCheck, etUserCell, etUserId, etUserBirth, etUserGender;
    LinearLayout llHidden1, llHidden2;
    boolean isModeSetting;
    ProfileFragment parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_private, container, false);
        btnUserSet = view.findViewById(R.id.btnUserSet);
        etUserName = view.findViewById(R.id.etUserName);
        etUserEmail = view.findViewById(R.id.etUserEmail);
        etUserCell = view.findViewById(R.id.etUserCell);

        etUserPassword = view.findViewById(R.id.etUserPassword);
        etUserPasswordCheck = view.findViewById(R.id.etUserPasswordCheck);

        etUserId = view.findViewById(R.id.etUserId);
        etUserBirth = view.findViewById(R.id.etUserBirth);
        etUserGender = view.findViewById(R.id.etUserGender);

        llHidden1 = view.findViewById(R.id.llHidden1);
        llHidden2 = view.findViewById(R.id.llHidden2);

        btnUserSet.setOnClickListener(this);

        isModeSetting = false;
        parent = ((ProfileFragment)getParentFragment());
        if(parent.isMyProfile)
            btnUserSet.setVisibility(View.VISIBLE);

        downloadPrivate();

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUserSet:
                if(isModeSetting)
                    uploadPrivate();
                else
                    setPrivate();
                break;
        }
    }
    public void downloadPrivate(){
        HandlerDB download_private = new HandlerDB("download_private.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("id_user", parent.id_target);
        download_private.execute(input);
        download_private.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    HashMap<String, String> dbPrivate =  dbResult.get(0);
                    updatePrivate(dbPrivate);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void updatePrivate(HashMap<String, String> dbPrivate){
        etUserName.setText(dbPrivate.get("name"));
        etUserEmail.setText(dbPrivate.get("email"));
        etUserCell.setText(dbPrivate.get("cell"));
        etUserPassword.setText(dbPrivate.get("password"));
        etUserPasswordCheck.setText(dbPrivate.get("password"));
        etUserId.setText(dbPrivate.get("id"));
        etUserBirth.setText(dbPrivate.get("birth"));
        etUserGender.setText(dbPrivate.get("gender"));
    }
    public void setPrivate(){
        isModeSetting = true;

        btnUserSet.setText("완료");

        llHidden1.setVisibility(View.VISIBLE);
        llHidden2.setVisibility(View.VISIBLE);

        etUserCell.setEnabled(true);
        etUserId.setEnabled(true);
        etUserBirth.setEnabled(true);
        etUserGender.setEnabled(true);
    }
    public void hidePrivate(){
        isModeSetting = false;

        btnUserSet.setText("설정");

        llHidden1.setVisibility(View.GONE);
        llHidden2.setVisibility(View.GONE);

        etUserCell.setEnabled(false);
        etUserId.setEnabled(false);
        etUserBirth.setEnabled(false);
        etUserGender.setEnabled(false);
    }
    public void uploadPrivate(){
        if(!etUserPassword.getText().toString().equals(etUserPasswordCheck.getText().toString())) {
            Toast.makeText(getActivity(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        HandlerDB upload_private = new HandlerDB("upload_private.php");
        HashMap<String, String> input = new HashMap<>();
        input.put("id_user", parent.id_target);
        input.put("id", etUserId.getText().toString());
        input.put("cell", etUserCell.getText().toString());
        input.put("password", etUserPassword.getText().toString());
        input.put("birth", etUserBirth.getText().toString());
        input.put("gender", etUserGender.getText().toString());
        upload_private.execute(input);
        upload_private.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("success")){
                    Toast.makeText(getActivity(), "회원님의 정보를 업데이트 했습니다", Toast.LENGTH_SHORT).show();
                    downloadPrivate();
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
        hidePrivate();
    }
}
