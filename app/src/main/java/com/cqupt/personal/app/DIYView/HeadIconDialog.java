package com.cqupt.personal.app.DIYView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cqupt.personal.app.Fragment.PersonalFragment;
import com.cqupt.personal.app.R;

/**
 * 更换头像时的对话框
 */

public class HeadIconDialog extends Dialog {

    private Context context;
    private PersonalFragment personalFragment;
    private TextView tv1;
    private TextView tv2;

    public HeadIconDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public HeadIconDialog(@NonNull Context context, int themeResId, PersonalFragment personalFragment) {
        super(context, themeResId);
        this.context = context;
        this.personalFragment = personalFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_dialog, null);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personalFragment.choosePhoto();
                dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personalFragment.takePhoto();
                dismiss();
            }
        });
        setContentView(view);
    }
}