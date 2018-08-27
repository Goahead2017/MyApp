package com.cqupt.personal.app.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.Activity.NewFriendActivity;
import com.cqupt.personal.app.Chat.ChatMessage;
import com.cqupt.personal.app.Chat.ChatModel;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.RecycleItemTouchHelper2;
import com.cqupt.personal.app.StaticData;

import java.util.List;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageHolder> implements RecycleItemTouchHelper2.ItemTouchHelperCallback2 {

    private List<String> mData;
    private List<String> url;

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private ItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(ItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public MyMessageAdapter(List<String> data, List<String> url){
        this.mData = data;
        this.url = url;
    }

    @Override
    public MyMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.my_message_item,parent,false);
        MyMessageHolder holder = new MyMessageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyMessageHolder holder, final int pos) {
        holder.account.setText(mData.get(pos));
        Glide.with(StaticData.messageFragment).load(url.get(pos)).asBitmap().into(holder.headIcon);
        if(ChatMessage.chatMessageRecord.size() != 0){
            ChatModel obj = (ChatModel) ChatMessage.chatMessageRecord.get(ChatMessage.chatMessageRecord.size() - 1).object;
            holder.message.setText(obj.getContent());
        }

        if(mOnItemClickListener != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void onItemDelete(int position){
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mData.size()-position);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
//        Collections.swap(mData,fromPosition,toPosition);
//        notifyItemMoved(fromPosition,toPosition);
    }

}

class MyMessageHolder extends RecyclerView.ViewHolder{

    public ImageView headIcon;
    public TextView account;
    public TextView message;

    MyMessageHolder(View view){
        super(view);
        headIcon = view.findViewById(R.id.profile_image);
        account = view.findViewById(R.id.text_item);
        message = view.findViewById(R.id.message);
    }
}