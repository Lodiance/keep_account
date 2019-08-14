package com.example.kuei.chrsitine;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class Dialog_number extends AppCompatDialogFragment {

    private EditText editText;
    private Dialog_number.Dialog_number_Listener listener;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_number, null);

        builder.setView(view);
        builder.setTitle("輸入資料");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String data1 = editText.getText().toString();
                if(data1.length()!=0 && data1!=null){
                    listener.applyText(data1);
                }


                //String data1 = editText.getText().toString().trim();
                //listener.applyText(data1);
            }
        });

        editText = view.findViewById(R.id.dialog_data1);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (Dialog_number.Dialog_number_Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必須輸入內容");
        }
    }

    public interface Dialog_number_Listener {
        void applyText(String data1);

    }
}
