package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginJoinActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etId, etPw, etPwChk, etEmail;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        etPwChk = (EditText) findViewById(R.id.etPwChk);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnJoin:
                btJoin();
        }
    }

    public void btJoin(){
        String sId= etId.getText().toString();
        String sPw = etPw.getText().toString();
        String sPwChk = etPwChk.getText().toString();
        String sEmail = etEmail.getText().toString();

        /* 패스워드 확인이 정상적으로 됨 */
        if(sPw.equals(sPwChk)){
            HandlerDB join = new HandlerDB("join.php");
            HashMap<String,String> input = new HashMap<>();
            input.put("email", sEmail);
            input.put("username", sId);
            input.put("password", sPw);
            join.execute(input);
            join.setDbResponse(new HandlerDB.DBResponse() {
                @Override
                public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                    //error 반환
                    if(log.substring(0, 7).equals("success")) {
                        Toast.makeText(LoginJoinActivity.this, "성공적으로 가입되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(log.substring(0, 5).equals("error"))
                        Toast.makeText(LoginJoinActivity.this, log, Toast.LENGTH_SHORT).show();

                }
            });
        /* 패스워드 확인이 불일치 함 */
        } else {
            Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
        }


    }
}
