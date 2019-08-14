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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class write extends AppCompatActivity implements Dialog.DialogListener, Dialog_number.Dialog_number_Listener {


    private int num = 0;
    private int cm = 0;
    private int dt = 0;
    private String io;
    private String coin;
    private String kd;
    private String nt;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioButton income;
    private RadioButton outcome;
    private TextView datetext;
    private Button confirm;
    private Button cancel;
    private TextView money;
    private TextView kind;
    private TextView note;
    private static final String TAG = "search";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


        datetext = findViewById(R.id.text_date);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        money = findViewById(R.id.money);
        kind = findViewById(R.id.kind);
        note = findViewById(R.id.note);
        io = "";


        RadioGroup RDG = findViewById(R.id.RDG);
        RDG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.income:
                        io = "收";
                        break;
                    case R.id.outcome:
                        io = "支";
                        break;
                }
            }
        });


        //確認
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Database db1 = new Database();
                //db1.add(money.getText().toString(),kind.getText().toString());

                coin = money.getText().toString();
                nt = note.getText().toString();
                kd = kind.getText().toString();

                if (dt == 0) {
                    Toast toast = Toast.makeText(write.this, "請選擇日期", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (io.length()==0) {
                    Toast toast = Toast.makeText(write.this, "請選擇收支", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (coin == "" || coin == null || coin == "0") {
                    Toast toast = Toast.makeText(write.this, "請輸入金額", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (kd == "") {
                    Toast toast = Toast.makeText(write.this, "請輸入類別", Toast.LENGTH_SHORT);
                    toast.show();
                }
                cm = 1;

                if (dt != 0 && io.length() != 0 && io!=null && coin.length() != 0 && kd.length() != 0) {
                    Intent intent = new Intent();
                    intent.setClass(write.this, Database.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("date", dt);
                    bundle.putString("io", io);
                    bundle.putString("money", coin);
                    bundle.putString("kind", kd);
                    bundle.putString("note", nt);
                    bundle.putInt("confirm", cm);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }


            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //輸入記帳金額
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog_num();
            }
        });

        //輸入記帳類別
        kind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                num = 2;
            }
        });

        //輸入記帳備註
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                num = 3;
            }
        });

        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        write.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);

                String date = year + "/" + month + "/" + day;
                dt = year * 10000 + month * 100 + day;
                datetext.setText(date);
            }
        };
    }

    public void openDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void openDialog_num() {
        Dialog_number dialog_number = new Dialog_number();
        dialog_number.show(getSupportFragmentManager(), "dialog_number");
    }

    @Override
    public void applyTexts(String data) {
        if (num == 2) {
            kind.setText(data);
        } else if (num == 3) {
            note.setText(data);
        }
    }

    public void applyText(String data1) {
        money.setText(data1);
        //coin = money.getText().toString();
    }
}