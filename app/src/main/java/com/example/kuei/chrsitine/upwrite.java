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

import java.util.Calendar;


public class upwrite extends AppCompatActivity implements Dialog.DialogListener, Dialog_number.Dialog_number_Listener {


    private int num=0,cm,dt;
    private String io,coin,kd,nt,id_text;
    private DatePickerDialog.OnDateSetListener  mDateSetListener;
    private TextView datetext;
    private Button confirm,cancel,del;
    private TextView money,kind,note;
    private static final String TAG = "search";
    private RadioButton income,outcome;
    public RadioGroup RDG;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upwrite);

        Intent it = getIntent();
        Bundle bd = it.getExtras();
        cm = bd.getInt("confirm");
        id_text = bd.getString("id");
        dt = bd.getInt("date");
        io = bd.getString("io");
        coin = bd.getString("money");
        kd = bd.getString("kind");
        nt = bd.getString("note");



        datetext = findViewById(R.id.uw_text_date);
        confirm = findViewById(R.id.uw_confirm);
        cancel = findViewById(R.id.uw_cancel);
        money = findViewById(R.id.uw_money);
        kind = findViewById(R.id.uw_kind);
        note = findViewById(R.id.uw_note);
        income = findViewById(R.id.uw_income);
        outcome = findViewById(R.id.uw_outcome);
        del = findViewById(R.id.uw_del);

        datetext.setText(String.valueOf(dt));
        money.setText(coin);
        kind.setText(kd);
        note.setText(nt);

        RDG =findViewById(R.id.uw_RDG);
        RDG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.uw_income:
                        io = "收";
                        //Toast toast = Toast.makeText(upwrite.this,"income success!",Toast.LENGTH_LONG);
                        //toast.show();
                        break;
                    case R.id.uw_outcome:
                        io = "支";
                        //Toast toast1 = Toast.makeText(upwrite.this,"outcome success!",Toast.LENGTH_LONG);
                        //toast1.show();
                        break;
                }
            }
        });



        //刪除
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coin = money.getText().toString();
                kd = kind.getText().toString();
                nt = note.getText().toString();
                cm = 2;

                Intent it1 = new Intent();
                it1.setClass(upwrite.this,Database.class);
                Bundle bd1 = new Bundle();
                bd1.putString("id", id_text);
                bd1.putInt("date", dt);
                bd1.putString("io", io);
                bd1.putString("money", coin);
                bd1.putString("kind", kd);
                bd1.putString("note", nt);
                bd1.putInt("confirm",cm);
                it1.putExtras(bd1);
                startActivity(it1);
                finish();
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




                cm = 3;


                Intent intent = new Intent();
                intent.setClass(upwrite.this,Database.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id_text);
                bundle.putInt("date", dt);
                bundle.putString("io", io);
                bundle.putString("money", coin);
                bundle.putString("kind", kd);
                bundle.putString("note", nt);
                bundle.putInt("confirm",cm);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

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
                        upwrite.this,
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
                dt = year*10000+month*100+day;
                datetext.setText(date);
            }
        };

        check();

    }

    public void check() {
        if(io.equals("收")){
            income.setChecked(true);
        }else if (io.equals("支")){
            outcome.setChecked(true);
        }
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
        if(num==2) {
            kind.setText(data);
            kd = kind.getText().toString();
        }else if(num==3) {
            note.setText(data);
            nt = note.getText().toString();
        }
    }

    public void applyText(String data1) {
        money.setText(data1);
        coin = money.getText().toString();
    }
}