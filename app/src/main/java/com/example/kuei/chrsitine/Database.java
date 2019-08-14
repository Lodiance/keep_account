package com.example.kuei.chrsitine;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Database extends AppCompatActivity {

    public MyDBHelper DH = null;
    public SQLiteDatabase db;
    public ListView LV1;
    private int dt,dt1,dt2,property=0;
    private String io;
    private String coin;
    private String kd;
    private String nt;
    private int confirm=0,push;
    private String id_text;
    public TextView t_id,t_date,t_io,t_money,t_kind,t_note,total;
    private Context context;
    private ProgressDialog progressDialog; //這個可以快速顯示一個Progress





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        //Bundle bundle = getIntent().getExtras();
        confirm = bundle.getInt("confirm");
        if(confirm==3||confirm==2){
            id_text = bundle.getString("id");
            dt = bundle.getInt("date");
        }else if(confirm==4){
            dt1 = bundle.getInt("date1");
            dt2 = bundle.getInt("date2");
        }else{
            dt = bundle.getInt("date");
        }
        io = bundle.getString("io");
        coin = bundle.getString("money");
        kd = bundle.getString("kind");
        nt = bundle.getString("note");


        DH = new MyDBHelper(this);
        db = DH.getWritableDatabase();//載入時就要先執行讀取資料
        LV1 = findViewById(R.id.list_item);//讀取元件

        //select();//開啟就先載入原本的資料
        if(confirm==1){
            add(dt,io,coin,kd,nt);
            finish();
        } else if (confirm==2){
            del(id_text);
            finish();
        }else if(confirm==3){
            update(id_text,dt,io,coin,kd,nt);
            finish();
        }else if(confirm==4){
            output(dt1,dt2);
        }else if(confirm==0){
            select();//開啟就先載入原本的資料
            search(dt);
        }else if(confirm==5){
            search();
        }else if(confirm==6){
            total();
            push=1;
            Intent it2 = new Intent();
            it2.setClass(Database.this,search.class);
            Bundle bt = new Bundle();
            bt.putInt("total_money",property);
            bt.putInt("push",push);
            it2.putExtras(bt);
            Database.this.setResult(RESULT_OK,it2);
            Database.this.finish();
        }

        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //parent = LV1;
                //view = R.layout.list_text;


                t_id = view.findViewById(R.id.list_text1);
                t_date = view.findViewById(R.id.list_text2);
                t_io = view.findViewById(R.id.list_text3);
                t_money = view.findViewById(R.id.list_text4);
                t_kind = view.findViewById(R.id.list_text5);
                t_note = view.findViewById(R.id.list_text6);
                //TextView tst = findViewById(R.id.test_text);//測試用
                //tst.setText(t_id.getText());//測試用

                id_text = t_id.getText().toString();
                dt = Integer.parseInt(t_date.getText().toString());
                io = t_io.getText().toString();
                coin = t_money.getText().toString();
                kd = t_kind.getText().toString();
                nt = t_note.getText().toString();

                Intent it = new Intent();
                it.setClass(Database.this,upwrite.class);
                Bundle bd = new Bundle();

                bd.putString("id",id_text);
                bd.putInt("date", dt);
                bd.putString("io", io);
                bd.putString("money", coin);
                bd.putString("kind", kd);
                bd.putString("note", nt);
                bd.putInt("confirm",confirm);
                it.putExtras(bd);
                startActivity(it);




            }
        });

    }



    //輸出excel
    public void output(int date1,int date2) {
        String dte1 = String.valueOf(date1);
        String dte2 = String.valueOf(date2);
        Cursor cursor = db.rawQuery("SELECT * FROM TB2018 WHERE (_date BETWEEN ? AND ?)",new String[]{dte1,dte2});
        //query("TB2018", new String[]{"_id","_date","_io","_money","_kind","_note"}, null,null,null,null,null);
        List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        cursor.moveToFirst();//創建資料集後移動到第一筆資料
        //叫出資料庫的資料
        for(int i=0;i < cursor.getCount();i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_date", cursor.getString(1));
            item.put("_io", cursor.getString(2));
            item.put("_money", cursor.getString(3));
            item.put("_kind", cursor.getString(4));
            item.put("_note", cursor.getString(5));
            items.add(item);//新增
            cursor.moveToNext();//移動到下一筆資料
        }
        SimpleAdapter SA = new SimpleAdapter(this,items, R.layout.list_text,new String[]{"_id","_date","_io","_money","_kind","_note"},new int[]{R.id.list_text1,R.id.list_text2,R.id.list_text3,R.id.list_text4,R.id.list_text5,R.id.list_text6});
        LV1.setAdapter(SA);

        //輸出function

        /*file = new File(getSDPath() + "/Bill");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/bill.xls", new String[]{"_id","_date","_io","_money","_kind","_note"});
        ExcelUtils.writeObjListToExcel(items, getSDPath() + "/Bill/bill.xls", this);*/



       ExportToCSV(cursor,"test.csv");
       File file = new File("/storage/emulated/0/test.csv");
       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.putExtra(Intent.EXTRA_SUBJECT,"記帳資料");
       intent.putExtra("body","test - 自動傳送sending");
       intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); //添加附件，附件为file对象
        if (file.getName().endsWith(".gz")) {
            intent.setType("application/x-gzip"); //如果是gz使用gzip的mime
        } else if (file.getName().endsWith(".txt")) {
            intent.setType("text/plain"); //纯文本则用text/plain的mime
        } else {
            intent.setType("application/octet-stream"); //其他的均使用流当做二进制数据来发送
        }
        startActivity(intent); //调用系统的mail客户端进行发送

    }

    //sqlite to CSV
    public void ExportToCSV(Cursor c, String fileName) {

        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;
        File sdCardDir = Environment.getExternalStorageDirectory();
        File saveFile = new File(sdCardDir, fileName);
        try {

            rowCount = c.getCount();
            colCount = c.getColumnCount();
            fw = new FileWriter(saveFile);
            bfw = new BufferedWriter(fw);
            bfw.write(0xFEFF);
            if (rowCount > 0) {
                c.moveToFirst();
                // 写入表头
                for (int i = 0; i < colCount; i++) {
                    if (i != colCount - 1)
                        bfw.write(c.getColumnName(i) + ',');
                    else
                        bfw.write(c.getColumnName(i));
                }
                // 写好表头后换行
                bfw.newLine();
                // 写入数据
                for (int i = 0; i < rowCount; i++) {
                    c.moveToPosition(i);
                    // Toast.makeText(mContext, "正在导出第"+(i+1)+"条",
                    // Toast.LENGTH_SHORT).show();
                    Log.v("导出数据", "正在导出第" + (i + 1) + "条");
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1)
                            bfw.write(c.getString(j) + ',');
                        else
                            bfw.write(c.getString(j));
                    }
                    // 写好每条记录后换行
                    bfw.newLine();
                }
            }
            // 将缓存数据写入文件
            bfw.flush();
            // 释放缓存
            bfw.close();
            // Toast.makeText(mContext, "导出完毕！", Toast.LENGTH_SHORT).show();
            Log.v("导出数据", "导出完毕！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            c.close();
        }
    }

    /*//取得SD卡存在與否
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    //製作目錄在SD卡
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }*/

    //搜尋資料
    public void search(int date) {
        String dte = String.valueOf(date);
        Cursor cursor = db.query("TB2018",new String[]{"_id","_date","_io","_money","_kind","_note"},"_date=?",new String[]{dte},null,null,null,null);
        //query("TB2018", new String[]{"_id","_date","_io","_money","_kind","_note"}, null,null,null,null,null);
        List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        cursor.moveToFirst();//創建資料集後移動到第一筆資料
        //叫出資料庫的資料
        for(int i=0;i < cursor.getCount();i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_date", cursor.getString(1));
            item.put("_io", cursor.getString(2));
            item.put("_money", cursor.getString(3));
            item.put("_kind", cursor.getString(4));
            item.put("_note", cursor.getString(5));
            items.add(item);//新增
            cursor.moveToNext();//移動到下一筆資料
        }
        SimpleAdapter SA = new SimpleAdapter(this,items, R.layout.list_text,new String[]{"_id","_date","_io","_money","_kind","_note"},new int[]{R.id.list_text1,R.id.list_text2,R.id.list_text3,R.id.list_text4,R.id.list_text5,R.id.list_text6});
        LV1.setAdapter(SA);
    }

    public void search() {
        //cursor物件是資料表型態，操作cursor物件可以得到資料表內容。
        Cursor cursor = db.query("TB2018",new String[]{"_id","_date","_io","_money","_kind","_note"},null,null,null,null,null);
        //query("TB2018", new String[]{"_id","_date","_io","_money","_kind","_note"}, null,null,null,null,null);
        List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        cursor.moveToFirst();//創建資料集後移動到第一筆資料
        //叫出資料庫的資料
        for(int i=0;i < cursor.getCount();i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_date", cursor.getString(1));
            item.put("_io", cursor.getString(2));
            item.put("_money", cursor.getString(3));
            item.put("_kind", cursor.getString(4));
            item.put("_note", cursor.getString(5));
            items.add(item);//新增
            cursor.moveToNext();//移動到下一筆資料
        }
        SimpleAdapter SA = new SimpleAdapter(this,items, R.layout.list_text,new String[]{"_id","_date","_io","_money","_kind","_note"},new int[]{R.id.list_text1,R.id.list_text2,R.id.list_text3,R.id.list_text4,R.id.list_text5,R.id.list_text6});
        LV1.setAdapter(SA);
    }


    //更新資料
    public void update(String id,int date,String io,String money,String kind,String note){
        ContentValues values = new ContentValues();
        values.put("_date", date);//載入資料date
        values.put("_io", io.toString());//載入資料io
        values.put("_money", money.toString());//載入資料money
        values.put("_kind", kind.toString());//載入資料kind
        values.put("_note",note.toString());//載入資料note
        db.update("TB2018",values,"_id="+id,null);
        select();
    }

    //刪除資料
    public void del(String id) {
        db.delete("TB2018", "_id="+id,null);
        select();
    }

    //新增資料
    public void add(int date,String io,String money,String kind,String note) {
        ContentValues values = new ContentValues();
        values.put("_date", date);//載入資料date
        values.put("_io", io.toString());//載入資料io
        values.put("_money", money.toString());//載入資料money
        values.put("_kind", kind.toString());//載入資料kind
        values.put("_note",note.toString());//載入資料note
        db.insert("TB2018", null, values);//寫入資料
        select();
    }

    //選取資料
    public void select() {
        //查詢資料庫並載入
        Cursor cursor = db.query("TB2018", new String[]{"_id","_date","_io","_money","_kind","_note"}, null,null,null,null,null);
        //query  (String table, String[] columns, String selection, String[] selectionArgs(條件),
        //String groupBy, String having, String orderBy, String limit)
        List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        cursor.moveToFirst();//創建資料集後移動到第一筆資料
        //叫出資料庫的資料
        for(int i=0;i < cursor.getCount();i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_date", cursor.getString(1));
            item.put("_io", cursor.getString(2));
            item.put("_money", cursor.getString(3));
            item.put("_kind", cursor.getString(4));
            item.put("_note", cursor.getString(5));
            items.add(item);//新增
            cursor.moveToNext();//移動到下一筆資料
        }
        SimpleAdapter SA = new SimpleAdapter(this,items, R.layout.list_text,new String[]{"_id","_date","_io","_money","_kind","_note"},new int[]{R.id.list_text1,R.id.list_text2,R.id.list_text3,R.id.list_text4,R.id.list_text5,R.id.list_text6});
        LV1.setAdapter(SA);
    }

    //計算總金額
    public void total(){
        Cursor cursor = db.query("TB2018", new String[]{"_id","_date","_io","_money","_kind","_note"}, null,null,null,null,null);
        //query  (String table, String[] columns, String selection, String[] selectionArgs(條件),
        //String groupBy, String having, String orderBy, String limit)
        List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        cursor.moveToFirst();//創建資料集後移動到第一筆資料
        //叫出資料庫的資料
        for(int i=0;i < cursor.getCount();i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_date", cursor.getString(1));
            item.put("_io", cursor.getString(2));
            item.put("_money", cursor.getString(3));
            item.put("_kind", cursor.getString(4));
            item.put("_note", cursor.getString(5));
            if(item.get("_io").equals("支")){
                property = property - Integer.valueOf(item.get("_money").toString());
            }else if(item.get("_io").equals("收")){
                property = property + Integer.valueOf(item.get("_money").toString());
            }
            items.add(item);//新增
            cursor.moveToNext();//移動到下一筆資料

        }
        SimpleAdapter SA = new SimpleAdapter(this,items, R.layout.list_text,new String[]{"_id","_date","_io","_money","_kind","_note"},new int[]{R.id.list_text1,R.id.list_text2,R.id.list_text3,R.id.list_text4,R.id.list_text5,R.id.list_text6});
        LV1.setAdapter(SA);
    }


}
