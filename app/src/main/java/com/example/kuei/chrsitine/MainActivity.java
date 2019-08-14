package com.example.kuei.chrsitine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_show;
    private ImageButton btn_show;
    private Button search;
    private Button write;
    private Button excel;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_show = findViewById(R.id.date_label);

        //search jump page
        search = findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                openActivity();

            }
        });

        //plus button change words
        btn_show = findViewById(R.id.plus);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show.setText("測試成功");
                tv_show.setTextSize(24);
            }
        });

        //write jump page
        search = findViewById(R.id.button_write);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                openActivity();

            }
        });

        //excel jump page
        excel = findViewById(R.id.button_excel);
        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 3;
                openActivity();

            }
        });
    }

    public void openActivity() {
        if (i == 1) {
            Intent intent = new Intent(this, search.class);
            startActivity(intent);
        } else if (i == 2) {
            Intent intent = new Intent(this, write.class);
            startActivity(intent);
        } else if (i == 3) {
            Intent intent = new Intent(this, excel.class);
            startActivity(intent);
        }
    }
}
