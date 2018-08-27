package com.cqupt.personal.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.cqupt.personal.app.DIYView.DrawView;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.DIYView.MyDialog;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;

/**
 * 登录界面
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et1;
    private EditText et2;
    private Button button;
    private DrawView drawView;
    private TextView register;
    private TextView correct;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InvisibleBar.initStatusBar(getWindow());
        init();

    }

    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginTop(38);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        //设置点击实现登录操作
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        drawView = findViewById(R.id.diy);

        //让第一个EditText首先获取焦点
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.requestFocus();
        et1.requestFocusFromTouch();

        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        correct = findViewById(R.id.correct);
        correct.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaticData.flag = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                final String account = et1.getText().toString();
                String password = et2.getText().toString();
                if(account.length() == 0 || password.length() == 0){
                    Toast.makeText(getApplicationContext(),"请先把信息填完整",Toast.LENGTH_SHORT).show();
                }else {
                    AVUser.logInInBackground(account, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("login", "done: " + e.toString());
                            }
                        }
                    });
                }
                break;
            case R.id.register:
                Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.correct:
                String text = "请输入邮箱地址";
                MyDialog myDialog = new MyDialog(this,R.style.Dialog,text);
                myDialog.show();
                Window dialogWindow = myDialog.getWindow();
                WindowManager m = this.getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
                p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.9
                dialogWindow.setAttributes(p);
                break;
        }
    }

}
