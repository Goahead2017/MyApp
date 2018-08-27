package com.cqupt.personal.app.DIYView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.cqupt.personal.app.Activity.MakeFriendActivity;
import com.cqupt.personal.app.Fragment.FriendFragment;
import com.cqupt.personal.app.R;

import java.util.List;

/**
 * 添加好友时的对话框
 */

public class MakeFriendDialog extends Dialog {

    private Context context;

    protected Button positive;
    private Button negative;
    private EditText et;
    private String text;
    private TextView tv;
    private FriendFragment friendFragment;

    public MakeFriendDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MakeFriendDialog(@NonNull Context context, int themeResId, String text, FriendFragment friendFragment) {
        super(context, themeResId);
        this.context = context;
        this.text = text;
        this.friendFragment = friendFragment;
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
               String account = et.getText().toString();
                if(account.length() == 0){
                    Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
                }else {
                    AVQuery<AVObject> avQuery = new AVQuery<>("TodoFolder");
                    avQuery.whereEqualTo("account",account);
                    avQuery.findInBackground(new FindCallback<AVObject>() {
                      @Override
                      public void done(List<AVObject> list, AVException e) {
                        if(e == null && list.size() != 0) {
                            Log.d("makeFriend", "done: " + list.toString() + "\n" + list.get(0).get("account") + "\n"
                                    + list.get(0).get("headIcon") + "\n" + list.get(0).get("signature"));
                            Intent intent = new Intent(friendFragment.getActivity(), MakeFriendActivity.class);
                            intent.putExtra("account",String.valueOf(list.get(0).get("account")));
                            intent.putExtra("headIcon",String.valueOf(list.get(0).get("headIcon")));
                            intent.putExtra("signature",String.valueOf(list.get(0).get("signature")));
                            intent.putExtra("flag","false");
                            friendFragment.getActivity().startActivity(intent);
                        }else if(e == null){
                            Toast.makeText(context,"找不到该用户，请重新输入",Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("makeFriend", "done: " + e.toString());
                        }
                      }
                    });
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