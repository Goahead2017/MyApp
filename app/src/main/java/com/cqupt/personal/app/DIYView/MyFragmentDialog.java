package com.cqupt.personal.app.DIYView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.cqupt.personal.app.Fragment.PersonalFragment;
import com.cqupt.personal.app.R;

/**
 * 更改个性签名是时的对话框
 */

public class MyFragmentDialog extends Dialog {

    private Context context;

    protected Button positive;
    private Button negative;
    private EditText et;
    private String text;
    private TextView tv;
    private PersonalFragment personalFragment;

    public MyFragmentDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MyFragmentDialog(@NonNull Context context, int themeResId, String text, PersonalFragment personalFragment) {
        super(context, themeResId);
        this.context = context;
        this.text = text;
        this.personalFragment = personalFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        et = view.findViewById(R.id.et);
        tv = view.findViewById(R.id.tv);
        tv.setText(text);
        positive = view.findViewById(R.id.button1);
        negative = view.findViewById(R.id.button2);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String signature = et.getText().toString();
                if(signature.length() == 0){
                    Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
                }else {
                    personalFragment.changSignature(signature);
                    dismiss();
                }
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(view);
    }
}