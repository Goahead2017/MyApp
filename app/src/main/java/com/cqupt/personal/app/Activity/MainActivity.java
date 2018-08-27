package com.cqupt.personal.app.Activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.CallBack.ChangeSkinCallback;
import com.cqupt.personal.app.Fragment.FriendFragment;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.Fragment.MessageFragment;
import com.cqupt.personal.app.Fragment.PersonalFragment;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;

/**
 * 主界面
 */

public class MainActivity extends AppCompatActivity implements ChangeSkinCallback {

    private BottomNavigationBar bar;
    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;
    private ImageView imageView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化头像
        if(AVUser.getCurrentUser().get("headIcon") == null) {
            AVUser.getCurrentUser().put("headIcon", "http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
            StaticData.todoFolder.put("headIcon","http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
            StaticData.todoFolder.saveInBackground();// 保存到服务端
        }
        Log.d("photo", "onCreateView: " + AVUser.getCurrentUser().get("headIcon"));

        //初始化个性签名
        if(AVUser.getCurrentUser().get("signature") == null) {
            AVUser.getCurrentUser().put("signature", "良药苦口利于病，忠言逆耳利于行");
            StaticData.todoFolder.put("signature","良药苦口利于病，忠言逆耳利于行");
            StaticData.todoFolder.saveInBackground();// 保存到服务端
        }

        context = this;
        InvisibleBar.initStatusBar(getWindow());
        init();
    }

    private void init() {
        imageView = findViewById(R.id.imageView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginTop(28);

        bar = findViewById(R.id.bottom_navigation_bar);
//        ShapeBadgeItem shapeBadgeItem = new ShapeBadgeItem()
//                .setShape(ShapeBadgeItem.SHAPE_OVAL)
//                .setShapeColorResource(R.color.colorAccent)
//                .setGravity(Gravity.TOP | Gravity.END)
//                .setHideOnSelect(false);
        bar.addItem(new BottomNavigationItem(R.drawable.cloud1, "好友"))
//                .addItem(new BottomNavigationItem(R.drawable.cloud3, "消息中心").setBadgeItem(shapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.cloud2, "个人中心"))
                .setActiveColor(R.color.blueSky)
                .setFirstSelectedPosition(0)
                .initialise();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear,new FriendFragment());
        fragmentTransaction.commit();

        bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            FragmentManager  manager = getSupportFragmentManager();
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        FragmentTransaction transaction1 = manager.beginTransaction();
                        transaction1.replace(R.id.linear,new FriendFragment()).commit();
                        break;
//                    case 1:
//                        FragmentTransaction transaction2 = manager.beginTransaction();
//                        transaction2.replace(R.id.linear, new MessageFragment()).commit();
//                        break;
                    case 1:
                        FragmentTransaction transaction3 = manager.beginTransaction();
                        transaction3.replace(R.id.linear, new PersonalFragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void changSkin(String imageUrl) {
        Glide.with(context).load(imageUrl).asBitmap().into(imageView);
        Log.d("skin", "changSkin: " + imageUrl);
    }
}
