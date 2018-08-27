package com.cqupt.personal.app.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;

import java.util.List;

/**
 * 注册界面
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et1;
    private EditText et2;
    private EditText et3;

    private Button button;

    private ImageView cat;
    private TextView text;
    private Toolbar toolbar;

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InvisibleBar.initStatusBar(getWindow());

        init();
        setAnim();

    }

    private void init() {
        iv = findViewById(R.id.iv);
        Glide.with(getApplicationContext()).load(Uri.parse("http://p5mi59sy0.bkt.clouddn.com/greensky.png")).into(iv);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);

        //让第一个EditText首先获取焦点
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.requestFocus();
        et1.requestFocusFromTouch();

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        cat = findViewById(R.id.cat);
        text = findViewById(R.id.text);
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

    }

    private void setAnim() {
        //设置缩放和透明度动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cat, "scaleX", 0, (float) 1.0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cat, "scaleY", 0, (float) 1.0);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(cat, "alpha", 0, (float) 1.0);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2, animator3);
        set.setDuration(1000);

        //设置物理动画
        SpringAnimation springAnimation = new SpringAnimation(cat, DynamicAnimation.X);
        //初始速度
        springAnimation.setStartVelocity(2000);
        SpringForce springForce = new SpringForce();
        //弹性阻尼，反弹次数
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        //生硬度，恢复成未拉伸状态所需的时间
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        springForce.setFinalPosition(cat.getX());
        //开始动画
        springAnimation.setSpring(springForce);
        //对动画开始进行监听
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (velocity == 2000) {
                    set.start();
                }
            }
        });
        springAnimation.start();
        //对动画结束进行监听
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                cat.setImageResource(R.drawable.cat2);
                text.setVisibility(View.VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "famous.ttf");
                text.setTypeface(typeface);
            }
        });
    }

    @Override
    public void onClick(View view) {
        final String account;
        final String password;
        String email;
        switch (view.getId()) {
                case R.id.button:
                    account = et1.getText().toString();
                    password = et2.getText().toString();
                    email = et3.getText().toString();
                    if (account.length() == 0 || password.length() == 0 || email.length() == 0) {
                        Toast.makeText(getApplicationContext(), "请先把信息填写完整", Toast.LENGTH_SHORT).show();
                    } else {
                        AVUser user = new AVUser();// 新建 AVUser 对象实例
                        user.setUsername(account);// 设置用户名
                        user.setPassword(password);// 设置密码
                        user.put("headIcon","http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
                        user.put("signature", "良药苦口利于病，忠言逆耳利于行");
                        user.setEmail(email);// 设置邮箱
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    // 注册成功
                                    StaticData.todoFolder.put("account", account);// 设置用户名
                                    StaticData.todoFolder.put("headIcon","http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
                                    StaticData.todoFolder.put("signature", "良药苦口利于病，忠言逆耳利于行");
                                    StaticData.todoFolder.saveInBackground();// 保存到服务端
                                    Toast.makeText(getApplicationContext(),"注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // 失败的原因可能有多种，常见的是用户名已经存在。
                                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("register", "done: " + e.toString());
                                }
                            }
                        });
                    }
                    break;
                }
        }
}
