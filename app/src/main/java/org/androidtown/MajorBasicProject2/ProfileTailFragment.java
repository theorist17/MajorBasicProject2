package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;


public class ProfileTailFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    Button btnHelp, btnPref, btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tail, container, false);

        btnHelp = view.findViewById(R.id.btnTailHelp);
        btnPref = view.findViewById(R.id.btnTailHelp);
        btnLogout = view.findViewById(R.id.btnTailLogout);

        btnLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTailLogout:
                Toast.makeText(getActivity(), "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                onClickLogout();
                break;
        }
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(getActivity(), LoginSignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
    }

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }
}
