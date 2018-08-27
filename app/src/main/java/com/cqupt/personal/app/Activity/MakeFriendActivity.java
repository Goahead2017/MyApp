package com.cqupt.personal.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVLiveQuery;
import com.avos.avoscloud.AVLiveQueryEventHandler;
import com.avos.avoscloud.AVLiveQuerySubscribeCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.Chat.ChatActivity;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;

public class MakeFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView headIcon;
    private TextView signature;
    private TextView account;
    private Button makeFriend;
    private Button sendMessage;
    private String url;
    private String name;
    private String text;
    private String flag;


    private AVObject mFriend;
    private AVQuery<AVObject> doingQuery;
    private AVLiveQuery doingLiveQuery;

    private AVObject mFriend2;
    private AVQuery<AVObject> doingQuery2;
    private AVLiveQuery doingLiveQuery2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_friend);

        InvisibleBar.initStatusBar(getWindow());
        //显示页面即配置相关信息
        Intent intent = getIntent();
        url = intent.getStringExtra("headIcon");
        name = intent.getStringExtra("account");
        text = intent.getStringExtra("signature");
        flag = intent.getStringExtra("flag");

        startSubscribe();
//        startSubscribe2();
        init();

        Glide.with(getApplicationContext()).load(Uri.parse(url)).asBitmap().into(headIcon);
        if(text != null){
            signature.setText(text);
        }
        account.setText(name);

    }

    private void startSubscribe() {
        doingQuery = new AVQuery<>("makeFriend");
        doingQuery.whereEqualTo("state", "true");
        //开启订阅
        doingLiveQuery = AVLiveQuery.initWithQuery(doingQuery);
        doingLiveQuery.setEventHandler(new AVLiveQueryEventHandler() {
            @Override
            public void onObjectCreated(AVObject avObject) {
                // avObject 即为新添加的数据
                if(StaticData.avObject == null || !StaticData.avObject.get("from").equals(avObject.get("from"))) {
                    if(AVUser.getCurrentUser().getUsername().equals(avObject.get("from"))) {
                        Toast.makeText(getApplicationContext(), "请求发送成功", Toast.LENGTH_SHORT).show();
                    }
                        StaticData.friendFragment.makeFriend(avObject);
                }else {
                    if(AVUser.getCurrentUser().getUsername().equals(avObject.get("from")))
                    Toast.makeText(getApplicationContext(), "请不要重复发送好友请求", Toast.LENGTH_SHORT).show();
                }
                Log.d("make", "onObjectCreated: " + "我执行了");
            }
        });
        doingLiveQuery.subscribeInBackground(new AVLiveQuerySubscribeCallback() {
            @Override
            public void done(AVException e) {
                if (null == e) {
                    // 订阅成功
                }else {
                    Log.d("make", "done: " + e.toString());
                }
            }
        });
    }

    private void startSubscribe2() {
        doingQuery2 = new AVQuery<>("sendMessage");
        doingQuery2.whereEqualTo("state", "true");
        //开启订阅
        doingLiveQuery2 = AVLiveQuery.initWithQuery(doingQuery2);
        doingLiveQuery2.setEventHandler(new AVLiveQueryEventHandler() {
            @Override
            public void onObjectCreated(AVObject avObject) {
                // avObject 即为新添加的数据
                StaticData.messageFragment.sendMessage(avObject);
                Log.d("send", "onObjectCreated: " + "我执行了");
            }
        });
        doingLiveQuery.subscribeInBackground(new AVLiveQuerySubscribeCallback() {
            @Override
            public void done(AVException e) {
                if (null == e) {
                    // 订阅成功
                }else {
                    Log.d("make", "done: " + e.toString());
                }
            }
        });
    }

    private void init() {
        headIcon = findViewById(R.id.profile_image);
        signature = findViewById(R.id.tv2);
        account = findViewById(R.id.tv1);
        makeFriend = findViewById(R.id.make_friend);
        if(flag.equals("false")) {
            makeFriend.setOnClickListener(this);
        }else {
            makeFriend.setVisibility(View.GONE);
        }
        sendMessage = findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.make_friend:
                mFriend = new AVObject("makeFriend");
                mFriend.put("from", AVUser.getCurrentUser().getUsername());
                mFriend.put("to",name);
                mFriend.put("headIcon", url);
                mFriend.put("state","true");
                mFriend.put("flag","false");
                mFriend.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            // 保存成功
                        }else {
                            Log.d("make", "done: " + e.toString());
                        }
                    }
                });
                break;
            case R.id.send_message:
                Intent intent = new Intent(MakeFriendActivity.this, ChatActivity.class);
                intent.putExtra("fromHeadIcon",String.valueOf(AVUser.getCurrentUser().get("headIcon")));
                intent.putExtra("fromAccount",String.valueOf(AVUser.getCurrentUser().getUsername()));
                intent.putExtra("toHeadIcon",url);
                intent.putExtra("toAccount",name);
                startActivity(intent);
//                mFriend2 = new AVObject("sendMessage");
//                mFriend2.put("fromAccount", AVUser.getCurrentUser().getUsername());
//                mFriend2.put("fromHeadIcon", AVUser.getCurrentUser().get("headIcon"));
//                mFriend2.put("toAccount",name);
//                mFriend2.put("toHeadIcon", url);
//                mFriend2.put("state","true");
//                mFriend2.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(AVException e) {
//                        if (null == e) {
//                            // 保存成功
//                        }else {
//                            Log.d("make", "done: " + e.toString());
//                        }
//                    }
//                });
                break;
        }
    }
}
