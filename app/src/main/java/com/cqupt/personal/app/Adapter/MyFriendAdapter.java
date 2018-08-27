package com.cqupt.personal.app.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqupt.personal.app.R;

import java.util.List;

public class MyFriendAdapter extends RecyclerView.Adapter<MyFriendViewHolder>{

    private List<String> account;
    private List<String> signature;
    private List<String> headIcon;
    private FragmentActivity activity;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public MyFriendAdapter(List<String> account, List<String> signature, List<String> headIcon, FragmentActivity activity){
        this.account = account;
        this.signature = signature;
        this.headIcon = headIcon;
        this.activity = activity;
    }

    @Override
    public MyFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.my_friend_item,parent,false);
        MyFriendViewHolder holder = new MyFriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyFriendViewHolder holder, final int position) {
        holder.account.setText(account.get(position));
        holder.signature.setText(signature.get(position));
        Glide.with(activity).load(headIcon.get(position)).asBitmap().into(holder.headIcon);

        if(mOnItemClickListener != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return account.size();
    }

}

class MyFriendViewHolder extends RecyclerView.ViewHolder{

    public ImageView headIcon;
    public TextView account;
    public TextView signature;

    MyFriendViewHolder(View view){
        super(view);
        headIcon = view.findViewById(R.id.profile_image);
        account = view.findViewById(R.id.account);
        signature = view.findViewById(R.id.signature);
    }
}