package org.androidtown.MajorBasicProject2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginSignUpActivity extends Activity  {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() { // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            }

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile.toString());

                HandlerDB kakao_login = new HandlerDB("kakao_login.php");
                HashMap<String, String> inputData = new HashMap<>();
                inputData.put("nickname", userProfile.getNickname());
                inputData.put("email", userProfile.getEmail());
                inputData.put("email_verified", userProfile.getEmailVerified()+"");
                inputData.put("thumnailImagePath", userProfile.getThumbnailImagePath());
                inputData.put("profileImagePath", userProfile.getProfileImagePath());
                kakao_login.execute(inputData);
                kakao_login.setDbResponse(new HandlerDB.DBResponse() {
                    @Override
                    public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                        if(dbResult != null)
                            redirectMainActivity(dbResult.get(0).get("id_user")); // 로그인 성공시 MainActivity로
                    }
                });
            }
        });
    }

    private void redirectMainActivity(String id_user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id_user", id_user);
        startActivity(intent);
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginSignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}