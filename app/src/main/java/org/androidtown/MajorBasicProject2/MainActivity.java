package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements TaskFragment.OnSendListener {


    public String id_user;
    BottomBar bottomBar;

    MainSearchFragment searchFragment;
    TaskFragment taskFragment;
    MainContractFragment contractFragment;
    MainHistoryFragment historyFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        id_user = getIntent().getStringExtra("id_user");
        searchFragment = new MainSearchFragment();
        taskFragment = new TaskFragment();
        contractFragment = new MainContractFragment();
        historyFragment = new MainHistoryFragment();
        profileFragment = new ProfileFragment();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (tabId == R.id.tab_search) {
                    transaction.replace(R.id.mainContainer, searchFragment).commit();
                } else if (tabId == R.id.tab_task) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mode", "unique");
                    bundle.putInt("num", 10);
                    taskFragment.setArguments(bundle);
                    transaction.replace(R.id.mainContainer, taskFragment).commit();
                } else if (tabId == R.id.tab_contract) {
                    transaction.replace(R.id.mainContainer, contractFragment).commit();
                } else if (tabId == R.id.tab_history) {
                    transaction.replace(R.id.mainContainer, historyFragment).commit();
                } else if (tabId == R.id.tab_profile) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id_user", id_user);
                    bundle.putString("id_target", id_user);
                    bundle.putBoolean("isEditMode", true);
                    profileFragment.setArguments(bundle);
                    transaction.replace(R.id.mainContainer, profileFragment).commit();
                }
            }
        });
    }

    @Override
    public void onSendData(ArrayList<String> selected) {
        Intent intent = new Intent(MainActivity.this, PageTaskActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("id_task", selected.get(0));
        intent.putExtra("id_user", id_user);
        startActivity(intent);
    }
}
