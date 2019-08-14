package com.example.kuei.chrsitine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class search extends AppCompatActivity {
    private Button read,all,button_total;
    private DatePickerDialog.OnDateSetListener  mDateSetListener;
    private TextView datetext,text_total;
    private static final String TAG = "search";
    private int cm,dt=0,total_money,push;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        datetext = findViewById(R.id.text_date);
        read = findViewById(R.id.button_read);
        all = findViewById(R.id.button_all);
        button_total = findViewById(R.id.button_total);
        text_total = findViewById(R.id.text_total);

        if(push==1){
            Intent it2 = getIntent();
            Bundle bd2 = it2.getExtras();

            total_money = bd2.getInt("total_money");
            text_total.setText("成功");//String.valueOf(total_money)
            push = 0;
        }




        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setDate();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        search.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet (DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy/mm/dd: "+ year + "/" + month + "/" + day);

                String date = year + "/" + month + "/" + day;
                dt = year*10000 + month*100 + day;
                datetext.setText(date);
            }
        };


        all.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                cm = 5;
                Intent intent = new Intent();
                intent.setClass(search.this,Database.class);
                Bundle bundle = new Bundle();
                bundle.putInt("confirm", cm);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dt!=0){
                    cm = 0;
                    Intent intent = new Intent();
                    intent.setClass(search.this,Database.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("confirm", cm);
                    bundle.putInt("date", dt);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(dt==0){
                    Toast toast = Toast.makeText(search.this,"請選擇初始日期", Toast.LENGTH_LONG);
                    toast.show();//如果長時間不消失提示框，再來這邊修改
                }

                //setContentView(R.layout.activity_database);
            }
        });


        button_total.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cm = 6;
                push = 1;
                Intent intent = new Intent();
                intent.setClass(search.this, Database.class);
                Bundle bundle = new Bundle();
                bundle.putInt("confirm", cm);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bundle bd2 = data.getExtras();
        total_money = bd2.getInt("total_money");
        push = bd2.getInt("push");
        text_total.setText(String.valueOf(total_money));
        push = 0;
    }
}
