package com.cqupt.personal.app.Chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVLiveQuery;
import com.avos.avoscloud.AVLiveQueryEventHandler;
import com.avos.avoscloud.AVLiveQuerySubscribeCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;
    private String fromHeadIcon;
    private String fromAccount;
    private String toHeadIcon;
    private String toAccount;

    private AVObject message;
    private AVQuery<AVObject> doingQuery;
    private AVLiveQuery doingLiveQuery;

//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        InvisibleBar.initStatusBar(getWindow());
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleMarginTop(28);
//        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        //接收对话双方的头像
        Intent intent = getIntent();
        fromHeadIcon = intent.getStringExtra("fromHeadIcon");
        fromAccount = intent.getStringExtra("fromAccount");
        toHeadIcon = intent.getStringExtra("toHeadIcon");
        toAccount = intent.getStringExtra("toAccount");

        startSubscribe();

        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setAdapter(adapter = new ChatAdapter());
        if(ChatMessage.chatMessageRecord != null){
            adapter.addAll(ChatMessage.chatMessageRecord);
        }
        et = findViewById(R.id.et);
        tvSend = findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        initData();
    }

    private void startSubscribe() {
        doingQuery = new AVQuery<>("chat");
        doingQuery.whereEqualTo("state", "true");
        //开启订阅
        doingLiveQuery = AVLiveQuery.initWithQuery(doingQuery);
        doingLiveQuery.setEventHandler(new AVLiveQueryEventHandler() {
            @Override
            public void onObjectCreated(AVObject avObject) {
                // avObject 即为新添加的数据
                if(AVUser.getCurrentUser().getUsername().equals(fromAccount)){
                    ChatModel model2 = new ChatModel();
                    model2.setContent(String.valueOf(avObject.get("text")));
                    model2.setIcon(fromHeadIcon);
                    ChatMessage.chatMessage.add(new ItemModel(ItemModel.CHAT_B, model2));
                    ChatMessage.chatMessageRecord.add(new ItemModel(ItemModel.CHAT_B, model2));
                }
                if(!AVUser.getCurrentUser().getUsername().equals(fromAccount)){
                    ChatModel model = new ChatModel();
                    model.setContent(String.valueOf(avObject.get("text")));
                    model.setIcon(toHeadIcon);
                    ChatMessage.chatMessage.add(new ItemModel(ItemModel.CHAT_A, model));
                    ChatMessage.chatMessageRecord.add(new ItemModel(ItemModel.CHAT_A, model));
                }
                adapter.addAll(ChatMessage.chatMessage);
                et.setText("");
                hideKeyBorad(et);
                ChatMessage.chatMessage = new ArrayList<>();
                Log.d("chat", "onObjectCreated: " + "我执行了" + "\n" + ChatMessage.chatMessage.toString());
            }
        });
        doingLiveQuery.subscribeInBackground(new AVLiveQuerySubscribeCallback() {
            @Override
            public void done(AVException e) {
                if (null == e) {
                    // 订阅成功
                }else {
                    Log.d("chat", "done: " + e.toString());
                }
            }
        });
    }

    private void initData() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(et.getText() != null) {
                    message = new AVObject("chat");
                    message.put("fromHeadIcon", fromHeadIcon);
                    message.put("fromAccount", fromAccount);
                    message.put("toHeadIcon", toHeadIcon);
                    message.put("toAccount", toAccount);
                    message.put("state", "true");
                    message.put("text", et.getText().toString());
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (null == e) {
                                // 保存成功
                            } else {
                                Log.d("make", "done: " + e.toString());
                            }
                        }
                    });
                }
            }
        });

    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

}
