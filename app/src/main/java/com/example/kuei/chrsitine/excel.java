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

public class excel extends AppCompatActivity {

    private TextView begin,end;
    private Button output;
    private DatePickerDialog.OnDateSetListener  mDateSetListener;
    private DatePickerDialog.OnDateSetListener  nDateSetListener;
    private static final String TAG = "excel";
    private int dt1=0,dt2=0,cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);

        begin = findViewById(R.id.output_date1);
        end = findViewById(R.id.output_date2);
        output = findViewById(R.id.output_excel);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setDate();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        excel.this,
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
                dt1 = year*10000 + month*100 + day;
                begin.setText(date);
            }
        };


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setDate();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        excel.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        nDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet (DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy/mm/dd: "+ year + "/" + month + "/" + day);

                String date = year + "/" + month + "/" + day;
                dt2 = year*10000 + month*100 + day;
                end.setText(date);
            }
        };

        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dt1!=0&&dt2!=0){
                    cm = 4;
                    Intent intent = new Intent();
                    intent.setClass(excel.this,Database.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("confirm", cm);
                    bundle.putInt("date1", dt1);
                    bundle.putInt("date2", dt2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(dt1==0){
                    Toast toast = Toast.makeText(excel.this,"請選擇起始日期", Toast.LENGTH_LONG);
                    toast.show();//如果長時間不消失提示框，再來這邊修改
                }else if(dt2==0){
                    Toast toast = Toast.makeText(excel.this,"請選擇結束日期", Toast.LENGTH_LONG);
                    toast.show();//如果長時間不消失提示框，再來這邊修改
                }
            }


        });


    }
}