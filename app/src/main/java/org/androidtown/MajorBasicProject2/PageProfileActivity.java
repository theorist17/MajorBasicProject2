package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class PageProfileActivity extends AppCompatActivity {

    public String id_user;
    public String id_target;

    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_profile);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");
        id_target = intent.getStringExtra("id_target");

        initFragment();
    }

    public void initFragment(){
        Bundle bundle = new Bundle();
        bundle.putString("id_user", id_user);
        bundle.putString("id_target", id_target);

        profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.pageProfileContainer, profileFragment);
        transaction.commit();
    }
}
