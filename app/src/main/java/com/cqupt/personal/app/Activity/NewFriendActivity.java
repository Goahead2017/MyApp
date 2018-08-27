package com.cqupt.personal.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.cqupt.personal.app.InvisibleBar;
import com.cqupt.personal.app.Bean.NewFriendDataBean;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.RecycleItemTouchHelper;
import com.cqupt.personal.app.Adapter.SimpleAdapter;
import com.cqupt.personal.app.StaticData;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private NewFriendDataBean dataBean;
    private AVQuery<AVObject> doingQuery;
    private NewFriendActivity newFriendActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        InvisibleBar.initStatusBar(getWindow());
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginTop(28);

        doingQuery = new AVQuery<>("makeFriend");
        doingQuery.whereEqualTo("state", "true");
        doingQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> parseObjects, AVException parseException) {
                // 符合查询条件的 Todo
                if(parseException == null && parseObjects.size() != 0) {
                    StaticData.parseObjects = parseObjects;
                    dataBean = new NewFriendDataBean();
                    dataBean.setUrl(new ArrayList<String>());
                    dataBean.setData(new ArrayList<String>());
                    dataBean.setFlag(new ArrayList<String>());
                    //首先判断表中是否有数据，有数据的话就先同步显示
                    if (StaticData.parseObjects != null) {
                        for (int i = 0; i < StaticData.parseObjects.size(); i++) {
                            if (i == 0) {
                                dataBean.getData().add(String.valueOf(StaticData.parseObjects.get(i).get("from")));
                                dataBean.getUrl().add(String.valueOf(StaticData.parseObjects.get(i).get("headIcon")));
                                dataBean.getFlag().add(String.valueOf(StaticData.parseObjects.get(i).get("flag")));
                            } else {
                                int j = 0;
                                while (j < dataBean.getData().size() && !dataBean.getData().get(j).equals(StaticData.parseObjects.get(i).get("from")) ) {
                                    j++;
                                }
                                if (j >= dataBean.getData().size()) {
                                    dataBean.getData().add(String.valueOf(StaticData.parseObjects.get(i).get("from")));
                                    dataBean.getUrl().add(String.valueOf(StaticData.parseObjects.get(i).get("headIcon")));
                                    dataBean.getFlag().add(String.valueOf(StaticData.parseObjects.get(i).get("flag")));
                                }
                            }
                        }
                        Log.d("make", "init: 1" + dataBean.getData().toString() + "\n" + String.valueOf(StaticData.parseObjects.get(0).get("from")));
                    }
                    //再判断是否有增加数据,并去除重复
                    if (StaticData.avObject != null) {
                            if (dataBean.getData().size() != 0) {
                                int i = 0;
                                while (!dataBean.getData().get(i).equals(StaticData.avObject.get("from")) && i < dataBean.getData().size()) {
                                    i++;
                                }
                                if (i >= dataBean.getData().size()) {
                                    dataBean.getData().add(String.valueOf(StaticData.avObject.get("from")));
                                    dataBean.getUrl().add(String.valueOf(StaticData.avObject.get("headIcon")));
                                    dataBean.getFlag().add(String.valueOf(StaticData.avObject.get("flag")));
                                }
                            } else {
                                dataBean.getData().add(String.valueOf(StaticData.avObject.get("from")));
                                dataBean.getUrl().add(String.valueOf(StaticData.avObject.get("headIcon")));
                                dataBean.getFlag().add(String.valueOf(StaticData.avObject.get("flag")));
                            }
                            AVUser.getCurrentUser().put("NewFriendDataBean", dataBean);
                            AVUser.getCurrentUser().saveInBackground();

                        }
                        Log.d("make", "init: 2" + dataBean.getData().toString());
                        //如果dataBean中有数据则显示RecycleView
                        if (dataBean.getData().size() != 0) {
                            mRecyclerView = findViewById(R.id.recycler_view);
                            mAdapter = new SimpleAdapter(dataBean.getData(), dataBean.getUrl(), dataBean.getFlag(), newFriendActivity);
                            mRecyclerView.setAdapter(mAdapter);
                            ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(mAdapter);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                            itemTouchHelper.attachToRecyclerView(mRecyclerView);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(newFriendActivity, LinearLayoutManager.VERTICAL, false);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                            mAdapter.setOnItemClickListener(new SimpleAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                }

                                @Override
                                public void onItemLongClick(View view, int position) {
                                    Toast.makeText(getApplicationContext(), "可以移动哦~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Log.d("make", "done: " + parseObjects.get(0).toString());
                    } else {
                        if (parseException != null)
                            Log.d("make", "done: " + parseException.toString());
                    }
            }
        });

    }
}
