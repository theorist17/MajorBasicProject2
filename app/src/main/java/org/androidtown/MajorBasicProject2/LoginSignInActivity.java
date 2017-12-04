package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class LoginSignInActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etId, etPw;
    Button btnJoin, btnLogin;

    GlobalApplication app;

    private SessionCallback callback;
    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        app = (GlobalApplication) getApplicationContext();
        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //카카오톡 API를 활용하기 위해 디버그 키 해쉬 알아내는 방법
        Log.w("KEY HASH : ", getKeyHash(this));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnJoin:
                redirectJoinActivity();
                break;
            case R.id.btnLogin:
                HandlerDB login = new HandlerDB("login.php");
                HashMap<String,String> input = new HashMap<>();
                input.put("username", etId.getText().toString());
                input.put("password", etPw.getText().toString());
                login.execute(input);
                login.setDbResponse(new HandlerDB.DBResponse() {
                    @Override
                    public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                        if(dbResult != null)
                            redirectMainActivity(dbResult.get(0).get("id_user")); // 로그인 성공시 MainActivity로
                        else
                            Toast.makeText(LoginSignInActivity.this, log, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            // 세션 연결성공 시 redirectSignupActivity() 호출해서 SignupActivity로 넘김
            redirectSignupActivity();
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때 로그인화면을 다시 불러옴
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, LoginSignUpActivity.class); //세션 연결 성공 시
        startActivity(intent);
        finish();
    }
    protected void redirectJoinActivity(){
        final Intent intent = new Intent(LoginSignInActivity.this, LoginJoinActivity.class);
        startActivity(intent);
    }
    private void redirectMainActivity(String id_user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id_user", id_user);
        startActivity(intent);
        finish();
    }
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("KEY HASH : ", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}


