package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class PageQuestActivity extends AppCompatActivity implements View.OnClickListener{

    String id_user;
    String id_quest;

    ImageView ivUserCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_quest);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");
        id_quest = intent.getStringExtra("id_quest");

        ivUserCover = (ImageView) findViewById(R.id.ivUserCover);
        ivUserCover.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivUserCover:
                Intent intent = new Intent(this, PageProfileActivity.class);
                intent.putExtra("id_user", id_user);
                intent.putExtra("id_target", "17");
                startActivity(intent);
                break;
        }
    }

    public void selectQuest(){

    }
    public void writeQuest(){

    }
}
