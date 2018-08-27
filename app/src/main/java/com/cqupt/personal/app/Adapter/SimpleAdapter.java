package com.cqupt.personal.app.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.Activity.NewFriendActivity;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.RecycleItemTouchHelper;
import com.cqupt.personal.app.StaticData;

import java.util.Collections;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder> implements RecycleItemTouchHelper.ItemTouchHelperCallback {

    private List<String> mData;
    private List<String> url;
    private List<String> flag;
    private NewFriendActivity newFriendActivity;

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private ItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(ItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public SimpleAdapter(List<String> data, List<String> url, List<String> flag, NewFriendActivity newFriendActivity){
        this.mData = data;
        this.url = url;
        this.flag = flag;
        this.newFriendActivity = newFriendActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_single_textview,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        holder.account.setText(mData.get(pos));
        if(url.get(pos) != null) {
            Glide.with(newFriendActivity).load(url.get(pos)).asBitmap().into(holder.headIcon);
        }else {
            Glide.with(newFriendActivity).load(Uri.parse("http://p5mi59sy0.bkt.clouddn.com/picture2.jpg")).asBitmap().into(holder.headIcon);
        }

        if(mOnItemClickListener != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }

        if(flag.get(pos).equals("false")) {
            holder.agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag.set(pos,"true");
                    holder.agree.setVisibility(View.GONE);
                    holder.disagree.setVisibility(View.GONE);
                    String from = mData.get(pos);
                    String to = AVUser.getCurrentUser().getUsername();
                    StaticData.friendFragment.becomeFriend(from,to);
                    Toast.makeText(newFriendActivity, "已同意", Toast.LENGTH_SHORT).show();
                }
            });

            holder.disagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag.set(pos,"true");
                    holder.agree.setVisibility(View.GONE);
                    holder.disagree.setVisibility(View.GONE);
                    Toast.makeText(newFriendActivity, "已拒绝", Toast.LENGTH_SHORT).show();
                }
            });
            StaticData.parseObjects.get(pos).put("flag","true");
            StaticData.parseObjects.get(pos).saveInBackground();
        }else {
            holder.agree.setVisibility(View.GONE);
            holder.disagree.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

//    public void onItemDelete(int position){
//        mData.remove(position);
//        notifyItemRemoved(position);
//        notifyItemChanged(position,mData.size()-position);
//    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        Collections.swap(mData,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder{

    public ImageView headIcon;
    public TextView account;
    public Button agree;
    public Button disagree;

    MyViewHolder(View view){
        super(view);
        headIcon = view.findViewById(R.id.profile_image);
        account = view.findViewById(R.id.text_item);
        agree = view.findViewById(R.id.agree);
        disagree = view.findViewById(R.id.disagree);
    }
}