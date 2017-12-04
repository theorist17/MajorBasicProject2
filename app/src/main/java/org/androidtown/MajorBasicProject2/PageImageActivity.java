package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PageImageActivity extends AppCompatActivity implements View.OnClickListener {

    String id_image;

    ImageView ivImage;
    EditText etCaption;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_image);

        ivImage = (ImageView)findViewById(R.id.ivImage);
        etCaption = (EditText)findViewById(R.id.etImageCaption);
        btnRegister = (Button)findViewById(R.id.btnImageRegister);

        btnRegister.setOnClickListener(this);

        Intent intent = getIntent();
        id_image = intent.getStringExtra("id_image");
        HandlerImage download_image = new HandlerImage();
        download_image.execute(id_image);
        download_image.setDbResponse(new HandlerImage.DBResponse() {
            @Override
            public void onResepones(Bitmap bmpResult) {
                ivImage.setImageBitmap(bmpResult);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnImageRegister:
                HandlerDB update_image = new HandlerDB("update_image.php");
                HashMap<String,String> input = new HashMap<>();
                input.put("caption", etCaption.getText().toString());
                input.put("id_image", id_image);
                update_image.execute(input);
                update_image.setDbResponse(new HandlerDB.DBResponse() {
                    @Override
                    public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                        if(log.equals("success"))
                            Toast.makeText(PageImageActivity.this, "캡션을 달았습니다.", Toast.LENGTH_SHORT).show();
                         else
                            Toast.makeText(PageImageActivity.this, log, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
        }
    }
}
