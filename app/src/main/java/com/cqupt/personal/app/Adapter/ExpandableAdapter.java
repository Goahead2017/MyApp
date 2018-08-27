package com.cqupt.personal.app.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;

/**
 * 个人中心菜单栏的数据适配器
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private String[] parentStrings;
    private String[][] childStrings;

    public ExpandableAdapter(String[] parentStrings, String[][] childStrings) {
        this.parentStrings = parentStrings;
        this.childStrings = childStrings;
    }

    //获取分组的个数
    @Override
    public int getGroupCount() {
        return parentStrings.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int i) {
        return childStrings[i].length;
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int i) {
        return parentStrings[i];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int i, int i1) {
        return childStrings[i][i1];
    }

    //获取指定分组的ID
    @Override
    public long getGroupId(int i) {
        return i;
    }

    //获取子选项的ID
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    //分组和子选项是否持有稳定的ID，即底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //获取显示指定分组的视图
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = view.findViewById(R.id.parent_text);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.tvTitle.setText(parentStrings[i]);
        if(StaticData.blue){
            groupViewHolder.tvTitle.setTextColor(view.getResources().getColor(R.color.colorPrimary));
        }else if(StaticData.white){
            groupViewHolder.tvTitle.setTextColor(Color.WHITE);
        }else if(StaticData.blank){
            groupViewHolder.tvTitle.setTextColor(Color.BLACK);
        }else {
            groupViewHolder.tvTitle.setTextColor(view.getResources().getColor(R.color.blueSky));
        }
        return view;
    }

    //获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = view.findViewById(R.id.child_text);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.tvTitle.setText(childStrings[i][i1]);
        if(StaticData.blue){
            childViewHolder.tvTitle.setTextColor(view.getResources().getColor(R.color.colorPrimary));
        }else if(StaticData.white){
            childViewHolder.tvTitle.setTextColor(Color.WHITE);
        }else if(StaticData.blank){
            childViewHolder.tvTitle.setTextColor(Color.BLACK);
        }else {
            childViewHolder.tvTitle.setTextColor(view.getResources().getColor(R.color.blueSky));
        }
        return view;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }
}
