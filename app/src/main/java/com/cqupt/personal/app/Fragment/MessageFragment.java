package com.cqupt.personal.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.cqupt.personal.app.Adapter.MyMessageAdapter;
import com.cqupt.personal.app.Bean.SendMessageDataBean;
import com.cqupt.personal.app.CallBack.SendMessageCallback;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.RecycleItemTouchHelper2;
import com.cqupt.personal.app.StaticData;

import java.util.ArrayList;

/**
 * 消息中心界面
 */

public class MessageFragment extends Fragment implements SendMessageCallback{

    private RecyclerView mRecyclerView;
    private MyMessageAdapter mAdapter;
    private SendMessageDataBean dataBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        StaticData.messageFragment = this;
        mRecyclerView = view.findViewById(R.id.recycler);
        init();

        return view;
    }

    private void init() {
        dataBean = new SendMessageDataBean();
        dataBean.setAccount(new ArrayList<String>());
        dataBean.setHeadIcon(new ArrayList<String>());
        //首先判断表中是否有数据，有数据的话就先同步显示
        if (AVUser.getCurrentUser().get("MessageAccount") != null) {
            for (int i = 0; i < StaticData.parseObjects.size(); i++) {
                dataBean.setAccount(AVUser.getCurrentUser().getList("MessageAccount"));
                dataBean.setHeadIcon(AVUser.getCurrentUser().getList("MessageHeadIcon"));
                }
            }
            Log.d("send", "init: 1" + dataBean.getAccount().toString() + "\n" );
        //如果dataBean中有数据则显示RecycleView
        if(dataBean.getAccount().size() != 0) {
            mAdapter = new MyMessageAdapter(dataBean.getAccount(),dataBean.getHeadIcon());
            mRecyclerView.setAdapter(mAdapter);
            ItemTouchHelper.Callback callback = new RecycleItemTouchHelper2(mAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter.setOnItemClickListener(new MyMessageAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            });
        }
    }

    @Override
    public void sendMessage(AVObject avObject) {
        //再判断是否有增加数据,并去除重复
        if(avObject != null) {
            if(dataBean.getAccount().size() != 0) {
                int i = 0;
                while (!dataBean.getAccount().get(i).equals(avObject.get("toAccount")) && i < dataBean.getAccount().size()) {
                    i++;
                }
                if (i >= dataBean.getAccount().size()) {
                    dataBean.getAccount().add(String.valueOf(avObject.get("toAccount")));
                    dataBean.getHeadIcon().add(String.valueOf(avObject.get("toHeadIcon")));
                }
            }else {
                dataBean.getAccount().add(String.valueOf(avObject.get("toAccount")));
                dataBean.getHeadIcon().add(String.valueOf(avObject.get("toHeadIcon")));
            }
            AVUser.getCurrentUser().put("MessageAccount",dataBean.getAccount());
            AVUser.getCurrentUser().put("MessageHeadIcon",dataBean.getHeadIcon());
            AVUser.getCurrentUser().saveInBackground();
            //如果dataBean中有数据则显示RecycleView
            if(dataBean.getAccount().size() != 0) {
                mAdapter = new MyMessageAdapter(dataBean.getAccount(),dataBean.getHeadIcon());
                mRecyclerView.setAdapter(mAdapter);
                ItemTouchHelper.Callback callback = new RecycleItemTouchHelper2(mAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(mRecyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter.setOnItemClickListener(new MyMessageAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }
        }
    }
}
